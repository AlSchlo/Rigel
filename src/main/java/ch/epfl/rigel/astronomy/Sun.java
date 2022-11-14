package ch.epfl.rigel.astronomy;

import java.util.Objects;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * A Sun.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Sun extends CelestialObject {

    private final static float SUN_MAGNITUDE = -26.7f;
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;
    
    /**
     * Default constructor of a Sun : constructs a Sun from a given eclipticPos (EclipticCoordinates),
     * equatorialPos (EquatorialCoordinates), angularSize (float) and meanAnomaly (float).
     * Throws IAE if angularSize is negative, or if the equatorialPos or the eclipticPos are null (NullPointerException).
     * 
     * @param EclipticCoordinates (eclipticPos)
     * @param EquatorialCoordinates (equatorialPos)
     * @param float (angularSize)
     * @param float (meanAnomaly)
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, SUN_MAGNITUDE);
        Objects.requireNonNull(eclipticPos);
        this.eclipticPos = eclipticPos;
        this.meanAnomaly = meanAnomaly;
    }
    
    /**
     * Returns the EclipticCoordinates of the Sun.
     *
     * @return EclipticCoordinates (eclipticPos)
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }
    
    /**
     * Returns the mean anomaly of the Sun.
     *
     * @return double (meanAnomaly)
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}