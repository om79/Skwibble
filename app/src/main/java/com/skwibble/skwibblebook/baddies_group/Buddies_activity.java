package com.skwibble.skwibblebook.baddies_group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.story_group.Post_details;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.OnSwipeTouchListener;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buddies_activity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    public StaggeredGridView mGridView;
    HomeAdapter adapter;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    UsefullData objUsefullData;
    SaveData save_data;
    TextView head_title,head_child_name,not_found_txt_first,not_found_txt_sec;
    DynamicHeightImageView head_pic;
    TextViewDrawableSize head_likes,head_cmnt,head_date,head_media_count;
    View header;
    int head_id;
    LinearLayout head_layout;
    SimpleDraweeView head_child_image;
    SimpleDraweeView user_pic;
    TextView user_name,title;
    ImageView invite,switch_child;
    RelativeLayout invite_click;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout not_found,add_post_layout;
    boolean header_like_status,header_available=false,header_visible=true,load_more=true;
    int size=20,more=1;
    Button not_enter_code,not_share;
    boolean refresh=false;
    int start_call=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_layout);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Dark,R.color.orange);

        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());

        if(!save_data.isExist(Definitions.show_feed_invite)) {

            objUsefullData.showpopup(R.mipmap.show_feed_invite,Definitions.show_feed_invite);
        }



        size=objUsefullData.screen_size()+3;
        title = (TextView) findViewById(R.id.not_ghgffxsound_txt);
        title.setText(R.string.buddies_post_not_found);
        title.setTypeface(objUsefullData.get_proxima_regusr());

        not_found = (LinearLayout) findViewById(R.id.not_found_xxxladddddyout);
        add_post_layout = (LinearLayout) findViewById(R.id.layout_add_post);
        add_post_layout.setVisibility(View.GONE);

        not_enter_code = (Button) findViewById(R.id.not_enter_code);
        not_share = (Button) findViewById(R.id.not_share);
        not_enter_code.setTypeface(objUsefullData.get_ubntu_regular());
        not_share.setTypeface(objUsefullData.get_ubntu_regular());

        not_found_txt_first = (TextView) findViewById(R.id.not_found_txt_first);
        not_found_txt_sec = (TextView) findViewById(R.id.not_found_txt_sec);
        not_found_txt_first.setTypeface(objUsefullData.get_proxima_regusr());
        not_found_txt_sec.setTypeface(objUsefullData.get_proxima_regusr());

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        invite = (ImageView) findViewById(R.id.inviteimageView10);
        LayoutInflater layoutInflater = LayoutInflater.from(getParent());
        header = (View) layoutInflater.inflate(R.layout.buddies_listview_header, null);
        head_title = (TextView) header.findViewById(R.id.buddies_head_title);
        head_pic = (DynamicHeightImageView) header.findViewById(R.id.buddies_head_image);
        head_likes = (TextViewDrawableSize) header.findViewById(R.id.buddies_head_textVilikesew21);
        head_cmnt = (TextViewDrawableSize) header.findViewById(R.id.buddies_head_textVicmntew9);
        head_date = (TextViewDrawableSize) header.findViewById(R.id.buddies_head_textView2date2);
        head_media_count = (TextViewDrawableSize) header.findViewById(R.id.buddies_head_media_count);

        head_layout = (LinearLayout) header.findViewById(R.id.buddies_head_back_layout);
        head_child_name = (TextView) header.findViewById(R.id.header_textView22);
        head_child_image = (SimpleDraweeView) header.findViewById(R.id.header_imageView10);
        switch_child = (ImageView) findViewById(R.id.xxx);
        switch_child.setVisibility(View.GONE);

        head_title.setTypeface(objUsefullData.get_proxima_regusr());
        head_likes.setTypeface(objUsefullData.get_proxima_regusr());
        head_cmnt.setTypeface(objUsefullData.get_proxima_regusr());
        head_date.setTypeface(objUsefullData.get_proxima_regusr());
        head_child_name.setTypeface(objUsefullData.get_proxima_regusr());
        head_media_count.setTypeface(objUsefullData.get_proxima_regusr());

        head_pic.setHeightRatio(0.7);
        user_pic = (SimpleDraweeView) findViewById(R.id.user_imageview);
        user_name = (TextView) findViewById(R.id.textView7);
        user_name.setTypeface(objUsefullData.get_proxima_regusr());
        user_pic.setVisibility(View.GONE);
        user_name.setText("Feed");
        user_name.setTypeface(objUsefullData.get_proxima_regusr());
        invite.setImageResource(R.mipmap.buddies_invite);

        adapter = new HomeAdapter(getParent(), R.layout.buddies_list_item_sample, actorsList);


        try {
            if (objUsefullData.isNetworkConnected()) {
                get_post(true,"/merge_feed?page=1",false);
            } else {
                objUsefullData.showMsgOnUI("Please check your internet connection and try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        not_enter_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getParent(), Add_budddies.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                edit.putExtra("first_time","false");
                parentActivity.startChildActivity("buddiinvitesActivity", edit,false);
            }
        });

        not_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objUsefullData.share_options("Hey! Have you seen Skwibble? It's a great way to record your child's memories. Check it out: https://skwibble.com");

            }
        });

        header.setClickable(true);

        header.setOnTouchListener(new OnSwipeTouchListener(getParent()) {


            @Override
            public void onClick() {
                super.onClick();

                if (!swipeRefreshLayout.isRefreshing()) {
                    Intent edit = new Intent(getParent(), Post_details.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    save_data.save(Definitions.POST_DETAILS_REQUEST, true);
                    save_data.save(Definitions.CURRENT_POSITION, -1);
                    save_data.save(Definitions.CURRENT_LIKE, Integer.parseInt(head_likes.getText().toString()));
                    save_data.save(Definitions.CURRENT_USER_LIKE, String.valueOf(header_like_status));
                    save_data.save(Definitions.CURRENT_COMMENT, Integer.parseInt(head_cmnt.getText().toString()));

                    edit.putExtra("post_id", String.valueOf(head_id));
                    edit.putExtra("call", "feed");
                    parentActivity.startChildActivity("buddies_postdetailsActivity", edit, true);
                }
            }
        });
        invite_click = (RelativeLayout) findViewById(R.id.invidfsdfdeteimageView10);
        invite_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                Intent edit = new Intent(getParent(), Buddies_invite_activity.class);
                TabGroupActivity parentActivity = (TabGroupActivity) getParent();


                parentActivity.startChildActivity("invite_Activity", edit,false);
            }

        });



        head_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(header_like_status)
                {
                    post_unlike(head_id,-1);

                }else {
                    post_like(head_id,-1);
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

                        if(load_more) {
                            more++;
                            get_post(true, "/merge_feed?page=" + more, true);
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

    @Override
    protected void onStart() {
        super.onStart();
        save_data.save(Definitions.POST_DETAILS_REQUEST,false);
    }

    private void get_post(final boolean loader,final String url,final boolean more) {

        if (loader) {
            objUsefullData.showProgress();
        }
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
                        if (!loader) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }

                        if(actorsList.size()==0 && !header_available){
                            not_found.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!loader ) {

                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            objUsefullData.dismissProgress();
                        }
                        objUsefullData.showMsgOnUI("Not Found");
                        if(actorsList.size()==0 && !header_available){
                            not_found.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private void set_up_values(JSONObject response, boolean more)
    {
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

            for (int i = 0; i < comments.length(); i++)
            {


                JSONObject in = comments.getJSONObject(i);

                String date = in.optString("date");
                int id = in.optInt("id");
                String content = in.optString("content","null");
                int like_counts = in.optInt("like_counts");
                int comment_counts = in.optInt("comment_counts");
                int attachment_count = in.optInt("attachment_count");
                String child_name = in.optString("child_name");
                String child_image_url = in.optString("child_image_url");
                boolean you_like = in.optBoolean("you_like");
//                String post_type = in.getString("post_type");
                String privacy_type = in.optString("privacy_type");
                String default_post_type = in.optString("default_post_type");
                String media_url = in.optString("media_url","null");

                if(i==0  && !content.equals("null") && !media_url.equals("null") && !more){
                    head_id=id;
                    head_title.setText(content);
                    head_date.setText(""+date);
                    head_cmnt.setText(""+comment_counts);

                    head_likes.setText(""+like_counts);
                    head_child_name.setText(""+child_name);
                    header_like_status=you_like;

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
                    head_child_image.setImageURI(child_image_url);


                    if(header_like_status) {
                        Drawable dr = getResources().getDrawable(R.mipmap.liked);
                        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                        head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                    }else {
                        Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                        head_likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                    }



                    if(default_post_type.equalsIgnoreCase("Celebration"))
                    {
                        head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card_purple));
                    }else if(default_post_type.equalsIgnoreCase("Milestone"))
                    {
                        head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card_green));
                    }else {
                        head_layout.setBackground(getResources().getDrawable(R.drawable.header_background_card));
                    }

                    mGridView.removeHeaderView(header);
                    mGridView.setAdapter(null);
                    mGridView.addHeaderView(header, null, false);
                    header_available=true;
                }else {


                    Actors actor = new Actors();

                    actor.settitle(media_url);
                    actor.setdescription(content);
                    actor.setid(id);
                    actor.setlikes(like_counts);
                    actor.setcmnt_count(comment_counts);
                    actor.setcreated_date(date);
                    actor.setpicture(default_post_type);
                    actor.setusername(child_name);
                    actor.setchild_pic(child_image_url);
                    actor.setcomnt(""+you_like);
                    actor.setlikes2(attachment_count);

                    actorsList.add(actor);
                }

            }



            mGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mGridView.invalidateViews();

        } catch (JSONException e) {
            e.printStackTrace();
            objUsefullData.dismissProgress();
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

                holder.imageviewhoome = (DynamicHeightImageView) v.findViewById(R.id.buddies_image);
                holder.title = (TextView) v.findViewById(R.id.buddies_title);
                holder.date = (TextViewDrawableSize) v.findViewById(R.id.buddies_textView2date2);
                holder.likes = (TextViewDrawableSize) v.findViewById(R.id.buddies_textVilikesew21);
                holder.cmnt = (TextViewDrawableSize) v.findViewById(R.id.buddies_textVicmntew9);
                holder.view = (RelativeLayout) v.findViewById(R.id.buddies_click_layout);
                holder.media_count = (TextViewDrawableSize) v.findViewById(R.id.buddies_list_media_count);
                holder.other = (LinearLayout) v.findViewById(R.id.buddies_other_layout_head);
                holder.child_name = (TextView) v.findViewById(R.id.buddies_textView22);
                holder.child_pic = (SimpleDraweeView) v.findViewById(R.id.buddies_imageView10);

                holder.title.setTypeface(objUsefullData.get_proxima_regusr());
                holder.date.setTypeface(objUsefullData.get_proxima_regusr());
                holder.likes.setTypeface(objUsefullData.get_proxima_regusr());
                holder.cmnt.setTypeface(objUsefullData.get_proxima_regusr());
                holder.child_name.setTypeface(objUsefullData.get_proxima_regusr());
                holder.media_count.setTypeface(objUsefullData.get_proxima_regusr());

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.child_pic.setImageURI(actorList.get(position).getchild_pic());

            if(actorList.get(position).gettitle().equals("null")) {
                holder.imageviewhoome.setVisibility(View.GONE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if(size==20){
                    params.setMargins(0, 90, 0, 90);
                }else{
                    params.setMargins(0, 115, 0, 115);
                }
                holder.title.setLayoutParams(params);
                holder.media_count.setVisibility(View.GONE);
            }else{
                holder.media_count.setText(""+actorList.get(position).getlikes2());
                if(actorList.get(position).getlikes2()>1){
                    holder.media_count.setVisibility(View.VISIBLE);
                }else {
                    holder.media_count.setVisibility(View.GONE);
                }
                holder.imageviewhoome.setVisibility(View.VISIBLE);

                Glide.with(getParent())
                        .load(actorList.get(position).gettitle())
                        .placeholder( R.mipmap.story_placeholer) //this is optional the image to display while the url image is downloading
                        .error( R.mipmap.story_placeholer)
                        .crossFade()//this is also optional if some error has occurred in downloading the image this image would be displayed
                        .into(holder.imageviewhoome);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                holder.title.setLayoutParams(params);

                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(20, 10, 20, 0);
                holder.imageviewhoome.setLayoutParams(param);
            }

            holder.imageviewhoome.setHeightRatio(0.7);
            if(actorList.get(position).getdescription().equals("null")){
                holder.title.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 20, 20, 20);
                holder.imageviewhoome.setLayoutParams(params);
            }else {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText("" + actorList.get(position).getdescription());
            }
            holder.date.setText(""+actorList.get(position).getcreated_date());
            holder.likes.setText(""+actorList.get(position).getlikes());
            holder.cmnt.setText(""+actorList.get(position).getcmnt_count());
            holder.child_name.setText(""+actorList.get(position).getusername());

            if(actorList.get(position).getpicture().equalsIgnoreCase("Celebration"))
            {
                holder.other.setBackground(getResources().getDrawable(R.drawable.background_card_purple));
            }else if(actorList.get(position).getpicture().equalsIgnoreCase("Milestone"))
            {
                holder.other.setBackground(getResources().getDrawable(R.drawable.background_card_green));
            }else {
                holder.other.setBackground(getResources().getDrawable(R.drawable.background_card));
            }


            if(actorList.get(position).getcomnt().equals("true")) {
                Drawable dr = getResources().getDrawable(R.mipmap.liked);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                holder.likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
            }else {
                Drawable dr = getResources().getDrawable(R.mipmap.not_like);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));

                holder.likes.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
            }


//            holder.view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Intent edit = new Intent(getParent(), Post_details.class);
//                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
//                    save_data.save(Definitions.POST_DETAILS_REQUEST,true);
//                    edit.putExtra("post_id",String.valueOf(actorList.get(position).getid()));
//                    edit.putExtra("call", "feed");
//                    parentActivity.startChildActivity("buddies_postdetailsActivity", edit,true);
//
//
//
//                }
//
//
//
//            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    save_data.save(Definitions.CURRENT_POSITION,position);
                    save_data.save(Definitions.CURRENT_LIKE,actorList.get(position).getlikes());
                    save_data.save(Definitions.CURRENT_USER_LIKE,actorList.get(position).getchild_pic());
                    save_data.save(Definitions.CURRENT_COMMENT,actorList.get(position).getcmnt_count());

                    Intent edit = new Intent(getParent(), Post_details.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    save_data.save(Definitions.POST_DETAILS_REQUEST,true);
                    edit.putExtra("call", "feed");
                    edit.putExtra("post_id",String.valueOf(actorList.get(position).getid()));
                    parentActivity.startChildActivity("buddies_postdetailsActivity", edit, true);
                }
            });


            holder.likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(actorsList.get(position).getcomnt().equals("true"))
                    {
                        post_unlike(actorsList.get(position).getid(),position);
                    }else {
                        post_like(actorsList.get(position).getid(),position);
                    }


                }
            });




            return v;

        }

        class ViewHolder {
            public DynamicHeightImageView imageviewhoome;
            public TextViewDrawableSize likes,cmnt,date,media_count;
            public TextView title,child_name;
            public SimpleDraweeView child_pic;
            public RelativeLayout view;
            public LinearLayout other;

        }


    }


    @Override
    public void onRefresh() {

        if(objUsefullData.isNetworkConnected()) {

            swipeRefreshLayout.setRefreshing(true);
            load_more=true;
            more=1;
            get_post(false,"/merge_feed?page=1",false);

        }else {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        }
    }


    private void post_like(int post_id,final int pos)
    {

        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.showMsgOnUI("No Internet Connection");
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
                                    actorsList.get(pos).setcomnt(""+response.optBoolean("you_like"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            objUsefullData.firebase_analytics("likePost");

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
            objUsefullData.showMsgOnUI("No Internet Connection");
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
                                    actorsList.get(pos).setcomnt("" + response.optBoolean("you_like"));
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




    public boolean updateListView(int position, String value ,int pic ) {

//        objUsefullData.make_toast("HeaderViewsCount-"+mGridView.getHeaderViewsCount()+"position-"+position);

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


                TextViewDrawableSize likes = (TextViewDrawableSize) convertView.findViewById(R.id.buddies_textVilikesew21);
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
    public void onResume() {
        super.onResume();

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
                actorsList.get(save_data.getInt(Definitions.CURRENT_POSITION)).setcomnt(save_data.getString(Definitions.CURRENT_USER_LIKE));
                actorsList.get(save_data.getInt(Definitions.CURRENT_POSITION)).setcmnt_count(save_data.getInt(Definitions.CURRENT_COMMENT));
                adapter.notifyDataSetChanged();
                save_data.save(Definitions.POST_DETAILS_REQUEST, false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        refresh=true;

    }
    private boolean listIsAtTop()   {
        if(mGridView.getChildCount() == 0) return true;
        return mGridView.getChildAt(0).getTop() == 0;
    }


}
