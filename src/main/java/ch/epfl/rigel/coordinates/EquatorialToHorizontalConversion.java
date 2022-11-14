package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

/**
 * Conversion From Equatorial To Horizontal Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {
    
    private final double siderealTime;
    private final double cosLatitude;
    private final double sinLatitude;

    
    /**
     * Default constructor of EquatorialToHorizontalConversion.
     * Requires a given ZonedDateTime and GeographicCoordinates to compute the sidereal time.
     * The instance created can then be used to perform convertions from EquatorialCoordinates
     * to HorizontalCoordinates.
     * 
     * @param ZonedDateTime (when)
     * @param GeographicCoordinates (where)
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.siderealTime = SiderealTime.local(when, where);
        this.cosLatitude = Math.cos(where.lat());
        this.sinLatitude = Math.sin(where.lat());
    }
    
    /**
     * Performs the conversion from given EquatorialCoordinates to HorizontalCoordinates.
     *  
     * @param EquatorialCoordinates (equ)
     * @return HorizontalCoordinates (horCoords)
     */
    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double angleH = siderealTime - equ.ra();
        double dec = equ.dec();
        double sinDec = Math.sin(dec);
        double cosDec = Math.cos(dec);        
        double alt = Math.asin(sinDec*sinLatitude + cosDec*cosLatitude*Math.cos(angleH)); // Bijective function
        double az = Math.atan2(-cosDec*cosLatitude*Math.sin(angleH), sinDec - sinLatitude*Math.sin(alt));
        return HorizontalCoordinates.of(Angle.normalizePositive(az),alt);
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