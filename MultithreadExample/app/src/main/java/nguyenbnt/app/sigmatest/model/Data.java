package nguyenbnt.app.sigmatest.model;

/**
 * This class is to save battery and location information
 */
public class Data {
    private int battery;
    private double latitude;
    private double longitude;

    /**
     * Constructor
     */
    public Data(){}

    /**
     * Constructor
     * @param battery
     * @param latitude
     * @param longitude
     */
    public Data(int battery, double latitude, double longitude) {
        this.battery = battery;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
