package weatherly.datasource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.netty.RxNetty;
import rx.Observable;
import weatherly.events.WeatherlyEvent;

public abstract class WeatherHtmlDataProvider extends DataProvider {

    public abstract String getUrl();

    public abstract Observable<? extends WeatherlyEvent> parseResponse(Document document);

    @Override
    protected Observable<? extends WeatherlyEvent> makeRequest() {
	return RxNetty.createHttpRequest(this.prepareHttpGETRequest(WeatherHtmlDataProvider.this.getUrl()))
		.compose(this::unpackResponse).map(Jsoup::parse).flatMap(document -> {
		    return this.parseResponse(document);
		});
    }

}
