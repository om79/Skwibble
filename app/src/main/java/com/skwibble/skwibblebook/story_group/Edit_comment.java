package com.skwibble.skwibblebook.story_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_comment extends AppCompatActivity {

    TextView user_name;
    EditText cmnt;
    SimpleDraweeView user_pic;
    UsefullData objUsefullData;
    RelativeLayout back;
    SaveData save_data;
    LinearLayout update;
    Intent e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);


        objUsefullData = new UsefullData(getParent());
        save_data = new SaveData(getParent());
        user_name=(TextView) findViewById(R.id.buddweView22);
        cmnt=(EditText) findViewById(R.id.editTdddext2);
        user_pic=(SimpleDraweeView) findViewById(R.id.buzaqs_imageView10);
        back=(RelativeLayout) findViewById(R.id.imsdvb8_back);
        update=(LinearLayout) findViewById(R.id.imageVssiewss_edit);

        e=getIntent();

        user_name.setText(e.getStringExtra("user_name"));
        cmnt.setText(e.getStringExtra("cmnt"));
        user_pic.setImageURI(e.getStringExtra("cmnt"));
//        AQuery aq = new AQuery(getApplicationContext());
//        aq.id(user_pic).image(e.getStringExtra("cmnt"), false, false,50,R.mipmap.feed_default_user);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                finish();

            }

        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String text = cmnt.getText().toString();
                if(!text.equals("")) {
                    if (text.charAt(0) == ' ') {
                        objUsefullData.make_toast("Space is Removed");
                        cmnt.setText(cmnt.getText().toString().trim());
                        cmnt.setSelection(cmnt.getText().length());
                    }
                }

                if(e.getStringExtra("edit_for").equals("playpen"))
                {
                    update_comment_playpen(cmnt.getText().toString(),e.getStringExtra("cmnt_id"));
                }else {
                    update_comment_story(cmnt.getText().toString(),e.getStringExtra("post_id"),e.getStringExtra("cmnt_id"));
                }




            }
        });

    }

    private void update_comment_story(String cmnt,String post_id,String cmnt_id)
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
                user.put("user_id", ""+save_data.getInt(Definitions.id));
                user.put("story_post_id", post_id);
                user.put("content",cmnt);

                request.put("story_post_comment", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.put_StringResp("/story_post_comments/"+cmnt_id, request, headers,null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();
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

    private void update_comment_playpen(String cmnt,String cmnt_id)
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
//                user.put("user_id", ""+save_data.getInt(Definitions.id));
//                user.put("story_post_id", post_id);
                user.put("content",cmnt);

                request.put("playpen_post_comment", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.put_StringResp("playpen_post_comments/"+cmnt_id, request, headers,null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();
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
