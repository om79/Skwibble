package com.skwibble.skwibblebook.journal_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.user_group.Add_child;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.GridViewAdaterForAttachementMenu;
import com.skwibble.skwibblebook.utility.MenuPopupWindowItems;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class Journal_activitry extends AppCompatActivity {

    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    Journal_adapter adapter;
    GridView gv;
    UsefullData objUsefullData;
    RelativeLayout setting;
    SaveData save_data;
    LinearLayout years,not_found,not_add_child;
    public  TextView current_year_txt,title,not_found_txt,not_child_txt;
    private PopupWindow popupWindow;
    List<MenuPopupWindowItems> list = new ArrayList<MenuPopupWindowItems>();
    ImageView rotate_view;
    boolean refresh=false;
    int start_call=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_layout);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        if(!save_data.isExist(Definitions.show_journal)) {
            objUsefullData.showpopup(R.mipmap.show_journal,Definitions.show_journal);
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
        rotate_view=(ImageView) findViewById(R.id.rotate_view);
        setting=(RelativeLayout) findViewById(R.id.indddiew10);
        years=(LinearLayout) findViewById(R.id.yesass);
        current_year_txt=(TextView) findViewById(R.id.current_year);
        title=(TextView) findViewById(R.id.textdddView7);
        not_found_txt=(TextView) findViewById(R.id.not_ddsound_txt);
        not_child_txt=(TextView) findViewById(R.id.not_ffdtxt);
        not_found=(LinearLayout)findViewById(R.id.not_sd_xxxlayout);
        not_add_child=(LinearLayout)findViewById(R.id.not_xxx_layout);
        current_year_txt.setTypeface(objUsefullData.get_proxima_regusr());
        title.setTypeface(objUsefullData.get_proxima_regusr());
        not_child_txt.setTypeface(objUsefullData.get_proxima_regusr());
        not_found_txt.setTypeface(objUsefullData.get_proxima_regusr());
        gv=(GridView)findViewById(R.id.lvuxsadsdser);

        adapter = new Journal_adapter(getParent(), R.layout.row_journal, actorsList);

        gv.setAdapter(adapter);
        if(!save_data.getBoolean(Definitions.has_child)){
            years.setVisibility(View.INVISIBLE);
            setting.setVisibility(View.INVISIBLE);
            if(actorsList.size()==0){
                not_found.setVisibility(View.VISIBLE);
                gv.setVisibility(View.GONE);
            }else {
                not_found.setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
            }
        }else {
            years.setVisibility(View.VISIBLE);
            setting.setVisibility(View.VISIBLE);
            not_found.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
            if(!save_data.isExist(Definitions.current_journal_year))
            {
                get_journal(true,"2017");

            }else {
                get_journal(false,save_data.getString(Definitions.current_journal_year));
            }


        }


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id)
            {

                if(actorsList.get(position).gettitle().equals("true")) {
                    Intent edit = new Intent(getParent(), Months_details.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    edit.putExtra("month_id", String.valueOf(actorsList.get(position).getid()));
                    parentActivity.startChildActivity("journal_months_Activity", edit, false);
                }
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Journal_settings.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();

                parentActivity.startChildActivity("journal_settings_Activity", edit, false);
            }

        });

        years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pop_up_intial();




            }




        });

        not_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Add_child.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                parentActivity.startChildActivity("add_child_Activity", edit, false);
            }
        });


    }

    private void pop_up_intial() {
        rotate_view.animate().rotation(180).start();
        current_year_txt.setVisibility(View.GONE);
        LayoutInflater layoutInflater  = (LayoutInflater) getParent().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.trans));
        popupWindow.setOutsideTouchable(true);


        popupWindow.showAsDropDown((TextView) findViewById(R.id.current_year));
        ListView mGridView = (ListView) popupView.findViewById(R.id.listView_pop);
        mGridView.setLongClickable(false);


        GridViewAdaterForAttachementMenu gridViewAdaterForAttachementMenu = new GridViewAdaterForAttachementMenu(getParent(), list);
        mGridView.setAdapter(gridViewAdaterForAttachementMenu);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                rotate_view.animate().rotation(360).start();
                current_year_txt.setText(list.get(i).getMenu_text());
                current_year_txt.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                get_journal(false,list.get(i).getMenu_text());



            }


        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //Do Something here
                rotate_view.animate().rotation(360).start();

                current_year_txt.setVisibility(View.VISIBLE);
            }
        });

    }


    private void get_journal(boolean start,String year)
    {

        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
            save_data.save(Definitions.current_journal_year,"2017");
            if(actorsList.size()==0){
                not_found.setVisibility(View.GONE);
                gv.setVisibility(View.GONE);
            }else {
                not_found.setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            objUsefullData.showProgress();
            current_year_txt.setText(year);
            save_data.save(Definitions.current_journal_year,year);
            //Define Headers


            Map<String,String> headers = new HashMap<>();
            headers.put( "Accept", Definitions.version );

            headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
            headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );
            JSONObject request = new JSONObject();
            if(!start) {
                try {

                    request.put("year", year);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            UserAPI.post_JsonResp("/show_current_journal", request, headers, null,
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



                        }
                    });
        }
    }


    private void set_up_values(JSONObject response)
    {



        try {

            actorsList.clear();
            list.clear();
            JSONArray current_year = response.getJSONArray("current_year");

            for (int i = 0; i < current_year.length(); i++)
            {
                JSONObject in = current_year.getJSONObject(i);


                String year_name = in.optString("year_name");
//                boolean active_year = in.optBoolean("active_year");
//                int id = in.getInt("id");
//                boolean active=in.optBoolean("active");

                list.add(new MenuPopupWindowItems(year_name));
//                items.add(year_name);
//                if(active ==true) {
//                    current_year_txt.setText(year_name);
//                    selected_yr=i;
//                    save_data.save(Definitions.current_journal_year,year_name);
//
//                }

            }



            JSONArray comments = response.getJSONArray("months");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);

                Actors actor = new Actors();
                String name = in.optString("month_name");
                boolean active_month = in.optBoolean("active_month");
                int id = in.getInt("id");
                boolean attach=in.optBoolean("has_attachment");
                if(attach ==true) {
                String image = in.getJSONObject("attachment").optString("thumb",null);
                    actor.setpicture(image);
                }else {
                    actor.setpicture("null");
                }

                    actor.setid(id);
                    actor.setdescription(name);
                    actor.settitle(""+active_month);

                    actorsList.add(actor);
            }


            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();

    }
    @Override
    public void onPause() {
        super.onPause();
        if(start_call>1) {
            refresh = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        start_call++;
        if(refresh){
            if(objUsefullData.isNetworkConnected()) {

                if(!save_data.isExist(Definitions.current_journal_year))
                {
                    get_journal(true,"2017");

                }else {
                    get_journal(false,save_data.getString(Definitions.current_journal_year));
                }

            }else {
                objUsefullData.showMsgOnUI("Please check your internet connection and try again");
            }
        }
    }
}



