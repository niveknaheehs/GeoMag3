package com.bignerdranch.android.geomag3;

import android.location.Location;

import com.indooratlas.android.sdk.IALocation;



class GeoFence {

    private static int TRUE = 1;
    private static int FALSE = 0;

    private static final int GEOFENCE_DIST = 50;

    private Location poi = new Location("myProvider");

    public void setPOILocation(Double longditude,Double latidude ) {
        poi.setLongitude(longditude);
        poi.setLatitude(latidude);
    }

    public double  getDistance(Location location) {

        return location.distanceTo(poi);
    }

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

