package weatherly;

import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import weatherly.datasource.DataSources;
import weatherly.datasource.WeatherData;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataAvailableEvent;
import weatherly.events.WeatherlyDataRequestValidationEvent;
import weatherly.events.WeatherlyDataSourceChangedEvent;
import weatherly.events.WeatherlyDataUpdateEvent;
import weatherly.events.WeatherlyQuitEvent;
import weatherly.events.WeatherlyRequestAboutPanelEvent;
import weatherly.events.WeatherlyRequestDisplayPanelEvent;
import weatherly.events.WeatherlyRequestSettingsPanelEvent;
import weatherly.events.WeatherlyWindowDragEvent;
import weatherly.events.WeatherlyWindowDragEventType;
import weatherly.meters.Meters;

public class AppMain extends Application {

    public static final int DATA_DEPRECATION_TIMEOUT = 10;

    public static final String FONT_CSS = "/css/jfoenix-fonts.css";

    public static final String FXML_ABOUT_FORM_TEMPLATE = "/fxml/weatherly-about.fxml";

    public static final String FXML_MAIN_FORM_TEMPLATE = "/fxml/weatherly-main.fxml";

    public static final String FXML_SETTINGS_FORM_TEMPLATE = "/fxml/weatherly-settings.fxml";

    public static final String JFX_CSS = "/css/jfx.css";

    public static final String MATERIAL_CSS = "/css/jfoenix-design.css";

    public static final String SOURCES_CONFIG = "/sources.xml";

    public static final String SOURCES_METERS = "/meters.xml";

    private static DataSources dataSources = null;

    private static final Logger log = Logger.getLogger(AppMain.class);

    private static final String[] LOGOS = { "/icons/weatherly16.png", "/icons/weatherly32.png",
	    "/icons/weatherly64.png", "/icons/weatherly128.png" };

    private static Meters meters = null;

    private Scene aboutScene;

    private Scene displayScene;

    private Stage mainStage;

    private Scene settingsScene;

    public static void main(final String[] args) {
	AppMain.setupExceptionHandler();

	AppMain.setupTextRendering();

	Platform.setImplicitExit(true); // This should exit the application when
					// the main window gets closed

	AppMain.setupConfig();

	Application.launch(AppMain.class, args);
    }

    private static void setupConfig() {
	AppMain.log.info("[CONFIG] Looking for external config files... ");

	AppMain.dataSources = JAXB.unmarshal(AppMain.class.getResource(AppMain.SOURCES_CONFIG), DataSources.class);
	AppMain.meters = JAXB.unmarshal(AppMain.class.getResource(AppMain.SOURCES_METERS), Meters.class);

	AppMain.log.info("[CONFIG] Search done.");
    }

    private static void setupExceptionHandler() {
	Thread.setDefaultUncaughtExceptionHandler(
		(t, e) -> AppMain.log.error("Uncaught exception in thread \'" + t.getName() + "\'", e));
    }

    private static void setupTextRendering() {
	/*
	 * A workaround for the font rendering issue on some platforms.
	 * Consult: @link{https://stackoverflow.com/questions/18382969/can-the-
	 * rendering-of-the-javafx-2-8-font-be-improved} and linked materials
	 */
	System.setProperty("prism.text", "t2k");
	System.setProperty("prism.lcdtext", "true");
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
	AppMain.log.info("Starting Weatherly...");

	this.mainStage = primaryStage;
	this.setupEventHandler();
	this.setupDataSources();

	final Parent displayPane = FXMLLoader.load(AppMain.class.getResource(AppMain.FXML_MAIN_FORM_TEMPLATE));
	this.displayScene = new Scene(displayPane);
	this.displayScene.setFill(null);
	this.displayScene.getStylesheets().addAll(AppMain.class.getResource(AppMain.FONT_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.MATERIAL_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.JFX_CSS).toExternalForm());

	final Parent settingsPane = FXMLLoader.load(AppMain.class.getResource(AppMain.FXML_SETTINGS_FORM_TEMPLATE));
	this.settingsScene = new Scene(settingsPane);
	this.settingsScene.setFill(null);
	this.settingsScene.getStylesheets().addAll(AppMain.class.getResource(AppMain.FONT_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.MATERIAL_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.JFX_CSS).toExternalForm());

	final Parent aboutPane = FXMLLoader.load(AppMain.class.getResource(AppMain.FXML_ABOUT_FORM_TEMPLATE));
	this.aboutScene = new Scene(aboutPane);
	this.aboutScene.setFill(null);
	this.aboutScene.getStylesheets().addAll(AppMain.class.getResource(AppMain.FONT_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.MATERIAL_CSS).toExternalForm(),
		AppMain.class.getResource(AppMain.JFX_CSS).toExternalForm());

	this.mainStage.setScene(this.displayScene);
	this.addLogo();

	this.mainStage.setWidth(300);
	this.mainStage.setHeight(500);
	this.mainStage.initStyle(StageStyle.UNDECORATED);
	this.mainStage.setResizable(false);
	this.mainStage.show();

	AppMain.dataSources.initSources();
	AppMain.meters.initMeters();

	AppMain.log.info("Weatherly's up");
    }

    private void addLogo() {
	for (final String logoPath : AppMain.LOGOS) {
	    this.mainStage.getIcons().add(new Image(AppMain.class.getResourceAsStream(logoPath)));
	}
    }

    private void onClose() {
	AppMain.log.info("onClose");
	this.mainStage.close();
	System.exit(0);
    }

    private void setupDataSources() {
	AppMain.dataSources.initSources();
	AppMain.log.info("Loaded data sources: " + AppMain.dataSources);
	EventStream.joinStream(AppMain.dataSources.dataStream());
    }

    private void setupEventHandler() {

	EventStream.eventStream().eventsInFx().ofType(WeatherlyDataUpdateEvent.class).subscribe(event -> {
	    AppMain.log.info("Weather update.");
	});

	EventStream.eventStream().eventsInFx().ofType(WeatherlyRequestSettingsPanelEvent.class).subscribe(event -> {
	    AppMain.log.info("Show settings pane.");
	    this.mainStage.setScene(this.settingsScene);
	});

	EventStream.eventStream().eventsInFx().ofType(WeatherlyRequestDisplayPanelEvent.class).subscribe(event -> {
	    AppMain.log.info("Show display pane.");
	    this.mainStage.setScene(this.displayScene);
	});

	EventStream.eventStream().eventsInFx().ofType(WeatherlyRequestAboutPanelEvent.class).subscribe(event -> {
	    AppMain.log.info("Show about pane.");
	    this.mainStage.setScene(this.aboutScene);
	});

	EventStream.joinStream(EventStream.eventStream().eventsInFx().ofType(WeatherlyDataSourceChangedEvent.class)
		.map(e -> new WeatherlyDataRequestValidationEvent()));

	EventStream.joinStream(EventStream.eventStream().eventsInFx().ofType(WeatherlyDataAvailableEvent.class)
		.scan(new WeatherlyDataUpdateEvent(new WeatherData()), (acc, e) -> {
		    if (e instanceof WeatherlyDataRequestValidationEvent) {
			final WeatherData data = acc.getData();
			data.validate(0);
			return new WeatherlyDataUpdateEvent(data);
		    } else {
			final WeatherData data = acc.getData();
			data.push(e.getData());
			data.validate(AppMain.DATA_DEPRECATION_TIMEOUT);
			return new WeatherlyDataUpdateEvent(data);
		    }
		}));

	EventStream.eventStream().events().ofType(WeatherlyQuitEvent.class).subscribe(e -> {
	    AppMain.this.onClose();
	});

	EventStream.eventStream().events().ofType(WeatherlyWindowDragEvent.class)
		.scan(new WeatherlyWindowDragEvent(0, 0, 0, 0, WeatherlyWindowDragEventType.DRAG_STARTED), (acc, e) -> {
		    if (e.getType() == WeatherlyWindowDragEventType.DRAG_STARTED) {
			return new WeatherlyWindowDragEvent(e.getStartX() - this.mainStage.getX(),
				e.getStartY() - this.mainStage.getY(), e.getEndX(), e.getEndY(),
				WeatherlyWindowDragEventType.DRAG_STARTED);
		    } else {
			return new WeatherlyWindowDragEvent(acc.getStartX(), acc.getStartY(), e.getEndX(), e.getEndY(),
				WeatherlyWindowDragEventType.DRAG_HAPPENS);
		    }
		}).skip(1).subscribe(e -> {
		    this.mainStage.setX(Math.abs(e.getDeltaX()));
		    this.mainStage.setY(Math.abs(e.getDeltaY()));
		});
    }
}
