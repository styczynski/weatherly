package weatherly.meters;

import java.util.HashMap;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Meter {

    private HashMap<String, String> configuration = new HashMap<>();
    private final Property<FontIcon> icon;
    private final Property<String> label;

    public Meter() {
	super();
	this.icon = new SimpleObjectProperty<>(null);
	this.label = new SimpleObjectProperty<>(null);
    }

    public void configure(final HashMap<String, String> settings) {
	this.configuration = settings;
	this.onConfigurationChanged();
    }

    public HashMap<String, String> getConfiguration() {
	return this.configuration;
    }

    public String getConfiguration(final String propertyName, final String defaultValue) {
	if (!this.configuration.containsKey(propertyName)) {
	    return defaultValue;
	}
	return this.configuration.get(propertyName);
    }

    public Property<FontIcon> getMeterIconProperty() {
	return this.icon;
    }

    public Property<String> getMeterLabelProperty() {
	return this.label;
    }

    public abstract String name();

    public void onConfigurationChanged() {

    }

    public abstract void setup();
}
