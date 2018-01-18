package com.skwibble.skwibblebook.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.firebase.client.core.Context;

/**
 * Created by POPLIFY on 3/4/2017.
 */

abstract public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");

        Bundle dataBundle = intent.getBundleExtra("data");
        Log.d(TAG, dataBundle.toString());

    }
}