package weatherly.datasource.sources;

import rx.Observable;
import weatherly.datasource.JsonDataContainer;
import weatherly.datasource.WeatherData;
import weatherly.datasource.WeatherDataProperty;
import weatherly.datasource.WeatherJsonDataProvider;
import weatherly.events.WeatherlyEvent;

public class PowietrzeGiosSource extends WeatherJsonDataProvider {

    @Override
    public String getName() {
	return "powietrze.gios.gov.pl";
    }

    @Override
    public String getUrl() {
	return "http://powietrze.gios.gov.pl/pjp/current/getAQIDetailsList?param=AQI";
    }

    @Override
    public Observable<? extends WeatherlyEvent> parseResponse(final JsonDataContainer object) {
	final WeatherData data = new WeatherData();

	object.iter(entry -> {
	    entry.getString("stationName").iter(name -> {
		if (name.contains("Warszawa")) {
		    try {
			data.push(WeatherDataProperty.AIR_QUALITY, entry, "values");
		    } catch (final Throwable t) {
			t.printStackTrace();
		    }
		}
	    });
	});

	return data.toObservable();
    }
}
