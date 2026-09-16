package structures;

/**
 * Represents a movie and its average rating, used for sorting movies by average rating.
 * Implements Comparable to allow sorting by average rating in descending order.
 * 
 */
public class MovieAverageRating implements Comparable<MovieAverageRating> {
    public int movieID;
    public float averageRating;

    public MovieAverageRating(int movieID, float averageRating) {
        this.movieID = movieID;
        this.averageRating = averageRating;
    }

    @Override
    public int compareTo(MovieAverageRating other) {
        // descending by average rating
        return Float.compare(other.averageRating, this.averageRating);
    }
}