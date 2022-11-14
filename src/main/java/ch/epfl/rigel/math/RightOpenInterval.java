package ch.epfl.rigel.math;

import java.util.Locale;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A RightOpenInterval.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double left, double right) {
        super(left, right);
    }
    
    /**
     * Returns whether a given value belongs in the RightOpenInterval [a;b[.
     * 
     * @param double (v)
     * @return boolean (inside)
     */
    @Override
    public boolean contains(double v) {
        return (low() <= v && high() > v);
    }

    /**
     * Creates a RightOpenInterval from two given values [low, high[. Throws IllegalArgumentException if low < high. 
     * 
     * @param double (low)
     * @param double (high)
     * @return RightOpenInterval (interval)
     */
    public static RightOpenInterval of(double low, double high) {
        return new RightOpenInterval(low, high);
    }

    /**
     * Creates a RightOpenInterval from a given size, centered in zero [-size/2;size/2[. Throws IllegalArgumentException if size <= 0. 
     * 
     * @param double (size)
     * @return RightOpenInterval (interval)
     */
    public static RightOpenInterval symmetric(double size) {
        checkArgument(size > 0);
        return new RightOpenInterval(-size/2, size/2);
    }

    /**
     * The reduce function associated with the RightOpenInterval [a;b[. Returns :
     * - a + floorMod(x-a,b-a)
     * 
     * @param double (v)
     * @return double
     */
    public double reduce(double v) {
        return (low() + floorMod(v - low(), size()));
    }

   
    private static double floorMod(double x, double y) {
        return (x - y * Math.floor(x / y));
    }

    /**
     * Redefines the toString method. Expresses a RightOpenInterval in the form : [low,high[.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f,%f[", low(), high());
    }
}