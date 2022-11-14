package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

/**
 * A Stereographic Projection. 
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final HorizontalCoordinates center;
    private final double cosCenterAlt;
    private final double sinCenterAlt;
    private final double tanCenterAlt;

    /**
     * Default constructor of StereographicProjection.
     * Only requires a given HorizontalCoordinates as parameter in order to fix a center.
     * The instance created can then be used to perform convertions from HorizontalCoordinates to CartesianCoordinates,
     * using a StereographicProjection.
     * 
     * @param HorizontalCoordinates (center)
     */
    public StereographicProjection(HorizontalCoordinates center) {
        this.center = center;
        this.cosCenterAlt = Math.cos(center.alt());
        this.sinCenterAlt = Math.sin(center.alt());
        this.tanCenterAlt = sinCenterAlt / cosCenterAlt;
    }

    /**
     * Performs the StereographicProjection conversion from given HorizontalCoordinates to CartesianCoordinates.
     *  
     * @param HorizontalCoordinates (hor)
     * @return CartesianCoordinates (cart)
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double ld = azAlt.az() - center.az();
        double cosLd = Math.cos(ld);
        double sinAzAltAlt = Math.sin(azAlt.alt());
        double cosAzAltAlt = Math.cos(azAlt.alt());
        double d = 1/(1 + sinAzAltAlt*sinCenterAlt + cosAzAltAlt*cosCenterAlt*cosLd);
        double x = d*cosAzAltAlt*Math.sin(ld);
        double y = d*(sinAzAltAlt*cosCenterAlt - cosAzAltAlt*sinCenterAlt*cosLd);   
        return CartesianCoordinates.of(x, y);
    }
    
    /**
     * Performs the inverse StereographicProjection conversion from given CartesianCoordinates to HorizontalCoordinates.
     *  
     * @param CartesianCoordinates (cart)
     * @return HorizontalCoordinates (hor)
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double x = xy.x();
        double y = xy.y();
        double p2 = x*x + y*y;
        double p = Math.sqrt(p2);
        if(x == 0 && y == 0) return center;
        double sinC = (2*p)/(p2 + 1);
        double cosC = (1 - p2)/(p2 + 1);
        double az = Math.atan2(x*sinC, p*cosCenterAlt*cosC - y*sinCenterAlt*sinC) + center.az();
        double alt = Math.asin(cosC*sinCenterAlt + (y*sinC*cosCenterAlt)/p);
        return HorizontalCoordinates.of(Angle.normalizePositive(az), alt);
    }

    /**
     * Returns the CartesianCoordinates of the center of the circle from the given HorizontalCoordinates of a parallel.
     * If the latitude of the center and the parallel are both 0, returns infinity (the circle is a straight line).
     * @param (HorizontalCoordinates) hor 
     * @return (CartesianCoordinates) cart
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates parallel) {
        double y = (cosCenterAlt / (Math.sin(parallel.alt()) + sinCenterAlt));
        return CartesianCoordinates.of(0, y);
    }

    /**
     * Returns the length of the radius of the circle from the given HorizontalCoordinates of a parallel.
     * If the latitude of the center and the parallel are both 0, returns infinity (the circle is a straight line).
     * @param HorizontalCoordinates (parallel) 
     * @return double (radius)
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return (Math.cos(parallel.alt()) / (Math.sin(parallel.alt()) + sinCenterAlt));
    }
    
    /**
     * Returns the length of the radius of the circle from the given HorizontalCoordinates of a meridian.
     * If the altitude of the center and the meridian are both 0, returns infinity (the circle is a straight line).
     * @param HorizontalCoordinates (meridian) 
     * @return double (radius)
     */
    public double circleRadiusForMeridian(HorizontalCoordinates meridian) {
        double lambda = meridian.az() - center.az();
        return 1 / (cosCenterAlt * Math.sin(lambda));
    }
    
    /**
     * Returns the CartesianCoordinates of the center of the circle from the given HorizontalCoordinates of a meridian.
     * If the latitude of the center and the meridian are both 0, returns infinity (the circle is a straight line).
     * @param (HorizontalCoordinates) hor 
     * @return (CartesianCoordinates) cart
     */
    public CartesianCoordinates circleCenterForMeridian(HorizontalCoordinates meridian) {
        double lambda = meridian.az() - center.az();
        double x = - Math.cos(lambda) / (cosCenterAlt * Math.sin(lambda));
        double y = - tanCenterAlt;
        return CartesianCoordinates.of(x, y);
    }
    
    /**
     * Returns the CartesianCoordinates of the center of the projection.
     * @return (HorizontalCoordinates) center
     */
    public HorizontalCoordinates center() {
        return center;
    }
    
    /**
     * Returns the projected diameter of a sphere of apparent diameter rad, which center is the center of the projection.
     *  
     * @param double (rad)
     * @return double (diam)
     */
    public double applyToAngle(double rad) {
        return 2*Math.tan(rad/4);
    }
    
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Redefines the toString method. Expresses StereographicProjection in the form : 
     * - StereographicProjection of center horCoord(az=X°, alt=Y°)
     * Four digits of precision.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection centered in horCoord " + center.toString());
    }
}