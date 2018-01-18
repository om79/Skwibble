package com.skwibble.skwibblebook.playpen_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.user_group.Add_child;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Playpen_show extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public ListView lv;
    UsefullData objUsefullData;
    Playpen_show_adapter adapter;
    SaveData save_data;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    RelativeLayout create_playpen;
    LinearLayout not_found,not_found_create_btn;
    TextView create,default_txt,title;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean refresh=false;
    int start_call=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playpen_show);

        save_data = new SaveData(getParent());

        objUsefullData = new UsefullData(getParent());

        if(!save_data.isExist(Definitions.show_playpen)) {

            objUsefullData.showpopup(R.mipmap.show_playpen_index,Definitions.show_playpen);
        }



        Intent intent1=getIntent();
        Bundle extras = intent1.getExtras();
        if (extras != null) {
            if (extras.containsKey("current_event_msg")) {
                if(save_data.isExist(Definitions.current_event_msg)) {
                    objUsefullData.showMsgOnUI(extras.getString("current_event_msg"));
                    save_data.remove(Definitions.current_event_msg);
                }
            }
        }

        lv=(ListView)findViewById(R.id.playpen_list);
        create_playpen=(RelativeLayout) findViewById(R.id.invideiew10);
        not_found=(LinearLayout)findViewById(R.id.not_found_layout);
        not_found_create_btn=(LinearLayout)findViewById(R.id.not_btn_layout);
        title=(TextView) findViewById(R.id.tesssxtView7);
        title.setTypeface(objUsefullData.get_ubntu_regular());
        create=(TextView) findViewById(R.id.not_ghgffound_txt);
        create.setTypeface(objUsefullData.get_proxima_regusr());
        default_txt=(TextView) findViewById(R.id.not_found_txt);
        default_txt.setTypeface(objUsefullData.get_proxima_regusr());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.playpen_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Dark,R.color.orange);
        adapter = new Playpen_show_adapter(getParent(), R.layout.row_show_playpen, actorsList);
        lv.setAdapter(adapter);



        if(!save_data.getBoolean(Definitions.has_child))
        {
            default_txt.setText("Add Child");
            create_playpen.setVisibility(View.INVISIBLE);
            get_playpen_list(true);


            objUsefullData.firebase_analytics("withoutChild");

        }else {
            default_txt.setText("Create Playpen");
            create_playpen.setVisibility(View.VISIBLE);
            get_playpen_list(true);
        }




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edit = new Intent(getParent(), Playpen_expand_activity.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();
                edit.putExtra("gruop_id",String.valueOf(actorsList.get(position).getid()));
                parentActivity.startChildActivity("playpen_expend_Activity", edit, false);

            }
        });


        create_playpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Create_playpen.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();

                parentActivity.startChildActivity("create_playpen_Activity", edit, false);
            }

        });
        not_found_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(default_txt.getText().toString().equals("Create Playpen")) {

                    Intent edit = new Intent(getParent(), Create_playpen.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                    parentActivity.startChildActivity("create_playpen_Activity", edit, false);
                }else {
                    Intent edit = new Intent(getParent(), Add_child.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                    parentActivity.startChildActivity("add_child_Activity", edit, false);
                }

            }
        });


    }

    private void get_playpen_list(final boolean loader) {

        if (loader) {
            objUsefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/playpens", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);
                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                        if(actorsList.size()==0){
                            not_found.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!loader ) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                        if(actorsList.size()==0){
                            not_found.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private void set_up_values(JSONObject response)
    {
        try {

            actorsList.clear();
            JSONArray comments = response.getJSONArray("playpens");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                Actors actor = new Actors();

                String name = in.optString("name");
                int id = in.optInt("id");
                String image_url = in.optString("image_url",null);
                String member_counts = in.optString("member_counts");
                boolean admin = in.optBoolean("admin");

                String latest_date = in.optString("latest_date",null);

                actor.setpicture(image_url);
                actor.setid(id);
                actor.setdescription(name);
                actor.setcreated_date(member_counts);
                actor.setcomnt(""+admin);
                actor.settitle(latest_date);

                actorsList.add(actor);

            }



        adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onRefresh() {

        if(objUsefullData.isNetworkConnected()) {


            get_playpen_list(false);
            swipeRefreshLayout.setRefreshing(true);
        }else {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        start_call++;
        if(refresh){
            onRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(start_call>1) {
            refresh = true;
        }

    }



}
