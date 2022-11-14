package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Abstract Spherical Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
abstract class SphericalCoordinates {

    private final double longitude;
    private final double latitude;

    /**
     * Default constructor of SphericalCoordinates : constructs SphericalCoordinates from a given
     * longitude and latitude in degrees.
     * 
     * @param double (longitude)
     * @param double (latitude)
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Returns the latitude in radiant.
     *
     * @return double (latitude)
     */
    double lat() {
        return latitude;
    }

    /**
     * Returns the latitude in degrees.
     *
     * @return double (latitudeDeg)
     */
    double latDeg() {
        return Angle.toDeg(latitude);
    }

    /**
     * Returns the longitude in radiant.
     *
     * @return double (longitude)
     */
    double lon() {
        return longitude;
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return double (longitudeDeg)
     */
    double lonDeg() {
        return Angle.toDeg(longitude);
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