package com.example.mycontactbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<String> contactData;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        //declares and codes behavior of the ViewHolder class that is owned by the adapter

        public TextView textViewContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textViewName);
        }

        public TextView getContactTextView(){
            return textViewContact;
        }
    }
    //constructor for the adapter - used to associate data to be displayed w/ adapter
    public ContactAdapter(ArrayList<String> arrayList){
        contactData = arrayList;
    }

    @NonNull
    @Override //required method- overrides superclass method - method called for each item
    //in the data to be displayed
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view,
                parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position));
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }


}
