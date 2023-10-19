# Write the assembly code for main

.global main

.text

main:
  #function prologue
  push %r12 #push callee saved registers
  push %r13
  push %r14 
  # we pushed 16 bytes, and thus the stack is aligned
  enter $0, $0 #Allocate stack space for local variables
  mov %rdi, %r14 #get the number of arguments
  movq 8(%rsi), %r12 # get a
  movq 16(%rsi), %r13 # get b
  #function body: 
  #check if number of arguments is equal to 3 (because program name is included), if not call a helper to handle invalid inputs
  cmp $3, %r14
  jne handleInvalidInput
  # convert the a and b (string arguments) to long using atol
  # we must place r12 in rdi, then mov the result in rax back to r12
  movq %r12, %rdi
  call atol
  movq %rax, %r12
  #do the same for b which is in r13
  movq %r13, %rdi
  call atol
  mov %rax, %r13
  #mov r12 and r13 into rdi and rsi respectively, in order to call compare(long a, long b)
  # the return of compare is stored in rax
  mov %r12, %rdi
  mov %r13, %rsi
  mov $0, %al
  call compare 
  # next step is to compare 0 with return val of compare (if rax = 0, then a = b, if rax is less than zero, it is less, so print "less"
  # if rax is greater, then print "greater", else, print "equal"
  cmp $0, %rax
  jg printGreater
  jl printLess 
  #Else, mov global variable "equal" into rdi to print it
  mov $equal, %rdi
  mov $0, %al
  call printf
  mov $0, %rax
  #this will basically be the function epilogue
  jmp exitProgram

#Exit the program with a status of 1 (indicating failed), with the message: "Two arguments required."
handleInvalidInput:
  mov $1, %rax
  mov $invalidArgs, %rdi
  mov $0, %al
  call printf
  jmp exitProgram
# prints the global variable "less"
# mov the variable to rdi, call printf, and exit the program
# void printLess()
printLess:
  mov $less, %rdi
  mov $0, %al
  call printf
  mov $0, %rax
  jmp exitProgram

# prints the global variable "greater"
# mov the variable to rdi, call printf, and exit the program
# void printGreater()
printGreater:
  mov $greater, %rdi
  mov $0,%al
  call printf
  mov $0, %rax
  jmp exitProgram

#this function will be the function epilogue for main, printLess, and pritnGreater
# clean up the stack frame, then restore callee-saved registers r13 and r12
# finally, ret what was in rax to indicate that the program terminated.
# void exitProgram()
exitProgram:
  leave
  pop %r14
  pop %r13
  pop %r12
  ret

.data
invalidArgs:
   .asciz "Two arguments required.\n"
less:
  .asciz "less \n"  
greater:
  .asciz "greater \n"
equal:
  .asciz "equal \n"
 
