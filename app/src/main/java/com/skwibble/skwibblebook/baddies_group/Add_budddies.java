package com.skwibble.skwibblebook.baddies_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.App_startup;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add_budddies extends AppCompatActivity {

    RelativeLayout back,add;
    UsefullData objUsefullData;
    SaveData save_data;
    TextView share_txt,label_txt;
    EditText ref_code,nick_name;
    Button add_btn_txt;
    Context context;
    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budddies);


        i=getIntent();

        if(i.getStringExtra("first_time").equals("true")){

            context=Add_budddies.this;

        }else{
            context=getParent();
        }

        save_data = new SaveData(context);
        objUsefullData = new UsefullData(context);

        add=(RelativeLayout) findViewById(R.id.email_sxaxa_forgot);

        share_txt=(TextView) findViewById(R.id.xxxyt);
        label_txt=(TextView) findViewById(R.id.textViezw7as);

        ref_code=(EditText) findViewById(R.id.editsl_forgot);
        ref_code.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        nick_name=(EditText) findViewById(R.id.editxsrgot);
        add_btn_txt=(Button) findViewById(R.id.emaildforgot);

        ref_code.setTypeface(objUsefullData.get_proxima_regusr());
        nick_name.setTypeface(objUsefullData.get_proxima_regusr());
        add_btn_txt.setTypeface(objUsefullData.get_proxima_regusr());

        share_txt.setTypeface(objUsefullData.get_ubntu_regular());
        label_txt.setTypeface(objUsefullData.get_ubntu_regular());


        back=(RelativeLayout) findViewById(R.id.imagdfback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
               onBackPressed();


            }

        });

        add_btn_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");



                if(ref_code.getText().toString().equals(""))
                {
                    objUsefullData.make_toast("Unique code can't be blank");

                }else if(nick_name.getText().toString().equals("")) {
                    objUsefullData.make_toast("Nickname can't be blank");

                }else{
                    if (ref_code.getText().toString().charAt(0) == ' ') {

                        ref_code.setText(ref_code.getText().toString().trim());
                        ref_code.setSelection(ref_code.getText().length());

                    }
                    if (nick_name.getText().toString().charAt(0) == ' ') {
                        objUsefullData.make_toast("Space is Removed");
                        nick_name.setText(nick_name.getText().toString().trim());
                        nick_name.setSelection(nick_name.getText().length());

                    }
                    if(!ref_code.getText().toString().equals("") && !nick_name.getText().toString().equals(""))
                    {
                        add_buddeis(ref_code.getText().toString(),nick_name.getText().toString());
                    }else {
                        objUsefullData.make_toast("Blank not allowed!");
                    }

                }


            }

        });
    }



    private void add_buddeis(String code,String name)
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


            JSONObject user = new JSONObject();




            try {
                user.put("ref_code", code);
                user.put("nick_name", name);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("TAG", user.toString());


            UserAPI.post_JsonResp("/invite_by_ref", user, headers, null,
                    new Response.Listener<JSONObject>() {
                                        @Override
                    public void onResponse(JSONObject response) {
                            Log.v("TAG", response.toString());
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast(response.optString("success_msg"));
                            ref_code.setText("");
                            nick_name.setText("");

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            String json;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {

                                    case 500:
                                        json = new String(response.data);
                                        json = trimMessage(json, "error_msg");
                                        if(json != null) objUsefullData.showMsgOnUI(json);

                                        break;
                                    default:

                                       objUsefullData.make_toast("Please try again");
                                        break;

                                }
                            }


                        }
                    });
        }
    }


    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(i.getStringExtra("first_time").equals("true")){

            Intent intent = new Intent(context, App_startup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
