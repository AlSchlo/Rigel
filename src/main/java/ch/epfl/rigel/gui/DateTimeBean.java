package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A DateTimeBean representing an observable and modifiable ZonedDateTime.
 * Regroups a date (LocalDate), a time (LocalTime) and a time zone (ZoneId).
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class DateTimeBean {

    private final ObjectProperty<LocalDate> dateProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<LocalTime> timeProperty = new SimpleObjectProperty<>(null);
    private final ObjectProperty<ZoneId> zoneProperty = new SimpleObjectProperty<>(null);
            
    /**
     * Returns the property containing the date.
     * 
     * @return ObjectProperty<LocalDate> (dateProperty)
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return dateProperty;
    }
    
    /** 
     * Date getter.
     * 
     * @return LocaleDate (currentDate)
     */
    public LocalDate getDate() {
        return dateProperty.get();
    }
    
    /**
     * Date setter.
     * 
     * @param LocalDate (newDate)
     */
    public void setDate(LocalDate newDate) {
        dateProperty.set(newDate);
    }
    
    /**
     * Returns the property containing the time.
     * 
     * @return ObjectProperty<LocalTime> (timeProperty)
     */
    public ObjectProperty<LocalTime> timeProperty() {
        return timeProperty;
    }
    
    /** 
     * Time getter.
     * 
     * @return LocalTime (currentTime)
     */
    public LocalTime getTime() {
        return timeProperty.get();
    }
    
    /**
     * Time setter.
     * 
     * @param LocalTime (newTime)
     */
    public void setTime(LocalTime newTime) {
        timeProperty.set(newTime);
    }
    
    /**
     * Returns the property containing the time zone.
     * 
     * @return ObjectProperty<ZoneId> (zoneProperty)
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zoneProperty;
    }
    
    /** 
     * Zone getter.
     * 
     * @return ZoneId (currentZone)
     */
    public ZoneId getZone() {
        return zoneProperty.get();
    }
    
    /**
     * Zone setter.
     * 
     * @param ZoneId (newZone)
     */
    public void setZone(ZoneId newZone) {
        zoneProperty.set(newZone);
    }
    
    /**
     * Getter of the date, time and zone in the form of a ZonedDateTime instance.
     * 
     * @return ZonedDateTime (zdt)
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(dateProperty.get(), timeProperty.get(), zoneProperty.get());
    }
    
    /**
     * Setter of the date, time and zone in the form of a ZonedDateTime instance.
     * 
     * @param ZonedDateTime (zdt)
     */
    public void setZonedDateTime(ZonedDateTime zdt) {
        dateProperty.set(zdt.toLocalDate());
        timeProperty.set(zdt.toLocalTime());
        zoneProperty.set(zdt.getZone());
    }    
}