package com.bignerdranch.android.geomag3;


import android.location.Location;

import com.indooratlas.android.sdk.IALocation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class GeoFenceTests {

    private static final  double POILatitude = 51.5225261374409;
    private static final double  POILongditude = -0.13083979995587666;

    private static final  double TestLatitudeInside = 51.5225261374409;
    private static final double  TestLongditudeInside = -0.13083979995587666;


    private static final  double TestLatitudeOutside = 50.5225261374409;
    private static final double  TestLongditudeOutside = -0.13083979995587666;

    @Test
    public void InsideGeoFence() throws Exception {

        GeoFence geoFence = new GeoFence();

        // Set Current Location
        Location location;
        location.setLatitude(TestLatitudeInside);
        location.setLongitude(TestLongditudeInside);

        // Set Point of interest
        geoFence.setLocation(POILongditude,POILatitude);

        // Test if we are inside the geofence
        int res = geoFence.insideGeoFence(location);

        // The  test passes if we are in the geofence
        assertEquals(res, 1);
    }

    @Test
    public void OutsideGeoFence() throws Exception {

        GeoFence geoFence = new GeoFence();

        // Set Current Location
        Location location;
        location.setLatitude(TestLatitudeOutside);
        location.setLongitude(TestLongditudeOutside);

        // Set Point of interest
        geoFence.setLocation(POILongditude,POILatitude);

        // Test if we are inside the geofence
        int res = geoFence.insideGeoFence(location);

        // The  test passes if we are not in the geofence
        assertEquals(res, 0);
    }
}