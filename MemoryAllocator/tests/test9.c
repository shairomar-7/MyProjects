#ifndef DEMO_TEST
#include <malloc.h>
#else
#include <stdlib.h>
#endif

#include <stdio.h>
#include <assert.h>
#include <pthread.h>
#include <string.h>
#define ARRAY_ELEMENTS 1024
#define NTHREADS 500
pthread_t threads[NTHREADS];

void * thread1() {
     int * data9 = (int *)malloc(256);
    memset(data9, 1, 256);
    int * data10= (int *)malloc(2048);
    memset(data10, 1, 2048);
    int * data11= (int *)malloc(512);
    memset(data11, 1, 512);
    int * data12 = (int *)malloc(32);
    memset(data12, 1, 32);
//    int * data13 = (int *)malloc(1048576);
//    memset(data13, 1, 1048576);
    int * data14 = (int *)malloc(512);
    memset(data14, 1, 512);
    int * data15 = (int *)malloc(512);
    memset(data15, 1, 512);
    int * data16 = (int *)malloc(111);
    memset(data16, 1, 111);

    free(data9);
    free(data10);
    free(data11);
    free(data12);
  //  free(data13);
    free(data14);
    free(data15);
    free(data16);

  printf("Allocing\n");
  int * data1 = (int *)malloc(500);
  int * data2 = (int *)malloc(20);
  free(data1);
  int * data3 = (int*) malloc(5000);
  free(data2);
  free(data3);
}

int main() {

  void * randM = malloc(50);
  free(randM);
  printf("Concurrent Test!\n");
  for (int i = 0; i < NTHREADS; ++ i) {
    pthread_create(&threads[i], NULL, thread1, NULL);
  }
  for (int i = 0; i < NTHREADS; ++i) {
    pthread_join(threads[i], NULL);
  }
  return 0;
}

