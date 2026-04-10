import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node-based implementation of IndexUnsortedList
 * support a basic Iterator and a ListIterator
 * 
 * @author mvail and CS221-1 Sp2026
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Constructs a new, empty double-linked list.
     */
    public IUDoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) {
            tail = newNode;
        } else {
            newNode.setNextNode(head);
            head.setPrevNode(newNode);
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) {
            head = newNode;
        } else {
            newNode.setPrevNode(tail);
            tail.setNextNode(newNode);
        }
        tail = newNode;
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        // Find the target node
        Node<T> current = head;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNextNode();
        }
        if (current == null) {
            throw new NoSuchElementException();
        }
        // Insert newNode between current and current.next
        Node<T> newNode = new Node<T>(element);
        newNode.setPrevNode(current);
        newNode.setNextNode(current.getNextNode());
        if (current.getNextNode() != null) {
            current.getNextNode().setPrevNode(newNode);
        } else {
            // current was tail
            tail = newNode;
        }
        current.setNextNode(newNode);
        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addToFront(element);
        } else if (index == size) {
            addToRear(element);
        } else {
            // Walk to the node currently at 'index'
            Node<T> nodeAtIndex = nodeAt(index);
            Node<T> newNode = new Node<T>(element);
            Node<T> prev = nodeAtIndex.getPrevNode();
            newNode.setNextNode(nodeAtIndex);
            newNode.setPrevNode(prev);
            prev.setNextNode(newNode);
            nodeAtIndex.setPrevNode(newNode);
            size++;
            modCount++;
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = head.getElement();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNextNode();
            head.setPrevNode(null);
        }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevNode();
            tail.setNextNode(null);
        }
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T remove(T element) {
        Node<T> current = head;
        while (current != null && !current.getElement().equals(element)) {
            current = current.getNextNode();
        }
        if (current == null) {
            throw new NoSuchElementException();
        }
        return removeNode(current);
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return removeNode(nodeAt(index));
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        nodeAt(index).setElement(element);
        modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return nodeAt(index).getElement();
    }

    @Override
    public int indexOf(T element) {
        int retIndex = -1;
        // try to find a better index
        Node<T> currentNode = head;
        int currentIndex = 0;
        while (currentNode != null && retIndex < 0) {
            if (currentNode.getElement().equals(element)) {
                retIndex = currentIndex;
            } else {
                currentNode = currentNode.getNextNode();
                currentIndex++;
            }
        }
        return retIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) >= 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Removes the given node from the list and returns its element.
     * 
     * @param node the node to remove (must be in this list)
     * @return the element stored in the removed node
     */
    private T removeNode(Node<T> node) {
        T retVal = node.getElement();
        if (node == head) {
            head = node.getNextNode();
            if (head != null) {
                head.setPrevNode(null);
            } else {
                tail = null; // list is now empty
            }
        } else if (node == tail) {
            tail = node.getPrevNode();
            tail.setNextNode(null);
        } else {
            node.getPrevNode().setNextNode(node.getNextNode());
            node.getNextNode().setPrevNode(node.getPrevNode());
        }
        size--;
        modCount++;
        return retVal;
    }

    /**
     * Returns the node at the given index.
     * 
     * @param index a valid index (0 <= index < size)
     * @return the node at that position
     */
    private Node<T> nodeAt(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNextNode();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrevNode();
            }
        }
        return current;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            str.append(current.getElement());
            if (current.getNextNode() != null) {
                str.append(", ");
            }
            current = current.getNextNode();
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public Iterator iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    /**
     * ListIterator ( and basic Iterator for Double Linked List)
     */
    private class DLLIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private Node<T> lastReturned;
        private boolean lastWasNext;
        private int iterModCount;
        private int nextIndex;

        /**
         * initialize iterator before the first element
         */
        public DLLIterator() {
            this(0); // c
        }

        /**
         * Initializes iterator before the given startingIndex
         * 
         * @param startingIndex
         * @throws IndexOutOfBounds
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }
            iterModCount = modCount;
            nextIndex = startingIndex;
            lastReturned = null;
            lastWasNext = false;

            if (startingIndex == size) {
                nextNode = null; // cursor is at the end
            } else {
                // Walk to position – nodeAt() uses bidirectional traversal
                nextNode = nodeAt(startingIndex);
            }
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode;
            nextNode = nextNode.getNextNode();
            nextIndex++;
            lastWasNext = true;
            return lastReturned.getElement();
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextNode != head;
        }

        @Override
        public T previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            // Step the cursor back
            if (nextNode != null) {
                nextNode = nextNode.getPrevNode();
            } else {
                nextNode = tail; // cursor was at end; step back to tail
            }
            nextIndex--;
            lastReturned = nextNode;
            lastWasNext = false;
            return lastReturned.getElement();
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            // Adjust nextNode/nextIndex so cursor stays logically in place
            if (lastWasNext) {
                // next() was the last movement – lastReturned is behind cursor
                nextIndex--;
            } else {
                // previous() was the last movement – lastReturned is at cursor; advance it
                nextNode = lastReturned.getNextNode();
            }
            removeNode(lastReturned); // updates size and modCount
            iterModCount = modCount;
            lastReturned = null;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.setElement(e);
            // set() increments the list modCount; sync the iterator's copy
            modCount++;
            iterModCount = modCount;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            Node<T> newNode = new Node<>(e);

            if (isEmpty()) {
                // List was empty
                head = newNode;
                tail = newNode;
            } else if (nextNode == null) {
                // Cursor is at the end – append
                newNode.setPrevNode(tail);
                tail.setNextNode(newNode);
                tail = newNode;
            } else if (nextNode == head) {
                // Cursor is at the front – prepend
                newNode.setNextNode(head);
                head.setPrevNode(newNode);
                head = newNode;
            } else {
                // Cursor is in the middle
                Node<T> prev = nextNode.getPrevNode();
                newNode.setPrevNode(prev);
                newNode.setNextNode(nextNode);
                prev.setNextNode(newNode);
                nextNode.setPrevNode(newNode);
            }

            size++;
            nextIndex++; // new node is now on the "previous" side
            modCount++;
            iterModCount = modCount;
            lastReturned = null; // cannot remove/set after add
        }

    } // End the iterator!
}
