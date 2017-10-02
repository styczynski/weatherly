package weatherly.geoloc;

public class Location {

    private final double latitude;
    private final double longitude;

    public Location(final double longitude, final double latitude) {
	this.longitude = longitude;
	this.latitude = latitude;
    }

    public double getLatitude() {
	return this.latitude;
    }

    public double getLongitude() {
	return this.longitude;
    }

}
