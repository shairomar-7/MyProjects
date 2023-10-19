#include "tokens.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#define MAX_LENGTH 256 // max length of a line
char previousCommand[256]; // Global var represents the previous command

// Checks if the given command is "exit"
// returns 1 if not equal, and 0 if equal and prints Bye bye.
int isCmdExit(char * cmd) {
  int result = strcmp("exit", cmd);
  if (result == 0) printf("Bye bye.\n");
  return result;
}

// checks if the given command is "prev"
// returns 0 if not equal, and 1 if equal and prints the prev cmd
int isPrevCommand(char * cmd) {
  int result = strcmp("prev", cmd);
  if (result == 0) printf("%s\n", previousCommand);
  return result;
}

// executes the command; if successful, return 0, else return 1
int executeCommand(const char *const * tokens) {
  pid_t pid;
  pid = fork();
  int status,exitStatus;
  if (pid == 0) {
    exitStatus = execvp(tokens[0], tokens);
    if (exitStatus != NULL) {
      exit(1);
    }
  }
  else {
    wait(&status);
    if (!(WIFEXITED(status) && WEXITSTATUS(status) == 0)) {
      printf("%s: command not found\n", tokens[0]);
      return 1;
    }
  }
  return 0;
}

// Check if given cmd is "help"
// Return 0 and print the help menu if it is equal, and return other if not equal
int isHelpCommand(char * cmd) {
  int result = strcmp(cmd, "help");
  if (result == 0) {
    printf("We are here to help!\nAvailable build-in commands:\ncd [dir-path, ..] : This command should change the current working directory of the shell.\nsource [file-path] : Takes a filename as an argument and processes each line of the file as a command, including built-ins.\nprev : Prints the previous command line and executes it again.\nhelp : Explains all the built-in commands available in your shell\n");
  }
  return result;
}

// Determine if the given tokens contain the redirection symbol
// returns 0 for < for input redirection
// returns 1 for > output redirection
// returns -1 if neither!
int determineRedirection(const char * const * tokens) {
  int result = -1;
  int index = 0;
  while (tokens[index] != NULL) {
    if (strcmp(tokens[index], "<") == 0) {
     return 0; 
    }
    else if (strcmp(tokens[index], ">") == 0) {
      return 1;
    }
    index++;
  }
  return result;
}

// get number of commands to pipe
int numPipeCommands(const char * const * tokens) {
  int idx = 0;
  int numCommands = 1;
  while (tokens[idx] != NULL) {
    if (strcmp(tokens[idx], "|") == 0) {
      numCommands++;
    }
    idx++;
  }
  return numCommands;
}

// determines whether command is a pipe
int isPipe(const char * const * tokens) {
  int result = -1;
  int index = 0;
  while (tokens[index] != NULL) {
    if (strcmp(tokens[index], "|") == 0) {
        return 0;
    }
    index++;
  }
  return result;
}

// Helps the executePipe function by executing one single command by reading from the given input file descriptor,
// and writing to the given output file descriptor. This function is also given the double char pointer command, to pass to exec and execute the command. 
// We fork, if child then we set up the ends of the pipe and set them to either stdout for outFD or stdin for inFD, depending on their 
// given value. Then we execute, and if it fails, we exit with a status of 1
// If parent, then wait for the child to finish, and if the child exited with a status not 0, the print error to user regarding the given command. If child fails, then we return -1;
int executePipeHelper(int inFD, int outFD, const char *const* command) {
  pid_t pid;
  pid = fork();
  int status;
  int redirection = determineRedirection(command);
  if (pid == 0) {
    if (inFD != 0) {
      close(0);
      assert(dup(inFD) == 0);
      close(inFD);
    }
    if (outFD != 1) {
      close(1);
      assert(dup(outFD) == 1);
      close(outFD);
    }
    if (redirection == 0) { // check for input redirection
      if (executeRedirection(command, inFD) != 0) exit(1); 
      else exit(0); 
    }
    else if (redirection == 1) { // check for output redirection
      if (executeRedirection(command, outFD) !=0) exit(1);
      else exit(0);
    }
    else if (redirection == -1 && execvp(command[0], command) == -1) { // if no redirection, then execute the command with execvp
      exit(1);
    }
  }
  else {
   wait(&status);
    if (!(WIFEXITED(status) && WEXITSTATUS(status) == 0)) {
      printf("%s: command not found\n", command[0]);
      return -1;
    }
    return 0;
  }
}

//Function will execute the given tokens which contain a pipe symbol
//Get the number of commands seperated by the pipes, for each of those minus the last one,
//parse the current command, create a new pipe, call executePipeHelper with the previous pipe's input file descriptor
//And, the current pipe's output file descriptor and the currentcommand.
// close the current pipe's ouput file descriptor, and set inFD to the current pipe's input fd, AND KEEP LOOPING.
//If the last cmd contains a redirection symbol, call executeRedirection with either input or output fd of the current pipe, depending on the rediretion symbol. If it doesn't, then execute it with executeCommand.
//Returns 0 if success, -1 if fails
int executePipe(const char const * const * tokens) {
  int inFD = 0, pipe_fd[2], index, index2 = 0, status, redirection;// index2 represents the current location within tokens
  int cmdCount = numPipeCommands(tokens);
  char * currentCommand[20];
  pid_t pid;
  pid = fork();
  // for each command except the last one,
  // create a new pipe, call a helper to execute the current command with the given input and output file descriptors
  //then, close the write end of the pipe, and set the input fd to the the read end of the pipe and REPEAT!
  if (pid == 0) {
  for (index = 0; index < cmdCount - 1; ++index) {
    // Parse the tokens, store the current command:
    int i = 0; 
    while(tokens[index2] != NULL && strcmp(tokens[index2], "|") != 0) {// "ls", "-F" |
      currentCommand[i] = tokens[index2];
      i++;
      index2++;
    }
    index2++;// inc the index to skip over the | in the next iter
    currentCommand[i] = NULL;// set the last elt to NULL; this is for execv's sanity
    pipe(pipe_fd);
    if (executePipeHelper(inFD, pipe_fd[1], currentCommand) == -1) { // ls | nl -> currentcommand {ls ... NULL}
      close(pipe_fd[1]);
      exit(1);
    }
    close(pipe_fd[1]);
    inFD = pipe_fd[0];
  }
  if (inFD != 0) {
    close(0);
    assert(dup(inFD) == 0);
  }
  if (pipe_fd[1] == 1) {
    close(pipe_fd[1]);
  }
  int i = 0; // now, grab the last item! 
  while(tokens[index2] != NULL) {
    currentCommand[i] = tokens[index2];
    i++;
    index2++;
  }
  currentCommand[i] = NULL;
  redirection = determineRedirection(currentCommand);
  if (redirection == -1) executeCommand(currentCommand);
  else executeRedirection(currentCommand, redirection);
  close(pipe_fd[0]);
  close(inFD);
  exit(0);
  }
  else {
    wait(&status);
    if (!(WIFEXITED(status) && WEXITSTATUS(status) == 0)) {
      printf("Error performing redirection!\n");
      return -1;
    }
    return 0;
  }
}


// Executes redirection with the given tokens and the redirection type.
// Redirection type -> is it output or input redirection?
// Fork, close the given redirection type (ie, either 0 or 1).
// If redirection type 1, then open write only, create if does not exist, truncate if does exist (umask 0644)
// Then, execute the modifiedTokens (just parsed tokens without the > and file name) with execvp
// Return 0 on success, -1 on error.
int executeRedirection(const char * const * tokens, int redirectionType) {
  int index = 0;
  char * modifiedTokens[20];
  char * file;
  while (strcmp(tokens[index], ">") != 0 && strcmp(tokens[index], "<") != 0) {
    modifiedTokens[index] = tokens[index];
    index++;
  }
  modifiedTokens[index] = NULL;
  if (tokens[index + 1] != NULL) file = tokens[index + 1];
  else return -1; // no output file given!
  pid_t pid;
  pid = fork();
  int status;
  if (pid == 0) {
    if (close(redirectionType) == -1) {
      perror("Error closing std");
      exit(1);
    }
    int fd;
    if (redirectionType == 1) fd = open(file, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    else if (redirectionType == 0) fd = open(file, O_RDONLY);
    assert(fd == redirectionType); // make sure fd is redirectionType
    if (execvp(modifiedTokens[0], modifiedTokens) != NULL) {
      exit(1);
    }
  }
  else {
    wait(&status);
    if (!(WIFEXITED(status) && WEXITSTATUS(status) == 0)) {
      printf("Error performing redirection!\n");
      return -1;
    }
  }
  return 0;
}

// Opens the file with the given file path, if invalid, it prints a message to user, and returns -1.
// If path is valid, it will keep looping until there are no more lines(size 255) to be read, and we return 0.
// If one of the commands was exit or command d, then we would just return 1 and the program would end for real.
int executeSource(const char * filePath) {
  FILE * file;
  file = fopen(filePath, "r");
  if (file == NULL) {
    printf("Invalid file path given!\n");
    return -1;
  }
  else {
    char input[MAX_LENGTH];
    char str[MAX_LENGTH] = "source ";
    strcat(str, filePath);
    while (fgets(input, MAX_LENGTH, file)) {
      if (strncmp(str, input, strlen(str)) == 0) {
        printf("Error: cannot call same source command within source file.\n");
        return 0;
      }
      if (commandHelper1(input) == 1) return 1;
    }
    return 0;
  }
}

// changes the current working directory to the given dirPath
// return the value of chdir system call with the given dirPath
// If the dirPath is something like : "/omar/", then this is an invalid path for chdir, so point to the next letter!
int changeDirectory(const char * dirPath) {
  if (dirPath[0] == '/') return chdir(dirPath + 1);
  else return chdir(dirPath);
}

// Executes the previoys command, by creating new tokens and calling the executeCommand with new tokens.
// Make sure to free the tokens.
// If the previousCommand is not null, then print to the user an error message!
void executePrev(char * previousCommand) {
  if (previousCommand == NULL) printf("No previous command is available!\n");
  else {
    char ** previousTokens = get_tokens(previousCommand);
    executeCommand(previousTokens);
    free_tokens(previousTokens);
  }
}

// Determines which command to execute, and executes it!
// Returns 1 if exit or cmd-d, and main will end the while loop and say bye bye, OR if the command was source, and one of
// the commands in the file was "exit", then we exit for real, no joke.
// Returns 0 upon success.
int determineAndExecuteCmd(const char * const * tokens, char * currentCommand) {
  int redirection = determineRedirection(tokens);
  if (isCmdExit(tokens[0]) == 0) {
    free_tokens(tokens);
    return 1;
  }
  else if (isPipe(tokens) == 0) {
    executePipe(tokens);
  }
  else if (redirection != -1) {
    executeRedirection(tokens, redirection);
  }
  else if (strcmp("source", tokens[0]) == 0) {
    if (executeSource(tokens[1]) == 1) {
      free_tokens(tokens);
      return 1;
    }
  }
  else if (isPrevCommand(tokens[0]) == 0) {
    executePrev(previousCommand);
  }
  else if (strcmp("cd", tokens[0]) == 0) {
    if (changeDirectory(tokens[1]) == -1) printf("Error Occured with cd: make sure path is valid!\n");
  }
  else if (isHelpCommand(tokens[0]) == 0) {}
  else {
    if (strstr(currentCommand, "\n") != NULL) currentCommand[strlen(currentCommand) - 1] = '\0';
    if (executeCommand(tokens) == 0) strcpy(previousCommand, currentCommand);
  }
  return 0;
}


// Parses the given input into different commands (if seperated by ;)
// Keeps looping until there are no more commands to be read: 
// in the loop, get the tokens from the currentcommand, and call the determineAndExecuteCmd with the tokens and current command.
// If determineAndExecuteCmd returns 1, then we return 1 in order to exit the program
// Free the tokens at every iteration, and get the next command to be executed.
// Return 0 upon success
int commandHelper1(char input[]) {
  char * currentCommand;
  // get the first command seperated by ;
  currentCommand = strtok(input, ";");
  while(currentCommand != NULL && currentCommand[0] != '\n') {
    char ** tokens = get_tokens(currentCommand);
    assert(tokens != NULL);
    if (determineAndExecuteCmd(tokens, currentCommand) == 1) return 1;
    free_tokens(tokens);
    currentCommand = strtok(NULL, ";");
  }
  return 0;
}

// Main keeps running the shell until the user enters exit or cmd-d or the input read from stdin is null
int main(int argc, char **argv) {
  printf("Welcome to mini-shell.\n");
  while (1) {
    char input[MAX_LENGTH];
    printf("shell $ ");
    int resultFgets = fgets(input, MAX_LENGTH, stdin);
    if (resultFgets == NULL) {
      printf("\nBye bye.\n");
      return 0;
    }
    if (commandHelper1(input) == 1) break;
  }
  return 0;
}
