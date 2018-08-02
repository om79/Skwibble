package com.skwibble.skwibblebook.firebase;

import android.app.Notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    int id,playpen_id,user_noti_id;
    String site_url;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Intent intent2 = new Intent(this, Tab_activity.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent2.putExtra("Bag_update",0);
//        startActivity(intent2);

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Notification Title: " + remoteMessage.getNotification().getTitle());
//            Log.e(TAG, "Notification Tag: " + remoteMessage.getNotification().getTag());
//            Log.e(TAG, "Notification click: " + remoteMessage.getNotification().getClickAction());
//            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json,""+remoteMessage.getNotification().getTitle(),""+remoteMessage.getNotification().getBody());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Definitions.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);



//        }else{
            // If the app is in background, firebase itself handles the notification
//        }
    }

    private void handleDataMessage(JSONObject json,String title, String body) {
        Log.e(TAG, "push json: " + json.toString());

        try {
//            JSONObject data = json.getJSONObject("data");

            String android_controller = json.optString("android_controller");

            if(json.has("id")){
                 id = json.optInt("id");
            }
            if(json.has("playpen_id")){
                playpen_id = json.optInt("playpen_id");
            }
            if(json.has("user_noti_id")){
                user_noti_id = json.optInt("user_noti_id");
            }
            if(json.has("site_url")){
                site_url = json.optString("site_url");
            }




//            Log.e(TAG, "title: " + id);
//            Log.e(TAG, "message: " + android_controller);


            sendNotification(title,body,""+id,android_controller,""+playpen_id,user_noti_id,site_url);


        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }





    private void sendNotification(String title,String messageBody, String id,String android_controller,String playpen_id,int user_noti_id, String site_url) {
        Intent intent = new Intent(this, Tab_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("id", id);
        intent.putExtra("android_controller", android_controller);
        intent.putExtra("playpen_id", playpen_id);
        intent.putExtra("user_noti_id", ""+user_noti_id);
        intent.putExtra("event_msg",messageBody);
        intent.putExtra("site_url",site_url);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//      Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Tab_activity.class);
//      Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        user_noti_id,
                        PendingIntent.FLAG_ONE_SHOT
                );


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(messageBody);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.small_app_icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.FLAG_GROUP_SUMMARY|Notification.DEFAULT_VIBRATE|Notification.FLAG_AUTO_CANCEL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.feed_app_icon))
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.Dark))

                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
//        {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            assert notificationManager != null;
//            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }

        assert notificationManager != null;
        notificationManager.notify(user_noti_id /* ID of notification */, notificationBuilder.build());

        Intent bag = new Intent("Msg");
        LocalBroadcastManager.getInstance(this).sendBroadcast(bag);



    }


}
