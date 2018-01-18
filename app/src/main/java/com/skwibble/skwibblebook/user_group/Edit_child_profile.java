package com.skwibble.skwibblebook.user_group;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.imagepicker.PickerBuilder;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Edit_child_profile extends Activity {

    RelativeLayout back,add_field;
    EditText name,nick_name,place_of_birth,height_at_birth,weight_at_birth;
    TextView dob,relation,label_update;
    boolean baby=false;
    RadioButton male,female;
    LinearLayout sbling_layout,update,date_alyout,rel_layout;
    UsefullData objUsefullData;
    int child_id;
    Sibling_Adapter adapter;
    ArrayList<Actors> actorList=new ArrayList<Actors>();
    ArrayList<Actors> temp=new ArrayList<Actors>();
    ExpandableHeightListView lv;
    SaveData save_data;
    ArrayList<String> sibling=new ArrayList<String>();
    public static HashMap<String, String> map = new HashMap<String, String>();
    public static HashMap<String, String> map_wt_at = new HashMap<String, String>();
    public static HashMap<String, String> map_ht_at = new HashMap<String, String>();
    public static HashMap<String, String> map_add_sblng = new HashMap<String, String>();
    String compress_pic="no_change";
    SimpleDraweeView pic;
    ToggleButton height_tb_at,weight_tb_at;
    ScrollView scroll;
    TextView lable_name,lable_nick,lable_at_ht,
             lable_place,title,lb_at_wt,basic_info,advance_info,sablg,date_lable,rel_with_user;
    boolean warnimg_popup=true,has_image=false;

    int i=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_profile);

        scroll=(ScrollView) findViewById(R.id.edit_scroll);
        height_tb_at = (ToggleButton) findViewById(R.id.toggleButton61);
        weight_tb_at = (ToggleButton) findViewById(R.id.toggleButton123);
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        save_data.remove(Definitions.current_journal_year);
        height_tb_at.setTypeface(objUsefullData.get_proxima_light());
        weight_tb_at.setTypeface(objUsefullData.get_proxima_light());
        lv=(ExpandableHeightListView ) findViewById(R.id.listview);

        adapter = new Sibling_Adapter(getParent(), R.layout.row_home_new,actorList);
        lv.setAdapter(adapter);
        lv.setExpanded(true);


        date_alyout=(LinearLayout) findViewById(R.id.textView_date);
        rel_layout=(LinearLayout) findViewById(R.id.textView6_relation);
        pic=(SimpleDraweeView) findViewById(R.id.imageView6_img);
        back=(RelativeLayout) findViewById(R.id.imageViewfdvb8_back);
        male=(RadioButton) findViewById(R.id.radio_boy);
        female=(RadioButton) findViewById(R.id.radio_girl);
        sbling_layout=(LinearLayout) findViewById(R.id.sblingdff);
        name=(EditText) findViewById(R.id.editText_child_name);
        nick_name=(EditText) findViewById(R.id.editText_childfdsf_name);
        update=(LinearLayout) findViewById(R.id.imageViewss_edit);
        dob=(TextView) findViewById(R.id.text_date);
        rel_with_user=(TextView) findViewById(R.id.oazzzsdf);
        relation=(TextView) findViewById(R.id.textView6_child_relation);
        add_field=(RelativeLayout) findViewById(R.id.imageView_add);
        date_lable=(TextView) findViewById(R.id.oaaazppsdf);
        place_of_birth=(EditText) findViewById(R.id.editText_cgfdsf_name);
        height_at_birth=(EditText) findViewById(R.id.edext_cgfdsf_name);
        weight_at_birth=(EditText) findViewById(R.id.edextdgfsdgfdsf_name);

        title=(TextView) findViewById(R.id.textViedw7);
        label_update=(TextView) findViewById(R.id.qwqwqw);
        lable_name=(TextView) findViewById(R.id.gfdgdg);
        lable_nick=(TextView) findViewById(R.id.oopppsdf);
        lable_at_ht=(TextView) findViewById(R.id.paawq);
        lable_place=(TextView) findViewById(R.id.ooopa);
        lb_at_wt=(TextView) findViewById(R.id.iisdddk);

        title.setTypeface(objUsefullData.get_ubntu_regular());
        label_update.setTypeface(objUsefullData.get_ubntu_regular());
        lable_name.setTypeface(objUsefullData.get_proxima_regusr());
        lable_nick.setTypeface(objUsefullData.get_proxima_regusr());
        lable_at_ht.setTypeface(objUsefullData.get_proxima_regusr());
        lable_place.setTypeface(objUsefullData.get_proxima_regusr());
        lb_at_wt.setTypeface(objUsefullData.get_proxima_regusr());
        male.setTypeface(objUsefullData.get_proxima_regusr());
        female.setTypeface(objUsefullData.get_proxima_regusr());
        date_lable.setTypeface(objUsefullData.get_proxima_regusr());
        rel_with_user.setTypeface(objUsefullData.get_proxima_regusr());

        name.setTypeface(objUsefullData.get_proxima_light());
        nick_name.setTypeface(objUsefullData.get_proxima_light());
        relation.setTypeface(objUsefullData.get_proxima_light());
        dob.setTypeface(objUsefullData.get_proxima_light());
        place_of_birth.setTypeface(objUsefullData.get_proxima_light());
        height_at_birth.setTypeface(objUsefullData.get_proxima_light());
        weight_at_birth.setTypeface(objUsefullData.get_proxima_light());



        basic_info=(TextView) findViewById(R.id.textVhhiew10);
        advance_info=(TextView) findViewById(R.id.textVvsiew18);
        sablg=(TextView) findViewById(R.id.ppwq);


        basic_info.setTypeface(objUsefullData.get_ubntu_regular());
        advance_info.setTypeface(objUsefullData.get_ubntu_regular());
        sablg.setTypeface(objUsefullData.get_ubntu_regular());

        try {

            get_child_details();

        } catch (Exception e) {
            e.printStackTrace();
        }





        pic.setOnClickListener(new View.OnClickListener() {
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


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objUsefullData.hideKeyboardFrom(getParent(),v);

                name.setError(null);
                nick_name.setError(null);
                String nam = name.getText().toString();
                String nick = nick_name.getText().toString();
                String msg="";


                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(nam)) {
                    name.setError(getString(R.string.error_field_required));
                    focusView = name;
                    focusView.requestFocus();
                    cancel = true;
                    msg="Please fill child name";
                }
                if (TextUtils.isEmpty(nick)) {
                    nick_name.setError(getString(R.string.error_field_required));
                    focusView = nick_name;
                    focusView.requestFocus();
                    cancel = true;
                    msg="Please fill child nickname";
                }

                temp.addAll(actorList);
                for (int i=0;i<temp.size();i++)
                {
                    if(temp.get(i).getdescription().equals("false")) {
                        if (temp.get(i).gettitle().equals("") || temp.get(i).getcreated_date().equals("Sibling Date Of Birth")) {

                            msg = "Please check siblings information";
                            cancel = true;
                        }
                    }
                }

                if (cancel==true) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
//                    focusView.requestFocus();
                    temp.clear();
                    objUsefullData.make_toast(msg);
                } else {

                    if(!nam.equals("")&&!nick.equals("")) {
                        if (nam.charAt(0) == ' ' || nick.charAt(0) == ' ') {
                            objUsefullData.make_toast("Space is not allowed");
                            name.setText(name.getText().toString().trim());
                            name.setSelection(name.getText().length());
                            nick_name.setText(nick_name.getText().toString().trim());
                            nick_name.setSelection(nick_name.getText().length());
                        }
                    }

                    temp.clear();
                    submit_child(child_id);





                }




            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
                Intent edit = new Intent(getParent(), User_feedback.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                parentActivity.startChildActivity("UserActivity", edit, false);
            }

        });
        add_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if(actorList.size()<30) {
                            Actors act = new Actors();

                            act.settitle("");
                            act.setcomnt("Brother");
                            act.setdescription("false");
                            act.setcreated_date("Sibling Date Of Birth");
                            act.setpicture("");
                            act.setchild_pic(map_add_sblng.get("Brother"));


                            actorList.add(act);
                            adapter.notifyDataSetChanged();


                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }else {
                            objUsefullData.make_toast("Maximum sibling limit exceed");
                        }

                    }
                });

            }


        });
        rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_up_relation();
            }
        });
        date_alyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(warnimg_popup==true){
                    warnimg_popup=false;
                    new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Alert!")
                            .setContentText("This will cause all your journal photos to be deleted")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Calendar c = Calendar.getInstance();

                                    SimpleDateFormat sdf = new SimpleDateFormat();

                                    int mYear = c.get(Calendar.YEAR);
                                    int mMonth = c.get(Calendar.MONTH);
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    System.out.println("the selected " + mDay);
                                    sdf.format(c.getTime());
                                    DatePickerDialog dialog = new DatePickerDialog(getParent(),
                                            new mDateSetListener(), mYear, mMonth, mDay);
                                    Date today = new Date();
                                    Calendar c1 = Calendar.getInstance();
                                    c1.setTime(today);
                                    c1.add( Calendar.MONTH, +12 ); // Subtract 6 months
                                    long minDate = c1.getTime().getTime();
                                    dialog.getDatePicker().setMaxDate(minDate);

                                    if(!dob.getText().toString().equals("D.O.B"))
                                    {
                                        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date1;
                                        try {
                                            date1 = new SimpleDateFormat("dd MMMM yyyy").parse(dob.getText().toString());

                                            StringTokenizer tokens = new StringTokenizer(simple.format(date1), "-");
                                            dialog.updateDate(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken())-1,Integer.parseInt(tokens.nextToken()));
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());

                                        }
                                    }



                                    dialog.show();

                                }
                            })
                            .show();
                }else {

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat sdf = new SimpleDateFormat();

                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    System.out.println("the selected " + mDay);
                    sdf.format(c.getTime());
                    DatePickerDialog dialog = new DatePickerDialog(getParent(),
                            new mDateSetListener(), mYear, mMonth, mDay);
                    Date today = new Date();
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(today);
                    c1.add( Calendar.MONTH, +12 ); // Subtract 6 months
                    long minDate = c1.getTime().getTime();
                    dialog.getDatePicker().setMaxDate(minDate);
                    dialog.show();
                }




                          }
        });
    }



    private void get_child_details() {


        objUsefullData.showProgress();



        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.get_JsonObjResp("/children_edit", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values_edit(response);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        objUsefullData.showMsgOnUI("Not Found");
                        objUsefullData.dismissProgress();
                    }
                });







    }

    private void submit_child(final int child_id)
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
            JSONObject user2 = new JSONObject();



            try {
                user.put("name", name.getText().toString());
                user.put("nick_name", nick_name.getText().toString());
                user.put("dob", dob.getText().toString());
                user.put("relation_id", map.get(relation.getText().toString()));
                user.put("gender_id", baby);
                user.put("height_imperial_at_id", map_ht_at.get(height_tb_at.getText().toString()));
                user.put("weight_imperial_at_id", map_wt_at.get(weight_tb_at.getText().toString()));

                user.put("place_of_birth", place_of_birth.getText().toString());
                user.put("height_at_birth", height_at_birth.getText().toString());
                user.put("weight_at_birth", weight_at_birth.getText().toString());

                if(!compress_pic.equals("") && !compress_pic.equals("Remove")&&!compress_pic.equals("no_change")) {
                    user.put("image", compress_pic);
                }else if(compress_pic.equals("Remove")) {

                }else if(compress_pic.equals("no_change")){

                        user.put("image_not_changed", "nil");


                }



                temp.addAll(actorList);
                for (int i=0;i<temp.size();i++)
                {
//                    Log.e("temp_name--",""+temp.get(i).gettitle());
//                    Log.e("temp_destroy--",""+temp.get(i).getdescription());
                    JSONObject user3 = new JSONObject();
                    user3.put("name",temp.get(i).gettitle());
                    user3.put("sibling_id",temp.get(i).getchild_pic());
                    user3.put("_destroy",temp.get(i).getdescription());
                    user3.put("dob",temp.get(i).getcreated_date());
                    user3.put("id",temp.get(i).getpicture());
                    user2.put(""+i,user3);

                }






                user.put("child_siblings_attributes",user2);

                request.put("child", user);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.put_StringResp("/children/"+child_id, request, headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();


                            objUsefullData.firebase_analytics("updateProfile");
                            objUsefullData.make_toast("Your child has been Updated");

                            finish();



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();



                        }
                    });
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_boy:
                if (checked)
                    // Pirates are the best
                    baby=false;

                break;
            case R.id.radio_girl:
                if (checked)
                    // Ninjas rule
                    baby=true;
                break;
        }
    }





    public void get_up_relation() {


        try {

            objUsefullData.dismissProgress();
            final Dialog dialog = new Dialog(getParent());
            dialog.setTitle("Select Relation");
            dialog.setContentView(R.layout.categories);
            final ListView listCategories = (ListView) dialog
                    .findViewById(R.id.listCategories);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getParent(), R.layout.list_category, R.id.textCat,
                    sibling);
            listCategories.setAdapter(adapter);//
            listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                     String cat = (String) listCategories
                            .getItemAtPosition(position);
                        relation.setText(cat);


                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dob.setText(objUsefullData.set_date(dayOfMonth,monthOfYear,year));
            dob.setTextColor(getResources().getColor(R.color.black));

        }
    }

    private void set_up_values_edit(JSONObject response)
    {

        try {



            child_id=response.getJSONObject("children").optInt("id");
            name.setText(response.getJSONObject("children").optString("name"));
            nick_name.setText(response.getJSONObject("children").optString("nick_name"));
            if(response.getJSONObject("children").has("dob")){
                dob.setText(response.getJSONObject("children").optString("dob"));

            }

            place_of_birth.setText(response.getJSONObject("children").optString("place_of_birth",""));
            weight_at_birth.setText(String.valueOf(response.getJSONObject("children").optDouble("weight_at_birth",0)));
            height_at_birth.setText(String.valueOf(response.getJSONObject("children").optDouble("height_at_birth",0)));

            if(place_of_birth.getText().toString().equals("null")){
                place_of_birth.setText("");

            }
            if(weight_at_birth.getText().toString().equals("0.0")){

                weight_at_birth.setText("");
            }
            if(height_at_birth.getText().toString().equals("0.0")){

                height_at_birth.setText("");
            }
            if(nick_name.getText().toString().equals("null")){

                nick_name.setText("");
            }

            if(response.getJSONObject("children").optBoolean("gender_id")==true)
            {

                female.setChecked(true);
                baby=true;

            }else {

                male.setChecked(true);
                baby=false;
            }

            JSONArray weight_imperial_at_id = response.getJSONObject("children").getJSONArray("weight_imperial_at_id");

            for (int i = 0; i < weight_imperial_at_id.length(); i++)
            {
                JSONObject in = weight_imperial_at_id.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean active = in.optBoolean("active");
                map_wt_at.put(name, ""+id);



                if (active==true){
                    weight_tb_at.setTextOn(name);
                    weight_tb_at.setText(name);
                    weight_tb_at.setEnabled(true);
                }else {
                    weight_tb_at.setTextOff(name);
                }

            }


            JSONArray height_imperial_at_id = response.getJSONObject("children").getJSONArray("height_imperial_at_id");

            for (int i = 0; i < height_imperial_at_id.length(); i++)
            {
                JSONObject in = height_imperial_at_id.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean active = in.optBoolean("active");
                map_ht_at.put(name, ""+id);




                if (active==true){
                    height_tb_at.setTextOn(name);
                    height_tb_at.setEnabled(true);
                    height_tb_at.setText(name);
                }else {
                    height_tb_at.setTextOff(name);
                }

            }


            JSONArray comments = response.getJSONObject("children").getJSONArray("relations");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean active = in.optBoolean("active");
                map.put(name, ""+id);
                sibling.add(name);

                if (active==true){
                    relation.setText(""+name);

                }

            }


            JSONArray commentsw = response.getJSONArray("master_siblings");

            for (int i = 0; i < commentsw.length(); i++)
            {
                JSONObject in = commentsw.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");

                map_add_sblng.put(name, ""+id);



            }

            if(!response.getJSONObject("children").isNull("siblings")) {
                JSONArray siblings = response.getJSONObject("children").getJSONArray("siblings");

                for (int i = 0; i < siblings.length(); i++) {


                    JSONObject ins = siblings.getJSONObject(i);


                    Actors act = new Actors();

                    String name = ins.optString("name", "Name");
                    int id = ins.optInt("id");
                    int sibling_id = ins.optInt("sibling_id");
                    String sibling_name = ins.optString("sibling_name", "Select");
                    String dob = ins.optString("dob", "D.O.B");

                    act.settitle(name);
                    act.setcomnt(sibling_name);
                    act.setdescription("false");
                    act.setcreated_date(dob);
                    act.setpicture("" + id);
                    act.setchild_pic("" + sibling_id);


                    actorList.add(act);
                    adapter.notifyDataSetChanged();





                }
            }
            pic.setImageURI(response.getJSONObject("children").optString("image_url"));
            if(response.getJSONObject("children").optString("image_url",null).equals("null")){
                has_image=false;
            }else {
                has_image=true;
            }










        } catch (Exception e) {
            e.printStackTrace();


        }

        scroll.smoothScrollTo(0,0);
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

                                        pic.setImageURI(imageUri);
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
                                        pic.setImageURI(imageUri);
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
                    pic.setImageResource(R.mipmap.sample);

                    compress_pic="Remove";
                    has_image=false;
                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    public class Sibling_Adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actList;
        UsefullData objUsefullData;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;



        public Sibling_Adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actList = objects;
            objUsefullData = new UsefullData(context);
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.remove = (LinearLayout) v.findViewById(R.id.imageView11);
                holder.spin = (TextView) v.findViewById(R.id.textVrelation);
                holder.edit = (EditText) v.findViewById(R.id.editchildame);
                holder.cal = (LinearLayout) v.findViewById(R.id.textiiate);

                holder.cal_txt = (TextView) v.findViewById(R.id.textdfate);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }



                holder.spin.setText(actorList.get(position).getcomnt());
                holder.edit.setText(actorList.get(position).gettitle());

                if(actorList.get(position).getcreated_date().equals("null"))
                {
                    holder.cal_txt.setText("Sibling Date Of Birth");
                    holder.cal_txt.setTextColor(getContext().getResources().getColor(R.color.border_color));
                }else {
                    holder.cal_txt.setText(actorList.get(position).getcreated_date());
                    holder.cal_txt.setTextColor(getContext().getResources().getColor(R.color.black));
                }



                holder.spin.setTypeface(objUsefullData.get_proxima_regusr());
                holder.edit.setTypeface(objUsefullData.get_proxima_regusr());
                holder.cal_txt.setTypeface(objUsefullData.get_proxima_regusr());

                holder.remove.setTag(position);
                holder.cal_txt.setTag(position);
                holder.spin.setTag(position);
                holder.edit.setTag(position);

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    objUsefullData.showProgress();
                    Integer index = (Integer) v.getTag();

                    Actors act=new Actors();

                    act.settitle(actList.get(index.intValue()).gettitle());
                    act.setcomnt(actList.get(index.intValue()).getcomnt());
                    act.setdescription("true");
                    act.setcreated_date(actList.get(index.intValue()).getcreated_date());
                    act.setpicture(actList.get(index.intValue()).getpicture());
                    act.setchild_pic(actList.get(index.intValue()).getchild_pic());




                    temp.add(act);
                    actorList.remove(index.intValue());
                    notifyDataSetChanged();
                    objUsefullData.dismissProgress();


                }
            });




            holder.cal.setOnClickListener(new View.OnClickListener() {

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
                            new mDateSetListener2(), mYear, mMonth, mDay);
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();



                }
                class mDateSetListener2 implements DatePickerDialog.OnDateSetListener {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
//                        String d=String.format("%1$td %1$tB %1$tY", date);
                        String d=objUsefullData.set_date(dayOfMonth,monthOfYear,year);
                        updateListView(position,d,3,"");



                    }
                }



            });


            holder.spin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                        String[] result = map_add_sblng.keySet().toArray(new String[0]);


                        final Dialog dialog = new Dialog(getParent());
                        dialog.setTitle("Select Relation");
                        dialog.setContentView(R.layout.categories);
                        final ListView listCategories = (ListView) dialog
                                .findViewById(R.id.listCategories);
                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(
                                getParent(), R.layout.list_category, R.id.textCat,
                                result);
                        listCategories.setAdapter(adapters);//
                        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int positions, long id) {
                                String cat = (String) listCategories
                                        .getItemAtPosition(positions);



                                updateListView(position,cat,2,""+map_add_sblng.get(cat));



                                dialog.dismiss();
                            }
                        });
                        dialog.show();





                }



            });




            final Handler mHandler = new Handler();
            holder.edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(final Editable s) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.postDelayed(userStoppedTyping, 2000); // 2 second
                }

                Runnable userStoppedTyping = new Runnable() {

                    @Override
                    public void run() {
                        Log.e("----hit---","----");
                        updateListView(position,"",1,"");
                        // user didn't typed for 2 seconds, do whatever you want
                    }
                };
            });




            return v;

        }

        class ViewHolder {

            public TextView spin,cal_txt;
            public EditText edit;
            public LinearLayout cal,remove;

        }
    }




    public boolean updateListView(int position ,String txt,int current,String rel_id) {

        int first = lv.getFirstVisiblePosition();
        int last = lv.getLastVisiblePosition();
        if(position < first || position > last) {
            //just update your DataSet
            //the next time getView is called
            //the ui is updated automatically
            return false;
        }
        else {
            View convertView = lv.getChildAt(position - first);
            //this is the convertView that you previously returned in getView
            //just fix it (for example:)


            if (current == 1) {
                EditText e = (EditText) convertView.findViewById(R.id.editchildame);

                actorList.get(position).settitle(e.getText().toString());
                e.setFocusable(true);

            } else if (current == 2){
                TextView spin = (TextView) convertView.findViewById(R.id.textVrelation);
                spin.setText(txt);
                actorList.get(position).setcomnt(txt);
                actorList.get(position).setchild_pic(rel_id);
            }else if(current==3) {
                TextView d = (TextView) convertView.findViewById(R.id.textdfate);
                d.setText(txt);
                d.setTextColor(getResources().getColor(R.color.black));
                actorList.get(position).setcreated_date(txt);
            }


            return true;
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
