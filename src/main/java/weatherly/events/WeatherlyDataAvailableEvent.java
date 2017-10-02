package weatherly.events;

import java.time.LocalDateTime;

import weatherly.datasource.WeatherData;

public class WeatherlyDataAvailableEvent extends WeatherlyEvent {

    private final LocalDateTime timestamp;
    private final WeatherData weatherData;

    public WeatherlyDataAvailableEvent(final WeatherData weatherData) {
	this.weatherData = weatherData;
	this.timestamp = LocalDateTime.now();
    }

    public WeatherData getData() {
	return this.weatherData;
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    @Override
    public String toString() {
	return "WeatherlyDataAvailable(timestamp=" + this.timestamp + "; data=" + this.weatherData + ")";
    }

}
