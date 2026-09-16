package structures;

import interfaces.IMap;

// This line allows us to cast our object to type (E) without any warnings.
@SuppressWarnings("unchecked")
public class HashMap<K extends Comparable<K>,V> implements IMap<K,V> {

    protected KeyValuePairLinkedList<K,V>[] table;
    protected int size;
    
    /**
     * Constructs a HashMap with a default table size.
     */
    public HashMap() {
        /* for very simple hashing, primes reduce collisions */
        this(11);
    }
    
    /**
     * Constructs a HashMap with the specified table size
     *
     * @param size the number of buckets in the hash table
     */
    public HashMap(int tableSize) {
        table = (KeyValuePairLinkedList<K,V>[]) new KeyValuePairLinkedList[tableSize];
        initTable();
        this.size = 0;
    }

    /**
     * Returns the number of comparisons needed to search for a given key in the hash table bucket where it would be stored
     *
     * @param key the key to search for
     * @return the number of comparisons performed while searching for the key
     */
    public int find(K key) {
        int hash_code = hash(key);
        int location = (hash_code & 0x7fffffff) % table.length;
        int comparisons = 0;

        KeyValuePairLinkedList<K,V> list = table[location];
        ListElement<KeyValuePair<K,V>> current = list.getHead();

        while (current != null) {
            comparisons++;
            if(current.getValue().getKey().equals(key)) return comparisons;
            current = current.getNext();
        }
        return comparisons;

    }
    
    /**
     * Initialises every bucket in the hash table.
     */
    protected void initTable() {
        for(int i = 0; i < table.length; i++) {
            table[i] = new KeyValuePairLinkedList<>();
        }
    }
    
    /**
     * Computes the hash code for a given key.
     *
     * @param key the key to hash
     * @return the hash code of the key
     */
    protected int hash(K key) {
        int code = key.hashCode();
        return code;    
    }
    
    /**
     * Adds a key-value pair to the hash table.
     *
     *
     * @param key the key to store
     * @param value the value associated with the key
     */
    public void add(K key, V value) {
        int hash_code = hash(key);
        int location = (hash_code & 0x7fffffff) % table.length;
        
        //System.out.println("Adding " + value + " under key " + key + " at location " + location);
        
        table[location].add(key,value);
        size++;
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key the key to search for
     * @return the value associated with the key, or null if the key is not found
     */
    public V get(K key) {
        int hash_code = hash(key);
        int location = (hash_code & 0x7fffffff) % table.length;
        
        KeyValuePair<K,V> ptr = table[location].get(key);
        
        if (ptr == null) {
            return null;
        }

        return (V) ptr.getValue();
    }

    /**
     * Removes the value associated with the specified key from the hash map.
     * 
     * @param key the key to remove
     * @return the value that was removed, or null if the key was not found
     */
    public V remove(K key) {
        int hash_code = hash(key);
        int location = (hash_code & 0x7fffffff) % table.length;

        KeyValuePair<K,V> removed = table[location].remove(key);
        if (removed == null) {
            return null;
        }
        size --;
        return removed.getValue();
    }

    /**
     * Returns all keys currently stored in the hash map.
     *
     * @return an array containing all keys in the map
     */
    public Integer[] keys() {
        Integer[] keys = new Integer[this.size];
        int index = 0;

        for (int i = 0; i < table.length; i++) {
            ListElement<KeyValuePair<K,V>> current = table[i].getHead();

            while (current != null) {
                keys[index] = (Integer) current.getValue().getKey();
                index++;
                current = current.getNext();
            }
        }
        return keys;
    }

}
