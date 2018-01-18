package com.skwibble.skwibblebook.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Belal on 3/18/2016.
 */
//Class extending service as it is a service that will run in background
public class NotificationListener extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new MyFirebaseMessagingService();

        return START_STICKY;
    }



}
