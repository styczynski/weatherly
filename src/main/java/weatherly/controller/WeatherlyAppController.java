package weatherly.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.ocpsoft.prettytime.PrettyTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import rx.Observable;
import rx.schedulers.Schedulers;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataUpdateEvent;

public class WeatherlyAppController {

    @FXML
    private Label updateTimeInfoText;

    @FXML
    public void initialize() {

	Observable
		.combineLatest(Observable.interval(0, 5, TimeUnit.SECONDS, Schedulers.io()),
			EventStream.eventStream().events().ofType(WeatherlyDataUpdateEvent.class), (ignore, e) -> e)
		.subscribe(e -> {
		    final PrettyTime p = new PrettyTime();
		    final ZonedDateTime zdt = e.getTimestamp().atZone(ZoneId.systemDefault());
		    final Date output = Date.from(zdt.toInstant());
		    this.updateTimeInfoText.setText("updated " + p.format(output));
		});

    }

}
