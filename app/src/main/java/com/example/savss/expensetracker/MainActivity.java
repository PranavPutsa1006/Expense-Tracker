package com.example.savss.expensetracker;

import android.content.Intent;
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
    }

    public void loginButton_onClick(View v){
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
