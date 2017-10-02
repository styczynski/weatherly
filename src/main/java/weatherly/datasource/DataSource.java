package weatherly.datasource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import rx.Observable;
import weatherly.events.WeatherlyEvent;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataSource {

    @XmlAttribute(name = "provider")
    private String provider;

    private DataProvider providerObj;

    public Observable<? extends WeatherlyEvent> dataStream(final Integer pollInterval) {
	return this.providerObj.dataSourceStream(pollInterval);
    }

    public String getName() {
	if (this.providerObj == null) {
	    return "";
	}
	return this.providerObj.getName();
    }

    public DataProvider getProvider() {
	return this.providerObj;
    }

    public void initSource() {
	this.updateProviderFromName();
    }

    @Override
    public String toString() {
	if (this.providerObj == null) {
	    return " Empty data source";
	}
	return this.providerObj.getName();
    }

    private void updateProviderFromName() {
	Class<?> clazz;
	try {
	    clazz = Class.forName(this.provider);
	    final Constructor<?> ctor = clazz.getConstructor();
	    final Object object = ctor.newInstance();
	    this.providerObj = (DataProvider) object;
	} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException
		| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    e.printStackTrace();
	}

    }
}
