package weatherly.events;

import java.time.LocalDateTime;

public class WeatherlyErrorEvent extends WeatherlyEvent {
    private final Throwable cause;
    private final LocalDateTime timestamp;

    public WeatherlyErrorEvent(final Throwable cause) {
	this.timestamp = LocalDateTime.now();
	this.cause = cause;
    }

    public Throwable getCause() {
	return this.cause;
    }

    public LocalDateTime getTimestamp() {
	return this.timestamp;
    }

    @java.lang.Override
    public java.lang.String toString() {
	return "WeatherlyErrorEvent(timestamp=" + this.getTimestamp() + ", cause=" + this.getCause() + ")";
    }

}