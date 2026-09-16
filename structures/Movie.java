package structures;

import java.time.LocalDate;

import stores.Company;
import stores.Genre;

/**
 * Represents a movie with all the movies additional metadata.
 * Allows hashmaps to be created with the movie's ID as the key and the movie object as the value.
 * 
 */
public class Movie {
    public int id;
    public String title;
    public String originalTitle;
    public String overview;
    public String tagline;
    public String status;
    public Genre[] genres;
    public LocalDate release;
    public long budget;
    public long revenue;
    public String[] languages;
    public String originalLanguage;
    public double runtime;
    public String homepage;
    public boolean adult;
    public boolean video;
    public String poster;
    public int collectionID = -1;
    public String collectionName;
    public String collectionPosterPath;
    public String collectionBackdropPath;
    public double voteAverage = 0.0;
    public int voteCount = 0;
    public String imdbID;
    public double popularity;
    public LinkedList<Company> productionCompanies;
    public int productionCompanyCount;
    public LinkedList<String> productionCountries;
    public int productionCountryCount;

    public Movie(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, LocalDate release, long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
        this.release = release;
        this.budget = budget;
        this.revenue = revenue;
        this.languages = languages;
        this.originalLanguage = originalLanguage;
        this.runtime = runtime;
        this.homepage = homepage;
        this.adult = adult;
        this.video = video;
        this.poster = poster;
        this.productionCompanies = new LinkedList<>();
        this.productionCompanyCount = 0;
        this.productionCountries = new LinkedList<>();
        this.productionCountryCount = 0;
    }
}
