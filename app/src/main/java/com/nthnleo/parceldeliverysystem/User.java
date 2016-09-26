package com.nthnleo.parceldeliverysystem;

import android.location.Address;

//Each row in the database can be represented by an object
//Columns will represent the objects properties
public class User {

    private int _id;
    private String _username;
    private String _password;
    private String _name;
    private int _location;

    public User(){
    }

    public User(String username, String password, Address address){
        this._username = username;
        this._password = password;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public int get_id() {
        return _id;
    }

    public String get_username() {
        return _username;
    }


    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public int get_Location() {
        return _location;
    }

    public void set_Location(int _location) {
        this._location = _location;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}
