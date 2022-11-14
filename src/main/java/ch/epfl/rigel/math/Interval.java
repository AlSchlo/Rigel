package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * An Abstract Interval.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public abstract class Interval {

    private final double low;
    private final double high;

    /**
     * Default constructor of an Interval given a low and a high. Throws IllegalArgumentException if low >= high.
     *
     * @param double (low)
     * @param double (high)
     */
    protected Interval(double low, double high) {
        checkArgument(low < high);
        this.low = low;
        this.high = high;
    }

    /**
     * Returns the lowest value of the interval
     * 
     * @return double (low)
     */
    public double low() {
        return low;
    }

    /**
     * Returns the highest value of the interval
     * 
     * @return double (high)
     */
    public double high() {
        return high;
    }

    /**
     * Returns the size of the interval
     * 
     * @return double (size)
     */
    public double size() {
        return (high - low);
    }

    /**
     * Returns whether a given value belongs in the interval
     * 
     * @param double (v)
     * @return boolean (inside)
     */
    abstract public boolean contains(double v);

    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

}
