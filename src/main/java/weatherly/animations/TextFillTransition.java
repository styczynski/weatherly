package weatherly.animations;

import javafx.animation.Transition;
import javafx.scene.control.Labeled;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class TextFillTransition extends Transition {

    private final int milisDuration;
    private final boolean reversed;
    private final Stop[] stops;
    private final Labeled target;

    public TextFillTransition(final Labeled target, final int milisDuration, final Stop[] stops) {
	this(target, milisDuration, stops, false);
    }

    public TextFillTransition(final Labeled target, final int milisDuration, final Stop[] stops,
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
	final LinearGradient lg1 = new LinearGradient(0, 0, 0, -1.0 + 1 / (0.999 - frac), true, CycleMethod.NO_CYCLE,
		this.stops);
	this.target.setTextFill(lg1);
    }

    public TextFillTransition reversed() {
	return new TextFillTransition(this.target, this.milisDuration, this.stops, true);
    }

}
