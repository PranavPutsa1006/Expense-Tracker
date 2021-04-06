package com.example.savss.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.Button;
public class ProfileManagement extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext, btnPrev, btn;
    private TextView name;
    private TextView email;
    private TextView address;
    private TextView phone;
    private TextView dob;
    private TextView password;
    private TextView confirmPassword;
    Toast toast;
    View viewapp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewapp = inflater.inflate(R.layout.fragment_profile_management, container, false);
        toast = Toast.makeText(viewapp.getContext(), "", Toast.LENGTH_SHORT);
        btnNext = (Button) viewapp.findViewById(R.id.changeProfile);
        btnPrev = (Button) viewapp.findViewById(R.id.updateProfile);
        btn = (Button) viewapp.findViewById(R.id.cancel);
        viewSwitcher = (ViewSwitcher) viewapp.findViewById(R.id.profileViewSwitcher);
        btnNext.setOnClickListener(setViewSwitcherNext);
        btnPrev.setOnClickListener(setViewSwitcherPrev);
        btn.setOnClickListener(setViewSwitcherPrev1);
        setName();
        setEmail();
        setAddress();
        setPhoneNumber();
        setDOB();
        setPassword();
        return viewapp;
    }

    private View.OnClickListener setViewSwitcherNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewSwitcher.setDisplayedChild(1);
        }
    };
    private View.OnClickListener setViewSwitcherPrev = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getActivity(), null, null, 1);
            isPasswordValid(password.getText().toString());
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                displayError(R.string.passwordNotMatchError, confirmPassword);
            } else {
                localDatabaseHelper.updateUserData(UserData.userID, name.getText().toString(), email.getText().toString(), address.getText().toString(), dob.getText().toString(), phone.getText().toString(), password.getText().toString());
                localDatabaseHelper.initializeUserData(UserData.userID);
                viewSwitcher.setDisplayedChild(0);
                setName();
                setEmail();
                setAddress();
                setPhoneNumber();
                setDOB();
                setPassword();
            }
        }
    };
    private View.OnClickListener setViewSwitcherPrev1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewSwitcher.setDisplayedChild(0);
        }
    };

    private void setName() {
        name = viewSwitcher.findViewById(R.id.textView6);
        name.setText(UserData.Name);
        name = viewSwitcher.findViewById(R.id.yourName);
        name.setText(UserData.Name);
    }

    private void setEmail() {
        email = viewSwitcher.findViewById(R.id.textView7);
        email.setText(UserData.email);
        email = viewSwitcher.findViewById(R.id.emailAddress);
        email.setText(UserData.email);
    }

    private void setAddress() {
        address = viewSwitcher.findViewById(R.id.textView8);
        address.setText(UserData.address);
        address = viewSwitcher.findViewById(R.id.address);
        address.setText(UserData.address);
    }

    private void setPhoneNumber() {
        phone = viewSwitcher.findViewById(R.id.textView3);
        phone.setText(UserData.phoneNumber);
        phone = viewSwitcher.findViewById(R.id.phoneNumber);
        phone.setText(UserData.phoneNumber);
    }

    private void setDOB() {
        dob = viewSwitcher.findViewById(R.id.textView10);
        dob.setText(UserData.dateOfBirth);
    }

    private void setPassword() {
        password = viewSwitcher.findViewById(R.id.password);
        password.setText(UserData.password);
        confirmPassword = viewSwitcher.findViewById(R.id.confirmPassword);
        confirmPassword.setText(UserData.password);
    }

    private void displayTosat(int message) {
        Vibrator vib = (Vibrator) viewapp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);
        toast.setText(message);
        toast.show();
    }

    private void displayError(int message, View view) {
        Animation animShake = AnimationUtils.loadAnimation(viewapp.getContext().getApplicationContext(), R.anim.shake);
        view.setAnimation(animShake);
        view.startAnimation(animShake);

        Vibrator vib = (Vibrator) viewapp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }

    private void displayError(String message, View view) {
        Animation animShake = AnimationUtils.loadAnimation(viewapp.getContext().getApplicationContext(), R.anim.shake);
        view.setAnimation(animShake);
        view.startAnimation(animShake);

        Vibrator vib = (Vibrator) viewapp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }

    private boolean isPasswordValid(String password) {
        EditText passwordEditText = viewapp.findViewById(R.id.password);

        if (password.isEmpty()) {
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
        for (int i = 0; i < password.length(); i++) {
            char passwordChar = password.charAt(i);

            if (Character.isUpperCase(passwordChar)) {
                upperCount++;
            } else if (Character.isLowerCase(passwordChar)) {
                lowerCount++;
            } else if (Character.isDigit(passwordChar)) {
                numberCount++;
            } else if ((passwordChar >= 33 && passwordChar <= 46) || passwordChar == 64) {
                specialCount++;
            } else {
                displayError(passwordChar + " character is not supported", passwordEditText);
                return false;
            }
        }

        String errorMessage = "";

        if (password.length() >= minLen && password.length() <= maxLen) {
            if (specialCount >= 1 && lowerCount >= 1 && upperCount >= 1 && numberCount >= 1) {
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