package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * Non Instantiable Class Used To Manipulate Angles.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Angle {

    public static final double TAU = 2 * Math.PI;
    private static final double DEG_PER_RAD = 360.0 / TAU;
    private static final double MIN_PER_RAD = 21600 / TAU;
    private static final double SEC_PER_RAD = 1296000 / TAU;
    private static final double HR_PER_RAD = 24 / TAU;
    private static final RightOpenInterval NORM_INTERVAL = RightOpenInterval.of(0, TAU);

    private Angle() {
        // Non instantiable class
    }

    /**
     * Normalizes the angle in radiant by reducing it to the interval [0,τ[,
     * 
     * @param double (rad)
     * @return double (normalizedRad)
     */
    public static double normalizePositive(double rad) {
        return NORM_INTERVAL.reduce(rad);
    }

    /**
     * Converts seconds in radiant
     * 
     * @param double (sec)
     * @return double (rad)
     */
    public static double ofArcsec(double sec) {
        return sec / SEC_PER_RAD;
    }

    /**
     * Converts degrees, minutes and seconds in radiant
     * 
     * @param double (deg)
     * @param double (min)
     * @param double (sec)
     * @return double (rad)
     */
    public static double ofDMS(int deg, int min, double sec) {
        checkArgument(0 <= deg);
        checkArgument(0 <= min && min < 60);
        checkArgument(0 <= sec && sec < 60);
        return deg / DEG_PER_RAD + min / MIN_PER_RAD + sec / SEC_PER_RAD;
    }

    /**
     * Converts degrees in radiant
     * 
     * @param double (deg)
     * @return double (rad)
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Converts radiant in degrees
     * 
     * @param double (rad)
     * @return double (deg)
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Converts hours in radiant
     * 
     * @param double (hr)
     * @return double (deg)
     */
    public static double ofHr(double hr) {
        return hr / HR_PER_RAD;
    }
    
    /**
     * Converts radiant in hours
     * 
     * @param double (rad)
     * @return double (hr)
     */
    public static double toHr(double rad) {
        return rad * HR_PER_RAD;
    }
}