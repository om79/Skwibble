package com.skwibble.skwibblebook.media_upload;

/**
 * Created by POPLIFY on 10/12/2017.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.InitializeActivity;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pankaj on 13/02/16.
 */


public class Background_update extends IntentService {
    SaveData save_data;

    public Background_update() {
        super(Background_update.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Extract the receiver passed into the service
//        rec = intent.getParcelableExtra("receiver");
        // Extract additional values from the bundle
        save_data = new SaveData(InitializeActivity.context);
        String request=intent.getStringExtra("request");

        if(request.equals("version")){
            add_verson();
        }else {
            app_not_use(intent.getBooleanExtra("app_status",false));
        }

    }

    private void add_verson()
    {
        //Define Headers
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        JSONObject user = new JSONObject();
        try {
            user.put("version", Definitions.app_version);
            user.put("token", save_data.getString(Definitions.firebase_token));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserAPI.post_JsonResp("add_version", user, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
    private void app_not_use(boolean is_in_app)
    {
        //Define Headers
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        JSONObject user = new JSONObject();
        try {
            user.put("token", save_data.getString(Definitions.firebase_token));
            user.put("is_in_app", is_in_app);
            user.put("version", Definitions.app_version);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserAPI.post_JsonResp("/check_app_used", user, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}