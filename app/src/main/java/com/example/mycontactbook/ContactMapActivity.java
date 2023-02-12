package com.example.mycontactbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        initGetLocation();
    }

    private void initGetLocation(){
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /*EditText editAddress = (EditText) findViewById(R.id.editAddress);
                EditText editCity = (EditText) findViewById(R.id.editCity);
                EditText editState = (EditText) findViewById(R.id.editState);
                EditText editZipcode = (EditText) findViewById(R.id.editZipcode);

                String address = editAddress.getText().toString() + ", " +
                        editCity.getText().toString() + ", " +
                        editState.getText().toString() + ", " +
                        editZipcode.getText().toString();
                //proper format for a call to the geocoding service - street address with elements of the
                //address seperated by commas

                List<Address> addresses = null;
                Geocoder geo = new Geocoder(ContactMapActivity.this);
                */
                //geo obj has all the info required to contact host service
                //addresses = geo.getFromLocationName(address, 1);
                //1- response
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

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onProviderDisabled(String provider) {
                        }
                        //required by by location listener + onLocationChanged
                    };
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                   /*LM is sent message requestLocationUpdates to begin listening for location changes
                   with non min time between updates and changes
                    */
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error, Location not available",
                            Toast.LENGTH_LONG).show();
                }
            }

    }
}


    @Override
    public void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
