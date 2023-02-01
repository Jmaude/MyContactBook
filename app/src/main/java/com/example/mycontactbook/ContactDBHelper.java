package com.example.mycontactbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*subclass of SQLLiteOpenHelper
All databases need two methods a helper (create, modify, Delete
and an opener tables in the database) and a the other is used for data access.
The data access subClass provides methods to open and close database and the
queries used to store, access and manipulate the data in the table
 */


public class ContactDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;
      /*Everytime the database is accessed it looks at this version if it is
    if the number is higher the onUpgrade method is called. The developer
    only needs to update this version number
    * */

    private static final String CREATE_TABLE_CONTACT =
            /*Creates the table. BBP define the table definitions like above so when
    changes to the table are needed all you do is change the definition in one
    place and increment the version number
    */
            "create table contact (_id integer primary key AUTOINCREMENT,"
            + "contactname text not null, streetaddress text,"
            + "city text, state text, zipcode text,"
            + "phonenumber text, cellnumber text,"
            +"email text, birthday text);";

    public ContactDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /*
  Oncreate is called the first time the db is opened. If the db named
  in the DATABASE_Name var does not exist this method is executed which
  executes the SQL assigned to the CREATE_TABLE_CONTACT variable.
   */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDBHelper.class.getName(),
                "Upgrading database from version" + oldVersion + " to "
                   + newVersion + ", which will destroy all the old data");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
