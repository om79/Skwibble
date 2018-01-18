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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.SmoothCheckBox;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class who_liked extends AppCompatActivity {

    UsefullData objUsefullData;
    SaveData save_data;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    likeshow_adapter adapter;
    ListView baddies_list;
    RelativeLayout back;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_liked);


        save_data = new SaveData(getParent());

        back=(RelativeLayout) findViewById(R.id.imaascgack);

        baddies_list=(ListView)findViewById(R.id.liked_list);

        objUsefullData = new UsefullData(getParent());
        title=(TextView) findViewById(R.id.textView7);

        title.setTypeface(objUsefullData.get_ubntu_regular());

        adapter = new likeshow_adapter(getParent(), R.layout.row_pending, actorsList);
        final Intent play= getIntent();
        try {
            if(objUsefullData.isNetworkConnected()==false)
            {
                objUsefullData.showMsgOnUI("Please check your internet connection and try again");
            }
            else
            {
                get_like_details(play.getStringExtra("id"));
            }

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
    }

    private void get_like_details(String id) {



            objUsefullData.showProgress();


        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/show_story_post_likes/"+id, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                            objUsefullData.dismissProgress();

                        objUsefullData.showMsgOnUI("Not Found");
                    }
                });

    }


    private void set_up_values(JSONObject response)
    {
        try {


            JSONArray comments = response.getJSONArray("likes");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                Actors actor = new Actors();

                String name = in.getString("name");
                String time = in.getString("time");
                String image = in.getString("image_url");




                actor.setpicture(image);
                actor.setdescription(name);
                actor.setcomnt(time);

                actorsList.add(actor);

            }



            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();
    }


    public class likeshow_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;

        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        Context mcontext;


        public likeshow_adapter(Context context, int resource, ArrayList<Actors> objects) {
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
                holder.seen.setVisibility(View.INVISIBLE);
                holder.uname.setTextColor(getResources().getColor(R.color.black));
                holder.udisplay_id.setTextColor(getResources().getColor(R.color.black));



                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.uname.setText(actorList.get(position).getdescription());
            holder.udisplay_id.setText(actorList.get(position).getcomnt());
            holder.uname.setTypeface(objUsefullData.get_proxima_light());
            holder.udisplay_id.setTypeface(objUsefullData.get_proxima_light());
//            holder.uname.setTextSize(objUsefullData.screen_size()-5);
//            holder.udisplay_id.setTextSize(objUsefullData.screen_size()-10);


            holder.imageview.getLayoutParams().width=objUsefullData.screen_size()+70;
            holder.imageview.getLayoutParams().height=objUsefullData.screen_size()+70;

            holder.imageview.setImageURI(actorList.get(position).getpicture());










            return v;

        }



        class ViewHolder {
            public SimpleDraweeView imageview;
            public SmoothCheckBox seen;
            public TextView uname,udisplay_id;


        }







    }
}
