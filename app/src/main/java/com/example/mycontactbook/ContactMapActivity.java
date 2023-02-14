package com.example.mycontactbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.*;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsListener;
    LocationListener networkListener;
    Location currentBestLocation;

    final int PERMISSION_REQUEST_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        initGetLocationButton();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGetLocationButton () {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(ContactMapActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    ContactMapActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                                Snackbar.make(findViewById(R.id.activity_contact_map),
                                                "MyContactList requires this permission to locate " +
                                                        "your contacts", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ActivityCompat.requestPermissions(
                                                        ContactMapActivity.this,
                                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_LOCATION);
                                            }
                                        })
                                        .show();
                            } else {
                                ActivityCompat.requestPermissions(ContactMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                        }
                    } else {
                        startLocationUpdates();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting prmission",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void startLocationUpdates() {

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                /*getSystemService method is sent to the activity's context with a parameter that tells the
                context that you want the location service manager.
                getBaseContext is used to get the root context- this case activity
                 */
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLattitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                    txtLongitude.setText(String.valueOf(location.getLatitude()));
                    txtLatitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                    //when a location is detected it is reported to this method as a location obj
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
                //required by by location listener + onLocationChanged
            };networkListener = new LocationListener() {
                /*getSystemService method is sent to the activity's context with a parameter that tells the
                context that you want the location service manager.
                getBaseContext is used to get the root context- this case activity
                 */
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLattitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                    txtLongitude.setText(String.valueOf(location.getLatitude()));
                    txtLatitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                    //when a location is detected it is reported to this method as a location obj
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
                //required by by location listener + onLocationChanged
            };

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                   /*LM is sent message requestLocationUpdates to begin listening for location changes
                   with non min time between updates and changes
                    */
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
                   /*LM is sent message requestLocationUpdates to begin listening for location changes
                   with non min time between updates and changes
                    */
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    //request response sends request code - determines which permission request
    public void onRequestPermissionsResult (int requestCode,
                                            String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                //only one permission - so only one case
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                } else {
                    Toast.makeText(ContactMapActivity.this,
                            "MyContactList will not locate your contacts.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isBetterLocation(Location location){
        boolean isBetter = false;
        if (currentBestLocation == null){
            isBetter = true;
        }
        else if (location.getAccuracy() <= currentBestLocation.getAccuracy()){
            isBetter = true;
        }
        else if (location.getTime() - currentBestLocation.getTime() > 5*60*1000){
            isBetter = true;
        }
        return isBetter;
    }
}
