package com.example.mycontactbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.location.*;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity implements
        OnMapReadyCallback {
    //OMRC- interface used to notify the activity that the map has been downloaded and is ready to be used

    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact currentContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
            getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this); //map is retrieved asyn - onMapReady()

        createLocationRequest(); //locationlistener
        createLocationCallback(); //process location changes

        initListButton();
        initMapButton();
        initSettingsButton();

        Bundle extras = getIntent().getExtras();
        try{
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            if(extras != null){
                currentContact = ds.getSpecificContact(extras.getInt("contactId"));
            }
            else{
                contacts = ds.getContacts("contactName", "ASC");
            }
            ds.close();
        }
        catch(Exception e) {
            Toast.makeText(this, "Contact(s) could not be retrieved.", Toast.LENGTH_LONG).show();
        }


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
    }



    private void createLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //create param of location request-sets standard monitoring interval,
        //fastest int needed, accuracy
    }
    //method responds to changes prov by FLPC- if location results
    // - loops through results to display as Toast
    private void createLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult (LocationResult locationResult){
                if (locationResult == null){
                    return;
                }
                for (Location location : locationResult.getLocations()){
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() +
                            "Long: " + location.getLongitude() +
                            "Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    //starts location updates - checks permissions
    private void startLocationUpdates(){
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                     PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null);
        gMap.setMyLocationEnabled(true);
    }
    //stops location updates from FLPC
    private void stopLocationUpdates(){
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    //receives google map obj and assigns it to gMap var - map type set
    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap= googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //options- MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN

        // put markers on the map
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;
        // to properly bound a group of points - app needs to know size of display

        if (contacts.size() > 0){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //used to construct geographic boundaries of a set of GPS coordinates
            for (int i = 0; i < contacts.size();i++){
                //adds contacts to map
                currentContact = contacts.get(i);
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", "+
                        currentContact.getCity() + ", "+
                        currentContact.getState() + ", " +
                        currentContact.getZipCode();

                try{
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                //LatLng obj instantiated - is point on map - is included in bounds
                LatLng point = new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude());
                builder.include(point);

                //marker is added to the app
                //add marker is a MarkerObject obj- set LatLng obj ad the position on the map for the marker
                gMap.addMarker(new MarkerOptions().position(point)
                        .title(currentContact.getContactName()).snippet(address));
            }
            //zoom into markers
            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    measuredWidth, measuredHeight,450)); // if padding too small - markers close to edge
        }
        else{
            if(currentContact !=null){
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", "+
                        currentContact.getCity() + ", "+
                        currentContact.getState() + ", " +
                        currentContact.getZipCode();

                try{
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                LatLng point = new LatLng(
                        addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                gMap.addMarker(new MarkerOptions().position(point).title(
                        currentContact.getContactName()).snippet(address));
                //zoom for single address
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,16));
            }

            else{
                AlertDialog alertDialog = new AlertDialog.Builder
                        (ContactMapActivity.this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data is available for the mapping function.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }});
                        alertDialog.show();
            }
        }
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


    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)  {
                Intent intent = new Intent(ContactMapActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setEnabled(false);
            }


    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ContactMapActivity.this, ContactActivitySettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
