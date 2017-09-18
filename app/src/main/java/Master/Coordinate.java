package Master;

/**
 * Created by Hendry on 9/16/2017.
 */

public class Coordinate {
    private double latitude, longitude;

    public Coordinate(double latitude, double longitude)
    {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
