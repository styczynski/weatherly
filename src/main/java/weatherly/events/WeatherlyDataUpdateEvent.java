package weatherly.events;

import java.time.LocalDateTime;

import weatherly.datasource.WeatherData;

public class WeatherlyDataUpdateEvent extends WeatherlyEvent {

    private final LocalDateTime timestamp;
    private final WeatherData weatherData;

    public WeatherlyDataUpdateEvent(final WeatherData weatherData) {
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
	return "WeatherlyDataUpdate(timestamp=" + this.timestamp + "; data=" + this.weatherData + ")";
    }

}
