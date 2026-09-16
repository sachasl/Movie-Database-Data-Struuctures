package structures;
import stores.Person;

/**
 * Stores crew-related data for a single person.
 * 
 * This includes the person's details and the list of films they have worked on as part of the crew.
 */
public class CrewStats {
    public Person person;
    public LinkedList<Integer> films;

    public CrewStats(Person person) {
        this.person = person;
        this.films = new LinkedList<>();
    }
}
