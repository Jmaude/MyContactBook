package com.example.mycontactbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ContactActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);
        initListButton();
        initMapButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();

    }
    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)  {
                Intent intent = new Intent(ContactActivitySettings.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)  {
                Intent intent = new Intent(ContactActivitySettings.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
        //disables button
    }

    private void initSettings(){
        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        /*string var declared - value for the field to sort contacts retrieved from SharedPref
        SharedPref method gets SharedPref obj - no need for multiple SP objects
        */
        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbCity = findViewById(R.id.radioCity);
        RadioButton rbBirthDay = findViewById(R.id.radioBirthday);
        if (sortBy.equalsIgnoreCase("contactname")){
            rbName.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("city")){
            rbCity.setChecked(true);
        }
        else {
            rbBirthDay.setChecked(true);
            //automatically sorts by birthday
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("ASC")){
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }
//Store the selected user preferences (one for each radio group)
    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton rbName = findViewById(R.id.radioName);
                    RadioButton rbCity = findViewById(R.id.radioCity);
                    if (rbName.isChecked()) {
                        getSharedPreferences("MyContactListPreferences" ,
                                Context.MODE_PRIVATE).edit()
                                .putString("sortfield" , "contactname").apply();
                    }
                    else if (rbCity.isChecked()){
                        getSharedPreferences("MyContactListPreferences" ,
                                Context.MODE_PRIVATE).edit()
                                .putString("sortfield", "city").apply();
                    }
                    else {
                        getSharedPreferences("MyContactPreferences" ,
                                Context.MODE_PRIVATE).edit()
                                .putString("sortfield", "birthday");
                    }
                }
        });
    }

    private void initSortOrderClick(){
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if(rbAscending.isChecked()){
                    getSharedPreferences("MyContactListPreferences" ,
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyContactListPreferences" ,
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }

            }
        });
    }
}