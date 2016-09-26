package com.nthnleo.parceldeliverysystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SendParcel extends ActionBarActivity {
    String dbString2;
    TextView friendList;
    String usernameExtra3;
    Intent intent;
    MyDBHandler dbHandler;
    EditText receiver;
    TextView ParcelList;
    String dbString3;
    Button directdeliveryButton;
    Button indirectdeliveryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_parcel);
        friendList = (TextView)findViewById(R.id.friendList);
        dbHandler = new MyDBHandler(this, null, null, 1);
        intent = getIntent();
        usernameExtra3 = intent.getStringExtra("usernameExtra2");
        dbString2 = dbHandler.friendToString(usernameExtra3);
        friendList.setText(dbString2);
        receiver = (EditText)findViewById(R.id.sendparcelUsername);
        directdeliveryButton = (Button)findViewById(R.id.directdeliveryButton);
        indirectdeliveryButton = (Button)findViewById(R.id.indirectdeliveryButton);
        ParcelList = (TextView)findViewById(R.id.ParcelList);
        dbString3 = dbHandler.parcelToString(usernameExtra3);
        ParcelList.setText(dbString3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_parcel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDirectDeliveryClicked(View view) {
        if(dbHandler.registerCheck(receiver.getText().toString())){
            if(!dbHandler.parcelRequested(usernameExtra3, receiver.getText().toString())) {


                dbHandler.addParcelRequest(usernameExtra3, receiver.getText().toString());
                directdeliveryButton.setText("Parcel Request Sent");

            }
        }
        else{
            directdeliveryButton.setText("No Parcel Found");
            receiver.setText("");
        }
        dbString3 = dbHandler.parcelToString(usernameExtra3);
        ParcelList.setText(dbString3);
    }

    public void onindirectdeliveryButton(View view){
        if(dbHandler.parcelRequested(usernameExtra3, receiver.getText().toString())) {
            dbHandler.deleteParcel(usernameExtra3, receiver.getText().toString());
            indirectdeliveryButton.setText("Parcel Request Denied");
        }
        else{
            receiver.setText("");
        }
        dbString3 = dbHandler.parcelToString(usernameExtra3);
        ParcelList.setText(dbString3);
    }


}
