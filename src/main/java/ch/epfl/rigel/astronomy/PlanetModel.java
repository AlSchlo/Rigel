package ch.epfl.rigel.astronomy;

import java.util.List;


import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * A Physical Planet Model.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {
    
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
          0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
          0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
         1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
           9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.1354, 172.884833, 0.046321,
           19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);
        
    private final String name;
    private final double tropicalYear;
    private final double longitudeJ2010;
    private final double longitudePerigee;
    private final double eccentricity;
    private final double semiMajorAxis;

    private final double longitudeAscendingNode;
    private final double theta0AngularSize;
    private final double v0Magnitude;
    private final double cosOrbitInclination;
    private final double sinOrbitInclination;

    private final static double SUN_TROPICAL_YEAR = 365.242191;
    
    private PlanetModel(String name, double tropicalYear, double longitudeJ2010, double longitudePerigee, double eccentricity, double semiMajorAxis, double orbitInclination, double longitudeAscendingNode, double theta0AngularSize, double v0Magnitude) {
        this.name = name;
        this.tropicalYear = tropicalYear;
        this.longitudeJ2010 = Angle.ofDeg(longitudeJ2010);
        this.longitudePerigee = Angle.ofDeg(longitudePerigee);
        this.eccentricity = eccentricity;
        this.semiMajorAxis = semiMajorAxis;
        this.longitudeAscendingNode = Angle.ofDeg(longitudeAscendingNode);
        this.theta0AngularSize = Angle.ofArcsec(theta0AngularSize);
        this.v0Magnitude = v0Magnitude;
        this.cosOrbitInclination = Math.cos(Angle.ofDeg(orbitInclination));
        this.sinOrbitInclination = Math.sin(Angle.ofDeg(orbitInclination));
    }

    /**
     * An Immutable list of all the planets.
     */
    public final static List<PlanetModel> ALL = List.of(values());

    /**
     * Returns the Planet modelized by the numbers of days since J2010, using
     * the given conversion to get its EquatorialCoordinates from its EclipticCoordinates.
     * 
     * @param double (daysSinceJ2010)
     * @param EclipticToEquatorialConversion (eclipticToEquatorialConversion)
     * @return Planet (planet)
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) { 
        double [] planetValues = usefullValues(daysSinceJ2010);
        double radiusInEclipticPlan = planetValues[0];
        double longitudeInEclipticPlan = planetValues[1];
        double helioEclipticLatitude = planetValues[2];
        double radiusInOrbitPlan = planetValues[3];
        double longitudeInOrbitPlan = planetValues[4];
        double [] earthValues = EARTH.usefullValues(daysSinceJ2010);
        double R = earthValues[3];
        double L = earthValues[4];
        double eclipticLon = 0;
        double eclipticLat = 0;
        switch(this) {
        case MERCURY:
        case VENUS:
            eclipticLon = Math.PI + L + Math.atan2(radiusInEclipticPlan*Math.sin(L - longitudeInEclipticPlan), R - radiusInEclipticPlan*Math.cos(L - longitudeInEclipticPlan));
            break;
        case MARS:
        case JUPITER:
        case SATURN:
        case URANUS:
        case NEPTUNE:
            eclipticLon = longitudeInEclipticPlan + Math.atan2(R*Math.sin(longitudeInEclipticPlan - L), radiusInEclipticPlan - R*Math.cos(longitudeInEclipticPlan - L));
            break;
        default:
            break;
        }
        eclipticLat = Math.atan((radiusInEclipticPlan * Math.tan(helioEclipticLatitude) * Math.sin(eclipticLon - longitudeInEclipticPlan)) / (R*Math.sin(longitudeInEclipticPlan - L)));
        EclipticCoordinates eclCoords = EclipticCoordinates.of(Angle.normalizePositive(eclipticLon), eclipticLat);
        EquatorialCoordinates eqCoords = eclipticToEquatorialConversion.apply(eclCoords);
        double distanceFromEarth = Math.sqrt(R*R + radiusInOrbitPlan*radiusInOrbitPlan - 2*R*radiusInOrbitPlan*Math.cos(longitudeInOrbitPlan - L)*Math.cos(helioEclipticLatitude));
        double angularSize = theta0AngularSize / distanceFromEarth;
        double phase = (1 + Math.cos(eclipticLon - longitudeInOrbitPlan)) / 2;
        double magnitude = v0Magnitude + 5*Math.log10(radiusInOrbitPlan*distanceFromEarth/(Math.sqrt(phase)));
        return new Planet(name, eqCoords, (float) angularSize, (float) magnitude);
    }
    
    /**
     * Private method returning usefull values from any given planet and days since J2010.
     * Returns the results in the form of a 5-dimension array :
     * -Index 0 = radiusInEclipticPlan
     * -Index 1 = longitudeInEclipticPlan
     * -Index 2 = helioEclipticLatitude
     * -Index 3 = radiusInOrbitPlan
     * -Index 4 = longitudeInOrbitPlan
     * @param double (daysSinceJ2010)
     * @return double [] (values)
     */
    private double [] usefullValues(double daysSinceJ2010) {
        double [] val = new double[5];
        double meanAnomaly = Angle.TAU / SUN_TROPICAL_YEAR * daysSinceJ2010 / tropicalYear + longitudeJ2010 - longitudePerigee;
        double trueAnomaly = meanAnomaly + 2*eccentricity*Math.sin(meanAnomaly);
        double radiusInOrbitPlan = semiMajorAxis*(1 - eccentricity*eccentricity) / (1 + eccentricity*Math.cos(trueAnomaly));
        double longitudeInOrbitPlan = trueAnomaly + longitudePerigee;
        double helioEclipticLatitude = Math.asin(Math.sin(longitudeInOrbitPlan - longitudeAscendingNode)*sinOrbitInclination);
        val[0] = radiusInOrbitPlan*Math.cos(helioEclipticLatitude);
        val[1] = Math.atan2(Math.sin(longitudeInOrbitPlan - longitudeAscendingNode)*cosOrbitInclination, Math.cos(longitudeInOrbitPlan - longitudeAscendingNode)) + longitudeAscendingNode;
        val[2] = helioEclipticLatitude;
        val[3] = radiusInOrbitPlan;
        val[4] = longitudeInOrbitPlan;
        return val;
    }
}