package weatherly.events;

import javafx.beans.binding.Binding;
import rx.Observable;
import rx.Observable.Transformer;
import rx.Subscription;
import rx.functions.Func1;
import rx.javafx.sources.CompositeObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;
import rx.subscribers.JavaFxSubscriber;

public class EventStream {

    private static EventStream instance = new EventStream();

    private final CompositeObservable<WeatherlyEvent> composite = new CompositeObservable<>();

    public static class WrappedObservable<T> {
	final Observable<T> inner;

	public WrappedObservable(final Observable<T> inner) {
	    this.inner = inner;
	}

	public <U> WrappedObservable<T> andOn(final Observable<U> observable,
		final Func1<? super U, ? extends T> mapper) {
	    return new WrappedObservable<>(this.inner.mergeWith(observable.map(mapper)));
	}

	public <U> WrappedObservable<T> andOn(final Observable<U> observable, final T value) {
	    return new WrappedObservable<>(this.inner.mergeWith(EventStream.fixedMap(observable, value)));
	}

	public Binding<T> toBinding() {
	    return EventStream.binding(this.inner);
	}

	public Observable<T> toObservable() {
	    return this.inner;
	}
    }

    public static <T> Binding<T> binding(final Observable<T> observable) {
	return JavaFxSubscriber.toBinding(observable);
    }

    public static EventStream eventStream() {
	return EventStream.instance;
    }

    public static <T> Observable<T> fixedMap(final Observable<?> observable, final T value) {
	return observable.map(ignore -> value);
    }

    public static Subscription joinBackpressuredStream(final Observable<? extends WeatherlyEvent> observable) {
	return EventStream.joinStream(observable.onBackpressureLatest());
    }

    @SuppressWarnings("unchecked")
    public static Subscription joinStream(final Observable<? extends WeatherlyEvent> observable) {
	final CompositeObservable<WeatherlyEvent> local = EventStream.eventStream().composite;
	synchronized (local) {
	    return local.addAll((Observable<WeatherlyEvent>) observable);
	}
    }

    public static <T, U> WrappedObservable<U> onEvent(final Observable<T> observable,
	    final Func1<? super T, ? extends U> mapper) {
	return new WrappedObservable<>(observable.map(mapper));
    }

    public static <T, U> WrappedObservable<U> onEvent(final Observable<T> observable, final U value) {
	return new WrappedObservable<>(EventStream.fixedMap(observable, value));
    }

    public Observable<WeatherlyEvent> events() {
	return EventStream.eventStream().composite.toObservable();
    }

    public Observable<WeatherlyEvent> eventsInFx() {
	return this.events().compose(this.fxTransformer());
    }

    public Observable<WeatherlyEvent> eventsInIO() {
	return this.events().compose(this.ioTransformer());
    }

    private <T> Transformer<T, T> fxTransformer() {
	return obs -> obs.observeOn(JavaFxScheduler.getInstance());
    }

    private <T> Transformer<T, T> ioTransformer() {
	return obs -> obs.observeOn(Schedulers.io());
    }
}
