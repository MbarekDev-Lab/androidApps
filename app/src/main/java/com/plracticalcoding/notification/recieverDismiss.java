package com.plracticalcoding.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

public class recieverDismiss extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.cancel(1);
    }
}
