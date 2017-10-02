package weatherly.datasource.sources;

import rx.Observable;
import weatherly.datasource.JsonDataContainer;
import weatherly.datasource.WeatherData;
import weatherly.datasource.WeatherDataProperty;
import weatherly.datasource.WeatherJsonDataProvider;
import weatherly.datasource.WeatherType;
import weatherly.events.WeatherlyEvent;

public class OpenWeatherMapSource extends WeatherJsonDataProvider {

    private static final String API_KEY = "cf24c94ac2550178e2ea4e970cd0f416";

    @Override
    public String getName() {
	return "www.openweathermap.org";
    }

    @Override
    public String getUrl() {
	return "http://api.openweathermap.org/data/2.5/weather?q=Warsaw&APPID=" + OpenWeatherMapSource.API_KEY;
    }

    @Override
    public Observable<? extends WeatherlyEvent> parseResponse(final JsonDataContainer object) {
	final WeatherData data = new WeatherData();

	data.push(WeatherDataProperty.LATITUDE, object, "coord.lat")
		.push(WeatherDataProperty.LONGITUDE, object, "coord.lon")
		.push(WeatherDataProperty.WEATHER_DESCRIPTION, object, "weather[0].description")
		.push(WeatherDataProperty.PRESSURE_GROUND, object, "main.pressure")
		.push(WeatherDataProperty.HUMIDITY, object, "main.humidity")
		.push(WeatherDataProperty.TEMPERATURE, object, "main.temp_max")
		.push(WeatherDataProperty.CLOUDINESS, object, "clouds.all")
		.push(WeatherDataProperty.WIND_DIRECTION, object, "wind.deg")
		.push(WeatherDataProperty.WIND_SPEED, object, "wind.speed")
		.push(WeatherDataProperty.SUNRISE, object, "sys.sunrise")
		.push(WeatherDataProperty.SUNSET, object, "sys.sunset").push(WeatherDataProperty.WEATHER_TYPE, () -> {
		    return object.getInt("weather[0].id").mapValue((e) -> {
			switch (e) {
			case 200:
			    return WeatherType.THUNDERSTORM_LIGHT;
			case 201:
			    return WeatherType.THUNDERSTORM;
			case 202:
			    return WeatherType.THUNDERSTORM_HEAVY;
			case 210:
			    return WeatherType.THUNDERSTORM_LIGHT;
			case 211:
			    return WeatherType.THUNDERSTORM;
			case 212:
			    return WeatherType.THUNDERSTORM_HEAVY;
			case 221:
			    return WeatherType.THUNDERSTORM;
			case 230:
			    return WeatherType.THUNDERSTORM_LIGHT;
			case 231:
			    return WeatherType.THUNDERSTORM;
			case 232:
			    return WeatherType.THUNDERSTORM_HEAVY;
			case 300:
			    return WeatherType.DRIZZLE_LIGHT;
			case 301:
			    return WeatherType.DRIZZLE;
			case 302:
			    return WeatherType.DRIZZLE_HEAVY;
			case 310:
			    return WeatherType.DRIZZLE_LIGHT;
			case 311:
			    return WeatherType.DRIZZLE;
			case 312:
			    return WeatherType.DRIZZLE_HEAVY;
			case 313:
			    return WeatherType.DRIZZLE;
			case 314:
			    return WeatherType.DRIZZLE_HEAVY;
			case 321:
			    return WeatherType.DRIZZLE;
			case 500:
			    return WeatherType.RAIN_LIGHT;
			case 501:
			    return WeatherType.RAIN;
			case 502:
			    return WeatherType.RAIN_HEAVY;
			case 503:
			    return WeatherType.RAIN_VERY_HEAVY;
			case 504:
			    return WeatherType.RAIN_VERY_HEAVY;
			case 511:
			    return WeatherType.RAIN;
			case 520:
			    return WeatherType.RAIN_LIGHT;
			case 521:
			    return WeatherType.RAIN;
			case 522:
			    return WeatherType.RAIN_HEAVY;
			case 531:
			    return WeatherType.RAIN;
			case 600:
			    return WeatherType.SNOW_LIGHT;
			case 601:
			    return WeatherType.SNOW;
			case 602:
			    return WeatherType.SNOW_HEAVY;
			case 611:
			    return WeatherType.SNOW;
			case 612:
			    return WeatherType.SNOW;
			case 615:
			    return WeatherType.SNOW_LIGHT;
			case 616:
			    return WeatherType.SNOW;
			case 620:
			    return WeatherType.SNOW_LIGHT;
			case 621:
			    return WeatherType.SNOW;
			case 622:
			    return WeatherType.SNOW_HEAVY;
			case 701:
			    return WeatherType.MIST;
			case 711:
			    return WeatherType.FOG;
			case 741:
			    return WeatherType.FOG;
			case 800:
			    return WeatherType.CLEAR_SKY;
			case 801:
			    return WeatherType.CLOUDS_FEW;
			case 802:
			    return WeatherType.CLOUDS_SCATTERED;
			case 803:
			    return WeatherType.CLOUDS_BROKEN;
			case 804:
			    return WeatherType.CLOUDS_OVERCAST;
			default:
			    return null;
			}
		    });
		});

	return data.toObservable();
    }

}
