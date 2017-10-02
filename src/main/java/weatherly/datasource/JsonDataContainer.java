package weatherly.datasource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;

public class JsonDataContainer implements DataContainer {

    private final JsonElement contents;

    public JsonDataContainer(final JsonElement contents) {
	this.contents = contents;
    }

    @Override
    public boolean findBoolean(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsBoolean();
    }

    @Override
    public byte findByte(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsByte();
    }

    @Override
    public char findCharacter(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsCharacter();
    }

    @Override
    public JsonDataContainer findDataProvider(final String path) {
	return (JsonDataContainer) this.find(path);
    }

    @Override
    public double findDouble(final String path) {
	final JsonDataContainer p = (JsonDataContainer) this.find(path);
	final JsonElement e = p.contents;
	return e.getAsDouble();
    }

    @Override
    public float findFloat(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsFloat();
    }

    @Override
    public int findInt(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsInt();
    }

    @Override
    public Set<String> findKeys(final String path) {
	final JsonElement node = ((JsonDataContainer) this.find(path)).contents;
	if (node.isJsonObject()) {
	    return node.getAsJsonObject().entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toSet());
	} else if (node.isJsonArray()) {
	    final int size = node.getAsJsonArray().size();
	    final HashSet<String> ret = new HashSet<>();
	    for (int i = 0; i < size; ++i) {
		ret.add("" + i);
	    }
	    return ret;
	} else {
	    return new HashSet<>();
	}
    }

    @Override
    public String findString(final String path) {
	return ((JsonDataContainer) this.find(path)).contents.getAsString();
    }

    @Override
    public DataContainer getChild(final String key) {
	if (this.contents.isJsonObject()) {
	    return new JsonDataContainer(this.contents.getAsJsonObject().get(key));
	} else if (this.contents.isJsonArray()) {
	    return new JsonDataContainer(this.contents.getAsJsonArray().get(Integer.parseInt(key)));
	}
	return this;
    }

    @Override
    public boolean has(final String path) {
	try {
	    return this.find(path) != null;
	} catch (final Exception e) {
	    return false;
	}
    }
}
