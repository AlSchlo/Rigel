package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * A Physical Celestial Object Model Interface.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public interface CelestialObjectModel <O> {

    /**
     * Returns the genericObject modelized by the numbers of days since J2010, using
     * the given conversion to get its EquatorialCoordinates from its EclipticCoordinates.
     * 
     * @param double (daysSinceJ2010)
     * @param EclipticToEquatorialConversion (eclipticToEquatorialConversion)
     * @return O (genericObject)
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
    
}