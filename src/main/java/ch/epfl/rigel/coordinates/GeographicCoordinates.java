package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

/**
 * Geographic Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval LON_VALID_DEG = RightOpenInterval.of(-180, 180);
    private final static ClosedInterval LAT_VALID_DEG = ClosedInterval.of(-90, 90);

    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Creates GeographicCoordinates from a given longitude and latitude in degrees. Throws
     * IllegalArgumentException if the longitude is not contained inside the interval [-180;180[ or
     * if the the latitude is not contained inside the interval [-90;90].
     * 
     * @param double (lonDeg)
     * @param double (latDeg)
     * @return GeographicCoordinates (geoCoords)
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        checkInInterval(LON_VALID_DEG, lonDeg);
        checkInInterval(LAT_VALID_DEG, latDeg);
        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }
    
    /**
     * Returns whether a given longitude in degrees is valid (if it belongs inside the interval [-180;180[)
     * 
     * @param double (lonDeg)
     * @return boolean (contained)
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return LON_VALID_DEG.contains(lonDeg);
    }
    
    /**
     * Returns whether a given latitude in degrees is valid (if it belongs inside the interval [-90;90])
     * 
     * @param double (lonDeg)
     * @return boolean (contained)
     */
    public static boolean isValidLatDeg(double latDeg) {
        return LAT_VALID_DEG.contains(latDeg);
    }
   
    /**
     * Returns the latitude in radiant.
     *
     * @return double (latitude)
     */
    @Override
    public double lat() {
        return super.lat();
    }
    
    /**
     * Returns the latitude in degrees.
     *
     * @return double (latitudeDeg)
     */
    @Override
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * Returns the longitude in radiant.
     *
     * @return double (longitude)
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return double (longitudeDeg)
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }
    
    /**
     * Redefines the toString method. Expresses GeographicCoordinates in the form : (lon=X°, lat=%Y°) 
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}