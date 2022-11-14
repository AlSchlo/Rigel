package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.math.ClosedInterval;
import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * A Star.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Star extends CelestialObject {

    private final static float STAR_ANGULAR_SIZE = 0f;
    private final static ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    private final int hipparcosId;
    @SuppressWarnings("unused")
    private final float colorIndex;
    private final int colorTemperature;
  
    /**
     * Default constructor of a Star : constructs a Star from a given hipparcosId (int),
     * name (String), equatorialPos (EquatorialCoordinates), magnitude (float) and colorIndex (float).
     * Throws IAE if the angularSize or hipparcosId are negative, if colorIndex is not contained inside the interval [-0.5;5.5]
     * or if the name or the equatorialPos are null (NullPointerException).
     * The angular size of a Star is supposed to be 0.
     * 
     * @param int (hipparcosId)
     * @param String (name)
     * @param EquatorialCoordinates (equatorialPos)
     * @param float (magnitude)
     * @param float (colorIndex)
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, STAR_ANGULAR_SIZE, magnitude);
        checkArgument(hipparcosId >= 0);
        this.hipparcosId = hipparcosId;
        this.colorIndex = (float)checkInInterval(COLOR_INDEX_INTERVAL, colorIndex);
        double temp = 0.92*colorIndex;
        this.colorTemperature = (int)(4600*(1/(temp + 1.7) + 1/(temp + 0.62)));
    }
    
    /**
     * Returns the name of the Star.
     *
     * @return int (hipparcosId)
     */
    public int hipparcosId() {
        return hipparcosId;
    }
   
    /**
     * Returns the colorTemperature of the Star in Kelvin (derived from its colorIndex).
     * Result is rounded to an integer using a default cast.
     *
     * @return int (colorTemperature)
     */
    public int colorTemperature() {
        return colorTemperature;
    }
}