package com.skwibble.skwibblebook.journal_group;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.ImageCompression;
import com.skwibble.skwibblebook.media_upload.Single_media_picker;
import com.skwibble.skwibblebook.media_upload.Single_trimmer;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Months_details extends AppCompatActivity {

    static ArrayList<Actors> actorsList=new ArrayList<Actors>();
    Journal_adapter adapter;
    GridView gv;
    UsefullData objUsefullData;
    TextView month_name;
    SaveData save_data;
    RelativeLayout back;
    Intent play;
    boolean load_more=true;
    int more=1,min_date;
    boolean normal_back= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_details);

        play= getIntent();
        actorsList.clear();
        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());


        Single_media_picker.singlearrayMediaPath.clear();
        Single_trimmer.done_path="";

        back=(RelativeLayout) findViewById(R.id.imaqseyfffcback);

        month_name = (TextView) findViewById(R.id.texdfdftView7);
        month_name.setTypeface(objUsefullData.get_ubntu_regular());
        gv=(GridView)findViewById(R.id.lvuxsfdfdser);
        adapter = new Journal_adapter(getParent(), R.layout.row_months, actorsList);

        try {


            get_months_details("/show_jrnl_months_attchments/"+play.getStringExtra("month_id")+"?&page=1", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gv.setAdapter(adapter);
        gv.setLongClickable(true);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id)
            {

                if(position==0){
                    Ask.on(getParent()).forPermissions(Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withRationales("In order to save file you will need to grant storage permission") //optional
                            .go();

                    Intent i=new Intent(getParent(), Single_media_picker.class);
                    i.putExtra(Definitions.IMAGE_REQUEST,"Journal");
                    startActivity(i);
                }else {

                    Intent edit = new Intent(getParent(), Show_image.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();
                    edit.putExtra("pic", actorsList.get(position).getpicture());
                    edit.putExtra("date", actorsList.get(position).gettitle());
                    edit.putExtra("pos", String.valueOf(position));
                    edit.putExtra("id", String.valueOf(actorsList.get(position).getid()));
                    edit.putExtra("thumb", actorsList.get(position).getcreated_date());
                    edit.putExtra("is_video", actorsList.get(position).getuser());
                    edit.putExtra("min_date", min_date);
                    parentActivity.startChildActivity("show_Activity", edit, false);

                }

            }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {


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
                                image_delete(String.valueOf(actorsList.get(position).getid()),position);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();


                            }
                        })
                        .show();

                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                onBackPressed();




            }

        });


        gv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) { // TODO Auto-generated method stub
                int threshold = 1;
                int count = gv.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (gv.getLastVisiblePosition() >= count
                            - threshold) {
                        // Execute LoadMoreDataTask AsyncTask

                        if(load_more) {
                            more++;

                            get_months_details("/show_jrnl_months_attchments/"+play.getStringExtra("month_id")+"?&page="+more,true);

                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Single_media_picker.singlearrayMediaPath.isEmpty()) {

            String paths = "";
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            for (String object : Single_media_picker.singlearrayMediaPath) {
                // do something with object
                ImageCompression i=new ImageCompression(getParent());
                paths=i.compressImage(object);
            }
            String type=objUsefullData.getMimeType(paths);

            upload(play.getStringExtra("month_id"),date,paths,type);
            Single_media_picker.singlearrayMediaPath.clear();

        }else if(!Single_trimmer.done_path.isEmpty()){

            String paths = Single_trimmer.done_path;
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String type=objUsefullData.getMimeType(paths);

            upload(play.getStringExtra("month_id"),date,paths,type);
            Single_trimmer.done_path="";
        }else {
            get_months_details("/show_jrnl_months_attchments/"+play.getStringExtra("month_id")+"?&page=1",false);

        }



    }


    private void get_months_details(String url,final boolean more) {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );


        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        set_up_values(response, more);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        save_data.save(Definitions.current_noti_request,"");
                        objUsefullData.dismissProgress();
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
            min_date=response.optInt("child_dob_year");
            month_name.setText(response.optString("month_name"));
            JSONArray comments = response.getJSONArray("attachments");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);

//                if(i!=0) {
                    Actors actor = new Actors();


                    int jrnl_month_id = in.optInt("jrnl_month_id");
                    String image = in.optString("media_url");
                    String thumb = in.optString("thumb");
                    int id = in.optInt("id");
                    String media_updated_at = in.optString("media_updated_at");
                    boolean is_video = in.optBoolean("is_video");

                    actor.setlikes(jrnl_month_id);
                    actor.setpicture(image);
                    actor.settitle(media_updated_at);
                    actor.setid(id);
                    actor.setuser(""+is_video);
                    actor.setcreated_date(thumb);

                    actorsList.add(actor);
//                }

            }


            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();

        if(save_data.getString(Definitions.current_noti_request).equals("Months_details")){

            save_data.save(Definitions.current_noti_request,"");
            normal_back=false;

        }
    }



    public class Journal_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;

        public Journal_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.imageviewhoome = (ImageView) v.findViewById(R.id.imageView20mm);
                holder.thumb = (ImageView) v.findViewById(R.id.is_video);

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            if(actorList.get(position).getuser().equals("true")){
                holder.thumb.setVisibility(View.VISIBLE);
            }else {
                holder.thumb.setVisibility(View.GONE);
            }

            if(position==0){
                holder.imageviewhoome.setImageResource(R.mipmap.open_gallery);
            }else {
                Glide.with(getParent())
                        .load(actorList.get(position).getcreated_date())
                        .asBitmap()
                        .placeholder(R.mipmap.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageviewhoome);
//                image_not_found
//                holder.imageviewhoome.setImageURI(actorList.get(position).getcreated_date());

            }

            return v;

        }

        class ViewHolder {
            public ImageView imageviewhoome;
            public ImageView thumb;

        }


    }


    private void image_delete(String post_id,final int pos)
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

            UserAPI.delete_StringResp("/jrnl_attachments/"+post_id,headers, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            objUsefullData.dismissProgress();
                            actorsList.remove(pos);
                            adapter.notifyDataSetChanged();

                new SweetAlertDialog(getParent(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Image deleted successfully!")
                        .show();



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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!normal_back){
            Intent intent = new Intent(getParent(), Tab_activity.class);
            intent.putExtra("from",2);
            startActivity(intent);
//            finish();

        }else{

//            finish();
        }
    }


    public void upload(final String ids,final String dates,final String paths,final String content_type){

        objUsefullData.showProgress();
        Ion.with(getParent())
                .load("POST", Definitions.APIdomain + Definitions.JOURNAL_UPLOAD_URL)
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
                .setMultipartParameter("jrnl_attachment[jrnl_month_id]", ids)

                .setMultipartParameter("jrnl_attachment[date]", dates)
                .setMultipartFile("jrnl_attachment[media]",content_type, new File(paths))



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

                            get_months_details("/show_jrnl_months_attchments/"+play.getStringExtra("month_id")+"?&page=1",false);

                        }
                    }
                });
    }
}
