package weatherly.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import rx.Observable;
import weatherly.events.WeatherlyEvent;

public abstract class WeatherHtmlSimpleTextDataProvider extends WeatherHtmlTextDataProvider {

    private final Map<WeatherDataProperty, Function<String, OptionalProperty<?>>> matchers = new HashMap<>();

    public void addMatcher(final WeatherDataProperty field, final Function<String, OptionalProperty<?>> matcher) {
	this.matchers.put(field, matcher);
    }

    @Override
    public Observable<? extends WeatherlyEvent> parseTextHtmlResponse(String text) {
	text = text.replaceAll("\\s+", " ");
	final WeatherData data = new WeatherData();

	this.setup(text);

	for (final WeatherDataProperty key : this.matchers.keySet()) {
	    data.push(key, this.matchers.get(key).apply(text));
	}
	return data.toObservable();
    }

    public abstract void setup(final String text);

}
