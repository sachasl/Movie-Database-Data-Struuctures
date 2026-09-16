package stores;

import interfaces.ICredits;
import structures.*;

public class Credits implements ICredits{
    Stores stores;

        // Added Hashmaps here to allow for efficient retrieval of credits by film ID, and stats by cast/crew ID
        private HashMap<Integer, CreditRecord> creditsByFilm;
        private HashMap<Integer, CastStats> castStatsByID;
        private HashMap<Integer, CrewStats> crewStatsByID;
        private int size;

    /**
     * The constructor for the Credits data store. This is where you should
     * initialise your data structures.
     * 
     * @param stores An object storing all the different key stores, 
     *               including itself
     */
    public Credits (Stores stores) {
        this.stores = stores;
        this.creditsByFilm = new HashMap<>();
        this.castStatsByID = new HashMap<>();
        this.crewStatsByID = new HashMap<>();
        this.size = 0;
    }

    /**
     * Adds data about the people who worked on a given film. The movie ID should be
     * unique
     * 
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The (unique) movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {
        // Reject duplicate film IDs
        if (creditsByFilm.get(id) != null) {
            return false;
        }

        CreditRecord record = new CreditRecord(id);

        // Add cast entries to the film record and update cast stats
        for (int i = 0; i < cast.length; i++) {
            CastCredit castCredit = cast[i];
            record.cast.add(castCredit);
            int castID = castCredit.getID();
            CastStats castStats = castStatsByID.get(castID);
            
            if (castStats == null) {
                Person castPerson = new Person(castCredit.getID(), castCredit.getName(), castCredit.getProfilePath());
                castStats = new CastStats(castPerson);
                castStatsByID.add(castID, castStats);
            }

            castStats.films.add(id);
            castStats.creditCount++;

            // Check if actor starred in top 3 for film
            if (castCredit.getOrder() <= 3) {
                castStats.starringFilms.add(id);
            }
        }

        // Add crew entries to the film record and update crew stats
        for (int j = 0; j < crew.length; j++) {
            CrewCredit crewCredit = crew[j];
            record.crew.add(crewCredit);
            int crewID = crewCredit.getID();
            CrewStats crewStats = crewStatsByID.get(crewID);

            if (crewStats == null) {
                Person crewPerson = new Person(crewCredit.getID(), crewCredit.getName(), crewCredit.getProfilePath());
                crewStats = new CrewStats(crewPerson);
                crewStatsByID.add(crewID, crewStats);
            }
            crewStats.films.add(id);
        }
        
        creditsByFilm.add(id, record);
        size++;
        return true;
    }

    /**
     * Remove a given films data from the data structure
     * 
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        CreditRecord record = creditsByFilm.remove(id);
        
        if (record == null) {
            return false;
        }

        // Update cast stats due to film removal
        ListElement<CastCredit> castCurrent = record.cast.getHead();
        while (castCurrent != null) {
            CastCredit castCredit = castCurrent.getValue();
            int castID = castCredit.getID();
            CastStats castStats = castStatsByID.get(castID);

            if (castStats != null) {
                castStats.creditCount--;
                castStats.films.remove(id);

                if (castCredit.getOrder() <= 3) {
                    castStats.starringFilms.remove(id);
                }

                if (castStats.creditCount <= 0) {
                    castStatsByID.remove(castID);
                }
            }

            castCurrent = castCurrent.getNext();
        }

        // Update crew stats due to film removal
        ListElement<CrewCredit> crewCurrent = record.crew.getHead();
        while (crewCurrent != null) {
            CrewCredit crewCredit = crewCurrent.getValue();
            int crewID = crewCredit.getID();
            CrewStats crewStats = crewStatsByID.get(crewID);

            if (crewStats != null) {
                crewStats.films.remove(id);

                if (crewStats.films.isEmpty()) {
                    crewStatsByID.remove(crewID);
                }
            }

            crewCurrent = crewCurrent.getNext();
        }

        size --;
        return true;
    }

    /**
     * Gets all the cast members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CastCredit objects, one for each member of cast that is 
     *         in the given film. The cast members should be in "order" order. If
     *         there is no cast members attached to a film, or the film cannot be 
     *         found in Credits, then return an empty array
     */
    @Override
    public CastCredit[] getFilmCast(int filmID) {
        CreditRecord record = creditsByFilm.get(filmID);

        if (record == null || record.cast.isEmpty()) {
            return new CastCredit[0];
        }

        CastCredit[] result = new CastCredit[record.cast.size()];
        record.cast.toArray(result, false);

        // Use quicksort to sort the cast members by their "order" field
        if (result.length > 1) {
            QuickSorter.quickSort(result, 0, result.length - 1);
        }

        return result;
    }

    /**
     * Gets all the crew members for a given film
     * 
     * @param filmID The movie ID
     * @return An array of CrewCredit objects, one for each member of crew that is
     *         in the given film. The crew members should be in "id" order (not "elementID"). If there 
     *         is no crew members attached to a film, or the film cannot be found in Credits, 
     *         then return an empty array
     */
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {
        CreditRecord record = creditsByFilm.get(filmID);

        if (record == null || record.crew.isEmpty()) {
            return new CrewCredit[0];
        }

        CrewCredit[] result = new CrewCredit[record.crew.size()];
        record.crew.toArray(result, false);

        // Use quicksort to sort the crew members by their "id" field
        if (result.length > 1) {
            QuickSorter.quickSort(result, 0, result.length - 1);
        }

        return result;
    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        CreditRecord record = creditsByFilm.get(filmID);
        
        if (record == null) {
            return -1;
        }
        
        return record.cast.size();
    }

    /**
     * Gets the number of crew that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found in Credits, then return -1
     */
    @Override
    public int sizeOfCrew(int filmID) {
        CreditRecord record = creditsByFilm.get(filmID);
        
        if (record == null) {
            return -1;
        }
        
        return record.crew.size();
    }

    /**
     * Gets a list of all unique cast members present in the data structure
     * 
     * @return An array of all unique cast members as Person objects. If there are 
     *         no cast members, then return an empty array
     */
    @Override
    public Person[] getUniqueCast() {
        Integer[] ids = castStatsByID.keys();
        Person[] result = new Person[ids.length];

        for (int i = 0; i < ids.length; i++) {
            CastStats castStats = castStatsByID.get(ids[i]);
            result[i] = castStats.person;
        }
        
        return result;
    }

    /**
     * Gets a list of all unique crew members present in the data structure
     * 
     * @return An array of all unique crew members as Person objects. If there are
     *         no crew members, then return an empty array
     */
    @Override
    public Person[] getUniqueCrew() {
        Integer[] ids = crewStatsByID.keys();
        Person[] result = new Person[ids.length];

        for (int i = 0; i < ids.length; i++) {
            CrewStats crewStats = crewStatsByID.get(ids[i]);
            result[i] = crewStats.person;
        }
        
        return result;
    }

    /**
     * Get all the cast members that have the given string within their name
     * 
     * @param cast The string that needs to be found
     * @return An array of unique Person objects of all cast members that have the 
     *         requested string in their name. If there are no matches, return an 
     *         empty array
     */
    @Override
    public Person[] findCast(String cast) {
        if (cast == null) {
            return new Person[0];
        }

        String term = cast.toLowerCase();
        Integer[] ids = castStatsByID.keys();
        int count = 0;
        
        // Count matches
        for (int i = 0; i < ids.length; i++) {
            CastStats castStats = castStatsByID.get(ids[i]);
            if (castStats != null
                    && castStats.person.getName() != null
                    && castStats.person.getName().toLowerCase().contains(term)) {
                count++;
            }
        }
        
        // Create exact-sized result array
        Person[] result = new Person[count];
        int index = 0;
        
        // Fill result array
        for (int i = 0; i < ids.length; i++) {
            CastStats castStats = castStatsByID.get(ids[i]);
            
            if (castStats != null
                    && castStats.person.getName() != null
                    && castStats.person.getName().toLowerCase().contains(term)) {
                result[index] = castStats.person;
                index++;
            }
        }

        return result;
    }

    /**
     * Get all the crew members that have the given string within their name
     * 
     * @param crew The string that needs to be found
     * @return An array of unique Person objects of all crew members that have the 
     *         requested string in their name. If there are no matches, return an 
     *         empty array
     */
    @Override
    public Person[] findCrew(String crew) {
        if (crew == null) {
            return new Person[0];
        }
        
        String term = crew.toLowerCase();
        Integer[] ids = crewStatsByID.keys();
        int count = 0;
        
        // Count matches
        for (int i = 0; i < ids.length; i++) {
            CrewStats crewStats = crewStatsByID.get(ids[i]);
            
            if (crewStats != null
                    && crewStats.person.getName() != null
                    && crewStats.person.getName().toLowerCase().contains(term)) {
                count++;
            }
        }
        
        // Create exact-sized result array
        Person[] result = new Person[count];
        int index = 0;
        
        // Fill result array
        for (int i = 0; i < ids.length; i++) {
            CrewStats crewStats = crewStatsByID.get(ids[i]);
            
            if (crewStats != null
                    && crewStats.person.getName() != null
                    && crewStats.person.getName().toLowerCase().contains(term)) {
                result[index] = crewStats.person;
                index++;
            }
        }

        return result;
    }

    /**
     * Gets the Person object corresponding to the cast ID
     * 
     * @param castID The cast ID of the person to be found
     * @return The Person object corresponding to the cast ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCast(int castID) {
        CastStats castStats = castStatsByID.get(castID);
        
        if (castStats == null) {
            return null;
        }

        return castStats.person;
    }

    /**
     * Gets the Person object corresponding to the crew ID
     * 
     * @param crewID The crew ID of the person to be found
     * @return The Person object corresponding to the crew ID provided. 
     *         If a person cannot be found, then return null
     */
    @Override
    public Person getCrew(int crewID){
        CrewStats crewStats = crewStatsByID.get(crewID);
        
        if (crewStats == null) {
            return null;
        }

        return crewStats.person;
    }

    
    /**
     * Get an array of film IDs where the cast member has starred in
     * 
     * @param castID The cast ID of the person
     * @return An array of all the films the member of cast has starred
     *         in. If there are no films attached to the cast member, 
     *         then return an empty array
     */
    @Override
    public int[] getCastFilms(int castID){
        CastStats castStats = castStatsByID.get(castID);
        
        if (castStats == null || castStats.films.isEmpty()) {
            return new int[0];
        }
        
        return castStats.films.toIntArray(true);
    }

    /**
     * Get an array of film IDs where the crew member has starred in
     * 
     * @param crewID The crew ID of the person
     * @return An array of all the films the member of crew has starred
     *         in. If there are no films attached to the crew member, 
     *         then return an empty array
     */
    @Override
    public int[] getCrewFilms(int crewID) {
        CrewStats crewStats = crewStatsByID.get(crewID);
        
        if (crewStats == null || crewStats.films.isEmpty()) {
            return new int[0];
        }

        return crewStats.films.toIntArray(true);
    }

    /**
     * Get the films that this cast member stars in (in the top 3 cast
     * members/top 3 billing). This is determined by the order field in
     * the CastCredit class
     * 
     * @param castID The cast ID of the cast member to be searched for
     * @return An array of film IDs where the the cast member stars in.
     *         If there are no films where the cast member has starred in,
     *         or the cast member does not exist, return an empty array
     */
    @Override
    public int[] getCastStarsInFilms(int castID){
        CastStats castStats = castStatsByID.get(castID);
        
        if (castStats == null || castStats.starringFilms.isEmpty()) {
            return new int[0];
        }

        return castStats.starringFilms.toIntArray(true);
    }
    
    /**
     * Get Person objects for cast members who have appeared in the most
     * films. If the cast member has multiple roles within the film, then
     * they would get a credit per role played. For example, if a cast
     * member performed as 2 roles in the same film, then this would count
     * as 2 credits. The list should be ordered by the highest to lowest number of credits.
     * 
     * @param numResults The maximum number of elements that should be returned
     * @return An array of Person objects corresponding to the cast members
     *         with the most credits, ordered by the highest number of credits.
     *         If there are less cast members that the number required, then the
     *         list should be the same number of cast members found.
     */
    @Override
    public Person[] getMostCastCredits(int numResults) {
        if (numResults <= 0) {
            return new Person[0];
        }

        Integer[] ids = castStatsByID.keys();
        CastStats[] castStatsArray = new CastStats[ids.length];
        for (int i = 0; i < ids.length; i++) {
            castStatsArray[i] = castStatsByID.get(ids[i]);
        }

        // Use quicksort to sort the cast members by their credit count in descending order
        if (castStatsArray.length > 1) {
            QuickSorter.quickSort(castStatsArray, 0, castStatsArray.length - 1);
        }

        int resultSize = Math.min(numResults, castStatsArray.length);
        Person[] result = new Person[resultSize];
        
        for (int i = 0; i < resultSize; i++) {
            result[i] = castStatsArray[i].person;
        }

        return result;
    }

    /**
     * Get the number of credits for a given cast member. If the cast member has
     * multiple roles within the film, then they would get a credit per role
     * played. For example, if a cast member performed as 2 roles in the same film,
     * then this would count as 2 credits.
     * 
     * @param castID A cast ID representing the cast member to be found
     * @return The number of credits the given cast member has. If the cast member
     *         cannot be found, return -1
     */
    @Override
    public int getNumCastCredits(int castID) {
        CastStats castStats = castStatsByID.get(castID);
        
        if (castStats == null) {
            return -1;
        }
        
        return castStats.creditCount;
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return size;
    }
}
