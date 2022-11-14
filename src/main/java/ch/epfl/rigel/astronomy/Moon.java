package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

/**
 * A Moon.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Moon extends CelestialObject {

    private final float phase;
    private final static ClosedInterval I = ClosedInterval.of(0, 1);
   
    /**
     * Default constructor of a Moon : constructs a Moon from a given
     * equatorialPos (EquatorialCoordinates), angularSize (float), magnitude (float) and phase (float).
     * Throws IAE if the angularSize is negative, or if the equatorialPos is null (NullPointerException).
     * The phase also has to be contained inside the interval [0;1].
     * 
     * @param EquatorialCoordinates (equatorialPos)
     * @param float (angularSize)
     * @param float (magnitude)
     * @param float (phase)
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        this.phase = (float)checkInInterval(I, phase);
    }
    
    /**
     * Returns a brief informative text concerning the Moon, for the user.
     * Information available :
     * - Name
     * - Phase of the moon (one digit after comma of precision).
     * 
     * @return String (info)
     */
    @Override
    public String info() {
        float percentage = phase*100;
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append(String.format(Locale.ROOT, " (%.1f", percentage));
        sb.append("%)");
        return sb.toString();
    }
}