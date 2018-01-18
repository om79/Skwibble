package com.skwibble.skwibblebook.user_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.UsefullData;


/**
 * Created by om on 1/22/2016.
 */
public class Notification_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;
        UsefullData objUsefullData;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;



public Notification_adapter(Context context, int resource, ArrayList<Actors> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        objUsefullData = new UsefullData(context);



        }


@Override
public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
        holder = new ViewHolder();
        v = vi.inflate(Resource, null);
        holder.imageview = (SimpleDraweeView) v.findViewById(R.id.imageView4_user);
        holder.utext=(TextView) v.findViewById(R.id.textView6hh);
        holder.time=(TextView) v.findViewById(R.id.texdwew6hh);
        holder.back_layer=(LinearLayout) v.findViewById(R.id.noti_main_layout);


         v.setTag(holder);
        } else {
        holder = (ViewHolder) v.getTag();
        }

        if(actorList.get(position).getchild_pic().equals("true"))
        {
                holder.back_layer.setBackgroundColor(getContext().getResources().getColor(R.color.noti_back));

        }else {
                holder.back_layer.setBackgroundColor(0);
        }
        holder.utext.setText(actorList.get(position).gettitle());
        holder.time.setText(actorList.get(position).getcreated_date());
        holder.utext.setTypeface(objUsefullData.get_proxima_light());
        holder.time.setTypeface(objUsefullData.get_proxima_light());
        holder.imageview.setImageURI(actorList.get(position).getpicture());



        return v;

        }

static class ViewHolder {
    public SimpleDraweeView imageview;
    public LinearLayout back_layer;
    public TextView utext,time;


}




}