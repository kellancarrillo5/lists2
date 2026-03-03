import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based Implementation of IndexUnsortedList supporting
 * a basic Iterator, but nor ListIterator
 * 
 * @author mvail, kellancarrillo5
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array; // holds list elements
    private int rear; // next open index/size

    public static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructor for new empty list with default capacity
     */
    @SuppressWarnings("unchecked")
    public IUArrayList() {
        this(DEFAULT_CAPACITY); // avoiding code duplication
    }

    /**
     * Constructor for a new empty list with given initial capcity
     * 
     * @param initialCapacity starting capcity for array
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        array = (T[]) (new Object[DEFAULT_CAPACITY]);
        rear = 0;
    }

    @Override
    public void addToFront(T element) {
        for (int i = rear; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = element;
        rear++;
    }

    @Override
    public void addToRear(T element) {
        array[rear] = element;
        rear++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int targetIndex = indexOf(target);
        if(targetIndex < 0 || targetIndex > rear){
            throw new NoSuchElementException();
        }
        add(targetIndex + 1, element);
    }

    @Override
    public void add(int index, T element) {
        if (index > rear || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = rear; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        rear++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return remove(0);
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return remove(rear - 1);
    }

    @Override
    public T remove(T element) {
        int index = indexOf(element); 
        if (index < 0 || index > rear) {
            throw new NoSuchElementException();
        }
        return remove(index);
    }

    @Override
    public T remove(int index) {
         if(index < 0 || index >= rear){
            throw new IndexOutOfBoundsException();
        }
        T removed = array[index];
        // Shift elements left to fill the gap
        for(int i = index; i < rear - 1; i++){
            array[i] = array[i + 1];
        }
        array[rear - 1] = null;
        rear--;
        return removed;
    }

    @Override
    public void set(int index, T element) {
        if(index < 0 || index >= rear){
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        // "best practices" version with only 1 return at the end
        int returnIndex = -1;
        for (int i = 0; returnIndex < 0 && i < rear; i++) {
            if (array[i].equals(element)) {
                returnIndex = i;
            }
        }
        return returnIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[rear - 1];
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1; // if it returns a valid index we found it
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
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
