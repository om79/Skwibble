package com.skwibble.skwibblebook.story_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Stroy_invite_activity extends AppCompatActivity {


    ExpandableHeightListView pending_list,accept_list;
    UsefullData objUsefullData;
    SaveData save_data;
    ArrayList<Actors> actorsList_pending=new ArrayList<Actors>();
    ArrayList<Actors> actorsList_accept=new ArrayList<Actors>();
    Invite_adapter adapter_pndng,adapter_accept;
    LinearLayout pen_lay,acc_lay,add_buddy;
    RelativeLayout back,invite_buddies;
    TextView title,demo_txt,pnding_label,accept_label,acc_txt,pen_txt,label_add_new_buddies;
    boolean normal_back= true;
    ScrollView main_view;
    boolean refresh=false;
    Intent intent1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_invite_layout);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        intent1=getIntent();
        Bundle extras = intent1.getExtras();
        if (extras != null) {
            if (extras.containsKey("current_event_msg")) {
                if(save_data.isExist(Definitions.current_event_msg)) {
                    objUsefullData.showMsgOnUI(extras.getString("current_event_msg"));
                    save_data.remove(Definitions.current_event_msg);
                }
            }
            if (extras.containsKey("story_invite_noti_id")) {

            }
        }
        main_view=(ScrollView) findViewById(R.id.story_invite_view);
        label_add_new_buddies=(TextView) findViewById(R.id.label_add_new_buddiesde);
        label_add_new_buddies.setTypeface(objUsefullData.get_ubntu_regular());
        pnding_label=(TextView) findViewById(R.id.textView41);
        accept_label=(TextView) findViewById(R.id.textView4);

        pnding_label.setTypeface(objUsefullData.get_proxima_light());
        accept_label.setTypeface(objUsefullData.get_proxima_light());

        acc_lay=(LinearLayout) findViewById(R.id.acc_not_found);
        pen_lay=(LinearLayout) findViewById(R.id.pend_not_found);
        add_buddy=(LinearLayout) findViewById(R.id.add_new_buddiesde);
        acc_txt=(TextView) findViewById(R.id.acccnotfound_txt);
        pen_txt=(TextView) findViewById(R.id.notfound_txt);

        acc_txt.setText("Oops! You haven\'t sent any invites anyone yet. Click the \n button below to start sharing "+save_data.getString(Definitions.child_name)+"\'s Story");
        pen_txt.setText(R.string.story_pndng_txt);
        acc_txt.setTypeface(objUsefullData.get_proxima_light());
        pen_txt.setTypeface(objUsefullData.get_proxima_light());

        title=(TextView) findViewById(R.id.textView7);
        demo_txt=(TextView) findViewById(R.id.dempo_txt);
        demo_txt.setText(R.string.follower_txt);
        demo_txt.setTypeface(objUsefullData.get_proxima_light());
        title.setText("Followers");
        title.setTypeface(objUsefullData.get_ubntu_regular());
        accept_list = (ExpandableHeightListView) findViewById(R.id.accept_list);
        pending_list = (ExpandableHeightListView) findViewById(R.id.pending_list);
        pending_list.setExpanded(true);
        accept_list.setExpanded(true);

        back=(RelativeLayout) findViewById(R.id.imageVifffew8_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");


                onBackPressed();

            }

        });
        invite_buddies=(RelativeLayout) findViewById(R.id.imageView_ffedit);
        invite_buddies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Invite_buddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                parentActivity.startChildActivity("invite_buddies_storyActivity", edit, false);
            }

        });
        add_buddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Invite_buddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();

                parentActivity.startChildActivity("invite_buddies_storyActivity", edit, false);
            }
        });


        adapter_pndng = new Invite_adapter(getParent(), R.layout.strory_row_pending, actorsList_pending);
        adapter_accept = new Invite_adapter(getParent(), R.layout.strory_row_pending, actorsList_accept);
        pending_list.setAdapter(adapter_pndng);
        accept_list.setAdapter(adapter_accept);

        get_list();

    }




    private void get_list() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        String url="/story_posts_buddy";
        Bundle extras = intent1.getExtras();
        if (extras != null) {

            if (extras.containsKey("story_invite_noti_id")) {

                url="/story_posts_buddy/"+extras.getString("story_invite_noti_id");

            }
        }


        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        set_up_values(response);

                        if(actorsList_pending.size()==0){
                            pen_lay.setVisibility(View.VISIBLE);
                        }else {
                            pen_lay.setVisibility(View.GONE);
                        }
                        if(actorsList_accept.size()==0){
                            acc_lay.setVisibility(View.VISIBLE);
                        }else {
                            acc_lay.setVisibility(View.GONE);
                        }
                        main_view.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        save_data.save(Definitions.current_noti_request,"");
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Not Found");
                        if(actorsList_pending.size()==0){
                            pen_lay.setVisibility(View.VISIBLE);
                        }else {
                            pen_lay.setVisibility(View.GONE);
                        }
                        if(actorsList_accept.size()==0){
                            acc_lay.setVisibility(View.VISIBLE);
                        }else {
                            acc_lay.setVisibility(View.GONE);
                        }
                        main_view.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void set_up_values(JSONObject response)
    {
        try {
            actorsList_accept.clear();
            actorsList_pending.clear();

            JSONArray pending = response.getJSONArray("pending");

            for (int i = 0; i < pending.length(); i++)
            {
                JSONObject in = pending.getJSONObject(i);
                Actors actor = new Actors();
                String user_name = in.optString("user_name");
                String user_image_url = in.optString("user_image_url");
                String invited_email = in.optString("invited_email");
                String nick_name = in.optString("nick_name");
                int id = in.optInt("id");

                if(!user_name.equals("null")){
                    actor.settitle(user_name);
                }else {
                    actor.settitle(invited_email);
                }

                actor.setdescription(user_image_url);
                actor.setcreated_date(nick_name);
                actor.setid(id);
                actor.setlikes(0);
                actorsList_pending.add(actor);



            }


            JSONArray following = response.getJSONArray("accepted");

            for (int i = 0; i < following.length(); i++)
            {
                JSONObject in = following.getJSONObject(i);
                Actors actor_following = new Actors();
                String user_name = in.optString("user_name");
                String user_image_url = in.optString("user_image_url");
                int id = in.optInt("id");
                String nick_name = in.optString("nick_name");


                actor_following.setdescription(user_image_url);
                actor_following.setcreated_date(nick_name);
                actor_following.settitle(user_name);
                actor_following.setid(id);
                actor_following.setlikes(1);
                actorsList_accept.add(actor_following);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        adapter_pndng.notifyDataSetChanged();
        adapter_accept.notifyDataSetChanged();
        objUsefullData.dismissProgress();

        if(save_data.getString(Definitions.current_noti_request).equals("Story_invite_page")){

            save_data.save(Definitions.current_noti_request,"");
            normal_back=false;

        }


    }



    public class Invite_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;

        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        Context mcontext;


        public Invite_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
            mcontext=context;

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);//ideashub.india@gmail.com


                holder.imageview = (SimpleDraweeView) v.findViewById(R.id.story_imageView10);
                holder.uname=(TextView) v.findViewById(R.id.story_textView23);
                holder.udisplay_id=(TextView) v.findViewById(R.id.story_textView22);
                holder.accept_btn=(Button) v.findViewById(R.id.acc_bt);
                holder.reject_btn=(Button) v.findViewById(R.id.rej_bt);

                holder.uname.setTypeface(objUsefullData.get_proxima_light());
                holder.udisplay_id.setTypeface(objUsefullData.get_proxima_light());
                holder.accept_btn.setTypeface(objUsefullData.get_ubntu_regular());
                holder.reject_btn.setTypeface(objUsefullData.get_ubntu_regular());




                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.uname.setText(actorList.get(position).gettitle());
            holder.udisplay_id.setText(actorList.get(position).getcreated_date());
            if(actorList.get(position).getlikes()==0)
            {
                holder.accept_btn.setVisibility(View.VISIBLE);
                holder.reject_btn.setVisibility(View.VISIBLE);
                holder.accept_btn.setText("Accept");

            }else{
                holder.accept_btn.setVisibility(View.VISIBLE);
                holder.reject_btn.setVisibility(View.GONE);
                holder.accept_btn.setText("Remove");

            }



            holder.imageview.setImageURI(actorList.get(position).getdescription());

            holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.accept_btn.getText().toString().equals("Accept")) {
                        accept_reject(String.valueOf(actorList.get(position).getid()), actorList.get(position).getcreated_date(),"/follow_buddy",true);
                    }else{
                        accept_reject(String.valueOf(actorList.get(position).getid()), actorList.get(position).getcreated_date(),"/unfollow_buddy",false);
                    }
                }
            });
            holder.reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Remove Buddy")
                            .setContentText("Are you sure you want to remove this buddy?")
                            .setCancelText("cancel")
                            .setConfirmText("Remove")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();


                                    objUsefullData.firebase_analytics("removeBuddy");


                                    accept_reject(String.valueOf(actorList.get(position).getid()), actorList.get(position).getcreated_date(),"/unfollow_buddy",false);

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();


                                }
                            })
                            .show();
                }
            });

            return v;

        }



        class ViewHolder {
            public SimpleDraweeView imageview;
            public Button accept_btn,reject_btn;
            public TextView uname,udisplay_id,seen;
        }
    }

    private void accept_reject(String id,String nick,String url,boolean follow)
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



            JSONObject user = new JSONObject();
            try {
                if(follow) {
                    user.put("invite_request_id", id);
                    user.put("nick_name", nick);
                    user.put("version_two", true);
                }else{
                    user.put("invite_request_id", id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", user.toString());

            UserAPI.post_JsonResp(url, user, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            get_list();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Something went wrong!");

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!normal_back){
            Intent intent = new Intent(getParent(), Tab_activity.class);
            intent.putExtra("from",0);
            startActivity(intent);
//          finish();
        }else{
//          finish();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(refresh) {

            get_list();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        refresh=true;
    }

}
