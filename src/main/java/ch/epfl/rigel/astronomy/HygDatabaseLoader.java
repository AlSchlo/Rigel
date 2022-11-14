package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;;

/**
 * A Hygdatabase Loader.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {
    
    INSTANCE;

    /**
     * Loads stars from a given InputStream and adds them in the Builder.
     * Throws IOE in case of an IO error.
     * 
     * @param InputStream (inputStream)
     * @param Builder (builder)
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, Builder builder) throws IOException {     
            try(BufferedReader b = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")))) {
                b.lines().filter(s -> !s.isBlank() && !s.startsWith("#") && !s.startsWith("id"))
                         .forEachOrdered(s -> {
                             String [] st = s.split(",");
                             int hipparcosId = (st[Hyg.HIP.ordinal()].isEmpty()) ? 0 : Integer.parseInt(st[Hyg.HIP.ordinal()]);
                             String name = st[Hyg.PROPER.ordinal()];
                             if(name.isEmpty()) {
                               name = (st[Hyg.BAYER.ordinal()].isEmpty()) ? "?" : st[Hyg.BAYER.ordinal()];
                               name += " " + st[Hyg.CON.ordinal()];
                             }
                             EquatorialCoordinates eqCoords = EquatorialCoordinates.of(Double.parseDouble(st[Hyg.RARAD.ordinal()]), Double.parseDouble(st[Hyg.DECRAD.ordinal()]));
                             float magnitude = (st[Hyg.MAG.ordinal()].isEmpty()) ? 0f : Float.parseFloat(st[Hyg.MAG.ordinal()]);
                             float colorIndex = (st[Hyg.CI.ordinal()].isEmpty()) ? 0f : Float.parseFloat(st[Hyg.CI.ordinal()]);
                             Star star = new Star(hipparcosId, name, eqCoords, magnitude, colorIndex);
                             builder.addStar(star);
                         });
        }
    }
    
    private enum Hyg {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}