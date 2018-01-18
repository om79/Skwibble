package com.skwibble.skwibblebook.user_group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
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

public class Child_profile extends Activity {

    RelativeLayout back,setting;
    ExpandableHeightGridView sbling_layout;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    TextView name,nick_name,gender,dob,relation,place_of_birth,height_at_birth,weight_at_birth;
    SimpleDraweeView pic;
    UsefullData objUsefullData;
    TextView height_at_param,weight_at_parm,title,basic_info,advance_info,sablg;
    SaveData save_data;
    TextView lable_name,lable_dob,lable_nick,lable_gender,lable_relation,lable_place;
    Child_adapter adapter;
    ScrollView child_sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        objUsefullData = new UsefullData(getParent());

        save_data = new SaveData(getParent());


        if(!save_data.isExist(Definitions.show_child_profile)) {

            objUsefullData.showpopup(R.mipmap.show_child_profile,Definitions.show_child_profile);
        }

        setting=(RelativeLayout) findViewById(R.id.imageView8_ass);
        back=(RelativeLayout) findViewById(R.id.imageViewfdvb8_back);
        child_sv=(ScrollView) findViewById(R.id.child_scroll);


        height_at_param=(TextView) findViewById(R.id.textVdddiew1623);
        weight_at_parm=(TextView) findViewById(R.id.textViewewr164);
        height_at_param.setTypeface(objUsefullData.get_proxima_regusr());
        weight_at_parm.setTypeface(objUsefullData.get_proxima_regusr());

        pic=(SimpleDraweeView) findViewById(R.id.imageView6_img_child);

        name=(TextView) findViewById(R.id.textView11);
        nick_name=(TextView) findViewById(R.id.textView12);
        gender=(TextView) findViewById(R.id.textView13);
        dob=(TextView) findViewById(R.id.textView14);
        relation=(TextView) findViewById(R.id.textView15);

        place_of_birth=(TextView) findViewById(R.id.textView1645t);
        height_at_birth=(TextView) findViewById(R.id.textView1623);
        weight_at_birth=(TextView) findViewById(R.id.textView164);


        name.setTypeface(objUsefullData.get_proxima_regusr());
        nick_name.setTypeface(objUsefullData.get_proxima_regusr());
        gender.setTypeface(objUsefullData.get_proxima_regusr());
        dob.setTypeface(objUsefullData.get_proxima_regusr());
        relation.setTypeface(objUsefullData.get_proxima_regusr());

        place_of_birth.setTypeface(objUsefullData.get_proxima_regusr());
        height_at_birth.setTypeface(objUsefullData.get_proxima_regusr());
        weight_at_birth.setTypeface(objUsefullData.get_proxima_regusr());


        lable_name=(TextView) findViewById(R.id.dddsfsdferyjhgjm);
        lable_dob=(TextView) findViewById(R.id.dddtextView14);
        lable_nick=(TextView) findViewById(R.id.textddView12);
        lable_gender=(TextView) findViewById(R.id.textddddView12);
        lable_relation=(TextView) findViewById(R.id.textdddfew12);
        lable_place=(TextView) findViewById(R.id.textdddqqq2);

        lable_name.setTypeface(objUsefullData.get_proxima_regusr());
        lable_dob.setTypeface(objUsefullData.get_proxima_regusr());
        lable_nick.setTypeface(objUsefullData.get_proxima_regusr());
        lable_gender.setTypeface(objUsefullData.get_proxima_regusr());
        lable_relation.setTypeface(objUsefullData.get_proxima_regusr());
        lable_place.setTypeface(objUsefullData.get_proxima_regusr());


        title=(TextView) findViewById(R.id.textView7);
        basic_info=(TextView) findViewById(R.id.textView10);
        advance_info=(TextView) findViewById(R.id.textView18);
        sablg=(TextView) findViewById(R.id.textView18frswgf);

        title.setTypeface(objUsefullData.get_ubntu_regular());
        basic_info.setTypeface(objUsefullData.get_ubntu_regular());
        advance_info.setTypeface(objUsefullData.get_ubntu_regular());
        sablg.setTypeface(objUsefullData.get_ubntu_regular());




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();

            }

        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                Intent edit = new Intent(getParent(), User_feedback.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                parentActivity.startChildActivity("UserActivity", edit, false);
            }

        });

        sbling_layout=(ExpandableHeightGridView) findViewById(R.id.sublings_layout);
        adapter = new Child_adapter(getParent(), R.layout.row_child_sbling, actorsList);
        sbling_layout.setExpanded(true);
        sbling_layout .setAdapter(adapter);
        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
        else
        {
            if (save_data.getBoolean(Definitions.has_child)) {
                get_child_details();
            }
        }



    }


    private void get_child_details() {


        objUsefullData.showProgress();



        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.get_JsonObjResp("/children_edit", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                                set_up_values(response);





                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        objUsefullData.make_toast("Not Found");
                        objUsefullData.dismissProgress();
                    }
                });







    }
    private void set_up_values(JSONObject response)
    {

        try {

            String s=response.getJSONObject("children").optString("name","");
            if(s.length() > 10)
            { s = s.substring(0, 10) + "..";
            }

            name.setText(""+response.getJSONObject("children").optString("name",""));
            title.setText(s+"\'s Profile");
            nick_name.setText(""+response.getJSONObject("children").optString("nick_name",""));
            if(response.getJSONObject("children").has("dob")){
                dob.setText(""+response.getJSONObject("children").optString("dob",""));
            }
            place_of_birth.setText(response.getJSONObject("children").optString("place_of_birth",""));
            weight_at_birth.setText(String.valueOf(response.getJSONObject("children").optDouble("weight_at_birth",0)));
            height_at_birth.setText(String.valueOf(response.getJSONObject("children").optDouble("height_at_birth",0)));

            if(place_of_birth.getText().toString().equals("null")){
                place_of_birth.setVisibility(View.GONE);
            }
            if(weight_at_birth.getText().toString().equals("0.0")){
                weight_at_birth.setVisibility(View.GONE);
            }
            if(height_at_birth.getText().toString().equals("0.0")){
                height_at_birth.setVisibility(View.GONE);
            }
            if(nick_name.getText().toString().equals("null")){
                nick_name.setVisibility(View.GONE);
            }


            if(response.getJSONObject("children").optBoolean("gender_id")==true)
            {
                gender.setText("Girl");

            }else {
                gender.setText("Boy");

            }

            JSONArray comments = response.getJSONObject("children").getJSONArray("relations");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name","Name");
                int id = in.optInt("id");
                boolean active = in.optBoolean("active");
                if (active==true){
                    relation.setText(""+name);
                }
            }



            if(!response.getJSONObject("children").isNull("height_imperial_at_id")) {
                JSONArray height_imperial_at_id = response.getJSONObject("children").getJSONArray("height_imperial_at_id");

                for (int i = 0; i < height_imperial_at_id.length(); i++) {
                    JSONObject in = height_imperial_at_id.getJSONObject(i);
                    String name = in.optString("name");
//                int id = in.getInt("id");
                    boolean active = in.optBoolean("active");
                    if (active == true) {
                        height_at_param.setText("Height at birth (" + name + ")");

                    }

                }
            }


            if(!response.getJSONObject("children").isNull("weight_imperial_at_id")) {
                JSONArray weight_imperial_at_id = response.getJSONObject("children").getJSONArray("weight_imperial_at_id");

                for (int i = 0; i < weight_imperial_at_id.length(); i++) {
                    JSONObject in = weight_imperial_at_id.getJSONObject(i);
                    String name = in.optString("name");
//                int id = in.getInt("id");
                    boolean active = in.optBoolean("active");
                    if (active == true) {
                        weight_at_parm.setText("Weight at birth (" + name + ")");

                    }

                }

            }



            if(!response.getJSONObject("children").isNull("siblings")) {
                JSONArray siblings = response.getJSONObject("children").getJSONArray("siblings");

                for (int i = 0; i < siblings.length(); i++) {

                    JSONObject ins = siblings.getJSONObject(i);
                    Actors actor = new Actors();


                    String name = ins.optString("name");
                    String sibling_name = ins.optString("sibling_name");
                    String dob = ins.optString("dob");

                    actor.setdescription(name);
                    actor.setcreated_date(sibling_name);
                    actor.setcomnt(dob);

                    actorsList.add(actor);


                }
                adapter.notifyDataSetChanged();
            }


            pic.setImageURI(response.getJSONObject("children").optString("image_url"));





            child_sv.smoothScrollTo(0,0);

        } catch (JSONException e) {
            e.printStackTrace();


        }
        objUsefullData.dismissProgress();

    }



}
