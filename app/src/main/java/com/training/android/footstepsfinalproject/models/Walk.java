package com.training.android.footstepsfinalproject.models;

import android.location.Location;

/**
 * Created by mwszedybyl on 5/3/15.
 */
public class Walk
{

    public Walk(){
        startingLocation = new Location("");
        endingLocation = new Location("");

    }
    private int id;
    private float distanceInMeters;
    private Location startingLocation;
    private Location endingLocation;
    private String temperatureFormatted;

    public float getDistanceInMeters()
    {
        return distanceInMeters;
    }

    public void setDistanceInMeters(float distanceInMeters)
    {
        this.distanceInMeters = distanceInMeters;
    }

    public Location getStartingLocation()
    {
        return startingLocation;
    }

    public void setStartingLocation(Location startingLocation)
    {
        this.startingLocation = startingLocation;
    }

    public Location getEndingLocation()
    {
        return endingLocation;
    }

    public void setEndingLocation(Location endingLocation)
    {
        this.endingLocation = endingLocation;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTemperatureFormatted()
    {
        return temperatureFormatted;
    }

    public void setTemperatureFormatted(String temperatureFormatted)
    {
        this.temperatureFormatted = temperatureFormatted;
    }
}
