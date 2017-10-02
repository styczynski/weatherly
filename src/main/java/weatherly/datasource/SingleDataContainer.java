package weatherly.datasource;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SingleDataContainer implements DataContainer {
    private final Object contents;

    public SingleDataContainer(final Object contents) {
	this.contents = contents;
    }

    /*
     * @SuppressWarnings("unchecked") public Object find(final String path) { if
     * (path.equals("")) { return this.contents; } Object entry = this.contents;
     * final String tokens[] = path.split(Pattern.quote(".")); for (final String
     * token : tokens) { final String arrTokens[] = token.split("[\\[\\]]"); if
     * (token.charAt(0) != '[') { entry = ((Map<?, ?>) entry).get(arrTokens[0]);
     * } for (int i = 1; i < arrTokens.length; ++i) { entry = ((List<?>)
     * entry).get(Integer.parseInt(arrTokens[0])); } } return entry; }
     */

    @Override
    public boolean findBoolean(final String path) {
	return (boolean) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public byte findByte(final String path) {
	return (byte) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public char findCharacter(final String path) {
	return (char) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public SingleDataContainer findDataProvider(final String path) {
	return new SingleDataContainer(this.find(path));
    }

    @Override
    public double findDouble(final String path) {
	return (double) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public float findFloat(final String path) {
	return (float) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public int findInt(final String path) {
	return (int) ((SingleDataContainer) this.find(path)).contents;
    }

    @Override
    public Set<String> findKeys(final String path) {
	final Object node = this.find(path);
	if (node instanceof Map<?, ?>) {
	    return ((Map<?, ?>) this.find(path)).entrySet().stream().map(entry -> entry.getKey().toString())
		    .collect(Collectors.toSet());
	} else if (node instanceof List<?>) {
	    final int size = ((List<?>) this.find(path)).size();
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
	return ((SingleDataContainer) this.find(path)).contents.toString();
    }

    @Override
    public DataContainer getChild(final String key) {
	if (this.contents instanceof Map<?, ?>) {
	    return new SingleDataContainer(((Map<?, ?>) this.contents).get(key));
	} else if (this.contents instanceof List<?>) {
	    return new SingleDataContainer(((List<?>) this.contents).get(Integer.parseInt(key)));
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
