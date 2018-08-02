package com.skwibble.skwibblebook.user_group;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.imagepicker.PickerBuilder;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_child extends AppCompatActivity  {


    EditText child_name,nickname;
    SimpleDraweeView img;
    ArrayList<String> sibling=new ArrayList<String>();
    LinearLayout bod,relation;
    TextView date,child_relation,title;
    UsefullData objUsefullData;
    Button submit;
    RadioButton boy_btn,girl_btn;
    SaveData save_data;
    boolean has_image=false;
    int i=1;
    HashMap<String, String> map = new HashMap<String, String>();
    String baby="false",compress_pic;
    RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        // Get the application context

        save_data = new SaveData(getParent());
        // Get the Resources

        objUsefullData = new UsefullData(getParent());
        save_data.remove(Definitions.current_journal_year);



        if(!save_data.isExist(Definitions.show_add_child)) {

            objUsefullData.showpopup(R.mipmap.show_add_child,Definitions.show_add_child);
        }




        child_name=(EditText) findViewById(R.id.editText_child_name);
        nickname=(EditText) findViewById(R.id.editText2_nickname);
        img=(SimpleDraweeView) findViewById(R.id.imageView6_img);
        bod=(LinearLayout) findViewById(R.id.textView_date);
        relation=(LinearLayout) findViewById(R.id.textView6_relation);
        date=(TextView) findViewById(R.id.text_date);
        title=(TextView) findViewById(R.id.textVhtiew7);
        child_relation=(TextView) findViewById(R.id.textView6_child_relation);
        submit=(Button) findViewById(R.id.button2);

        child_name.setTypeface(objUsefullData.get_proxima_light());
        nickname.setTypeface(objUsefullData.get_proxima_light());
        date.setTypeface(objUsefullData.get_proxima_light());
        child_relation.setTypeface(objUsefullData.get_proxima_light());
        submit.setTypeface(objUsefullData.get_ubntu_regular());
        title.setTypeface(objUsefullData.get_ubntu_regular());

        boy_btn=(RadioButton) findViewById(R.id.radio_boy);
        girl_btn=(RadioButton) findViewById(R.id.radio_girl);
        boy_btn.setTypeface(objUsefullData.get_ubntu_regular());
        girl_btn.setTypeface(objUsefullData.get_ubntu_regular());




        back=(RelativeLayout) findViewById(R.id.imaqscback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });
        bod.setOnClickListener(new View.OnClickListener() {
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
                Date today = new Date();
                Calendar c1 = Calendar.getInstance();
                c1.setTime(today);
                c1.add( Calendar.MONTH, +12 ); // Subtract 6 months
                long minDate = c1.getTime().getTime();
                dialog.getDatePicker().setMaxDate(minDate);
                dialog.show();


            }
        });
        if (objUsefullData.isNetworkConnected()) {
            get_relation();
        }else {
            objUsefullData.make_toast("Please check your internet connection and try again");
        }

        relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    oper_relations(sibling);



            }
        });

   submit.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {



           nickname.setError(null);
           child_name.setError(null);
           String name = child_name.getText().toString();
           String nick = nickname.getText().toString();
           String dat = date.getText().toString();
           String rel = child_relation.getText().toString();
           String msg="Please check your field";
           boolean cancel = false;
           View focusView = null;


           if (rel.equals("Relation with child")) {

               msg="Please fill in the relation with child";
               cancel = true;
           }

           if (dat.equals("D.O.B / Due Date")) {

               msg="Please fill in the Date of Birth";
               cancel = true;
           }


           if (TextUtils.isEmpty(nick)) {
               nickname.setError(getString(R.string.error_field_required));
               focusView = nickname;
               focusView.requestFocus();
               cancel = true;
               msg="Please fill in the child's nickname";
           }

           if (TextUtils.isEmpty(name)) {
               child_name.setError(getString(R.string.error_field_required));
               focusView = child_name;
               focusView.requestFocus();
               cancel = true;
               msg="Please fill in the child's name";
           }



           if (cancel==false) {
               // There was an error; don't attempt login and focus the first
               // form field with an error.
               String text = child_name.getText().toString();
               String text2 = nickname.getText().toString();
               if(!text.equals("")&&!text2.equals("")) {
                   if (text.charAt(0) == ' ' || text2.charAt(0) == ' ') {
                       objUsefullData.make_toast("Space is Removed");
                       child_name.setText(child_name.getText().toString().trim());
                       child_name.setSelection(child_name.getText().length());
                       nickname.setText(nickname.getText().toString().trim());
                       nickname.setSelection(nickname.getText().length());
                   }
               }

               if(!child_name.getText().toString().equals("") && !nickname.getText().toString().equals(""))
               {
                   submit_child();
               }else {
                   objUsefullData.make_toast(msg);
               }





           } else {
               objUsefullData.make_toast(msg);

           }



       }
   });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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





    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
//            Date dates = new Date(year - 1900, monthOfYear, dayOfMonth);
//            String d=String.format("%1$td %1$tB %1$tY", dates);
            date.setText(objUsefullData.set_date(dayOfMonth,monthOfYear,year));
            date.setTextColor(getResources().getColor(R.color.black));


        }


    }



    private void get_relation() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.get_JsonObjResp("/children/new", headers, null,
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_boy:
                if (checked)

                baby="false";

                    break;
            case R.id.radio_girl:
                if (checked)

                    baby="true";
                    break;
        }
    }




    private void set_up_values(JSONObject response)
    {
        try {
            sibling.clear();
            JSONArray comments = response.getJSONArray("relations");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                map.put(name,""+id);
                sibling.add(name);
            }





            objUsefullData.dismissProgress();




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void oper_relations(ArrayList<String> s) {

        final Dialog dialog = new Dialog(getParent());
        dialog.setTitle("Select Relation");
        dialog.setContentView(R.layout.categories);
        final ListView listCategories = (ListView) dialog
                .findViewById(R.id.listCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getParent(), R.layout.list_category, R.id.textCat,
                s);

        listCategories.setAdapter(adapter);//
        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                String cat = (String) listCategories
                        .getItemAtPosition(position);
                child_relation.setText(cat);
                child_relation.setTextColor(getResources().getColor(R.color.black));

                dialog.dismiss();
            }
        });
        dialog.show();

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
                            .setCropScreenColor(getResources().getColor(R.color.Dark))
                            .start();




                } else if (items[item].equals("Choose from Library")) {

                    i++;
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
                            .setImageName("test"+i)
                            .setImageFolderName("testFolder"+i)
                            .setCropScreenColor(getResources().getColor(R.color.Dark))
                            .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                                @Override
                                public void onPermissionRefused() {

                                }
                            })
                            .start();
                } else if (items[item].equals("Remove Image")) {
                    img.setImageResource(R.mipmap.sample);

                    compress_pic="Remove";
                    has_image=false;
                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    private void submit_child()
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
                user.put("name", child_name.getText().toString());
                user.put("nick_name", nickname.getText().toString());
                user.put("dob", date.getText().toString());
                user.put("relation_id", map.get(child_relation.getText().toString()));
                user.put("gender_id", baby);
                try {
                    user.put("image", compress_pic);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.put("child", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/children", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            objUsefullData.dismissProgress();

                            objUsefullData.make_toast("Your child has been added");
                            save_data.save(Definitions.has_child, response.optBoolean("has_child"));
                            save_data.save(Definitions.has_first_post, response.optBoolean("has_first_posts"));


                            objUsefullData.firebase_analytics("multipleChildAdd");

                            Intent intent = new Intent(getParent(), Tab_activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Please try again");

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
