package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Non Instantiable Class Used To Verify Conditions.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Preconditions {

    private Preconditions() {
        // Non instantiable class
    }

    /**
     * Throws IllegalArgumentException if the condition is false
     * 
     * @param boolean (isTrue)
     * 
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException("Une valeur illegale est rentrée dans le système !");
        }
    }

    /**
     * Throws IllegalArgumentException if the value is not inside the given
     * interval, else returns the value.
     * 
     * @param Interval (interval)
     * @param double (value)
     * @return double (value)
     */
    public static double checkInInterval(Interval interval, double value) {
        checkArgument(interval.contains(value));
        return value;
    }
}