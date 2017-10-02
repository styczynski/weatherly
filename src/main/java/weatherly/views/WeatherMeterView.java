package weatherly.views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import weatherly.meters.Meter;

public class WeatherMeterView extends HBox {

    private static final String FXML_TEMPLATE = "/fxml/weather-meter-view.fxml";

    private final Meter meter;

    @FXML
    private VBox meterIconContainer;

    @FXML
    private Label meterText;

    public WeatherMeterView(final Meter meter) throws IOException {
	super();
	this.meter = meter;
	final javafx.fxml.FXMLLoader loader = new FXMLLoader(
		WeatherMeterView.class.getResource(WeatherMeterView.FXML_TEMPLATE));
	loader.setController(this);
	this.getChildren().add(loader.load());

	meter.getMeterIconProperty().addListener((icon, oldIcon, newIcon) -> {
	    WeatherMeterView.this.meterIconContainer.getChildren().setAll(newIcon);
	});
	meter.getMeterLabelProperty().addListener((label, oldLabel, newLabel) -> {
	    WeatherMeterView.this.meterText.setText(newLabel);
	});
	meter.setup();

    }

    public Meter getMeter() {
	return this.meter;
    }

}
