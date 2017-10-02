package weatherly.events;

import weatherly.meters.Meters;

public class WeatherlyMetersLoadedEvent extends WeatherlyEvent {

    private final Meters meters;

    public WeatherlyMetersLoadedEvent(final Meters meters) {
	this.meters = meters;
    }

    public Meters getMeters() {
	return this.meters;
    }

    @Override
    public String toString() {
	return "WeatherlyMetersLoadedEvent(meters=" + this.meters + ")";
    }

}
