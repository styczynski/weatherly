package weatherly.datasource;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptionalProperty<T> {

    private T data;

    public OptionalProperty() {
	this.data = null;
	this.onDataUpdate();
    }

    public OptionalProperty(final T data) {
	this.data = data;
	this.onDataUpdate();
    }

    public static OptionalProperty<String> fromMatch(final String input, final String regexp) {
	return OptionalProperty.fromMatch(input, regexp, (m) -> new OptionalProperty<>(m.group()));
    }

    public static <T> OptionalProperty<T> fromMatch(final String input, final String regexp,
	    final Function<Matcher, OptionalProperty<T>> matchDataRetriever) {
	final Pattern p = Pattern.compile(regexp);
	final Matcher m = p.matcher(input);
	while (m.find()) {
	    return matchDataRetriever.apply(m);
	}
	return new OptionalProperty<>();
    }

    public static <T> OptionalProperty<T> fromValue(final T value) {
	return new OptionalProperty<>(value);
    }

    public String asString() {
	this.onDataRead();
	return this.data.toString();
    }

    public String asString(final String defaultString) {
	if (!this.isAvailable()) {
	    return defaultString;
	}
	this.onDataRead();
	return this.data.toString();
    }

    public String asString(final String defaultString, final String format) {
	if (!this.isAvailable()) {
	    return defaultString;
	}
	this.onDataRead();
	return String.format(format, this.data);
    }

    public OptionalProperty<? super T> generalize() {
	return this;
    }

    public T get() {
	this.onDataRead();
	return this.data;
    }

    public <E> E get(final E onNoValue, final Function<T, E> transformer) {
	if (!this.isAvailable()) {
	    return onNoValue;
	}
	this.onDataRead();
	return transformer.apply(this.data);
    }

    public T get(final T onNoValue) {
	if (!this.isAvailable()) {
	    return onNoValue;
	}
	this.onDataRead();
	return this.data;
    }

    public boolean isAvailable() {
	return this.data != null;
    }

    public void iter(final Consumer<T> consumer) {
	if (!this.isAvailable()) {
	    return;
	}
	this.onDataRead();
	consumer.accept(this.data);
    }

    public <E> OptionalProperty<E> map(final Function<T, OptionalProperty<E>> transformer) {
	if (!this.isAvailable()) {
	    return new OptionalProperty<>();
	}
	return transformer.apply(this.get());
    }

    public <E> OptionalProperty<E> mapValue(final Function<T, E> transformer) {
	if (!this.isAvailable()) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(transformer.apply(this.get()));
    }

    public OptionalProperty<T> or(final Function<OptionalProperty<T>, OptionalProperty<T>> other) {
	if (!this.isAvailable()) {
	    return other.apply(this);
	}
	return this;
    }

    public OptionalProperty<T> or(final OptionalProperty<T> other) {
	if (!this.isAvailable()) {
	    return other;
	}
	return this;
    }

    public OptionalProperty<T> push(final OptionalProperty<T> value) {
	if (value.isAvailable()) {
	    this.data = value.data;
	    this.onDataUpdate();
	}
	return this;
    }

    public OptionalProperty<T> set(final T value) {
	this.data = value;
	this.onDataUpdate();
	return this;
    }

    @Override
    public String toString() {
	if (this.isAvailable()) {
	    return "(" + this.data + ")";
	}
	return "N/A";
    }

    protected void onDataRead() {

    }

    protected void onDataUpdate() {

    }

}
