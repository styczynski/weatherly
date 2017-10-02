package weatherly.views;

import java.io.IOException;

import javafx.scene.layout.VBox;
import rx.Observable;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataSourcesLoadedEvent;

public class SourcesSelectView extends VBox {

    public SourcesSelectView() throws IOException {
	EventStream.eventStream().events().ofType(WeatherlyDataSourcesLoadedEvent.class).flatMap(event -> {
	    return Observable.from(event.getSources().getSourcesList()).map(source -> {
		try {
		    return new SourceToggleView(source);
		} catch (final IOException e) {
		    e.printStackTrace();
		}
		return null;
	    }).toList();
	}).subscribe(views -> {
	    this.getChildren().setAll(views);
	});
    }

}
