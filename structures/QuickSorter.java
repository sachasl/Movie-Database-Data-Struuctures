package structures;

/**
 * Provides a shared generic quicksort implementation for arrays of comparable objects.
 */
public class QuickSorter {

    /**
     * Recursively sorts a subrange of the array using quicksort.
     *
     * @param arr the array to sort
     * @param begin the start index
     * @param end the end index
     * @param <T> the element type
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    /**
     * Partitions the array around a pivot element.
     *
     * @param arr the array to partition
     * @param begin the start index
     * @param end the end index
     * @param <T> the element type
     * @return the final pivot index
     */
    private static <T extends Comparable<T>> int partition(T[] arr, int begin, int end) {
        T pivot = arr[end];
        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            if (arr[j].compareTo(pivot) < 0) {
                i++;
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        T temp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = temp;

        return i + 1;
    }
}