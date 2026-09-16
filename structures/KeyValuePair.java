package structures;

public class KeyValuePair<K extends Comparable<K>,V> implements Comparable<KeyValuePair<K,V>> {

    protected K key;
    protected V value;
    
    /**
     * Constructs a KeyValuePair with the given key and value
     *
     * @param k the key
     * @param v the value associated with the key
     */
    public KeyValuePair(K k, V v) {
        key = k;
        value = v;
    }
    
    /**
     * Returns the key of this key-value pair
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }
    
    /**
     * Returns the value associated with the key
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Compares this KeyValuePair with another based on their keys.
     *
     * @param o the other KeyValuePair to compare with
     * @return a negative integer, zero, or a positive integer as this object
     *         is greater than, equal to, or less than the specified object
     */
    public int compareTo(KeyValuePair<K,V> o) {
        return o.getKey().compareTo(this.getKey());
    }
}
