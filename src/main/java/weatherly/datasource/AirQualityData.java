package weatherly.datasource;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AirQualityData {

    private final HashMap<String, Double> substanceQuantity = new HashMap<>();

    public AirQualityData() {

    }

    public OptionalProperty<Double> getSubstance(final String substanceName) {
	if (this.substanceQuantity.containsKey(substanceName)) {
	    return new OptionalProperty<>(this.substanceQuantity.get(substanceName));
	}
	return new OptionalProperty<>();
    }

    public void iter(final BiConsumer<String, Double> consumer) {
	this.iter(consumer, (self) -> {
	});
    }

    public void iter(final BiConsumer<String, Double> whenDataConsumer,
	    final Consumer<AirQualityData> whenNoDataConsumer) {
	final Set<String> keys = this.substanceQuantity.keySet();
	if (keys.isEmpty()) {
	    whenNoDataConsumer.accept(this);
	    return;
	}
	for (final String key : keys) {
	    whenDataConsumer.accept(key, this.substanceQuantity.get(key));
	}
    }

    public void push(final AirQualityData data) {
	this.substanceQuantity.putAll(data.substanceQuantity);
    }

    public void pushSubstance(final String substanceName, final double quantity) {
	this.substanceQuantity.put(substanceName, quantity);
    }

    public Set<String> substances() {
	return this.substanceQuantity.keySet();
    }

    @Override
    public String toString() {

	String descr = "";
	final Set<String> keys = this.substanceQuantity.keySet();
	;
	for (final String key : keys) {
	    descr += key + ": " + this.substanceQuantity.get(key) + "; ";
	}

	return "AirQualityData {" + descr + "}";
    }
}
