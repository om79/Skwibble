package com.skwibble.skwibblebook.story_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
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

public class Multiple_child extends AppCompatActivity {

    ListView lv;
    UsefullData objUsefullData;
    child_adapter adapter;
    SaveData save_data;
    LinearLayout add_new,cross;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    TextView title,add_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_child);


        save_data = new SaveData(getParent());
        save_data.remove(Definitions.current_journal_year);
        objUsefullData = new UsefullData(getParent());
        cross=(LinearLayout) findViewById(R.id.imageView_edfit);
        add_new=(LinearLayout)findViewById(R.id.add_new_child);
        lv=(ListView) findViewById(R.id.switch_list);
        title=(TextView) findViewById(R.id.textasVhtiew7);
        add_title=(TextView) findViewById(R.id.textViewws21);
        title.setTypeface(objUsefullData.get_ubntu_regular());
        add_title.setTypeface(objUsefullData.get_ubntu_regular());
        adapter = new child_adapter(getParent(), R.layout.row_show_multiple_child, actorsList);
        lv.setAdapter(adapter);
        get_children_list();

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Add_child.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                parentActivity.startChildActivity("add_child_Activity", edit, false);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id)
            {

                if(objUsefullData.isNetworkConnected()==false)
                {
                    objUsefullData.showMsgOnUI("Please check your internet connection and try again");
                }
                else
                {
                    change_current_child(actorsList.get(position).getid());
                }


            }
        });
    }


    private void get_children_list() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/children", headers, null,
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
            JSONArray comments = response.getJSONArray("childrens");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                Actors actor = new Actors();

                String name = in.optString("name");

                int id=in.optInt("id");

                String image_url = in.optString("image_url");

                boolean active = in.getBoolean("active");


                actor.setpicture(image_url);

                actor.setdescription(name);
                actor.setid(id);
                actor.setcomnt(""+active);

                actorsList.add(actor);

            }



            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();

    }




    public class child_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        UsefullData usefullData;

        public child_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
            usefullData=new UsefullData(context);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.imageviewhoome = (SimpleDraweeView) v.findViewById(R.id.imageView17mm);
                holder.group_name = (TextView) v.findViewById(R.id.textView26mm);
                holder.member_count = (TextView) v.findViewById(R.id.textView25mm);
                holder.back_layer = (LinearLayout) v.findViewById(R.id.back_layermm);
                holder.admin = (TextView) v.findViewById(R.id.textView27mm);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.group_name.setText(actorList.get(position).getdescription());
            holder.member_count.setVisibility(View.GONE);

            holder.group_name.setTypeface(usefullData.get_proxima_regusr());

            holder.admin.setTypeface(usefullData.get_proxima_regusr());

            if(actorList.get(position).getcomnt().equals("true"))
            {
                holder.admin.setText("Active");
                holder.back_layer.setBackgroundColor(getResources().getColor(R.color.medium_trans));

            }else {
                holder.admin.setText("");
                holder.back_layer.setBackgroundColor(0);
            }
            holder.imageviewhoome.setImageURI(actorList.get(position).getpicture());

            return v;

        }

         class ViewHolder {
            public SimpleDraweeView imageviewhoome;
            public TextView group_name,member_count,admin;
            public LinearLayout back_layer;

        }


    }


    private void change_current_child(final int id) {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.post_JsonResp("/change_active_child/"+id, null, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        save_data.save(Definitions.id,id );
                        save_data.save(Definitions.has_first_post, response.optBoolean("has_first_posts"));
                        objUsefullData.dismissProgress();
                        actorsList.clear();
                        finish();


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
