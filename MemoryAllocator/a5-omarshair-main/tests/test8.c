#ifndef DEMO_TEST
#include <malloc.h>
#else
#include <stdlib.h>
#endif

#include <stdio.h>
#include <assert.h>
#define ARRAY_ELEMENTS 1024

int main() {
  // Allocate some data
  printf("Allocating unreasonably large chunk of memory\n");
  int *data = (int *) malloc(40000000000); // allocating 40 GB (should fail)
  assert(data == NULL);
  return 0;
}

