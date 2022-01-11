package com.example.patchment20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String toggle = intent.getExtras().getString("toggle");
        Intent service_intent = new Intent(context, AlarmPlayingService.class);
         service_intent.putExtra("toggle", toggle);
        context.startService(service_intent);
    }
}
