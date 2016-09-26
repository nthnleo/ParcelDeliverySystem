package com.nthnleo.parceldeliverysystem;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import java.lang.Override;import java.lang.String;import java.util.Random;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_userTable = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_friendTable = "friends";
    public static final String COLUMN_TRUSTLEVEL = "trustlevel";
    public static final String COLUMN_FRIENDS = "friendsUser";
    public static final String COLUMN_USER = "usernames";

    public static final String TABLE_requestTable = "requests";

    public static final String TABLE_parcelTable = "parcelTable";
    public static final String COLUMN_SENDER = "sender";
    public static final String COLUMN_RECEIVER = "receiver";
    public static final String COLUMN_ZIP = "zip";


    //We need to pass database information along to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_userTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT," + COLUMN_PASSWORD + " TEXT" + ")";

        String query2 = "CREATE TABLE " + TABLE_friendTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT," + COLUMN_FRIENDS + " TEXT," + COLUMN_TRUSTLEVEL + " INTEGER"
                + ")";

        String query3 = "CREATE TABLE " + TABLE_requestTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER + " TEXT, " + COLUMN_FRIENDS + " TEXT)";

        String query4 = "CREATE TABLE " + TABLE_parcelTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SENDER + " TEXT," + COLUMN_RECEIVER + " TEXT," + COLUMN_ZIP + " INTEGER" + ")";


        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_userTable);
        onCreate(db);
    }

    public void addRequest(String friend, String username){
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIENDS, friend);
        values.put(COLUMN_USER, username);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_requestTable, null, values);
        db.close();
    }

    //Add a new row to the database
    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.get_username());
        values.put(COLUMN_PASSWORD, user.get_password());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_userTable, null, values);
        db.close();
    }

    //Delete a friend request from the database
    public void deleteRequest(String userName, String friendName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_requestTable + " WHERE " + COLUMN_FRIENDS + "='" + userName + "' AND " + COLUMN_USER + "='" + friendName + "';");
    }

    public void deleteParcel(String userName, String friendName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_friendTable + " SET " + COLUMN_TRUSTLEVEL + "=" + COLUMN_TRUSTLEVEL + "+1 WHERE " + COLUMN_USERNAME + "='" + userName + "' AND " + COLUMN_FRIENDS + "='" + friendName + "';");
        db.execSQL("UPDATE " + TABLE_friendTable + " SET " + COLUMN_TRUSTLEVEL + "=" + COLUMN_TRUSTLEVEL + "+1 WHERE " + COLUMN_USERNAME + "='" + friendName + "' AND " + COLUMN_FRIENDS + "='" + userName + "';");
        db.execSQL("DELETE FROM " + TABLE_parcelTable + " WHERE " + COLUMN_RECEIVER + "='" + userName + "' AND " + COLUMN_SENDER + "='" + friendName + "';");
    }

    public void deleteFriend(String userName, String friendName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_friendTable + " WHERE " + COLUMN_FRIENDS + "='" + friendName + "' AND " + COLUMN_USERNAME + "='" + userName + "';");
    }

    public boolean loginCheck(String username, String password){
        SQLiteDatabase db = getWritableDatabase();
        String i = "SELECT * FROM " + TABLE_userTable + " WHERE " + COLUMN_USERNAME + " = " + '"' + username + '"' + " AND " + COLUMN_PASSWORD + " = '" + password + "' ;";
        Cursor c = db.rawQuery(i, null);
        if(c.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean alreadyRequested(String yourUsername, String theirUsername) {
        SQLiteDatabase db = getWritableDatabase();
        String i = "SELECT * FROM " + TABLE_requestTable + " WHERE " + COLUMN_USER + " = '" + theirUsername + "' AND " + COLUMN_FRIENDS + " = '" + yourUsername + "';";
        Cursor c = db.rawQuery(i, null);
        if(c.getCount() > 0){
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, theirUsername);
            values.put(COLUMN_FRIENDS, yourUsername);
            values.put(COLUMN_TRUSTLEVEL, 0);
            db.insert(TABLE_friendTable, null, values);

            deleteRequest(yourUsername, theirUsername);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean parcelRequested(String yourUsername, String theirUsername) {
        SQLiteDatabase db = getWritableDatabase();
        String i = "SELECT * FROM " + TABLE_parcelTable + " WHERE " + COLUMN_SENDER + " = '" + theirUsername + "' AND " + COLUMN_RECEIVER + " = '" + yourUsername + "';";
        Cursor c = db.rawQuery(i, null);
        if(c.getCount() > 0){
            ContentValues values = new ContentValues();
            values.put(COLUMN_SENDER, theirUsername);
            values.put(COLUMN_RECEIVER, yourUsername);
            db.insert(TABLE_parcelTable, null, values);

            deleteParcel(yourUsername, theirUsername);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }



    public boolean registerCheck(String username){
        SQLiteDatabase db = getWritableDatabase();
        String i = "SELECT * FROM " + TABLE_userTable + " WHERE " + COLUMN_USERNAME + " = " + "'" + username +  "';";
        Cursor c = db.rawQuery(i, null);
        if(c.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean requestCheck(String username){
        SQLiteDatabase db = getWritableDatabase();
        String i = "SELECT * FROM " + TABLE_requestTable + " WHERE " + COLUMN_FRIENDS + " = " + "'" + username +  "';";
        Cursor c = db.rawQuery(i, null);
        if(c.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_userTable + " WHERE 1;";

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("username")) != null) {
                dbString += c.getString(c.getColumnIndex("username"));

                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;

    }
    public String requestToString(String user){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_requestTable + " WHERE " + COLUMN_FRIENDS + " = '" + user + "';" ;

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("usernames")) != null) {
                dbString += c.getString(c.getColumnIndex("usernames"));

                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;

    }

    public String friendToString(String user){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_friendTable + " WHERE " + COLUMN_USERNAME + " = '" + user + "';" ;

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("friendsUser")) != null) {
                dbString += c.getString(c.getColumnIndex("friendsUser"));
                dbString += " (Trust Level: ";
                dbString += c.getString(c.getColumnIndex(COLUMN_TRUSTLEVEL));
                dbString += ")\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public void addParcelRequest(String sender, String receiver) {
        Random random = new Random();
        int zip = random.nextInt(9999) + 90000;
        ContentValues values = new ContentValues();
        values.put(COLUMN_SENDER, sender);
        values.put(COLUMN_RECEIVER, receiver);
        values.put(COLUMN_ZIP, zip);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_parcelTable, null, values);
        db.close();
    }

    public String parcelToString(String user){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_parcelTable + " WHERE " + COLUMN_RECEIVER + " = '" + user + "';" ;

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sender")) != null) {
                dbString += "Name: ";
                dbString += c.getString(c.getColumnIndex("sender"));
                dbString += ", ";
                dbString += "Zip: ";
                dbString += c.getString(c.getColumnIndex("zip"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public void updateLocation(String user, double lon, double lat) {

    }

}