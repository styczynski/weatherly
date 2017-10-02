package weatherly.meters;

import java.util.Set;

import javax.measure.UnitConverter;

import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;
import org.ocpsoft.prettytime.PrettyTime;

import javafx.scene.transform.Rotate;
import tec.units.ri.unit.Units;
import weatherly.datasource.Direction;
import weatherly.datasource.OptionalProperty;
import weatherly.datasource.WeatherData;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataUpdateEvent;

public class GeneralMeter extends Meter {

    private String airQualityProperties = "all";

    private final int FONT_SIZE = 18;

    @Override
    public String name() {
	switch (this.getConfiguration("type", "all")) {
	case "airQuality":
	    return "Air substances";
	case "temperature":
	    return "Temperature";
	case "pressure":
	    return "Pressure";
	case "humidity":
	    return "Humidity";
	case "sunset":
	    return "Sunset";
	case "wind":
	    return "Wind spped";
	case "sunrise":
	    return "Sunrise";
	case "weather":
	    return "Weather";
	default:
	    return "";
	}

    }

    public void setAirQualityProperties(final String str) {
	this.airQualityProperties = str;
    }

    @Override
    public void setup() {

	EventStream.eventStream().eventsInFx().ofType(WeatherlyDataUpdateEvent.class).subscribe(event -> {
	    final WeatherData data = event.getData();
	    switch (this.getConfiguration("type", "all")) {
	    case "airQuality": {
		final FontIcon icon = new FontIcon(FontAwesome.FILTER);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);

		final Set<String> substances = data.getAirQualityData().substances();

		if (substances.isEmpty()) {
		    this.getMeterLabelProperty().setValue("n/a");
		} else {
		    String descr = "";
		    if (this.airQualityProperties.equals("all")) {
			for (final String substance : substances) {
			    final OptionalProperty<Double> substanceDetails = data.getAirQualityData()
				    .getSubstance(substance);
			    descr += String.format("%-6s : %3.2f µg/m3\n", substance, substanceDetails.get(0.0));
			}
		    } else {
			final String airQualityProps[] = this.airQualityProperties.split(",");
			for (final String substance : airQualityProps) {
			    final OptionalProperty<Double> substanceDetails = data.getAirQualityData()
				    .getSubstance(substance);
			    if (substanceDetails.isAvailable()) {
				descr += String.format("%-10s :  %3.2f µg/m3\n", substance, substanceDetails.get(0.0));
			    }
			}
		    }
		    this.getMeterLabelProperty().setValue(descr);
		}

		// this.getMeterLabelProperty().setValue(data.getAirQualityData().substances().);
		break;
	    }
	    case "temperature": {
		final FontIcon icon = new FontIcon(WeatherIcons.THERMOMETER);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		final UnitConverter toCelcius = Units.KELVIN.getConverterTo(Units.CELSIUS);
		this.getMeterLabelProperty()
			.setValue(data.getTemperature().get("n/a", e -> String.format("%.2f C", toCelcius.convert(e))));
		break;
	    }
	    case "pressure": {
		final FontIcon icon = new FontIcon(WeatherIcons.BAROMETER);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		this.getMeterLabelProperty()
			.setValue(data.getGroundPressure().get("n/a", e -> String.format("%.2f HPa", e)));
		break;
	    }
	    case "humidity": {
		final FontIcon icon = new FontIcon(WeatherIcons.HUMIDITY);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		this.getMeterLabelProperty().setValue(data.getHumidity().get("n/a", e -> String.format("%.0f %%", e)));
		break;
	    }
	    case "sunset": {
		final PrettyTime p = new PrettyTime();
		final FontIcon icon = new FontIcon(WeatherIcons.SUNSET);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		this.getMeterLabelProperty().setValue(data.getSunsetTime().get("n/a", e -> p.format(e)));
		break;
	    }
	    case "wind": {
		final FontIcon icon = new FontIcon(WeatherIcons.WIND_DIRECTION);

		final Rotate rotationTransform = data.getWindDirectionDeg().mapValue(e -> {
		    return new Rotate(e, 10, -5);
		}).get(new Rotate(0));

		icon.getTransforms().add(rotationTransform);

		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		final OptionalProperty<Direction> windDirection = data.computeWindDirection();
		final OptionalProperty<Double> windSpeed = data.getWindSpeed();

		if (!windSpeed.isAvailable() && !windDirection.isAvailable()) {
		    this.getMeterLabelProperty().setValue("n/a");
		} else if (windSpeed.isAvailable() && windDirection.isAvailable()) {
		    this.getMeterLabelProperty()
			    .setValue(String.format("%.0f m/s   %s", windSpeed.get(), windDirection.get().toString()));
		} else if (windSpeed.isAvailable()) {
		    this.getMeterLabelProperty().setValue(String.format("%.0f m/s", windSpeed.get()));
		} else {
		    this.getMeterLabelProperty()
			    .setValue(String.format("Direction %s", windDirection.get().toString()));
		}

		break;
	    }
	    case "sunrise": {
		final PrettyTime p = new PrettyTime();
		final FontIcon icon = new FontIcon(WeatherIcons.SUNRISE);
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);
		this.getMeterLabelProperty().setValue(data.getSunriseTime().get("n/a", e -> p.format(e)));
		break;
	    }
	    case "weather": {
		final FontIcon icon = data.getWeatherType().mapValue(e -> {
		    switch (e) {
		    case CLEAR_SKY:
			return new FontIcon(WeatherIcons.DAY_SUNNY);
		    case CLOUDS_BROKEN:
			return new FontIcon(WeatherIcons.DAY_CLOUDY);
		    case CLOUDS_FEW:
			return new FontIcon(WeatherIcons.DAY_CLOUDY);
		    case CLOUDS_OVERCAST:
			return new FontIcon(WeatherIcons.CLOUDY);
		    case CLOUDS_SCATTERED:
			return new FontIcon(WeatherIcons.CLOUD);
		    case DRIZZLE:
			return new FontIcon(WeatherIcons.DAY_SLEET);
		    case DRIZZLE_HEAVY:
			return new FontIcon(WeatherIcons.DAY_SHOWERS);
		    case DRIZZLE_LIGHT:
			return new FontIcon(WeatherIcons.DAY_SLEET);
		    case FOG:
			return new FontIcon(WeatherIcons.DAY_FOG);
		    case MIST:
			return new FontIcon(WeatherIcons.DAY_FOG);
		    case RAIN:
			return new FontIcon(WeatherIcons.DAY_RAIN);
		    case RAIN_HEAVY:
			return new FontIcon(WeatherIcons.DAY_SPRINKLE);
		    case RAIN_LIGHT:
			return new FontIcon(WeatherIcons.DAY_SHOWERS);
		    case RAIN_VERY_HEAVY:
			return new FontIcon(WeatherIcons.DAY_SPRINKLE);
		    case SNOW:
			return new FontIcon(WeatherIcons.DAY_SNOW);
		    case SNOW_HEAVY:
			return new FontIcon(WeatherIcons.DAY_SNOW);
		    case SNOW_LIGHT:
			return new FontIcon(WeatherIcons.DAY_SNOW);
		    case THUNDERSTORM:
			return new FontIcon(WeatherIcons.DAY_THUNDERSTORM);
		    case THUNDERSTORM_HEAVY:
			return new FontIcon(WeatherIcons.THUNDERSTORM);
		    case THUNDERSTORM_LIGHT:
			return new FontIcon(WeatherIcons.DAY_THUNDERSTORM);
		    default:
			return new FontIcon(WeatherIcons.DAY_SUNNY);
		    }
		}).get(new FontIcon(WeatherIcons.DAY_SUNNY));
		icon.setIconSize(this.FONT_SIZE);
		this.getMeterIconProperty().setValue(icon);

		this.getMeterLabelProperty().setValue(data.computeWeatherDescription().get("n/a", e -> e));
		break;
	    }
	    default: {
		this.getMeterLabelProperty().setValue("Wrong type");
		break;
	    }
	    }
	});

    }

}
