import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based Implementation of IndexUnsortedList supporting
 * a basic Iterator, but nor ListIterator
 * 
 * @author mvail, CS221 class, kellancarrillo5
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array; // holds list elements
    private int rear; // next open index/size
    private int modCount;

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
        modCount = 0;
    }

    /**
     * Double the array capacity if full
     */
    private void expandIfNecessay() {
        if (array.length == rear) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    @Override
    public void addToFront(T element) {
        expandIfNecessay();
        //shift all the elements up one position to make room at the very front
        for (int i = rear; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        expandIfNecessay();
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        expandIfNecessay();
        int targetIndex = indexOf(target);
        if (targetIndex < 0 || targetIndex > rear) {
            throw new NoSuchElementException();
        }
        add(targetIndex + 1, element);
    }

    @Override
    public void add(int index, T element) {
        expandIfNecessay();
        if (index > rear || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        //Shift all elements to the right, make space at the index
        for (int i = rear; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        rear++;
        modCount++;
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
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        T removed = array[index];
        // Shift elements left to fill the gap
        for (int i = index; i < rear - 1; i++) {
            array[i] = array[i + 1];
        }
        array[rear - 1] = null;
        rear--;
        modCount++;
        return removed;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        modCount++;
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
        //returns a valid index if only if it exists
        return indexOf(target) > -1; 
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
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        // loop through the list and append all the elements
        for (T element : this) {
            str.append(element.toString());
            str.append(", ");
        }
        if (str.length() > 1) {
            str.delete(str.length() - 2, str.length());
        }
        str.append("]");
        return str.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ALIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }

    /** Basic Iterator for IUArrayList */
    private class ALIterator implements Iterator<T> {
        private int nextIndex; // index of element that would be next
        private int iterModCount;
        private boolean canRemove;

        /**
         * Initialize Iterator before first element
         */
        public ALIterator() {
            nextIndex = 0;
            iterModCount = modCount;
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            //There is a next element as long as nextIndex hasn't reached rear
            return nextIndex < rear;
        }

        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
            return array[nextIndex++];
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;
            // Last returned element was at nextIndex - 1
            IUArrayList.this.remove(nextIndex - 1);
            nextIndex--;
            iterModCount = modCount;
        }
    } // end of ALIterator Class
}
