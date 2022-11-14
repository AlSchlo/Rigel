package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.StarCatalogue.Builder;

/**
 * An Asterism Loader.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    /**
     * Loads asterisms from a given InputStream and adds them in the Builder.
     * Throws IOE in case of an IO error.
     * 
     * @param InputStream (inputStream)
     * @param Builder (builder)
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, Builder builder) throws IOException {

            try(BufferedReader b = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")))) {
                b.lines().filter(s -> !s.isBlank())
                         .forEachOrdered(s -> {
                             String[] st = s.split(",");
                             List<Star> l = new ArrayList<Star>();
                             Map<Integer, Star> map = new HashMap<>();
                             for(Star star : builder.stars()) map.put(star.hipparcosId(), star);
                             for (int i = 0; i < st.length; ++i) {
                                 l.add(map.get(Integer.parseInt(st[i])));
                             }
                             builder.addAsterism(new Asterism(l)); 
                             });
        }
    }
}