package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;



public class SettingsFragment extends Fragment {
   // private SectionsPagerAdapter mSectionsPagerAdapter;
    //private ViewPager mViewPager;



    Toast toast;
    private FirebaseAuth firebaseAuth;
    @SuppressLint("ShowToast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabs,container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);//fixture_tabs?????????????
        tabs.setupWithViewPager(viewPager);
        return view;

    }

    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ProfileManagement(), "Profile");
        adapter.addFragment(new AppSettings(), "App Settings");

        viewPager.setAdapter(adapter);



    }

   /* @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public static class PlaceholderFragment extends Fragment {
        /**
     * The fragment argument representing the section number for this
     * fragment.

     private static final String ARG_SECTION_NUMBER = "section_number";

     public PlaceholderFragment() {
     }

     /**
     * Returns a new instance of this fragment for the given section
     * number.

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int option = getArguments().getInt(ARG_SECTION_NUMBER);
            if (option == 1) {
                return inflater.inflate(R.layout.fragment_profile_management, container, false);
            }
            else{
                return  inflater.inflate(R.layout.fragment_app_settings, container, false);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }*/
   static class Adapter extends FragmentPagerAdapter {
       private final List<Fragment> mFragmentList = new ArrayList<>();
       private final List<String> mFragmentTitleList = new ArrayList<>();

       public Adapter(FragmentManager manager) {
           super(manager);
       }

       @Override
       public Fragment getItem(int position) {
           return mFragmentList.get(position);
       }

       @Override
       public int getCount() {
           return mFragmentList.size();
       }

       public void addFragment(Fragment fragment, String title) {
           mFragmentList.add(fragment);
           mFragmentTitleList.add(title);
       }

       @Override
       public CharSequence getPageTitle(int position) {
           return mFragmentTitleList.get(position);
       }
   }

   public static class error_display extends AppCompatActivity{
       Toast toast;
       @SuppressLint("ShowToast")
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.fragment_app_settings);
           setTitle("new category");
           toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
       }

       public void error_detection(View v) {
           EditText id = findViewById(R.id.newCategory);
           EditText password = findViewById(R.id.newCatBudget);

           if (id.getText().toString().isEmpty()){
               displayError(Integer.parseInt("Enter Category"));
               return;
           }

           if (password.getText().toString().isEmpty()){
               displayError(Integer.parseInt("Enter Budget"));
               return;
           }





           // TODO: Remove this if in final product
//           if (id.getText().toString().equals("a") && password.getText().toString().equals("a")) {
//               Intent toDashboard = new Intent(this, HomeActivity.class);
//               UserData.userID = 1;
//               startActivity(toDashboard);
//           }

           IDType idType;

           if (!password.getText().toString().isEmpty() && !id.getText().toString().isEmpty()) {
               Toast.makeText(error_display.this, "New Category added", Toast.LENGTH_LONG).show();
           }
           else {
               displayError(Integer.parseInt("Invalid details"));
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
}
