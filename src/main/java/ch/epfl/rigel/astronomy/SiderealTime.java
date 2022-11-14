package ch.epfl.rigel.astronomy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * A Non Instantiable Class Representing The Astrological Sidereal Time.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class SiderealTime {

    private final static int MILLIS_IN_HOUR = 1000*60*60;
    private final static Polynomial P = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private final static double C = 1.002737909;

    
    private SiderealTime() {
        // Non instantiable class
    }
    
    /**
     * Returns the siderealtime of Greenwich from a given ZonedDateTime (when) in radiant.
     * Result normalized in the interval [0, TAU[.
     * The reference is J2000 Epoch.
     * 
     * @param ZonedDateTime (when)
     * @return double (siderealtime)
     */
    public static double greenwich(ZonedDateTime when) {
        ZonedDateTime zdtGreenwich = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime zdtGreenwich0h = zdtGreenwich.truncatedTo(ChronoUnit.DAYS);
        double sg = P.at(Epoch.J2000.julianCenturiesUntil(zdtGreenwich0h)) + C*(zdtGreenwich0h.until(zdtGreenwich, ChronoUnit.MILLIS)/(double)MILLIS_IN_HOUR);
        return Angle.normalizePositive(Angle.ofHr(sg));
    }
        
    
    /**
     * Returns the siderealtime of given GeographicCoordinates (where)
     * from a given ZonedDateTime (when) in radiant.
     * Result normalized in the interval [0, TAU[.
     * The reference is J2000 Epoch.
     * 
     * @param ZonedDateTime (when)
     * @param GeographicCoordinates (where)
     * @return double (siderealtime)
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }   
}