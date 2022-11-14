package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Horizontal Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

    private final static RightOpenInterval AZ_VALID_DEG = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALT_VALID_DEG = ClosedInterval.of(-90, 90);
    
    private final static RightOpenInterval NORTH_INTERVAL_1 = RightOpenInterval.of(0, 67.5);
    private final static RightOpenInterval NORTH_INTERVAL_2 = RightOpenInterval.of(292.5, 360);
    private final static RightOpenInterval SOUTH_INTERVAL = RightOpenInterval.of(112.5, 247.5);
    private final static  RightOpenInterval WEST_INTERVAL = RightOpenInterval.of(202.5, 337.5);
    private final static  RightOpenInterval EAST_INTERVAL = RightOpenInterval.of(22.5, 157.5);
    
    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * Creates HorizontalCoordinates from a given azimut and altitude in radiant. Throws
     * IllegalArgumentException if the azimut is not contained inside the interval [0;TAU[ or
     * if the the altitude is not contained inside the interval [-TAU/4;TAU/4].
     * 
     * @param double (az)
     * @param double (alt)
     * @return HorizontalCoordinates (horCoords)
     */
    public static HorizontalCoordinates of(double az, double alt) {
        checkInInterval(AZ_VALID_DEG, Angle.toDeg(az));
        checkInInterval(ALT_VALID_DEG, Angle.toDeg(alt));
        return new HorizontalCoordinates(az, alt);
    }
    
    /**
     * Creates HorizontalCoordinates from a given azimut and altitude in degrees. Throws
     * IllegalArgumentException if the azimut is not contained inside the interval [0;360[ or
     * if the the altitude is not contained inside the interval [-90;90].
     * 
     * @param double (azDeg)
     * @param double (altDeg)
     * @return HorizontalCoordinates (horCoords)
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        checkInInterval(AZ_VALID_DEG, azDeg);
        checkInInterval(ALT_VALID_DEG, altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }
    
    
    /**
     * Returns the azimut in radiant.
     *
     * @return double (azimut)
     */
    public double az() {
        return lon();
    }
    
    /**
     * Returns the azimut in degrees.
     *
     * @return double (azimutDeg)
     */
    public double azDeg() {
        return lonDeg();
    }
    
    /**
     * Returns the altitude in radiant.
     *
     * @return double (altitude)
     */
    public double alt() {
        return lat();
    }
    
    /**
     * Returns the altitude in degrees.
     *
     * @return double (altitudeDeg)
     */
    public double altDeg() {
        return latDeg();
    }
    
    /**
     * Returns a String which corresponds to the octant in which the azimut is contained,
     * according to the String representation of the 4 cardinal points entered as parameters.   
     *  
     * @param String (north)
     * @param String (east)
     * @param String (south)
     * @param String (west)
     *
     * @return String (azOctantName)
     */
    public String azOctantName(String n, String e, String s, String w) {
        StringBuilder sb = new StringBuilder();
        if(NORTH_INTERVAL_1.contains(azDeg()) || NORTH_INTERVAL_2.contains(azDeg())) {
            sb.append(n);
        }
        else if(SOUTH_INTERVAL.contains(azDeg())) {
            sb.append(s);
        }
        if(WEST_INTERVAL.contains(azDeg())) {
            sb.append(w);
        }
        else if(EAST_INTERVAL.contains(azDeg())) {
            sb.append(e);
        }
        return sb.toString();
    }
    
    /**
     * Returns the angular distance in radiant between the current HorizontalCoordinates (this) and a given HorrizontalCoordinates (that).
     * 
     * @param HorizontalCoordinates (that)
     * @return double (rad)
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double az1 = this.az();
        double az2 = that.az();
        double alt1 = this.alt();
        double alt2 = that.alt();
        return Math.acos(Math.sin(alt1) * Math.sin(alt2) + Math.cos(alt1) * Math.cos(alt2) * Math.cos(az1 - az2));
    }
    
    /**
     * Redefines the toString method. Expresses HorizontalCoordinates in the form : (az=X°, alt=Y°).
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}