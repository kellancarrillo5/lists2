/**
 * Single-linked node for linear data stuctures
 * @author mvail, CS221-1 Sp26, kellancarrillo5
 */
public class Node<T> {
    private Node<T> nextNode;
    private T element; 

    /**
     * Create Node with the Given element
     * @param element
     */
    public Node(T element){
        this.element = element;
        nextNode = null;
    }

    /**
     * Create Node with the given element and next Node.
     * @param element
     * @param nextNode
     */
    public Node(T element, Node<T> nextNode){
        this.element = element;
        this.nextNode = nextNode;
    }

    /**
     * 
     * @return
     */
    public Node<T> getNextNode() {
        return nextNode;
    }

    /**
     * 
     * @param nextNode
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * 
     * @return
     */
    public T getElement() {
        return element;
    }

    /**
     * 
     * @param element
     */
    public void setElement(T element) {
        this.element = element;
    }

    
}
