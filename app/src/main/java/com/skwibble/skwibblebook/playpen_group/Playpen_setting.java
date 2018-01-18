package com.skwibble.skwibblebook.playpen_group;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.imagepicker.PickerBuilder;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Playpen_setting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    SimpleDraweeView playpen_pic;
    UsefullData objUsefullData;
    SaveData save_data;
    RelativeLayout update,back;
    Button delete_playpen;
    Intent play;
    EditText play_name;
    static Boolean isTouched = false,has_image=false;
    String compress_pic="no_change";
    SwitchCompat swt;
    TextViewDrawableSize noti_txt;
    TextView update_lable,title;

    LinearLayout disable_lay;
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playpen_setting);


        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        disable_lay=(LinearLayout) findViewById(R.id.disable_layout);
        update_lable=(TextView) findViewById(R.id.uff);
        title=(TextView) findViewById(R.id.textView7);
        update=(RelativeLayout) findViewById(R.id.textVjdjygwffpost);
        back=(RelativeLayout) findViewById(R.id.imaqelggack);
        playpen_pic=(SimpleDraweeView) findViewById(R.id.imageViewss6_img);
        delete_playpen=(Button) findViewById(R.id.emaghfgfhn);
        noti_txt=(TextViewDrawableSize) findViewById(R.id.textView6);
        delete_playpen.setTypeface(objUsefullData.get_ubntu_regular());
        update_lable.setTypeface(objUsefullData.get_ubntu_regular());
        title.setTypeface(objUsefullData.get_ubntu_regular());
        noti_txt.setTypeface(objUsefullData.get_proxima_regusr());
        swt = (SwitchCompat)findViewById(R.id.Switch_playpen);
        swt.setOnCheckedChangeListener (this);
        swt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });
        play_name=(EditText) findViewById(R.id.textView31);
        play_name.setTypeface(objUsefullData.get_proxima_regusr());
        play= getIntent();
        try {
            swt.setChecked (play.getBooleanExtra("notification_status",false));
            play_name.setText(play.getStringExtra("playpen_name"));
            delete_playpen.setText(play.getStringExtra("delete_txt"));

            if(play.getStringExtra("delete_txt").equals("Exit Playpen"))
            {
                play_name.setEnabled(false);
                playpen_pic.setEnabled(false);
                disable_lay.setAlpha(.3f);
            }
            if (play.getStringExtra("playpen_pic").equals("null")) {
                has_image=false;
            }else {
                has_image=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        playpen_pic.setImageURI(play.getStringExtra("playpen_pic"));

        delete_playpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playpen_delete(play.getStringExtra("member_id"));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                String text = play_name.getText().toString();
                if(!text.equals("")) {
                    if (text.charAt(0) == ' ') {
                        objUsefullData.make_toast("Space is Removed");
                        play_name.setText(play_name.getText().toString().trim());
                        play_name.setSelection(play_name.getText().length());
                    }
                }
         submit_baddies(play.getStringExtra("playpen_id"));
            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });


        playpen_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ask.on(getParent()).forPermissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withRationales("In order to save file you will need to grant storage permission") //optional
                        .go();


                if (has_image==false) {
                    // Its visible
                    CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel"};
                    selectImage(items);
                } else {
                    // Either gone or invisible

                    CharSequence[] items_edit = {"Take Photo", "Choose from Library", "Remove Image", "Cancel"};
                    selectImage(items_edit);
                }
            }
        });

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("Sample", "click");
        switch (buttonView.getId()) {

            case R.id.Switch_playpen:



                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        noti_on_off(play.getStringExtra("playpen_id"),1,true);
                    }
                    else {
                        noti_on_off(play.getStringExtra("playpen_id"),0,false);
                    }
                }

                break;

            default:
                break;
        }
    }



    private void selectImage(final CharSequence[] items) {
        i++;
        AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    new PickerBuilder(getParent(), PickerBuilder.SELECT_FROM_CAMERA)
                            .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                                @Override
                                public void onImageReceived(Uri imageUri) {
                                    try {

                                        compress_pic=objUsefullData.BitMapToString(objUsefullData.getBitmap(imageUri));

                                        playpen_pic.setImageURI(imageUri);
                                        has_image=true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setImageName("testImage"+i)
                            .setImageFolderName("testFolder"+i)
                            .withTimeStamp(false)
                            .setCropScreenColor(getParent().getResources().getColor(R.color.Dark))
                            .start();




                } else if (items[item].equals("Choose from Library")) {


                    new PickerBuilder(getParent(), PickerBuilder.SELECT_FROM_GALLERY)
                            .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                                @Override
                                public void onImageReceived(Uri imageUri) {

                                    try {

                                        compress_pic=objUsefullData.BitMapToString(objUsefullData.getBitmap(imageUri));
//                                        playpen_pic.setImageBitmap(objUsefullData.getBitmap(imageUri));
                                        playpen_pic.setImageURI(imageUri);
                                        has_image=true;

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .setImageName("testImage"+i)
                            .setImageFolderName("testFolder"+i)
                            .setCropScreenColor(getParent().getResources().getColor(R.color.Dark))
                            .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                                @Override
                                public void onPermissionRefused() {

                                }
                            })
                            .start();
                } else if (items[item].equals("Remove Image")) {
                    playpen_pic.setImageResource(R.mipmap.create_playpen_pic);
                    has_image=false;
                    compress_pic="Remove";
                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

            try {
                user.put("name", play_name.getText().toString());
                try {

                    if(!compress_pic.equals("") && !compress_pic.equals("Remove")&&!compress_pic.equals("no_change")) {
                        user.put("image", compress_pic);
                    }else if(compress_pic.equals("Remove")) {

                    }else if(compress_pic.equals("no_change")){

                        user.put("image", "nil");


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


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
                            objUsefullData.make_toast("Hooray! Your Playpen settings have been updated!");
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

    private void playpen_delete(String post_id)
    {

        if(!objUsefullData.isNetworkConnected())
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

            UserAPI.delete_StringResp("/playpen_members/"+post_id,headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();
//
//                            finish();
                            Intent edit = new Intent(getParent(), Playpen_show.class);
                            TabGroupActivity parentActivity = (TabGroupActivity)getParent();


                            parentActivity.startChildActivity("playpen_show_Activity", edit, false);



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





    private void noti_on_off(String id,int data, final boolean sawt)
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
                request.put("id",id);
                request.put("on_or_off",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());


            UserAPI.custom("/turn_on_or_off_playpen_noti", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            Log.v("response", ""+response);
                            swt.setChecked(sawt);


                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {



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
