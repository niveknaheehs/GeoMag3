package com.bignerdranch.android.geomag3;

import android.location.Location;


// This Class Implements the Geo Fence Logic
class GeoFence {

    private static int TRUE = 1;
    private static int FALSE = 0;

    // Define being inside the Geofence as being 3 meters from the Point Of Interest
    private static final int GEOFENCE_DIST = 3;

    private Location poi = new Location("myProvider");

    // Set the Point Of Interest location object
    public void setPOILocation(Double longditude,Double latidude ) {
        poi.setLongitude(longditude);
        poi.setLatitude(latidude);
    }

    // Calcualte the distance to the Point Of Interest
    public double  getDistance(Location location) {

        return location.distanceTo(poi);
    }

    // Determine whether we are inside the GeoFence
    public  int  insideGeoFence(Location location) {

        if (getDistance(location) >= GEOFENCE_DIST) {
            return FALSE;
        }
        else if (getDistance(location) < GEOFENCE_DIST) {
            return TRUE;
        }

        return -1;
    }

}

