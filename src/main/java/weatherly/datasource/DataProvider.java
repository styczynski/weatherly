package weatherly.datasource;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rx.schedulers.Schedulers;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataSourceChangedEvent;
import weatherly.events.WeatherlyDataSourceRequestFinishedEvent;
import weatherly.events.WeatherlyDataSourceTimeoutEvent;
import weatherly.events.WeatherlyErrorEvent;
import weatherly.events.WeatherlyEvent;
import weatherly.events.WeatherlyRefreshRequestEvent;

public abstract class DataProvider {

    private static final int DEFAULT_POLL_INTERVAL = 60;

    private static final int INITIAL_DELAY = 1;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataProvider.class);

    private static final int TIMEOUT = 20;

    public DataProvider() {

    }

    public Observable<? extends WeatherlyEvent> dataSourceStream() {
	return this.dataSourceStream(DataProvider.DEFAULT_POLL_INTERVAL);
    }

    public Observable<? extends WeatherlyEvent> dataSourceStream(Integer pollInterval) {
	/*
	 * This creates a stream of data events. Each event emitted corresponds
	 * to a piece of data fetched from a remote (i.e. Internet) data source.
	 * This class is capable of grabbing data in one of two ways. Firstly,
	 * it can poll the data source every POLL_INTERVAL seconds. Secondly, it
	 * can fetch data on request (e.g. when a user hits the refresh button
	 * which causes a RefreshRequestEvent to be triggered; the event is
	 * handled here). The code below essentially merges events that arrive
	 * via one of the two routes into a single stream of events.
	 */
	if (pollInterval == null) {
	    pollInterval = DataProvider.DEFAULT_POLL_INTERVAL;
	}
	return this.filterStreamEnabled(this.fixedIntervalStream(pollInterval)).compose(this::wrapRequest)
		.mergeWith(this
			.filterStreamEnabled(
				EventStream.eventStream().eventsInIO().ofType(WeatherlyRefreshRequestEvent.class))
			.compose(this::wrapRequest));
    }

    public abstract String getName();

    @Override
    public String toString() {
	return "DataProvider(" + this.getClass().getName() + ")";
    }

    private Observable<?> filterStreamEnabled(final Observable<?> observable) {

	return Observable.combineLatest(EventStream.eventStream().eventsInFx()
		.ofType(WeatherlyDataSourceChangedEvent.class).filter(lockEvent -> {
		    return lockEvent.getSource().getProvider().getName().equals(this.getName());
		}), observable, (lockEvent, value) -> {

		    switch (lockEvent.getType()) {
		    case DISABLED_EVENT:
			return null;
		    case ENABLED_EVENT:
			return value;
		    default:
			return value;
		    }
		}).filter(e -> e != null);

    }

    private <T> Observable<WeatherlyEvent> wrapRequest(final Observable<T> observable) {

	/*
	 * Issues an HTTP query but emits an appropriate even before the query
	 * is made and another event when the query is completed. This allows us
	 * to give visual feedback (spinning icon) to the user during the
	 * request.
	 */

	return observable.flatMap(ignore -> Observable.concat(Observable.just(new WeatherlyDataSourceTimeoutEvent()), // emit
		// NetworkRequestIssuedEvent
		// before
		this.makeRequest().timeout(DataProvider.TIMEOUT, TimeUnit.SECONDS).doOnError(DataProvider.log::error) // if
			// a
			// request
			// takes
			// more
			// than
			// 30
			// seconds
			// abort
			// it
			.cast(WeatherlyEvent.class).onErrorReturn(WeatherlyErrorEvent::new), // and
		// emit
		// an
		// error
		// event
		Observable.just(new WeatherlyDataSourceRequestFinishedEvent())) // emit
	// NetworkRequestFinishedEvent
	// after
	);
    }

    protected Observable<Long> fixedIntervalStream(Integer pollInterval) {
	if (pollInterval == null) {
	    pollInterval = DataProvider.DEFAULT_POLL_INTERVAL;
	}
	return Observable.interval(DataProvider.INITIAL_DELAY, pollInterval, TimeUnit.SECONDS, Schedulers.io());
    }

    protected abstract Observable<? extends WeatherlyEvent> makeRequest();

    protected HttpClientRequest<ByteBuf> prepareHttpGETRequest(final String url) {
	/*
	 * As the name says, this creates an HTTP GET request (but does not send
	 * it, sending is done elsewhere).
	 */
	return HttpClientRequest.createGet(url);
    }

    protected Observable<String> unpackResponse(final Observable<HttpClientResponse<ByteBuf>> responseObservable) {
	/*
	 * Extracts HTTP response's body to a plain Java string
	 */
	return responseObservable.flatMap(HttpClientResponse::getContent)
		.map(buffer -> buffer.toString(CharsetUtil.UTF_8));
    }
}
