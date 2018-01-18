package com.skwibble.skwibblebook.playpen_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.baddies_group.Add_budddies;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.SmoothCheckBox;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class Playpen_baddies_list extends AppCompatActivity {

    UsefullData objUsefullData;
    SaveData save_data;
    boolean header=true;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    HashMap<String, String> map = new HashMap<String, String>();
    Invite_adapter adapter;
    ListView baddies_list;
    RelativeLayout done,back;
    boolean is_admin=false;
    TextView title,done_txt,select_txt;
    LinearLayout not_found,not_found_create_btn;
    TextView create_btn,default_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playpen_baddies_list);

        save_data = new SaveData(getParent());
        done=(RelativeLayout) findViewById(R.id.textaaagwffpost);
        back=(RelativeLayout) findViewById(R.id.imaascgack);


        baddies_list=(ListView)findViewById(R.id.badasdf_list);

        objUsefullData = new UsefullData(getParent());
        title=(TextView) findViewById(R.id.textView7);
        title.setTypeface(objUsefullData.get_ubntu_regular());
        done_txt=(TextView) findViewById(R.id.texuust);
        done_txt.setTypeface(objUsefullData.get_ubntu_regular());
        select_txt=(TextView) findViewById(R.id.textView30);
        select_txt.setTypeface(objUsefullData.get_proxima_light());
        not_found=(LinearLayout)findViewById(R.id.notssund_layout);
        not_found_create_btn=(LinearLayout)findViewById(R.id.not_bztn_laxyout);
        create_btn=(TextView) findViewById(R.id.not_found_ztxt);
        create_btn.setTypeface(objUsefullData.get_proxima_regusr());
        default_txt=(TextView) findViewById(R.id.not_xxxzfand_txt);
        default_txt.setTypeface(objUsefullData.get_proxima_regusr());

        adapter = new Invite_adapter(getParent(), R.layout.row_pending, actorsList);


        final Intent play= getIntent();
        try {
            set_up_values(Playpen_expand_activity.group_info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baddies_list.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                finish();
            }

        });
        done.setVisibility(View.INVISIBLE);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                try {

                    submit_baddies(play.getStringExtra("playpen_id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });


        not_found_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent edit = new Intent(getParent(), Add_budddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                edit.putExtra("first_time","false");
                parentActivity.startChildActivity("buddiinvitesActivity", edit, false);


            }
        });
    }




    private void set_up_values(JSONObject response)
    {
        try {
            Log.e("-response-",response.toString());
            is_admin = response.getBoolean("is_admin");
            if(is_admin==true){
                done.setVisibility(View.VISIBLE);
            }else {
                done.setVisibility(View.INVISIBLE);
            }
            JSONArray buddies = response.getJSONArray("buddies");

            for (int i = 0; i < buddies.length(); i++)
            {
                JSONObject in = buddies.getJSONObject(i);
                Actors actor = new Actors();

                String name = in.getString("name");
                int member_id = in.getInt("member_id");

                String image = in.getString("image");

                boolean active = in.getBoolean("active");
                if(!in.isNull("id")){
                    int id = in.getInt("id");
                    actor.setlikes2(id);
                }else {
                    actor.setlikes2(-1);
                }

                if(is_admin==true) {
                    int child_id = in.optInt("child_id");
                    boolean disable = in.getBoolean("disable");
                    boolean _destroy = in.getBoolean("_destroy");

                    actor.setlikes(child_id);
                    actor.setusername(""+disable);
                    actor.setuser(""+_destroy);
                }else {

                    actor.setlikes(0);
                    actor.setusername(""+false);
                    actor.setuser(""+false);
                }



                actor.setpicture(image);
                actor.setid(member_id);

                actor.setcreated_date("buddies");
                actor.setdescription(name);

                actor.setcomnt(""+active);

                actor.settitle(""+false);
                actor.setchild_pic("");
                actorsList.add(actor);

            }

            if(is_admin==true) {

                JSONArray playpen_buddies = response.getJSONArray("playpen_buddies");

                for (int i = 0; i < playpen_buddies.length(); i++) {
                    JSONObject in = playpen_buddies.getJSONObject(i);
                    Actors actor = new Actors();

                    String name = in.getString("name");
                    int member_id = in.getInt("member_id");
                    if(!in.isNull("id")){
                        int id = in.getInt("id");
                        actor.setlikes2(id);
                    }else {
                        actor.setlikes2(-1);
                    }

                    String image = in.getString("image");
                    String buddy_name = in.getString("buddy_name");
                    int child_id = in.optInt("child_id");
                    boolean active = in.getBoolean("active");

                    boolean disable = in.getBoolean("disable");
                    boolean _destroy = in.getBoolean("_destroy");

                       if(active==true) {

                            actor.setpicture(image);
                            actor.setid(member_id);
                            actor.setcreated_date("playpen");
                            actor.setdescription(name);
                            actor.setlikes(child_id);
                            actor.setcomnt("" + active);
                            actor.setusername("" + disable);
                            actor.setuser("" + _destroy);
                            actor.setchild_pic(buddy_name);

                            if (header == true) {

                                actor.settitle("" + true);
                                header=false;
                            } else {
                                actor.settitle("" + false);
                            }
                            actorsList.add(actor);
                        }

                }
            }


            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();

        if(actorsList.size()==0){
            not_found.setVisibility(View.VISIBLE);
            baddies_list.setVisibility(View.GONE);
        }else {
            not_found.setVisibility(View.GONE);
            baddies_list.setVisibility(View.VISIBLE);
        }
    }

    private void submit_baddies(String id)
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

            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();

            JSONObject att= new JSONObject();
            try {


                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    JSONObject member = new JSONObject();
                    // print if found
                    StringTokenizer tokens = new StringTokenizer(""+pair.getValue(), ",");

                        member.put("member_id", Integer.parseInt(tokens.nextToken()));
                        member.put("child_id", Integer.parseInt(tokens.nextToken()));
                        if (Integer.parseInt(tokens.nextToken()) == -1) {

                        } else {
                            member.put("id", Integer.parseInt(tokens.nextToken()));
                        }

                        if (tokens.nextToken().equals("0")) {
                            member.put("_destroy", true);
                        } else {
                            member.put("_destroy", false);
                        }

                    att.put(""+objUsefullData.randInt(0,999999), member);



                }




//                for (int i=0;i<actorsList.size();i++)
//                {
//
//
//                    if(actorsList.get(i).getlikes2()==-1){
//                        member.put("id", "nil");
//                    }else {
//                        member.put("id", actorsList.get(i).getlikes2());
//                    }
//
//                    member.put("member_id", actorsList.get(i).getid());
//                    member.put("child_id", actorsList.get(i).getlikes());
//                    member.put("_destroy", actorsList.get(i).getuser());
//
//                    att.put(""+objUsefullData.randInt(0,999999), member);
//                }



                user.put("members_attributes",att);
                request.put("playpen", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

           UserAPI.put_StringResp("/playpens/"+id, request, headers,null,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Buddies has been sucessfully updated");
                            finish();



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


                holder.imageview = (SimpleDraweeView) v.findViewById(R.id.imageView10);
                holder.uname=(TextView) v.findViewById(R.id.textView23);
                holder.udisplay_id=(TextView) v.findViewById(R.id.textView22);
                holder.seen=(SmoothCheckBox) v.findViewById(R.id.checkBox4_last);
                holder.playpen_header=(LinearLayout) v.findViewById(R.id.header_playpen);
                holder.overlay_layout=(LinearLayout) v.findViewById(R.id.overlay_layout);

                holder.uname.setTypeface(objUsefullData.get_proxima_light());

                holder.uname.setTextColor(getContext().getResources().getColor(R.color.black));



                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.uname.setText(actorList.get(position).getdescription());

            if(actorList.get(position).getcomnt().equals("true"))
            {
                holder.seen.setChecked(true);
            }else{
                holder.seen.setChecked(false);
            }

            if(actorList.get(position).getusername().equals("true")){

                holder.overlay_layout.setAlpha(.4f);
                holder.seen.setEnabled(false);
            }else {
                holder.overlay_layout.setAlpha(1f);
                holder.seen.setEnabled(true);
            }

            holder.imageview.getLayoutParams().width=objUsefullData.screen_size()*4;
            holder.imageview.getLayoutParams().height=objUsefullData.screen_size()*4;
            holder.imageview.setImageURI(actorList.get(position).getpicture());
            if(actorList.get(position).gettitle().equals("true")){
                holder.playpen_header.setVisibility(View.VISIBLE);

            }else {
                holder.playpen_header.setVisibility(View.GONE);
            }

            if(actorList.get(position).getcreated_date().equals("playpen")){
                holder.udisplay_id.setVisibility(View.VISIBLE);
                holder.udisplay_id.setText(actorList.get(position).getchild_pic());

            }else {
                holder.udisplay_id.setVisibility(View.GONE);
            }

//            if(actorList.get(position).getcreated_date().equals("playpen")){
//                holder.playpen_header.setVisibility(View.VISIBLE);
//
//            }else {
//                holder.playpen_header.setVisibility(View.GONE);
//            }


            if(is_admin==true)
            {
                holder.seen.setVisibility(View.VISIBLE);
            }else{
                holder.seen.setVisibility(View.INVISIBLE);
            }


            holder.seen.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                    done.setVisibility(View.VISIBLE);
                    if (isChecked == true) {

                        map.put(String.valueOf(position), ""+actorList.get(position).getid()+","+actorList.get(position).getlikes()+","+actorList.get(position).getlikes2()+","+actorList.get(position).getlikes2()+","+1);
                    } else {
//                          actorList.get(position).setuser("true");
//                        Log.e("uncheck","--");
//                        map.remove(String.valueOf(position));
//                        map.put(String.valueOf(position), ""+actorList.get(position).getid()+","+actorList.get(position).getlikes());
                        map.put(String.valueOf(position), ""+actorList.get(position).getid()+","+actorList.get(position).getlikes()+","+actorList.get(position).getlikes2()+","+actorList.get(position).getlikes2()+","+0);

                    }


                }
            });






            return v;

        }



        class ViewHolder {
            public SimpleDraweeView imageview;
            public SmoothCheckBox seen;
            public TextView uname,udisplay_id;
            public LinearLayout playpen_header,overlay_layout;


        }







    }
}
