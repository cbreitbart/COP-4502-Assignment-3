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
