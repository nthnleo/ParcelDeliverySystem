package com.nthnleo.parceldeliverysystem;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.io.IOException;
import java.lang.String;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener{

    protected EditText usernameField;
    protected EditText passwordField;
    protected EditText locationField;
    protected EditText nameField;
    protected Button registerText;
    protected TextView alansText;
    protected Button loginText;
    protected String usernameExtra;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected MyDBHandler dbHandler;
    protected double lng, lat;
    protected List<Address> addresses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameField = (EditText) findViewById(R.id.usernameField);
        usernameExtra = usernameField.getText().toString();
        passwordField = (EditText) findViewById(R.id.passwordField);
        loginText = (Button) findViewById(R.id.loginButton);
        registerText = (Button) findViewById(R.id.registerButton);
        alansText = (TextView) findViewById(R.id.alansText);

        dbHandler = new MyDBHandler(this, null, null, 1);
//        printDatabase();
        getLocation();
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        alansText.setText("Current Location: " + addresses.get(0).getThoroughfare() + ", " + addresses.get(0).getLocality());
        //sets location text, need to comment out if using emulator
    }

    //Add a  to the database
    public void addButtonClicked(View view){
        if(!dbHandler.registerCheck(usernameField.getText().toString())) {
            User user = new User(usernameField.getText().toString(), passwordField.getText().toString(), addresses.get(0));
            dbHandler.addUser(user);
    //printDatabase();
        }
        else{
            registerText.setText("Username Taken");
        }
    }

    //login items
    public void loginButtonClicked(View view) {
        if (dbHandler.loginCheck(usernameField.getText().toString(), passwordField.getText().toString())) {
            Intent i = new Intent(MainActivity.this, MainMenu.class);
            i.putExtra("usernameExtra", usernameField.getText().toString());
            startActivity(i);
        }
        else{
            loginText.setText("Login Failed");
        }
    }

    //Print the database
    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        alansText.setText(dbString);
        usernameField.setText("");
        passwordField.setText("");
        nameField.setText("");
        locationField.setText("");
    }

    public void getLocation() {
        String provider;
        Criteria c = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(c, false);
        Location currLocation = locationManager.getLastKnownLocation(provider);
        if(currLocation != null) {
            lat = currLocation.getLatitude();
            lng = currLocation.getLongitude();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}