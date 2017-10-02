package weatherly.datasource;

public class WeatherDescription {

    private final String description;
    private final WeatherType type;

    public WeatherDescription(final String description, final WeatherType type) {
	this.description = description;
	this.type = type;
    }

    public String getDescription() {
	if (this.description == null) {
	    return this.type.getReadableDescription();
	}
	return this.description;
    }

    public WeatherType getType() {
	return this.type;
    }

    @Override
    public String toString() {
	return this.getDescription();
    }
}
