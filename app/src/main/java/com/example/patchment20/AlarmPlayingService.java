package com.example.patchment20;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;


public class AlarmPlayingService extends Service {
    Notification notification;
    NotificationManager notification_manager;
    Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get ringtone and set duration
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        //Create Notification
        notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Alarm is on")
                .setContentText("You had set up your alarm")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notification_manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification_manager.notify(0, notification);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
        notification_manager.cancel(0);
    }
}
