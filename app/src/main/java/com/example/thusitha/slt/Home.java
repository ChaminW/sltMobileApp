package com.example.thusitha.slt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

public class Home extends AppCompatActivity implements View.OnClickListener  {
    //Textview to show currently logged in user
    private TextView textView;
    private Button blogout;
    private Button breports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Initializing textview
        textView = (TextView) findViewById(R.id.textView);
        blogout = (Button) findViewById(R.id.blogout);
        breports = (Button) findViewById(R.id.breports);
        //Adding click listener
        blogout.setOnClickListener(this);
        breports.setOnClickListener(this);
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF,"Not Available");

        //Showing the current logged in email to textview
        textView.setText("Current User: " + username);
    }
    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.USERNAME_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void material(){
        Intent intent = new Intent(Home.this, Material.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.blogout:
                logout();
                break;
            case R.id.breports:
                material();
                break;
            default:
                break;
        }

    }

    public void btasks(View view) {

        Intent intent = new Intent(Home.this, customerDetail.class);
        startActivity(intent);


    }

    public void bnearby(View view) {
        Intent intent = new Intent(Home.this,PeerViewActivity .class);
        startActivity(intent);

    }
}
