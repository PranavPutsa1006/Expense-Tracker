package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.text.DecimalFormat;

import java.util.ArrayList;


public class AppSettings extends Fragment {

    private ViewSwitcher viewSwitcher;
    Button btnNext, btnPrev, btnCan;
    View viewapp;
    Toast toast;

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

    private void displayToast(String message) {
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }

    public void notifyIfExceededLimit(String category, float amount) {
        LocalDatabaseHelper localDatabaseHelper = new LocalDatabaseHelper(getActivity(), null, null, 1);
        ArrayList<String> categories = localDatabaseHelper.getAllCategories();
        ArrayList<Integer> budgets = localDatabaseHelper.getAllCategoryBudgets();
        ArrayList<Float> expense = localDatabaseHelper.getCategoryWiseExpenses();

        int categoryIndex = categories.indexOf(category);
        float finalAmount = expense.get(categoryIndex) + amount;
        String message = "";

        if (budgets.get(categoryIndex) < (expense.get(categoryIndex) + amount)) {
            /*String NOTIFICATION_CHANNEL_ID = "Limit-Alert";

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(addTransactionView.getContext(), NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Category " + category + " Exceeded Limit")
                    .setContentText("You have exceeded your budget " + budgets.get(categoryIndex) + ". Your current expenditure is " + finalAmount + ".");

            Intent notificationIntent = new Intent(getContext(), HomeActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);

            notificationManager.notify(1, notificationBuilder.build());
            System.out.println("notify");*/

            message = "You have exceeded your budget " + budgets.get(categoryIndex) + " for " + category + ". Your current expenditure is " + finalAmount + ".\n";
        }

        alert a = new alert();
        a.hello(message);
    }

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
        String cv[]={"Category", "Budget", "Spent"};
        int rowCount=count+1;
        int columnCount=cv.length;

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = (TableLayout) viewapp.findViewById(R.id.cat_table);
        tableLayout.removeAllViewsInLayout();
        tableLayout.setStretchAllColumns(true);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(0, 2, 0, 2);
        tableRowParams.height = 120;
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
                    DecimalFormat df = new DecimalFormat("#.##");
                    rowentry[2]=df.format(spent)+"%";
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
        //alert a = new alert();
        //a.hello();
    }

    static class alert extends AppCompatActivity{
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(getApplicationContext()).setContentTitle("Expense Tracker").setContentText("Alert Triggered, Maximum expense exceeded").setContentTitle("Alert").setSmallIcon(R.mipmap.ic_launcher_round).build();

        public void testMessage(String message, Intent intent){

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);


            String channelId = "some_channel_id";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            android.support.v4.app.NotificationCompat.Builder notificationBuilder =
                    new android.support.v4.app.NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setBadgeIconType(android.support.v4.app.NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }

            assert notificationManager != null;
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        public void hello(String message){
            alert a = new alert();
            Intent resultIntent = new Intent(this,alert.class);
            a.testMessage(message,resultIntent);
        }
    }

}

