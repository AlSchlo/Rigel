package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Ecliptic Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval RA_VALID_DEG = RightOpenInterval.of(0, 360);
    private final static ClosedInterval DEC_VALID_DEG = ClosedInterval.of(-90, 90);
    
    private EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }
    
    /**
     * Creates EclipticCoordinates from a given longitude and latitude in radiant. Throws
     * IllegalArgumentException if the longitude is not contained inside the interval [0;TAU[ or
     * if the the latitude is not contained inside the interval [-TAU/4;TAU/4].
     * 
     * @param double (ra)
     * @param double (dec)
     * @return EclipticCoordinates (eclCoords)
     */
    public static EclipticCoordinates of(double lon, double lat) {
        checkInInterval(RA_VALID_DEG, Angle.toDeg(lon));
        checkInInterval(DEC_VALID_DEG, Angle.toDeg(lat));
        return new EclipticCoordinates(lon, lat);
    }
    
    /**
     * Returns the latitude in radiant.
     *
     * @return double (latitude)
     */
    public double lat() {
        return super.lat();
    }
    
    /**
     * Returns the latitude in degrees.
     *
     * @return double (latitudeDeg)
     */
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * Returns the longitude in radiant.
     *
     * @return double (longitude)
     */
    public double lon() {
        return super.lon();
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return double (longitudeDeg)
     */
    public double lonDeg() {
        return super.lonDeg();
    }
    
    /**
     * Redefines the toString method. Expresses EclipticCoordinates in the form : (λ=X°, β=Y°). 
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }
}
