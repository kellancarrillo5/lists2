import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based Implementation of IndexUnsortedList supporting 
 * a basic Iterator, but nor ListIterator
 * @author mvail, kellancarrillo5
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array; //holds list elements
    private int rear; //next open index/size

    public static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructor for new empty list with default capacity
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(){
        this(DEFAULT_CAPACITY); //avoiding code duplication
    }

    /**
     * Constructor for a new empty list with given initial capcity
     * @param initialCapacity starting capcity for array
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity){
        array =(T[])(new Object[DEFAULT_CAPACITY]);
        rear = 0;
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
        if(index < 0 || index >= rear){
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(T element) {
        // for(int i = 0; i < rear-1; i++){
        //     if(array[i].equals(element)){ //looking for "equivalent' object - "looks like" element
        //         return i;
        //     }
        // }
        // return -1;
        //"best practices" version with only 1 return at the end
        int returnIndex = -1;
        for(int i = 0; returnIndex < 0 && i < rear; i++){
            if(array[i].equals(element)){
                returnIndex = i;
            }
        }
        return returnIndex;
    }

    @Override
    public T first() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return array[rear-1];
    }

    @Override
    public boolean contains(T target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
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
