****************
* Double Linked List
* CS 221
* 4/9/26
* Kellan McCamish, kellancarrillo5
**************** 

OVERVIEW:

This program implements Double-linked, a node-based implementation of IndexUnsortedList that supports a basic Iterator and a ListIterator. These iterators allow you to navigate forwards and backwards and supports add, remove, and set operations. Additonally there is a full test suite in ListTester that was utilized to verify correct behavior for all methods and scenerios. 


INCLUDED FILES:

 * IUDoubleLinkedList.java - source file, double linked list implementation of IndexUnsortedList including a DLLIterator class
 * ListTester.java - source file, verifies the behavior of IndexedUnsortedList and implementation with all ListIterator methods and concurrency scenarios
 * Node.java - source file, double linked node that stores an element and is able to references to both the next and previous nodes
 * IndexUnsortedList.java - source file, the interface that defines all methods that IUDoubleLinkedList inherits
 * README.txt - this file


COMPILING AND RUNNING:

 From the directory containing all source files, compile the driver class and all dependent files with the command:
$ javac ListTester.java
Run the compiled class file with the command: 
$ java ListTester
The output will display to the console each test result followed by a final summary showing total tests run, passed, and failed.

PROGRAM DESIGN AND IMPORTANT CONCEPTS:

The interface IndexUnsortedList interface defines the contract signed by DoubleLinkedList when it inherits IndexUnsortedList and is ultimately what this program is centered around. Some of these methods include indexed access, unsorted storage, add/remove/set operations at different positions, and both Iterator and ListIterator support. By using an interface, it can be easily modified for any future implementation as we had done previously in assignments SLL and arrayList. Node<T> is a double-linked node for linear data stuctures that is utilized in DoubleLinkedList, essentially creating the building blocks of the list. It is able to hold an element and references to both the next and previous nodes. Having both directions is what makes the list double linked by allowing you to go through the list both forward and backward. From there IUDoubleLinkedList is ready to inherit IndexUnsortedList. It keeps track of a head reference, tail reference, size counter, and modCount that increments on every change to the list. There are two helper methods I added to IUDoubleLinkedList, nodeAt(int index) and removeNode(Node<T>). nodeAt(int index) starts at the head when the target index is in the first half of the list and starts at the tail when its in the second half, helping save time to find the node. Additionally removeNode(Node<T>) saves lines of code that would have been written in each remove method. Each remove method finds the correct node and passes it to the helper, keeping it effective and clear. There is also a inner class, DLLIterator that handles both the basic Iterator and ListIterator. It keeps track of the nextNode, prevNode, and which direction the last movement was. A key component of the DLLIterator is that every method checks whether the list has been modified from outside the iterator and throws a ConcurrentModificationException if it has. 


TESTING:

 
Testing was done with the provided testing class ListTester and adding upon it in stages. Each scenario builder method constructs a list in a specific state, modifies it, and then tests if it was the anticipated result. These test checks every observable property of the list in that state like the size, isEmpty, first, last, contains, get, indexOf, toString, all iterator states, and all expected exceptions for out-of-bounds or invalid operations. The testing class covers four list sizes: empty, one element, two elements, three elements for all of the construction scenarios. The ListIterator were also written for all of the list sizes. 

All tests are passing with no failure. The program checks that all index and element arguments and throws the correct exceptions in every case required by the interface contract.


DISCUSSION:
 
 The hardest part of this project for me was grasping the ListIterator cursor sat between elements instead of on one. Specifically this proved a challenge in the remove() method. After removing the element having the cursor in the right spot depended on if you called next() or previous(). Getting the direction logic wrong caused test to fail that used the iterators remove method because the cursor would be pointing at the wrong node after the removal, making the next next() or previous() call return the wrong element. What really helped me think about how to solve this was drawing out the nodes and working through where the cursor should be. Similarly to how we worked on the methods in class, visually seeing what we were talking about helped me find this issue. After slowly figuring out the logic I was able to fix this with the boolean lastWasNext to track the last direction. If lastWasNext is true it meant the node was behind the cursor because next() was called last, meaning that you'd have to decrement nextIndex. While if it was false the node would be at the curse and previous() was called last so it needed to advance nextNode past it. After this fix a significant amount of my tests were passing. This cursor confustion in the DLLIterator was my main challenge to grasp, and finally was able to click after much trouble shooting. Drawing the nodes became my biggest asset to really force myself to fully understand what was happening. Overall there were many challenging aspects of this project that allowed me to gain a deep understanding of iterators and how a double-linked list functions.
 