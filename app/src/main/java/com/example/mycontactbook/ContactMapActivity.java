package com.example.mycontactbook;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {

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
                EditText editAddress = (EditText) findViewById(R.id.editAddress);
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
                //geo obj has all the info required to contact host service
                try{
                    addresses = geo.getFromLocationName(address, 1);
                    //1- response
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                TextView txtLatitude = (TextView) findViewById(R.id.textLattitude);
                TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);

                txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                txtLongitude.setText(String.valueOf(addresses.get(0).getLatitude()));
            }
        });
    }
}