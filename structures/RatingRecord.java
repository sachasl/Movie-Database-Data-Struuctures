package structures;
import java.time.LocalDateTime;

/**
 * RatingRecord is a class that represents a single rating given by a user to a movie.
 * It contains the user ID, movie ID, rating value, and timestamp of the rating.
 * 
 */
public class RatingRecord {
    public int userID;
    public int movieID;
    public float rating;
    public LocalDateTime timestamp;

    public RatingRecord(int userID, int movieID, float rating, LocalDateTime timestamp) {
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.timestamp = timestamp;
    }
}
