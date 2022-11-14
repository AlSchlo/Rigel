package ch.epfl.rigel.astronomy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * A 2-Dimensional Observed Sky Containing All The Celestial Objects And Their
 * Respective Cartesian Coordinates.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class ObservedSky {

    private final StarCatalogue starCatalogue;
    private final StereographicProjection stereoProj;
    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;
    private final Map<CelestialObject, CartesianCoordinates> positions; 
    
    /**
     * Default constructor of an ObservedSky. Creates an ObservedSky at a given moment (ZonedDateTime) and position (GeographicCoordinates),
     * as well as a given StereographicProjection and StarCatalogue which represent the state of the sky.
     * 
     * @param ZonedDateTime (zdt)
     * @param GeographicCoordinates (geoCoords)
     * @param StereographicProjection (stereoProj)
     * @param StarCatalogue (starCatalogue)
     */
    public ObservedSky(ZonedDateTime zdt, GeographicCoordinates geoCoords, StereographicProjection stereoProj, StarCatalogue starCatalogue) {        
        this.stereoProj = stereoProj;
        this.starCatalogue = starCatalogue;
        double daysUntilJ2010 = Epoch.J2010.daysUntil(zdt);
        EclipticToEquatorialConversion eclToEqConversion = new EclipticToEquatorialConversion(zdt);
        EquatorialToHorizontalConversion eqToHorConversion = new EquatorialToHorizontalConversion(zdt, geoCoords);
        Function<EquatorialCoordinates, CartesianCoordinates> eqToCart = eqToHorConversion.andThen(stereoProj);
        Map<CelestialObject, CartesianCoordinates> intPositions = new HashMap<CelestialObject, CartesianCoordinates>();
        this.sun = SunModel.SUN.at(daysUntilJ2010, eclToEqConversion);
        intPositions.put(sun, eqToCart.apply(sun.equatorialPos()));
        this.moon = MoonModel.MOON.at(daysUntilJ2010, eclToEqConversion);
        intPositions.put(moon, eqToCart.apply(moon.equatorialPos()));
        List<Planet> planetsInt = new ArrayList<Planet>();
        for(PlanetModel model : PlanetModel.ALL) {
            if(!model.equals(PlanetModel.EARTH)) {
                Planet p = model.at(daysUntilJ2010, eclToEqConversion);
                planetsInt.add(p);
            }
        }
        this.planets = Collections.unmodifiableList(planetsInt);
        for(Planet planet : planets) {
            intPositions.put(planet, eqToCart.apply(planet.equatorialPos()));
        }
        for(Star star : starCatalogue.stars()) {
            intPositions.put(star, eqToCart.apply(star.equatorialPos()));
        }
        this.positions = Collections.unmodifiableMap(intPositions);
    }
    
    /**
     * Returns the stereoProj (StereographicProjection) used to create the ObservedSky.
     * 
     * @return StereographicProjection (stereoProj)
     */
    public StereographicProjection projection() {
        return stereoProj;
    }
    
    /**
     * Returns the sun in the ObservedSky.
     * 
     * @return Sun (sun)
     */
    public Sun sun() {
        return sun;
    }
    
    /**
     * Returns the sunPosition (CartesianCoordinates) in the ObservedSky.
     * 
     * @return CartesianCoordinates (sunPosition)
     */
    public CartesianCoordinates sunPosition() {
        return positions.get(sun);
    }
    
    /**
     * Returns the moon in the ObservedSky.
     * 
     * @return Moon (moon)
     */
    public Moon moon() {
        return moon;
    }
    
    /**
     * Returns the moonPosition (CartesianCoordinates) in the ObservedSky.
     * 
     * @return CartesianCoordinates (moonPosition)
     */
    public CartesianCoordinates moonPosition() {
        return positions.get(moon);
    }
    
    /**
     * Returns the list of planets in the ObservedSky.
     * 
     * @return List<Planet> (planets)
     */
    public List<Planet> planets() {
        return planets;
    }
    
    /**
     * <pre>Returns the planetPositions in the ObservedSky in the form of a primitive Array.
     * The array has 14 indexes which represent :
     * -[0] : X coordinate of Mercury  [1] : Y coordinate of Mercury
     * -[2] : X coordinate of Venus    [3] : Y coordinate of Venus
     * -[4] : X coordinate of Mars     [5] : Y coordinate of Mars
     * -[6] : X coordinate of Jupiter  [7] : Y coordinate of Jupiter
     * -[8] : X coordinate of Saturn   [9] : Y coordinate of Saturn
     * -[10] : X coordinate of Uranus   [11] : Y coordinate of Uranus
     * -[12] : X coordinate of Neptune  [13] : Y coordinate of Neptune
     * 
     * @return [] double (planetPositions)</pre>
     */
    public double [] planetPositions() {
        return extractPosition(planets);
    }
    
    /**
     * Returns the list of stars in the ObservedSky.
     * 
     * @return List<Star> (stars)
     */
    public List<Star> stars() {
        return starCatalogue.stars();
    }
    
    /**
     * <pre>Returns the starPositions in the ObservedSky in the form of a primitive Array.
     * The array has stars.size() * 2 indexes which represent :
     * -[0] : X coordinate of 1st star [1] : Y coordinate of 1st star
     * -[2] : X coordinate of 2nd star [3] : Y coordinate of 2nd star
     * ...
     * ...
     * -[stars.size()-2] : X coordinate of last star  [stars.size()-1] : Y coordinate of last star
     * 
     * @return [] double (planetPositions)</pre>
     */
    public double [] starPositions() {
        return extractPosition(starCatalogue.stars());
    }
    
    private double[] extractPosition(List<? extends CelestialObject> c) {
        double [] array = new double[2*c.size()];
        int counter = 0;
        for(CelestialObject o : c) {
            array[counter++] = positions.get(o).x();
            array[counter++] = positions.get(o).y();
        }
        return array;
    }
    
    /**
     * Returns the set of asterisms in the ObservedSky.
     * 
     * @return Set<Asterism> (asterisms)
     */
    public Set<Asterism> asterisms() {
        return starCatalogue.asterisms();
    }
    
    /**
     * Returns a list of indexes (Integers) of the stars in the asterism
     * from the list of stars of the ObservedSky`s catalogue.
     * 
     * @return List<Integer> (list)
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        return starCatalogue.asterismIndices(asterism);
    }
    
    /**
     * Returns the closest CelestialObject (Optional) from given CartesianCoordinates in the ObservedSky. The closest CelestialObject must be smaller than a given
     * distance (otherwise an Empty Optional is returned).
     * 
     * @param CartesianCoordinates (cartCoords)
     * @param double (distance)
     * @return Optional<CelestialObject> (celestObj)
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartCoords, double distance) {
        double closestDistance = Double.MAX_VALUE;
        CelestialObject closestObject = null;
        ClosedInterval xInterval = ClosedInterval.of(cartCoords.x() - distance, cartCoords.x() + distance);
        ClosedInterval yInterval = ClosedInterval.of(cartCoords.y() - distance, cartCoords.y() + distance);
        double dist = 0;
        for(CelestialObject obj : positions.keySet()) {
          if(xInterval.contains(positions.get(obj).x()) &&  yInterval.contains(positions.get(obj).y())) {
            if((dist = cartCoords.distanceToSquare(positions.get(obj))) < closestDistance) {
                closestDistance = dist;
                closestObject = obj;
            }
          }
        }
        return ((closestDistance > distance) ? Optional.empty() : Optional.of(closestObject));
    }        
}