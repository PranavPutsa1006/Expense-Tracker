package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

/**
 * Created by Shradhaa on 26-02-2018.
 */

public class AppSettings extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext, btnPrev, btnCan;
    View viewapp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewapp = inflater.inflate(R.layout.fragment_app_settings,container, false);
        tableCreate();

        //view switcher code
        btnNext = (Button) viewapp.findViewById(R.id.addCategory);
        btnPrev = (Button) viewapp.findViewById(R.id.saveCategory);
        btnCan = (Button) viewapp.findViewById(R.id.cancel);
        viewSwitcher = (ViewSwitcher) viewapp.findViewById(R.id.simpleViewSwitcher);
        btnNext.setOnClickListener(setViewSwitcherNext);
        btnPrev.setOnClickListener(setViewSwitcherPrev);
        btnCan.setOnClickListener(setViewSwitcherCancel);
        return viewapp;
    }

    private View.OnClickListener setViewSwitcherNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewSwitcher.setDisplayedChild(1);
            //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener setViewSwitcherPrev = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LocalDatabaseHelper DB = new LocalDatabaseHelper(getActivity(), null, null, 1);
            TextView cat= viewSwitcher.findViewById(R.id.category);
            TextView bud= viewSwitcher.findViewById(R.id.budget);
            int budget=Integer.parseInt(bud.getText().toString().trim());
            DB.makeNewCategory(cat.getText().toString(), budget);
            tableCreate();
            DB.initializeUserData(UserData.userID);
            viewSwitcher.setDisplayedChild(0);
            //Toast.makeText(getActivity(), bud.length(), Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener setViewSwitcherCancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewSwitcher.setDisplayedChild(0);
            //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        }
    };

    public void tableCreate()
    {
        LocalDatabaseHelper DB = new LocalDatabaseHelper(getActivity(), null, null, 1);
        ArrayList<String> categories = DB.getAllCategories();
        ArrayList<Integer> budgets = DB.getAllCategoryBudgets();
        ArrayList<Float> expenses = DB.getCategoryWiseExpenses();

        int count=categories.size(); //has to be retrieved dynamically
        String rv[]=new String[count];
        for(int i=0;i<count;i++)
        {
            rv[i]=""+(i+1);
        }
        String cv[]={"Category", "Budget", "%Spent"};
        int rowCount=count+1;
        int columnCount=cv.length;

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = (TableLayout) viewapp.findViewById(R.id.cat_table);
        tableLayout.removeAllViewsInLayout();
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;


        for (int i = 0; i < rowCount; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setBackgroundColor(Color.BLACK);

            for (int j= 0; j < columnCount+1; j++) {
                // 4) create textView
                TextView textView = new TextView(getActivity());
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                String rowentry[]=new String[3];
                if(i!=0)
                {
                    rowentry[0]=categories.get(i-1);
                    rowentry[1]=""+budgets.get(i-1);
                    float spent;
                    if(i-1<expenses.size())
                        spent = expenses.get(i-1)/budgets.get(i-1);
                    else
                        spent= (float) 0.0;
                    rowentry[2]=spent+"%";
                }
                //rowentry[2]="0%";

                //Log.d ("TAG", "-___>"+id);
                if (i ==0 && j==0){
                    textView.setText("SNo");
                } else if(i==0){
                    Log.d("TAAG", "set Column Headers");
                    textView.setText(cv[j-1]);
                }else if( j==0){
                    Log.d("TAAG", "Set Row Headers");
                    textView.setText(rv[i-1]);
                }else {
                    textView.setText(rowentry[j-1]);
                }

                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        /*ScrollView sv = new ScrollView(getActivity());
        HorizontalScrollView hsv = new HorizontalScrollView(getActivity());
        hsv.addView(tableLayout);
        sv.addView(hsv);*/
    }

}
