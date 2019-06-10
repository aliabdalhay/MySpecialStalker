package com.image.get.myspecialstalker;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;


public class Notifications {

    static NotificationCompat.Builder builder;

    public static NotificationCompat.Builder build_noti(String title, Context context)
    {
        builder = new NotificationCompat.Builder(context.getApplicationContext(), "1")
                .setSmallIcon(R.drawable.noti)
                .setContentTitle("mySpecialStalker")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        return builder;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Notification noti_builder(String title, Context context)
    {
        Notification notification = new Notification.Builder(context.getApplicationContext())
                .setSmallIcon(R.drawable.noti)
                .setContentTitle("mySpecialStalker")
                .setContentText(title)
                .setChannelId("1").build();

        return notification;
    }

    public static void noti_compat(Context context, NotificationCompat.Builder builder){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
        notificationManager.notify(1, builder.build());
    }


    public static void Notifications_do(String title, Context context) {

        int build_version_sdk = Build.VERSION.SDK_INT;
        int build_version_codes = Build.VERSION_CODES.O;


        if (build_version_sdk < build_version_codes) {

            builder = build_noti(title, context);
            noti_compat(context, builder);
        }
        else
        {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("1", "Notification Channel", importance);

            Notification noti = noti_builder(title, context);
            NotificationManager notificationManager = context.getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, noti);
        }
    }



    public static PendingIntent sent_PI(Context context){
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
                "SMS_SENT"), 0);
        return sentPI;
    }

    public static PendingIntent deliver_PI(Context context){
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent("SMS_DELIVERED"), 0);

        return deliveredPI;
    }

    public static void send_sms(Context context, String phoneNumber, String message, PendingIntent sentPI, PendingIntent deliveredPI, BroadcastReceiver sendSMS, BroadcastReceiver deliverSMS){
        context.getApplicationContext().registerReceiver(sendSMS, new IntentFilter("SMS_SENT"));
        context.getApplicationContext().registerReceiver(deliverSMS, new IntentFilter("SMS_DELIVERED"));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }



    public static void send_message(String phoneNumber, String message, final Context context) {

        PendingIntent sentPI = sent_PI(context);
        PendingIntent deliveredPI = deliver_PI(context);
        BroadcastReceiver sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if(getResultCode() == Activity.RESULT_OK){
                    Notifications_do("message sent successfully!", context);
                    return;
                }
            }
        };

        BroadcastReceiver deliverSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if(getResultCode() == Activity.RESULT_OK) {
                    Notifications_do("message received successfully!", context);
                    return;
                }
            }
        };

        send_sms(context, phoneNumber, message, sentPI, deliveredPI, sendSMS, deliverSMS);
    }


}
