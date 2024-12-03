package com.plracticalcoding.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class receivers extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String txt = intent.getStringExtra("toast");
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();

    }
}
