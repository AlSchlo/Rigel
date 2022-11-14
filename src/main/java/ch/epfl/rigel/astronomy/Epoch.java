package ch.epfl.rigel.astronomy;

import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * An Epoch Enumeration Consisting Of The Two Astrological Epochs :
 * -J2000
 * -J2010
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum Epoch {
        
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneOffset.UTC)),
    J2010((ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)).minusDays(1));
   
    private final ZonedDateTime t;
    private final static int DAYS_IN_JUSTINIAN_CENTURY = 36525;
    private final static int MILLIS_IN_DAY = 24*60*60*1000;
    
    private Epoch(ZonedDateTime t) {
        this.t = t;
    }
    
    /**
     * Returns the number of days between a given ZonedDateTime and an epoch(this).
     * 
     * @param ZonedDateTime (when)
     * @return double (nbDays)
     */
    public double daysUntil(ZonedDateTime when) {
      return t.until(when, ChronoUnit.MILLIS) / (double)MILLIS_IN_DAY;
    }
    
    /**
     * Returns the number of Julian centuries between a given ZonedDateTime and an epoch(this).
     * 
     * @param ZonedDateTime (when)
     * @return double (nbJulianCenturies)
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        return daysUntil(when)/DAYS_IN_JUSTINIAN_CENTURY;
    }   
}