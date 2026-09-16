package structures;

/**
 * UserRatingStats is a class that represents statistics for a user's ratings.
 * It stores information about the user ID, a list of ratings, the total rating, and the number of ratings.
 * 
 */
public class UserRatingStats implements Comparable<UserRatingStats> {
    public int userID;
    public LinkedList<Float> ratings;
    public float totalRating;
    public int numRatings;

    public UserRatingStats(int userID) {
        this.userID = userID;
        this.ratings = new LinkedList<>();
        this.totalRating = 0.0f;
        this.numRatings = 0;
    }

    public void addRating(RatingRecord record) {
        ratings.add(record.rating);
        totalRating += record.rating;
        numRatings++;
    }
    
    public boolean removeRating(RatingRecord record) {
        boolean removed = ratings.remove(record.rating);

        if (removed) {
            totalRating -= record.rating;
            numRatings--;
        }

        return removed;
    }

    @Override
    public int compareTo(UserRatingStats other) {
        return Integer.compare(other.numRatings, this.numRatings);
    }
}
