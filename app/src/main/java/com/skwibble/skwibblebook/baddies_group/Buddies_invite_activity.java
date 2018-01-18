package com.skwibble.skwibblebook.baddies_group;

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
import com.skwibble.skwibblebook.story_group.Actors;
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

public class Buddies_invite_activity extends AppCompatActivity {

    ExpandableHeightListView accept_list;
    UsefullData objUsefullData;
    SaveData save_data;
    ArrayList<Actors> actorsList_accept=new ArrayList<Actors>();
    Invite_adapter adapter_accept;
    RelativeLayout back,invite;
    TextView title,demo_txt,acc_txt,label_add_new_buddies,demo_txt_bottom,label_share;
    LinearLayout acc_lay,add_new_buddies,share_lay,show_lay;
    boolean normal_back= true;
    ScrollView main_view;
    String share_msg;
    boolean refresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);


        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        main_view=(ScrollView) findViewById(R.id.invite_scrollview);
        acc_lay=(LinearLayout) findViewById(R.id.first_hide_layoutbb);
        show_lay=(LinearLayout) findViewById(R.id.show_layoutbb);
        acc_txt=(TextView) findViewById(R.id.acccnotfound_txtbb);
        acc_txt.setTypeface(objUsefullData.get_proxima_light());

        invite=(RelativeLayout) findViewById(R.id.imageView_ffeditbb);

        title=(TextView) findViewById(R.id.textView7bb);
        title.setText("Following");
        title.setTypeface(objUsefullData.get_ubntu_regular());
        label_share=(TextView) findViewById(R.id.label_add_new_buddiesdebb);
        demo_txt=(TextView) findViewById(R.id.dempo_txtbb);
        demo_txt.setText(R.string.following_new_top);
        demo_txt.setTypeface(objUsefullData.get_proxima_light());
        label_share.setTypeface(objUsefullData.get_ubntu_regular());

        demo_txt_bottom=(TextView) findViewById(R.id.dempo_txtdebb);
        demo_txt_bottom.setText(R.string.following_new_bottom);
        demo_txt_bottom.setTypeface(objUsefullData.get_proxima_light());

        label_add_new_buddies=(TextView) findViewById(R.id.label_add_new_buddiesbb);
        label_add_new_buddies.setTypeface(objUsefullData.get_ubntu_regular());
        add_new_buddies=(LinearLayout) findViewById(R.id.add_new_buddiesbb);

        add_new_buddies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Add_budddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                edit.putExtra("first_time","false");
                parentActivity.startChildActivity("buddiinvitesActivity", edit,false);
            }
        });
        share_lay=(LinearLayout) findViewById(R.id.add_new_buddiesdebb);

        share_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(share_msg.equals("")){
                    objUsefullData.share_options("Hey! Have you seen Skwibble? It's a great way to record your child's memories. Check it out: https://skwibble.com");
                }else {
                    objUsefullData.share_options(share_msg);
               }
            }
        });

        back=(RelativeLayout) findViewById(R.id.imageVifffew8_backbb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                onBackPressed();
            }

        });

        accept_list = (ExpandableHeightListView) findViewById(R.id.accept_listbb);
        adapter_accept = new Invite_adapter(getParent(), R.layout.row_feed_invite, actorsList_accept);
        accept_list.setExpanded(true);
        accept_list.setAdapter(adapter_accept);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Add_budddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                edit.putExtra("first_time","false");
                parentActivity.startChildActivity("buddiinvitesActivity", edit,false);
            }

        });

        service();

    }

    public void service(){
        if(objUsefullData.isNetworkConnected()){
            get_list();

        }else {
            objUsefullData.make_toast("Please check your internet connection and try again");
            if(actorsList_accept.size()==0){
                acc_lay.setVisibility(View.VISIBLE);
                show_lay.setVisibility(View.GONE);
            }else {
                acc_lay.setVisibility(View.GONE);
                show_lay.setVisibility(View.VISIBLE);
            }
        }
    }

    private void get_list() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/invite_requests", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);
                        if(actorsList_accept.size()==0){
                            acc_lay.setVisibility(View.VISIBLE);
                            show_lay.setVisibility(View.GONE);
                        }else {
                            acc_lay.setVisibility(View.GONE);
                            show_lay.setVisibility(View.VISIBLE);
                        }
                        main_view.setVisibility(View.VISIBLE);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        save_data.save(Definitions.current_noti_request,"");
                        objUsefullData.dismissProgress();
                        objUsefullData.make_toast("Not Found");
                        if(actorsList_accept.size()==0){
                            acc_lay.setVisibility(View.VISIBLE);
                            show_lay.setVisibility(View.GONE);
                        }else {
                            acc_lay.setVisibility(View.GONE);
                            show_lay.setVisibility(View.VISIBLE);
                        }
                        main_view.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void set_up_values(JSONObject response)
    {
        try {


            actorsList_accept.clear();
            share_msg=response.getString("android_share_msg");
            JSONArray following = response.getJSONArray("following");

            for (int i = 0; i < following.length(); i++)
            {
                JSONObject in = following.getJSONObject(i);
                Actors actor_following = new Actors();
                String child_name = in.getString("child_name");
                String user_image_url = in.getString("child_image_url");
                int id = in.optInt("id");
                String nick_name = in.getString("nick_name");


                actor_following.setdescription(user_image_url);
                actor_following.setcreated_date(nick_name);
                actor_following.settitle(child_name);
                actor_following.setid(id);


                actorsList_accept.add(actor_following);
            }






        } catch (JSONException e) {
            e.printStackTrace();
        }




        adapter_accept.notifyDataSetChanged();
        objUsefullData.dismissProgress();

        if(save_data.getString(Definitions.current_noti_request).equals("Feed_invite_page")){

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
                v = vi.inflate(Resource, null);


                holder.imageview = (SimpleDraweeView) v.findViewById(R.id.story_imageView10_feed);

                holder.uname=(TextView) v.findViewById(R.id.story_textView23_feed);
                holder.udisplay_id=(TextView) v.findViewById(R.id.story_textView22_feed);
                holder.accept_btn=(Button) v.findViewById(R.id.acc_bt_feed);
                holder.reject_btn=(Button) v.findViewById(R.id.rej_bt_feed);

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

                holder.accept_btn.setVisibility(View.VISIBLE);
                holder.reject_btn.setVisibility(View.GONE);
                holder.accept_btn.setText("Unfollow");





            holder.imageview.setImageURI(actorList.get(position).getdescription());



            holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Unfollow Buddy")
                            .setContentText("Are you sure you want to unfollow this buddy?")
                            .setCancelText("cancel")
                            .setConfirmText("Remove")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    unfollow(String.valueOf(actorList.get(position).getid()), "/unfollow_buddy");
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

    private void unfollow(String id,String url)
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

                    user.put("invite_request_id", id);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", user.toString());

            UserAPI.post_JsonResp(url, user, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();


                            actorsList_accept.clear();

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
            intent.putExtra("from",3);
            startActivity(intent);
//            finish();

        }else{

//            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(refresh) {

            service();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        refresh=true;
    }

}
