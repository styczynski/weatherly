package weatherly.datasource;

import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

import org.pojava.datetime.DateTime;
import org.pojava.datetime.DateTimeConfig;

public interface DataContainer {

    public default boolean asBoolean() {
	return this.findBoolean("");
    }

    public default byte asByte() {
	return this.findByte("");
    }

    public default char asCharacter() {
	return this.findCharacter("");
    }

    public default DataContainer asDataProvider() {
	return this.findDataProvider("");
    }

    public default Date asDate() {
	return this.findDate("");
    }

    public default double asDouble() {
	return this.findDouble("");
    }

    public default float asFloat() {
	return this.findFloat("");
    }

    public default int asInt() {
	return this.findInt("");
    }

    public default String asString() {
	return this.findString("");
    }

    public default DataContainer find(final String path) {
	if (path.equals("")) {
	    return this;
	}
	DataContainer entry = this;

	final String otherThanQuote = " [^\"] ";
	final String quotedString = String.format(" \" %s* \" ", otherThanQuote);
	final String regex = String.format(
		"(?x) " + // enable comments, ignore
			  // white spaces
			"\\.                         " + // match a comma
			"(?=                       " + // start positive look
						       // ahead
			"  (?:                     " + // start non-capturing
						       // group 1
			"    %s*                   " + // match 'otherThanQuote'
						       // zero or
						       // more times
			"    %s                    " + // match 'quotedString'
			"  )*                      " + // end group 1 and repeat
						       // it zero
						       // or more times
			"  %s*                     " + // match 'otherThanQuote'
			"  $                       " + // match the end of the
						       // string
			")                         ", // stop positive look
						      // ahead
		otherThanQuote, quotedString, otherThanQuote);
	final String[] tokens = path.split(regex, -1);

	for (final String token : tokens) {
	    final String arrTokens[] = token.split("[\\[\\]]");
	    for (String arrToken : arrTokens) {
		arrToken = arrToken.replaceAll("^\"|\"$", "");
		if (!arrToken.equals("")) {
		    entry = entry.getChild(arrToken);
		}
	    }
	}
	return entry;
    }

    public boolean findBoolean(final String path);

    public byte findByte(final String path);

    public char findCharacter(final String path);

    public DataContainer findDataProvider(final String path);

    public default Date findDate(final String path) {
	try {
	    final long milis = Long.parseLong(this.findString(path)) * 1000;
	    return new Date(milis);
	} catch (final Exception e) {

	}
	final DateTimeConfig config = DateTimeConfig.getGlobalDefault();
	final DateTime dt = DateTime.parse(this.findString(path), config);
	return dt.toDate();
    }

    public double findDouble(final String path);

    public float findFloat(final String path);

    public int findInt(final String path);

    public Set<String> findKeys(final String path);

    public String findString(final String path);

    public default OptionalProperty<Boolean> getBoolean(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findBoolean(path));
    }

    public default OptionalProperty<Byte> getByte(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findByte(path));
    }

    public default OptionalProperty<Character> getCharacter(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findCharacter(path));
    }

    public DataContainer getChild(final String key);

    public default OptionalProperty<DataContainer> getDataProvider(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findDataProvider(path));
    }

    public default OptionalProperty<Date> getDate(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findDate(path));
    }

    public default OptionalProperty<Double> getDouble(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findDouble(path));
    }

    public default OptionalProperty<Float> getFloat(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findFloat(path));
    }

    public default OptionalProperty<Integer> getInt(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findInt(path));
    }

    public default OptionalProperty<Set<String>> getKeys(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findKeys(path));
    }

    public default OptionalProperty<String> getString(final String path) {
	if (!this.has(path)) {
	    return new OptionalProperty<>();
	}
	return new OptionalProperty<>(this.findString(path));
    }

    public boolean has(final String path);

    public default void iter(final Consumer<DataContainer> consumer) {
	this.iter("", consumer);
    }

    public default void iter(final String path, final Consumer<DataContainer> consumer) {
	this.getDataProvider(path).iter(subnode -> {
	    final Set<String> keys = subnode.keys();
	    for (final String key : keys) {
		consumer.accept(subnode.getChild(key));
	    }
	});
    }

    public default Set<String> keys() {
	return this.findKeys("");
    }

}
