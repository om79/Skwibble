package com.skwibble.skwibblebook.user_group;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class User_setting extends Activity {

    TextView chn_pawsd,date,tt,email,lable_name,lable_email,lable_dob;
    LinearLayout date_click;
    SimpleDraweeView img;
    RelativeLayout back,save_user;
    EditText name;
    UsefullData objUsefullData;
    SaveData save_data;
    Button save;
    String compress_pic="";
    boolean has_image=false;
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        chn_pawsd=(TextView) findViewById(R.id.textView20);
        save=(Button) findViewById(R.id.emdfff_button);
        tt=(TextView) findViewById(R.id.textView7);
        date=(TextView) findViewById(R.id.text_date_qqqe);
        date_click=(LinearLayout) findViewById(R.id.textView_date);
        email = (TextView) findViewById(R.id.editTextqqq_child_email);
        name = (EditText) findViewById(R.id.editTeggggxt_child_name);
        img = (SimpleDraweeView) findViewById(R.id.imageView6user_img);
        save_user = (RelativeLayout) findViewById(R.id.email_saveffff_button);
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        lable_dob=(TextView) findViewById(R.id.ddd);
        lable_email=(TextView) findViewById(R.id.jjj);
        lable_name=(TextView) findViewById(R.id.kkk);

        chn_pawsd.setTypeface(objUsefullData.get_proxima_regusr());
        date.setTypeface(objUsefullData.get_proxima_light());
        email.setTypeface(objUsefullData.get_proxima_light());
        name.setTypeface(objUsefullData.get_proxima_light());
        tt.setTypeface(objUsefullData.get_ubntu_regular());
        save.setTypeface(objUsefullData.get_ubntu_regular());
        lable_dob.setTypeface(objUsefullData.get_ubntu_regular());
        lable_email.setTypeface(objUsefullData.get_ubntu_regular());
        lable_name.setTypeface(objUsefullData.get_ubntu_regular());


        chn_pawsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!save_data.getBoolean(Definitions.facebook_login)) {
                    Intent edit = new Intent(getParent(), Change_password.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    parentActivity.startChildActivity("change_password_Activity", edit, false);
                }else {
                    objUsefullData.showMsgOnUI("Sorry! you login from facebook");
                }
            }
        });
        back=(RelativeLayout) findViewById(R.id.imageView8_baddck);

        date_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat();

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                sdf.format(c.getTime());
                DatePickerDialog dialog = new DatePickerDialog(getParent(),
                        new mDateSetListener(), mYear, mMonth, mDay);


                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

//                if(!date.getText().toString().equals("D.O.B"))
//                {
                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1;
                    try {
                        date1 = new SimpleDateFormat("dd MMMM yyyy").parse(date.getText().toString());

                        StringTokenizer tokens = new StringTokenizer(simple.format(date1), "-");
                        dialog.updateDate(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken())-1,Integer.parseInt(tokens.nextToken()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());

                    }
//                }
                dialog.show();
            }
        });




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

                if(!TextUtils.isEmpty(name.getText().toString())) {
                        name.setText(name.getText().toString().trim());
                        name.setSelection(name.getText().length());

                    if(date.getText().toString().equals("dd MMMM yyyy"))
                    {
                        objUsefullData.make_toast("Please fill in the Date of Birth");

                    }else {
                        submit_user(v);
                    }

                }else {
                    objUsefullData.make_toast("Please enter user name");
                }



            }

        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ask.on(getParent()).forPermissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withRationales("In order to save file you will need to grant storage permission") //optional
                        .go();

                if (!has_image) {
                    // Its visible
                    CharSequence[] items = { "Take Photo","Choose from Library", "Cancel"};
                    selectImage(items);
                } else {
                    // Either gone or invisible

                    CharSequence[] items_edit = {"Take Photo","Choose from Library", "Remove Image", "Cancel"};
                    selectImage(items_edit);
                }


            }
        });
        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
        else
        {
            get_user();
        }

    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date.setText(objUsefullData.set_date(dayOfMonth,monthOfYear,year));
            date.setTextColor(getResources().getColor(R.color.black));

        }
    }

    public void submit_user(final View view)
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

            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();
            try {

                user.put("name", name.getText().toString());
                user.put("dob", date.getText().toString());
                user.put("id", String.valueOf(save_data.getInt(Definitions.id)));
                if(!compress_pic.equals("")&& !compress_pic.equals("Remove")) {
                    user.put("image", compress_pic);
                }else if(compress_pic.equals("Remove")){

                }else{
                    user.put("image", "nil");
                }
                request.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.put_StringResp("/users", request, headers,null,
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
                            Snackbar.make(view, "Please try again", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    });
        }
    }




    private void get_user() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/user_details", headers, null,
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

            name.setText(""+response.getJSONObject("user").optString("name","Name"));
            email.setText(""+response.getJSONObject("user").optString("email","Email"));

            if(response.getJSONObject("user").optString("dob","null").equals("null")){
                date.setText("D.O.B");
                date.setTextColor(getResources().getColor(R.color.border_color));
            }else {
                date.setText(""+response.getJSONObject("user").optString("dob","dd MMMM yyyy"));
                date.setTextColor(getResources().getColor(R.color.black));


            }

            if(response.getJSONObject("user").optString("image_url", null).equals("null")){
                has_image=false;
            }else {
                has_image=true;
            }
            img.setImageURI(response.getJSONObject("user").optString("image_url", null));



        } catch (JSONException e) {
            e.printStackTrace();
        }
        objUsefullData.dismissProgress();

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

                                        img.setImageURI(imageUri);
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
                                        img.setImageURI(imageUri);
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
                    img.setImageResource(R.mipmap.user_default_icon);

                    compress_pic="Remove";
                    has_image=false;
                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
