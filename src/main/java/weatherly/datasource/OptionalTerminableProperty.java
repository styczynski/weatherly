package weatherly.datasource;

import java.time.LocalDateTime;

public class OptionalTerminableProperty<T> extends OptionalProperty<T> {

    private LocalDateTime timestamp = null;

    public OptionalTerminableProperty() {
	super();
	this.timestamp = LocalDateTime.now();
    }

    public OptionalTerminableProperty(final T value) {
	super(value);
	this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    public boolean olderThan(final int seconds) {
	return this.timestamp.plusSeconds(seconds).compareTo(LocalDateTime.now()) < 0;
    }

    public void validate(final int secondsTimeout, final T defaultValue) {
	if (this.olderThan(secondsTimeout)) {
	    this.set(defaultValue);
	}
    }

    private void refresh() {
	this.timestamp = LocalDateTime.now();
    }

    @Override
    protected void onDataUpdate() {
	this.refresh();
    }
}
