package structures;

public class KeyValuePairLinkedList<K extends Comparable<K>,V> {

    protected ListElement<KeyValuePair<K,V>> head;
    protected int size;
    
    public KeyValuePairLinkedList() {
        head = null;
        size = 0;
    }
    
    /**
     * Adds a new key-value pair to the list using the provided key and value
     *
     * @param key the key to add
     * @param value the value associated with the key
     */
    public void add(K key, V value) {
        this.add(new KeyValuePair<K,V>(key,value));
    }

    /**
     * Adds a KeyValuePair object to the front of the linked list.
     *
     * @param kvp the key-value pair to add to the list
     */
    public void add(KeyValuePair<K,V> kvp) {
        ListElement<KeyValuePair<K,V>> new_element = 
                new ListElement<>(kvp);
        new_element.setNext(head);
        head = new_element;
        size++;
    }
    
    /**
     * Returns the number of key-value pairs stored in the list.
     *
     * @return the size of the list
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the head node of the linked list.
     *
     * @return the head ListElement of the list
     */    
    public ListElement<KeyValuePair<K,V>> getHead() {
        return head;
    }
    
    /**
     * Retrieves the KeyValuePair associated with the specified key.
     *
     *
     * @param key the key to search for
     * @return the KeyValuePair associated with the key, or null if not found
     */
    public KeyValuePair<K,V> get(K key) {
        ListElement<KeyValuePair<K,V>> temp = head;
        
        while(temp != null) {
            if(temp.getValue().getKey().equals(key)) {
                return temp.getValue();
            }
            
            temp = temp.getNext();
        }
        
        return null;
    }

    /**
     * Removes the key-value pair with the specified key from the linked list.
     *
     * @param key the key to remove
     * @return the removed KeyValuePair, or null if the key was not found
     */
    public KeyValuePair<K,V> remove(K key) {
        ListElement<KeyValuePair<K,V>> current = head;
        ListElement<KeyValuePair<K,V>> previous = null;
        while (current!= null) {
            if (current.getValue().getKey().equals(key)) {
                if (previous == null) {
                    head = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                size --;
                return current.getValue();
            }
            previous = current;
            current = current.getNext();
        }
        return null;
    }
}
