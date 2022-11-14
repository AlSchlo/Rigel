package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

@FunctionalInterface
/**
 * A Time Accelerator.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public interface TimeAccelerator {
        
    /**
     * Calculates the new simulatedTime (ZonedDateTime) from an initial simulation time 
     * (ZonedDateTime) and the realElapsedTime IRL (in nanoseconds).
     * 
     * @param ZonedDateTime (initialSimulatedTime)
     * @param long (realElapsedTime)
     * @return ZonedDateTime (simulatedTime)
     */
    public ZonedDateTime adjust(ZonedDateTime simulatedInitialTime, long realElapsedTime);

    /**
     * Returns a continuous TimeAccelerator given an accelerationFactor.
     * 
     * @param int (advanceFrequence)
     * @param Duration (pace)
     * @return TimeAccelerator (discreteAccelerator)
     */
    public static TimeAccelerator continuous(int accelerationFactor) {
        return (simulatedInitialTime, realElapsedTime) -> simulatedInitialTime.plusNanos(accelerationFactor * realElapsedTime); 
    }
    
    /**
     * Returns a discrete TimeAccelerator given an advanceFrequence and
     * a pace (Duration).
     * 
     * @param int (advanceFrequence)
     * @param Duration (pace)
     * @return TimeAccelerator (discreteAccelerator)
     */
    public static TimeAccelerator discrete(int advanceFrequence, Duration pace) {
        return (simulatedInitialTime, realElapsedTime) -> simulatedInitialTime.plusNanos((advanceFrequence*realElapsedTime / 1_000_000_000) * pace.toNanos());
    }   
}