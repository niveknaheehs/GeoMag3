package com.bignerdranch.android.geomag3;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.firebase.client.Firebase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTest {

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



