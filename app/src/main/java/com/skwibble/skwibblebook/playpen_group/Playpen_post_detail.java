package com.skwibble.skwibblebook.playpen_group;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.journal_group.Videoplayer;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.story_group.Comment_adapter;
import com.skwibble.skwibblebook.story_group.Edit_comment;
import com.skwibble.skwibblebook.story_group.Post_to_skwibble;
import com.skwibble.skwibblebook.story_group.SliderPagerAdapter;
import com.skwibble.skwibblebook.utility.ClickableViewPager;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Playpen_post_detail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{

//    ImageView post_pic;
    SimpleDraweeView child_pic;
    TextView txt,child_name;
    TextViewDrawableSize like_count,comnt_count,date,like,edit_post,delete_post,post_type_icon,privacy_icon,share,clone_post;
    boolean pic_like=false;
    UsefullData objUsefullData;
    RelativeLayout back;
    SaveData save_data;
    ExpandableHeightListView lv;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    Comment_adapter adapter;
    EditText comment;
    ImageView post_cmnt;
    int size=20;
    ScrollView scroll;
    String post_share_url;
    boolean make_cmnt=false;
    Intent intent;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean normal_back= true;
    RelativeLayout top_layer;
    private ClickableViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Actors> slider_image_list = new ArrayList<Actors>();
    private TextView[] dots;
    List<String> lst= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        objUsefullData = new UsefullData(getParent());

        save_data = new SaveData(getParent());

        init();
        size=objUsefullData.screen_size();
        intent = getIntent();
        child_pic=(SimpleDraweeView) findViewById(R.id.imageView10_child);
        top_layer=(RelativeLayout) findViewById(R.id.LinearLayout1);

        txt=(TextView) findViewById(R.id.textViejjw9);
        child_name=(TextView) findViewById(R.id.textView7_childname);
        back=(RelativeLayout) findViewById(R.id.imageViaaewfdvb8_back);
        lv=(ExpandableHeightListView ) findViewById(R.id.listview_cmnt);
        like_count=(TextViewDrawableSize) findViewById(R.id.textView21_like);
        comnt_count=(TextViewDrawableSize) findViewById(R.id.textView9_cmnt);
        date=(TextViewDrawableSize) findViewById(R.id.textView22_date);
        comment=(EditText) findViewById(R.id.editTcmntext3);
        post_cmnt=(ImageView) findViewById(R.id.buttopostn3);
        like=(TextViewDrawableSize) findViewById(R.id.texcas1_like);
        edit_post=(TextViewDrawableSize) findViewById(R.id.textViedit_date);
        edit_post.setVisibility(View.GONE);
        delete_post=(TextViewDrawableSize) findViewById(R.id.textView9);
        scroll=(ScrollView) findViewById(R.id.post_detail_scroll);
        post_type_icon=(TextViewDrawableSize) findViewById(R.id.textVuiuiiew21);
        post_type_icon.setVisibility(View.INVISIBLE);
        privacy_icon=(TextViewDrawableSize) findViewById(R.id.textVidfsadadew21);
        privacy_icon.setVisibility(View.INVISIBLE);

        txt.setTypeface(objUsefullData.get_proxima_light());
        child_name.setTypeface(objUsefullData.get_proxima_regusr());
        like_count.setTypeface(objUsefullData.get_proxima_regusr());
        comnt_count.setTypeface(objUsefullData.get_proxima_regusr());
        date.setTypeface(objUsefullData.get_proxima_regusr());
        comment.setTypeface(objUsefullData.get_proxima_light());
        like.setTypeface(objUsefullData.get_proxima_regusr());

        delete_post.setTypeface(objUsefullData.get_proxima_regusr());
        share=(TextViewDrawableSize) findViewById(R.id.texonlyew9_cmnt);
        share.setVisibility(View.GONE);
        clone_post=(TextViewDrawableSize) findViewById(R.id.textVissew9);
        clone_post.setTypeface(objUsefullData.get_proxima_regusr());
        clone_post.setVisibility(View.VISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Dark,R.color.orange);


//        post_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /** Initiate Popup view **/
//                if(!post_share_url.equals("null")) {
//                    try {
//                        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(getResources())
//                                .setFailureImage(R.drawable.warning_sigh)
//                                .setProgressBarImage(R.drawable.progress_bar_states);
//
//                        List<String> lst= new ArrayList<String>();
//                        lst.add(post_share_url);
//                        new ImageViewer.Builder(getParent(), lst)
//                                .setStartPosition(0)
//                                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
//
//                                .show();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(pic_like==true)
                {
                    post_unlike(intent.getStringExtra("post_id"));
                }else {
                    post_like(intent.getStringExtra("post_id"));
                }
            }
        });
        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Delete Post")
                        .setContentText("Woah...Are you sure you want to delete this post?")
                        .setCancelText("No, cancel!")
                        .setConfirmText("Yes, delete it!")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                post_delete(intent.getStringExtra("post_id"));
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
        clone_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent edit = new Intent(getParent(), Post_to_skwibble.class);
//                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                edit.putExtra("intent", "clone_playpen");
                edit.putExtra("id", intent.getStringExtra("post_id"));
                startActivity(edit);

//                parentActivity.startChildActivity("edit_story_post_Activity", edit, false);




            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                onBackPressed();


            }

        });

        try {
            if(objUsefullData.isNetworkConnected()) {

                adapter = new Comment_adapter(getParent(), R.layout.comment_list_row, actorsList);
                lv.setAdapter(adapter);
                lv.setExpanded(true);
                lv.setLongClickable(true);
                get_post_details(intent.getStringExtra("post_id"),true);

            }else {
                objUsefullData.showMsgOnUI("Please check your internet connection and try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        post_cmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!comment.getText().toString().equals("")) {

                        post_comment(comment.getText().toString().trim(),intent.getStringExtra("post_id"));

                }else{
                    objUsefullData.make_toast("Blank not allowed");
                }



            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {

//                CharSequence[]  items = { "Edit", "Delete", "Cancel" };
                if(actorsList.get(position).getcreated_date().equals("false")&& actorsList.get(position).getuser().equals("false"))
                {

//                        comment_option(items,position,intent.getStringExtra("post_id"));
                } else if(actorsList.get(position).getcreated_date().equals("true") && actorsList.get(position).getuser().equals("false")){
                    CharSequence[] items = { "Edit", "Cancel" };
                    comment_option(items,position,intent.getStringExtra("post_id"));
                } else if(actorsList.get(position).getcreated_date().equals("false")&& actorsList.get(position).getuser().equals("true") ){
                    CharSequence[] items = {  "Delete", "Cancel" };
                    comment_option(items,position,intent.getStringExtra("post_id"));
                }else if(actorsList.get(position).getcreated_date().equals("true")&& actorsList.get(position).getuser().equals("true") ){
                    CharSequence[]  items = { "Edit", "Delete", "Cancel" };
                    comment_option(items,position,intent.getStringExtra("post_id"));
                }






                return true;
            }
        });
    }




    private void get_post_details(String id,final boolean loader) {

        if (loader) {
            objUsefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/playpen_posts/"+id, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        actorsList.clear();
                        slider_image_list.clear();
                        set_up_values(response);

                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        save_data.save(Definitions.current_noti_request,"");
                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {

                                case 500:

                                    objUsefullData.showMsgOnUI("Something went wrong");
                                    break;
                                case 422:

                                    objUsefullData.showMsgOnUI("Either you are not a part of this playpen or the playpen post no longer exists");
                                    break;
                                case 404:

                                    objUsefullData.showMsgOnUI("Playpen post no longer exists");
                                    break;

                            }
                        }
                        finish();
                    }
                });

    }

    private void set_up_values(JSONObject response)
    {

        try {

            post_share_url=response.optString("original_media_url","null");

            if(!response.getJSONObject("post").optString("content").equals("null"))
            {
                txt.setText(response.getJSONObject("post").optString("content"));
            }

            date.setText(response.getJSONObject("post").optString("date"));
            like_count.setText(""+response.getJSONObject("post").optInt("like_counts",0));
            comnt_count.setText(""+response.getJSONObject("post").optInt("comment_counts",0));

            pic_like=response.getJSONObject("post").optBoolean("you_like");


            if(response.getJSONObject("post").optBoolean("delete")==true){

                delete_post.setVisibility(View.VISIBLE);
            }

            if(pic_like)
            {

                Drawable dr = getResources().getDrawable(R.mipmap.liked);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size+8, size+8, true));
                like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
//                like.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.liked, 0, 0, 0);
            }else {
                Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size+8, size+8, true));
                like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
//                like.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_like, 0, 0, 0);
            }


            child_name.setText(response.getJSONObject("user").optString("name"));


            if(!response.isNull("comments")) {
                JSONArray comments = response.getJSONArray("comments");

                for (int i = 0; i < comments.length(); i++) {
                    Actors actor = new Actors();

                    JSONObject in = comments.getJSONObject(i);
                    String user_name = in.optString("user_name");
                    int id = in.getInt("id");
                    boolean comment_edit = in.optBoolean("comment_edit");
                    boolean comment_delete = in.optBoolean("comment_delete");
                    String content = in.optString("content");
                    String date = in.optString("date");
                    String user_image_url = in.optString("user_image_url");


                    actor.setid(id);
                    actor.setcomnt(content);
                    actor.settitle(user_name);
                    actor.setdescription(date);
                    actor.setpicture(user_image_url);
                    actor.setcreated_date("" + comment_edit);
                    actor.setuser("" + comment_delete);

                    actorsList.add(actor);
                }
            }

            adapter.notifyDataSetChanged();


            if(!response.getJSONObject("post").isNull("media_url")) {
                Actors actor = new Actors();

                String video_url = response.getJSONObject("post").optString("video_url");
                String media_url = response.getJSONObject("post").optString("media_url");
                boolean is_video = response.getJSONObject("post").optBoolean("is_video");

                actor.setpicture(media_url);
                actor.setPara_1(is_video);
                actor.settitle(video_url);

                slider_image_list.add(actor);
                if(!is_video) {
                    lst.add(media_url);
                }

                if(slider_image_list.size()>1) {
                    addBottomDots(0);
                }

                sliderPagerAdapter.notifyDataSetChanged();

                RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,
                        objUsefullData.post_details_size() );

                top_layer.setLayoutParams(layout_description);
            }else {
                RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        size*3 );

                top_layer.setLayoutParams(layout_description);
            }

            child_pic.setImageURI(response.getJSONObject("playpen").optString("image_url"));
            child_pic.setVisibility(View.VISIBLE);


            scroll.setVisibility(View.VISIBLE);
            objUsefullData.dismissProgress();
            if(!make_cmnt){
                scroll.smoothScrollTo(0,0);
            }else {
                scroll.post(new Runnable() { public void run() { scroll.fullScroll(View.FOCUS_DOWN); } });
                make_cmnt=false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            objUsefullData.dismissProgress();
            objUsefullData.showMsgOnUI("Something went wrong!");
        }
        if(save_data.getString(Definitions.current_noti_request).equals("Playpen_post_details"))
        {
            save_data.save(Definitions.current_noti_request,"");
            normal_back=false;
        }

    }

    private void post_comment(String cmnt,String post_id)
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
//                user.put("user_id", String.valueOf(save_data.getInt(Definitions.id)));
                user.put("playpen_post_id", post_id);
                user.put("content",cmnt);

                request.put("playpen_post_comment", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/playpen_post_comments", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();

                            make_cmnt=true;
                            get_post_details(intent.getStringExtra("post_id"),true);
                            comment.setText("");


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

    private void post_like(String post_id)
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
//                user.put("user_id", String.valueOf(save_data.getInt(Definitions.id)));
                user.put("playpen_post_id", post_id);

                request.put("playpen_post_like", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/playpen_post_likes", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();

                            Drawable dr = getResources().getDrawable(R.mipmap.liked);
                            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size+8, size+8, true));
                            like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
//                            like.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.liked, 0, 0, 0);
                            pic_like=response.optBoolean("you_like");

                            like_count.setText(""+response.optInt("like_counts"));


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

    private void post_unlike(String post_id)
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
                user.put("playpen_post_id", post_id);

                request.put("playpen_post_like", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/playpen_post_unlikes", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size+8, size+8, true));
                            like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                            pic_like=response.optBoolean("you_like");

                            like_count.setText(""+response.optInt("like_counts"));


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.showMsgOnUI("Please try again");


                        }
                    });
        }
    }


    private void post_delete(String post_id)
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

            UserAPI.delete_StringResp("/playpen_posts/"+post_id,headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();

                            objUsefullData.make_toast("Post Deleted");
                            finish();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();

                            objUsefullData.showMsgOnUI("Please try again");

                        }
                    });
        }
    }






    @Override
    public void onRefresh() {


        if(objUsefullData.isNetworkConnected()) {


            swipeRefreshLayout.setRefreshing(true);
            make_cmnt=false;
            get_post_details(intent.getStringExtra("post_id"),false);

        }else {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
    }



    public void comment_option(final CharSequence[] items,final int position,final String post_id)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
        builder.setTitle("Comment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Edit")) {


                    Intent edit = new Intent(getParent(), Edit_comment.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    edit.putExtra("cmnt_id", ""+actorsList.get(position).getid());
                    edit.putExtra("post_id", post_id);
                    edit.putExtra("user_pic",actorsList.get(position).getpicture());
                    edit.putExtra("user_name",actorsList.get(position).gettitle());
                    edit.putExtra("cmnt",actorsList.get(position).getcomnt());
                    edit.putExtra("edit_for","playpen");
                    parentActivity.startChildActivity("cmnt_Activity", edit, false);



                } else if (items[item].equals("Delete")) {

                    new SweetAlertDialog(getParent(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Delete this comment !")
                            .setCancelText("No, cancel !")
                            .setConfirmText("Yes, delete it !")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    comment_delete(String.valueOf(actorsList.get(position).getid()));
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();


                                }
                            })
                            .show();


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void comment_delete(String cmnt_id)
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

            UserAPI.delete_StringResp("/playpen_post_comments/"+cmnt_id,headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();

                            get_post_details(intent.getStringExtra("post_id"),true);



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
    public void onBackPressed() {
        super.onBackPressed();

        if(!normal_back){

            Intent intent = new Intent(getParent(), Tab_activity.class);
            intent.putExtra("from",1);
            startActivity(intent);

        }else{

//            finish();
        }

    }

    private void init() {

        vp_slider = (ClickableViewPager) findViewById(R.id.vp_slider_post_show);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots__post_show);

        sliderPagerAdapter = new SliderPagerAdapter(getParent(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);


        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                enableDisableSwipeRefresh( state == ViewPager.SCROLL_STATE_IDLE );

            }
        });
        vp_slider.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // your code
                try {
                    if(!slider_image_list.get(position).getpara_1()) {
                        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(getResources())
                                .setFailureImage(R.drawable.warning_sigh)
                                .setProgressBarImage(R.drawable.progress_bar_states);

                        new ImageViewer.Builder(getParent(), lst)
                                .setStartPosition(position)
                                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)

                                .show();
                    }else {
                        Intent i = new Intent(getParent(), Videoplayer.class);
                        i.putExtra("url",slider_image_list.get(position).gettitle());
                        getParent().startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#9E88C2"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FA700D"));
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
