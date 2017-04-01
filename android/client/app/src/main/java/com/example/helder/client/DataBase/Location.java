package com.example.helder.client.DataBase;

/**
 * Created by Nelson on 31/03/2017.
 */

public class Location {
    private String Latitude;
    private String Longitude;


    public Location(){

    }

    public Location(String latitude, String longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
