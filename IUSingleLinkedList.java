import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node-based implementation of IndexUnsortedList
 * supporting a basic Iterator but not ListIterator
 * @author mvail and CS221-1- Sp26
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head; //not optional, must-have
    private Node<T> tail; //for efficiency
    private int size; //for efficiency

    /**
     * Initialize a new empty list.
     */
    public IUSingleLinkedList(){
        head = tail = null;
        size = 0;
    }


    @Override
    public void addToFront(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToFront'");
    }

    @Override
    public void addToRear(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToRear'");
    }

    @Override
    public void add(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void addAfter(T element, T target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, T element) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
    }

    @Override
    public T remove(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
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
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for(int i = 0; i<index; i++){
            currentNode= currentNode.getNextNode();
        }
        return currentNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        int retIndex = -1;
        //try to find a better index
        Node<T> currentNode = head;
        int currentIndex = 0;
        while(currentNode != null && retIndex < 0){
            if(currentNode.getElement().equals(element)){
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
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
         if(isEmpty()){
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
        return size == 0; //head == null, tail ==null,
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

}
