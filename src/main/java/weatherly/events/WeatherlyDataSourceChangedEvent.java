package weatherly.events;

import weatherly.datasource.DataSource;

public class WeatherlyDataSourceChangedEvent extends WeatherlyEvent {

    private final DataSource source;
    private final WeatherlyDataSourceChangedEventType type;

    public WeatherlyDataSourceChangedEvent(final DataSource source, final WeatherlyDataSourceChangedEventType type) {
	this.source = source;
	this.type = type;
    }

    public DataSource getSource() {
	return this.source;
    }

    public WeatherlyDataSourceChangedEventType getType() {
	return this.type;
    }

    @Override
    public String toString() {
	return "WeatherlyDataSourceChanged(source=" + this.source + ")";
    }

}
