package weatherly.datasource;

import org.jsoup.nodes.Document;

import rx.Observable;
import weatherly.events.WeatherlyEvent;

public abstract class WeatherHtmlTextDataProvider extends WeatherHtmlDataProvider {

    @Override
    public Observable<? extends WeatherlyEvent> parseResponse(final Document document) {
	return this.parseTextHtmlResponse(document.text());
    }

    public abstract Observable<? extends WeatherlyEvent> parseTextHtmlResponse(final String text);

}
