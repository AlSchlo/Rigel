package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A Catalogue of all the stars and asterisms.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class StarCatalogue {

    private final List<Star> stars;
    private final Map <Asterism, List<Integer>> map;
    
    /**
     * Default constructor of a StarCatalogue. Creates a StarCatalogue from a given list of stars and asterisms.
     * Throws IEA if not all the asterisms are contained in the general list of stars. 
     * 
     * @param List<Star> (stars)
     * @param List<Asterism> (asterisms)
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        Map <Asterism, List<Integer>> map = new HashMap<Asterism, List<Integer>>();
        Map <Star, Integer> indexOfStar = new HashMap<Star, Integer>();
        for(Star s : stars) indexOfStar.put(s, indexOfStar.size());
        for(Asterism a : asterisms) {
            List<Integer> l = new ArrayList<Integer>();
            for(Star s : a.stars()) {
                checkArgument(indexOfStar.containsKey(s));
                l.add(indexOfStar.get(s));
            }
            map.put(a, Collections.unmodifiableList(l));
        }
        this.stars = List.copyOf(stars);
        this.map = Collections.unmodifiableMap(map);
    }
    
    /**
     * Returns an unmodifiable list of the stars in the StarCatalogue.
     * 
     * @return List<Star> (stars)
     */
    public List<Star> stars() {
        return stars;
    }
    
    /**
     * Returns an unmodifiable set of the asterisms in the StarCatalogue.
     * 
     * @return Set<Asterism> (asterisms)
     */
    public Set<Asterism> asterisms() {
        return map.keySet();
    }
    
    /**
     * Returns an unmodifiable list of indexes (Integers) of the stars in the asterism
     * from the list of stars of the Catalogue.
     * 
     * @return List<Integer> (list)
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        checkArgument(map.containsKey(asterism));
        return map.get(asterism);
    }
    
    /**
     * Static imbricated class representing a StarCatalogue.Builder.
     * Mutable version of StarCatalogue. 
     *
     * @author Paul Guillon (314517)
     * @author Alexis Schlomer (315616)
     */
    public final static class Builder {
        
        private final List<Star> stars;
        private final List<Asterism> asterisms;
        
        /**
         * Default constructor of a StarCatalogue.Builder.
         * Creates an empty list of stars and asterisms. 
         */
        public Builder() {
            this.stars = new ArrayList<Star>();
            this.asterisms = new ArrayList<Asterism>();
        }
        
        /**
         * Adds a star to the list of stars under construction.
         * @param Star (star)
         * @return Builder (this)
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }
        
        /**
         * Adds an asterism to the list of asterisms under construction.
         * @param Asterism (asterism)
         * @return Builder (this)
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }
        
        /**
         * Returns an unmodifiable view of the list of stars under construction.
         * @return List<Star> (stars)
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(stars);
        }
        
        /**
         * Returns an unmodifiable view of the list of asterisms under construction.
         * @return List<Asterism> (asterisms)
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }
        
        /**
         * Loads the given information inside an InputStream to the Builder (this) using a given Loader.
         * Throws IOE in case of an IO error. 
         * 
         * @param InputStream (InputStream)
         * @param Loader (loader)
         * @return Builder (this)
         * @throws IOException
         */        
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }
        
        /**
         * Builds an immutable version of a StarCatalogue.
         * @return StarCatalogue (catalogue)
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars, asterisms);
        }
    }
    
    /**
     * Imbricated interface representing a StarCatalogue.Loader.
     *
     * @author Paul Guillon (314517)
     * @author Alexis Schlomer (315616)
     */
    public interface Loader {
        
        /**
         * Loads stars / asterisms from a given InputStream and adds them in the Builder.
         * Throws IOE in case of an IO error.
         * 
         * @param InputStream (inputStream)
         * @param Builder (builder)
         * @throws IOException
         */
        public abstract void load(InputStream inputStream, Builder builder) throws IOException;
    }  
}