package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Conversion From Ecliptic To Equatorial Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {
    
    private final double cosEclipticObliquity;
    private final double sinEclipticObliquity;
    private final static Polynomial P = Polynomial.of(0.00181, -0.00006, -46.815, 0);
    private final static double A = Angle.ofDMS(23, 26, 21.45);

    /**
     * Default constructor of EclipticToEquatorialConversion.
     * Only requires a given ZonedDateTime as parameter in order to calculate
     * the ecliptic obliquity of the earth at that given moment.
     * The instance created can then be used to perform convertions from EclipticCoordinates
     * to EquatorialCoordinates.
     * 
     * @param ZonedDateTime (when)
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {       
        double t = Epoch.J2000.julianCenturiesUntil(when);
        double inSeconds = P.at(t);
        double inRadiant =  Angle.ofArcsec(inSeconds) + A; 
        this.cosEclipticObliquity = Math.cos(inRadiant);
        this.sinEclipticObliquity = Math.sin(inRadiant);
    }

    /**
     * Performs the conversion from given EclipticCoordinates to EquatorialCoordinates.
     *  
     * @param EclipticCoordinates (ecl)
     * @return EquatorialCoordinates (eqCoords)
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {
        double longitude = ecl.lon();
        double lat = ecl.lat();
        double sinLongitude = Math.sin(longitude);
        double decl = Math.asin(Math.sin(lat)*cosEclipticObliquity + Math.cos(lat)*sinEclipticObliquity*sinLongitude); // Bijective Function
        double ra = Math.atan2((sinLongitude*cosEclipticObliquity - Math.tan(lat)*sinEclipticObliquity), Math.cos(longitude));
        return EquatorialCoordinates.of(Angle.normalizePositive(ra), decl);
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