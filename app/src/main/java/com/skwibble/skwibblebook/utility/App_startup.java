package com.skwibble.skwibblebook.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.skwibble.skwibblebook.media_upload.Background_update;
import com.skwibble.skwibblebook.user_group.LoginActivity;
import com.skwibble.skwibblebook.view_pager.Startup_activity;

/**
 * Created by POPLIFY on 4/23/2017.
 */

public class App_startup extends Activity
{
    SaveData save_data;
    UsefullData objUsefullData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        save_data = new SaveData(App_startup.this);
        objUsefullData = new UsefullData(App_startup.this);


//        if(save_data.isExist(Definitions.app_install_first)) {
            if (save_data.isExist(Definitions.auth_token)) {
                onVersion_update();
                Intent intent1 = getIntent();

                Bundle extras = intent1.getExtras();
                if (extras != null) {

                    Intent intent = new Intent(getApplicationContext(), Tab_activity.class);

                    if (!save_data.getBoolean(Definitions.has_child) && !intent.hasExtra("android_controller") ) {
                        intent.putExtra("from",3);
                    }else {
                        intent.putExtra("android_controller", extras.getString("android_controller"));
                        intent.putExtra("playpen_id", extras.getString("playpen_id"));
                        intent.putExtra("id", extras.getString("id"));
                        intent.putExtra("user_noti_id", extras.getString("user_noti_id"));
                        intent.putExtra("event_msg",extras.getString("event_msg"));
                        intent.putExtra("site_url",extras.getString("site_url"));
                    }
                    startActivity(intent);
                    finish();

                } else {

                    if (!save_data.getBoolean(Definitions.has_child)) {

                        Intent intent = new Intent(getApplicationContext(), Tab_activity.class);
                        intent.putExtra("from",3);
                        startActivity(intent);
                        finish();
                    }else {

                        Intent intent = new Intent(getApplicationContext(), Tab_activity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }else{
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
//        }else{
//            Intent how2=new Intent(getApplicationContext(),Startup_activity.class);
//            how2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            how2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(how2);
//        }
    }


    public void onVersion_update() {
        if (save_data.isExist(Definitions.auth_token)) {
            Intent i = new Intent(this, Background_update.class);
            i.putExtra("request", "version");
            startService(i);
        }

    }
    }


