package com.skwibble.skwibblebook.story_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.TextViewDrawableSize;
import com.skwibble.skwibblebook.utility.UsefullData;

public class Comment_adapter extends ArrayAdapter<Actors> {
	ArrayList<Actors> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	UsefullData objUsefullData;

	public Comment_adapter(Context context, int resource, ArrayList<Actors> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
		objUsefullData=new UsefullData(context);
	}
 
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageviewhoome = (SimpleDraweeView) v.findViewById(R.id.imageViewqwe10);
			holder.title = (TextView) v.findViewById(R.id.textView2asdf5);
			holder.cmnt = (TextView) v.findViewById(R.id.textView2oufdg4);
			holder.date = (TextViewDrawableSize) v.findViewById(R.id.textVivbkkew23);
			holder.title.setTypeface(objUsefullData.get_proxima_regusr());
			holder.cmnt.setTypeface(objUsefullData.get_proxima_light());
			holder.date.setTypeface(objUsefullData.get_proxima_light());

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		holder.imageviewhoome.setImageURI(actorList.get(position).getpicture());


		holder.title.setText(actorList.get(position).gettitle());
		holder.cmnt.setText(actorList.get(position).getcomnt());
		holder.date.setText(actorList.get(position).getdescription());


		
		return v;

	}

	static class ViewHolder {
		public SimpleDraweeView imageviewhoome;
		public TextView title,cmnt;
		public TextViewDrawableSize date;


	}


	}
	
	
	
	
	
	
	
	
	
