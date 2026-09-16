package structures;

public class ListElement<E> {

    private final E value;    // Value stored in the node
    private ListElement<E> next;    // Reference to the next node
    private ListElement<E> prev;    // Reference to the previous node

    /**
     * Constructs a new listElement node storing the given value
     * @param value
     */
    public ListElement(E value) {
        this.value = value;
    }

    /**
     * Returns the value stored in the node
     * @return the element stored at the current node
     */
    public E getValue() {
        return this.value;
    }

    /**
     * Returns the next node in the linked list.
     * @return the next listElement in the list, or null if it's the last node
     */
    public ListElement<E> getNext() {
        return this.next;
    }

    /**
     * Returns the previous node in the linked list.
     * @return previous element in the list, or null if it's the first node
     */
    public ListElement<E> getPrev() {
        return this.prev;
    }

    /**
     * Sets reference to the next node in the list
     * @param e
     */
    public void setNext(ListElement<E> e) {
        this.next = e;
    }

    /**
     * Sets reference to the previous node in the linked list
     * @param e
     */
    public void setPrev(ListElement<E> e) {
        this.prev = e;
    }

}
