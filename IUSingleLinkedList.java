import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node-based implementation of IndexUnsortedList
 * supporting a basic Iterator but not ListIterator
 * 
 * @author mvail and CS221-1- Sp26
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head; // not optional, must-have
    private Node<T> tail; // for efficiency
    private int size; // for efficiency
    private int modCount;

    /**
     * Initialize a new empty list.
     */
    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNextNode(head);
        head = newNode;
        if (tail == null) { // maybe isEmpty or size == 0
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<T>(element);
        if (isEmpty()) {
            head = newNode;
        } else {
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
        Node<T> current = head;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNextNode();
        }
        if (current == null) {
            throw new NoSuchElementException();
        }
        Node<T> newNode = new Node<T>(element, current.getNextNode());
        if (current == tail) {
            tail = newNode;
        }
        current.setNextNode(newNode);
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = head.getElement();
        head = head.getNextNode();
        if (head == null) { // or size =1 -- we're removing the only element
            tail = null;
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
        T retvaL = tail.getElement();
        if (size == 1) { // special case of single element list
            head = tail = null;
        } else { // "general case" where list is "long"
            Node<T> currentNode = head;
            while (currentNode.getNextNode() != tail) {
                currentNode = currentNode.getNextNode();
            }
            currentNode.setNextNode(null);
            tail = currentNode;
        }
        size--;
        modCount++;
        return retvaL;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        Node<T> previous = null;
        Node<T> current = head;

        while (current != null && !found) {
            if (element.equals(current.getElement())) {
                found = true;
            } else {
                previous = current;
                current = current.getNextNode();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        if (size() == 1) { // only node
            head = tail = null;
        } else if (current == head) { // first node
            head = current.getNextNode();
        } else if (current == tail) { // last node
            tail = previous;
            tail.setNextNode(null);
        } else { // somewhere in the middlse
            previous.setNextNode(current.getNextNode());
        }

        size--;
        modCount++;
        return current.getElement();
    }

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNextNode();
        }
        return currentNode.getElement();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // head == null, tail ==null,
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }

    /** Iterator for IUSingleLinkedList */
    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private boolean canRemove;

        /** Creates a new iterator for the list */
        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            canRemove = false;
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
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T retVal = nextNode.getElement();
            nextNode = nextNode.getNextNode();
            canRemove = true;
            return retVal;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }

            if (head.getNextNode() == nextNode) {
                head = nextNode;
                if (head == null) {
                    tail = null;
                }
            } else {
                canRemove = false;
                Node<T> current = head;
                while (current.getNextNode().getNextNode() != nextNode) {
                    current = current.getNextNode();
                }
                current.setNextNode(nextNode);
                if (nextNode == null) {
                    tail = current;
                }
            }
            size--;
            modCount++;
            iterModCount++;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            str.append(current.getElement());
            if (current.getNextNode() != null)
                str.append(", ");
            current = current.getNextNode();
        }
        str.append("]");
        return str.toString();
    }

}
