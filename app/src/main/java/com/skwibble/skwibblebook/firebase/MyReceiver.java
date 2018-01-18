package com.skwibble.skwibblebook.firebase;

import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by POPLIFY on 3/4/2017.
 */

 public class MyReceiver extends WakefulBroadcastReceiver {



    @Override
    public void onReceive(android.content.Context context, Intent intent) {
        Log.e("-----","got it");
    }
}
///finally you have to write the following codes on the
//    onMessageReceived
//            Intent intents=new Intent();
//            intents.setAction("MyReceiver");
//            intents.putExtra("message",message.getData().get("message"));
//            intents.putExtra("from",message.getData().get("from"));
//            getBaseContext().sendBroadcast(intents);