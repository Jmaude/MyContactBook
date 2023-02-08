package com.example.mycontactbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnItemClickListener; //holds on click listener passed from activity
    private boolean isDeleting;
    //needed to open database so contact can be deleted
    private Context parentContext;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        //declares and codes behavior of the ViewHolder class that is owned by the adapter

        public TextView textViewContact;
        public TextView textPhone;
        public TextView textAddress;
        public Button deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            textAddress = itemView.findViewById(R.id.textShowAddress);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            //set tag to identify what was clicked
            itemView.setTag(this);
            //sets viewholder's OCL to the listener passed from activity
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getContactTextView(){
            return textViewContact;
        }

        //used by the adapter to return the textView to set and change displayed text
        public TextView getPhoneTextView() {
            return textPhone;
        }

        public TextView getTextAddress(){return textAddress;}

        public Button getDeleteButton() {
            return deleteButton;
        }

    }
    public ContactAdapter(ArrayList<Contact> arrayList, Context context){
        contactData = arrayList;
        parentContext = context;//modified so Context can be passed to it from Activity and stored
    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        //activity to the adapter
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override //required method- overrides superclass method - method called for each item
    //in the data to be displayed
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    //Called by recycler view to display the data at pos s
    //tells adapter to update data on each of our rows based on RV pos
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position){
        //final added to allow OnClick to access its value
        //change value holder
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getTextAddress().setText((contactData.get(position).getStreetAddress()) + ", " +
                (contactData.get(position).getCity()) + " , " +(contactData.get(position).getState()));
        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position); // replace with holder.getHolderPosition
                    }
                });
            }
            else {
                cvh.getDeleteButton().setVisibility(View.INVISIBLE);
            }

    }

    public void setDelete(boolean b){
        isDeleting = b;
    }

    private void deleteItem(int position){
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();

            if (didDelete) {
                contactData.remove(position);
                notifyDataSetChanged(); // refresh display
                Toast.makeText(parentContext, "Delete Success!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {return contactData.size();
    }

}

