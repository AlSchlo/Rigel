package ch.epfl.rigel.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * A Bonus Parameters Bean.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class UsersParametersBean {

    private final BooleanProperty asterismsOnProperty;
    private final BooleanProperty trackerOnProperty;
    private final BooleanProperty gridOnProperty;
    
    /**
     * Specific BonusParametersBean constructor.
     * @param boolean (initialAsterismsOn)
     */
    public UsersParametersBean(boolean initialAsterismsOn, boolean initialTrackerOn, boolean initialGridOn) {
        this.asterismsOnProperty = new SimpleBooleanProperty(initialAsterismsOn);
        this.trackerOnProperty = new SimpleBooleanProperty(initialTrackerOn);
        this.gridOnProperty = new SimpleBooleanProperty(initialGridOn); 
    }
    
    /**
     * Default constructor of a BonusParametersBean. Initializes the properties to 0, false and null.
     */
    public UsersParametersBean() {
        this(false, false, false);
    }
    
    /**
     * Setter of the asterism property state.
     * @param boolean (newValue)
     */
    public void setAsterismsOn(boolean newValue) {
        asterismsOnProperty.set(newValue);
    }
    
    /**
     * Getter of the asterism property state.
     * @return boolean (currentValue)
     */
    public boolean isAsterismsOn() {
        return asterismsOnProperty.get();
    }
    
    /**
     * Getter of the asterism property..
     * @return BooleanProperty (asterismsOnProperty)
     */
    public BooleanProperty asterismsOnProperty() {
        return asterismsOnProperty;
    }
    
    /**
     * Setter of the tracker property state.
     * @param boolean (newValue)
     */
    public void setTrackerOn(boolean newValue) {
        trackerOnProperty.set(newValue);
    }
    
    /**
     * Getter of the tracker property state.
     * @return boolean (currentValue)
     */
    public boolean isTrackerOn() {
        return trackerOnProperty.get();
    }
    
    /**
     * Getter of the tracker property.
     * @return BooleanProperty (trackerOnProperty)
     */
    public BooleanProperty trackerOnProperty() {
        return trackerOnProperty;
    }
    
    /**
     * Setter of the grid property state.
     * @param boolean (newValue)
     */
    public void setGridOn(boolean newValue) {
        gridOnProperty.set(newValue);
    }
    
    /**
     * Getter of the grid property state.
     * @return boolean (currentValue)
     */
    public boolean isGridOn() {
        return gridOnProperty.get();
    }
    
    /**
     * Getter of the grid property.
     * @return BooleanProperty (gridOnProperty)
     */
    public BooleanProperty gridOnProperty() {
        return gridOnProperty;
    }
}