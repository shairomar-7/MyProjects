# Project 2: FTP Client
This is a client program for the File Transfer Protocol programmed in Python. FTP is a client-server-oriented protocol for uploading, downloading, and managing files. The server will listen for client connections and respond to their requests. The requests could be listing the files in a directory, making and deleting directories, uploading files from the client machine to the server, and downloading files from the server to the client machines.

## How to run
```./3700ftp [operation] [param1] [param2]```

```operation``` is required and represents one of ls, mkdir, rm, rmdir, cp, and mv.

```param1``` and ```param2``` are strings that either represent a path to a file on the local filesystem or a URL to a file or directory on an FTP server. For some operations (mv and cp), both params are required, where one represents the server URL, and the other represents a path to a local file/directory. For other operations (ls, mkdir, rm, rmdir), param1 is required and must represent the server URL. 

The server URL is of the format: ```ftp://[USER[:PASSWORD]@]HOST[:PORT]/PATH```, where HOST and PATH are required. If USER is not given, then PASSWORD is neglected, and the user logs into the server anonymously. The default PORT is 21. 

## High-Level Approach
At first glance, the project requirements were a bit overwhelming, so I approached each problem individually. Firstly, I wrote a script to successfully parse the command line arguments and identify invalid commands (for example, invalid operations) using the argparse library and urlib library for parsing URLs. Secondly, I wrote a function to establish a socket connection with the FTP server using the socket library. Once connected, I started sending and setting the FTP commands for "USER", "PASS", "TYPE", "STRU" and "MODE", and listening for the server's response. The next step was to try creating a directory on the server (mkdir), and deleting that empty directory (rmdir). Once these 2 simple operations worked, I moved them to a function that would process ```operation```, parsed from the command line arguments, with a switch statement. I then moved on to setting up my data channel and sending the "PASV" command to be able to list the files in a directory on the FTP server. Once I got that done, I implemented a helper function for the "cp" command, which copies a file from the FS to the FTP server or vice versa, depending on the order of the URLs. In the first case, we open the file as "rb" meaning read-only and in byte mode, and load the file's contents into a buffer using the file.read() method. I then send over my data through the data channel after sending the "STOR [path]" command to the server. In the second case, we open the data channel and listen for incoming data that will be stored in a buffer. Then, I create a file by opening it in "wb" mode, meaning write only, and in binary mode. I then write the buffer into the file using the file.write(buff) function. Finally, all that was left was "mv" and "rm". The "mv" command is the same exact thing as "cp" except it deletes the file on the source's end. So, I reused my "cp" helper function. After copying over the file, if the source is my file system, then I delete the file in the local file system using os.remove(filePath). If the source is the server, then I send the "DELE [path]" command to ask the server to delete the file with the corresponding path. Finally, my code ran perfectly locally, and I started refining it by documenting my work, organizing the code into small functions, and creating a class (FTPData) to store all of the required data, including host, port, username, password, control channel, data channel, and the local file path (if given). 

## Challenges 
The one and only challenge I faced was a major one, because it couldn't be caught locally, but instead, by the Gradescope auto-grader. I wasn't aware that the semantics of sending and receiving data through the data channel differs from the control channel. So, when sending and receiving data, I reused the same function as the control channel. The send function would encode the message in utf-8 before sending and post-appends '\r\n'. The receive function decodes in utf-8 when receiving and keeps listening until it sees a '\n'. Once I submitted it to Gradescope, I realized my code was stuck in an infinite loop due to the tests timing out after 600 seconds of run-time. I boiled the issue down to the receive function, and figured the data sent from the server through the data channel does not always end with a '\n'. So, I defined a different function that would keep listening until the amount read is 0; this means the server finished sending the data and closed the data channel. I then received another error message indicating that the file contents differ (as a result of copying or moving a file). So, I figured the encoding and decoding of data received by the data channel were invalid and that the data should simply be in binary. To resolve this, I open the file to be read in read-only and in binary mode, and write-only and in binary mode when writing (creating a file). Finally, my code worked perfectly!

## Testing
Testing was mainly done by printing variables and server responses.
Since I took this project one step at a time, testing was frankly easy because I made sure each individual component worked and the combination of different components also worked. Starting with parsing and validating the command line arguments, I would pass in valid and invalid arguments and print out what was parsed. The next step was connecting to the server, initializing the FTP commands, and sending mkdir and rmdir commands. The responses I received from the server all indicated success. After that, the project was taken to the next level by adding functionality for listing directories, copying/moving files, and deleting a file. The way I tested these was not only by printing and checking the responses from the server but also by checking the actual contents of the file(s) copied/moved. When I sensed that everything was working fine, I decided to submit it to Gradescope, which contains some other tests that verify the functionality of my program. 

