package com.plracticalcoding.notification;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.plracticalcoding.myapplication.R;

public class NotificationReciever extends BroadcastReceiver {
    public final String CHANAL_ID = "1";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Notiffy me   ....!", Toast.LENGTH_SHORT).show();


        NotificationChannel channel = new NotificationChannel(CHANAL_ID,"1"
                , NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification.Builder(context,CHANAL_ID);
        builder.setSmallIcon(R.drawable.round_add_alert_24)
                .setContentTitle("Title")
                .setContentText("Notification")
                .setPriority(Notification.PRIORITY_DEFAULT);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 0);


            NotificationManagerCompat compat = NotificationManagerCompat.from(context);
            compat.notify(1, builder.build());
        }

    }
}
