#define _DEFAULT_SOURCE
#define _BSD_SOURCE 
#include <malloc.h> 
#include <stdio.h> 
#include <errno.h>
#include <unistd.h>
#include <string.h>
#include <assert.h>
#include <math.h>
#include <sys/mman.h>
#include <sched.h>
extern int errnol;
#include <debug.h> // definition of debug_printf
#include <pthread.h>
#define BLOCK_SIZE sizeof(block_t) // size of the struct block

// This data structure is basically a linked list with additional data (free, size).
// It will be used to keep track of allocated/freed memory!
typedef struct block {
  size_t size;        // How many bytes beyond this block have been allocated in the heap
  struct block *next; // Where is the next block in your linked list
} block_t;

// headA represents the first allocated block, and tailA represents the last allocated block. 
// headF represents the first free block in our free linked list.
block_t * headF = NULL;
size_t PAGE_SIZE = 4096;
pthread_mutex_t mtx1 = PTHREAD_MUTEX_INITIALIZER; // global mutex for thread-safe memory allocator

// returns the ceil of the given double as an int.
// If "fraction" is less than 1, return 1, else cast to int and add 1.
int getCeil(double fraction) {
  if (fraction < 1) return 1;
  int roundDown = (int) fraction;
  return roundDown + 1;
}

// Gets a multiple of the page size and returns the total number of bytes.
// If the given size is divisible by the page size, return it.
// If not, divide it by the page size, and round up the decimal result and multiply by page size.
size_t getMultiplePage(size_t s) {
  if (s % PAGE_SIZE == 0) return s;
  double fraction = s / PAGE_SIZE;
  return getCeil(fraction) * PAGE_SIZE;
}

// Iterate the free linked list -> 
// if a block of size greater or equal to the given size is found, return it.
// Else, return NULL to indicate that no free block has found with a satisfying size. 
block_t * getFreeBlock(size_t s) {
  block_t * tmp = headF;
  while (tmp){
    if (tmp->size >= s) return tmp;
    tmp = tmp->next;
  }
  return NULL;
}

// Removes the given block from the free linked list.
// Iterates the free linked list, if we find a match, we will delete it from the list.
// Deleting process: set the previous's next to currrent's next, edge cases:
// If there is no previous, then we will set the head of the free list to current's next.
// If no block is found, nothing happens to the list. 
void removeFromFree(block_t * block) {
  block_t * curr, * prev = NULL;
  curr = headF;
  while (curr) {
    if (curr == block) {
      if (prev) prev->next = curr->next;
      else headF = curr->next;
      return;
    }
    prev = curr;
    curr = curr->next;
  }
}

// inserts the given block into the free linked list.
// This function maintains the "sortness" property of our free list by ascending order of mem address.
// This function will iterate the free list and will keep track of:
// the previous block, and the current block.
// It will place the given block such as the previous's address is smaller and,
// the current's address is greater.
void insert(block_t * b) {
  assert(b != NULL); // check for invalid input
  block_t * curr, *prev = NULL;
  curr = headF;
  if (!curr) { // if the head is null, the given block is now the head!
    headF = b;
    b->next = NULL;
  } 
  while (curr) {
    if ((char *) b < (char *)curr) { // if address of b < address of current
      if (!prev) { 
        b->next = curr;
	headF = b;
      }
      else {
        prev->next = b;
	b->next = curr;
      }
      return;
    } 
    prev = curr;
    curr = curr->next;
  }
}

// Splits the given block based on the user's requested size of memory, and the actual.
// If the actual minus the requested size leads to a block less than a byte plus the size of the header
// then we won't split the block, we'll just set it's size to actual, and return the block.
// If not, set the block's size to the request, and  split the block
// by creating a block_t * remainder which will be set to
// the given block's address plus the size of the header plus the requested size. 
// Now, set it's size to the actual minus the requested size minus the size of the new header.
// Finally, insert the remainder 
// Assumptions: requestedSize, and actual will never be null!
block_t * splitBlock(size_t requestedSize, size_t actual, block_t * block) {
  assert(block != NULL);
  if (actual - requestedSize < 1 + BLOCK_SIZE) {
    block->size = actual;
    block->next = NULL;
    return block;
  }
  block->size = requestedSize;
  block_t * remainder = (block_t *)((char *)block + BLOCK_SIZE + requestedSize);
  remainder->size = actual - requestedSize - BLOCK_SIZE;
  insert(remainder);
  block->next = NULL;
  return block;
}

// Coalesces contiguous blocks in memory.
// If the free list is empty, exit function.
// If two blocks have contigous address, we will combine them into one bigger block.
// contiguous if and only if: block A's address + A's size + BLOCKSIZE = block's B's address.
// The process of combining them involves iterating the free list, tracking the previous and current,
// and if the previous and current are contiguous, set the previous' next to current's next, and
// add the size of prev with the size of current, and add the size of struct block_t.
void coalesce() {
  block_t * prev, * curr;
  prev = headF;
  if (prev == NULL) return;
  curr = prev->next;
  while (curr) {
    if ((char *)prev + (prev->size + BLOCK_SIZE) == (char *) curr) {
      prev->next = curr->next;
      prev->size += curr->size;
      prev->size += BLOCK_SIZE;
      curr = curr->next;
    }
    else {
      prev = curr;
      curr = curr->next;
    }
  }
}

// returns a new block by requesting total size bytes from the operating system with mmap.
// This new block will be split into two: the 1st chunk is of the requested size and is returned,
// the 2nd is the remainder of total size and requested size; it will be put in the free list.
// Assumptions: requestedSize and totalSize will not be null!
block_t * getNewBlock(size_t requestedSize, size_t totalSize) {
  void * osBlock = mmap(0, totalSize, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
  assert(osBlock != (void *) -1);
  block_t * block = (block_t *) osBlock;
  block = splitBlock(requestedSize, totalSize - BLOCK_SIZE, block);
  return block;
}

// returns a void pointer to the requested memory block of size s.
// Failing cases: nmemb or s is NULL, system is out of memory!
// This critical section of mymalloc, mostly all of it, is locked for one thread with a mutex.
// The mutex will be unlocked right before the every return statement. 
// If the request was less than a page, check the free list for a block of size >= s.
// If a block was found, remove it from the free list, try to split the block to minimize waste,
// return the address of the block plus the size of the struct. 
// If not, get a new block by requesting a new page from the OS, split, and return the split block.
// If the request was greater than a page, get a multiple of the page size, and request that amount 
// from the OS. Set the block's size to the multiple of page, and return block+1.
void * mymalloc(size_t s) {
  if (!s) return NULL;
  pthread_mutex_lock(&mtx1);
  block_t * block;
  if (s < PAGE_SIZE) {
     block = getFreeBlock(s);
     if (block) {
       removeFromFree(block);
       splitBlock(s, block->size, block);
       debug_printf("Malloc %zu bytes, actual = %zu\n", s, block->size);
       pthread_mutex_unlock(&mtx1);
       return (void *) (block+1);
     }
     block = getNewBlock(s, PAGE_SIZE); 
     debug_printf("Malloc %zu bytes\n, actual = FULLPAGE", s);
     pthread_mutex_unlock(&mtx1);
    return (void *) (block+1);
  }
  size_t bytes = getMultiplePage(s + BLOCK_SIZE); 
  void * osBlock = mmap(0, bytes, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
  assert(osBlock != (void *) -1);
  block = (block_t*)osBlock;
  block->size = bytes - BLOCK_SIZE;
  block->next = NULL;
  debug_printf("Malloc %zu bytes, actual = %zu\n",s, block->size);
  pthread_mutex_unlock(&mtx1);
  return (void *)(block + 1);
}

// Frees the given pointer from memory and hands the memory to the operating system.
// This function is thread-safe and will lock most of it's body with a pthread_mutex
// If the size of the header indicates less than a page, we will insert the block in the free list,
// and will try to coalesce the blocks.
// If the size of block is greater than a page, we will unmap it from memory. 
// If the given pointer is null, exit.
void myfree(void * ptr) {
  if (!ptr) return;
  pthread_mutex_lock(&mtx1);
  block_t * block = (block_t *) (ptr - BLOCK_SIZE);
  assert(block != NULL);
  if (block->size + BLOCK_SIZE < PAGE_SIZE) {
    insert(block);
    coalesce();
    debug_printf("Freed %zu bytes\n", block->size);
  }
  else {
    size_t alias = block->size + BLOCK_SIZE;
    munmap(block, block->size + BLOCK_SIZE);
    debug_printf("Freed %zu bytes\n", alias);
  }
  pthread_mutex_unlock(&mtx1);
}

// Allocates memory for an array of nmemb number of size s bytes each.
// Returns a pointer to the allocated chunk of memory upon success. If failed, mycalloc returns NULL.
// Failing cases: nmemb or s is NULL, system is out of memory!
void *mycalloc(size_t nmemb, size_t s) {
  if (!nmemb || !s) return NULL; // check arguments are valid!
  size_t totalSize = nmemb * s;
  if (s != totalSize / nmemb) return NULL; // check for mul overflow
  void * block = mymalloc(totalSize);
  if (!block) return NULL;
  memset(block, 0, totalSize); // set the chunk of memory to 0
  debug_printf("Calloc %zu bytes\n", totalSize);
  return block;
}
