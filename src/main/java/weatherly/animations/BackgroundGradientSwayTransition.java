package weatherly.animations;

import javafx.animation.Transition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class BackgroundGradientSwayTransition extends Transition {

    private final int milisDuration;
    private final boolean reversed;
    private final Stop[] stops;
    private final Region target;

    public BackgroundGradientSwayTransition(final Region target, final int milisDuration, final Stop[] stops) {
	this(target, milisDuration, stops, false);
    }

    public BackgroundGradientSwayTransition(final Region target, final int milisDuration, final Stop[] stops,
	    final boolean reversed) {
	this.setCycleDuration(Duration.millis(milisDuration));
	this.target = target;
	this.stops = stops;
	this.reversed = reversed;
	this.milisDuration = milisDuration;
    }

    @Override
    public void interpolate(double frac) {
	if (this.reversed) {
	    frac = 1.0 - frac;
	}
	final LinearGradient lg1 = new LinearGradient(0, 0, -1.0 + 1 / (0.999 - frac), 0, true, CycleMethod.NO_CYCLE,
		this.stops);
	this.target.setBackground(new Background(new BackgroundFill(lg1, null, null)));
    }

    public BackgroundGradientSwayTransition reversed() {
	return new BackgroundGradientSwayTransition(this.target, this.milisDuration, this.stops, true);
    }

}
