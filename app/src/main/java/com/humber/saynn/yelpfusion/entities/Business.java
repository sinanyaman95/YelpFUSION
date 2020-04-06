package com.humber.saynn.yelpfusion.entities;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Business {
    Bitmap image;
    String name;
    String description;
    String location;
    double rating;
    String url;
    long views;

    public Business(){
        new Business(null,"","","",0,"",0);
    }

    public Business(Bitmap image, String name, String description, String location, double rating, String url, long views) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.location = location;
        this.rating = rating;
        this.url = url;
        this.views = views;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
