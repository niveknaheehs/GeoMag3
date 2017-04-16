package com.bignerdranch.android.geomag3;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;

import com.bignerdranch.android.geomag3.GeoFence;

public class MainActivity extends AppCompatActivity {


    private Firebase mFirebaseRef;
    private static final String TAG = "MainActivity";
    private final int CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 102;
    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 103;
    private static final int REQUEST_CODE_ACCESS_WIFI_STATE = 104;
    private static final int REQUEST_CODE_CHANGE_WIFI_STATE = 105;
    private static final int REQUEST_CODE_ACCESS_LOCATION_EXTRA_COMMANDS = 106;

    private static double lastDistance;
    private static final  double POILatitude = 51.5225261374409;
    private static final double  POILongditude = -0.13083979995587666;

    private IALocationManager mIALocationManager;
    private String status = "unknown";
    private String quality = "unknown";
    private static int TRUE = 1;
    private static int FALSE = 0;
    private int currentlyinsideGeoFence;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIALocationManager = IALocationManager.create(this);

        Firebase.setAndroidContext(this);
        mFirebaseRef =  new Firebase("https://geomag-fa9c7.firebaseio.com/Paths");

        String timeStampStr ;
        Map<String,Object> values = new HashMap<>();

        timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        values.put("timestamp", timeStampStr);
        values.put("latitutde", 0);
        values.put("longtitue",0);

        mFirebaseRef.push().setValue(values);


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
                android.Manifest.permission.INSTALL_LOCATION_PROVIDER,
                android.Manifest.permission.CONTROL_LOCATION_UPDATES
        };

        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );


//        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);


    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//        //Handle if any of the permissions are denied, in grantResults
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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

            String timeStampStr ;
            Map<String,Object> values = new HashMap<>();
            timeStampStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            values.put("timestamp", timeStampStr);
            values.put("latitutde", location.getLatitude());//location.getLatitude());// ;);
            values.put("longtitue",location.getLongitude());// location.getLatitude()); //location.getLongitude());

             TextView mTextViewLong = (TextView) findViewById(R.id.Long_label);//"@+id/longLab");
             String mStringLong = "Longditude: "+ String.valueOf(location.getLongitude());
             mTextViewLong.setText(mStringLong);


            TextView mTextViewLat = (TextView) findViewById(R.id.Lat_label);//"@+id/longLab");
            String mStringLat = "Latitude: " + String.valueOf(location.getLatitude());
            mTextViewLat.setText(mStringLat);


           GeoFence geoFence = new GeoFence();

            geoFence.setLocation(location.getLongitude(),location.getLatitude());

            if (geoFence.insideGeoFence(location.toLocation()) == TRUE && currentlyinsideGeoFence == FALSE) {
                Toast.makeText(MainActivity.this, "Entering point of interest", Toast.LENGTH_LONG).show();
            }
            else if (geoFence.insideGeoFence(location.toLocation()) == FALSE && currentlyinsideGeoFence == TRUE) {
                Toast.makeText(MainActivity.this, "Leaving point of interest", Toast.LENGTH_LONG).show();
            }
            currentlyinsideGeoFence = geoFence.insideGeoFence(location.toLocation());
        }

        @Override
        public void onStatusChanged(String provider, int stat, Bundle extras) {

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

            TextView mTextViewCal = (TextView) findViewById(R.id.Cal_label);//"@+id/longLab");
            mTextViewCal.setText(mStringCal);

            TextView mTextViewStat= (TextView) findViewById(R.id.Stat_label);//"@+id/longLab");
            mTextViewStat.setText(mStringStat);

        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mIALocationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }
    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }


}
