package com.example.ahmed.gp_posts;

public class LocationsFirebase {
    private String Name ;
    private double lng ,lat;

    public LocationsFirebase(String name, double lng, double lat) {
        Name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public LocationsFirebase() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
