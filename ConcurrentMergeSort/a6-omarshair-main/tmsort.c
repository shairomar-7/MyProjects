/** 
 * Threaded Merge Sort
 *
 * Modify this file to implement your multi-threaded version of merge sort. 
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <pthread.h>
#include <unistd.h>
#include <assert.h>

#define tty_printf(...) (isatty(1) && isatty(0) ? printf(__VA_ARGS__) : 0)
#ifndef SHUSH
#define log(...) (fprintf(stderr, __VA_ARGS__))
#else 
#define log(...)
#endif

// Helper struct containing the required arguments to be passed to pthread_create.
// The reason we do this is because pthread_create only takes one argument for the "thread function" (processThreadArgs).
typedef struct{
  long *nums; // long pointer represents the array of numbers to be sorted
  long *target; // long pointer represents the result of sorting the numbers
  int to; // int represents the "to" index to be passed to merge_sort_aux 
  int from; // int represents the "from" index to be passed to merge_sort_aux
} arguments_t;

/** The number of threads to be used for sorting. Default: 1 */
int thread_count = 1;

/**
 * Compute the delta between the given timevals in seconds.
 */
double time_in_secs(const struct timeval *begin, const struct timeval *end) {
  long s = end->tv_sec - begin->tv_sec;
  long ms = end->tv_usec - begin->tv_usec;
  return s + ms * 1e-6;
}

/**
 * Print the given array of longs, an element per line.
 */
void print_long_array(const long *array, int count) {
  for (int i = 0; i < count; ++i) {
    printf("%ld\n", array[i]);
  }
}

/**
 * Merge two slices of nums into the corresponding portion of target.
 */
void merge(long nums[], int from, int mid, int to, long target[]) {
  int left = from;
  int right = mid;

  int i = from;
  for (; i < to && left < mid && right < to; i++) {
    if (nums[left] <= nums[right]) {
      target[i] = nums[left];
      left++;
    }
    else {
      target[i] = nums[right];
      right++;
    }
  }
  if (left < mid) {
    memmove(&target[i], &nums[left], (mid - left) * sizeof(long));
  }
  else if (right < to) {
    memmove(&target[i], &nums[right], (to - right) * sizeof(long));
  }
}

// This function will serve as the thread function, ie, the function that each created thread calls.
// Function returns a void pointer; The weird signature is required for a thread function.
// Type cast the given void * args, into an (arguments_t *) and pass the appropriate arguments to merge_sort_aux.
void* processThreadArgs(void * args);

/**
 * Sort the given slice of nums into target.
 * If the thread count is greater than 1, that this function will perform mergeSort recusion concurrently:
 * collect the arguments (nums, from, mid, target) in an arguments_t helper struct, create a thread, and pass the arguments to pthread_create casted
 * to a void * as required by the signature. While the newly created thread recursively sorts the lower half of the array, the main/caller thread
 * (ie, the thread that created a new thread) will be responsible for sorting the upper half of the array in parallel, This leads to an increased
 * performance, depending on the size of the array of numbers, and the requested number of threads by the user.
 * This function will abort the program if pthread_create failed to create a new thread, or if pthread_join failed to wait for a thread (the errors
 * these two functions are stated in their man pages, check them out for information).
 * Warning: nums gets overwritten.
 */
void merge_sort_aux(long nums[], int from, int to, long target[]) {
  if (to - from <= 1) {
    return;
  }
  int mid = (from + to) / 2;
  // If the thread count is greater than or equal to 2, we will spawn new threads to sort the lower half of the given nums array.
  // If the thread count is equal to 1, this means only the main thread is running the mergeSort, and we won't spawn new threads!
  if (thread_count > 1) {
    thread_count--; // Decrement the thread count
    pthread_t newThread; // declare some new thread to be created!
    // Collect the arguments to be accessible by the newThread in an arguments_t helper struct.
    arguments_t * args = (arguments_t *)malloc(sizeof(arguments_t));
    args->nums = nums;
    args->from = from;
    args->to = mid;
    args->target = target;
    // Create a thread to sort the lower half of the array, and have the caller/main thread sort the upper half in parallel. 
    assert(pthread_create(&newThread, NULL, processThreadArgs, (void*) args) == 0); // make sure pthread_create succeeds
    merge_sort_aux(target, mid, to, nums); // let the main thread (or caller thread) sort the upper half of the array in parallel
    assert(pthread_join(newThread, NULL) == 0); // make sure pthread_join succeeds
    free(args); // free the heap allocated arguments!
  }
  else { // In case the thread_count is equal to 1, the main/caller thread will sort both the lower and upper half recursively!
    merge_sort_aux(target, from, mid, nums);
    merge_sort_aux(target, mid, to, nums);
  }
  merge(nums, from, mid, to, target); // merge the two sorted lower half and upper half of the array!
}

// The function description is given above in it's declaration!
void* processThreadArgs(void * args) {
  assert(args != NULL); // check that the given input is not null!
  arguments_t * arguments = (arguments_t *) args; // cast the given void * into an arguments_t * to extract its fields
  merge_sort_aux(arguments->target, arguments->from, arguments->to, arguments->nums); // call merge_sort_aux with appropriate args
}

/**
 * Sort the given array and return the sorted version.
 *
 * The result is malloc'd so it is the caller's responsibility to free it.
 *
 * Warning: The source array gets overwritten.
 */
long *merge_sort(long nums[], int count) {
  long *result = calloc(count, sizeof(long));
  assert(result != NULL);
  memmove(result, nums, count * sizeof(long));
  merge_sort_aux(nums, 0, count, result);
  return result;
}

/**
 * Based on command line arguments, allocate and populate an input and a 
 * helper array.
 *
 * Returns the number of elements in the array.
 */
int allocate_load_array(int argc, char **argv, long **array) {
  assert(argc > 1);
  int count = atoi(argv[1]);
  *array = calloc(count, sizeof(long));
  assert(*array != NULL);
  long element;
  tty_printf("Enter %d elements, separated by whitespace\n", count);
  int i = 0;
  while (i < count && scanf("%ld", &element) != EOF)  {
    (*array)[i++] = element;
  }
  return count;
}

int main(int argc, char **argv) {
  if (argc != 2) {
    fprintf(stderr, "Usage: %s <n>\n", argv[0]);
    return 1;
  }
  struct timeval begin, end;

  // get the number of threads from the environment variable SORT_THREADS
  if (getenv("MSORT_THREADS") != NULL)
    thread_count = atoi(getenv("MSORT_THREADS"));
  // make sure the given thread_count is greater than or equal to 1.
  assert(thread_count >= 1);

  log("Running with %d thread(s). Reading input.\n", thread_count);
  
 // Read the input
  gettimeofday(&begin, 0);
  long *array = NULL;
  int count = allocate_load_array(argc, argv, &array);
  gettimeofday(&end, 0);
  log("Array read in %f seconds, beginning sort.\n", 
  time_in_secs(&begin, &end));

  // Sort the array
  gettimeofday(&begin, 0);
  long *result = merge_sort(array, count);
  gettimeofday(&end, 0);
  
  log("Sorting completed in %f seconds.\n", time_in_secs(&begin, &end));

  // Print the result
  gettimeofday(&begin, 0);
  print_long_array(result, count);
  gettimeofday(&end, 0);
  
  log("Array printed in %f seconds.\n", time_in_secs(&begin, &end));

  free(array);
  free(result);
  return 0;
}
