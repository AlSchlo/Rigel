package ch.epfl.rigel.astronomy;

import java.util.List;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * An Asterism Of A Non Empty List Of Stars.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Asterism {
    
    private final List<Star> stars;
    
    /**
     * Default constructor of an Asterism from a given list of stars.
     * Throws IAE if the list is empty.
     * 
     * @throws IllegalArgumentException
     * @param List<Star> (stars)
     */
    public Asterism(List<Star> stars) {
        checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars);
    }
    
    /**
     * Returns an unmodifiable list of the stars in the Asterism.
     * 
     * @return List<Star> (stars)
     */
    public List<Star> stars() {
        return stars;
    }
}