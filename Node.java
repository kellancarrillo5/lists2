/**
 * Double-linked node for linear data stuctures
 * 
 * @author mvail, CS221-1 Sp26, kellancarrillo5
 */
public class Node<T> {
    private Node<T> nextNode;
    private Node<T> prevNode;
    private T element;

    /**
     * Creates an empty node.
     */
    public Node() {
        nextNode = null;
        element = null;
        prevNode = null;
    }

    /**
     * Create Node with the Given element
     * 
     * @param element
     */
    public Node(T element) {
        this.element = element;
        nextNode = null;
        prevNode = null;
    }

    /**
     * Returns the node that follows this one.
     * 
     * @return the node that follows the current one
     */
    public Node<T> getNextNode() {
        return nextNode;
    }

    /**
     * Create Node with the given element and next Node.
     * 
     * @param element
     * @param nextNode
     */
    public Node(T element, Node<T> nextNode) {
        this.element = element;
        this.nextNode = nextNode;
    }
    /////////////////
    /** FIX LATER//
     * /////////////
     * Returns the element stored in this node.
     * 
     * @return the element stored in this node
     */
    public T getPrevNode() {
        return prevNode;
    }

    ///////////////
    /**FIX LATER///
     * ////////////
     * Sets the element stored in this node.
     * 
     * @param element the element to be stored in this node
     */
    public void setPrevNode(T element) {
        this.prevNode = prevNode;
    }
    /**
     * Sets the node that follows this one.
     * 
     * @param nextNode the node to be set to follow the current one
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * Returns the element stored in this node.
     * 
     * @return the element stored in this node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the element stored in this node.
     * 
     * @param element the element to be stored in this node
     */
    public void setElement(T element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "Element: " + element.toString() + " Has next: " + (nextNode != null);
    }
}
