package ch.epfl.rigel.gui;

import java.util.Optional;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Transform;


/**
 * A Sky Canvas Manager.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class SkyCanvasManager {
       
    private final static ClosedInterval ALT_INTERVAL = ClosedInterval.of(5, 90);
    private final static ClosedInterval FOV_INTERVAL = ClosedInterval.of(30, 300);
    private final static int VERTICAL_PACE = 5;
    private final static int HORIZONTAL_PACE = 10;
    private final static double DISTANCE_DET = 10;
    private final static int DEGREES_IN_CERCLE = 360;
    private final static Transform DEFAULT_DEBUG_TRANS = Transform.affine(1300, 0, 0, -1300, 400, 300);
    
    //Drawing Properties
    private final UsersParametersBean upb;
    //The canvas and its painter
    private final Canvas canvas = new Canvas(800, 600);
    private final SkyCanvasPainter painter = new SkyCanvasPainter(canvas);
    
    //Main Properties
    private final ObjectBinding<StereographicProjection> projectionProperty;
    private final ObjectBinding<Transform> planeToCanvasProperty;
    private final ObjectBinding<ObservedSky> observedSkyProperty;   
    //Mouse
    private final ObjectProperty<Point2D> mousePositionProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPositionProperty;
    private final ObjectBinding<CartesianCoordinates> mouseCartesianPositionProperty;
    //Public
    private final DoubleBinding mouseAzDegProperty;
    private final DoubleBinding mouseAltDegProperty;
    private final ObjectBinding<Optional<CelestialObject>> objectUnderMouseProperty;
        
    /**
     * Default constructor of a SkyCanvasManager. Creates an instance of SkyCanvasManager
     * given an ObservedSky, a DateTimeBean, an ObserverLocationBean, a ViewingParametersBean and a BonusParametersBean.
     * 
     * @param ObservedSky (sky)
     * @param DateTimeBean (dtb)
     * @param ObserverLocationBean (olb)
     * @param ViewingParametersBean (vpb)
     * @param BonusParametersBean (bpb)
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dtb, ObserverLocationBean olb, ViewingParametersBean vpb, UsersParametersBean upb) {
        //Main Bindings
        this.upb = upb;
        this.projectionProperty = Bindings.createObjectBinding(() -> new StereographicProjection(vpb.centerProperty().get()) , vpb.centerProperty());
        this.planeToCanvasProperty = Bindings.createObjectBinding(() -> {
                int dilatation = (int) (canvas.widthProperty().get() / projectionProperty.get().applyToAngle(Angle.ofDeg(vpb.fieldOfViewDegProperty().get())));
                Transform t = Transform.affine(dilatation, 0, 0, -dilatation, canvas.widthProperty().get() / 2, canvas.heightProperty().get() / 2);
                if(t.determinant() == 0) return DEFAULT_DEBUG_TRANS;
                return t;
            }, canvas.widthProperty(), canvas.heightProperty(), vpb.fieldOfViewDegProperty(), projectionProperty);
        this.observedSkyProperty = Bindings.createObjectBinding(() -> new ObservedSky(dtb.getZonedDateTime(),
                olb.coordinatesProperty().get(), projectionProperty.get(), catalogue), dtb.dateProperty(), dtb.timeProperty(), dtb.zoneProperty(),
                olb.coordinatesProperty(), projectionProperty);
        //Mouse Bindings
        this.canvas.setOnMouseMoved(e -> {
            if(canvas.isHover()) mousePositionProperty.set(new Point2D(e.getX(), e.getY()));  
        });
        this.mouseCartesianPositionProperty = Bindings.createObjectBinding(() -> {
            Point2D point = planeToCanvasProperty.get().inverseTransform(mousePositionProperty.get());
            return CartesianCoordinates.of(point.getX(), point.getY());
        }, mousePositionProperty, planeToCanvasProperty, projectionProperty);
        this.mouseHorizontalPositionProperty = Bindings.createObjectBinding(() -> projectionProperty.get().inverseApply(mouseCartesianPositionProperty.get()), projectionProperty, mouseCartesianPositionProperty);
        this.mouseAzDegProperty = Bindings.createDoubleBinding(() -> mouseHorizontalPositionProperty.get().azDeg(), mouseHorizontalPositionProperty);
        this.mouseAltDegProperty = Bindings.createDoubleBinding(() -> mouseHorizontalPositionProperty.get().altDeg(), mouseHorizontalPositionProperty);
        this.objectUnderMouseProperty = Bindings.createObjectBinding(() -> observedSkyProperty.get().objectClosestTo(mouseCartesianPositionProperty.get(), DISTANCE_DET), 
                mouseCartesianPositionProperty, observedSkyProperty);
        this.planeToCanvasProperty.addListener(o -> drawSky());
        this.observedSkyProperty.addListener(o -> drawSky());
        this.upb.asterismsOnProperty().addListener(o -> drawSky());
        this.upb.gridOnProperty().addListener(o -> drawSky());
        this.upb.trackerOnProperty().addListener(o -> drawSky());
        this.mouseHorizontalPositionProperty.addListener(o -> {
            if(upb.isTrackerOn()) drawSky();
        });
       //Movement Interaction
        this.canvas.setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.PRIMARY) canvas.requestFocus();
            });
        this.canvas.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                double azDeg = vpb.getCenter().azDeg();
                double altDeg = vpb.getCenter().altDeg();
                if(e.getCode() == KeyCode.LEFT) azDeg = ((azDeg - HORIZONTAL_PACE) + DEGREES_IN_CERCLE) % DEGREES_IN_CERCLE;
                else if(e.getCode() == KeyCode.RIGHT) azDeg = ((azDeg + HORIZONTAL_PACE) + DEGREES_IN_CERCLE) % DEGREES_IN_CERCLE;
                else if(e.getCode() == KeyCode.UP) altDeg = ALT_INTERVAL.clip(altDeg + VERTICAL_PACE);
                else if(e.getCode() == KeyCode.DOWN) altDeg = ALT_INTERVAL.clip(altDeg - VERTICAL_PACE);
                vpb.setCenter(HorizontalCoordinates.ofDeg(azDeg, altDeg));
            } else if(e.getCode() == KeyCode.SHIFT) upb.setTrackerOn(true);
            e.consume();            
            });
        this.canvas.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.SHIFT) upb.setTrackerOn(false);
        });
        this.canvas.setOnScroll(e -> {
                double change = Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY()) ? e.getDeltaX() : e.getDeltaY();
                vpb.setFieldOfViewDeg(FOV_INTERVAL.clip(vpb.getFieldOfViewDeg() - change));
        });
    }
    
    /**
     * Returns the property containing the mouseAzDeg.
     * 
     * @return DoubleBinding (mouseAzDegProperty)
     */
    public DoubleBinding mouseAzDegProperty() {
        return mouseAzDegProperty;
    }
    
    /** 
     * MouseAz getter.
     * 
     * @return double (currentMouseAz)
     */
    public double getMouseAzDeg() {
        return mouseAzDegProperty.get();
    }
    
    /**
     * Returns the property containing the mouseAltDeg.
     * 
     * @return DoubleBinding (mouseAltDegProperty)
     */
    public DoubleBinding mouseAltDegProperty() {
        return mouseAltDegProperty;
    }
    
    /** 
     * MouseAlt getter.
     * 
     * @return double (currentMouseAlt)
     */
    public double getMouseAltDeg() {
        return mouseAltDegProperty.get();
    }
    
    /**
     * Returns the property containing the objectUnderMouse.
     * 
     * @return ObjectBinding<Optional<CelestialObject>> (objectUnderMouseProperty)
     */
    public ObjectBinding<Optional<CelestialObject>> objectUnderMouseProperty() {
        return objectUnderMouseProperty;
    }
    
    /** 
     * Object under mouse getter.
     * 
     * @return <Optional<CelestialObject>> (currentObjectUnderMouse)
     */
    public Optional<CelestialObject> getObjectUnderMouse() {
        return objectUnderMouseProperty.get();
    } 
    
    /**
     * Canvas getter.
     * 
     * @return Canvas (canvas)
     */
    public Canvas canvas() {
        return canvas;
    }
    
    /**
     * Private method drawing the full sky in the right order. With asterims or not, according to the values inside the bean.
     */
    private void drawSky() {
        painter.clear();
        painter.drawStarsAndAsterisms(observedSkyProperty.getValue(), planeToCanvasProperty.getValue(), upb.isAsterismsOn());
        painter.drawPlanets(observedSkyProperty.getValue(), planeToCanvasProperty.getValue());
        painter.drawSun(observedSkyProperty.getValue(), planeToCanvasProperty.getValue());
        painter.drawMoon(observedSkyProperty.getValue(), planeToCanvasProperty.getValue());
        painter.drawHorizon(observedSkyProperty.getValue(), planeToCanvasProperty.getValue());
        if(upb.isGridOn()) painter.drawGrid(observedSkyProperty.getValue(), planeToCanvasProperty.getValue());
        if(upb.isTrackerOn()) painter.drawTracker(observedSkyProperty.getValue(), planeToCanvasProperty.getValue(), mouseHorizontalPositionProperty.get());
    }
}