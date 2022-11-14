package ch.epfl.rigel.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.AffineFunction;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * A Sky Canvas Painter.
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class SkyCanvasPainter {

    private final static double SUN_AVERAGE_DIAMETER = 2*Math.tan(Angle.ofDeg(0.5)/4);
    private final static ClosedInterval MAGNITUDES_INTERVAL = ClosedInterval.of(-2, 5);
    private final static HorizontalCoordinates CENTER_OF_HOR = HorizontalCoordinates.ofDeg(0, 0);
    private final static HorizontalCoordinates TOP_HOR = HorizontalCoordinates.ofDeg(0, 90);
    private final static int CARDINAL_POINT_SPACING = 45;
    private final static Color SPECIAL_YELLOW = Color.YELLOW.deriveColor(0, 1, 1, 0.25);
    private final static List<HorizontalCoordinates> PARAL_GRID_POINTS = loadParalGridList();
    private final static List<HorizontalCoordinates> MERID_GRID_POINTS = loadMeridGridList();
    private final static ClosedInterval STRAIGHT_LINE_SENSITIVITY_ALT_TOP = ClosedInterval.of(89.9, 90);
    private final static ClosedInterval STRAIGHT_LINE_SENSITIVITY_AZ_1 = ClosedInterval.of(0, 0.1);
    private final static ClosedInterval STRAIGHT_LINE_SENSITIVITY_AZ_2 = ClosedInterval.of(179.9, 180.1);
    private final static RightOpenInterval STRAIGHT_LINE_SENSITIVITY_AZ_3 = RightOpenInterval.of(359.9, 360);
    private final static double TOLERANCE = 0.1;
    
    private static List<HorizontalCoordinates> loadParalGridList() {
        List<HorizontalCoordinates> temp = new ArrayList<HorizontalCoordinates>();
        for(int i = -90 ; i < 90 ; i += 15) {
            if(i != 0) temp.add(HorizontalCoordinates.ofDeg(0, i));           
        }
        return Collections.unmodifiableList(temp);
    }
    
    private static List<HorizontalCoordinates> loadMeridGridList() {
        List<HorizontalCoordinates> temp = new ArrayList<HorizontalCoordinates>();
        for(int i = 0 ; i < 360 ; i += 30) {
            temp.add(HorizontalCoordinates.ofDeg(i, 0));           
        }
        return Collections.unmodifiableList(temp);
    }
    
    private final Canvas canvas;
    private final GraphicsContext graphics;
    
    /**
     * Default constructor of a SkyCanvasPainter given a Canvas.
     * @param Canvas (canvas)
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.graphics = canvas.getGraphicsContext2D(); 
    }
    
    /**
     * Calculates the diameter of a CelestialObject given its magnitude.
     * 
     * @param double (magnitude)
     * @return double (diameter) 
     */
    private static double diameter(double magnitude) {
        double clippedMagnitude = MAGNITUDES_INTERVAL.clip(magnitude);
        double sizeFactor = (99 - 17*clippedMagnitude) / 140;
        return sizeFactor * SUN_AVERAGE_DIAMETER;
    }
    
    /**
     * Clears all the drawings and turns everything black.
     */
    public void clear() {
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    /**
     * Draws the stars and asterisms of a given sky (ObservedSky) and a plane to canvas transformation (Transform). Third argument represents whether or not the asterisms should be activated.
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     * @param boolean (asterisms)
     */
    public void drawStarsAndAsterisms(ObservedSky sky, Transform planeToCanvas, boolean asterisms) {
        double [] starPositions = sky.starPositions();
        double [] transformedStarPositions = new double[starPositions.length];
        planeToCanvas.transform2DPoints(starPositions, 0, transformedStarPositions, 0, starPositions.length / 2);
        if(asterisms) {
            graphics.setStroke(Color.BLUE);
            graphics.setLineWidth(1);
            for(Asterism a : sky.asterisms()) {
                for(int i = 0 ; i < sky.asterismIndices(a).size() - 1 ; ++i) {
                    int index1 = sky.asterismIndices(a).get(i);
                    int index2 = sky.asterismIndices(a).get(i + 1);
                    double x1 =  transformedStarPositions[2*index1];
                    double y1 =  transformedStarPositions[2*index1 + 1];
                    double x2 =  transformedStarPositions[2*index2];
                    double y2 =  transformedStarPositions[2*index2 + 1];
                    if(canvas.getBoundsInLocal().contains(x1, y1) || canvas.getBoundsInLocal().contains(x2, y2)) graphics.strokeLine(x1, y1, x2, y2);
                }
            }
        }
        int counter = 0;
        for(Star s : sky.stars()) {
            double diam = planeToCanvas.deltaTransform(diameter(s.magnitude()), 0).magnitude();
            setFillAndFillCircle(BlackBodyColor.ofTemperature(s.colorTemperature()), transformedStarPositions[counter], transformedStarPositions[counter + 1], diam);
            counter += 2;
        }
    }
    
    /**
     * Draws the planets of a given sky (ObservedSky) and a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     */
    public void drawPlanets(ObservedSky sky, Transform planeToCanvas) {
        double [] planetPositions = sky.planetPositions();
        double [] transformedPlanetPositions = new double[planetPositions.length];
        planeToCanvas.transform2DPoints(planetPositions, 0, transformedPlanetPositions, 0, planetPositions.length / 2);
        int counter = 0;
        for(Planet p : sky.planets()) {
            double diam = planeToCanvas.deltaTransform(diameter(p.magnitude()), 0).magnitude();
            setFillAndFillCircle(Color.LIGHTGREY, transformedPlanetPositions[counter], transformedPlanetPositions[counter + 1], diam);
            counter += 2;
        }
    }
    
    /**
     * Draws the moon of a given sky (ObservedSky) as well as a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     */
    public void drawMoon(ObservedSky sky, Transform planeToCanvas) {
        Moon m = sky.moon();
        Point2D p = planeToCanvas.transform(sky.moonPosition().x(), sky.moonPosition().y());
        double diam = planeToCanvas.deltaTransform(sky.projection().applyToAngle(m.angularSize()), 0).magnitude();
        setFillAndFillCircle(Color.WHITE, p.getX(), p.getY(), diam);
    }
    
    /**
     * Draws the sun of a given sky (ObservedSky) and a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     */
    public void drawSun(ObservedSky sky, Transform planeToCanvas) {
        Sun s = sky.sun();
        Point2D p = planeToCanvas.transform(sky.sunPosition().x(), sky.sunPosition().y());
        double diam = planeToCanvas.deltaTransform(sky.projection().applyToAngle(s.angularSize()), 0).magnitude();
        setFillAndFillCircle(SPECIAL_YELLOW, p.getX(), p.getY(), 2.2*diam);
        setFillAndFillCircle(Color.YELLOW, p.getX(), p.getY(), diam + 2);
        setFillAndFillCircle(Color.WHITE, p.getX(), p.getY(), diam);
    }
    
    private void setFillAndFillCircle(Color c, double x, double y, double diam) {
        graphics.setFill(c);
        graphics.fillOval(x - diam / 2, y - diam / 2, diam, diam);
    }
    
    private void setStrokeAndStrokeCircle(Color c, double x, double y, double diam, double lineWidth) {
        graphics.setStroke(c);
        graphics.setLineWidth(lineWidth);
        graphics.strokeOval(x - diam / 2, y - diam / 2, diam, diam);
    }
    
    /**
     * Draws the horizon of a given sky (ObservedSky) and a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     */
    public void drawHorizon(ObservedSky sky, Transform planeToCanvas) {
        drawParallel(sky, planeToCanvas, CENTER_OF_HOR, Color.RED, 2);
        graphics.setFill(Color.RED);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.TOP);
        for (int i = 0; i < 8; i++) {
            HorizontalCoordinates cardinalPoint = HorizontalCoordinates.ofDeg(CARDINAL_POINT_SPACING * i, -0.5);
            String pointName = cardinalPoint.azOctantName("N", "E", "S", "O");
            CartesianCoordinates projectedPoint = sky.projection().apply(cardinalPoint);
            graphics.fillText(pointName, planeToCanvas.transform(projectedPoint.x(), projectedPoint.y()).getX(),
                                         planeToCanvas.transform(projectedPoint.x(), projectedPoint.y()).getY());
        }
    }
    
    /**
     * Draws the grid of a given sky (ObservedSky) and a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas) 
     */
    public void drawGrid(ObservedSky sky, Transform planeToCanvas) {
        for(HorizontalCoordinates point : PARAL_GRID_POINTS) drawParallel(sky, planeToCanvas, point, Color.LIGHTGRAY, 1);
        for(HorizontalCoordinates point : MERID_GRID_POINTS) drawMeridian(sky, planeToCanvas, point, Color.LIGHTGRAY, 1);
    }
    
    /**
     * Draws a meridian on the canvas. If it is a line, truncates big values (JavaFX does not support INFINITY)
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     * @param HorizontalCoordinates (meridian)
     * @param Color (color)
     * @param double (lineWidth)
     */
    private void drawMeridian(ObservedSky sky, Transform planeToCanvas, HorizontalCoordinates meridian, Color color, double lineWidth) {
        double lambda = Angle.toDeg(Angle.normalizePositive(meridian.az() - sky.projection().center().az()));
        //Condition that a line must be drawn instead of a meridian.
        if(STRAIGHT_LINE_SENSITIVITY_ALT_TOP.contains(sky.projection().center().altDeg()) || STRAIGHT_LINE_SENSITIVITY_AZ_1.contains(lambda) || STRAIGHT_LINE_SENSITIVITY_AZ_2.contains(lambda) || STRAIGHT_LINE_SENSITIVITY_AZ_3.contains(lambda)) {
            CartesianCoordinates topCart = sky.projection().apply(TOP_HOR);
            Point2D topPoint = planeToCanvas.transform(topCart.x(), topCart.y());
            HorizontalCoordinates meridianHor = HorizontalCoordinates.ofDeg(meridian.azDeg(), 0);
            CartesianCoordinates meridianCart = sky.projection().apply(meridianHor);
            Point2D meridianOnHorizonP = planeToCanvas.transform(meridianCart.x(), meridianCart.y());
            List<Point2D> points = new ArrayList<>(); //p1 and p2 will represent the two points of the line that we want to draw on the intersections with the two sides of the canvas
            if(meridianOnHorizonP.getX() - topPoint.getX() == 0) { //Horizontal Case
                points.add(new Point2D(meridianOnHorizonP.getX(), 0));
                points.add(new Point2D(meridianOnHorizonP.getX(), canvas.getHeight()));
            } else if(topPoint.getY() - meridianOnHorizonP.getY() == 0) { //Vertical Case
                points.add(new Point2D(0, meridianOnHorizonP.getY()));
                points.add(new Point2D(canvas.getWidth(), meridianOnHorizonP.getY()));
            } else { //Oblic Case
                double m = (meridianOnHorizonP.getY() - topPoint.getY()) / (meridianOnHorizonP.getX() - topPoint.getX());
                double h = topPoint.getY() - m*topPoint.getX();
                AffineFunction line = new AffineFunction(m, h);
                ClosedInterval topBot = ClosedInterval.of(0, canvas.getWidth());
                ClosedInterval rightLeft = ClosedInterval.of(0, canvas.getHeight());
                double x1 = line.inverseApply(0); //Intersection with TOP side of the canvas 
                double x2 = line.inverseApply(canvas.getHeight()); //Intersection with BOTTOM
                double y1 = line.apply(0); //Intersection with LEFT
                double y2 = line.apply(canvas.getWidth()); //Intersection with RIGHT
                if(topBot.contains(x1)) points.add(new Point2D(x1, 0));
                if(topBot.contains(x2)) points.add(new Point2D(x2, canvas.getHeight()));
                if(rightLeft.contains(y1)) points.add(new Point2D(0, y1));
                if(rightLeft.contains(y2)) points.add(new Point2D(canvas.getWidth(), y2));
            }           
            graphics.setStroke(color);
            graphics.setLineWidth(1);
            graphics.strokeLine(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY());
        } else {
            CartesianCoordinates meridianCenter = sky.projection().circleCenterForMeridian(meridian);
            Point2D meridianCenterP = planeToCanvas.transform(meridianCenter.x(), meridianCenter.y());
            double radiusMeridian = sky.projection().circleRadiusForMeridian(meridian);
            double diamMeridian = planeToCanvas.deltaTransform(2*radiusMeridian, 0).magnitude();
            setStrokeAndStrokeCircle(color, meridianCenterP.getX(), meridianCenterP.getY(), diamMeridian, lineWidth);
        }
    }
    
    /**
     * Draws a parallel on the canvas. If it is a line, truncates big values (JavaFX does not support INFINITY)
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     * @param HorizontalCoordinates (parallel)
     * @param Color (color)
     * @param double (lineWidth)
     */
    private void drawParallel(ObservedSky sky, Transform planeToCanvas, HorizontalCoordinates parallel, Color color, double lineWidth) {
        //Condition that a line must be drawn instead of a parallel.
        if(Math.abs(sky.projection().center().altDeg() + parallel.altDeg()) <= TOLERANCE) {
            HorizontalCoordinates parallelBeginH = HorizontalCoordinates.ofDeg(0, parallel.altDeg());
            CartesianCoordinates parallelBeginC = sky.projection().apply(parallelBeginH);
            Point2D parallelBeginP = planeToCanvas.transform(parallelBeginC.x(), parallelBeginC.y());
            Point2D p1 = new Point2D(0, parallelBeginP.getY());
            Point2D p2 = new Point2D(canvas.getWidth(), parallelBeginP.getY()); //Only the horizontal case must be considered
            graphics.setStroke(color);
            graphics.setLineWidth(1);
            graphics.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());

        } else {
            CartesianCoordinates parallelCenter = sky.projection().circleCenterForParallel(parallel);
            Point2D parallelCenterP = planeToCanvas.transform(parallelCenter.x(), parallelCenter.y());
            double radiusParallel = sky.projection().circleRadiusForParallel(parallel);
            double diamParallel = planeToCanvas.deltaTransform(2*radiusParallel, 0).magnitude();
            setStrokeAndStrokeCircle(color, parallelCenterP.getX(), parallelCenterP.getY(), diamParallel, lineWidth);
        }
    }
        
    /**
     * Draws the grid of a given sky (ObservedSky) and a plane to canvas transformation (Transform).
     * @param ObservedSky (sky)
     * @param Transform (planeToCanvas)
     */
    public void drawTracker(ObservedSky sky, Transform planeToCanvas, HorizontalCoordinates trackPoint) {
        drawMeridian(sky, planeToCanvas, trackPoint, Color.LIGHTGREEN, 1);
        drawParallel(sky, planeToCanvas, trackPoint, Color.LIGHTGREEN, 1);
    } 
}