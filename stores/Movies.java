package stores;

import java.time.LocalDate;

import interfaces.IMovies;
import structures.*;

public class Movies implements IMovies{
    Stores stores;
    // Creating hashmap to store movies
    private HashMap<Integer, Movie> movies;
    // Creating hashmap to store movie collections
    private HashMap<Integer, CollectionRecord> collectionsByID;
    // Size variable to store size of hashmap
    private int size;

    /**
     * The constructor for the Movies data store. This is where you should
     * initialise your data structures.
     * @param stores An object storing all the different key stores,
     *               including itself
     */
    public Movies(Stores stores) {
        this.stores = stores;
        // Initialise hashmaps
        this.movies = new HashMap<>();  
        this.collectionsByID = new HashMap<>();
        this.size = 0;
    }

    /**
     * Adds data about a film to the data structure
     * 
     * @param id               The unique ID for the film
     * @param title            The English title of the film
     * @param originalTitle    The original language title of the film
     * @param overview         An overview of the film
     * @param tagline          The tagline for the film (empty string if there is no
     *                         tagline)
     * @param status           Current status of the film
     * @param genres           An array of Genre objects related to the film
     * @param release          The release date for the film
     * @param budget           The budget of the film in US Dollars
     * @param revenue          The revenue of the film in US Dollars
     * @param languages        An array of ISO 639 language codes for the film
     * @param originalLanguage An ISO 639 language code for the original language of
     *                         the film
     * @param runtime          The runtime of the film in minutes
     * @param homepage         The URL to the homepage of the film
     * @param adult            Whether the film is an adult film
     * @param video            Whether the film is a "direct-to-video" film
     * @param poster           The unique part of the URL of the poster (empty if
     *                         the URL is not known)
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        // Reject duplicate movie IDs
        if (movies.get(id) != null) {
            return false;
        }
        // Add Movie object to hashmap
        Movie movie = new Movie(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue, languages, originalLanguage, runtime, homepage, adult, video, poster);   // Create new movie as object
        movies.add(id, movie);
        size++;
        return true;
    }

    /**
     * Removes a film from the data structure, and any data
     * added through this class related to the film
     * 
     * @param id The film ID
     * @return TRUE if the film has been removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        Movie removed = movies.remove(id);  // Remove movie from hashmap
        if (removed == null) {
            return false;
        }
        // Remove film from collection index if it belonged to a collection
        if (removed.collectionID != -1) {
            CollectionRecord collectionRecord = collectionsByID.get(removed.collectionID);
            
            if (collectionRecord != null) {
                collectionRecord.filmIDs.remove(id);
                // If no films remain in the collection, remove the collection entry
                if (collectionRecord.filmIDs.isEmpty()) {
                    collectionsByID.remove(removed.collectionID);
                }
            }
        }
        size --;
        return true;
    }

    /**
     * Gets all the IDs for all films
     * 
     * @return An array of all film IDs stored
     */
    @Override
    public int[] getAllIDs() {
        Integer[] ids = movies.keys();  // Get all ids from hashmap
        int[] result = new int[ids.length];
        // Transfer ids into a integer array
        for (int i = 0; i < ids.length; i++) {
            result[i] = ids[i];
        }
        return result;
    }

    /**
     * Finds the film IDs of all films released within a given range. If a film is
     * released either on the start or end dates, then that film should not be
     * included
     * 
     * @param start The start point of the range of dates
     * @param end   The end point of the range of dates
     * @return An array of film IDs that were released between start and end
     */
    @Override
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end) {
        int[] ids = getAllIDs();
        int count = 0;
        // Count how many films are strictly within the range
        for (int i = 0; i < ids.length; i++) {
            Movie movie = movies.get(ids[i]);

            if (movie != null && movie.release != null && movie.release.isAfter(start) && movie.release.isBefore(end)) {
                count++;
            }
        }
        // Create result array with exact size
        int[] result = new int[count];
        int index = 0;
        // Fill the result array
        for (int i = 0; i < ids.length; i++) {
            Movie movie = movies.get(ids[i]);
            
            if (movie != null && movie.release != null && movie.release.isAfter(start) && movie.release.isBefore(end)) {
                result[index] = ids[i];
                index++;
            }
        }
        return result;
    }

    /**
     * Gets the title of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The title of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTitle(int id) {
        Movie movie = movies.get(id);
        if(movie == null) {
            return null;
        }
        return movie.title;
    }

    /**
     * Gets the original title of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original title of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalTitle(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.originalTitle;
    }

    /**
     * Gets the overview of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The overview of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getOverview(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.overview;
    }

    /**
     * Gets the tagline of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The tagline of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTagline(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.tagline;
    }

    /**
     * Gets the status of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The status of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getStatus(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.status;
    }

    /**
     * Gets the genres of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The genres of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public Genre[] getGenres(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.genres;
    }

    /**
     * Gets the release date of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The release date of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public LocalDate getRelease(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.release;
    }

    /**
     * Gets the budget of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The budget of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getBudget(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1;
        }
        return movie.budget;
    }

    /**
     * Gets the revenue of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The revenue of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getRevenue(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1;
        }
        return movie.revenue;
    }

    /**
     * Gets the languages of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The languages of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String[] getLanguages(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.languages;
    }

    /**
     * Gets the original language of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original language of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalLanguage(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.originalLanguage;
    }

    /**
     * Gets the runtime of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The runtime of the requested film. If the film cannot be found, then
     *         return -1.0d
     */
    @Override
    public double getRuntime(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1.0d;
        }
        return movie.runtime;
    }

    /**
     * Gets the homepage of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The homepage of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getHomepage(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.homepage;
    }

    /**
     * Gets weather a particular film is classed as "adult", given the ID number of
     * that film
     * 
     * @param id The movie ID
     * @return The "adult" status of the requested film. If the film cannot be
     *         found, then return false
     */
    @Override
    public boolean getAdult(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        return movie.adult;
    }

    /**
     * Gets weather a particular film is classed as "direct-to-video", given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The "direct-to-video" status of the requested film. If the film
     *         cannot be found, then return false
     */
    @Override
    public boolean getVideo(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        return movie.video;
    }

    /**
     * Gets the poster URL of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The poster URL of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String getPoster(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }
        return movie.poster;
    }

    /**
     * Sets the average IMDb score and the number of reviews used to generate this
     * score, for a particular film
     * 
     * @param id          The movie ID
     * @param voteAverage The average score on IMDb for the film
     * @param voteCount   The number of reviews on IMDb that were used to generate
     *                    the average score for the film
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        movie.voteAverage = voteAverage;
        movie.voteCount = voteCount;
        return true;
    }

    /**
     * Gets the average score for IMDb reviews of a particular film, given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The average score for IMDb reviews of the requested film. If the film
     *         cannot be found, then return -1.0d
     */
    @Override
    public double getVoteAverage(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1.0d;
        }
        return movie.voteAverage;
    }

    /**
     * Gets the amount of IMDb reviews used to generate the average score of a
     * particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The amount of IMDb reviews used to generate the average score of the
     *         requested film. If the film cannot be found, then return -1
     */
    @Override
    public int getVoteCount(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1;
        }
        return movie.voteCount;
    }

    /**
     * Adds a given film to a collection. The collection is required to have an ID
     * number, a name, and a URL to a poster for the collection
     * 
     * @param filmID                 The movie ID
     * @param collectionID           The collection ID
     * @param collectionName         The name of the collection
     * @param collectionPosterPath   The URL where the poster can
     *                               be found
     * @param collectionBackdropPath The URL where the backdrop can
     *                               be found
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return false;
        }
        // Assign correct attributes
        movie.collectionID = collectionID;
        movie.collectionName = collectionName;
        movie.collectionPosterPath = collectionPosterPath;
        movie.collectionBackdropPath = collectionBackdropPath;
        // Create new collection record object
        CollectionRecord record = collectionsByID.get(collectionID);
        if (record == null) {
            record = new CollectionRecord(collectionID, collectionName, collectionPosterPath, collectionBackdropPath);
            collectionsByID.add(collectionID, record);
        }
        // Only add if film isn't already in collection
        if (!record.filmIDs.contains(filmID)) {
            record.filmIDs.add(filmID);
        }
        return true;
    }

    /**
     * Get all films that belong to a given collection
     * 
     * @param collectionID The collection ID to be searched for
     * @return An array of film IDs that correspond to the given collection ID. If
     *         there are no films in the collection ID, or if the collection ID is
     *         not valid, return an empty array.
     */
    @Override
    public int[] getFilmsInCollection(int collectionID) {
        CollectionRecord collectionRecord = collectionsByID.get(collectionID);
        if (collectionRecord == null || collectionRecord.filmIDs.isEmpty()) {
            return new int[0];
        }
        // Iterate through collection to fill array
        int[] result = new int[collectionRecord.filmIDs.size()];
        for (int i = 0; i < collectionRecord.filmIDs.size(); i++) {
            result[i] = collectionRecord.filmIDs.get(i);
        }
        return result;
    }

    /**
     * Gets the name of a given collection
     * 
     * @param collectionID The collection ID
     * @return The name of the collection. If the collection cannot be found, then
     *         return null
     */
    @Override
    public String getCollectionName(int collectionID) {
        CollectionRecord collectionRecord = collectionsByID.get(collectionID);
        if (collectionRecord == null) {
            return null;
        }
        return collectionRecord.collectionName;
    }

    /**
     * Gets the poster URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The poster URL of the collection. If the collection cannot be found,
     *         then return null
     */
    @Override
    public String getCollectionPoster(int collectionID) {
        CollectionRecord collectionRecord = collectionsByID.get(collectionID);
        if (collectionRecord == null) {
            return null;
        }
        return collectionRecord.collectionPosterPath;
    }

    /**
     * Gets the backdrop URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The backdrop URL of the collection. If the collection cannot be
     *         found, then return null
     */
    @Override
    public String getCollectionBackdrop(int collectionID) {
        CollectionRecord collectionRecord = collectionsByID.get(collectionID);
        if (collectionRecord == null) {
            return null;
        }
        return collectionRecord.collectionBackdropPath;
    }

    /**
     * Gets the collection ID of a given film
     * 
     * @param filmID The movie ID
     * @return The collection ID for the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public int getCollectionID(int filmID) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return -1;
        }
        return movie.collectionID;
    }

    /**
     * Sets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @param imdbID The IMDb ID
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return false;
        }
        movie.imdbID = imdbID;
        return true;
    }

    /**
     * Gets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @return The IMDb ID for the requested film. If the film cannot be found,
     *         return null
     */
    @Override
    public String getIMDB(int filmID) {
        Movie movie = movies.get(filmID);
        if (movie == null) {
            return null;
        }
        return movie.imdbID;
    }

    /**
     * Sets the popularity of a given film. If the popularity for a film already exists, replace it with the new value
     * 
     * @param id         The movie ID
     * @param popularity The popularity of the film
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setPopularity(int id, double popularity) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }
        movie.popularity = popularity;
        return true;
    }

    /**
     * Gets the popularity of a given film
     * 
     * @param id The movie ID
     * @return The popularity value of the requested film. If the film cannot be
     *         found, then return -1.0d. If the popularity has not been set, return 0.0
     */
    @Override
    public double getPopularity(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return -1.0d;
        }
        return movie.popularity;
    }

    /**
     * Adds a production company to a given film
     * 
     * @param id      The movie ID
     * @param company A Company object that represents the details on a production
     *                company
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCompany(int id, Company company) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }

        movie.productionCompanies.add(company);
        movie.productionCompanyCount++;
        return true;
    }

    /**
     * Adds a production country to a given film
     * 
     * @param id      The movie ID
     * @param country A ISO 3166 string containing the 2-character country code
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCountry(int id, String country) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return false;
        }

        movie.productionCountries.add(country);
        movie.productionCountryCount++;
        return true;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Company objects that represent all the production
     *         companies that worked on the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public Company[] getProductionCompanies(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }

        Company[] result = new Company[movie.productionCompanyCount];
        movie.productionCompanies.toArray(result, true);
        return result;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Strings that represent all the production countries (in
     *         ISO 3166 format) that worked on the requested film. If the film
     *         cannot be found, then return null
     */
    @Override
    public String[] getProductionCountries(int id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            return null;
        }

    String[] result = new String[movie.productionCountryCount];
    movie.productionCountries.toArray(result, true);
    return result;
    }

    /**
     * States the number of movies stored in the data structure
     * 
     * @return The number of movies stored in the data structure
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Produces a list of movie IDs that have the search term in their title,
     * original title or their overview
     * 
     * @param searchTerm The term that needs to be checked
     * @return An array of movie IDs that have the search term in their title,
     *         original title or their overview. If no movies have this search term,
     *         then an empty array should be returned
     */
    @Override
    public int[] findFilms(String searchTerm) {
        int[] ids = getAllIDs();
        if (searchTerm == null) {
            return new int[0];
        }
        String lowerTerm = searchTerm.toLowerCase();
        int count = 0;
        // Count matches
        for (int i = 0; i < ids.length; i++) {
            Movie movie = movies.get(ids[i]);
            if (movie != null) {
                boolean inTitle = movie.title != null && movie.title.toLowerCase().contains(lowerTerm);
                boolean inOriginalTitle = movie.originalTitle != null && movie.originalTitle.toLowerCase().contains(lowerTerm);
                boolean inOverview = movie.overview != null && movie.overview.toLowerCase().contains(lowerTerm);
                if (inTitle || inOriginalTitle || inOverview) {
                    count++;
                }
            }
        }
        // Create result array with exact size
        int[] result = new int[count];
        int index = 0;
        // Fill result
        for (int i = 0; i < ids.length; i++) {
            Movie movie = movies.get(ids[i]);
            if (movie != null) {
                boolean inTitle = movie.title != null && movie.title.toLowerCase().contains(lowerTerm);
                boolean inOriginalTitle = movie.originalTitle != null && movie.originalTitle.toLowerCase().contains(lowerTerm);
                boolean inOverview = movie.overview != null && movie.overview.toLowerCase().contains(lowerTerm);
                if (inTitle || inOriginalTitle || inOverview) {
                    result[index] = ids[i];
                    index++;
                }
            }
        }
        return result;
    }
}
