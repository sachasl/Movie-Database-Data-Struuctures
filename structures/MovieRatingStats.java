package structures;

/**
 * MovieRatingStats is a class that represents statistics for a movie's ratings.
 * It stores information about the movie ID, a list of ratings, the total rating, and the number of ratings.
 * 
 */
public class MovieRatingStats implements Comparable<MovieRatingStats> {
    public int movieID;
    public LinkedList<Float> ratings;
    public float totalRating;
    public int numRatings;

    public MovieRatingStats(int movieID) {
        this.movieID = movieID;
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
    public int compareTo(MovieRatingStats other) {
        return Integer.compare(other.numRatings, this.numRatings);
    }
}
