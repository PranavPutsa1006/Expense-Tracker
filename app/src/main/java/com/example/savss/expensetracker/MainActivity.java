package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        IDType idType = IDType.Email;
//        String checkQuery = String.format("SELECT * FROM %s", "users");
//        final LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(this, null, null, 1);
//        String id = localDatabaseHelper.id;
//        if(!localDatabaseHelper.isLoggedIn()) {
//            Toast.makeText(getApplicationContext(), "Welcome, "+localDatabaseHelper.getName(id, idType), Toast.LENGTH_LONG).show();
//            Intent toDashboard = new Intent(this, HomeActivity.class);
//            UserData.userID = localDatabaseHelper.getUserID(id);
//            localDatabaseHelper.loggedin = true;
//            startActivity(toDashboard);
//        }else{
//            setContentView(R.layout.activity_main);
//        }
    }

    public void loginButton_onClick(View v){
        Intent toLoginPage = new Intent(this, LoginActivity.class);
        startActivity(toLoginPage);
    }

    @Override
    public void onBackPressed () {
        Intent toLoginPage = new Intent(this, LoginActivity.class);
        startActivity(toLoginPage);
    }

    public void signUpButton_onClick(View v){
        final LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(this, null, null, 1);
        if(!localDatabaseHelper.isExisting()) {
            Intent toSignUpPage = new Intent(this, SignUpActivity.class);
            startActivity(toSignUpPage);
        }else{
            Toast.makeText(getApplicationContext(), "Only one account possible", Toast.LENGTH_LONG).show();
        }
    }

}
