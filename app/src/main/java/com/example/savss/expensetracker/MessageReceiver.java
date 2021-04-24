package com.example.savss.expensetracker;

import android.content.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;

import java.util.Date;
import java.util.Random;

public class MessageReceiver extends BroadcastReceiver {

    private static MessageListener mListener;
    private LocalDatabaseHelper localDatabaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        String format = data.getString("format");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i],format);


            String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                    + "Display message body: " + smsMessage.getDisplayMessageBody()
                    + "Time in millisecond: " + smsMessage.getTimestampMillis()
                    + "Message: " + smsMessage.getMessageBody();
            String temp=message.toLowerCase();
            String temp2;

            //For Amazon




            //For Paytm
            if(temp.indexOf("send to")!=-1 && temp.indexOf("paytm")!=-1 && temp.indexOf("upi ref")!=-1) {
                temp2 = "Category: Paytm expense";
                String body=smsMessage.getDisplayMessageBody();
                String amount="100";
                String ShopName[]={"MRK Bite N Slurp","SV Dine Spot","SS Aahaar","SS Curd Rice","SS Salad"};
                Random rand = new Random();
                String s=ShopName[rand.nextInt(5)];

                try {
                    amount = body.substring(body.indexOf("Rs.") + 3, body.indexOf(" ", body.indexOf("Rs.")));
                }
                catch(Exception e){
                }
                Date date = new Date(smsMessage.getTimestampMillis());
                localDatabaseHelper.addMessage(String.valueOf(UserData.userID),UserData.categories.indexOf("Bank") ,body,"expense",amount,s,date);


            }
            else if(temp.indexOf("received")!=-1 && temp.indexOf("paytm")!=-1 && temp.indexOf("upi ref")!=-1) {
                temp2 = "Category: Paytm income";

                String body=smsMessage.getDisplayMessageBody();
                String amount="100";
                String ShopName[]={"Megha Bite N Slurp","Sachu Dine Spot","Sahana Aahaar","Shururu Curd Rice","Shreyas Salad paradise","Pra Pra Pizza Hut"};
                Random rand = new Random();
                String s=ShopName[rand.nextInt(6)];

                try {
                    amount = body.substring(body.indexOf("Rs.") + 3, body.indexOf(".", body.indexOf("Rs.")));
                }
                catch(Exception e){
                }
                Date date = new Date(smsMessage.getTimestampMillis());
                localDatabaseHelper.addMessage(String.valueOf(UserData.userID),UserData.categories.indexOf("Bank") ,body,"income",amount,s,date);
            }
            else
                temp2="Category: others";
            message=message+temp2;
            mListener.messageReceived(message);
        }
        //localDatabaseHelper.addMessage();
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}