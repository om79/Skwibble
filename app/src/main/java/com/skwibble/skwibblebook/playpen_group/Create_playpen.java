package com.skwibble.skwibblebook.playpen_group;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.imagepicker.PickerBuilder;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.story_group.Invite_buddies;
import com.skwibble.skwibblebook.user_group.Add_child;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.SmoothCheckBox;
import com.skwibble.skwibblebook.utility.TabGroupActivity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class Create_playpen extends AppCompatActivity {

    SimpleDraweeView playpen_pic;
    UsefullData objUsefullData;
    ArrayList<Actors> actorsList=new ArrayList<Actors>();
    String compress_pic;
    SaveData save_data;
    Invite_adapter adapter;
    ExpandableHeightListView baddies_list;
    RelativeLayout create,back;
    EditText playpen_name;
    HashMap<String, String> map = new HashMap<String, String>();
    TextView default_txt,title,create_btn,label,label_select;
    LinearLayout not_found,not_found_create_btn;
    boolean has_image=false;
    int i=1;
    ScrollView main_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playpen);


        save_data = new SaveData(getParent());
        objUsefullData = new UsefullData(getParent());
        main_view=(ScrollView) findViewById(R.id.create_main_view);
        default_txt=(TextView) findViewById(R.id.not_xxxfound_txt);
        default_txt.setTypeface(objUsefullData.get_proxima_regusr());

        label=(TextView) findViewById(R.id.textVisjfpost);
        label.setTypeface(objUsefullData.get_proxima_regusr());

        label_select=(TextView) findViewById(R.id.textView30);
        label_select.setTypeface(objUsefullData.get_proxima_regusr());

        not_found=(LinearLayout)findViewById(R.id.not_found_layout);
        not_found_create_btn=(LinearLayout)findViewById(R.id.not_btn_laxyout);

        title=(TextView) findViewById(R.id.textsrView7);
        title.setTypeface(objUsefullData.get_proxima_regusr());

        create_btn=(TextView) findViewById(R.id.not_found_txt);
        create_btn.setTypeface(objUsefullData.get_proxima_regusr());

        if(!save_data.getBoolean(Definitions.has_child))
        {
            create_btn.setText("Add Child");
        }else {
            create_btn.setText("Add Buddy");
        }

        create=(RelativeLayout) findViewById(R.id.textVieggwffpost);
        back=(RelativeLayout) findViewById(R.id.imaqetcback);
        playpen_name=(EditText) findViewById(R.id.edipapachild_name);
        playpen_name.setTypeface(objUsefullData.get_proxima_regusr());

        playpen_pic=(SimpleDraweeView) findViewById(R.id.imageViewss6_img);
        baddies_list=(ExpandableHeightListView)findViewById(R.id.badies_list);

        adapter = new Invite_adapter(getParent(), R.layout.row_pending, actorsList);
        baddies_list.setExpanded(true);
        get_buddies_list();
        baddies_list.setAdapter(adapter);

        playpen_pic.setOnClickListener(new View.OnClickListener() {
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
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                objUsefullData.hideKeyboardFrom(getParent(),v);


                    if (TextUtils.isEmpty(playpen_name.getText().toString())) {
                        objUsefullData.make_toast("Playpen name can't be blank");

                    }else {

                        String text = playpen_name.getText().toString();
                        if(!text.equals("")) {
                            if (text.charAt(0) == ' ') {
                                objUsefullData.make_toast("Space is Removed");
                                playpen_name.setText(playpen_name.getText().toString().trim());
                                playpen_name.setSelection(playpen_name.getText().length());
                            }
                        }
                        submit_child();



                    }


            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sample", "Ripple completed");
                objUsefullData.hideKeyboardFrom(getParent(),v);
                finish();
            }

        });
        not_found_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(create_btn.getText().toString().equals("Add Child"))
                {
                    Intent edit = new Intent(getParent(), Add_child.class);
                    TabGroupActivity parentActivity = (TabGroupActivity) getParent();

                    parentActivity.startChildActivity("add_child_Activity", edit, false);

                }else {
                    Intent edit = new Intent(getParent(), Invite_buddies.class);
                    TabGroupActivity parentActivity = (TabGroupActivity)getParent();
                    parentActivity.startChildActivity("invite_buddies_storyActivity", edit, false);
                }

            }
        });

    }



    private void submit_child()
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
            Log.e("---",""+save_data.get(Definitions.auth_token));
            headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
            headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );




            JSONObject request = new JSONObject();
            JSONObject user = new JSONObject();

            JSONObject member = new JSONObject();
            JSONObject attibute= new JSONObject();
            try {

                user.put("name", playpen_name.getText().toString());
                try {
                    if(!compress_pic.equals("")) {
                        user.put("image", compress_pic);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    // print if found
                    StringTokenizer tokens = new StringTokenizer(""+pair.getValue(), ",");
//                    while(tokens.hasMoreTokens()) {
                    member.put("member_id", Integer.parseInt(tokens.nextToken()));
                    member.put("child_id", Integer.parseInt(tokens.nextToken()));

//                    }
                    member.put("_destroy", false);

                    attibute.put("" + objUsefullData.randInt(0, 999999), member);

                    System.out.println(pair.getKey() + " = " + pair.getValue());
                     }


                user.put("members_attributes",attibute);
                request.put("playpen", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("TAG", request.toString());

            UserAPI.post_JsonResp("/playpens", request, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            objUsefullData.dismissProgress();



                            objUsefullData.firebase_analytics("createPlaypen");

                            finish();



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

    private void get_buddies_list() {

        objUsefullData.showProgress();
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.version);
        headers.put( "X-User-Email", save_data.get(Definitions.user_email) );
        headers.put( "X-User-Token", save_data.get(Definitions.auth_token) );

        UserAPI.get_JsonObjResp("/playpens/new", headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);

                        if(actorsList.size()==0){
                            not_found.setVisibility(View.VISIBLE);
                            baddies_list.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            baddies_list.setVisibility(View.VISIBLE);
                        }
                        main_view.setVisibility(View.VISIBLE);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.make_toast("Somthing went wrong!");
                        if(actorsList.size()==0){
                            not_found.setVisibility(View.VISIBLE);
                            baddies_list.setVisibility(View.GONE);
                        }else {
                            not_found.setVisibility(View.GONE);
                            baddies_list.setVisibility(View.VISIBLE);
                        }
                        main_view.setVisibility(View.VISIBLE);
                    }
                });


    }

    private void set_up_values(JSONObject response)
    {
        try {
            JSONArray comments = response.getJSONArray("buddies");

            for (int i = 0; i < comments.length(); i++)
            {
                JSONObject in = comments.getJSONObject(i);
                Actors actor = new Actors();

                String name = in.optString("name");
                int id = in.optInt("member_id");
                String image = in.optString("image",null);
                String nick_name = in.optString("nick_name");
                int child_id = in.optInt("child_id");
                boolean _destroy = in.getBoolean("_destroy");


                actor.setpicture(image);
                actor.setid(id);
                actor.setlikes(child_id);
                actor.setdescription(name);
                actor.setcreated_date(nick_name);
                actor.setcomnt(""+_destroy);

                actorsList.add(actor);

            }



            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            e.printStackTrace();
        }

        objUsefullData.dismissProgress();


    }




    public class Invite_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;

        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        Context mcontext;


        public Invite_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
            mcontext=context;

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new Invite_adapter.ViewHolder();
                v = vi.inflate(Resource, null);


                holder.imageview = (SimpleDraweeView) v.findViewById(R.id.imageView10);
                holder.uname=(TextView) v.findViewById(R.id.textView23);
                holder.udisplay_id=(TextView) v.findViewById(R.id.textView22);
                holder.seen=(SmoothCheckBox) v.findViewById(R.id.checkBox4_last);
                holder.uname.setTextColor(getContext().getResources().getColor(R.color.black));
                holder.udisplay_id.setTextColor(getContext().getResources().getColor(R.color.black));



                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.uname.setText(actorList.get(position).getdescription());
            holder.udisplay_id.setText(actorList.get(position).getcreated_date());
            holder.uname.setTypeface(objUsefullData.get_proxima_light());
            holder.udisplay_id.setTypeface(objUsefullData.get_proxima_light());

            holder.imageview.getLayoutParams().width=objUsefullData.screen_size()*4;
            holder.imageview.getLayoutParams().height=objUsefullData.screen_size()*4;

            holder.imageview.setImageURI(actorList.get(position).getpicture());




            holder.seen.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    if (isChecked == true) {




                        map.put(String.valueOf(position), ""+actorList.get(position).getid()+","+actorList.get(position).getlikes());


                        Log.e("check","--");
                    } else {
                        Log.e("uncheck","--");
                        map.remove(String.valueOf(position));

                    }


                }
            });






            return v;

        }



        class ViewHolder {
            public SimpleDraweeView imageview;
            public SmoothCheckBox seen;
            public TextView uname,udisplay_id;


        }







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
                                        playpen_pic.setImageURI(imageUri);
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
                                        playpen_pic.setImageURI(imageUri);
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
                    playpen_pic.setImageResource(R.mipmap.create_playpen_pic);
                    compress_pic="";
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
