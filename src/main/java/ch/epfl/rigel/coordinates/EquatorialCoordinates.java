package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Equatorial Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RA_VALID_DEG = RightOpenInterval.of(0, 360);
    private final static ClosedInterval DEC_VALID_DEG = ClosedInterval.of(-90, 90);
    
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }
    
    /**
     * Creates EquatorialCoordinates from a given right ascension and declination in radiant. Throws
     * IllegalArgumentException if the right ascension is not contained inside the interval [0;TAU[ or
     * if the the declination is not contained inside the interval [-TAU/4;TAU/4].
     * 
     * @param double (ra)
     * @param double (dec)
     * @return EquatorialCoordinates (eqCoords)
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        checkInInterval(RA_VALID_DEG, Angle.toDeg(ra));
        checkInInterval(DEC_VALID_DEG, Angle.toDeg(dec));
        return new EquatorialCoordinates(ra, dec);
    }
    
    /**
     * Returns the right ascension in radiant.
     *
     * @return double (ra)
     */
    public double ra() {
        return lon();
    }
    
    /**
     * Returns the right ascension in degrees.
     *
     * @return double (raDeg)
     */
    public double raDeg() {
        return lonDeg();
    }
    
    /**
     * Returns the right ascension in hours.
     *
     * @return double (raHr)
     */
    public double raHr() {
       return Angle.toHr(lon());
    }
    
    /**
     * Returns the declination in radiant.
     *
     * @return double (dec)
     */
    public double dec() {
        return lat();
    }
    
    /**
     * Returns the declination in degrees.
     *
     * @return double (decDeg)
     */
    public double decDeg() {
        return latDeg();
    }
    
    /**
     * Redefines the toString method. Expresses EquatorialCoordinates in the form : (ra=Xh, dec=Y°). 
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }
}
