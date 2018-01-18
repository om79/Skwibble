package com.skwibble.skwibblebook.journal_group;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Journal_settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat swt;
    UsefullData objUsefullData;
    SaveData save_data;
    HashMap<String, String> map = new HashMap<String, String>();
    TextViewDrawableSize daily,noti;
    RelativeLayout back;
    LinearLayout daily_layout;
    static Boolean isTouched = false;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_settings);
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        swt = (SwitchCompat)findViewById(R.id.Switch);
        back=(RelativeLayout) findViewById(R.id.imakkl8_fffback);
        daily_layout=(LinearLayout) findViewById(R.id.settzzyout);
        daily=(TextViewDrawableSize) findViewById(R.id.tefgxtView2);
        daily.setTypeface(objUsefullData.get_proxima_light());
        title=(TextView) findViewById(R.id.textVdiew7);
        title.setTypeface(objUsefullData.get_ubntu_regular());
        noti=(TextViewDrawableSize) findViewById(R.id.textView6);
        noti.setTypeface(objUsefullData.get_proxima_light());
        swt.setTypeface(objUsefullData.get_proxima_light());
        swt.setOnCheckedChangeListener (this);
        get_settings();




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                finish();
            }

        });
        daily_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] result = map.keySet().toArray(new String[0]);

                objUsefullData.dismissProgress();
                final Dialog dialog = new Dialog(getParent());
                dialog.setTitle("Select Notification Reminder");
                dialog.setContentView(R.layout.categories);
                final ListView listCategories = (ListView) dialog
                        .findViewById(R.id.listCategories);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getParent(), R.layout.list_category, R.id.textCat,
                        result);
                listCategories.setAdapter(adapter);//
                listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        String cat = (String) listCategories
                                .getItemAtPosition(position);
                        daily.setText(cat);

                        dialog.dismiss();
                        submit_feq(map.get(daily.getText().toString()));
                    }
                });
                dialog.show();
            }

        });

        swt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.Switch:
                Log.e("Switch",""+isChecked);

                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {

                        daily_layout.setEnabled(true);
                        daily.setTextColor(getResources().getColor(R.color.black));
                        daily_layout.setAlpha(.7f);
                        noti_on_off(1,true);
                    }
                    else {
                        daily_layout.setEnabled(false);
                        daily.setTextColor(getResources().getColor(R.color.border_color));
                        daily_layout.setAlpha(.3f);
                        noti_on_off(0,false);
                    }
                }


                break;

            default:
                break;
        }
    }


    private void get_settings() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/edit_jrml_rem_freq", headers, null,
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
                        objUsefullData.make_toast("Something went wrong");
                        finish();
                    }
                });
        }


    private void set_up_values(JSONObject response)
    {
        objUsefullData.dismissProgress();
        try {

            if(response.optBoolean("btn_on_or_off")==true){
                swt.setChecked (true);
                daily_layout.setEnabled(true);
                daily.setTextColor(getResources().getColor(R.color.black));
                daily_layout.setAlpha(.7f);
            }else {
                swt.setChecked (false);
                daily_layout.setEnabled(false);
                daily.setTextColor(getResources().getColor(R.color.border_color));
                daily_layout.setAlpha(.3f);
            }
            JSONArray comments = response.getJSONArray("rem_freq");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean active = in.optBoolean("active");
                if(active==true){
                    daily.setText(name);
                }
                map.put(name,String.valueOf(id));
            }







        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void noti_on_off(int data, final boolean sawt)
    {
        objUsefullData.showProgress();
        if(objUsefullData.isNetworkConnected()==false)
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");

        }
        else
        {

            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put("Accept", Definitions.version);
            headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
            headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

            JSONObject request = new JSONObject();

            try {

                request.put("on_or_off",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());


            UserAPI.custom("/turn_on_or_off_jrnl_noti", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            Log.v("response", ""+response);
                                            swt.setChecked(sawt);
                            if(sawt==true){
                                daily.setTextColor(getResources().getColor(R.color.black));
                            }else {
                                daily.setTextColor(getResources().getColor(R.color.border_color));
                            }
                            daily_layout.setEnabled(sawt);

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            if (sawt == true) {
//                                swt.setChecked(false);
                                daily_layout.setEnabled(false);
                                daily.setTextColor(getResources().getColor(R.color.border_color));
                            } else {
//                                swt.setChecked(true);
                                daily_layout.setEnabled(true);
                                daily.setTextColor(getResources().getColor(R.color.black));
                            }

                        }


//                            NetworkResponse response = error.networkResponse;
//                            if (response != null && response.data != null) {
//                                switch (response.statusCode) {
//
//                                    case 204:
//
//                                        swt.setChecked(sawt);
//                                        break;
//                                    case 500:
//
//                                        if (sawt == true) {
//                                            swt.setChecked(false);
//                                        } else {
//                                            swt.setChecked(true);
//                                        }
//                                        break;
//
//                                }
//                            }
//
//                        }
                    });







        }
        objUsefullData.dismissProgress();

    }



    private void submit_feq(String frq_id)


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
//            JSONObject user = new JSONObject();
            try {
                request.put("rem_freq_id", frq_id);
//                request.put("journal_id", jurnl_id);

//                request.put("child", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/update_jrml_rem_freq", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();




                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {

                                    case 500:

                                        objUsefullData.showMsgOnUI("Something went wrong");
                                        break;
                                    case 201:

                                        objUsefullData.make_toast("Done");
                                        break;

                                }
                            }

                        }
                    });
        }
    }



}
