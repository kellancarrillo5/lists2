****************
* Double Linked List
* CS 221
* 4/9/26
* Kellan McCamish, kellancarrillo5
**************** 

OVERVIEW:

This program implements Double-linked, a node-based implementation of IndexUnsortedList that supports a basic Iterator and a 
ListIterator. These iterators allow use to navigate forwards and backwards and supports add, remove, and set operations.
Additonally there is a full test suite in ListTester that was utilized to verify correct behavior for all methods and scenerios. 

INCLUDED FILES:

 List the files required for the project with a brief
 explanation of why each is included.

 e.g.
 * IUDoubleLinkedList.java - source file, double linked list implementation
 * ListTester.java - source file, verifies the behavior of IndexedUnsortedList implementation with all ListIterator methods and concurrency scenarios
 * Node.java - source file, double linked node that stores an element and references to both the next and previous nodes
 * IndexUnsorted.java - source file, the interface that defines all methods that IUDoubleLinkedList inherits
 * README.txt - this file


COMPILING AND RUNNING:

 Give the command for compiling the program, the command
 for running the program, and any usage instructions the
 user needs.
 
 In the command line enter the following:
  1. Start by entering the folder with the included files above using cd and following the directory path to where the file is stored.
  2. Once you are in the folder, verify with ls "filename" that all files listed in the included files section are there. This confirms you are in the directory containing all the source files.
  3. Enter the command: $ javac ListTester.java
  4. Enter the command: $ java ListTester
  5. The output will display to the console each test result followed by a final summary showing total tests run, passed, and failed.

PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 This is the sort of information someone who really wants to
 understand your program - possibly to make future enhancements -
 would want to know.

 Explain the main concepts and organization of your program so that
 the reader can understand how your program works. This is not a repeat
 of javadoc comments or an exhaustive listing of all methods, but an
 explanation of the critical algorithms and object interactions that make
 up the program.

 Explain the main responsibilities of the classes and interfaces that make
 up the program. Explain how the classes work together to achieve the program
 goals. If there are critical algorithms that a user should understand, 
 explain them as well.
 
 If you were responsible for designing the program's classes and choosing
 how they work together, why did you design the program this way? What, if 
 anything, could be improved? 

 The interface IndexUnsortedList.java is what this program is centered around. 
 This interface defines the contract signed by DoubleLinkedList when it inherits IndexUnsortedList. Some of these methods include indexed access, unsorted storage, add/remove/set operations at
 different positions, and both Iterator and ListIterator support. By using an interface, it can be easily modified for any future implementation.
 Node<T> is a double-linked node for linear data stuctures. It is able to hold an element and references to both the next and previous nodes. 
 Allowing for easy adding and removal of components on the list with the ability to move backwards. From there IUDoubleLinkedList is ready to inherit IndexUnsortedList. It maintains a head
 reference, a tail reference, a size counter, and a modCount that increments on every structural change. There are two helper methods I added to IUDoubleLinkedList, nodeAt(int index) and removeNode(Node<T>). 

TESTING:

 How did you test your program to be sure it works and meets all of the
 requirements? What was the testing strategy? What kinds of tests were run?
 Can your program handle bad input? Is your program  idiot-proof? How do you 
 know? What are the known issues / bugs remaining in your program?


DISCUSSION:
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?
 
 