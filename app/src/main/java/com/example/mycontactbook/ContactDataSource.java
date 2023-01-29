package com.example.mycontactbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;
    //declared to hold instances of the SQLite database and helper class

    public ContactDataSource(Context context ){
        dbHelper = new ContactDBHelper(context);
    } // helper class instantiated when data source is instantiated


    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c){
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

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
            Long rowId = (long) c.getContactId();
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
            //max id retreived from record set- fields in record set indexed at 0
            cursor.close();
        }
        catch (Exception e){
            lastId = -1;
        }
        return lastId;
    }
}
