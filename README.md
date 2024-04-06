# COP-4502-Assignment-3

To compile from the command line, type the following:

javac problem1a3.java (for problem 1)
javac ConcurrentLinkedList.java

javac problem2a3.java (for problem 2)

After compiling successfully, to run from the command line type the following:
java problem1a3 (for problem 1)

java problem2a3 (for problem 2)

Problem 1:

The execution time of this program is around 30 seconds on average. One of the requirements that slows it down a lot is the use of so many locks within the Concurrent Linked List. Almost all operations (inserting, removing, searching (contain)). 

The program works by having an unordered set originally. The gift bag has enough room allocated for the number of guests, NUM_GUESTS (500000). When each thread executes, a random task 0-2 is chosen defining their actions. ADD_TO_CHAIN = 0, WRITE_CARD = 1, SEARCH_FOR_PRESENT = 2. in ADD_TO_CHAIN, a lock is placed on the gift bag to maintain mutual exlusivity between threads and the first item is removed and inserted into the linked list. WRITE_CARD has the servent removing a gift from the linked list, to write the thank you card. SEARCH_FOR_PRESENT just has the servent checking if a random present is in the linked list or not.

Problem 2:

The efficiency of this program takes around 220 milliseconds to run on average for the period of 48 hours that I have for testing. This is subject to change based on the number of hours set.
The code utilizes an ExecutorService to manage eight threads (THREAD_COUNT) that each simulate temperature readings over a period of time. (I have 2 days or 48 hours just for testing as a set period was not stated in the prompt). Each thread updates a shared list of sensor readings (sensorReadings) at one-minute intervals (MINUTES). The readings are synchronized to ensure thread safety. The code also checks that all sensors are ready before proceeding to the next minute's reading. A lock (ReentrantLock) is used to generate hourly reports in a thread-safe manner, summarizing the temperature data collected. These reports include the largest temperature difference over a ten-minute interval, as well as the highest and lowest temperatures recorded during the hour.

