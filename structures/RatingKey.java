package structures;

/**
 * RatingKey is a class that represents a unique combination of userID and movieID.
 * It is used as a key in the ratingsMap to store and retrieve RatingRecord objects efficiently.
 * 
 */
public class RatingKey implements Comparable<RatingKey> {
    public int userID;
    public int movieID;

    public RatingKey(int userID, int movieID) {
        this.userID = userID;
        this.movieID = movieID;
    }

    @Override
    public int compareTo(RatingKey other) {
        if (this.userID != other.userID) {
            return Integer.compare(this.userID, other.userID);
        }
        return Integer.compare(this.movieID, other.movieID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RatingKey)) {
            return false;
        }
        RatingKey other = (RatingKey) obj;
        return this.userID == other.userID && this.movieID == other.movieID;
    }

    @Override
    public int hashCode() {
        return 31 * userID + movieID;
    }
}