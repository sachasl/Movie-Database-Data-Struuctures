package structures;

import stores.CastCredit;
import stores.CrewCredit;

/**
 * Represents all credit information for a single film.
 * 
 * This class stores the cast and crew associated with a film, allowing efficient retrieval of film-based credit data.
 */
public class CreditRecord {
    public int filmID;
    public LinkedList<CastCredit> cast;
    public LinkedList<CrewCredit> crew;

    public CreditRecord(int filmID) {
        this.filmID = filmID;
        this.cast = new LinkedList<>();
        this.crew = new LinkedList<>();
    }
}