package stores;

import java.time.LocalDateTime;

import interfaces.IRatings;
import structures.*;

public class Ratings implements IRatings {
    Stores stores;
    private HashMap<RatingKey, RatingRecord> ratingsByPair;
    private HashMap<Integer, MovieRatingStats> movieStatsByID;
    private HashMap<Integer, UserRatingStats> userStatsByID;
    private int size;

    /**
     * The constructor for the Ratings data store. This is where you should
     * initialise your data structures.
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Ratings(Stores stores) {
        this.stores = stores;
        this.ratingsByPair = new HashMap<>();
        this.movieStatsByID = new HashMap<>();
        this.userStatsByID = new HashMap<>();
        this.size = 0;
    }

    /**
     * Adds a rating to the data structure. The rating is made unique by its user ID
     * and its movie ID
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The rating gave to the film by this user (between 0 and 5
     *                  inclusive)
     * @param timestamp The time at which the rating was made
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int userid, int movieid, float rating, LocalDateTime timestamp) {
        // Validate input
        if (rating < 0.0f || rating > 5.0f || timestamp == null) {
            return false;
        }

        RatingKey key = new RatingKey(userid, movieid);
        // Check if key already exists
        if (ratingsByPair.get(key) != null) {
            return false;
        }

        // Create record
        RatingRecord record = new RatingRecord(userid, movieid, rating, timestamp);
        ratingsByPair.add(key, record);

        // Update movie stats
        MovieRatingStats movieRatingStats = movieStatsByID.get(movieid);
        if (movieRatingStats == null) {
            movieRatingStats = new MovieRatingStats(movieid);
            movieStatsByID.add(movieid, movieRatingStats);
        }
        movieRatingStats.addRating(record);

        // Update user stats
        UserRatingStats userRatingStats = userStatsByID.get(userid);
        if (userRatingStats == null) {
            userRatingStats = new UserRatingStats(userid);
            userStatsByID.add(userid, userRatingStats);
        }
        userRatingStats.addRating(record);

        size++;
        return true;
    }

    /**
     * Removes a given rating, using the user ID and the movie ID as the unique
     * identifier
     * 
     * @param userID  The user ID
     * @param movieID The movie ID
     * @return TRUE if the data was removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int userid, int movieid) {
        RatingKey key = new RatingKey(userid, movieid);

        // Remove the rating record from the main index
        RatingRecord record = ratingsByPair.remove(key);

        if (record == null) {
            return false;
        }

        // Update movie stats
        MovieRatingStats movieRatingStats = movieStatsByID.get(movieid);
        if (movieRatingStats != null) {
            movieRatingStats.removeRating(record);

            if (movieRatingStats.numRatings == 0) {
                movieStatsByID.remove(movieid);
            }
        }

        // Update user stats
        UserRatingStats userRatingStats = userStatsByID.get(userid);
        if (userRatingStats != null) {
            userRatingStats.removeRating(record);

            if (userRatingStats.numRatings == 0) {
                userStatsByID.remove(userid);
            }
        }

        size--;
        return true;
    }

    /**
     * Sets a rating for a given user ID and movie ID. Therefore, should the given
     * user have already rated the given movie, the new data should overwrite the
     * existing rating. However, if the given user has not already rated the given
     * movie, then this rating should be added to the data structure
     * 
     * @param userID    The user ID
     * @param movieID   The movie ID
     * @param rating    The new rating to be given to the film by this user (between
     *                  0 and 5 inclusive)
     * @param timestamp The time at which the new rating was made
     * @return TRUE if the data able to be added/updated, FALSE otherwise
     */
    @Override
    public boolean set(int userid, int movieid, float rating, LocalDateTime timestamp) {
        // Validate input
        if (rating < 0.0f || rating > 5.0f || timestamp == null) {
            return false;
        }

        RatingKey key = new RatingKey(userid, movieid);

        // If rating already exists
        if (ratingsByPair.get(key) != null) {
            remove(userid, movieid);
        }

        return add(userid, movieid, rating, timestamp);
    }

    /**
     * Get all the ratings for a given film
     * 
     * @param movieID The movie ID
     * @return An array of ratings. If there are no ratings or the film cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getMovieRatings(int movieid) {
        MovieRatingStats stats = movieStatsByID.get(movieid);

        if (stats == null || stats.ratings.isEmpty()) {
            return new float[0];
        }

        return stats.ratings.toFloatArray();
    }

    /**
     * Get all the ratings for a given user
     * 
     * @param userID The user ID
     * @return An array of ratings. If there are no ratings or the user cannot be
     *         found in Ratings, then return an empty array
     */
    @Override
    public float[] getUserRatings(int userid) {
        UserRatingStats stats = userStatsByID.get(userid);

        if (stats == null || stats.ratings.isEmpty()) {
            return new float[0];
        }

        return stats.ratings.toFloatArray();
    }

    /**
     * Get the average rating for a given film
     * 
     * @param movieID The movie ID
     * @return Produces the average rating for a given film. 
     *         If the film cannot be found in Ratings, but does exist in the Movies store, return 0.0f. 
     *         If the film cannot be found in Ratings or Movies stores, return -1.0f.
     */
    @Override
    public float getMovieAverageRating(int movieid) {
        MovieRatingStats stats = movieStatsByID.get(movieid);
        
        if (stats != null && stats.numRatings > 0) {
            return stats.totalRating / stats.numRatings;
        }

        if (stores.getMovies().getTitle(movieid) != null) {
            return 0.0f;
        }

        return -1.0f;
    }

    /**
     * Get the average rating for a given user
     * 
     * @param userID The user ID
     * @return Produces the average rating for a given user. If the user cannot be
     *         found in Ratings, or there are no rating, return -1.0f
     */
    @Override
    public float getUserAverageRating(int userid) {
        UserRatingStats stats = userStatsByID.get(userid);

        if (stats != null && stats.numRatings > 0) {
            return stats.totalRating / stats.numRatings;
        }

        return -1.0f;
    }

    /**
     * Gets the top N movies with the most ratings, in order from most to least
     * 
     * @param num The number of movies that should be returned
     * @return A sorted array of movie IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in Ratings
     */
    @Override
    public int[] getMostRatedMovies(int num) {
        if (num <= 0) {
        return new int[0];
        }

        Integer[] ids = movieStatsByID.keys();
        MovieRatingStats[] statsArray = new MovieRatingStats[ids.length];

        for (int i = 0; i < ids.length; i++) {
            statsArray[i] = movieStatsByID.get(ids[i]);
        }

        // Use quicksort to sort the stats array in descending order by numRatings
        if (statsArray.length > 1) {
            QuickSorter.quickSort(statsArray, 0, statsArray.length - 1);
        }

        int resultSize = Math.min(num, statsArray.length);
        int[] result = new int[resultSize];

        for (int i = 0; i < resultSize; i++) {
            result[i] = statsArray[i].movieID;
        }

        return result;
    }

    /**
     * Gets the top N users with the most ratings, in order from most to least
     * 
     * @param num The number of users that should be returned
     * @return A sorted array of user IDs with the most ratings. The array should be
     *         no larger than num. If there are less than num users in the store,
     *         then the array should be the same length as the number of users in Ratings
     */
    @Override
    public int[] getMostRatedUsers(int num) {
        if (num <= 0) {
            return new int[0];
        }

        Integer[] ids = userStatsByID.keys();
        UserRatingStats[] statsArray = new UserRatingStats[ids.length];

        for (int i = 0; i < ids.length; i++) {
            statsArray[i] = userStatsByID.get(ids[i]);
        }

        // Use quicksort to sort the stats array in descending order by numRatings
        if (statsArray.length > 1) {
            QuickSorter.quickSort(statsArray, 0, statsArray.length - 1);
        }

        int resultSize = Math.min(num, statsArray.length);
        int[] result = new int[resultSize];

        for (int i = 0; i < resultSize; i++) {
            result[i] = statsArray[i].userID;
        }

        return result;
    }

    /**
     * Get the number of ratings that a movie has
     * 
     * @param movieid The movie id to be found
     * @return The number of ratings the specified movie has. 
     *         If the movie exists in the Movies store, but there are no ratings for it, then return 0. 
     *         If the movie does not exist in the Ratings or Movies store, then return -1.
     */
    @Override
    public int getNumRatings(int movieid) {
        MovieRatingStats stats = movieStatsByID.get(movieid);

        if (stats != null) {
            return stats.numRatings;
        }

        // Check if movie exists in Movies store
        if (stores.getMovies().getTitle(movieid) != null) {
            return 0;
        }

        return -1;
    }

    /**
     * Get the highest average rated film IDs, in order of there average rating
     * (hightst first).
     * 
     * @param numResults The maximum number of results to be returned
     * @return An array of the film IDs with the highest average ratings, highest
     *         first. If there are less than num movies in the store,
     *         then the array should be the same length as the number of movies in Ratings
     */
    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        if (numResults <= 0) {
            return new int[0];
        }

        Integer[] ids = movieStatsByID.keys();
        MovieAverageRating[] statsArray = new MovieAverageRating[ids.length];

        // Calculate average ratings for each movie in stats array
        for (int i = 0; i < ids.length; i++) {
            MovieRatingStats movieStats = movieStatsByID.get(ids[i]);
            float average = movieStats.totalRating / movieStats.numRatings;
            statsArray[i] = new MovieAverageRating(movieStats.movieID, average);
        }

        // Use quicksort to sort the stats array in descending order by average rating
        if (statsArray.length > 1) {
            QuickSorter.quickSort(statsArray, 0, statsArray.length - 1);
        }

        int resultSize = Math.min(numResults, statsArray.length);
        int[] result = new int[resultSize];

        for (int i = 0; i < resultSize; i++) {
            result[i] = statsArray[i].movieID;
        }

        return result;
    }

    @Override
    public int size() {
        return size;
    }

}
