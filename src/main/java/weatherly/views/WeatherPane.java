package weatherly.views;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import rx.exceptions.Exceptions;
import rx.observables.JavaFxObservable;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyMetersLoadedEvent;

public class WeatherPane extends VBox {

    public WeatherPane() throws IOException {

	EventStream.eventStream().events().ofType(WeatherlyMetersLoadedEvent.class).flatMap(event -> {
	    return event.getMeters().getMetersList().map(meter -> {
		WeatherMeterView view;
		try {
		    view = new WeatherMeterView(meter);
		    return view;
		} catch (final IOException e) {
		    throw Exceptions.propagate(e);
		}
	    }).toList();
	}).subscribe(views -> {
	    this.getChildren().setAll(views);
	    final Rectangle indicator = new Rectangle(0, 0, 4, 15);
	    final Text indicatorLabel = new Text("");
	    // indicatorLabel.setRotationAxis(new Point3D(0, 0, 0));
	    indicatorLabel.setRotate(-90.0);
	    indicatorLabel.setTranslateX(-78);

	    this.getChildren().add(indicator);
	    this.getChildren().add(indicatorLabel);
	    double maxYCap = 0;
	    for (final WeatherMeterView view : views) {
		maxYCap = Math.max(maxYCap, view.getLayoutY());
	    }
	    final TranslateTransition indicatorTransition = new TranslateTransition(Duration.millis(250.0), indicator);
	    final TranslateTransition labelTransition = new TranslateTransition(Duration.millis(250.0), indicatorLabel);

	    for (final WeatherMeterView view : views) {
		JavaFxObservable.eventsOf(view, MouseEvent.MOUSE_ENTERED).subscribe(e -> {
		    final double newY = -indicator.getLayoutY() - indicator.getY() + view.getLayoutY();
		    indicatorTransition.pause();
		    indicatorTransition.setToY(newY);
		    indicatorTransition.playFromStart();
		    labelTransition.pause();
		    labelTransition.setToY(newY - 20 - view.getMeter().name().length() * 4);
		    labelTransition.playFromStart();

		    indicatorLabel.setText(String.format("%-30s", view.getMeter().name()));
		});
	    }
	});
    }

}
