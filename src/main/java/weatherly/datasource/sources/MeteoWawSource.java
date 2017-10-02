package weatherly.datasource.sources;

import javax.measure.UnitConverter;

import tec.units.ri.unit.Units;
import weatherly.datasource.OptionalProperty;
import weatherly.datasource.WeatherDataProperty;
import weatherly.datasource.WeatherHtmlSimpleTextDataProvider;

public class MeteoWawSource extends WeatherHtmlSimpleTextDataProvider {

    @Override
    public String getName() {
	return "www.meteo.waw.pl";
    }

    @Override
    public String getUrl() {
	return "http://www.meteo.waw.pl/";
    }

    @Override
    public void setup(final String text) {

	this.addMatcher(WeatherDataProperty.TEMPERATURE,
		(input) -> OptionalProperty.fromMatch(input, "temp[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    final UnitConverter toKelvins = Units.CELSIUS.getConverterTo(Units.KELVIN);
		    return new OptionalProperty<>(toKelvins.convert(value));
		}));

	this.addMatcher(WeatherDataProperty.HUMIDITY,
		(input) -> OptionalProperty.fromMatch(input, "wilg[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    return new OptionalProperty<>(value);
		}));

	this.addMatcher(WeatherDataProperty.PRESSURE_GROUND,
		(input) -> OptionalProperty.fromMatch(input, "ci[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    return new OptionalProperty<>(value);
		}));

	this.addMatcher(WeatherDataProperty.PRESSURE_GROUND,
		(input) -> OptionalProperty.fromMatch(input, "ci[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    return new OptionalProperty<>(value);
		}));

	this.addMatcher(WeatherDataProperty.WIND_SPEED,
		(input) -> OptionalProperty.fromMatch(input, "wiatru pr[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    return new OptionalProperty<>(value);
		}));

	this.addMatcher(WeatherDataProperty.WIND_DIRECTION,
		(input) -> OptionalProperty.fromMatch(input, "kierunek[^\\s]* ([0-9]+,[0-9]+)", (m) -> {
		    final double value = new Double(m.group(1).replace(',', '.'));
		    return new OptionalProperty<>(value);
		}));
    }

}
