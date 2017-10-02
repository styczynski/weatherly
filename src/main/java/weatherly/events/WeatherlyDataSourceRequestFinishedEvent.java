package weatherly.events;

import java.time.LocalDateTime;

public class WeatherlyDataSourceRequestFinishedEvent extends WeatherlyEvent {

    private final LocalDateTime timestamp;

    public WeatherlyDataSourceRequestFinishedEvent() {
	this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    @Override
    public String toString() {
	return "WeatherlyDataSourceRequestFinished(timestamp=" + this.timestamp + ")";
    }

}
