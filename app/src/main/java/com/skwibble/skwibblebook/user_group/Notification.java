package com.skwibble.skwibblebook.user_group;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.baddies_group.Buddies_invite_activity;
import com.skwibble.skwibblebook.journal_group.Months_details;
import com.skwibble.skwibblebook.playpen_group.Playpen_post_detail;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.story_group.Post_details;
import com.skwibble.skwibblebook.story_group.Stroy_invite_activity;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notification extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView lv;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    Notification_adapter adapter;
    SaveData save_data;
    UsefullData objUsefullData;

    TextView title,title_not;
    LinearLayout not_found;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean load_more=true;
    int more=1;
    boolean refresh=false;
    int start_call=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_notiswipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Dark,R.color.orange);

        title=(TextView) findViewById(R.id.textVizew7);
        title.setTypeface(objUsefullData.get_ubntu_regular());
        title_not=(TextView) findViewById(R.id.textView41);
        title_not.setTypeface(objUsefullData.get_ubntu_regular());
        not_found=(LinearLayout)findViewById(R.id.not_found_noti);


        lv=(ListView)findViewById(R.id.listView6_notilist);
        adapter = new Notification_adapter(getParent(), R.layout.row_notification, actorsList);
        lv.setAdapter(adapter);



            if(objUsefullData.isNetworkConnected()) {

                try {

                    Service_response(true,"/user_notification?page=1",false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                objUsefullData.showMsgOnUI("Please check your internet connection and try again");
                if(actorsList.size()==0){
                    not_found.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }else {
                    not_found.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id)
            {

                notifiaction_handler(position);
            }
        });



        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) { // TODO Auto-generated method stub
                int threshold = 1;
                int count = lv.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (lv.getLastVisiblePosition() >= count
                            - threshold) {
                        // Execute LoadMoreDataTask AsyncTask

                        if(load_more) {
                            more++;

                            Service_response(true,"/user_notification?page="+more, true);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }

        });

    }

    private void Service_response(final boolean loader,final String url, final boolean more) {

        if (loader) {
            objUsefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response ,more);

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
                        objUsefullData.showMsgOnUI("Something went wrong");


                    }
                });







    }

    private void set_up_values(JSONObject response, boolean more)
    {
        try {

            if(!more){
                actorsList.clear();
            }

            if(response.isNull("next_page"))
            {
                load_more=false;
            }
            JSONArray comments = response.getJSONArray("notifications");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                String android_controller = in.optString("android_controller");
                String desc = in.optString("desc");
                String time = in.optString("time");
                int post_id = in.optInt("post_id");
                int id = in.optInt("id");
                int child_id = in.optInt("child_id");
                int notification_counts = in.optInt("notification_counts");
                boolean read=in.getBoolean("read");
                boolean merge_feed=in.getBoolean("merge_feed");
                String image_url = in.optString("image_url");
                String site_url=in.optString("site_url");


                Actors actor = new Actors();


                actor.setbuddies(site_url);
                actor.setusername(android_controller);
                actor.setid(post_id);
                actor.setlikes(id);
                actor.settitle(desc);
                actor.setcreated_date(time);
                actor.setpicture(image_url);
                actor.setchild_pic(""+read);
                actor.setdescription(""+merge_feed);
                actor.setlikes2(child_id);


                actorsList.add(actor);

            }

            adapter.notifyDataSetChanged();




        } catch (JSONException e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();


    }

    @Override
    public void onRefresh() {

        if(objUsefullData.isNetworkConnected()) {

            load_more=true;
            more=1;
            Service_response(false,"/user_notification?page=1",false);

            swipeRefreshLayout.setRefreshing(true);
        }else {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
    }


    private void open_noti(int noti_id,int id,final int pos)
    {

        if(!objUsefullData.isNetworkConnected())
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


            UserAPI.post_JsonResp("/new_read_user_notification/"+noti_id, null, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            actorsList.get(pos).setchild_pic("true");
                            notifiaction_handler(pos);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Something went wrong");


                        }
                    });
        }
    }


    public void notifiaction_handler(int position){


        if(actorsList.get(position).getchild_pic().equals("true"))
        {
            switch (actorsList.get(position).getusername())
            {
                case "Feed_invite_page":

                    Intent edit = new Intent(getParent(), Buddies_invite_activity.class);
                    TabGroupActivity parentActivity = (TabGroupActivity)getParent();
                    parentActivity.startChildActivity("noti_budies_Activity", edit, false);

                    break;
                case "Story_invite_page":

                    Intent edit2 = new Intent(getParent(), Stroy_invite_activity.class);
                    TabGroupActivity parentActivity2 = (TabGroupActivity)getParent();
                    edit2.putExtra("story_invite_noti_id",String.valueOf(actorsList.get(position).getlikes2()));
                    parentActivity2.startChildActivity("noti_story_Activity", edit2, false);
                    break;
                case "Feed_post_details":
                    Intent edit3 = new Intent(getParent(), Post_details.class);
                    TabGroupActivity parentActivity3 = (TabGroupActivity)getParent();
                    edit3.putExtra("post_id",String.valueOf(actorsList.get(position).getid()));
                    edit3.putExtra("call", "feed");
                    parentActivity3.startChildActivity("noti_post_Activity", edit3, false);

                    break;
                case "Story_post_details":


                    if(actorsList.get(position).getdescription().equals("true"))
                    {
                        Intent edit3t = new Intent(getParent(), Post_details.class);
                        TabGroupActivity parentActivity3t = (TabGroupActivity)getParent();
                        edit3t.putExtra("post_id",String.valueOf(actorsList.get(position).getid()));
                        edit3t.putExtra("call", "feed");
                        parentActivity3t.startChildActivity("noti_post_Activity", edit3t, false);
                    }else {
                        Intent edit34 = new Intent(getParent(), Post_details.class);
                        TabGroupActivity parentActivity34 = (TabGroupActivity)getParent();
                        edit34.putExtra("post_id",String.valueOf(actorsList.get(position).getid()));
                        edit34.putExtra("call", "story");
                        parentActivity34.startChildActivity("noti_post_Activity", edit34, false);

                    }

                    break;
                case "Playpen_post_details":

                    Intent edit4 = new Intent(getParent(), Playpen_post_detail.class);
                    TabGroupActivity parentActivity4 = (TabGroupActivity)getParent();
                    edit4.putExtra("post_id",String.valueOf(actorsList.get(position).getid()));

                    parentActivity4.startChildActivity("noti_playpen_Activity", edit4, false);
                    break;
                case "Months_details":

                    Intent edit5 = new Intent(getParent(), Months_details.class);
                    TabGroupActivity parentActivity5 = (TabGroupActivity)getParent();
                    edit5.putExtra("month_id",String.valueOf(actorsList.get(position).getid()));

                    parentActivity5.startChildActivity("noti_month_Activity", edit5, false);
                    break;

                case "openWeb":


                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse(actorsList.get(position).getbuddies()));
                    getParent().startActivity(i);

                   break;

                default:

                    objUsefullData.make_toast("Something went wrong!");
                    break;
            }
        }else {
            open_noti(actorsList.get(position).getlikes(),actorsList.get(position).getid(),position);
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        start_call++;
        if (refresh) {
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
