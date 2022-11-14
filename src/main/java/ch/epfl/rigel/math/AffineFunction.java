package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * An affine function of the form y = mx + h. 
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class AffineFunction {
    private final double m;
    private final double h;
    
    /**
     * Default constructor of an AffineFunction of the form y = mx + h. Given m and h.
     * @param m (double) must be finite
     * @param h (double) must be finite
     */
    public AffineFunction(double m, double h) {
        checkArgument(Double.isFinite(m) && Double.isFinite(h));
        this.h = h;
        this.m = m;
    }
    
    /**
     * Applies the function for a given X.
     * @param double (x)
     * @return double (y)
     */
    public double apply(double x) {
        return m * x + h;
    }
    
    /**
     * Applies the inverse function for a given Y. An AffineFunction is always bijective.
     * @param double (y)
     * @return double (x)
     */
    public double inverseApply(double y) {
        return (y - h) / m;
    }
}