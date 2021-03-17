package com.example.savss.expensetracker;

import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shantam on 14-03-2018.
 */

public class FirebaseDBHelper {
    private static DatabaseReference myRef;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void addUser(String name, String email, String number, String password) {
        myRef = database.getReference("message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String child=number;
        myRef=database.getReference("Users").child(child);
        myRef.child("Email").setValue(email);
        myRef.child("Name").setValue(name);
        myRef.child("Number").setValue(number);
        myRef.child("Password").setValue(password);
    }
}

