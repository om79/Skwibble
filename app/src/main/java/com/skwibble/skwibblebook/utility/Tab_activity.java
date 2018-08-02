package com.skwibble.skwibblebook.utility;

import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.baddies_group.Buddies_group_activity;
import com.skwibble.skwibblebook.firebase.NotificationListener;
import com.skwibble.skwibblebook.journal_group.Journal_group_activity;
import com.skwibble.skwibblebook.media_upload.Background_update;
import com.skwibble.skwibblebook.playpen_group.Playpen_group_activity;
import com.skwibble.skwibblebook.story_group.Story_group_activity;
import com.skwibble.skwibblebook.user_group.LoginActivity;
import com.skwibble.skwibblebook.user_group.User_group_activitty;
import com.skwibble.skwibblebook.volley.UserAPI;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Tab_activity extends TabActivity {

    SaveData save_data;
    UsefullData objUsefullData;
    TabHost tabHost;
    TabHost.TabSpec spec;
    CircularTextView bag;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save_data = new SaveData(Tab_activity.this);

        objUsefullData = new UsefullData(Tab_activity.this);

        if(!save_data.isExist(Definitions.auth_token)){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        setContentView(R.layout.tab_activity);
        final TabWidget tw=(TabWidget) findViewById(android.R.id.tabs);
        final FrameLayout fl=(FrameLayout) findViewById(android.R.id.tabcontent);
        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                        fl.setLayoutParams(param);
                        }else {
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0,
                                    1.83f
                            );
                            fl.setLayoutParams(param);
                        }

                    }
                });
        Intent intent1=getIntent();
        Bundle extras = intent1.getExtras();
        if (extras != null) {
            if (extras.containsKey("index")) {
                    setTabs(extras.getInt("index",0),false);
            }else if (extras.containsKey("from")) {
                setTabs(extras.getInt("from",0),true);
            }else{

                if (extras.containsKey("android_controller")) {
                if(extras.getString("android_controller") != null && !extras.getString("android_controller").equalsIgnoreCase("null")) {

                    intent_handler(intent1,true);
                }else{

                    setTabs(0,true);
                }
                }else{
                    setTabs(0,true);
                }
            }
        }else {
            if (!save_data.getBoolean(Definitions.has_child) ) {
                setTabs(3,true);
            }else {
                setTabs(0,true);
            }

        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.startForegroundService(new Intent(Tab_activity.this, NotificationListener.class));
//        } else {
//            this.startService(new Intent(Tab_activity.this, NotificationListener.class));
//        }
        startService(new Intent(Tab_activity.this, NotificationListener.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


            getTabWidget().getChildAt(4).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //do whatever you need
                    save_data.save(Definitions.badge_count, 0);
                    save_data.save(Definitions.has_badges, false);
                    tabHost.setCurrentTab(4);
                    setbag();
                    NotificationManager notifManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.cancelAll();
                }
            });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (save_data.isExist(Definitions.auth_token)) {
            Intent i = new Intent(this, Background_update.class);
            i.putExtra("request", "app");
            i.putExtra("app_status", true);

            startService(i);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (save_data.isExist(Definitions.auth_token)) {
            Intent i = new Intent(this, Background_update.class);
            i.putExtra("request", "app");
            i.putExtra("app_status", false);
            startService(i);
        }
    }

    private void setTabs(int current_tab,boolean firebase) {
        if(firebase) {
            addTab("Story", R.drawable.tab_story, Story_group_activity.class);
            addTab("Playpens", R.drawable.tab_playpen, Playpen_group_activity.class);
            addTab("Photo Journal", R.drawable.tab_journal, Journal_group_activity.class);
            addTab("Feed", R.drawable.tab_buddies, Buddies_group_activity.class);
            addTab("Notifications", R.drawable.tab_user, User_group_activitty.class);
            tabHost.setCurrentTab(current_tab);
        }else {
            tabHost.setCurrentTab(4);
            tabHost.setCurrentTab(current_tab);
        }
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTypeface(objUsefullData.get_ubntu_regular());
        bag = (CircularTextView) tabIndicator.findViewById(R.id.qazxsw);
        bag.setSolidColor("#F00000");
        bag.setVisibility(View.GONE);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        icon.setPadding(objUsefullData.screen_size()-10,objUsefullData.screen_size()-10,objUsefullData.screen_size()-10,objUsefullData.screen_size()-10);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);




        if(labelId.equals("Notifications")&&objUsefullData.isNetworkConnected()){

                get_badges();

        }
    }

    @Override
    public void onNewIntent(Intent intent) {

        intent_handler(intent,false);

    }

    public   void get_badges() {
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );
        UserAPI.get_JsonObjResp("/check_badges", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        save_data.save(Definitions.badge_count,response.optInt("badge_count"));
                        save_data.save(Definitions.has_badges,response.optBoolean("has_badges"));
                        setbag();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }

    public  void setbag(){

        View tab = tabHost.getTabWidget().getChildTabViewAt(4);
        TextView t = (TextView)tab.findViewById(R.id.qazxsw);
        t.setTextColor(getResources().getColor(R.color.white));
        t.setTypeface(objUsefullData.get_ubntu_regular());

        if(save_data.getBoolean(Definitions.has_badges))
        {

            if(save_data.getInt(Definitions.badge_count)>99){
                t.setVisibility(View.VISIBLE);
                t.setText("99+");

            }else if(save_data.getInt(Definitions.badge_count)==0){

                t.setVisibility(View.GONE);
            }else {
                t.setVisibility(View.VISIBLE);
                t.setText(String.valueOf(save_data.getInt(Definitions.badge_count)));

            }
        }else{

            bag.setVisibility(View.GONE);
        }


    }

    public void intent_handler(Intent intent,boolean firebase){


        try {
            if(save_data.getInt(Definitions.badge_count)!=0) {
                int n = save_data.getInt(Definitions.badge_count);
                save_data.save(Definitions.badge_count, n - 1);
                setbag();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("android_controller")) {


                save_data.save(Definitions.current_noti_request,extras.getString("android_controller"));
                save_data.save(Definitions.current_noti_id,extras.getString("id"));
                save_data.save(Definitions.current_event_msg,extras.getString("event_msg"));

              String playpen_id = extras.getString("playpen_id");

              switch (save_data.getString(Definitions.current_noti_request)) {
                    case "Feed_invite_page":

                        setTabs(3,firebase);

                        break;
                    case "Story_invite_page":


                        setTabs(0,firebase);

                        break;
                    case "Feed_post_details":



                        setTabs(3,firebase);

                        break;
                    case "Story_post_details":

                        if(playpen_id!=null){
                        if(playpen_id.equals("88888888"))
                        {
                            save_data.save(Definitions.current_noti_request,"Feed_post_details");
                            setTabs(3,firebase);
                        }else {
                            setTabs(0, firebase);
                        }
                        }else {
                            setTabs(0, firebase);
                        }
                        break;
                    case "Playpen_post_details":

                        setTabs(1,firebase);

                        break;
                    case "Months_details":

                        setTabs(2,firebase);

                        break;
                    case "Story_index_toast":

                        setTabs(0,firebase);

                        break;
                    case "Story_invite_toast":

                        setTabs(0,firebase);

                        break;
                    case "Journal_index_toast":

                        setTabs(2,firebase);

                        break;
                    case "Playpen_index_toast":

                        setTabs(1,firebase);

                        break;
                  case "openWeb":

                      if(save_data.getBoolean(Definitions.has_child)) {
                          setTabs(0, firebase);
                      }else {
                          setTabs(3,firebase);
                      }

                      String url=extras.getString("site_url");

                      if(url.startsWith("\"") && url.endsWith("\"")){
                          url=url.replace("\"", "");
                      }


                      try {
                          Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                          startActivity(myIntent);
                      } catch (ActivityNotFoundException e) {
                          objUsefullData.make_toast( "No application can handle this request."
                                  + " Please install a Web browser");
                          e.printStackTrace();
                      }

                      break;


                    default:
                        setTabs(0,firebase);

                        break;
                }
            }
        }
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(objUsefullData.isNetworkConnected()){

                get_badges();
            }

        }
    };




}