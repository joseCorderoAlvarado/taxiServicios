package com.example.taxiservicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.plus.internal.PlusCommonExtras.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String deviceToken = s;

        System.out.println("Refreshed token: " + s);

        // Do whatever you want with your token now
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server


    }
}
