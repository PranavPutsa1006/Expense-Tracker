package com.example.savss.expensetracker;

import android.content.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    private static MessageListener mListener;

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
            if(temp.indexOf("send to")!=-1 && temp.indexOf("paytm")!=-1)
                temp2="Category: Paytm expense";
            else if(temp.indexOf("received")!=-1 && temp.indexOf("paytm")!=-1)
                temp2="Category: Paytm income";
            else
                temp2="Category: others";
            message=message+temp2;
            mListener.messageReceived(message);
        }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}