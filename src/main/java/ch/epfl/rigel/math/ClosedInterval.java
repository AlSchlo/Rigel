package ch.epfl.rigel.math;

import java.util.Locale;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A Closed Interval.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class ClosedInterval extends Interval {

    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    /**
     * Creates a ClosedInterval from two given values [low, high]. Throws IllegalArgumentException if low < high. 
     * 
     * @param double (low)
     * @param double (high)
     * @return ClosedInterval (interval)
     */
    public static ClosedInterval of(double low, double high) {
        return new ClosedInterval(low, high);
    }

    /**
     * Creates a ClosedInterval from a given size, centered in zero [-size/2;size/2]. Throws IllegalArgumentException if size <= 0. 
     * 
     * @param double (size)
     * @return ClosedInterval (interval)
     */
    public static ClosedInterval symmetric(double size) {
        checkArgument(size > 0);
        return new ClosedInterval(-size/2, size/2);
    }

    /**
     * Returns whether a given value belongs in the ClosedInterval [a;b].
     * 
     * @param double (v)
     * @return boolean (inside)
     */
    @Override
    public boolean contains(double v) {
        return (low() <= v && high() >= v);
    }

    /**
     * The clip function associated with the ClosedInterval [a;b]. Returns :
     * - a : if v <= a
     * - b : if v >= b
     * - v : else
     * 
     * @param double (v)
     * @return double
     */
    public double clip(double v) {
        if (v <= low()) {
            return low();
        } else if (v >= high()) {
            return high();
        } else {
            return v;
        }
    }

    /**
     * Redefines the toString method. Expresses a ClosedInterval in the form : [low,high].
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f,%f]", low(), high());
    }

}
