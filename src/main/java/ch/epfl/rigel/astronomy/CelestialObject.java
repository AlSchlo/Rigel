package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Objects;


/**
 * An Abstract Celestial Object.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;
    
    /**
     * Default constructor of a CelestialObject : constructs a CelestialObject from a given
     * name (String), equatorialPos (EquatorialCoordinates), angularSize (float) and magnitude (float).
     * Throws IAE if the angularSize is negative, or if the name or the equatorialPos are null.
     * 
     * @param String (name)
     * @param EquatorialCoordinates (equatorialPos)
     * @param float (angularSize)
     * @param float (magnitude)
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        checkArgument(angularSize >= 0);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }
    
    /**
     * Returns the name of the CelestialObject.
     *
     * @return String (name)
     */
    public String name() {
       return name; 
    }
    
    /**
     * Returns the angularSize of the CelestialObject.
     *
     * @return double (angularSize)
     */
    public double angularSize() {
        return angularSize;
    }
    
    /**
     * Returns the magnitude of the CelestialObject.
     *
     * @return double (magnitude)
     */
    public double magnitude() {
        return magnitude;
    }
    
    /**
     * Returns the equatorialCoordinates of the CelestialObject.
     *
     * @return EquatorialCoordinates (equatorialPos)
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }
    
    /**
     * Returns a brief informative text concerning the CelestialObject, for the user.
     * By default returns only the name of the CelestialObject.
     *
     * @return String (info)
     */
    public String info() {
        return name();
    }
    
    /**
     * Returns a brief informative text concerning the CelestialObject, for the user.
     * By default returns only the name of the CelestialObject.
     *
     * @return String (info)
     */
    @Override
    public String toString() {
        return info();
    }  
}