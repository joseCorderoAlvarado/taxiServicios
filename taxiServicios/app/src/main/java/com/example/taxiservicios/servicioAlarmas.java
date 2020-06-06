package com.example.taxiservicios;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import android.app.Service;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class servicioAlarmas extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final String ADMIN_CHANNEL_ID ="admin_channel";

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context,ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Recordatorio")
                .setContentText("Faltan 20 minutos para tu servicio").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("sss"))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
        notificationManager.notify(notificationId, mNotifyBuilder.build());
    }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void setupChannels() {
            CharSequence adminChannelName = "Cliente";
            String adminChannelDescription = "Canal del Cliente";
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }

        }


}

