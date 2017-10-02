package weatherly.datasource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;

import rx.Observable;
import weatherly.events.WeatherlyDataAvailableEvent;

public class WeatherData {

    private final AirQualityData airQualityData = new AirQualityData();
    private final OptionalTerminableProperty<Double> cloudiness = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> groundPressure = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> humidity = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> latitude = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> longitude = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> seaLevelPressure = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Date> sunrise = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Date> sunset = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> temperature = new OptionalTerminableProperty<>();
    private final LocalDateTime timestamp;
    private final OptionalTerminableProperty<String> weatherDescription = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<WeatherType> weatherType = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> windDirection = new OptionalTerminableProperty<>();
    private final OptionalTerminableProperty<Double> windSpeed = new OptionalTerminableProperty<>();

    public WeatherData() {
	this.timestamp = LocalDateTime.now();
    }

    public OptionalProperty<String> computeWeatherDescription() {
	return this.weatherDescription.or(this.weatherType.mapValue(type -> type.getReadableDescription()));
    }

    public OptionalProperty<Direction> computeWindDirection() {
	return this.windDirection.mapValue(e -> Direction.from(e));
    }

    public AirQualityData getAirQualityData() {
	return this.airQualityData;
    }

    public OptionalTerminableProperty<Double> getCloudiness() {
	return this.cloudiness;
    }

    public OptionalTerminableProperty<Double> getGroundPressure() {
	return this.groundPressure;
    }

    public OptionalTerminableProperty<Double> getHumidity() {
	return this.humidity;
    }

    public OptionalTerminableProperty<Double> getLocationLatitude() {
	return this.latitude;
    }

    public OptionalTerminableProperty<Double> getLocationLongitude() {
	return this.longitude;
    }

    public OptionalTerminableProperty<Double> getSeaLevelPressure() {
	return this.seaLevelPressure;
    }

    public OptionalTerminableProperty<Date> getSunriseTime() {
	return this.sunrise;
    }

    public OptionalTerminableProperty<Date> getSunsetTime() {
	return this.sunset;
    }

    public OptionalTerminableProperty<Double> getTemperature() {
	return this.temperature;
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    public OptionalTerminableProperty<String> getWeatherDescription() {
	return this.weatherDescription;
    }

    public OptionalTerminableProperty<WeatherType> getWeatherType() {
	return this.weatherType;
    }

    public OptionalTerminableProperty<Double> getWindDirectionDeg() {
	return this.windDirection;
    }

    public OptionalTerminableProperty<Double> getWindSpeed() {
	return this.windSpeed;
    }

    public void push(final WeatherData latest) {

	this.airQualityData.push(latest.airQualityData);
	this.cloudiness.push(latest.cloudiness);
	this.groundPressure.push(latest.groundPressure);
	this.humidity.push(latest.humidity);
	this.latitude.push(latest.latitude);
	this.longitude.push(latest.longitude);
	this.seaLevelPressure.push(latest.seaLevelPressure);
	this.sunrise.push(latest.sunrise);
	this.sunset.push(latest.sunset);
	this.temperature.push(latest.temperature);
	this.weatherDescription.push(latest.weatherDescription);
	this.weatherType.push(latest.weatherType);
	this.windDirection.push(latest.windDirection);
	this.windSpeed.push(latest.windSpeed);
    }

    public WeatherData push(final WeatherDataProperty fieldType, final DataContainer data, final String key) {
	switch (fieldType) {
	case TEMPERATURE:
	    this.getTemperature().push(data.getDouble(key));
	    break;
	case HUMIDITY:
	    this.getHumidity().push(data.getDouble(key));
	    break;
	case PRESSURE_GROUND:
	    this.getGroundPressure().push(data.getDouble(key));
	    break;
	case PRESSURE_SEA:
	    this.getSeaLevelPressure().push(data.getDouble(key));
	    break;
	case WIND_SPEED:
	    this.getWindSpeed().push(data.getDouble(key));
	    break;
	case WIND_DIRECTION:
	    this.getWindDirectionDeg().push(data.getDouble(key));
	    break;
	case WEATHER_DESCRIPTION:
	    this.getWeatherDescription().push(data.getString(key));
	    break;
	case SUNSET:
	    this.getSunsetTime().push(data.getDate(key));
	    break;
	case SUNRISE:
	    this.getSunriseTime().push(data.getDate(key));
	    break;
	case WEATHER_TYPE:
	    this.getWeatherType().push(data.getString(key).mapValue(WeatherType::valueOf));
	    break;
	case CLOUDINESS:
	    this.getCloudiness().push(data.getDouble(key));
	    break;
	case LONGITUDE:
	    this.getLocationLongitude().push(data.getDouble(key));
	    break;
	case LATITUDE:
	    this.getLocationLatitude().push(data.getDouble(key));
	    break;
	case AIR_QUALITY: {
	    data.getDataProvider(key).iter(subdata -> {
		final Set<String> subdataKeys = subdata.keys();
		for (final String subdataKey : subdataKeys) {
		    final double value = subdata.getChild(subdataKey).asDouble();
		    this.getAirQualityData().pushSubstance(subdataKey, value);
		}
	    });
	    break;
	}
	default:
	    break;
	}
	return this;
    }

    public <T> WeatherData push(final WeatherDataProperty fieldType, final OptionalProperty<T> value) {
	if (!value.isAvailable()) {
	    return this;
	}
	return this.push(fieldType, new SingleDataContainer(value.get()), "");
    }

    public <T> WeatherData push(final WeatherDataProperty fieldType,
	    final Supplier<OptionalProperty<T>> valueSupplier) {
	return this.push(fieldType, valueSupplier.get());
    }

    public <T> WeatherData pushValue(final WeatherDataProperty fieldType, final T value) {
	return this.push(fieldType, new OptionalTerminableProperty<>(value));
    }

    public WeatherlyDataAvailableEvent toEvent() {
	return new WeatherlyDataAvailableEvent(this);
    }

    public Observable<WeatherlyDataAvailableEvent> toObservable() {
	return Observable.just(this.toEvent());
    }

    @Override
    public String toString() {
	String descr = "";
	descr += "latitude: " + this.latitude + "; ";
	descr += "longitude: " + this.longitude + "; ";
	descr += "cloudiness: " + this.cloudiness + "; ";
	descr += "groundPressure: " + this.groundPressure + "; ";
	descr += "humidity: " + this.humidity + "; ";
	descr += "seaLevelPressure: " + this.seaLevelPressure + "; ";
	descr += "sunrise: " + this.sunrise + "; ";
	descr += "sunset: " + this.sunset + "; ";
	descr += "temperature: " + this.temperature + "; ";
	descr += "weatherDescription: " + this.weatherDescription + "; ";
	descr += "weatherType: " + this.weatherType + "; ";
	descr += "windSpeed: " + this.windSpeed + "; ";
	descr += "windDirection: " + this.windDirection + "; ";
	descr += "airQuality: " + this.airQualityData + "; ";

	return "WeatherData {" + descr + "}";
    }

    public void validate(final int secondsTimeout) {

	this.cloudiness.validate(secondsTimeout, null);
	this.groundPressure.validate(secondsTimeout, null);
	this.humidity.validate(secondsTimeout, null);
	this.latitude.validate(secondsTimeout, null);
	this.longitude.validate(secondsTimeout, null);
	this.seaLevelPressure.validate(secondsTimeout, null);
	this.sunrise.validate(secondsTimeout, null);
	this.sunset.validate(secondsTimeout, null);
	this.temperature.validate(secondsTimeout, null);
	this.weatherDescription.validate(secondsTimeout, null);
	this.weatherType.validate(secondsTimeout, null);
	this.windDirection.validate(secondsTimeout, null);
	this.windSpeed.validate(secondsTimeout, null);
    }

}
