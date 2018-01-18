package com.skwibble.skwibblebook.journal_group;

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


public class Journal_adapter extends ArrayAdapter<Actors> {
	ArrayList<Actors> actorList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;
	UsefullData objUsefullData;

	public Journal_adapter(Context context, int resource, ArrayList<Actors> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		actorList = objects;
		objUsefullData=new UsefullData(context);
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageviewhoome = (SimpleDraweeView) v.findViewById(R.id.imageView20);
			holder.month = (TextView) v.findViewById(R.id.monthtxt);
			holder.disable_months = (LinearLayout) v.findViewById(R.id.dim_disable);
			holder.month.setTypeface(objUsefullData.get_ubntu_bold());
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		holder.month.setText(actorList.get(position).getdescription());
		holder.imageviewhoome.setImageURI(actorList.get(position).getpicture());

		if (actorList.get(position).gettitle().equals("true")){
			holder.disable_months.setVisibility(View.GONE);
		} else{
			holder.disable_months.setVisibility(View.VISIBLE);
		}

		return v;

	}

	static class ViewHolder {
		public SimpleDraweeView imageviewhoome;
		public TextView month;
		public LinearLayout disable_months;
	}

	
	}
	
	
	
	
	
	
	
	
	
