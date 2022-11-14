package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * A Physical Sun Model.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    
    SUN(279.557208, 283.112438, 0.016705, 365.242191, 0.533128);
    
    private final double longitudeJ2010;
    private final double longitudePerigee;
    private final double eccentricity;
    private final double tropicalYear;
    private final double theta0AngularSize;

    private SunModel(double longitudeJ2010, double longitudePerigee, double eccentricity, double tropicalYear, double theta0AngularSize) {
        this.longitudeJ2010 = Angle.ofDeg(longitudeJ2010);
        this.longitudePerigee = Angle.ofDeg(longitudePerigee);
        this.eccentricity = eccentricity;
        this.tropicalYear = tropicalYear;
        this.theta0AngularSize = Angle.ofDeg(theta0AngularSize);
    }
        
    /**
     * Returns the Sun modelized by the numbers of days since J2010, using
     * the given conversion to get its EquatorialCoordinates from its EclipticCoordinates.
     * 
     * @param double (daysSinceJ2010)
     * @param EclipticToEquatorialConversion (eclipticToEquatorialConversion)
     * @return Sun (sun)
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = Angle.TAU / tropicalYear * daysSinceJ2010 + longitudeJ2010 - longitudePerigee;
        double trueAnomaly = meanAnomaly + 2*eccentricity*Math.sin(meanAnomaly);
        double eclipticLon = trueAnomaly + longitudePerigee;
        EclipticCoordinates eclCoords = EclipticCoordinates.of(Angle.normalizePositive(eclipticLon), 0);
        EquatorialCoordinates eqCoords = eclipticToEquatorialConversion.apply(eclCoords);
        double angularSize = theta0AngularSize*((1+eccentricity*Math.cos(trueAnomaly))/(1-eccentricity*eccentricity));
        return new Sun(eclCoords, eqCoords, (float)angularSize, (float)Angle.normalizePositive(meanAnomaly));
    }
}