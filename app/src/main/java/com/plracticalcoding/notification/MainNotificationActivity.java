package com.plracticalcoding.notification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

import java.util.Calendar;

public class MainNotificationActivity extends AppCompatActivity {
    Button button;
    public final String CHANAL_ID = "1";
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button = findViewById(R.id.notification_button);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 43);
        calendar.set(Calendar.SECOND, 0);

        Intent i = new Intent(getApplicationContext(), NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, i, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                button.setText("" + count);
                if (count == 5) {
                    startNotification();
                }
            }
        });
    }

    private void startNotification() {
        Intent i = new Intent(this, MainNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        Intent actionIntent = new Intent(this, receivers.class);
        actionIntent.putExtra("toast", "this is a Notification");
        PendingIntent actionPending = PendingIntent.getBroadcast(this, 0, actionIntent, 0);
        Notification.Action action = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.bell), "Tost Message", actionPending).build();

        Intent dismissIntent = new Intent(this, recieverDismiss.class);
        PendingIntent dismissPending = PendingIntent.getBroadcast(this, 0, dismissIntent, 0);
        Notification.Action dismissAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.round_add_alert_24), "Dismiss", dismissPending).build();

        NotificationChannel channel = new NotificationChannel(CHANAL_ID, "1"
                , NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.mbarek);
        String text = getResources().getString(R.string.big_text);


        Notification.Builder builder = new Notification.Builder(MainNotificationActivity.this, CHANAL_ID);
        builder.setSmallIcon(R.drawable.round_add_alert_24)
                .setContentTitle("Title")
                .setContentText("Notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(action)
                .addAction(dismissAction)
                .setColor(Color.BLUE)
                .setLargeIcon(icon)
                .setStyle(new Notification.BigTextStyle().bigText(text));

        NotificationManagerCompat compat = NotificationManagerCompat.from(MainNotificationActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        compat.notify(1, builder.build());

    }


}