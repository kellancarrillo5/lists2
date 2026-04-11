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
     * Create node with the given element.
     * 
     * @param element the element to store in this node
     */
    public Node(T element) {
        this.element = element;
        nextNode = null;
        prevNode = null;
    }

    /**
     * Creates a node with the given element and next node reference while prevNode is set to null.
     * 
     * @param element  the element to store
     * @param nextNode the node that follows this one
     */
    public Node(T element, Node<T> nextNode) {
        this.element = element;
        this.nextNode = nextNode;
        this.prevNode = null;
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
     * Sets the node that follows this one.
     * 
     * @param nextNode the node to be set to follow the current one
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * Returns the node that is before this one.
     * 
     * @return the previous node, or null if none
     */
    public Node<T> getPrevNode() {
        return prevNode;
    }

    /**
     * Sets the node that is before this one.
     * 
     * @param prevNode the node to precede this one
     */
    public void setPrevNode(Node<T> prevNode) {
        this.prevNode = prevNode;
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
