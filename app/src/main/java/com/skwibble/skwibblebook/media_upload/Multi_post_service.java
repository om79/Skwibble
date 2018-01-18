package com.skwibble.skwibblebook.media_upload;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Post_to_skwibble;
import com.skwibble.skwibblebook.story_group.Story_activity;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.InitializeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Multi_post_service extends Service {
    SaveData save_data;
    UsefullData objUsefullData;
    public static final String BROADCAST_ACTION = "com.skwibble.skwibblebook.story_group.Story_activity";
    Intent intent;


    public void upload( final String content, final String date, final String post_type, final String privacy){

        List<Part> files = new ArrayList<>();
        for (int i = 0; i < Post_to_skwibble.slider_image_list.size(); i++) {


            files.add(new FilePart("story_post[story_post_attachments_attributes]["+i+"]media", new File(Post_to_skwibble.slider_image_list.get(i).getpicture())));


        }

        Ion.with(InitializeActivity.context)
                .load("POST",Definitions.APIdomain+Definitions.STORY_FILE_UPLOAD_URL)
                .setTimeout(60 * 60 * 60 * 1000)
                .uploadProgress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {


                        int percent = (int) (downloaded * 100 / total);
                        intent.putExtra("status", "pending");
                        intent.putExtra("percent",percent);
                        sendBroadcast(intent);
                        Story_activity.uploading=true;

                    }
                })

                .addHeader("Accept", Definitions.version)
                .addHeader("X-User-Email", save_data.get(Definitions.user_email))
                .addHeader("X-User-Token", save_data.get(Definitions.auth_token))
                .addMultipartParts(files)
                .setMultipartParameter("story_post[content]", content)
                .setMultipartParameter("story_post[date]", date)
                .setMultipartParameter("story_post[post_type_id]", post_type)
                .setMultipartParameter("story_post[privacy_id]", privacy)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            // error: log the message here
//                            save_data.save(Definitions.POST_COUNT,0);

                            intent.putExtra("status", "done");
                            intent.putExtra("percent",100);
                            sendBroadcast(intent);
                            Story_activity.uploading=false;
                            return;
                        }
                        if (result != null) {
                            // result is the response of your server

//                            save_data.save(Definitions.POST_COUNT,0);
                            JSONObject jsonObj = null;
                            try{
                                jsonObj = new JSONObject(result);
                                System.out.println(jsonObj.getString("status"));
                                save_data.save(Definitions.show_child_form, jsonObj.optString("show_child_form"));

                                save_data.save(Definitions.has_child, jsonObj.optBoolean("has_child"));

                                save_data.save(Definitions.has_first_post, jsonObj.optBoolean("has_first_posts"));
                            }

                            catch(JSONException e1){
                                e1.printStackTrace();
                            }
                            intent.putExtra("status", "done");
                            intent.putExtra("percent",100);
                            sendBroadcast(intent);
                            Story_activity.uploading=false;
                        }
                    }
                });


    }


    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        save_data = new SaveData(InitializeActivity.context);
        objUsefullData = new UsefullData(InitializeActivity.context);

        if(intent!=null) {
            String content = intent.getStringExtra("content");
            String date = intent.getStringExtra("date");
            String post_type = intent.getStringExtra("post_type");
            String privacy = intent.getStringExtra("privacy");


            upload(content, date, post_type, privacy);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}