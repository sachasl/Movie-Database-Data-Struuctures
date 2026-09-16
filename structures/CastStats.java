package structures;
import stores.Person;


/**
 * Stores cast-related data for a single person.
 * 
 * This includes the person's details, the list of films they have
 * appeared in, the subset of films where they are a starring actor,
 * and the total number of credits.
 */
public class CastStats implements Comparable<CastStats>{
    public Person person;
    public LinkedList<Integer> films;
    public LinkedList<Integer> starringFilms;
    public int creditCount;

    public CastStats(Person person) {
        this.person = person;
        this.films = new LinkedList<>();
        this.starringFilms = new LinkedList<>();
        this.creditCount = 0;
    }

    @Override
    public int compareTo(CastStats other) {
        // descending by creditCount
        return Integer.compare(other.creditCount, this.creditCount);
    }
}
