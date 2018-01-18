package com.skwibble.skwibblebook.playpen_group;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.ImageCompression;
import com.skwibble.skwibblebook.media_upload.Single_media_picker;
import com.skwibble.skwibblebook.media_upload.Single_trimmer;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.story_group.Post_to_skwibble;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Adil Soomro
 *
 */
public class Playpen_expand_activity extends Activity implements SwipyRefreshLayout.OnRefreshListener{

    public ListView lv;
    Playpen_adapter adapter;
    UsefullData objUsefullData;
    SaveData save_data;
    TextView plygroup_name;
    SimpleDraweeView plygroup_pic;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    RelativeLayout back,setting,add_buddies;
    ImageView capture,remove_pic,make_post,add_buddies_img;
    LinearLayout fetch_img;
    String media="";
    EditText txt;
    String gruop_pic,delete_txt;
    int member_id;
    boolean notification_status=false;
    public static JSONObject group_info=null;
    LinearLayout not_found,if_video;
    TextView no_found_title;
    int size=20;
    private SwipyRefreshLayout swipeRefreshLayout;
    Intent play;
    boolean load_more=true;
    int more=1,currentFirstVisibleItem,currentScrollState,currentVisibleItemCount,currentTotalItemCount;
    int i=1;
    RelativeLayout media_layer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playpen_layout);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        if(!save_data.isExist(Definitions.show_playpen_setting)) {

            objUsefullData.showpopup(R.mipmap.show_playpen_setting,Definitions.show_playpen_setting);
        }

        Single_trimmer.done_path="";
        Single_media_picker.singlearrayMediaPath.clear();

        size=objUsefullData.screen_size()+3;
        not_found = (LinearLayout) findViewById(R.id.not_found_xaalayout);
        if_video = (LinearLayout) findViewById(R.id.video_locator);
        media_layer = (RelativeLayout) findViewById(R.id.media_layer);
        no_found_title=(TextView) findViewById(R.id.post_not_xd_playpen);
        no_found_title.setTypeface(objUsefullData.get_proxima_regusr());
        back=(RelativeLayout) findViewById(R.id.imaqetddcffffbasck);
        setting=(RelativeLayout) findViewById(R.id.invidfsiew10);
        add_buddies=(RelativeLayout) findViewById(R.id.invidgdgdfsiew10);

        swipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSoundEffectsEnabled(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.Dark,R.color.orange);
        add_buddies_img=(ImageView) findViewById(R.id.inviteimadddgeView10);
        lv=(ListView)findViewById(R.id.lvuser);
        plygroup_name=(TextView) findViewById(R.id.textdddView7);
        plygroup_name.setTypeface(objUsefullData.get_ubntu_regular());
        capture=(ImageView) findViewById(R.id.capture);
        remove_pic=(ImageView) findViewById(R.id.capture_remove);
        fetch_img=(LinearLayout) findViewById(R.id.capture_camera);
        make_post=(ImageView) findViewById(R.id.buttghopostn3);
        txt=(EditText) findViewById(R.id.editTcmddntext3);
        txt.setTypeface(objUsefullData.get_proxima_regusr());
        plygroup_pic=(SimpleDraweeView) findViewById(R.id.usedageview);



        adapter = new Playpen_adapter(getParent(), R.layout.row_playpen, actorsList);
        lv.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                finish();
            }

        });
        play= getIntent();
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Playpen_setting.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();
                edit.putExtra("playpen_name",plygroup_name.getText().toString());
                edit.putExtra("playpen_pic",gruop_pic);
                edit.putExtra("member_id",String.valueOf(member_id));
                edit.putExtra("delete_txt",delete_txt);
                edit.putExtra("playpen_id",play.getStringExtra("gruop_id"));
                edit.putExtra("notification_status",notification_status);
                parentActivity.startChildActivity("playpen_setting_Activity", edit, false);
            }

        });

        add_buddies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");

                Intent edit = new Intent(getParent(), Playpen_baddies_list.class);
                TabGroupActivity parentActivity = (TabGroupActivity)getParent();
                edit.putExtra("playpen_id",play.getStringExtra("gruop_id"));

                parentActivity.startChildActivity("playpen_buddiess_list_Activity", edit, false);

            }

        });

        try {

            get_playpen_list("/playpens/"+play.getStringExtra("gruop_id")+"?&page=1",true,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fetch_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ask.on(getParent()).forPermissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withRationales("In order to save file you will need to grant storage permission") //optional
                        .go();

                fetch_img.setEnabled(false);
                fetch_img.setAlpha(.3f);
                Intent i=new Intent(getParent(), Single_media_picker.class);
                i.putExtra(Definitions.IMAGE_REQUEST,"Playpen");
                startActivity(i);
            }
        });
        remove_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture.setVisibility(View.GONE);
                remove_pic.setVisibility(View.GONE);
                if_video.setVisibility(View.GONE);
                media_layer.setVisibility(View.GONE);
                capture.setImageResource(0);
                media="";
                fetch_img.setEnabled(true);
                fetch_img.setAlpha(1f);
            }
        });
        make_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = txt.getText().toString();

                if(text.equals("") && media.equals(""))
                {
                    objUsefullData.make_toast("Either story or image is required");

                }else if(!media.equals("")){

                    fetch_img.setEnabled(true);
                    fetch_img.setAlpha(1f);
                    String id=play.getStringExtra("gruop_id");

                    String type=objUsefullData.getMimeType(media);
                    objUsefullData.showProgress();
                    upload(id,txt.getText().toString(),media,type);

                    capture.setVisibility(View.GONE);
                    remove_pic.setVisibility(View.GONE);
                    if_video.setVisibility(View.GONE);
                    media_layer.setVisibility(View.GONE);
                    capture.setImageResource(0);
                    txt.setText("");
                    media="";

                }else {

                    String id=play.getStringExtra("gruop_id");
                    submit_msg(id);

                }
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged (AbsListView view,int scrollState){
                currentScrollState = scrollState;
                if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE) {
                    if (currentFirstVisibleItem == 0) {
                        if(load_more==true) {
                            more++;
                            get_playpen_list("/playpens/" + play.getStringExtra("gruop_id") + "?&page=" + more, true,true);
                        }

                    }
                }
            }

            @Override
            public void onScroll (AbsListView view,int firstVisibleItem, int visibleItemCount,
                                  int totalItemCount){
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                currentTotalItemCount = totalItemCount;
            }
        });


    }

    @Override
    protected void onResume() {



        if(!Single_media_picker.singlearrayMediaPath.isEmpty()) {

            media = "";
            for (String object : Single_media_picker.singlearrayMediaPath) {
                // do something with object
                ImageCompression i=new ImageCompression(getParent());
                media=i.compressImage(object);
            }
         }else if(!Single_trimmer.done_path.isEmpty()){

            media = Single_trimmer.done_path;

        }else {
            get_playpen_list("/playpens/"+play.getStringExtra("gruop_id")+"?&page=1",true,false);
        }

        File imgFile = new  File(media);

        if(imgFile.exists()){
            boolean type=objUsefullData.is_image(imgFile);
            media_layer.setVisibility(View.VISIBLE);
            if(type){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                capture.setImageBitmap(myBitmap);
                capture.setVisibility(View.VISIBLE);
                remove_pic.setVisibility(View.VISIBLE);
                if_video.setVisibility(View.GONE);

            }else {
                if_video.setVisibility(View.VISIBLE);
                capture.setVisibility(View.VISIBLE);
                capture.setImageBitmap(ThumbnailUtils.createVideoThumbnail(media, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                remove_pic.setVisibility(View.VISIBLE);

            }
        }
        Single_media_picker.singlearrayMediaPath.clear();
        Single_trimmer.done_path="";
        if(media.equals("")){
            fetch_img.setEnabled(true);
            fetch_img.setAlpha(1f);
        }else {
            fetch_img.setEnabled(false);
            fetch_img.setAlpha(.3f);
        }

        super.onResume();
    }

    private void get_playpen_list(String url,final boolean loader,final boolean more) {

        if (loader) {
            objUsefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response,more);
                        lv.setEnabled(true);
                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }

                        if(actorsList.size()==0){
                            not_found.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lv.setEnabled(true);

                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                        objUsefullData.make_toast("Something went wrong");
                        finish();
                    }
                });


    }

    private void set_up_values(JSONObject response,boolean more)
    {
        try {

            if(!more){
                actorsList.clear();
            }
            if(response.isNull("next_page"))
            {
                load_more=false;
            }
            group_info=response;
            String gruop_name=response.getJSONObject("playpen").optString("name");
            gruop_pic=response.getJSONObject("playpen").optString("image_url","null");
            if(response.optBoolean("is_admin"))
            {
                member_id=response.getJSONObject("you").optInt("id");
                delete_txt="Delete Playpen";
                add_buddies_img.setImageResource(R.mipmap.invite_icon);

            }else {
                delete_txt="Exit Playpen";
                add_buddies_img.setImageResource(R.mipmap.user_buddies);
                JSONArray buddies = response.getJSONArray("buddies");

                for (int i = 0; i < buddies.length(); i++)
                {
                    JSONObject in = buddies.getJSONObject(i);


                    int id = in.optInt("id");


                    boolean you_active = in.optBoolean("you_active");

                    if(you_active){
                        member_id=id;
                    }

                }

            }

            notification_status=response.optBoolean("playpen_notification");

            plygroup_name.setText(gruop_name);

            plygroup_pic.setImageURI(gruop_pic);
            plygroup_pic.setVisibility(View.VISIBLE);



            JSONArray posts = response.getJSONArray("posts");

            for (int i = 0; i < posts.length(); i++)
            {
                JSONObject in = posts.getJSONObject(i);

                Actors actor = new Actors();

                if(in.has("user_left")){

                    String user_name = in.optString("user_name");


                    actor.setuser("true");
                    actor.setusername(user_name+" left");




                }else {
                    String user_name = in.optString("user_name");
                    String content = in.optString("content");
                    String date = in.optString("date");
                    String user_image_url = in.optString("user_image_url");
                    String media_url = in.optString("media_url", "null");
                    boolean is_video = in.optBoolean("is_video");

                    int id = in.optInt("id");
                    int likes = in.optInt("likes");
                    int comments = in.optInt("comments");

                    boolean your_post = in.optBoolean("your_post");
                    boolean user_like = in.optBoolean("you_like");



                    actor.setid(id);
                    actor.setlikes(likes);
                    actor.setlikes2(comments);
                    actor.setPara_1(is_video);
                    actor.setuser("false");
                    actor.setpicture(media_url);
                    actor.setdescription(user_name);
                    actor.setcreated_date(content);
                    actor.setcomnt(date);
                    actor.settitle("" + your_post);
                    actor.setchild_pic("" + user_like);
                    actor.setusername(user_image_url);
                }
                    actorsList.add(0,actor);
            }


            adapter.notifyDataSetChanged();

            if(!more) {

                lv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv.setSelection(actorsList.size());
                    }
                }, 500);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();

    }





    private void submit_msg(final String id)
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

                user.put("content", txt.getText().toString());
                user.put("playpen_id", id);



                request.put("playpen_post", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/playpen_posts", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            capture.setVisibility(View.GONE);
                            remove_pic.setVisibility(View.GONE);
                            capture.setImageResource(0);
                            txt.setText("");
                            media="";



                            objUsefullData.firebase_analytics("playpenPostCreate");




                            get_playpen_list("/playpens/"+play.getStringExtra("gruop_id")+"?&page=1",true,false);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.showMsgOnUI("Something went  wrong");


                        }
                    });
        }
    }


    public class Playpen_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;

        public Playpen_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.imageviewhoome = (SimpleDraweeView) v.findViewById(R.id.imageView12);
                holder.purple_pic = (ImageView) v.findViewById(R.id.imageView15);
                holder.orange_pic = (ImageView) v.findViewById(R.id.imageView16);
                holder.video_thumb = (ImageView) v.findViewById(R.id.playpen_video_thumb);
                holder.txt = (TextView) v.findViewById(R.id.textView24);
                holder.user_left = (TextView) v.findViewById(R.id.user_left);
                holder.other = (SimpleDraweeView) v.findViewById(R.id.imageView18);
                holder.user = (SimpleDraweeView) v.findViewById(R.id.imageView19);
                holder.txtdate = (TextView) v.findViewById(R.id.textView2dateff2);
                holder.like = (TextView) v.findViewById(R.id.textVilikeffsew21);
                holder.comnt = (TextView) v.findViewById(R.id.textVicmntsdsew9);
                holder.user_name = (TextView) v.findViewById(R.id.textView28);
                holder.other_user_name = (TextView) v.findViewById(R.id.textView29);
                holder.clone_post_purple = (LinearLayout) v.findViewById(R.id.imageView13);
                holder.clone_post_orange = (LinearLayout) v.findViewById(R.id.imageView14);
                holder.left_layout = (LinearLayout) v.findViewById(R.id.left_layout);
                holder.normal_layout = (RelativeLayout) v.findViewById(R.id.normal_layout);
                holder.media_available = (RelativeLayout) v.findViewById(R.id.media_available);
                holder.txt.setTypeface(objUsefullData.get_proxima_regusr());
                holder.txtdate.setTypeface(objUsefullData.get_proxima_regusr());
                holder.like.setTypeface(objUsefullData.get_proxima_regusr());
                holder.comnt.setTypeface(objUsefullData.get_proxima_regusr());
                holder.user_name.setTypeface(objUsefullData.get_proxima_regusr());
                holder.user_left.setTypeface(objUsefullData.get_proxima_regusr());
                holder.other_user_name.setTypeface(objUsefullData.get_proxima_regusr());
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }


            if(actorList.get(position).getuser().equals("false")) {


                holder.normal_layout.setVisibility(View.VISIBLE);
                holder.left_layout.setVisibility(View.GONE);
                holder.txtdate.setText(actorList.get(position).getcomnt());
                holder.like.setText("" + actorList.get(position).getlikes());
                holder.comnt.setText("" + actorList.get(position).getlikes2());

                if(actorList.get(position).getcreated_date().equals("")||actorList.get(position).getcreated_date().equals("null"))
                {
                    holder.txt.setVisibility(View.GONE);
                }else {
                    holder.txt.setVisibility(View.VISIBLE);
                    holder.txt.setText(actorList.get(position).getcreated_date());

                }





                if (actorList.get(position).getpicture().equals("null")) {
                    holder.imageviewhoome.setVisibility(View.GONE);
                    holder.video_thumb.setVisibility(View.GONE);
                    holder.media_available.setVisibility(View.GONE);
                } else {
                    holder.imageviewhoome.setVisibility(View.VISIBLE);
                    holder.media_available.setVisibility(View.VISIBLE);
                    holder.imageviewhoome.setImageURI(actorList.get(position).getpicture());

                    if(actorList.get(position).getpara_1()){
                        holder.video_thumb.setVisibility(View.VISIBLE);
                    }else {
                        holder.video_thumb.setVisibility(View.GONE);
                    }
                }

                if (actorList.get(position).getchild_pic().equals("true")) {

                    Drawable dr = getResources().getDrawable(R.mipmap.puerple_liked);


                    if(actorList.get(position).gettitle().equals("false")){
                        dr = getResources().getDrawable(R.mipmap.liked);

                    }

                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));




                    holder.like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    Drawable dr = getResources().getDrawable(R.mipmap.purple_not_like);
                    Drawable dc = getResources().getDrawable(R.mipmap.purple_cmnt);
                    Drawable dd = getResources().getDrawable(R.mipmap.purple_date);

                    if(actorList.get(position).gettitle().equals("false")){
                        dr = getResources().getDrawable(R.mipmap.not_like);
                        dc = getResources().getDrawable(R.mipmap.cmnt);
                        dd = getResources().getDrawable(R.mipmap.strory_date);

                    }
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                    Bitmap bitmapc = ((BitmapDrawable) dc).getBitmap();
                    Drawable c = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapc, size, size, true));

                    Bitmap bitmapd = ((BitmapDrawable) dd).getBitmap();
                    Drawable date = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapd, size, size, true));

                    holder.comnt.setCompoundDrawablesWithIntrinsicBounds(c, null, null, null);
                    holder.like.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                    holder.txtdate.setCompoundDrawablesWithIntrinsicBounds(date, null, null, null);
                }
                if (actorList.get(position).gettitle().equals("true")) {
                    holder.purple_pic.setVisibility(View.VISIBLE);
                    holder.orange_pic.setVisibility(View.INVISIBLE);
                    holder.other.setVisibility(View.INVISIBLE);
                    holder.other_user_name.setVisibility(View.INVISIBLE);
                    holder.user_name.setVisibility(View.VISIBLE);
                    holder.user_name.setText("Me");
                    holder.user.setVisibility(View.VISIBLE);
                    holder.user.setImageURI(actorList.get(position).getusername());

                } else {

                    holder.purple_pic.setVisibility(View.INVISIBLE);
                    holder.orange_pic.setVisibility(View.VISIBLE);
                    holder.user.setVisibility(View.INVISIBLE);
                    holder.other_user_name.setVisibility(View.VISIBLE);
                    holder.user_name.setVisibility(View.INVISIBLE);
                    holder.other.setVisibility(View.VISIBLE);
                    holder.other_user_name.setText(actorList.get(position).getdescription());
                    holder.other.setImageURI(actorList.get(position).getusername());
                }




            }else {
                holder.normal_layout.setVisibility(View.GONE);
                holder.left_layout.setVisibility(View.VISIBLE);

                holder.user_left.setText(actorList.get(position).getusername());
            }


            holder.imageviewhoome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!swipeRefreshLayout.isRefreshing()) {

                            Intent edit = new Intent(getContext(), Playpen_post_detail.class);
                            TabGroupActivity parentActivity = (TabGroupActivity) getContext();
                            edit.putExtra("post_id", String.valueOf(actorList.get(position).getid()));
                            parentActivity.startChildActivity("playpen_postdetailsActivity", edit, false);

                    }
                }
            });
            holder.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!swipeRefreshLayout.isRefreshing()) {
                        Intent edit = new Intent(getContext(), Playpen_post_detail.class);
                        TabGroupActivity parentActivity = (TabGroupActivity) getContext();
                        edit.putExtra("post_id", String.valueOf(actorList.get(position).getid()));
                        parentActivity.startChildActivity("playpen_postdetailsActivity", edit, false);
                    }
                }
            });

            holder.clone_post_orange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!swipeRefreshLayout.isRefreshing()) {
                            Intent edit = new Intent(getParent(), Post_to_skwibble.class);
//                            TabGroupActivity parentActivity = (TabGroupActivity) getContext();
                            edit.putExtra("intent", "clone_playpen");
                            edit.putExtra("id", String.valueOf(actorList.get(position).getid()));
                            startActivity(edit);
//                            parentActivity.startChildActivity("edit_story_post_Activity", edit, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.clone_post_purple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!swipeRefreshLayout.isRefreshing()) {
                            Intent edit = new Intent(getParent(), Post_to_skwibble.class);
//                          TabGroupActivity parentActivity = (TabGroupActivity) getContext();
                            edit.putExtra("intent", "clone_playpen");
                            edit.putExtra("id", String.valueOf(actorList.get(position).getid()));
                            startActivity(edit);
//                          parentActivity.startChildActivity("edit_story_post_Activity", edit, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!swipeRefreshLayout.isRefreshing()) {

                        if (actorsList.get(position).getchild_pic().equals("true")) {
                            post_unlike(actorsList.get(position).getid(), position);
                        } else {
                            post_like(actorsList.get(position).getid(), position);
                        }
                    }

                }
            });

            return v;

        }

        class ViewHolder {
            public ImageView purple_pic,orange_pic;
            public SimpleDraweeView imageviewhoome;
            public SimpleDraweeView other,user;
            public TextView txt,txtdate,like,comnt,other_user_name,user_name,user_left;
            public ImageView video_thumb;
            public LinearLayout left_layout,clone_post_purple,clone_post_orange;
            public RelativeLayout normal_layout,media_available;
        }


    }

    public boolean updateListView(int position, String value ,int pic ) {
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

            TextViewDrawableSize likes = (TextViewDrawableSize) convertView.findViewById(R.id.textVilikeffsew21);
            likes.setText(value);


            Drawable dr = getResources().getDrawable(pic);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);


            return true;
        }
    }


    private void post_like(int post_id,final int pos)
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
                            try {
                                    if(actorsList.get(pos).gettitle().equals("false")){
                                        updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.liked);
                                    }else {
                                        updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.puerple_liked);
                                    }


                                actorsList.get(pos).setlikes(response.optInt("like_counts"));
                                actorsList.get(pos).setchild_pic(""+response.optBoolean("you_like"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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

    private void post_unlike(int post_id,final int pos)
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


                            try {

                                if(actorsList.get(pos).gettitle().equals("false")){
                                    updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.not_like);
                                }else {
                                    updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.purple_not_like);
                                }


                                actorsList.get(pos).setlikes(response.optInt("like_counts"));
                                actorsList.get(pos).setchild_pic("" + response.optBoolean("you_like"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                getParent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mBinding.swipyrefreshlayout.setRefreshing(false);
                                if(objUsefullData.isNetworkConnected()) {
                                         lv.setEnabled(false);

                                         load_more=true;
                                         swipeRefreshLayout.setRefreshing(true);
                                         get_playpen_list("/playpens/"+play.getStringExtra("gruop_id")+"?&page=1",false,false);

                                    }else {
                                        objUsefullData.showMsgOnUI("Please check your internet connection and try again");
                                    }

                    }
                });
            }
        }, 2000);
    }


    public void upload(final String playpen_id,final String content,final String media,final String content_type){

        objUsefullData.showProgress();
        Ion.with(getParent())
                .load("POST", Definitions.APIdomain + Definitions.PLAYPEN_UPLOAD_URL)
                .setTimeout(60 * 60 * 60 * 1000)
                .uploadProgress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        int percent = (int) (downloaded * 100 / total);
                        // update your progressbar with this percent if needed

                    }
                })
                .addHeader("Accept", Definitions.version)
                .addHeader("X-User-Email", save_data.get(Definitions.user_email))
                .addHeader("X-User-Token", save_data.get(Definitions.auth_token))

                .setMultipartParameter("playpen_post[content]", content)
                .setMultipartParameter("playpen_post[playpen_id]", playpen_id)
                .setMultipartFile("playpen_post[media]",content_type, new File(media))



                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            // error: log the message here

                            objUsefullData.dismissProgress();
                            objUsefullData.make_toast("Upload Unsuccessful");
                            return;
                        }
                        if (result != null) {
                            // result is the response of your server
                            objUsefullData.dismissProgress();


                            get_playpen_list("/playpens/"+play.getStringExtra("gruop_id")+"?&page=1",true,false);

                        }
                    }
                });

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
