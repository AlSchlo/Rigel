package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * A NamedTimeAccelerator enum.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum NamedTimeAccelerator {

    TIMES_1("1×", TimeAccelerator.continuous(1)), 
    TIMES_30("30×", TimeAccelerator.continuous(30)), 
    TIMES_300("300×", TimeAccelerator.continuous(300)),
    TIMES_3000("3000×", TimeAccelerator.continuous(3000)), 
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofDays(1))), 
    SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));
    
    private final String name;
    private final TimeAccelerator accelerator;
    
    private NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }
    
    /**
     * Returns the name of the NamedTimeAccelerator.
     * 
     * @return (String) name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the accelerator of the NamedTimeAccelerator.
     * 
     * @return (TimeAccelerator) accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }
    
    @Override
    /**
     * Redefinition of the toString method :
     * Returns the name of the NamedTimeAccelerator.
     * 
     * @return (String) name
     */
    public String toString() {
        return getName();
    }
}