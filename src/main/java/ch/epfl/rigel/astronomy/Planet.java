package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * A Planet.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Planet extends CelestialObject {

    /**
     * Default constructor of a Planet : constructs a Planet from a given
     * name (String), equatorialPos (EquatorialCoordinates), angularSize (float) and magnitude (float).
     * Throws IAE if the angularSize is negative, or if the name or the equatorialPos are null (NullPointerException).
     * 
     * @param String (name)
     * @param EquatorialCoordinates (equatorialPos)
     * @param float (angularSize)
     * @param float (magnitude)
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }   
}