package weatherly.events;

import weatherly.datasource.WeatherData;

public class WeatherlyDataRequestValidationEvent extends WeatherlyDataAvailableEvent {

    public WeatherlyDataRequestValidationEvent() {
	super(new WeatherData());
    }

}
