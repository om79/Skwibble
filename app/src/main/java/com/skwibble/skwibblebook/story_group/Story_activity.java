package com.skwibble.skwibblebook.story_group;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.firebase.MyReceiver;
import com.skwibble.skwibblebook.media_upload.Multi_post_service;
import com.skwibble.skwibblebook.user_group.Add_child;
import com.skwibble.skwibblebook.user_group.Child_profile;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.OnSwipeTouchListener;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Story_activity extends Activity  implements SwipeRefreshLayout.OnRefreshListener
{

    public static StaggeredGridView mGridView;
    HomeAdapter adapter;
    ArrayList<Actors> actorsList = new ArrayList<Actors>();
    TextViewDrawableSize add_post;
    UsefullData objUsefullData;
    SaveData save_data;
    TextView head_title;
    DynamicHeightImageView head_pic;
    TextViewDrawableSize head_likes, head_cmnt, head_date, head_media_count;
    View header;
    int head_id;
    LinearLayout head_layout,add_post_layout,header_click;
    SimpleDraweeView user_pic;
    TextView user_name;
    ImageView invite,drop_icon;
    RelativeLayout invite_click,profile;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout not_found,not_found_create_btn,switch_child;
    TextView create,title;
    int size=20,more=1;
    String s;
    boolean header_like_status,header_available=false,header_visible=true,load_more=true;
    private IconRoundCornerProgressBar progressOne;
    TextView post_count,post_percentage;
    LinearLayout progress_layer;
    boolean refresh=false;
    int start_call=0;
    public static boolean uploading=false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_layout);

        init();

        if(!save_data.isExist(Definitions.show_story_invite)) {

            objUsefullData.showpopup(R.mipmap.show_story_invite,Definitions.show_story_invite);
        }

        Intent intent1=getIntent();
        Bundle extras = intent1.getExtras();
        if (extras != null) {
            if (extras.containsKey("current_event_msg")) {
                if(save_data.isExist(Definitions.current_event_msg)) {
                    objUsefullData.showMsgOnUI(extras.getString("current_event_msg"));
                    save_data.remove(Definitions.current_event_msg);
                }
            }
        }
        adapter = new HomeAdapter(getParent(), R.layout.list_item_sample, actorsList);
        if (!save_data.getBoolean(Definitions.has_child)) {
            create.setText("Add Child");
            title.setText(R.string.post_not_found_add_child);
            user_pic.setVisibility(View.INVISIBLE);
            user_name.setText("Add Child");
            drop_icon.setVisibility(View.INVISIBLE);
            invite_click.setVisibility(View.INVISIBLE);

            add_post_layout.setVisibility(View.INVISIBLE);
            if (actorsList.size() == 0 && !header_available ) {
                not_found.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            } else {
                not_found.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            }
        } else {
            create.setText("Add Post");
            user_pic.setVisibility(View.VISIBLE);
            drop_icon.setVisibility(View.VISIBLE);
            title.setText(R.string.post_not_found_add_post);
            invite_click.setVisibility(View.VISIBLE);

            add_post_layout.setVisibility(View.VISIBLE);

            if (objUsefullData.isNetworkConnected()) {

                get_post(true, "/story_posts?dummypost=1&page=1", false);
            } else {
                objUsefullData.make_toast("Please check your internet connection and try again");

                if (actorsList.size() == 0 && !header_available) {
                    not_found.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    not_found.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        }


        not_found_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (create.getText().toString().equals("Add Child")) {
                    Intent edit = new Intent(getParent(), Add_child.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    parentActivity.startChildActivity("add_child_Activity", edit, false);

                } else {
                    Intent edit = new Intent(getParent(), Post_to_skwibble.class);
                    edit.putExtra("intent", "new_post");
                    startActivity(edit);
                }


            }
        });

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent edit = new Intent(getParent(), Post_to_skwibble.class);
                edit.putExtra("intent", "new_post");
                startActivity(edit);

            }
        });

        switch_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Multiple_child.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                parentActivity.startChildActivity("multiply_child_Activity", edit, false);


            }
        });



//        head_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!swipeRefreshLayout.isRefreshing()) {
//                    Intent edit = new Intent(getParent(), Post_details.class);
//                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
//                    edit.putExtra("post_id", String.valueOf(head_id));
//                    edit.putExtra("call", "story");
//                    save_data.save(Definitions.POST_DETAILS_REQUEST,true);
//                    save_data.save(Definitions.CURRENT_POSITION,-1);
//                    save_data.save(Definitions.CURRENT_LIKE,Integer.parseInt(head_likes.getText().toString()));
//                    save_data.save(Definitions.CURRENT_USER_LIKE,String.valueOf(header_like_status));
//                    save_data.save(Definitions.CURRENT_COMMENT,Integer.parseInt(head_cmnt.getText().toString()));
//
//                    parentActivity.startChildActivity("postdetailsActivity", edit, true);
//                }
//            }
//        });


        invite_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Stroy_invite_activity.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                edit.putExtra("name", s);
                parentActivity.startChildActivity("story_invite_Activity", edit, false);
            }

        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Child_profile.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                edit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                parentActivity.startChildActivity("newprggtgtofile_Activity", edit, false);

            }

        });


        head_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (header_like_status) {
                    post_unlike(head_id, -1);
                } else {
                    post_like(head_id, -1);
                }
            }
        });

        mGridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // get the top of  first child
                int top = 0;
                try {
                    View mView = mGridView.getChildAt(0);
                    top = mView.getTop();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                        // see if it top is at Zero, and first visible position is at 0
                        if (top == 0 && mGridView.getFirstVisiblePosition() == 0) {


                            Log.e("Header Item Visible", "Header Item Visible");
                            header_visible = true;
                        } else {
                            header_visible = false;
                        }
                }
                // dont forget to retrun false here
                return false;
            }

        });


        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) { // TODO Auto-generated method stub
                int threshold = 1;
                int count = mGridView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (mGridView.getLastVisiblePosition() >= count
                            - threshold) {
                        // Execute LoadMoreDataTask AsyncTask

                        if (load_more) {
                            more++;

                            get_post(true, "/story_posts?dummypost=2&page=" + more, true);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

                if(firstVisibleItem == 0 && listIsAtTop()){
                    header_visible = true;
                }else{
                    header_visible = false;
                }

            }

        });
    }

    private void get_post(final boolean loader,final String url ,final boolean more) {

        if (loader) {
            objUsefullData.showProgress();
        }

        if (save_data.getBoolean(Definitions.has_child)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.version);
            headers.put("X-User-Email", save_data.get(Definitions.user_email));
            headers.put("X-User-Token", save_data.get(Definitions.auth_token));

            UserAPI.get_JsonObjResp(url, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("TAG response", response.toString());
                            set_up_values(response, more);


                            if (loader) {
                                objUsefullData.dismissProgress();
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (loader) {
                                objUsefullData.dismissProgress();
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            objUsefullData.showMsgOnUI("Not Found");
                            mGridView.setEnabled(true);
                            adapter.notifyDataSetChanged();
                            if (actorsList.size() == 0 && !header_available) {
                                not_found.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                not_found.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }else {
            if (loader) {
                objUsefullData.dismissProgress();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        }


    }

    private void set_up_values(JSONObject response,final boolean more) {

        try {

            if(response.isNull("next_page"))
            {
                load_more=false;
            }

            if(!more){
                mGridView.removeHeaderView(header);
                actorsList.clear();
            }

            JSONArray comments = response.getJSONArray("posts");



            for (int i = 0; i < comments.length(); i++) {
                JSONObject in = comments.getJSONObject(i);

                boolean   dummy_post = in.optBoolean("dummy_post");
                if(dummy_post && !more){
                    String post_msg = in.optString("post_msg");
                    Actors actor = new Actors();

                    actor.settitle("");
                    actor.setdescription(post_msg);
                    actor.setid(0);
                    actor.setlikes(0);
                    actor.setcmnt_count(0);
                    actor.setcreated_date("");
                    actor.setpicture("dumy");
                    actor.setchild_pic("");

                    actorsList.add(actor);
                }else{

                    String date = in.optString("date");
                    int id = in.optInt("id");
                    String content = in.optString("content", "null");
                    int like_counts = in.optInt("like_counts");
                    int comment_counts = in.optInt("comment_counts");
                    int attachment_count = in.optInt("attachment_count");
                    boolean you_like = in.optBoolean("you_like");
//                  String privacy_type = in.optString("privacy_type");
                    String default_post_type = in.optString("default_post_type");
                    String media_url="null";

                    if(!in.isNull("media_url")) {
                        media_url = in.optString("media_url");


                    }


                    if (i == 0 && !content.equals("null") && !media_url.equals("null") && !dummy_post && !more) {

                        head_id = id;
                        head_title.setText(content);
                        head_date.setText(date);
                        header_like_status = you_like;
                        head_likes.setText(String.valueOf(like_counts));
                        head_cmnt.setText(String.valueOf(comment_counts));

                        if(attachment_count>1){
                            head_media_count.setVisibility(View.VISIBLE);
                            head_media_count.setText(""+attachment_count);
                        }else {
                            head_media_count.setVisibility(View.GONE);
                        }

                        Glide.with(getParent())
                                .load(media_url)
                                .placeholder( R.mipmap.story_placeholer) //this is optional the image to display while the url image is downloading
                                .error( R.mipmap.story_placeholer)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                                .into(head_pic);
                        if (header_like_status) {
                            Drawable dr = getResources().getDrawable(R.mipmap.liked);
                            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                            head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                        } else {
                            Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                            head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                        }
                        if (default_post_type.equalsIgnoreCase("Celebration")) {
                            head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card_purple));
                        } else if (default_post_type.equalsIgnoreCase("Milestone")) {
                            head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card_green));
                        } else {
                            head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card));
                        }
                        mGridView.removeHeaderView(header);
                        mGridView.setAdapter(null);
                        mGridView.addHeaderView(header, null, false);
                        header_available = true;
                    } else {
                        Actors actor = new Actors();

                        actor.settitle(media_url);
                        actor.setdescription(content);
                        actor.setid(id);
                        actor.setlikes(like_counts);
                        actor.setcmnt_count(comment_counts);
                        actor.setcreated_date(date);
                        actor.setpicture(default_post_type);
                        actor.setchild_pic("" + you_like);
                        actor.setlikes2(attachment_count);

                        actorsList.add(actor);
                    }
                }
            }

            s=response.getJSONObject("child").optString("name");
            save_data.save(Definitions.child_name,s);

            if(s.length() > 10)
            { s = s.substring(0, 10) + "..";
            }
            user_name.setText(s+"\'s Story");
            user_pic.setImageURI(response.getJSONObject("child").optString("image_url"));
            user_pic.setVisibility(View.VISIBLE);
            drop_icon.setVisibility(View.VISIBLE);

            mGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        mGridView.setEnabled(true);
        mGridView.invalidateViews();

        if(actorsList.size()==0 && !header_available){
            not_found.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }else {
            not_found.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    public class HomeAdapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;



        public HomeAdapter(Context context, int resource, ArrayList<Actors> objects) {
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
                v = vi.inflate(Resource, parent, false);

                holder.imageviewhoome = (DynamicHeightImageView) v.findViewById(R.id.image);
                holder.title = (TextView) v.findViewById(R.id.title);
                holder.hide_dummy = (LinearLayout) v.findViewById(R.id.hide_dummy);
                holder.date = (TextViewDrawableSize) v.findViewById(R.id.textView2date2);
                holder.likes = (TextViewDrawableSize) v.findViewById(R.id.textVilikesew21);
                holder.cmnt = (TextViewDrawableSize) v.findViewById(R.id.textVicmntew9);
                holder.view = (LinearLayout) v.findViewById(R.id.click_layout);
                holder.media_count = (TextViewDrawableSize) v.findViewById(R.id.list_media_count);
                holder.other = (RelativeLayout) v.findViewById(R.id.other_layout_head);
                holder.likes_layout = (LinearLayout) v.findViewById(R.id.bot);
                holder.title.setTypeface(objUsefullData.get_proxima_regusr());
                holder.date.setTypeface(objUsefullData.get_proxima_regusr());
                holder.likes.setTypeface(objUsefullData.get_proxima_regusr());
                holder.cmnt.setTypeface(objUsefullData.get_proxima_regusr());
                holder.media_count.setTypeface(objUsefullData.get_proxima_regusr());

                // Read your drawable from somewhere


                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }


            if (actorList.get(position).getpicture().equals("dumy")) {
                holder.title.setMaxLines(10);
                holder.title.setText(actorList.get(position).getdescription());
//                objUsefullData.make_toast(actorList.get(position).getdescription());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 30, 0, 80);
                holder.title.setLayoutParams(params);
                holder.imageviewhoome.setVisibility(View.GONE);
                holder.other.setBackground(getResources().getDrawable(R.drawable.background_dumy));
                holder.likes_layout.setVisibility(View.GONE);
                holder.media_count.setVisibility(View.GONE);
                holder.hide_dummy.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.hide_dummy.setVisibility(View.GONE);
                holder.likes_layout.setVisibility(View.VISIBLE);
                holder.title.setMaxLines(4);
                holder.imageviewhoome.setHeightRatio(0.7);
                holder.imageviewhoome.setVisibility(View.GONE);
                holder.media_count.setVisibility(View.GONE);

                if (actorList.get(position).getchild_pic().equals("true")) {
                    Drawable dr = getResources().getDrawable(R.mipmap.liked);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                    holder.likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                    holder.likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                }


                if (actorList.get(position).getdescription().equals("null")) {
                    holder.title.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(20, 20, 20, 20);
                    holder.imageviewhoome.setLayoutParams(params);
                } else {
                    holder.title.setVisibility(View.VISIBLE);
                    holder.title.setText(actorList.get(position).getdescription());
                }

                if (actorList.get(position).gettitle().equals("null")) {


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if(size==20){
                        params.setMargins(0, 110, 0, 110);
                    }else{
                        params.setMargins(0, 135, 0, 135);
                    }

                    holder.title.setLayoutParams(params);
                    holder.title.setVisibility(View.VISIBLE);


                } else {
                    holder.media_count.setText(""+actorList.get(position).getlikes2());
                    if(actorList.get(position).getlikes2()>1){
                        holder.media_count.setVisibility(View.VISIBLE);
                    }else {
                        holder.media_count.setVisibility(View.GONE);
                    }



                    Glide.with(getParent())
                            .load(actorList.get(position).gettitle())
                            .placeholder( R.mipmap.story_placeholer) //this is optional the image to display while the url image is downloading
                            .error( R.mipmap.story_placeholer)
                            .crossFade()//this is also optional if some error has occurred in downloading the image this image would be displayed
                            .into(holder.imageviewhoome);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 0, 10, 0);
                    holder.title.setLayoutParams(params);

                    RelativeLayout.LayoutParams paramss = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramss.setMargins(20, 20, 20, 20);
                    holder.imageviewhoome.setLayoutParams(paramss);

                    holder.imageviewhoome.setVisibility(View.VISIBLE);
                }



                holder.date.setText(actorList.get(position).getcreated_date());
                holder.likes.setText(String.valueOf(actorList.get(position).getlikes()));
                holder.cmnt.setText(String.valueOf(actorList.get(position).getcmnt_count()));

                if (actorList.get(position).getpicture().equalsIgnoreCase("Celebration")) {
                    holder.other.setBackground(getResources().getDrawable(R.drawable.background_card_purple));
                } else if (actorList.get(position).getpicture().equalsIgnoreCase("Milestone")) {
                    holder.other.setBackground(getResources().getDrawable(R.drawable.background_card_green));
                } else {
                    holder.other.setBackground(getResources().getDrawable(R.drawable.background_card));
                }

            }


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!swipeRefreshLayout.isRefreshing()) {

                        if(position==0 && actorList.get(position).getpicture().equals("dumy")){
                            Intent edit = new Intent(getParent(), Invite_buddies.class);
                            TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                            parentActivity.startChildActivity("invite_buddies_storyActivity", edit, false);

                        }else{

                            save_data.save(Definitions.CURRENT_POSITION,position);
                            save_data.save(Definitions.CURRENT_LIKE,actorList.get(position).getlikes());
                            save_data.save(Definitions.CURRENT_USER_LIKE,actorList.get(position).getchild_pic());
                            save_data.save(Definitions.CURRENT_COMMENT,actorList.get(position).getcmnt_count());

                            Intent edit = new Intent(getContext(), Post_details.class);
                            TabGroupActivity parentActivity = (TabGroupActivity) getContext();
                            save_data.save(Definitions.POST_DETAILS_REQUEST,true);
                            edit.putExtra("post_id", String.valueOf(actorList.get(position).getid()));
                            edit.putExtra("call", "story");
                            parentActivity.startChildActivity("postdetailsActivity", edit, true);


                        }
                    }
                }

            });



            holder.likes.setOnClickListener(new View.OnClickListener() {
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

            holder.hide_dummy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    remove_dummy();

                }
            });



            return v;

        }

        class ViewHolder {
            public DynamicHeightImageView imageviewhoome;
            public TextViewDrawableSize likes, cmnt, date, media_count;
            public TextView title;
            public LinearLayout view,likes_layout;
            public LinearLayout hide_dummy;
            public RelativeLayout other;


        }


    }

    public boolean updateListView(int position, String value ,int pic ) {

        if(header_available && header_visible){

            int firstPosition = mGridView.getFirstVisiblePosition() - mGridView.getHeaderViewsCount(); // This is the same as child #0
            int wantedChild = position - firstPosition;
//          Say, first visible position is 8, you want position 10, wantedChild will now be 2
//          So that means your view is child #2 in the ViewGroup:
            if (wantedChild < 0 || wantedChild >= mGridView.getChildCount()) {
//            Log.w(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
                return false;
            }
//           Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
            View convertView = mGridView.getChildAt(wantedChild);

            TextViewDrawableSize likes = (TextViewDrawableSize) convertView.findViewById(R.id.textVilikesew21);
            likes.setText(value);

            Drawable dr = getResources().getDrawable(pic);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
            likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

            return true;
        }else{
            int first = mGridView.getFirstVisiblePosition();
            int last = mGridView.getLastVisiblePosition();
            if(position < first || position > last) {
                //just update your DataSet
                //the next time getView is called
                //the ui is updated automatically
                return false;
            }
            else {
                View convertView = mGridView.getChildAt(position - first);
                //this is the convertView that you previously returned in getView
                //just fix it (for example:)


                TextViewDrawableSize likes = (TextViewDrawableSize) convertView.findViewById(R.id.textVilikesew21);
                likes.setText(value);


                Drawable dr = getResources().getDrawable(pic);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);



                return true;
            }
        }


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Hide the refresh after 2sec
                getParent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(objUsefullData.isNetworkConnected()) {

                            mGridView.setEnabled(false);
                            load_more=true;
                            more=1;
                            swipeRefreshLayout.setRefreshing(true);
                            get_post(false,"/story_posts?dummypost=1&page=1",false);


                        }else {
                            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
                        }
                    }
                });
            }
        }, 1000);
    }

    private void post_like(int post_id,final int pos)
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
                user.put("user_id", String.valueOf(save_data.getInt(Definitions.id)));
                user.put("story_post_id", post_id);

                request.put("story_post_like", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/story_post_likes", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            objUsefullData.firebase_analytics("likePost");
                            if(pos==-1){
                                Drawable dr = getResources().getDrawable(R.mipmap.liked);
                                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();


                                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                                head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

                                header_like_status=true;
                                int i=Integer.parseInt(head_likes.getText().toString())+1;
                                head_likes.setText(""+i);
                            }else{

                                try {
                                    updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.liked);
                                    actorsList.get(pos).setlikes(response.optInt("like_counts"));
                                    actorsList.get(pos).setchild_pic(""+response.optBoolean("you_like"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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
                user.put("story_post_id", post_id);

                request.put("story_post_like", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/story_post_unlikes", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();

                            Log.v("TAG", response.toString());

                            if(pos==-1){
                                Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();


                                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                                head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

                                header_like_status=false;
                                int i=Integer.parseInt(head_likes.getText().toString())-1;
                                head_likes.setText(""+i);
                            }else {
                                try {
                                    updateListView(pos, "" + response.optInt("like_counts"), R.mipmap.not_like);
                                    actorsList.get(pos).setlikes(response.optInt("like_counts"));
                                    actorsList.get(pos).setchild_pic("" + response.optBoolean("you_like"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

    private void remove_dummy()
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

            JSONObject user = new JSONObject();




            try {
                user.put("dummy_post", "false");


            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("TAG", user.toString());
            UserAPI.custom("/dummy_post_false", user, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();
                            if (objUsefullData.isNetworkConnected() ) {

                                get_post(true,"/story_posts?dummypost=1&page=1",false);
                            } else {
                                objUsefullData.make_toast("Please check your internet connection and try again");

                                if(actorsList.size()==0&&!header_available){
                                    not_found.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                }else {
                                    not_found.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                }
                            }




                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();




                        }
                    });
        }
    }

    public void init() {

        registerReceiver(new MyReceiver(), new IntentFilter("MyReceiver"));
        Ask.on(getParent())
                .forPermissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("Camera and storage Permission need for Image to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        size = objUsefullData.screen_size() + 3;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Dark, R.color.orange);
        swipeRefreshLayout.setSoundEffectsEnabled(true);
        progressOne = (IconRoundCornerProgressBar) findViewById(R.id.progress_one);


        title = (TextView) findViewById(R.id.not_ghgffxsound_txt);
        title.setTypeface(objUsefullData.get_proxima_regusr());
        progress_layer = (LinearLayout) findViewById(R.id.progress_layer);
        post_count = (TextView) findViewById(R.id.post_count);
        post_percentage = (TextView) findViewById(R.id.post_percentage);
        post_count.setTypeface(objUsefullData.get_proxima_regusr());
        post_percentage.setTypeface(objUsefullData.get_proxima_regusr());

        add_post_layout = (LinearLayout) findViewById(R.id.layout_add_post);
        not_found = (LinearLayout) findViewById(R.id.not_found_xxxlayout);
        not_found_create_btn = (LinearLayout) findViewById(R.id.not_btnxxx_layout);

        switch_child = (LinearLayout) findViewById(R.id.switch_child);

        create = (TextView) findViewById(R.id.not_fouxxxnd_txt);
        create.setTypeface(objUsefullData.get_proxima_regusr());


        add_post = (TextViewDrawableSize) findViewById(R.id.editText2qpwoei);
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        invite_click = (RelativeLayout) findViewById(R.id.invidfsdfdeteimageView10);
        profile = (RelativeLayout) findViewById(R.id.invidfxaView10);
        profile.setVisibility(View.VISIBLE);
        LayoutInflater layoutInflater = LayoutInflater.from(getParent());
        header = (View) layoutInflater.inflate(R.layout.listview_header, null);

        head_title = (TextView) header.findViewById(R.id.head_title);
        head_pic = (DynamicHeightImageView) header.findViewById(R.id.head_image);
        head_likes = (TextViewDrawableSize) header.findViewById(R.id.head_textVilikesew21);
        head_cmnt = (TextViewDrawableSize) header.findViewById(R.id.head_textVicmntew9);
        head_date = (TextViewDrawableSize) header.findViewById(R.id.head_textView2date2);
        head_media_count = (TextViewDrawableSize) header.findViewById(R.id.head_media_count);
        head_layout = (LinearLayout) header.findViewById(R.id.head_back_layout);
        header_click = (LinearLayout) header.findViewById(R.id.header_click);
        head_pic.setHeightRatio(0.7);
        header.setClickable(true);

        header.setOnTouchListener(new OnSwipeTouchListener(getParent()) {


            @Override
            public void onClick() {
                super.onClick();
                if (!swipeRefreshLayout.isRefreshing()) {

                    Intent edit = new Intent(getParent(), Post_details.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    edit.putExtra("post_id", String.valueOf(head_id));
                    edit.putExtra("call", "story");
                    save_data.save(Definitions.POST_DETAILS_REQUEST, true);
                    save_data.save(Definitions.CURRENT_POSITION, -1);
                    save_data.save(Definitions.CURRENT_LIKE, Integer.parseInt(head_likes.getText().toString()));
                    save_data.save(Definitions.CURRENT_USER_LIKE, String.valueOf(header_like_status));
                    save_data.save(Definitions.CURRENT_COMMENT, Integer.parseInt(head_cmnt.getText().toString()));

                    parentActivity.startChildActivity("postdetailsActivity", edit, true);

                }
                // your on click here
            }


        });



        user_pic = (SimpleDraweeView) findViewById(R.id.user_imageview);
        user_name = (TextView) findViewById(R.id.textView7);
        invite = (ImageView) findViewById(R.id.inviteimageView10);
        drop_icon = (ImageView) findViewById(R.id.xxx);

        head_title.setTypeface(objUsefullData.get_proxima_regusr());
        head_likes.setTypeface(objUsefullData.get_proxima_regusr());
        head_cmnt.setTypeface(objUsefullData.get_proxima_regusr());
        head_date.setTypeface(objUsefullData.get_proxima_regusr());
        head_media_count.setTypeface(objUsefullData.get_proxima_regusr());
        user_name.setTypeface(objUsefullData.get_ubntu_regular());
        add_post.setTypeface(objUsefullData.get_proxima_regusr());

    }






    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter(Multi_post_service.BROADCAST_ACTION));
        start_call++;
        if(refresh && !save_data.getBoolean(Definitions.POST_DETAILS_REQUEST)){
            onRefresh();
            save_data.save(Definitions.POST_DETAILS_REQUEST,false);
        }

        if(save_data.getBoolean(Definitions.POST_DETAILS_REQUEST)){

            if(save_data.getInt(Definitions.CURRENT_POSITION)==-1){

                head_cmnt.setText(""+save_data.getInt(Definitions.CURRENT_COMMENT));
                head_likes.setText(""+save_data.getInt(Definitions.CURRENT_LIKE));
                header_like_status=Boolean.parseBoolean(save_data.getString(Definitions.CURRENT_USER_LIKE));

                if (save_data.getString(Definitions.CURRENT_USER_LIKE).equals("true")) {
                    Drawable dr = getResources().getDrawable(R.mipmap.liked);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                    head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                    head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                }
                save_data.save(Definitions.POST_DETAILS_REQUEST, false);

            }else {
                actorsList.get(save_data.getInt(Definitions.CURRENT_POSITION)).setlikes(save_data.getInt(Definitions.CURRENT_LIKE));
                actorsList.get(save_data.getInt(Definitions.CURRENT_POSITION)).setchild_pic(save_data.getString(Definitions.CURRENT_USER_LIKE));
                actorsList.get(save_data.getInt(Definitions.CURRENT_POSITION)).setcmnt_count(save_data.getInt(Definitions.CURRENT_COMMENT));
                adapter.notifyDataSetChanged();
                save_data.save(Definitions.POST_DETAILS_REQUEST, false);
            }
        }

        if(!uploading){
            progress_layer.setVisibility(View.GONE);
            add_post.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(start_call>1) {
            refresh = true;
        }
        unregisterReceiver(broadcastReceiver);
    }
    @Override
    protected void onStart() {
        super.onStart();
        save_data.save(Definitions.POST_DETAILS_REQUEST,false);
    }

    private void updateUI(final Intent intent) {


        init_progress();
        int percent = intent.getIntExtra("percent",0);
        String status = intent.getStringExtra("status");

        add_post.setVisibility(View.GONE);
        progress_layer.setVisibility(View.VISIBLE);
        progressOne.setSecondaryProgress(percent);
        post_percentage.setText(percent+"%");
        if(status.equals("done")){
            progress_layer.setVisibility(View.GONE);
            add_post.setVisibility(View.VISIBLE);
            uploading=false;
            onRefresh();

        }
    }

    public void init_progress(){

        progressOne.setSecondaryProgressColor(getResources().getColor(R.color.colorPrimary));
        progressOne.setIconImageResource(R.mipmap.upload_image);
        progressOne.setIconPadding(10);
        progressOne.setIconBackgroundColor(getResources().getColor(R.color.custom_progress_purple_progress_half));
        progressOne.setProgressBackgroundColor(getResources().getColor(R.color.custom_progress_purple_progress_half));

    }


    private boolean listIsAtTop()   {
        if(mGridView.getChildCount() == 0) return true;
        return mGridView.getChildAt(0).getTop() == 0;
    }
}


