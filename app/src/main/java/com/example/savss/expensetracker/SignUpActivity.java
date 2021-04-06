package com.example.savss.expensetracker;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    Toast toast;
    FirebaseDatabase database;
    public String add = "";
    public String dateofb = "";
 private FirebaseAuth firebaseAuth;
 FirebaseFirestore firebaseFirestore;
 FirebaseUser firebaseUser;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

    }

    public void signUpButton_onClick(View view) {
        // TODO: OTP for Phone Number validation

        final EditText yourName = findViewById(R.id.yourName);
        final EditText emailAddress = findViewById(R.id.emailAddress);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);
        final EditText address = findViewById(R.id.address);
        final EditText dob = findViewById(R.id.date);
        final EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirmPassword);

        if (isSignUpDetailsValid(yourName, emailAddress, phoneNumber, address, dob, password, confirmPassword)) {
            final LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(this, null, null, 1);

            UserData.address = address.getText().toString();
            UserData.dateOfBirth = dob.getText().toString();
            boolean addUserResult = localDatabaseHelper.tryAddUser(yourName.getText().toString(), emailAddress.getText().toString(), phoneNumber.getText().toString(), password.getText().toString());

            if (addUserResult) {

                //DatabaseReference myRef = database.getReference("message");
                //String child=phoneNumber.toString();
                //myRef=database.getReference("Users").child(child);
                //myRef.child("Email").setValue(emailAddress);
                //myRef.child("Name").setValue(yourName);
                //myRef.child("Number").setValue(phoneNumber);
                //myRef.child("Password").setValue(password);
       firebaseAuth.createUserWithEmailAndPassword(emailAddress.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                        Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
//                        startActivity(intent);
//                        finish();
                        UserData.userID = localDatabaseHelper.getUserID(emailAddress.getText().toString());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("name",yourName.getText().toString().trim());
                        hashMap.put("email",emailAddress.getText().toString());
                        hashMap.put("phonenum",phoneNumber.getText().toString().trim());
                        hashMap.put("address",address.getText().toString());
                        hashMap.put("dob",dob.getText().toString());
                        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        String uid=firebaseUser.getUid();
                        firebaseFirestore.collection("users").document(phoneNumber.getText().toString()).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Intent intent=new Intent(SignUpActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                                else {
                                    Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }
                    else{
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(getApplicationContext(), "error" + " " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }


                }
            });

                Intent toDashboard = new Intent(this, HomeActivity.class);

//                toDashboard.putExtra(LocalDatabaseHelper.COLUMN_ID, localDatabaseHelper.getUserID(emailAddress.getText().toString()));
                startActivity(toDashboard);
                displayTosat(R.string.userSuccessfullyAdded);
            }
            else {
                displayTosat(R.string.userAlreadyExistError);
            }
        }

    }


    private boolean isSignUpDetailsValid(EditText yourName, EditText emailAddress, EditText phoneNumber, EditText address, EditText dob, EditText password, EditText confirmPassword) {
        if (yourName.getText().toString().isEmpty()){
            displayError(R.string.emptyYourNameError, yourName);
            return false;
        }

        if (!isNameValid(yourName.getText().toString())) {
            displayError("Enter a valid Name", yourName);
            return false;
        }

        if (emailAddress.getText().toString().isEmpty()){
            displayError(R.string.emptyEmailAddressError, emailAddress);
            return false;
        }

        if (!isEmailValid(emailAddress.getText().toString())) {
            displayError("Enter a valid Email Address", emailAddress);
            return false;
        }

        if (phoneNumber.getText().toString().isEmpty()){
            displayError(R.string.emptyPhoneNumberError, phoneNumber);
            return false;
        }

        if (!isPhoneValid(phoneNumber.getText().toString())) {
            displayError("Enter a valid Phone Number", phoneNumber);
            return false;
        }

        if (address.getText().toString().isEmpty()){
            displayError(R.string.emptyAddressError, phoneNumber);
            return false;
        }

        if (!isAddressValid(address.getText().toString())) {
            displayError("Enter a valid Address", address);
            return false;
        }

        if (dob.getText().toString().isEmpty()){
            displayError(R.string.emptyDobError, phoneNumber);
            return false;
        }

        if (!isDobValid(dob.getText().toString())) {
            displayError("Enter a valid Date of Birth", dob);
            return false;
        }

        if (!isPasswordValid(password.getText().toString())) {
            return false;
        }

        if (confirmPassword.getText().toString().isEmpty()){
            displayError(R.string.emptyConfirmPasswordError, confirmPassword);
            return false;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())){
            displayError(R.string.passwordNotMatchError, confirmPassword);
            return false;
        }

        return true;
    }

    private void displayTosat(int message) {
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

    private void displayError(String message, View view) {
        Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        view.setAnimation(animShake);
        view.startAnimation(animShake);

        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }
    private boolean isNameValid(String Name)
    {
        String nameRegex = "^[A-Za-z\\s]{3,30}$";
        Pattern pat = Pattern.compile(nameRegex);
        return pat.matcher(Name).matches();
    }
    private boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    private boolean isPhoneValid(String Phone)
    {
        Pattern pat = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = pat.matcher(Phone);
        return (m.find() && m.group().equals(Phone));
    }

    private boolean isAddressValid(String Name)
    {
        String nameRegex = "^[a-zA-z0-9/\\\\'(),\\-\\s]{2,255}";
        Pattern pat = Pattern.compile(nameRegex);
        return pat.matcher(Name).matches();
    }

    private boolean isDobValid(String Name)
    {
        String nameRegex = "^(?:0[1-9]|[12]\\d|3[01])([/.-])(?:0[1-9]|1[012])\\1(?:19|20)\\d\\d";
        Pattern pat = Pattern.compile(nameRegex);
        return pat.matcher(Name).matches();
    }



    private boolean isPasswordValid(String password)
    {
        EditText passwordEditText = findViewById(R.id.password);

        if (password.isEmpty()){
            displayError(R.string.emptyPasswordError, passwordEditText);
            return false;
        }

        int minLen = 8;
        int maxLen = 16;
        int numberCount = 0;
        int specialCount = 0;
        int upperCount = 0;
        int lowerCount = 0;

        // Count all types of characters
        for(int i = 0; i < password.length(); i++){
            char passwordChar = password.charAt(i);

            if(Character.isUpperCase(passwordChar)){
                upperCount++;
            }
            else if(Character.isLowerCase(passwordChar)){
                lowerCount++;
            }
            else if(Character.isDigit(passwordChar)){
                numberCount++;
            }
            else if((passwordChar >= 33 && passwordChar <= 46) || passwordChar == 64){
                specialCount++;
            }
            else {
                displayError(passwordChar + " character is not supported", passwordEditText);
                return false;
            }
        }

        String errorMessage = "";

        if (password.length() >= minLen && password.length() <= maxLen) {
            if(specialCount >= 1 && lowerCount >= 1 && upperCount >= 1 && numberCount >= 1) {
                return true;
            }
        }

        if (password.length() < minLen) {
            errorMessage += "Password must be at least " + minLen + " characters\n";
        }

        if (password.length() > maxLen) {
            errorMessage += "Password must be lesser than " + maxLen + " characters\n";
        }

        if (lowerCount == 0) {
            errorMessage += "You need at least one lower case character\n";
        }

        if (upperCount == 0) {
            errorMessage += "You need at least one upper case character\n";
        }

        if (numberCount == 0) {
            errorMessage += "You need at least one number\n";
        }

        if (specialCount == 0) {
            errorMessage += "You need at least one special character\n";
        }

        // Remove newlines from the end of the string
        errorMessage = errorMessage.trim();

        displayError(errorMessage, passwordEditText);

        return false;
    }
}
