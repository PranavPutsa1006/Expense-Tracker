package com.example.savss.expensetracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    private int selectedID = -1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add_transaction:
                    return setFragment(new AddTransactionFragment(), R.id.navigation_add_transaction);
                case R.id.navigation_dashboard:
                    return setFragment(new DashboardFragment(), R.id.navigation_dashboard);
                case R.id.navigation_settings:
                    return setFragment(new SettingsFragment(), R.id.navigation_settings);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(this, null, null, 1);
        localDatabaseHelper.initializeUserData(UserData.userID);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new DashboardFragment(), R.id.navigation_dashboard);
       // FirebaseDBHelper.setMyRef();
    }

    private boolean setFragment(Fragment fragment, int id) {
        if (selectedID == id) {
            return false;
        }
        selectedID = id;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
