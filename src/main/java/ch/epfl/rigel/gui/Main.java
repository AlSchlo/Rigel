package ch.epfl.rigel.gui;

import java.io.File;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * The Main Class of Rigel !
 * 
 * @author Paul Guillon (314517)
 * @author Alexis Schlomer (315616)
 */
public final class Main extends Application {
    
    private final static String RESTART_STRING = "\uf0e2";
    private final static String PLAY_STRING = "\uf04b";
    private final static String PAUSE_STRING = "\uf04c";
    private final static String PROJECT_NAME = "Rigel";
    private final static String FONT_PATH = "/Font Awesome 5 Free-Solid-900.otf";
    private final static String MUSIC_PATH = "src/main/resources/bensound-slowmotion.mp3";
    private final static String HYG_DATA = "/hygdata_v3.csv";
    private final static String AST_DATA = "/asterisms.txt";
    private final static String SOUND_MUTED_STRING = "\uf6a9";
    private final static String SOUND_ENABLED_STRING = "\uf028";
    
    //Initial Parameters
    private final static ZonedDateTime INIT_WHEN = ZonedDateTime.now();
    private final static double INIT_WHERE_LONDEG = 6.57;
    private final static double INIT_WHERE_LATDEG = 46.52;
    private final static GeographicCoordinates INIT_WHERE = GeographicCoordinates.ofDeg(INIT_WHERE_LONDEG, INIT_WHERE_LATDEG);
    private final static HorizontalCoordinates INIT_SKY_WHERE = HorizontalCoordinates.ofDeg(160, 15);
    private final static double INIT_POV = 100;
    private final static int MIN_WIDTH = 800;
    private final static int MIN_HEIGHT = 600;
    private final static NamedTimeAccelerator INIT_ACC = NamedTimeAccelerator.TIMES_30;
    private final static int FONT_SIZE = 15;
    private final static boolean INITIAL_ASTERISMS_ON = true;
    private final static boolean INITIAL_GRID_ON = false;
    private final static boolean INITIAL_TRACKER_ON = false;
    private final static double VOLUME_VALUE = 0.15;
    
    /**
     * Launches the program.
     * @param String (args)
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Private method returning the resourceAsStream of a ressource name.
     * @param String (ressourceName)
     * @return InputStream (inputStream)
     */
    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    /**
     * Effectively starts the JavaFX program.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
      
        try (InputStream fontStream = resourceStream(FONT_PATH);
                InputStream hs = resourceStream(HYG_DATA);
                InputStream as = resourceStream(AST_DATA);) {
                                
            StarCatalogue catalogue = new StarCatalogue.Builder().loadFrom(hs, HygDatabaseLoader.INSTANCE)
                                                                 .loadFrom(as, AsterismLoader.INSTANCE).build();            
            Font fontAwesome = Font.loadFont(fontStream, FONT_SIZE);
            Media media = new Media(new File(MUSIC_PATH).toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            
            DateTimeBean dateTimeBean = new DateTimeBean();
            dateTimeBean.setZonedDateTime(INIT_WHEN);
    
            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            observerLocationBean.setCoordinates(INIT_WHERE);
    
            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
            viewingParametersBean.setCenter(INIT_SKY_WHERE);
            viewingParametersBean.setFieldOfViewDeg(INIT_POV);
            
            UsersParametersBean bonusParametersBean = new UsersParametersBean();
            bonusParametersBean.setAsterismsOn(INITIAL_ASTERISMS_ON);
            bonusParametersBean.setGridOn(INITIAL_GRID_ON);
            bonusParametersBean.setTrackerOn(INITIAL_TRACKER_ON);
    
            TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);            
            SkyCanvasManager canvasManager = new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean, bonusParametersBean);
            
            primaryStage.setTitle(PROJECT_NAME);
            Canvas sky = canvasManager.canvas();
            Pane canvasPane = new Pane(sky);
            sky.widthProperty().bind(canvasPane.widthProperty());
            sky.heightProperty().bind(canvasPane.heightProperty());    
            BorderPane root = new BorderPane(canvasPane, controlBar(observerLocationBean, dateTimeBean, bonusParametersBean, timeAnimator, fontAwesome, player), null, infoBar(viewingParametersBean, canvasManager), null);                        
            
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            sky.requestFocus();
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });
            player.setVolume(VOLUME_VALUE);
            player.play();
        }
    }
    
    /**
     * Private method creating the HBox controller of the program.
     * @param ObserverLocationBean (olb)
     * @param DateTimeBean (dtb)
     * @param BonusParametersBean (bpb)
     * @param TimeAnimator (timeAnimator)
     * @param Font (font)
     * @return HBox (controlBar)
     */
    private static HBox controlBar(ObserverLocationBean olb, DateTimeBean dtb, UsersParametersBean upb, TimeAnimator timeAnimator, Font font, MediaPlayer player) {        
        Separator verticalSeparator1 = new Separator(Orientation.VERTICAL);
        Separator verticalSeparator2 = new Separator(Orientation.VERTICAL);
        Separator verticalSeparator3 = new Separator(Orientation.VERTICAL);
        HBox controlBar = new HBox(observerPos(olb), verticalSeparator1, observerTime(dtb, timeAnimator), verticalSeparator2, timeAcc(dtb, timeAnimator, font), verticalSeparator3, bonusControl(upb, player, font));
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        return controlBar;
    }
    
    /**
     * Private method creating the HBox controlling all the graphic and sound options.
     * @param BonusParametersBean (bpb)
     * @param MediaPlayer (player)
     * @param Font (font)
     * @return HBox (bonusControl)
     */
    private static HBox bonusControl(UsersParametersBean upb, MediaPlayer player, Font font) {
        Label asterismsLabel = new Label("Astérismes : ");
        CheckBox asterismsCheckBox = new CheckBox();
        asterismsCheckBox.setSelected(INITIAL_ASTERISMS_ON);
        asterismsCheckBox.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        upb.asterismsOnProperty().bind(asterismsCheckBox.selectedProperty());
        Label gridLabel = new Label("Grillage : ");
        CheckBox gridCheckBox = new CheckBox();
        gridCheckBox.setSelected(INITIAL_GRID_ON);
        gridCheckBox.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        upb.gridOnProperty().bind(gridCheckBox.selectedProperty());
        Button musicButton = new Button(SOUND_ENABLED_STRING);
        musicButton.setFont(font);
        musicButton.setOnAction(e -> {
            if(player.isMute()) {
                player.setMute(false);
                musicButton.setText(SOUND_ENABLED_STRING);
            } else {
                player.setMute(true);
                musicButton.setText(SOUND_MUTED_STRING);
            }
        });
        Separator verticalSeparator = new Separator(Orientation.VERTICAL);
        HBox graphicsControl = new HBox(asterismsLabel, asterismsCheckBox, gridLabel, gridCheckBox, verticalSeparator, musicButton);
        graphicsControl.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return graphicsControl;
    }
    
    /**
     * Private method creating the HBox controlling the observer position.
     * @param ObserverLocationBean (olb)
     * @return HBox (obsPos)
     */
    private static HBox observerPos(ObserverLocationBean olb) {
        TextFormatter<Number> lonTextFormatter = FilterType.LON.createTextFormatter();
        lonTextFormatter.setValue(INIT_WHERE_LONDEG);
        olb.lonDegProperty().bind(lonTextFormatter.valueProperty());
        TextFormatter<Number> latTextFormatter = FilterType.LAT.createTextFormatter();
        latTextFormatter.setValue(INIT_WHERE_LATDEG);
        olb.latDegProperty().bind(latTextFormatter.valueProperty());
        TextField lonTextField = new TextField();
        TextField latTextField = new TextField();
        lonTextField.setTextFormatter(lonTextFormatter);
        latTextField.setTextFormatter(latTextFormatter);
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        Label lonLabel = new Label("Longitude (°) : ");
        Label latLabel = new Label("Latitude (°) : ");
        HBox obsPos = new HBox(lonLabel, lonTextField, latLabel, latTextField);
        obsPos.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return obsPos;
    }
    
    /**
     * Private method creating the HBox controlling the observer time.
     * @param DateTimeBean (dtb)
     * @return HBox (obsTime)
     */
    private static HBox observerTime(DateTimeBean dtb, TimeAnimator timeAnimator) {
        Label dateLabel = new Label("Date : ");
        DatePicker datePicker = new DatePicker(INIT_WHEN.toLocalDate());
        dtb.dateProperty().bindBidirectional(datePicker.valueProperty());
        datePicker.setStyle("-fx-pref-width: 120;");
        Label hourLabel = new Label("Heure : ");
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
        TextField hourTextField = new TextField();
        hourTextField.setTextFormatter(timeFormatter);
        timeFormatter.setValue(INIT_WHEN.toLocalTime());
        dtb.timeProperty().bindBidirectional(timeFormatter.valueProperty());
        ComboBox<ZoneId> zoneBox = new ComboBox<>();
        zoneBox.setStyle("-fx-pref-width: 180;");
        ObservableList<ZoneId> itemList = FXCollections.observableArrayList();
        for (String zoneString : ZoneId.getAvailableZoneIds()) itemList.add(ZoneId.of(zoneString));
        itemList.sort((s1, s2) -> s1.toString().compareTo(s2.toString()));
        zoneBox.setItems(itemList);
        zoneBox.setValue(INIT_WHEN.getZone());
        dtb.zoneProperty().bindBidirectional(zoneBox.valueProperty());
        HBox obsTime = new HBox(dateLabel, datePicker, hourLabel, hourTextField, zoneBox);
        obsTime.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        obsTime.disableProperty().bind(timeAnimator.runningProperty());
        return obsTime;
    }
    
    /**
     * Private method creating the HBox controlling the time accelerator.
     * @param ObserverLocationBean (olb)
     * @return HBox (obsPos)
     */
    private static HBox timeAcc(DateTimeBean dtb, TimeAnimator timeAnimator, Font font) {
        ChoiceBox<NamedTimeAccelerator> acceleratorBox = new ChoiceBox<>();       
        ObservableList<NamedTimeAccelerator> acceleratorList = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        acceleratorBox.setItems(acceleratorList);
        acceleratorBox.setValue(INIT_ACC);
        timeAnimator.acceleratorProperty().bind(Bindings.select(acceleratorBox.valueProperty(), "accelerator"));
        acceleratorBox.disableProperty().bind(timeAnimator.runningProperty());
        Button restart = new Button(RESTART_STRING);
        restart.setFont(font);
        restart.setOnAction(e -> {
            dtb.setZonedDateTime(ZonedDateTime.now()); //Actualizes the time to the current real time (constant cannot be used)
        });
        restart.disableProperty().bind(timeAnimator.runningProperty());
        Button playPause = new Button(PLAY_STRING);
        playPause.setFont(font);
        playPause.setOnAction(e -> {
            if(timeAnimator.isRunning()) {
                timeAnimator.stop();
                playPause.setText(PLAY_STRING);
            } else {
                timeAnimator.start();
                playPause.setText(PAUSE_STRING);
            }
        });
        HBox timeAcc = new HBox(acceleratorBox, restart, playPause);
        timeAcc.setStyle("-fx-spacing: inherit;");
        return timeAcc;
    }
    
    /**
     * Private method returning the information bar of the UI.
     * @return BorderPane (infoBar)
     */
    private static BorderPane infoBar(ViewingParametersBean vpb, SkyCanvasManager skyManager) {
        Text fovText = new Text();
        fovText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", vpb.fieldOfViewDegProperty()));
        Text mouseText = new Text();
        mouseText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°", skyManager.mouseAzDegProperty(), skyManager.mouseAltDegProperty()));
        Text objectText = new Text();
        StringBinding sBinding = Bindings.createStringBinding(() -> {
            return skyManager.objectUnderMouseProperty().get().isPresent() ? skyManager.objectUnderMouseProperty().get().get().info() : "";
        }, skyManager.objectUnderMouseProperty());
        objectText.textProperty().bind(sBinding);
        BorderPane infoBar = new BorderPane(objectText, null, fovText, null, mouseText);
        infoBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return infoBar;
    }
    
    /**
     * Private enumeration consisting of two different filters.
     * 
     * @author Paul Guillon (314517)
     * @author Alexis Schlomer (315616)
     */
    private enum FilterType {
        
        LON, LAT;
   
        private final static NumberStringConverter STRING_CONVERTER = new NumberStringConverter("#0.00");

        /**
         * Private method creating a text formatter.
         * @param NumberStringConverter (stringConverter)
         * @return UnaryOperator<TextFormatter.Change> (filter)
         */
        private TextFormatter<Number> createTextFormatter() {
            UnaryOperator<TextFormatter.Change> filter = (change -> {
                try {
                    String newText = change.getControlNewText();
                    double newValue = STRING_CONVERTER.fromString(newText).doubleValue();
                    switch (this) {
                    case LON:
                        return GeographicCoordinates.isValidLonDeg(newValue) ? change : null;
                    case LAT:
                        return GeographicCoordinates.isValidLatDeg(newValue) ? change : null;
                    default:
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            });
            return new TextFormatter<>(STRING_CONVERTER, 0, filter);
        }
    }
}