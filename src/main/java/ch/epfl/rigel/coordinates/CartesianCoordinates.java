package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Cartesian Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class CartesianCoordinates {

    private final double x;
    private final double y;
    
    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Creates CartesianCoordinates from a given absciss (x) and ordinate (y).
     * 
     * @param double (x)
     * @param double (y)
     * @return CartesianCoordinates (cartCoords)
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }
    
    /**
     * Returns the absciss (x).
     *
     * @return double (x)
     */
    public double x() {
        return x;
    }
    
    /**
     * Returns the distance between this and that (CartesianCoordinates).
     *
     * @return double (dist)
     */
    public double distanceTo(CartesianCoordinates that) {
        return Math.hypot(x() - that.x(), y() - that.y());
    }
    
    /**
     * Returns the square distance between this and that (CartesianCoordinates).
     *
     * @return double (dist)
     */
    public double distanceToSquare(CartesianCoordinates that) {
        return (x()-that.x())*(x()-that.x()) + (y()-that.y())*(y()-that.y());
    }
    
    /**
     * Returns the ordinate (y).
     *
     * @return double (y)
     */
    public double y() {
        return y;
    }
    
    /**
     * Redefines the toString method. Expresses CartesianCoordinates in the form : (abs=X, ord=Y). 
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(abs=%.4f, ord=%.4f)", x(), y());
    }
    
    @Override
    public final boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }  
}