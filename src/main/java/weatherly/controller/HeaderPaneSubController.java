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
import weatherly.events.WeatherlyRequestDisplayPanelEvent;
import weatherly.events.WeatherlyWindowDragEvent;
import weatherly.events.WeatherlyWindowDragEventType;

public class HeaderPaneSubController {

    @FXML
    private Button buttonQuit;

    @FXML
    private FontIcon buttonQuitIcon;

    @FXML
    private AnchorPane containerPane;

    @FXML
    private Label labelText;

    public HeaderPaneSubController() throws IOException {
	super();
    }

    @FXML
    private void initialize() {
	EventStream.joinStream(
		JavaFxObservable.actionEventsOf(this.buttonQuit).map(e -> new WeatherlyRequestDisplayPanelEvent()));

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

	final FillTransition iconQuitColorTransition = new FillTransition(Duration.millis(250), this.buttonQuitIcon,
		backgroundColor, indicationColor);

	final FillTransition iconQuitColorTransitionReversed = new FillTransition(Duration.millis(250),
		this.buttonQuitIcon, indicationColor, backgroundColor);

	final TextFillTransition textColorTransitionReversed = new TextFillTransition(this.labelText, 250, colorStops);

	final TextFillTransition textColorTransition = textColorTransitionReversed.reversed();

	final Consumer<Integer> clearAnimations = (i) -> {
	    paneColorTransitionReversed.pause();
	    paneColorTransition.pause();
	    textColorTransition.pause();
	    textColorTransitionReversed.pause();

	    this.labelText.setText("Weatherly");
	    this.labelText.setTextFill(backgroundColor);
	    this.buttonQuitIcon.setFill(backgroundColor);
	    this.containerPane.setBackground(new Background(new BackgroundFill(indicationColor, null, null)));
	};

	JavaFxObservable.eventsOf(this.buttonQuit, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
	    clearAnimations.accept(0);
	    this.labelText.setText("Back");

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

	EventStream.joinStream(dragEventStream);

    }
}
