package weatherly.datasource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.CharsetUtil;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import rx.Observable;
import weatherly.events.WeatherlyEvent;

public abstract class WeatherJsonDataProvider extends DataProvider {

    public static HttpClientRequest<ByteBuf> withJsonHeader(final HttpClientRequest<ByteBuf> request) {
	/*
	 * Adds Accept: application/json header to an HTTP request
	 */
	return request.withHeader(HttpHeaderNames.ACCEPT.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
		.withHeader(HttpHeaderNames.ACCEPT_CHARSET.toString(), CharsetUtil.UTF_8.name());
    }

    private static JsonElement parse(final String s) {
	return new JsonParser().parse(s);
    }

    public abstract String getUrl();

    public abstract Observable<? extends WeatherlyEvent> parseResponse(JsonDataContainer object);

    @Override
    protected Observable<? extends WeatherlyEvent> makeRequest() {
	return RxNetty
		.createHttpRequest(WeatherJsonDataProvider
			.withJsonHeader(this.prepareHttpGETRequest(WeatherJsonDataProvider.this.getUrl())))
		.compose(this::unpackResponse).map(WeatherJsonDataProvider::parse).flatMap(jsonObject -> {
		    return this.parseResponse(new JsonDataContainer(jsonObject));
		});
    }

}
