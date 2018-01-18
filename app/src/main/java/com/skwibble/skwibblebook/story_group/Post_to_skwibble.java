package com.skwibble.skwibblebook.story_group;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.droid.mediamultiselector.activity.MediaSelectorActivity;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.ImageCompression;
import com.skwibble.skwibblebook.media_upload.Multi_post_service;
import com.skwibble.skwibblebook.media_upload.TrimmerActivity;
import com.skwibble.skwibblebook.media_upload.Update_multi_post_service;
import com.skwibble.skwibblebook.utility.ClickableViewPager;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post_to_skwibble extends Activity {


    TextView date, photo, post_type, privacy;
    TextViewDrawableSize click_date, click_photo, click_post_type, click_privacy_draw;
    LinearLayout date_lay, photo_lay, post_type_lay, privacy_lay, make_post;
    UsefullData objUsefullData;
    SaveData save_data;
    HashMap<String, String> map_post_type = new HashMap<String, String>();
    HashMap<String, String> map_privacy = new HashMap<String, String>();

    String request;
    TextView post, title_txt;
    EditText content;
    RelativeLayout back;//,slider_back;
    int i = 1;
    ImageView preview;
    private ClickableViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    private TextView[] dots;
    Intent intent;
    public static ArrayList<Actors> slider_image_list = new ArrayList<Actors>();
    ArrayList<String> trimmer_list = new ArrayList<String>();
    List<String> deletion_image_list = new ArrayList<String>();

    public  ArrayList<String> arrayMediaPath = new ArrayList<>();
    public static ArrayList<String> trimmer_done_list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_skwibble);

        objUsefullData = new UsefullData(Post_to_skwibble.this);
        save_data = new SaveData(Post_to_skwibble.this);

        slider_image_list.clear();
        deletion_image_list.clear();
        arrayMediaPath.clear();


        if(!save_data.isExist(Definitions.show_add_post)) {
            objUsefullData.showpopup(R.mipmap.show_add_post,Definitions.show_add_post);
        }

        intent = getIntent();
        request = intent.getStringExtra("intent");

//        slider_back = (RelativeLayout) findViewById(R.id.LinearLayout1);
        date_lay = (LinearLayout) findViewById(R.id.date_lay);
        photo_lay = (LinearLayout) findViewById(R.id.photo_lay);
        post_type_lay = (LinearLayout) findViewById(R.id.post_type_lay);
        privacy_lay = (LinearLayout) findViewById(R.id.privacy_lay);

        click_date = (TextViewDrawableSize) findViewById(R.id.datesdfsgd);
        click_photo = (TextViewDrawableSize) findViewById(R.id.datesphotodfsgd);
        click_post_type = (TextViewDrawableSize) findViewById(R.id.datesphotpostodfsgd);
        click_privacy_draw = (TextViewDrawableSize) findViewById(R.id.datesphotpoprivacystodfsgd);

        click_date.setTypeface(objUsefullData.get_proxima_regusr());
        click_photo.setTypeface(objUsefullData.get_proxima_regusr());
        click_post_type.setTypeface(objUsefullData.get_proxima_regusr());
        click_privacy_draw.setTypeface(objUsefullData.get_proxima_regusr());

        date = (TextView) findViewById(R.id.textView_date);
        title_txt = (TextView) findViewById(R.id.texsssView7);
        photo = (TextView) findViewById(R.id.textView_Photos);
        post_type = (TextView) findViewById(R.id.textView_posttype);
        post = (TextView) findViewById(R.id.textView10_post);
        privacy = (TextView) findViewById(R.id.textView_privacy);
        content = (EditText) findViewById(R.id.editText);

        make_post = (LinearLayout) findViewById(R.id.make_post);
        back = (RelativeLayout) findViewById(R.id.imaqseyetcback);
        preview = (ImageView) findViewById(R.id.datesphotimageodfsgd);

        date.setTypeface(objUsefullData.get_proxima_regusr());
        photo.setTypeface(objUsefullData.get_proxima_regusr());
        post_type.setTypeface(objUsefullData.get_proxima_regusr());
        post.setTypeface(objUsefullData.get_proxima_regusr());
        privacy.setTypeface(objUsefullData.get_proxima_regusr());
        content.setTypeface(objUsefullData.get_proxima_regusr());
        title_txt.setTypeface(objUsefullData.get_proxima_regusr());

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                onBackPressed();
            }

        });

        date_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat();

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                sdf.format(c.getTime());
                DatePickerDialog dialog = new DatePickerDialog(Post_to_skwibble.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        photo_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (request.equals("clone_playpen")) {
                    photo_lay.setEnabled(false);
                    ll_dots.setVisibility(View.INVISIBLE);
                }else {

                    if (Definitions.MULTI_MEDIA_COUNT == slider_image_list.size()) {
                        objUsefullData.make_toast("You reached maximum limit.");

                    } else {
                        arrayMediaPath.clear();
                        MediaSelectorActivity.startActivityForResult(Post_to_skwibble.this, Definitions.REQUEST_CODE_MEDIA_SELECT,
                                MediaSelectorActivity.SELECTION_MODE_MULTI, Definitions.MULTI_MEDIA_COUNT - slider_image_list.size(), MediaSelectorActivity.MEDIA_TYPE_ALL,
                                true, true, arrayMediaPath);

                    }
                }



            }
        });


        post_type_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_popup("post_type");

            }
        });


        privacy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_popup("privacy");




            }


        });


        try {
            if (request.equals("edit_post")) {

                title_txt.setText("Edit Post");
                post.setText("Update");
                edit_post("/story_posts/" + intent.getStringExtra("id") + "/edit");

//                if(slider_image_list.isEmpty()){
//                    slider_back.setBackgroundColor(getResources().getColor(R.color.white));
//                }else {
//                    slider_back.setBackgroundColor(getResources().getColor(R.color.dark_black));
//                }
            } else if (request.equals("clone_playpen")) {


                title_txt.setText("Share to Story");
                post.setText("Share");
                clone_post("/clone_playpen_post", intent.getStringExtra("id"));

//                if(slider_image_list.isEmpty()){
//                    slider_back.setBackgroundColor(getResources().getColor(R.color.white));
//                }else {
//                    slider_back.setBackgroundColor(getResources().getColor(R.color.dark_black));
//                }
            } else {
                get_post_type();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        make_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (content.getText().toString().trim().equals("") && slider_image_list.isEmpty()) {


                    objUsefullData.showMsgOnUI("Text or Media is required to post");

                } else {
                    if (post.getText().toString().trim().equals("Update")) {

                        update_post(intent.getStringExtra("id"));

                    } else {

                        post_for_story();

                    }

                }

            }
        });
    }



    @Override
    protected void onResume() {

            if(!trimmer_done_list.isEmpty()){
                for (String object : trimmer_done_list) {
                    // do something with object
                    Actors actor = new Actors();

                    actor.setpicture(object);
                    actor.setPara_1(false);
                    actor.setcreated_date("-1");
                    actor.settitle("false");
                    slider_image_list.add(actor);
                }

                sliderPagerAdapter = new SliderPagerAdapter(this);
                vp_slider.setAdapter(sliderPagerAdapter);
                sliderPagerAdapter.notifyDataSetChanged();
                photo.setText(" "+slider_image_list.size()+" out of 6 media Selected");
                trimmer_done_list.clear();

            }

//            if(slider_image_list.isEmpty()){
//                slider_back.setBackgroundColor(getResources().getColor(R.color.white));
//            }else {
//                slider_back.setBackgroundColor(getResources().getColor(R.color.dark_black));
//            }

        super.onResume();
    }




    private void post_for_story() {

        if(request.equals("clone_playpen")){

            clone_story();

        }else {

            Intent intent = new Intent(this, Multi_post_service.class);
            intent.putExtra("content", content.getText().toString().trim());
            intent.putExtra("date", date.getText().toString().trim());
            intent.putExtra("post_type", map_post_type.get(post_type.getText().toString().trim()));
            intent.putExtra("privacy", map_privacy.get(privacy.getText().toString().trim()));

            startService(intent);
            save_data.save(Definitions.POST_DETAILS_REQUEST,true);
            objUsefullData.showMsgOnUI("Your post will be uploaded shortly");
            finish();

        }







    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
//            Date dates = new Date(year - 1900, monthOfYear, dayOfMonth);
//            String d = String.format("%1$td %1$tB %1$tY", dates);
//
//            date.setText(" " + d);
            date.setText(" "+objUsefullData.set_date(dayOfMonth,monthOfYear,year));


        }
    }

    private void edit_post(String url) {

        objUsefullData.showProgress();
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put("X-User-Email", save_data.get(Definitions.user_email));
        headers.put("X-User-Token", save_data.get(Definitions.auth_token));


        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        objUsefullData.dismissProgress();
                        set_up_values_edit(response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Not Found");
                    }
                });


    }

    private void clone_post(String url, String id) {

        objUsefullData.showProgress();
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put("X-User-Email", save_data.get(Definitions.user_email));
        headers.put("X-User-Token", save_data.get(Definitions.auth_token));


        JSONObject request = new JSONObject();

        try {
            request.put("playpen_post_id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserAPI.post_JsonResp(url, request, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        objUsefullData.dismissProgress();
                        set_up_values_clone(response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Not Found");
                    }
                });


    }

    private void get_post_type() {

        objUsefullData.showProgress();
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put("X-User-Email", save_data.get(Definitions.user_email));
        headers.put("X-User-Token", save_data.get(Definitions.auth_token));



        UserAPI.get_JsonObjResp("/story_posts/new", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        objUsefullData.dismissProgress();
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


    private void set_up_values(JSONObject response) {
        Log.e("response----", "" + response);

        try {

            date.setText(" " + response.optString("date"));
            JSONArray comments = response.getJSONArray("post_types");

            for (int i = 0; i < comments.length(); i++) {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean def = in.optBoolean("default");
                if (def) {

                    post_type.setText(" " + name);

                }
                map_post_type.put(name, String.valueOf(id));
            }

            JSONArray privacies = response.getJSONArray("privacies");

            for (int i = 0; i < privacies.length(); i++) {
                JSONObject inn = privacies.getJSONObject(i);
                String name = inn.optString("name");
                int id = inn.optInt("id");
                boolean def = inn.optBoolean("default");
                if (def) {

                    privacy.setText(" " + name);

                }
                map_privacy.put(name, String.valueOf(id));
            }
            JSONArray children = response.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                JSONObject inm = children.getJSONObject(i);


                boolean active = inm.optBoolean("active");
                if (active == true) {

                    String s = inm.optString("name");
                    if (s.length() > 10) {
                        s = s.substring(0, 10) + "..";
                    }
                    title_txt.setText("Post to " + s + "\'s Story");
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void show_popup(final String type) {
        String[] result = null;
        final Dialog dialog = new Dialog(this);
        if (type.equals("post_type")) {
            result = map_post_type.keySet().toArray(new String[0]);
        } else {
            result = map_privacy.keySet().toArray(new String[0]);
        }
        objUsefullData.dismissProgress();


        dialog.setTitle("Select Privacy");
        dialog.setContentView(R.layout.categories);
        final ListView listCategories = (ListView) dialog
                .findViewById(R.id.listCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Post_to_skwibble.this, R.layout.list_category, R.id.textCat,
                result);
        listCategories.setAdapter(adapter);//
        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                String cat = (String) listCategories
                        .getItemAtPosition(position);


                if (type.equals("post_type")) {
                    post_type.setText(" " + cat);
                } else {
                    privacy.setText(" " + cat);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void set_up_values_edit(JSONObject response) {
        Log.e("response----", "" + response);

        try {

            if (!response.getJSONObject("post").isNull("content")) {
                content.setText(response.getJSONObject("post").optString("content"));
            }

            date.setText(" " + response.optString("date"));

            JSONArray comments = response.getJSONArray("post_types");

            for (int i = 0; i < comments.length(); i++) {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean def = in.getBoolean("default");
                if (def) {

                    post_type.setText(" " + name);

                }
                map_post_type.put(name, String.valueOf(id));
            }

            JSONArray privacies = response.getJSONArray("privacies");

            for (int i = 0; i < privacies.length(); i++) {
                JSONObject inn = privacies.getJSONObject(i);
                String name = inn.optString("name");
                int id = inn.optInt("id");
                boolean def = inn.getBoolean("default");
                if (def) {

                    privacy.setText(" " + name);

                }
                map_privacy.put(name, String.valueOf(id));
            }

            if (!response.getJSONObject("post").isNull("story_post_attachments")) {

                JSONArray attachments = response.getJSONObject("post").getJSONArray("story_post_attachments");

                for (int i = 0; i < attachments.length(); i++) {
                    Actors actor = new Actors();

                    JSONObject in = attachments.getJSONObject(i);
                    String media_url = in.optString("media_url");
                    int id = in.optInt("id");
                    boolean is_video = in.optBoolean("is_video");

                    actor.setpicture(media_url);
                    actor.setPara_1(is_video);
                    actor.setcreated_date(""+id);
                    actor.settitle("false");

                    slider_image_list.add(actor);
                }

            }



            photo.setText(" "+slider_image_list.size()+" out of 6 media Selected");
            sliderPagerAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void set_up_values_clone(JSONObject response) {
        Log.e("response----", "" + response);

        try {

            if (!response.getJSONObject("post").optString("content").equals("null")) {
                content.setText(response.getJSONObject("post").optString("content"));
            }

            date.setText(" " + response.optString("date"));

            JSONArray comments = response.getJSONArray("post_types");

            for (int i = 0; i < comments.length(); i++) {
                JSONObject in = comments.getJSONObject(i);
                String name = in.optString("name");
                int id = in.optInt("id");
                boolean def = in.getBoolean("default");
                if (def ) {

                    post_type.setText(" " + name);

                }
                map_post_type.put(name, String.valueOf(id));
            }

            JSONArray privacies = response.getJSONArray("privacies");

            for (int i = 0; i < privacies.length(); i++) {
                JSONObject inn = privacies.getJSONObject(i);
                String name = inn.optString("name");
                int id = inn.optInt("id");
                boolean def = inn.getBoolean("default");
                if (def) {

                    privacy.setText(" " + name);

                }
                map_privacy.put(name, String.valueOf(id));
            }

            if (!response.getJSONObject("post").isNull("media_url")) {


                Actors actor = new Actors();


                String media_url = response.getJSONObject("post").optString("media_url");
                int id = response.getJSONObject("post").optInt("id");
                boolean is_video = response.getJSONObject("post").optBoolean("is_video");

                actor.setpicture(media_url);
                actor.setPara_1(is_video);
                actor.setcreated_date(""+id);
                actor.settitle("false");

                slider_image_list.add(actor);
                Glide.with(this)
                        .load(media_url)
                        .placeholder(R.mipmap.add_media)
                        .error(R.mipmap.add_media)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(preview);




            }

            if(!slider_image_list.isEmpty()) {
                photo.setText(" Media selected");
            }else {
                photo.setText(" No media selected");
            }
            sliderPagerAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void update_post(String id) {

        if (!objUsefullData.isNetworkConnected()) {
            objUsefullData.showMsgOnUI("Please check your internet connection and try again");
        } else {
            Intent i = new Intent(Post_to_skwibble.this, Update_multi_post_service.class);
            i.putExtra("content", content.getText().toString().trim());
            i.putExtra("date", date.getText().toString().trim());
            i.putExtra("post_type", map_post_type.get(post_type.getText().toString().trim()));
            i.putExtra("privacy",map_privacy.get(privacy.getText().toString().trim()));
            i.putExtra("url",Definitions.APIdomain+Definitions.STORY_FILE_UPLOAD_URL+"/"+id);
            i.putExtra("deleted_ids",deletion_image_list.toString().replace("[","").replace("]",""));
            i.setAction(Multi_post_service.BROADCAST_ACTION);
            startService(i);
            save_data.save(Definitions.POST_DETAILS_REQUEST,true);
            objUsefullData.showMsgOnUI("Your post will be uploaded shortly");
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void init() {


        vp_slider = (ClickableViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        sliderPagerAdapter = new SliderPagerAdapter(this);
        vp_slider.setAdapter(sliderPagerAdapter);
        photo.setText(" "+slider_image_list.size()+" out of 6 media Selected");

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                addBottomDots(position);
            }

            @Override
            public void onPageSelected(int position) {

//                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

    }


    private void addBottomDots(int currentPage) {
        if (request.equals("clone_playpen")) {
            ll_dots.setVisibility(View.INVISIBLE);
        }else {
            ll_dots.setVisibility(View.VISIBLE);
        }

        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#BF979797"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#ffffff"));
    }


    public class SliderPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        Activity activity;




        public SliderPagerAdapter(Activity activity) {
            this.activity = activity;



        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_post_to_skwibble, container, false);
            final ImageView img_slider = (ImageView) view.findViewById(R.id.img_item_post);
            final LinearLayout thumb = (LinearLayout) view.findViewById(R.id.video_thumb_post);
            final ImageView delete_img = (ImageView) view.findViewById(R.id.delete_image_post);
            final ImageView thumb_img = (ImageView) view.findViewById(R.id.mnb_post);
            thumb_img.setImageResource(R.mipmap.video_thumb);
            thumb.setGravity(Gravity.LEFT);

            if (request.equals("clone_playpen")) {
                delete_img.setVisibility(View.INVISIBLE);
            }else {
                delete_img.setVisibility(View.VISIBLE);
            }


            if(objUsefullData.checkURL(slider_image_list.get(position).getpicture())){
                if(slider_image_list.get(position).getpara_1()){
                    thumb.setVisibility(View.VISIBLE);
                }else {
                    thumb.setVisibility(View.GONE);
                }
                Glide.with(activity)
                        .load(slider_image_list.get(position).getpicture())
                        .asBitmap()
                        .placeholder(R.mipmap.story_placeholer)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img_slider);
            }else {
                File imgFile = new  File(slider_image_list.get(position).getpicture());

                if(imgFile.exists()){
                    boolean type=objUsefullData.is_image(imgFile);
                    if(type){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        img_slider.setImageBitmap(myBitmap);
                        thumb.setVisibility(View.GONE);

                    }else {
                        thumb.setVisibility(View.VISIBLE);
                        img_slider.setImageBitmap(ThumbnailUtils.createVideoThumbnail(slider_image_list.get(position).getpicture(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

                    }
                }
            }





            delete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!slider_image_list.get(position).getcreated_date().equals("-1")){
                        deletion_image_list.add(slider_image_list.get(position).getcreated_date());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            slider_image_list.remove(position);

                            sliderPagerAdapter = new SliderPagerAdapter(Post_to_skwibble.this);
                            vp_slider.setAdapter(sliderPagerAdapter);
                            sliderPagerAdapter.notifyDataSetChanged();
                            if(slider_image_list.size()==0){
                                ll_dots.setVisibility(View.INVISIBLE);
                            }
                            if (request.equals("clone_playpen")) {
                                photo.setText(" No media Selected");

                            }else {
                                photo.setText(" "+slider_image_list.size()+" out of 6 media Selected");

                            }

//                            if(slider_image_list.isEmpty()){
//                                slider_back.setBackgroundColor(getResources().getColor(R.color.white));
//                            }else {
//                                slider_back.setBackgroundColor(getResources().getColor(R.color.dark_black));
//                            }


                        }
                    });


                }
            });




            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {

            return slider_image_list.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        slider_image_list.clear();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode ==  Definitions.REQUEST_CODE_MEDIA_SELECT) {
            arrayMediaPath = data.getStringArrayListExtra(MediaSelectorActivity.RESULTS_SELECTED_MEDIA);


            trimmer_list.clear();
            new post_media_work().execute();

//            for (String obj : arrayMediaPath) {
//                String type = objUsefullData.getMimeType(obj);
//                if (type.equalsIgnoreCase("video/mp4")) {
//                    trimmer_list.add(obj);
//                } else {
//
//                    Actors actor = new Actors();
//                    ImageCompression i=new ImageCompression(this);
//                    actor.setpicture(i.compressImage(obj));
//                    actor.setPara_1(false);
//                    actor.setcreated_date("-1");
//                    actor.settitle("false");
//                    slider_image_list.add(actor);
//                }
//            }
//            sliderPagerAdapter = new SliderPagerAdapter(this);
//            vp_slider.setAdapter(sliderPagerAdapter);
//            sliderPagerAdapter.notifyDataSetChanged();
//            photo.setText(" " + slider_image_list.size() + " out of 6 media Selected");
//
//
//            if(!trimmer_list.isEmpty()){
//                for(String s:trimmer_list) {
//                    Intent intent = new Intent(this, TrimmerActivity.class);
//                    intent.putExtra(Definitions.EXTRA_VIDEO_PATH, s);
//                    startActivity(intent);
//                }
//            }
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

    private void clone_story() {


        objUsefullData.showProgress();

        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        JSONObject request = new JSONObject();
        JSONObject user_info = new JSONObject();

        try {
            user_info.put("content", content.getText().toString().trim());

            user_info.put("date",  date.getText().toString().trim());
            user_info.put("playpen_post_id", intent.getStringExtra("id"));
            user_info.put("post_type_id",  map_post_type.get(post_type.getText().toString().trim()));
            user_info.put("privacy_id",  map_privacy.get(privacy.getText().toString().trim()));
            request.put("story_post", user_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        UserAPI.post_JsonResp("/story_posts",request,headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        objUsefullData.dismissProgress();
                        if(post_type.getText().toString().trim().equals("Milestone")){
                            objUsefullData.firebase_analytics("postMilestone");
                        }else if(post_type.getText().toString().trim().equals("Story")){
                            objUsefullData.firebase_analytics("postStory");
                        }else if(post_type.getText().toString().trim().equals("Celebration")){
                            objUsefullData.firebase_analytics("postCelebration");
                        }

                        if(privacy.getText().toString().trim().equals("Only Me")){
                            objUsefullData.firebase_analytics("postMe");
                        }else if(privacy.getText().toString().trim().equals("Everyone")){
                            objUsefullData.firebase_analytics("postEveryone");

                        }
                        objUsefullData.make_toast("Yay! your Playpen post was successfully added to your story!");
                        finish();


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Unsuccessful Post");
                    }
                });

    }

    class post_media_work extends AsyncTask<String, Void, Boolean> {


        protected void onPreExecute() {

            objUsefullData.showProgress();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                for (String obj : arrayMediaPath) {
                    String type = objUsefullData.getMimeType(obj);
                    if (type.equalsIgnoreCase("video/mp4")) {
                        trimmer_list.add(obj);
                    } else {

                        Actors actor = new Actors();
                        ImageCompression i=new ImageCompression(Post_to_skwibble.this);
                        actor.setpicture(i.compressImage(obj));
                        actor.setPara_1(false);
                        actor.setcreated_date("-1");
                        actor.settitle("false");
                        slider_image_list.add(actor);
                    }
                }


                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            sliderPagerAdapter = new SliderPagerAdapter(Post_to_skwibble.this);
            vp_slider.setAdapter(sliderPagerAdapter);
            sliderPagerAdapter.notifyDataSetChanged();
            photo.setText(" " + slider_image_list.size() + " out of 6 media Selected");


            if(!trimmer_list.isEmpty()){
                for(String s:trimmer_list) {
                    Intent intent = new Intent(Post_to_skwibble.this, TrimmerActivity.class);
                    intent.putExtra(Definitions.EXTRA_VIDEO_PATH, s);
                    startActivity(intent);
                }
            }
        objUsefullData.dismissProgress();
        }
    }

}
