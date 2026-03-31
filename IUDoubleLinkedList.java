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
     * 
     */

    @Override
    public void addToFront(Object element) {
        Node<T> newNode = new Node<T>(element);

        if (size > 0) {
            head.setPrevNode(newNode);
        } else {
            tail = newNode;
        }
        newNode.setNextNode(head);
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToRear'");
    }

    @Override
    public void add(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void addAfter(Object element, Object target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T retVal = tail.getElement();
        if (size > 1) {
            tail.getPrevNode().setNextNode(null);
        } else {
            head = null;
        }
        tail = tail.getPrevNode();
        size--;
        modCount++;
        return retVal;
    }

    @Override
    public T remove(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public T get(int index) {
        // can double search speed by starting from the most appropriate end
        // start at head if index is in the first half or tail if index is in second
        // half
        throw new UnsupportedOperationException("Unimplemented method 'get'");
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
        return size == 0;
    }

    @Override
    public int size() {
        return size;
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
     * ListIterator  ( and basic Iterator for Double Linked List)
     */
    private class DLLIterator implements ListIterator<T>{
        private Node<T> NextNode;
        private int iterModCount;
        private int nextIndex;

        /**
         * initialize iterator before the first element
         */
        public DLLIterator() {
            // nextNode = head;
            // iterModCount = modCount;
            // nextIndex = 0;
            this(0); //c
        }

        /**
         * Initializes iterator before the given startingIndex
         * @param startingIndex
         * @throws IndexOutOfBounds
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size){
                throw new IndexOutOfBoundsException();
            }
            nextIndex = startingIndex;
            nextNode = head;
            for (int i = 0; i < startingIndex; i++){
                nextNode = nextNode.getNextNode();
            }
            iterModCount = modCount;
        }
        @Override
        public boolean hasNext() {
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
            return nextNode != null;
        }

        @Override
        public T next() {
            if(iterModCount != modCount){
                throw new NoSuchElementException();
            }
            T retVal = nextNode.getElement();
            nextNode = nextNode.getNextNode();
            nextIndex++;
            return retVal;
        }

        @Override
        public boolean hasPrevious() {
            if(iterModCount != modCount){
                throw new ConcurrentModificationException();
            }
            return nextNode != head;
        }

        @Override
        public T previous() {
            if (!hasPrevious()){
                throw new NoSuchElementException();
            }
            if (nextNode != null) {
                nextNode = nextNode.getPrevNode();
            } else {
                nextNode = tail;
            }          
            nextIndex--;
            return nextNode.getElement();
        }

        @Override
        public int nextIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'nextIndex'");
        }

        @Override
        public int previousIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'previousIndex'");
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'remove'");
        }

        @Override
        public void set(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }

        @Override
        public void add(T e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
        }

    }
}
