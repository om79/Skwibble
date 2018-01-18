package com.skwibble.skwibblebook.journal_group;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Show_image extends AppCompatActivity {

    UsefullData objUsefullData;
    SaveData save_data;
    int current;
    String current_id;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_pic_view);
        objUsefullData = new UsefullData(getParent());
        save_data = new SaveData(getParent());

        final TextView txt = (TextView) findViewById(R.id.image_date);
        final TextView edit_text = (TextView) findViewById(R.id.edit_text);
        final TextView date_text = (TextView) findViewById(R.id.textsdd_date);
        final LinearLayout edit = (LinearLayout) findViewById(R.id.imageVddiew_edit);
        final LinearLayout date = (LinearLayout) findViewById(R.id.textViesssw_date);
        final RelativeLayout delete = (RelativeLayout) findViewById(R.id.submit_buttonmm);
        final Button btn = (Button) findViewById(R.id.email_savrrtton);

        btn.setTypeface(objUsefullData.get_proxima_regusr());

        i = getIntent();

        txt.setTypeface(objUsefullData.get_chalkduster());
        edit_text.setTypeface(objUsefullData.get_proxima_regusr());
        date_text.setTypeface(objUsefullData.get_proxima_regusr());
        txt.setText(time_stamp(i.getStringExtra("date")));
        date_text.setText(time_stamp(i.getStringExtra("date")));
        current_id = i.getStringExtra("id");

        final SimpleDraweeView pic = (SimpleDraweeView) findViewById(R.id.snappy_image_viewer);
        final ImageView thumb = (ImageView) findViewById(R.id.video_symbol);
        RelativeLayout cross = (RelativeLayout) findViewById(R.id.imaqscback);
        if(i.getStringExtra("is_video").equals("true")){
            pic.setImageURI(i.getStringExtra("thumb"));
            thumb.setVisibility(View.VISIBLE);

        }else {
            pic.setImageURI(i.getStringExtra("pic"));
            thumb.setVisibility(View.GONE);
        }






        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i.getStringExtra("is_video").equals("true")) {
                    String video_url=i.getStringExtra("pic");
                    Intent i = new Intent(getParent(), Videoplayer.class);
                    i.putExtra("url",video_url);
                    getParent().startActivity(i);
                }else {
                    try {
                        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(getResources())
                                .setFailureImage(R.drawable.warning_sigh)
                                .setProgressBarImage(R.drawable.progress_bar_states);

                        List<String> lst= new ArrayList<String>();
                        lst.add(i.getStringExtra("pic"));
                        new ImageViewer.Builder(getParent(), lst)
                                .setStartPosition(0)
                                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)

                                .show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });



        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
//                image_delete(current_id);

                if (btn.getText().toString().equals("Update")) {

                    submit_pic(date_text.getText().toString(), i.getStringExtra("id"));

                } else {
                    btn.setText("Update");

                    txt.setVisibility(View.GONE);
                    date.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }

            }

        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Delete this image !")
                        .setCancelText("No, cancel !")
                        .setConfirmText("Yes, delete it !")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                image_delete(current_id);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();


                            }
                        })
                        .show();

            }
        });

        Log.e("--min_date--",""+i.getIntExtra("min_date",2015));

        date.setOnClickListener(new View.OnClickListener() {
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

                if(i.getIntExtra("min_date",2017) > mYear)
                {

                    Date min = new Date(mYear - 1900, 0, 1);
                    dialog.getDatePicker().setMinDate(min.getTime());

                    if (!date_text.getText().toString().equals("D.O.B")) {
                        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1;
                        try {
                            date1 = new SimpleDateFormat("dd MMMM yyyy").parse(date_text.getText().toString());

                            StringTokenizer tokens = new StringTokenizer(simple.format(date1), "-");
                            dialog.updateDate(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()) - 1, Integer.parseInt(tokens.nextToken()));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());

                        }
                    }
                }else {
                    Date min = new Date(i.getIntExtra("min_date", 2017) - 1900, 0, 1);
                    dialog.getDatePicker().setMinDate(min.getTime());

                    if (!date_text.getText().toString().equals("D.O.B")) {
                        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1;
                        try {
                            date1 = new SimpleDateFormat("dd MMMM yyyy").parse(date_text.getText().toString());

                            StringTokenizer tokens = new StringTokenizer(simple.format(date1), "-");
                            dialog.updateDate(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()) - 1, Integer.parseInt(tokens.nextToken()));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());

                        }
                    }
                }
                dialog.show();
            }

            class mDateSetListener implements DatePickerDialog.OnDateSetListener {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

//                    Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
//                    String d=String.format("%1$td %1$tB %1$tY", date);
//                    date_text.setText(d);
                      date_text.setText(objUsefullData.set_date(dayOfMonth,monthOfYear,year));

                }
            }
        });

        current = Months_details.actorsList.size();
//        pic.setOnTouchListener(new OnSwipeTouchListener(getParent()) {
//
//
//            public void onSwipeTop() {
//            }
//
//            public void onSwipeRight() {
//
//
//                current++;
//                if (current > Months_details.actorsList.size()) {
//                    current = 1;
//                    if (!Months_details.actorsList.get(current).getpicture().equals("null")) {
////                        aq.id(pic).image(Months_details.actorsList.get(current).getpicture(), true, true, 0, R.mipmap.image_not_found);
//                        pic.setImageURI(Months_details.actorsList.get(current).getpicture());
//                        txt.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        date_text.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        current_id = String.valueOf(Months_details.actorsList.get(current).getid());
//                    }
//
//                } else {
//                    if (!Months_details.actorsList.get(current).getpicture().equals("null")) {
////                        aq.id(pic).image(Months_details.actorsList.get(current).getpicture(), true, true, 0, R.mipmap.image_not_found);
//                        pic.setImageURI(Months_details.actorsList.get(current).getpicture());
//                        txt.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        date_text.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        current_id = String.valueOf(Months_details.actorsList.get(current).getid());
//                    }
//                }
//
//
//            }
//
//            public void onSwipeLeft() {
//
//
//                current--;
//                if (current < 0) {
//                    current = Months_details.actorsList.size() - 1;
//                    if (!Months_details.actorsList.get(current).getpicture().equals("null")) {
////                        aq.id(pic).image(Months_details.actorsList.get(current).getpicture(), true, true, 0, R.mipmap.image_not_found);
//                        pic.setImageURI(Months_details.actorsList.get(current).getpicture());
//                        txt.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        date_text.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                        current_id = String.valueOf(Months_details.actorsList.get(current).getid());
//                    }
//                } else {
//                    {
//                        if (!Months_details.actorsList.get(current).getpicture().equals("null")) {
////                            aq.id(pic).image(Months_details.actorsList.get(current).getpicture(), true, true, 0, R.mipmap.image_not_found);
//                            pic.setImageURI(Months_details.actorsList.get(current).getpicture());
//                            txt.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                            date_text.setText(time_stamp(Months_details.actorsList.get(current).gettitle()));
//                            current_id = String.valueOf(Months_details.actorsList.get(current).getid());
//                        }
//                    }
//                }
//
//
//            }
//
//            public void onSwipeBottom() {
//            }
//
//        });


    }


    private void submit_pic(String date,String id)
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
                user.put("date", date);

                request.put("jrnl_attachment", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.custom("/update_jrnl_media_date/"+id, request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Updated");
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


//    public String time_stamp1(String date)
//    {
//        long timestamp = Long.parseLong(date) * 1000L;
//        Date netDate = (new Date(timestamp));
//        return String.format("%1$td %1$tB %1$tY", netDate);
//    }

    private String time_stamp(String date) {
        long timestamp = Long.parseLong(date) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String d = DateFormat.format("dd-MM-yyyy", cal).toString();
        return d;
    }



    private void image_delete(String post_id)
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

            UserAPI.delete_StringResp("/jrnl_attachments/"+post_id,headers, null,
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




}
