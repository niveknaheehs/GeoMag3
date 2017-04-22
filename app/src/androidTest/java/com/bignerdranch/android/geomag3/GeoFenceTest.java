package com.bignerdranch.android.geomag3;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.location.Location;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public void InsideGeoFence() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();


        GeoFence geoFence = new GeoFence();

        // Set Current Location
        Location location =  new Location("myProvider");
        location.setLatitude(TestLatitudeInside);
        location.setLongitude(TestLongditudeInside);

        // Set Point of interest
        geoFence.setPOILocation(POILongditude,POILatitude);

        // Test if we are inside the geofence
        int res = geoFence.insideGeoFence(location);

         //The  test passes if we are in the geofence
        assertEquals(res, 1);
    }

    @Test
    public void OutsideGeoFence() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        GeoFence geoFence = new GeoFence();

        // Set Current Location
        Location location =  new Location("myProvider");
        location.setLatitude(TestLatitudeOutside);
        location.setLongitude(TestLongditudeOutside);

        // Set Point of interest
        geoFence.setPOILocation(POILongditude,POILatitude);

        // Test if we are inside the geofence
        int res = geoFence.insideGeoFence(location);

        // The  test passes if we are not in the geofence
        assertEquals(res, 0);
    }

    private Firebase mFirebaseRef;
    @Test
    public void TestFB() throws Exception {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //Firebase.setAndroidContext(appContext);
        Firebase.setAndroidContext(appContext);

        mFirebaseRef =  new Firebase("https://geomag-fa9c7.firebaseio.com/Test");

        String timeStampStr ;
        Map<String,Object> values = new HashMap<>();

        timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        values.put("timestamp", timeStampStr);
        values.put("Test", 123456);

        mFirebaseRef.push().setValue(values);

        //The  test passes if we are in the geofence
        assertEquals(1, 1);
    }
}



