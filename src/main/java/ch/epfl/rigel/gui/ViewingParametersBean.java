package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A Viewing Parameters Bean.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class ViewingParametersBean {

    private final ObjectProperty<HorizontalCoordinates> centerProperty;
    private final DoubleProperty fieldOfViewDegProperty;
    
    /**
     * Default constructor of a ViewingParametersBean. Creates an instance of ViewingParametersBean
     * with the given values as initial properties values. 
     * 
     * @param double (initialFOV) [in degrees]
     * @param HorizontalCoordinates (initialCenter)
     */
    public ViewingParametersBean(double initialFOV, HorizontalCoordinates initialCenter) {
        this.centerProperty = new SimpleObjectProperty<>(initialCenter);
        this.fieldOfViewDegProperty = new SimpleDoubleProperty(initialFOV);
    }
    
    /**
     * No argument constructor. Initializes the properties to 0 and null.
     *
     */
    public ViewingParametersBean() {
        this(0, null);
    }
    
    /**
     * Returns the property containing the center.
     * 
     * @return ObjectProperty<HorizontalCoordinates> (centerProperty)
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return centerProperty;
    }
    
    /** 
     * Center getter.
     * 
     * @return HorizontalCoordinates (currentCenter)
     */
    public HorizontalCoordinates getCenter() {
        return centerProperty.get();
    }
    
    /**
     * Center setter.
     * 
     * @param HorizontalCoordinates (newCenter)
     */
    public void setCenter(HorizontalCoordinates newCenter) {
        centerProperty.set(newCenter);
    }
    
    /**
     * Returns the property containing the field of view.
     * 
     * @return DoubleProperty (fieldOfViewDegProperty)
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDegProperty;
    }
    
    /** 
     * Field of view getter.
     * 
     * @return double (currentFieldOfViewDeg)
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDegProperty.get();
    }
    
    /** 
     * Field of view setter.
     * 
     * @param double (newFieldOfViewDeg)
     */
    public void setFieldOfViewDeg(double newFieldOfViewDeg) {
        fieldOfViewDegProperty.set(newFieldOfViewDeg);
    } 
}