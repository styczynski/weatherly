package weatherly.events;

import weatherly.datasource.DataSources;

public class WeatherlyDataSourcesLoadedEvent extends WeatherlyEvent {

    private final DataSources sources;

    public WeatherlyDataSourcesLoadedEvent(final DataSources sources) {
	this.sources = sources;
    }

    public DataSources getSources() {
	return this.sources;
    }

    @Override
    public String toString() {
	return "WeatherlySourcesLoadedEvent(meters=" + this.sources + ")";
    }

}
