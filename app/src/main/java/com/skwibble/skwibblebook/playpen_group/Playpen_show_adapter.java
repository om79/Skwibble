package com.skwibble.skwibblebook.playpen_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.UsefullData;

import java.util.ArrayList;

public class Playpen_show_adapter extends ArrayAdapter<Actors> {
	ArrayList<Actors> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	UsefullData usefullData;

	public Playpen_show_adapter(Context context, int resource, ArrayList<Actors> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
		usefullData=new UsefullData(context);
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageviewhoome = (SimpleDraweeView) v.findViewById(R.id.imageView17);
			holder.group_name = (TextView) v.findViewById(R.id.textView26);
			holder.member_count = (TextView) v.findViewById(R.id.textView25);
			holder.admin = (TextView) v.findViewById(R.id.textView27);
			holder.time_ago = (TextView) v.findViewById(R.id.user_time);
			holder.admin.setTextColor(getContext().getResources().getColor(R.color.black));
			holder.imageviewhoome.getLayoutParams().height =100;
			holder.imageviewhoome.getLayoutParams().width =100;
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

			holder.group_name.setText(actorList.get(position).getdescription());
		    holder.member_count.setText(actorList.get(position).getcreated_date());
			holder.time_ago.setTypeface(usefullData.get_proxima_regusr());
			holder.group_name.setTypeface(usefullData.get_proxima_regusr());
			holder.member_count.setTypeface(usefullData.get_proxima_regusr());
			holder.admin.setTypeface(usefullData.get_proxima_regusr());

			if(actorList.get(position).getcomnt().equals("true"))
			{
				holder.admin.setText("Admin");
			}else {
				holder.admin.setText("");
			}

		if(!actorList.get(position).gettitle().equals("null")){
			holder.time_ago.setText(actorList.get(position).gettitle());
		}else {
			holder.time_ago.setText("");
		}



		holder.imageviewhoome.getLayoutParams().width=usefullData.screen_size()*5;
		holder.imageviewhoome.getLayoutParams().height=usefullData.screen_size()*5;


		holder.imageviewhoome.setImageURI(actorList.get(position).getpicture());


		return v;

	}

	static class ViewHolder {
		public SimpleDraweeView imageviewhoome;
		public TextView group_name,member_count,admin,time_ago;

	}

	
	}
	
	
	
	
	
	
	
	
	
