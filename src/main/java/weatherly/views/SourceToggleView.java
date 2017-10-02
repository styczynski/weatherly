package weatherly.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import rx.observables.JavaFxObservable;
import weatherly.datasource.DataSource;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataSourceChangedEvent;
import weatherly.events.WeatherlyDataSourceChangedEventType;

public class SourceToggleView extends VBox {

    private static final String FXML_TEMPLATE = "/fxml/source-toggle-view.fxml";

    @FXML
    private CheckBox checkBox;

    public SourceToggleView(final DataSource source) throws IOException {
	super();

	final javafx.fxml.FXMLLoader loader = new FXMLLoader(
		SourceToggleView.class.getResource(SourceToggleView.FXML_TEMPLATE));
	loader.setController(this);
	this.getChildren().add(loader.load());

	this.checkBox.setText(source.toString());
	this.checkBox.setSelected(true);

	EventStream.joinStream(
		JavaFxObservable.actionEventsOf(this.checkBox).scan(true, (acc, e) -> !acc).skip(0).map(state -> {
		    if (state) {
			return new WeatherlyDataSourceChangedEvent(source,
				WeatherlyDataSourceChangedEventType.ENABLED_EVENT);
		    } else {
			return new WeatherlyDataSourceChangedEvent(source,
				WeatherlyDataSourceChangedEventType.DISABLED_EVENT);
		    }
		}));

	EventStream.eventStream().events().ofType(WeatherlyDataSourceChangedEvent.class).subscribe((e) -> {
	    if (!e.getSource().getName().equals(source.getName())) {
		return;
	    }
	    switch (e.getType()) {
	    case DISABLED_EVENT:
		this.checkBox.setSelected(false);
		break;
	    case ENABLED_EVENT:
		this.checkBox.setSelected(true);
		break;
	    default:
		break;

	    }
	});
    }

}
