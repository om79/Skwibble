package com.skwibble.skwibblebook.user_group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.view_pager.Startup_activity;
import com.skwibble.skwibblebook.volley.UserAPI;

import java.util.HashMap;
import java.util.Map;

public class User_feedback extends Activity {

    RelativeLayout profile,setting,logout,help_lay;
    SaveData save_data;
    UsefullData objUsefullData;
    TextView title,child,user,help,logout_txt;
    RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        profile=(RelativeLayout) findViewById(R.id.profiledfds_layout);
        setting=(RelativeLayout) findViewById(R.id.setting_layout);
        logout=(RelativeLayout) findViewById(R.id.logout_layout);
        help_lay=(RelativeLayout) findViewById(R.id.help_layout);

        title=(TextView) findViewById(R.id.textView7);
        child=(TextView) findViewById(R.id.textView6);
        user=(TextView) findViewById(R.id.textView2);

        help=(TextView) findViewById(R.id.helptextView);
        logout_txt=(TextView) findViewById(R.id.textView);

        title.setTypeface(objUsefullData.get_ubntu_regular());
        child.setTypeface(objUsefullData.get_proxima_regusr());
        user.setTypeface(objUsefullData.get_proxima_regusr());

        help.setTypeface(objUsefullData.get_proxima_regusr());
        logout_txt.setTypeface(objUsefullData.get_proxima_regusr());


        back=(RelativeLayout) findViewById(R.id.imaqqwycback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });

        help_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent how2=new Intent(getParent(),Startup_activity.class);
                startActivity(how2);
            }

        });


        if (!save_data.getBoolean(Definitions.has_child)) {
            profile.setEnabled(false);
            profile.setAlpha(.3f);
        }




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                if (!save_data.getBoolean(Definitions.has_child) ) {
                    Intent edit = new Intent(getParent(), Add_child.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    parentActivity.startChildActivity("add_child_Activity", edit, false);
                }else {
                    finish();
                    Intent edit = new Intent(getParent(), Edit_child_profile.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    parentActivity.startChildActivity("EditActivity", edit, false);
                }

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), User_setting.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                parentActivity.startChildActivity("settingActivity", edit, false);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                                logout();

            }
        });
    }


    private void logout()
    {

        if(objUsefullData.isNetworkConnected()==false)
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
        else
        {
            objUsefullData.showProgress();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put("Accept", Definitions.version);
            headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
            headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

            Map<String,String> request = new HashMap<>();

            try {
                request.put("token", save_data.getString(Definitions.firebase_token));

            } catch (Exception e) {
                e.printStackTrace();
            }

            UserAPI.delete_StringResp("/users/sign_out",headers,request,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();

                            save_data.remove(Definitions.user_email);
                            save_data.remove(Definitions.auth_token);
                            save_data.remove(Definitions.show_child_form);
                            save_data.remove(Definitions.has_child);
                            save_data.remove(Definitions.has_first_post);
                            save_data.remove(Definitions.facebook_login);
                            save_data.remove(Definitions.badge_count);
                            save_data.remove(Definitions.has_badges);
                            save_data.remove(Definitions.current_noti_request);
                            save_data.remove(Definitions.current_noti_id);
                            save_data.remove(Definitions.current_journal_year);
                            save_data.remove(Definitions.id);

                            objUsefullData.firebase_analytics("logOut");

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.showMsgOnUI("Something went wrong!");


                        }
                    });
        }
    }

}
