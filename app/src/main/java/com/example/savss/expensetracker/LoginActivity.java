package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Toast toast;
    private FirebaseAuth firebaseAuth;
    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        firebaseAuth = FirebaseAuth.getInstance();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }


    public void loginValidation(View v) {
        EditText id = findViewById(R.id.emailAddress);
        EditText password = findViewById(R.id.password);

        if (id.getText().toString().isEmpty()){
            displayError(R.string.emptyIDError, id);
            return;
        }

        if (password.getText().toString().isEmpty()){
            displayError(R.string.emptyPasswordError, password);
            return;
        }
        else {

            firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Intent inn = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(inn);
                        finish();


                    } else {
                        Toast.makeText(LoginActivity.this, "No Record Has Found Please Signup ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }




        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(LoginActivity.this, null, null, 1);



        // TODO: Remove this if in final product
        if (id.getText().toString().equals("a") && password.getText().toString().equals("a")) {
            Intent toDashboard = new Intent(this, HomeActivity.class);
            UserData.userID = 1;
            startActivity(toDashboard);
        }

        IDType idType;

        if (id.getText().toString().contains("@")) {
            idType = IDType.Email;
        }
        else {
            idType = IDType.PhoneNumber;
        }

        if (password.getText().toString().equals(localDatabaseHelper.getPassword(id.getText().toString(), idType))) {
            Intent toDashboard = new Intent(this, HomeActivity.class);
            UserData.userID = localDatabaseHelper.getUserID(id.getText().toString(), idType);
            startActivity(toDashboard);
        }
        else {
            displayError(R.string.loginErrorMessage);
            Intent inn = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(inn);
            finish();
        }
    }

    private void displayError(int message) {
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }

    private void displayError(int message, View view) {
        Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        view.setAnimation(animShake);
        view.startAnimation(animShake);

        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }
}
