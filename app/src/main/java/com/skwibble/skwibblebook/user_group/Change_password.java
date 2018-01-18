package com.skwibble.skwibblebook.user_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_password extends AppCompatActivity {
    RelativeLayout back,chngPswd;
    EditText old,newely,confirm;
    UsefullData objUsefullData;
    SaveData save_data;
    Button save;
    TextView tt,old1,new1,con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        chngPswd=(RelativeLayout) findViewById(R.id.emailQWERtton);
        back=(RelativeLayout) findViewById(R.id.imageView8_fffback);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        tt=(TextView) findViewById(R.id.textsView7);
        old1=(TextView) findViewById(R.id.old);
        new1=(TextView) findViewById(R.id.new1);
        con=(TextView) findViewById(R.id.con);
        save=(Button) findViewById(R.id.emason);
        old=(EditText) findViewById(R.id.editText_chilfgdfgd_name);
        newely=(EditText) findViewById(R.id.editText_qqwwchild_email);
        confirm=(EditText) findViewById(R.id.editText_chippppvcmail);
        old.setTypeface(objUsefullData.get_proxima_regusr());
        newely.setTypeface(objUsefullData.get_proxima_regusr());
        confirm.setTypeface(objUsefullData.get_proxima_regusr());
        tt.setTypeface(objUsefullData.get_ubntu_regular());
        save.setTypeface(objUsefullData.get_ubntu_regular());
        old1.setTypeface(objUsefullData.get_proxima_regusr());
        new1.setTypeface(objUsefullData.get_proxima_regusr());
        con.setTypeface(objUsefullData.get_proxima_regusr());





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                objUsefullData.hideKeyboardFrom(getParent(),v);

                if(newely.getText().toString().equals("")||confirm.getText().toString().equals("")||old.getText().toString().equals("")) {

                    objUsefullData.make_toast("Blank not Allowed");
                }else {


                    if (newely.getText().toString().equals(confirm.getText().toString())) {

                        if(isPasswordValid(confirm.getText().toString())&& isPasswordValid(old.getText().toString()))
                        {
                            chng_password(v);
                        }else{
                            objUsefullData.make_toast(getString(R.string.error_invalid_password));
                        }

                    } else {
                        objUsefullData.make_toast("Passwords are not matching");
                    }
                }


            }

        });

    }


    public void chng_password(final View view)
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
            try {

                user.put("current_password", old.getText().toString());
                user.put("password", newely.getText().toString());
                user.put("confirmation_password", confirm.getText().toString());


                request.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

             UserAPI.put_StringResp("/update_password_details", request, headers,null,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.v("TAG", response);
                                            objUsefullData.dismissProgress();
                                            objUsefullData.make_toast("Your password has been updated");


                                            logout();



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
                                    case 422:

                                        objUsefullData.showMsgOnUI("Your current password is wrong. Please enter correct password");
                                        break;

                                }
                            }

                        }
                    });
        }
    }

    private void logout()
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

            Map<String,String> request = new HashMap<>();

            try {
                request.put("token", save_data.getString(Definitions.firebase_token));

            } catch (Exception e) {
                e.printStackTrace();
            }

            UserAPI.delete_StringResp("/users/sign_out",headers,request,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();

                            save_data.remove(Definitions.auth_token);
                            save_data.remove(Definitions.show_child_form);
                            save_data.remove(Definitions.has_child);
                            save_data.remove(Definitions.has_first_post);
                            save_data.remove(Definitions.facebook_login);
                            save_data.remove(Definitions.badge_count);
                            save_data.remove(Definitions.has_badges);

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);



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


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
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
