// A second test
#ifndef DEMO_TEST
#include <malloc.h>
#else
#include <stdlib.h>
#endif

#include <stdio.h>
#include <string.h>

int main() {

  // Constantly allocate and reallocate data.
  // Ideally your allocator can use the same block over
  // and over again.
  for (int i = 0; i < 100; i++) {
//    int *data = (int *) malloc(1024);
  //  memset(data, 1, 1024);
//    int * data1 = (int *)malloc(2048);
//    memset(data1, 1, 2048);
//    int * data2= (int *)malloc(65536);
//    memset(data2, 1, 65536);
//    int * data3= (int *)malloc(131072);
//    memset(data3, 1, 131072);
//    int * data4 = (int *)malloc(32768);
//    memset(data4, 1, 32768);
//    int * data5 = (int *)malloc(512);
//    memset(data5, 1, 512);
//    int * data6 = (int *)malloc(2048);
//    memset(data6, 1, 2048);
//    int * data7 = (int *)malloc(2048);
//    memset(data7, 1, 2048);
//    int * data8 = (int *)malloc(65536);
//    memset(data8, 1, 65536);

 //   free(data1);
   // free(data2);
    //free(data3);
   // free(data4);
   // free(data5);
   // free(data6);
   // free(data7);
    //free(data8);

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
  }
  return 0;
}
