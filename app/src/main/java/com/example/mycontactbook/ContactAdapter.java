package com.example.mycontactbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private View.OnClickListener OnItemClickListener;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        //declares and codes behavior of the ViewHolder class that is owned by the adapter

        public TextView textViewContact;
        public TextView textPhone;
        public Button deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            itemView.setTag(this);
            itemView.setOnClickListener(OnItemClickListener);
        }

        //used by the adapter to return the textView to set and change displayed text
        public TextView getPhoneTextView() {
            return textPhone;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public TextView getContactTextView(){
            return textViewContact;
        }
    }
    //constructor for the adapter - used to associate data to be displayed w/ adapter
    public ContactAdapter(ArrayList<Contact> arrayList){
        contactData = arrayList;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        OnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override //required method- overrides superclass method - method called for each item
    //in the data to be displayed
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent, false);
        return new ContactViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());

    }

    @Override
    public int getItemCount() {return contactData.size();
        }

}

