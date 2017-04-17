package com.bignerdranch.android.geomag3;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.location.Location;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GeoFenceTest {

    private static final  double POILatitude = 51.5225261374409;
    private static final double  POILongditude = -0.13083979995587666;

    private static final  double TestLatitudeInside = 51.5225261374409;
    private static final double  TestLongditudeInside = -0.13083979995587666;


    private static final  double TestLatitudeOutside = 50.5225261374409;
    private static final double  TestLongditudeOutside = -0.13083979995587666;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.bignerdranch.android.geomag3", appContext.getPackageName());
    }

    @Test
    public void InsideGeoFence() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();


        GeoFence geoFence = new GeoFence();

        // Set Current Location
        Location location =  new Location("myProvider");
        location.setLatitude(TestLatitudeInside);
        location.setLongitude(TestLongditudeInside);

        // Set Point of interest
        geoFence.setLocation(POILongditude,POILatitude);

        // Test if we are inside the geofence
        int res = geoFence.insideGeoFence(location);

         //The  test passes if we are in the geofence
        assertEquals(res, 1);
    }
}



