package ch.epfl.rigel.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import static ch.epfl.rigel.Preconditions.checkInInterval;


/**
 * Non Instantiable Class Used To Obtain A Black Body´s Temperature Given It´s
 * Temperature.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public class BlackBodyColor {
    
    private final static ClosedInterval LEGAL_TEMPERATURES = ClosedInterval.of(950, 40049);
    private final static Map<Integer, String> DATA_MAP = getData();
    private final static String BBR_COLOR_PATH = "/bbr_color.txt";
    private final static String ENCODING = "US-ASCII";
    
    private static Map<Integer, String> getData() {
        try (BufferedReader b = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(BBR_COLOR_PATH), Charset.forName(ENCODING)))) {
            Map<Integer, String> data = new HashMap<Integer, String>();
            b.lines().filter(s -> !s.startsWith("#") && s.contains("10deg"))
                     .forEachOrdered(s -> {
                         String [] str = s.stripLeading().split("\\s+");
                         data.put(Integer.parseInt(str[0]), str[12]);
                     });
            return Collections.unmodifiableMap(data);
        }      
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private BlackBodyColor() {
        // Non instantiable class
    }

    /**
     * Returns the Color (JavaFX Color) of a given temperature, according
     * to the BBR_COLOR database. Throws IAE if the temperature is not
     * inside the interval [950, 40049]. 
     * 
     * @throws IllegalArgumentException 
     * @param double (temperature)
     * @return Color (color)
     */
    public static Color ofTemperature(double temperature) {
        int truncated = (int) checkInInterval(LEGAL_TEMPERATURES, temperature);
        int shift = truncated % 100;
        int value = (shift >= 50) ? (truncated + (100 - shift)) : (truncated - shift);
        return Color.web(DATA_MAP.get(value));
    }
}