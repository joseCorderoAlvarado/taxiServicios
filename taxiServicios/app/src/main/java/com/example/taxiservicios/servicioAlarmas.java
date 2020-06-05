package com.example.taxiservicios;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import android.app.Service;

import androidx.core.app.NotificationCompat;

public class servicioAlarmas extends BroadcastReceiver {

//        DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        String quote;

        long when = System.currentTimeMillis();

        System.out.println("Cuando: " + when);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, servicioAlarmas.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // get your quote here
        // quote = doSomeMethod();


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Recordatorio")
                .setContentText("20 minutos para tu ese").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("sss"))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
        notificationManager.notify(5, mNotifyBuilder.build());
    }


}

