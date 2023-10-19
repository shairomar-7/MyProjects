#ssembly code for array_max

.global array_max



.text
# array_max will return the maximum value of the given array of longs
# unsigned long array_max(unsigned long numberOfElements, unsigned long addressOfFirstUL)
array_max:
  #Function Prologue
  push %rdi #save the value of the first argument (number of elements in array), might be needed in the future
  push %rsi #save the value of the second argument (address of first item in array), might be needed in the future
  push %r12 #push callee saved registers to preserve the value
  push %r13 #push callee saved registers to preserve the value
  enter $0, $0 #Allocate stack space for local variables (since stack is already aligned 32 bytes = 2*16bytes, $0)
  
  #indexSoFar = 0
  mov $0, %r12 #index
  #maxValSoFar = 0 (since we can assume the items of the array are unsigned long)
  mov $0, %r13 #max
  #call arrayMaxHelper to perform while loop
  jmp arrayMaxHelper

# This function basically represents a while loop that compares the stored max value, and the elements of the list.
# If the element of the list is greater, set it as the new max value. Keep looping until we reached the end of the list (index == numberofElements).
#void arrayMaxHelper()
arrayMaxHelper:
  #if index >= numberOfElements, then exit the loop and return the max value 
  cmp %rdi, %r12
  jge endLoop
  #else, if (array[index] > maxValSoFar, then maxValSoFar = array[index] 
  cmp %r13, (%rsi, %r12, 8)
  jg storeMax
  # index++
  inc %r12
  #recurse, or in other words, keep looping
  jmp arrayMaxHelper

# Function will store array[index] in r13 (ie, maxValSoFar =  array[index]) 
#void arrayMaxHelper()
storeMax:
  mov (%rsi, %r12, 8), %r13
  # make sure to increment r12, because we are skipping that step by jmping to this function.
  inc %r12
  # return back to caller
  jmp arrayMaxHelper

# This function is basically the epilogue.
# mov the maxValSoFar into rax (register for return of program),
# then clean up the stack, then pop/restore the registers being used,
# then finally, return rax to terminate program
# void endLoop() 
endLoop:
  mov %r13, %rax # mov the max value of rax
  leave #clean up the stack
  pop %r13 # restore
  pop %r12
  pop %rsi
  pop %rdi
  ret
