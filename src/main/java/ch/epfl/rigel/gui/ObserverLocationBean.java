package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * An Observer Location Bean.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class ObserverLocationBean {

    private final DoubleProperty lonDegProperty;
    private final DoubleProperty latDegProperty;
    private final ObjectBinding<GeographicCoordinates> coordinatesProperty;
    
    /**
     * Default constructor of an ObserverLocationBean. Creates an instance of ObserverLocationBean
     * given a latitude and longitude in degrees. Creates the binding GeographicCoordinates -- (longitude / latitude).
     * 
     * @param double (initialLon)
     * @param double (initialLat)
     */
    public ObserverLocationBean(double initialLon, double initialLat) {
        this.lonDegProperty = new SimpleDoubleProperty(initialLon);
        this.latDegProperty = new SimpleDoubleProperty(initialLat);
        this.coordinatesProperty = Bindings.createObjectBinding(() -> (GeographicCoordinates.ofDeg(lonDegProperty.get(), latDegProperty.get())), lonDegProperty, latDegProperty);
    }
    
    /**
     * No argument constructor. Initializes the properties to 0.
     *
     */
    public ObserverLocationBean() {
        this(0, 0);
    }

    /**
     * Returns the property containing the coordinates.
     * 
     * @return ObjectProperty<GeographicCoordinates> (coordinatesProperty)
     */
    public ObjectBinding<GeographicCoordinates> coordinatesProperty() {
        return coordinatesProperty;
    }
    
    /** 
     * Coordinates getter.
     * 
     * @return GeographicCoordinates (currentCoordinates)
     */
    public GeographicCoordinates getCoordinates() {
        return coordinatesProperty.get();
    }
    
    /** 
     * Coordinates setter.
     * 
     * @param GeographicCoordinates (newCoordinates)
     */
    public void setCoordinates(GeographicCoordinates newCoordinates) {
        latDegProperty.set(newCoordinates.latDeg());
        lonDegProperty.set(newCoordinates.lonDeg());
    }
    
    /**
    * Returns the property containing the longitude.
    * 
    * @return DoubleProperty (lonDegProperty)
    */
   public DoubleProperty lonDegProperty() {
       return lonDegProperty;
   }
   
   /** 
    * Longitude getter.
    * 
    * @return double (currentLonDeg)
    */
   public double getLonDeg() {
       return lonDegProperty.get();
   }
   
   /** 
    * Longitude setter.
    * 
    * @param double (newLonDeg)
    */
   public void setLonDeg(double newLonDeg) {
       lonDegProperty.set(newLonDeg);
   }
   
   /**
    * Returns the property containing the latitude.
    * 
    * @return DoubleProperty (latDegProperty)
    */
   public DoubleProperty latDegProperty() {
       return latDegProperty;
   }
   
   /** 
    * Latitude getter.
    * 
    * @return double (currentLatDeg)
    */
   public double getLatDeg() {
       return latDegProperty.get();
   }
   
   /** 
    * Latitude setter.
    * 
    * @param double (newLatDeg)
    */
   public void setLatDeg(double newLatDeg) {
       latDegProperty.set(newLatDeg);
   }
}