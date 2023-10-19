(This is a template. Delete this line and fill in the sections below)
# Threaded Merge Sort Experiments


## Host 1: Khoury XOA VM

- CPU: Intel Xeon CPU E5-2690 v3 @ 2.6 GHz
- Cores: 2 cores (2 cores per socket, and only 1 socket)
- RAM: 4GB
- Storage: 40GB
- OS: Linux Ubuntu

### Input data

*Briefly describe how large your data set is and how you created it. Also include how long `msort` took to sort it.*
- 100,000,000 numbers 
- command to generate dataset: shuf -i 1-100000000 > hundred-million.txt

### Experiments
- Processes Running Before Experiment: 115

#### 1 Thread

Command used to run experiment: `MSORT_THREADS=1 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 22.75 seconds
2. 22.92 seconds
3. 22.89 seconds
4. 22.78 seconds

#### 2 Threads

Command used to run experiment: `MSORT_THREADS=2 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 12.22 seconds
2. 12.75 seconds
3. 12.76 seconds
4. 12.56 seconds

#### 4 Threads

Command used to run experiment: `MSORT_THREADS=4 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 12.58 seconds
2. 12.55 seconds
3. 12.54 seconds
4. 12.27 seconds


## Host 2: Macbook Pro 2021 M1

- CPU: Apple M1 Pro 
- Cores: 8 (6 performance 2 efficiency)
- RAM: 16GB
- Storage: 494.38GB
- OS: Mac OS (version 12.2.1)

### Input data

*Briefly describe how large your data set is and how you created it. Also include how long `msort` took to sort it.*
- 100,000,000 numbers 
- command to generate dataset: shuf -i 1-100000000 > hundred-million.txt

### Experiments

- Processes Running Before Experiment: 507

#### 1 Thread

Command used to run experiment: `MSORT_THREADS=1 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 19.15 seconds
2. 19.27 seconds
3. 20.16 seconds
4. 21.13 seconds

#### 2 Threads

Command used to run experiment: `MSORT_THREADS=2 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 10.30 seconds
2. 10.32 seconds
3. 10.16 seconds
4. 9.97 seconds

#### 4 Threads

Command used to run experiment: `MSORT_THREADS=4 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 5.92 seconds
2. 7.93 seconds
3. 5.84 seconds
4. 8.12 seconds

#### 8 Threads

Command used to run experiment: `MSORT_THREADS=8 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 6.20 seconds
2. 6.42 seconds
3. 6.25 seconds
4. 8.01 seconds

#### 16 Threads

Command used to run experiment: `MSORT_THREADS=16 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 6.58 seconds
2. 6.41 seconds
3. 4.79 seconds
4. 5.06 seconds

#### 32 Threads

Command used to run experiment: `MSORT_THREADS=32 ./tmsort 100000000 < hundred-million.txt > result.txt`

Sorting portion timings:

1. 5.41 seconds
2. 5.60 seconds
3. 5.35 seconds
4. 6.71 seconds

## Observations and Conclusions

*Reflect on the experiment results and the optimal number of threads for your concurrent merge sort implementation on different hosts or platforms. Try to explain why the performance stops improving or even starts deteriorating at certain thread counts.*

Based on our experiments, we concluded that the optimal number of threads is equal to the number of cores on the machine that we ran the code from. This is because each core is responsible for executing a single thread, and when we increase the number of threads past the number of cores on the machine, it seems that threads cannot fully be run independently on a CPU core and causes the threads to overlap on certain CPU cores. Thus, the performance is similar or even worse when running the program when threads exceed the number of cores. 


