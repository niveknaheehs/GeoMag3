package com.bignerdranch.android.geomag3;


import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

public class MainActivity extends AppCompatActivity {


    private Firebase mFirebaseRef;
    private static final String TAG = "MainActivity";
    private final int CODE_PERMISSIONS = 100;

    private static double lastDistance;

    // Point of interest Longditude and latitude
    private static final  double POILatitude = 51.5225261374409;
    private static final double  POILongitude = -0.13083979995587666;

    private IALocationManager mIALocationManager;
    private String status = "unknown";
    private String quality = "unknown";
    private static int TRUE = 1;
    private static int FALSE = 0;
    private int currentlyinsideGeoFence;


    public void RecordLocation(IALocation location) {


        // Setup a timestamp
        String timeStampStr ;
        Map<String,Object> values = new HashMap<>();
        timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        // Write timestamp/latitude/longitude to firebase
        values.put("timestamp", timeStampStr);
        values.put("latitutde", location.getLatitude());
        values.put("longtitue",location.getLongitude());

        mFirebaseRef.push().setValue(values);


        // Change the Longitude on the display
        TextView mTextViewLong = (TextView) findViewById(R.id.Long_label);//"@+id/longLab");
        String mStringLong = "Longditude: "+ String.valueOf(location.getLongitude());
        mTextViewLong.setText(mStringLong);

        // Change the Latitude on the display
        TextView mTextViewLat = (TextView) findViewById(R.id.Lat_label);//"@+id/longLab");
        String mStringLat = "Latitude: " + String.valueOf(location.getLatitude());
        mTextViewLat.setText(mStringLat);


        // Determine the distance to the Geofence and determine if Inside or outside of the Geofence

        GeoFence geoFence = new GeoFence();

        geoFence.setPOILocation(POILongitude,POILatitude);


        // Display the Distance to the Point Of interest
        TextView mTextViewDist = (TextView) findViewById(R.id.Dist_label);
        String mStringDist = "Distance: " + geoFence.getDistance(location.toLocation());
        mTextViewDist.setText(mStringDist);


        // Make toast when leaving or entering the GeoFence
        if (geoFence.insideGeoFence(location.toLocation()) == TRUE && currentlyinsideGeoFence == FALSE) {
            Toast.makeText(MainActivity.this, "Entering point of interest", Toast.LENGTH_LONG).show();
        }
        else if (geoFence.insideGeoFence(location.toLocation()) == FALSE && currentlyinsideGeoFence == TRUE) {
            Toast.makeText(MainActivity.this, "Leaving point of interest", Toast.LENGTH_LONG).show();
        }

        // Store the Geofence in/out status for the next location change
        currentlyinsideGeoFence = geoFence.insideGeoFence(location.toLocation());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Location manager Object to access Indoor Atlas Services
        mIALocationManager = IALocationManager.create(this);

        // Create the firebase Object to access firebase
        Firebase.setAndroidContext(this);
        mFirebaseRef =  new Firebase("https://geomag-fa9c7.firebaseio.com/Paths");


        // Ensure that the required permissions are in place
        String[] neededPermissions = {
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.INSTALL_LOCATION_PROVIDER
        };

        // Request the required permissions
        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Process the permission request results & show toasts for the outcomes
        for(int i=0; i< grantResults.length; i++){
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, permissions[i] +  " succeeded", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(MainActivity.this, permissions[i] +  " denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    private IALocationListener mIALocationListener = new IALocationListener() {

        // Called when the location has changed.
        @Override
        public void onLocationChanged(IALocation location) {

            RecordLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int stat, Bundle extras) {
            // Display the Calibration and Status Changes
            switch (stat) {
                case IALocationManager.STATUS_CALIBRATION_CHANGED:

                    switch (extras.getInt("quality")) {
                        case IALocationManager.CALIBRATION_POOR:
                            quality = "Poor";
                            break;
                        case IALocationManager.CALIBRATION_GOOD:
                            quality = "Good";
                            break;
                        case IALocationManager.CALIBRATION_EXCELLENT:
                            quality = "Excellent";
                            break;
                    }
                    String mStringCal = "Calibration: " + quality;
                    break;

                case IALocationManager.STATUS_AVAILABLE:
                    status = "Available";
                    break;
                case IALocationManager.STATUS_LIMITED:
                    status =  "Limited";
                    break;
                case IALocationManager.STATUS_OUT_OF_SERVICE:
                    status =  "Out of service";
                    break;
                case IALocationManager.STATUS_TEMPORARILY_UNAVAILABLE:
                    status =  "Temporarily unavailable";
            }

            String mStringCal = "Calibration: " + quality;
            String mStringStat = "Status: " + status;

            TextView mTextViewCal = (TextView) findViewById(R.id.Cal_label);
            mTextViewCal.setText(mStringCal);

            TextView mTextViewStat= (TextView) findViewById(R.id.Stat_label);
            mTextViewStat.setText(mStringStat);

        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        // Initiate the request for Location Update notifications
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop Location Update notifications
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }
    @Override
    protected void onDestroy() {
        // Destroy the Location Manager Object - housekeeping
        mIALocationManager.destroy();
        super.onDestroy();
    }


}
