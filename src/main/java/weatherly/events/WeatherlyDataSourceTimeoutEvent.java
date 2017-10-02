package weatherly.events;

import java.time.LocalDateTime;

public class WeatherlyDataSourceTimeoutEvent extends WeatherlyEvent {

    private final LocalDateTime timestamp;

    public WeatherlyDataSourceTimeoutEvent() {
	this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    @Override
    public String toString() {
	return "WeatherlyDataSourceTimeout(timestamp=" + this.timestamp + ")";
    }

}
