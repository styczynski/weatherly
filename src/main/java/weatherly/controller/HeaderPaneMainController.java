package weatherly.controller;

import java.io.IOException;
import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import rx.Observable;
import rx.observables.JavaFxObservable;
import weatherly.animations.BackgroundGradientSwayTransition;
import weatherly.animations.TextFillTransition;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataRequestValidationEvent;
import weatherly.events.WeatherlyQuitEvent;
import weatherly.events.WeatherlyRefreshRequestEvent;
import weatherly.events.WeatherlyRequestAboutPanelEvent;
import weatherly.events.WeatherlyRequestSettingsPanelEvent;
import weatherly.events.WeatherlyWindowDragEvent;
import weatherly.events.WeatherlyWindowDragEventType;

public class HeaderPaneMainController {

    @FXML
    private Button buttonAbout;

    @FXML
    private FontIcon buttonAboutIcon;

    @FXML
    private Button buttonQuit;

    @FXML
    private FontIcon buttonQuitIcon;

    @FXML
    private Button buttonRefresh;

    @FXML
    private FontIcon buttonRefreshIcon;

    @FXML
    private Button buttonSettings;

    @FXML
    private FontIcon buttonSettingsIcon;

    @FXML
    private AnchorPane containerPane;

    @FXML
    private Label labelText;

    public HeaderPaneMainController() throws IOException {
	super();
    }

    @FXML
    private void initialize() {
	EventStream.joinStream(JavaFxObservable.actionEventsOf(this.buttonQuit).map(e -> new WeatherlyQuitEvent()));

	EventStream.joinStream(JavaFxObservable.actionEventsOf(this.buttonSettings)
		.map(e -> new WeatherlyRequestSettingsPanelEvent()));

	EventStream.joinStream(
		JavaFxObservable.actionEventsOf(this.buttonAbout).map(e -> new WeatherlyRequestAboutPanelEvent()));

	EventStream.joinStream(JavaFxObservable.actionEventsOf(this.buttonRefresh).flatMap(
		e -> Observable.just(new WeatherlyDataRequestValidationEvent(), new WeatherlyRefreshRequestEvent())));

	final Observable<MouseEvent> mousePressedEvents = JavaFxObservable.eventsOf(this.containerPane,
		MouseEvent.MOUSE_PRESSED);
	final Observable<MouseEvent> mouseDraggedEvents = JavaFxObservable.eventsOf(this.containerPane,
		MouseEvent.MOUSE_DRAGGED);

	/*
	 * EventStream.joinStream(Observable.combineLatest(mousePressedEvents,
	 * mouseDraggedEvents, (press, drag) -> { return new
	 * WeatherlyWindowDragEvent(drag.getScreenX() - press.getScreenX(),
	 * drag.getScreenY() - press.getScreenY()); }));
	 */

	final Observable<WeatherlyWindowDragEvent> dragEventStream = Observable
		.merge(mousePressedEvents, mouseDraggedEvents)
		.scan(new WeatherlyWindowDragEvent(0, 0, 0, 0, WeatherlyWindowDragEventType.DRAG_STARTED),
			(acc, event) -> {
			    if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				return new WeatherlyWindowDragEvent(event.getScreenX(), event.getScreenY(),
					event.getScreenX(), event.getScreenY(),
					WeatherlyWindowDragEventType.DRAG_STARTED);
			    } else {
				return new WeatherlyWindowDragEvent(acc.getStartX(), acc.getStartY(),
					event.getScreenX(), event.getScreenY(),
					WeatherlyWindowDragEventType.DRAG_HAPPENS);
			    }
			})
		.skip(1);

	final ColorAdjust colorAdjust = new ColorAdjust();
	colorAdjust.setBrightness(0.0);

	this.buttonQuit.setEffect(colorAdjust);

	final Color backgroundColor = Color.WHITE;
	final Color indicationColor = Color.rgb(27, 137, 239);
	final Stop[] colorStops = new Stop[] { new Stop(0, backgroundColor), new Stop(1, indicationColor) };

	final BackgroundGradientSwayTransition paneColorTransition = new BackgroundGradientSwayTransition(
		this.containerPane, 250, colorStops);
	final BackgroundGradientSwayTransition paneColorTransitionReversed = paneColorTransition.reversed();

	this.buttonQuitIcon.setFill(backgroundColor);
	this.buttonSettingsIcon.setFill(backgroundColor);
	this.buttonRefreshIcon.setFill(backgroundColor);
	this.buttonAboutIcon.setFill(backgroundColor);

	final FillTransition iconQuitColorTransition = new FillTransition(Duration.millis(250), this.buttonQuitIcon,
		backgroundColor, indicationColor);

	final FillTransition iconQuitColorTransitionReversed = new FillTransition(Duration.millis(250),
		this.buttonQuitIcon, indicationColor, backgroundColor);

	final FillTransition iconSettingsColorTransition = new FillTransition(Duration.millis(250),
		this.buttonSettingsIcon, backgroundColor, indicationColor);

	final FillTransition iconSettingsColorTransitionReversed = new FillTransition(Duration.millis(250),
		this.buttonSettingsIcon, indicationColor, backgroundColor);

	final FillTransition iconRefreshColorTransition = new FillTransition(Duration.millis(250),
		this.buttonRefreshIcon, backgroundColor, indicationColor);

	final FillTransition iconRefreshColorTransitionReversed = new FillTransition(Duration.millis(250),
		this.buttonRefreshIcon, indicationColor, backgroundColor);

	final FillTransition iconAboutColorTransition = new FillTransition(Duration.millis(250), this.buttonAboutIcon,
		backgroundColor, indicationColor);

	final FillTransition iconAboutColorTransitionReversed = new FillTransition(Duration.millis(250),
		this.buttonAboutIcon, indicationColor, backgroundColor);

	final TextFillTransition textColorTransitionReversed = new TextFillTransition(this.labelText, 250, colorStops);

	final TextFillTransition textColorTransition = textColorTransitionReversed.reversed();

	final Consumer<Integer> clearAnimations = (i) -> {
	    paneColorTransitionReversed.pause();
	    paneColorTransition.pause();
	    iconSettingsColorTransition.pause();
	    iconSettingsColorTransitionReversed.pause();
	    iconRefreshColorTransition.pause();
	    iconRefreshColorTransitionReversed.pause();
	    iconQuitColorTransition.pause();
	    iconQuitColorTransitionReversed.pause();
	    iconAboutColorTransition.pause();
	    iconAboutColorTransitionReversed.pause();
	    textColorTransition.pause();
	    textColorTransitionReversed.pause();

	    this.labelText.setTextFill(backgroundColor);
	    this.buttonRefreshIcon.setFill(backgroundColor);
	    this.buttonQuitIcon.setFill(backgroundColor);
	    this.buttonSettingsIcon.setFill(backgroundColor);
	    this.buttonAboutIcon.setFill(backgroundColor);
	    this.labelText.setText("Weatherly");
	    this.containerPane.setBackground(new Background(new BackgroundFill(indicationColor, null, null)));
	};

	JavaFxObservable.eventsOf(this.buttonAbout, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
	    clearAnimations.accept(0);
	    this.labelText.setText("About the app");

	    paneColorTransition.play();
	    iconAboutColorTransition.play();
	    textColorTransition.play();
	});

	JavaFxObservable.eventsOf(this.buttonAbout, MouseEvent.MOUSE_EXITED).subscribe(e -> {
	    clearAnimations.accept(0);

	    paneColorTransitionReversed.play();
	    iconAboutColorTransitionReversed.play();
	    textColorTransitionReversed.play();
	});

	JavaFxObservable.eventsOf(this.buttonQuit, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
	    clearAnimations.accept(0);
	    this.labelText.setText("Exit");

	    paneColorTransition.play();
	    iconQuitColorTransition.play();
	    textColorTransition.play();
	});

	JavaFxObservable.eventsOf(this.buttonQuit, MouseEvent.MOUSE_EXITED).subscribe(e -> {
	    clearAnimations.accept(0);

	    paneColorTransitionReversed.play();
	    iconQuitColorTransitionReversed.play();
	    textColorTransitionReversed.play();
	});

	JavaFxObservable.eventsOf(this.buttonSettings, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
	    clearAnimations.accept(0);
	    this.labelText.setText("Settings");

	    paneColorTransition.play();
	    iconSettingsColorTransition.play();
	    textColorTransition.play();
	});

	JavaFxObservable.eventsOf(this.buttonSettings, MouseEvent.MOUSE_EXITED).subscribe(e -> {
	    clearAnimations.accept(0);

	    paneColorTransitionReversed.play();
	    iconSettingsColorTransitionReversed.play();
	    textColorTransitionReversed.play();
	});

	JavaFxObservable.eventsOf(this.buttonRefresh, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
	    clearAnimations.accept(0);
	    this.labelText.setText("Refresh");

	    paneColorTransition.play();
	    iconRefreshColorTransition.play();
	    textColorTransition.play();
	});

	JavaFxObservable.eventsOf(this.buttonRefresh, MouseEvent.MOUSE_EXITED).subscribe(e -> {
	    clearAnimations.accept(0);

	    paneColorTransitionReversed.play();
	    iconRefreshColorTransitionReversed.play();
	    textColorTransitionReversed.play();
	});

	EventStream.joinStream(dragEventStream);

    }
}
