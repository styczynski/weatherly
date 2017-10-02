package weatherly.meters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rx.Observable;
import rx.observables.JavaFxObservable;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyMetersLoadedEvent;

@XmlRootElement(name = "meters")
public class Meters {

    @XmlAnyElement
    private List<Object> meter;

    private final ObservableList<Meter> meters = FXCollections.observableArrayList();

    public Observable<Meter> getMetersList() {
	return Observable.from(this.meters);
    }

    public void initMeters() {
	for (final Object meterObj : this.meter) {
	    final Node meterNode = (Node) meterObj;
	    final String nodeName = meterNode.getNodeName();
	    try {
		final Class<?> clazz = Class.forName(nodeName);

		final HashMap<String, String> attrsMap = new HashMap<>();
		final NamedNodeMap attrs = meterNode.getAttributes();
		final int attrLen = attrs.getLength();
		for (int i = 0; i < attrLen; ++i) {
		    final Node attrNode = attrs.item(i);
		    attrsMap.put(attrNode.getNodeName(), attrNode.getNodeValue());
		}

		final Constructor<?> ctor = clazz.getConstructor();
		final Meter meter = (Meter) ctor.newInstance();

		meter.configure(attrsMap);
		this.meters.add(meter);

	    } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException
		    | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
		    | SecurityException e) {
		e.printStackTrace();
	    }
	}

	EventStream.joinStream(JavaFxObservable.emitOnChanged(this.meters).map(e -> {
	    return new WeatherlyMetersLoadedEvent(Meters.this);
	}));
    }

    @Override
    public String toString() {
	String descr = "";
	for (final Meter meterObj : this.meters) {
	    descr += meterObj.toString() + "; ";
	}
	return "Meters { " + descr + "}";
    }
}
