/* Complete the C version of the driver program for compare. This C code does
 * not need to compile. */

#include <stdio.h>

long compare(long a, long b) {
  if (a < b)
    return -1;
  if (a == b)
    return 0;

  return 1;
}


int main(int argc, char *argv[]) {
  if (argc == 3) {
    return 1;
  }
  int a;
  int b;
  char resultStr[7];
  a = atol(argv[1]);
  b = atol(argv[2]);
  long result = compare(a, b);
  if (result < 0) {
    printf("less");
  }
  else if (result > 0) {
    printf("greater");
  }
  else {
    printf("equal");
  }
  return 0;
}

