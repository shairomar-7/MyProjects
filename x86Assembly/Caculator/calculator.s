#
# Usage: ./calculator <op> <arg1> <arg2>
#

# Make `main` accessible outside of this module
.global main

# Start of the code section
.text

# int main(int argc, char argv[][])
main:
  # Function prologue
  enter $0, $0

  # Variable mappings:
  # op -> %r12
  # arg1 -> %r13
  # arg2 -> %r14
  movq 8(%rsi), %r12  # op = argv[1]
  movq 16(%rsi), %r13 # arg1 = argv[2]
  movq 24(%rsi), %r14 # arg2 = argv[3]


  # Hint: Convert 1st operand to long int
  movq %r13, %rdi
  mov $0, %al
  call atol
  movq %rax, %r13
  # Hint: Convert 2nd operand to long int
  movq %r14, %rdi
  mov $0, %al
  call atol
  movq %rax, %r14
  # Hint: Copy the first char of op into an 8-bit register
  # i.e., op_char = op[0] - something like mov 0(%r12), ???
  mov 0(%r12), %dl
  cmp $'+, %dl
 # mov $format,%rdi
 # movq %r13, %rsi
 # mov $0, %al
 # call printf
  je add
  cmp $'-, %dl
  je subtract
  cmp $'*, %dl
  je multiply
  cmp $'/, %dl
  je divide
  

  # THIS IS THE ELSE CASE: just move error message into rdi, and print it
  mov $error, %rdi
  mov $0, %al
  call printf  
  # if (op_char == '+') {
  #   ...
  # }
  # else if (op_char == '-') {
  #  ...
  # }
  # ...
  # else {
  #   // print error
  #   // return 1 from main
  # }

  # Function epilogue
  leave
  ret

exitDueToError:
  mov $divByZeroError, %rdi
  mov $0, %al
  call printf
  leave
  ret

printResult:
  movq %r14, %rsi
  mov $format, %rdi
  mov $0, %al
  call printf
  movq %r14, %rax
  leave
  ret

add: 
  addq %r13, %r14
  jmp printResult
  leave
  ret 

subtract:
  subq %r14, %r13
  movq %r13, %r14
  jmp printResult
  leave
  ret

multiply:
  imulq %r13, %r14
  jmp printResult
  leave 
  ret
#execute a function that clears 
divide:
  movq %r13, %rax
  mov $0,  %rdx   
  cmpq $0, %r14
  je exitDueToError
 # call somefunction, double quad divident
  cqto 
  idivq %r14
  cltq
  movq %rax, %rsi
  mov $format, %rdi
  mov $0, %al
  call printf
 # movq %rax, %r14
  leave
  ret
# Start of the data section
.data

format: 
  .asciz "%ld\n"
error:
  .asciz "Unknown operation\n"
divByZeroError:
  .asciz "Cannot divide by zero, review your math!\n"
