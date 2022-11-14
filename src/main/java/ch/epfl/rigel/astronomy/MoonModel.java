package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * A Physical Moon Model.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON(91.929336, 130.143076, 291.682547, 5.145396, 0.0549, 0.5181);
    
    private final double meanLongitude;
    private final double perigeeMeanLongitude;
    private final double longitudeAscendingNode;
    private final double eccentricity;
    private final double theta0AngularSize;
    private final double cosOrbitInclination;
    private final double sinOrbitInclination;

    private MoonModel(double meanLongitude, double perigeeMeanLongitude, double longitudeAscendingNode, double orbitInclination, double eccentricity, double theta0AngularSize) {
        this.meanLongitude = Angle.ofDeg(meanLongitude);
        this.perigeeMeanLongitude = Angle.ofDeg(perigeeMeanLongitude);
        this.longitudeAscendingNode = Angle.ofDeg(longitudeAscendingNode);
        this.eccentricity = eccentricity;
        this.theta0AngularSize = Angle.ofDeg(theta0AngularSize);
        this.cosOrbitInclination = Math.cos(Angle.ofDeg(orbitInclination));
        this.sinOrbitInclination = Math.sin(Angle.ofDeg(orbitInclination)); 
    }

    /**
     * Returns the Moon modelized by the numbers of days since J2010, using
     * the given conversion to get its EquatorialCoordinates from its EclipticCoordinates.
     * 
     * @param double (daysSinceJ2010)
     * @param EclipticToEquatorialConversion (eclipticToEquatorialConversion)
     * @return Moon (moon)
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun s = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sunMeanAnomaly = s.meanAnomaly();
        double sunEclipticLongitude = s.eclipticPos().lon();
        double meanOrbitalLongitude = Angle.ofDeg(13.1763966) * daysSinceJ2010 + meanLongitude;
        double meanAnomaly = meanOrbitalLongitude - Angle.ofDeg(0.1114041) * daysSinceJ2010 - perigeeMeanLongitude;
        double Ev = Angle.ofDeg(1.2739) * Math.sin(2 * (meanOrbitalLongitude - sunEclipticLongitude) - meanAnomaly);
        double sinSunMeanAnomaly = Math.sin(sunMeanAnomaly);
        double Ae = Angle.ofDeg(0.1858) *sinSunMeanAnomaly;
        double A3 = Angle.ofDeg(0.37) * sinSunMeanAnomaly;
        double correctedMeanAnomaly = meanAnomaly + Ev - Ae - A3;
        double Ec = Angle.ofDeg(6.2886) * Math.sin(correctedMeanAnomaly);
        double A4 = Angle.ofDeg(0.214) * Math.sin(2 * correctedMeanAnomaly);
        double correctedMeanOrbitalLongitude = meanOrbitalLongitude + Ev + Ec - Ae + A4;
        double V = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedMeanOrbitalLongitude - sunEclipticLongitude));
        double trueOrbitalLongitude = correctedMeanOrbitalLongitude + V;
        double meanAscendingNodeLongitude = longitudeAscendingNode - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double correctedAscendingNodeLongitude = meanAscendingNodeLongitude - Angle.ofDeg(0.16) * sinSunMeanAnomaly;
        double temp = Math.sin(trueOrbitalLongitude - correctedAscendingNodeLongitude);
        double eclipticLongitude = Math.atan2(temp * cosOrbitInclination, Math.cos(trueOrbitalLongitude - correctedAscendingNodeLongitude)) + correctedAscendingNodeLongitude;
        double eclipticLatitude = Math.asin(temp * sinOrbitInclination);
        EclipticCoordinates eclCoords = EclipticCoordinates.of(Angle.normalizePositive(eclipticLongitude), eclipticLatitude);
        EquatorialCoordinates eqCoords = eclipticToEquatorialConversion.apply(eclCoords);
        double phase = (1 - Math.cos(trueOrbitalLongitude - sunEclipticLongitude)) / 2;
        double moonEarthDistance = (1 - eccentricity * eccentricity) / (1 + eccentricity*Math.cos(correctedMeanAnomaly + Ec));
        double angularSize = theta0AngularSize / moonEarthDistance;
        return new Moon(eqCoords, (float) angularSize, 0, (float) phase);
    }   
}