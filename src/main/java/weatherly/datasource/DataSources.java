package weatherly.datasource;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rx.Observable;
import rx.javafx.sources.CompositeObservable;
import rx.observables.JavaFxObservable;
import weatherly.events.EventStream;
import weatherly.events.WeatherlyDataSourcesLoadedEvent;
import weatherly.events.WeatherlyEvent;

@XmlRootElement(name = "sources")
public class DataSources {

    @XmlAttribute(name = "interval")
    private final Integer mInterval = null;

    @XmlElement(name = "source", type = DataSource.class)
    private List<DataSource> mSources;

    private ObservableList<DataSource> sources;

    @SuppressWarnings("unchecked")
    public Observable<WeatherlyEvent> dataStream() {
	final CompositeObservable<WeatherlyEvent> local = new CompositeObservable<>();
	synchronized (local) {
	    for (final DataSource source : this.sources) {
		local.addAll((Observable<WeatherlyEvent>) source.dataStream(this.mInterval));
	    }
	}
	return local.toObservable();
    }

    public ObservableList<DataSource> getSourcesList() {
	return this.sources;
    }

    public void initSources() {
	this.sources = FXCollections.observableList(this.mSources);
	for (final DataSource source : this.sources) {
	    source.initSource();
	}

	EventStream.joinStream(JavaFxObservable.emitOnChanged(this.sources).map(e -> {
	    return new WeatherlyDataSourcesLoadedEvent(this);
	}));

    }

    @Override
    public String toString() {
	String descr = "";
	for (final DataSource source : this.mSources) {
	    descr += source + "; ";
	}
	return "DataSources { " + descr + "}";
    }
}
