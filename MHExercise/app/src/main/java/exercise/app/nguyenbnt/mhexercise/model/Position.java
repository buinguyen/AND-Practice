package exercise.app.nguyenbnt.mhexercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    @JsonProperty("name")
    public String name;
    @JsonProperty("latitude")
    public double latitude;
    @JsonProperty("longitude")
    public double longitude;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("latitude")
    public double getLatitude() {
        return latitude;
    }

    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    public Position(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
