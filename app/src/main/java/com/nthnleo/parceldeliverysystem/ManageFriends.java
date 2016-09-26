package com.nthnleo.parceldeliverysystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ManageFriends extends AppCompatActivity {
    MyDBHandler dbHandler;
    EditText inputName;
    Button addButton;
    TextView friendRequest;
    String dbString;
    String dbString2;
    Intent intent;
    String usernameExtra3;
    Button removeButton;
    TextView friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);
        inputName = (EditText) findViewById(R.id.inputName);
        addButton = (Button)findViewById(R.id.addButton);
        dbHandler = new MyDBHandler(this, null, null, 1);
        friendRequest = (TextView) findViewById(R.id.friendRequests);
        friendList = (TextView) findViewById(R.id.friendList);
        removeButton = (Button)findViewById(R.id.removeButton);

        intent = getIntent();
        usernameExtra3 = intent.getStringExtra("usernameExtra2");

        dbString = dbHandler.requestToString(usernameExtra3);
        friendRequest.setText(dbString);

        dbString2 = dbHandler.friendToString(usernameExtra3);
        friendList.setText(dbString2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_friends, menu);
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

    public void addOnClick(View view){
        if(dbHandler.registerCheck(inputName.getText().toString())){
            if(!dbHandler.alreadyRequested(usernameExtra3, inputName.getText().toString())) {


                dbHandler.addRequest(inputName.getText().toString(), usernameExtra3);
                addButton.setText("Friend Request Sent");

            }
        }
        else{
            addButton.setText("Invalid Username");
        }
        dbString = dbHandler.requestToString(usernameExtra3);
        friendRequest.setText(dbString);
        dbString2 = dbHandler.friendToString(usernameExtra3);
        friendList.setText(dbString2);
    }

    public void deleteOnClick(View view){
        if(!dbHandler.requestCheck(inputName.getText().toString())) {
            dbHandler.deleteRequest(usernameExtra3, inputName.getText().toString());
            removeButton.setText("Removed");
        }
        else{
            removeButton.setText("Invalid User");
        }
        dbHandler.deleteFriend(usernameExtra3, inputName.getText().toString());

        dbString = dbHandler.requestToString(usernameExtra3);
        friendRequest.setText(dbString);
        dbString2 = dbHandler.friendToString(usernameExtra3);
        friendList.setText(dbString2);
    }


}
