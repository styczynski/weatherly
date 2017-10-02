package weatherly.events;

public class WeatherlyWindowDragEvent extends WeatherlyEvent {

    private final double endX;

    private final double endY;

    private final double startX;

    private final double startY;

    private final WeatherlyWindowDragEventType type;

    public WeatherlyWindowDragEvent(final double startX, final double startY, final double endX, final double endY,
	    final WeatherlyWindowDragEventType type) {
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	this.type = type;
    }

    public double getDeltaX() {
	return this.endX - this.startX;
    }

    public double getDeltaY() {
	return this.endY - this.startY;
    }

    public double getEndX() {
	return this.endX;
    }

    public double getEndY() {
	return this.endY;
    }

    public double getStartX() {
	return this.startX;
    }

    public double getStartY() {
	return this.startY;
    }

    public WeatherlyWindowDragEventType getType() {
	return this.type;
    }

    /*
     * public void setEndX(final double endX) { this.endX = endX; }
     *
     * public void setEndY(final double endY) { this.endY = endY; }
     *
     * public void setStartX(final double startX) { this.startX = startX; }
     *
     * public void setStartY(final double startY) { this.startX = startY; }
     */

    @Override
    public String toString() {
	return "WeatherlyWindowDragHappens(from: {" + this.startX + ", " + this.startY + "} to: {" + this.endX + ", "
		+ this.endY + "})";
    }

}
