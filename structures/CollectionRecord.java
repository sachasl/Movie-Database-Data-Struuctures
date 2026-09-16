package structures;

/**
 * Represents a film collection, which is a group of related films.
 * 
 */ 
public class CollectionRecord {
    public int collectionID;
    public String collectionName;
    public String collectionPosterPath;
    public String collectionBackdropPath;
    public MyArrayList<Integer> filmIDs;

    public CollectionRecord(int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        this.collectionID = collectionID;
        this.collectionName = collectionName;
        this.collectionPosterPath = collectionPosterPath;
        this.collectionBackdropPath = collectionBackdropPath;
        this.filmIDs = new MyArrayList<>();
    }
}