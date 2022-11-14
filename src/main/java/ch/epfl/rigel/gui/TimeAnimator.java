package ch.epfl.rigel.gui;

import java.time.ZonedDateTime;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A TimeAnimator, animates the the DateTimeBean from a TimeAccelerator.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean dtb;
    private final ObjectProperty<TimeAccelerator> acceleratorProperty = new SimpleObjectProperty<>(null);
    private final ReadOnlyBooleanProperty runningProperty = new SimpleBooleanProperty(false);
    private boolean initialize;
    private ZonedDateTime zdt0;
    private long simulatedT0;

    /**
     * Default constructor of a TimeAnimator from a given DataTimeBean.
     * 
     * @param DateTimeBean (dtb)
     */
    public TimeAnimator(DateTimeBean dtb) {
        this.dtb = dtb;
        this.initialize = false;
        this.zdt0 = dtb.getZonedDateTime();
        this.simulatedT0 = 0;
    }

    @Override
    /**
     * Is called in every frame while the AnimationTimer is active.
     * Progresses the DateTimeBean by using the time accelerator. 
     * 
     * @param long (now)
     */
    public void handle(long now) {
       if(initialize) {
           this.zdt0 = dtb.getZonedDateTime();
           this.simulatedT0 = now;
           this.initialize = false;
       } else {
           dtb.setZonedDateTime(acceleratorProperty.get().adjust(zdt0, now - simulatedT0));
       }
    }
      
    @Override
    public void start() {
        super.start();
        ((SimpleBooleanProperty)runningProperty).set(true);
        this.initialize = true;
    }
    
    @Override
    public void stop() {
        super.stop();
        ((SimpleBooleanProperty)runningProperty).set(false);
    }

    /**
     * Returns the property containing the accelerator.
     * 
     * @return ObjectProperty<TimeAccelerator> (acceleratorProperty)
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return acceleratorProperty;
    }
    
    /** 
     * Accelerator getter.
     * 
     * @return TimeAccelerator (accelerator)
     */
    public TimeAccelerator getAccelerator() {
        return acceleratorProperty.get();
    }
    
    /**
     * Accelerator setter.
     * 
     * @param TimeAccelerator (newAccelerator)
     */
    public void setAccelerator(TimeAccelerator newAccelerator) {
        acceleratorProperty.set(newAccelerator);
    }
    
    /**
     * Returns the property containing the running state.
     * 
     * @return ReadOnlyBooleanProperty (runningProperty)
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return runningProperty;
    }
    
    /** 
     * Is running.
     * 
     * @return boolean (isRunning)
     */
    public boolean isRunning() {
        return runningProperty.get();
    }    
}