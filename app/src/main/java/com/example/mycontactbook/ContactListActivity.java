package com.example.mycontactbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    RecyclerView contactList;
    ArrayList<Contact> contacts; //holds contacts
    ContactAdapter contactAdapter;


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick (View view){
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactId = contacts.get(position).getContactId();
            Intent intent = new Intent (ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactID", contactId);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initListButton();
        initMapButton();
        initSettingsButton();




        ContactDataSource ds = new ContactDataSource(this);
       ArrayList<Contact> contacts;

        try {
            ds.open();
            contacts = ds.getContacts();
            ds.close();

            contactList = findViewById(R.id.rvContact);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            contactList.setLayoutManager(layoutManager);

            ContactAdapter contactAdapter = new ContactAdapter(contacts);

            contactAdapter.setOnItemClickListener(onItemClickListener);

            contactList.setAdapter(contactAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);

    }

    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)  {
                Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactActivitySettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
