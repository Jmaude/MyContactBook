package com.example.mycontactbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;
    //declared to hold instances of the SQLite database and helper class



    public ContactDataSource(Context context ){
        dbHelper = new ContactDBHelper(context);
    } // helper class instantiated when data source is instantiated


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //the database insets the ID bc the _id field was declared as an autoincrement field
    public boolean insertContact(Contact c){
        boolean didSucceed = false;
        try {
            database = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();
            //Values retrieved from contact obj and inserted into ContentValues obj
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            if (c.getPicture()!= null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                initialValues.put("contactphoto", photo);
            }

            didSucceed = database.insert("contact", null, initialValues) >0;
        }
        catch (Exception e){
            //Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }
    public boolean updateContact(Contact c ) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.getEMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            didSucceed = database.update("contact", updateValues, "_id=" +rowId, null) > 0;
        }
        catch (Exception e){
            //Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    //get new ID and set the currentContact ContactID attribute to val
    public int getLastContactID(){
        int lastId;
        try{
            String query = "Select MAX(_id) from contact";
            /*SQL query written to get max val for _id field in contact table -
            _id - auto increment
            * */
            Cursor cursor = database.rawQuery(query, null);
            //cursor is declared and assigned to hold results of exception of query
            //cursor- obj used to hold and move results of query

            cursor.moveToFirst();
            //move to first record in data
            lastId = cursor.getInt(0);
            //max id retrieved from record set- fields in record set indexed at 0
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getContactName(){
        ArrayList<String> contactNames = new ArrayList<>();
        try{
            String query = "Select contactname from contact";
            //SQL query is written to return contactname field for all records in the contact table
            Cursor cursor = database.rawQuery(query,null);
            //obj holds results of query

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            //loop to traverse contacts - initialized by moving to the first record
            //contact name added to ArrayList
            cursor.close();
        }
        catch (Exception e){
            contactNames = new ArrayList<>();
        }
        return contactNames;
    }


    public void close() {
        dbHelper.close();
    }


    public ArrayList<Contact> getContacts(String sortField, String sortOrder){
        ArrayList<Contact> contacts = new ArrayList<>();
        try{
            String query = "SELECT * FROM contact ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query,null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                newContact = new Contact(); // new contact obj instantiated for each record in the cursor
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.setEMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

    public Contact getSpecificContact(int contactId){
        Contact contact = new Contact();
        String query = "SELECT * FROM contact WHERE _id=" + contactId;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.setEMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(calendar);

            byte[] photo = cursor.getBlob(10);
            if (photo != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap thePicture = BitmapFactory.decodeStream(imageStream);
                contact.setPicture(thePicture);
            }

            cursor.close();
        }
        return contact;
    }
    //Delete contact from database
    public boolean deleteContact (int contactId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("contact", "_id=" + contactId,
                    null) > 0;
        } catch (Exception e) {
            // Do nothing -return value already set to false
        }

        return didDelete;
    }
}
