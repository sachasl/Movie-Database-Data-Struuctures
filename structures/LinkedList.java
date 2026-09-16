package structures;

import interfaces.IList;

public class LinkedList<E> implements IList<E> {
    ListElement<E> head = null;

    public LinkedList() {

    }

    /** 
     * Get an item at a specified index
     * 
     * @param index 
     * @return element at the specified index, and null if index is not found
     */
   public E get(int index) {
      if (!this.isEmpty() && index < this.size()) {
         ListElement<E> ptr = this.head;

         for(int i = 0; i < index; ++i) {
            ptr = ptr.getNext();
         }

         return (E)ptr.getValue();
      } else {
         return null;
      }
   }

   /** 
    * Get the index of the first occurence of an element
    * 
    * @param element
    * @return index of the specified elements first occurence, or -1 if it is not found
    */
   public int indexOf(E element) {
      ListElement<E> ptr = this.head;

      for(int i = 0; ptr != null; ptr = ptr.getNext()) {
         if (element.equals(ptr.getValue())) {
            return i;
         }

         ++i;
      }

      return -1;
   }

   /**
    * Update the element at the index given to the given element
    * 
    * @param index 
    * @param element
    * @return the element previous stored at the index, or null if the list is empty
    */
   public E set(int index, E element) {
      if (this.isEmpty()) {
         return null;
      } else {
         ListElement<E> ptr = this.head;
         ListElement<E> prev = null;

         for(int i = 0; i < index; ++i) {
            prev = ptr;
            ptr = ptr.getNext();
         }

         E ret = (E)ptr.getValue();
         ListElement<E> newLink = new ListElement<>(element);
         newLink.setNext(ptr.getNext());
         if (prev != null) {
            prev.setNext(newLink);
         } else {
            this.head = newLink;
         }

         return ret;
      }
   }


   /**
    * Adds new element to the front of the list
    * 
    * @param element element to add to the list
    * @return true once the element has been added
    */
    public boolean add(E element) {
        ListElement<E> temp = new ListElement<>(element);
        
        // if the list is not empty, point the new link to head
        if (head != null) {
            temp.setNext(head);
        }
        // update the head
        head = temp;

        return true;
    }

    /**
     * Removes all elements from the linked list
     */
    public void clear() {
        head = null;
    }

    /**
     * Checks whether the linked list contains the specified element
     * 
     * @param element
     * @return true if the element exists in the list, false otherwise
     */
    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    /**
     * Checks whether the linked list is empty
     * 
     * @return true if the list contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Removes the first occurence of the specified element from the linked list
     * 
     * @param element the element to remove from the list
     * @return true if the element was found and removed, false otherwise
     */
    public boolean remove(E element) {
        ListElement<E> ptr = head;
        ListElement<E> prev = null;

        while (ptr != null) {
            if (ptr.getValue().equals(element)) {
                if (prev == null) {
                    head = ptr.getNext();
                } else {
                    prev.setNext(ptr.getNext());
                }

                return true;
            }

            prev = ptr;
            ptr = ptr.getNext();
        }

        return false;
    }

    /**
     * Returns the number of elements currently stored in the linked list
     * 
     * @return the number of elements in the list
     */
    public int size() {
        if (isEmpty()) return 0;
        ListElement<E> ptr = head;
        int i=1;
        while (ptr.getNext() != null) {
            i++;
            ptr = ptr.getNext();
        }
        return i;
    }
    
    /**
     * Returns a string representation of the linked list
     * 
     * @return a string representation of the list
     * 
     */
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        String ret = "";
        ListElement<E> ptr = head;

        while (ptr.getNext() != null) {
            ret += ptr.getValue()+", ";
            ptr = ptr.getNext();
        }

        ret += ptr.getValue();
        ret = "[" + ret + "]";
        return ret;
    }
    
    /**
     * Returns the head element of the linked list
     * @return head
     */
    public ListElement<E> getHead() {
        return head;
    }

    /**
     * Copies the contents of this linked list into the given array.
     * When reverse is false, elements are written in traversal order.
     * When reverse is true, elements are written in reverse traversal order.
     *
     * @param result the array to fill
     * @param reverse whether to reverse the output order
     */
    public void toArray(E[] result, boolean reverse) {
        ListElement<E> current = head;
        int index;
        if (reverse) {
            index = result.length - 1;
        } else {
            index = 0;
        }

        while (current != null) {
            result[index] = current.getValue();
            current = current.getNext();
            if (reverse) {
                index--;
            } else {
                index++;
            }
        }
    }

    /**
     * Copies the contents of this linked list into the given array.
     * Prevents having to traverse list again to change data type to Float.
     * 
     * @return
     */
    public float[] toFloatArray() {
        float[] result = new float[size()];
        ListElement<E> current = head;
        int index = 0;

        while (current != null) {
            result[index] = (Float) current.getValue();
            current = current.getNext();
            index++;
        }

        return result;
    }

    /**
     * Copies the contents of this linked list into the given array.
     * When reverse is false, elements are written in traversal order.
     * When reverse is true, elements are written in reverse traversal order.
     * Prevents having to traverse list again to change data type to int.
     * 
     * @param reverse
     * @return
     */
    public int[] toIntArray(boolean reverse) {
        int size = size();
        int[] result = new int[size];

        ListElement<E> current = head;
        int index;
        if (reverse) {
            index = size - 1;
        } else {
            index = 0;
        }

        while (current != null) {
            result[index] = (Integer) current.getValue();
            current = current.getNext();

            if (reverse) {
                index--;
            } else {
                index++;
            }
        }

        return result;
    }
}
