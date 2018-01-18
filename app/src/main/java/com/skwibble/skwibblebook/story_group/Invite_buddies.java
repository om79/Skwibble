package com.skwibble.skwibblebook.story_group;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Invite_buddies extends AppCompatActivity {

    RelativeLayout back,invite;
    UsefullData objUsefullData;
    SaveData save_data;
    TextView share_txt,label_txt;
    String send_txt="";
    Button share;
    TextViewDrawableSize code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_buddies);
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        code=(TextViewDrawableSize) findViewById(R.id.code_xx);
        share=(Button) findViewById(R.id.share_btn);
        share_txt=(TextView) findViewById(R.id.xxxt);
        label_txt=(TextView) findViewById(R.id.textViezw7);

        share_txt.setTypeface(objUsefullData.get_proxima_light());
        code.setTypeface(objUsefullData.get_ubntu_regular());
        share.setTypeface(objUsefullData.get_ubntu_regular());
        label_txt.setTypeface(objUsefullData.get_ubntu_regular());



        back=(RelativeLayout) findViewById(R.id.imagreVkkljfback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });
        invite=(RelativeLayout) findViewById(R.id.zzxx);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                if(!send_txt.equals("")) {
                    objUsefullData.share_options(send_txt);
                }else {
                    objUsefullData.make_toast("Please try again");
                }
            }

        });


        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyToClipboard(code.getText().toString());
            }
        });

        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
        else
        {
            get_share_code();
        }
    }
    private void get_share_code() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );
        Log.e("TAG response", save_data.get(Definitions.user_email));
        Log.e("TAG response", save_data.get(Definitions.auth_token));


        UserAPI.get_JsonObjResp("/get_ref_code", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        try {
                            code.setText(response.optString("ref_code"));
                            send_txt=response.optString("ref_msg");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        objUsefullData.dismissProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Not Found");
                    }
                });







    }
    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("code", copyText);
            clipboard.setPrimaryClip(clip);
        }

        objUsefullData.make_toast("Your invite code has been copied to the clipboard!");

    }

}
