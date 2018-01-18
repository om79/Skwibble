package com.skwibble.skwibblebook.user_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;



import java.util.ArrayList;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Actors;
import com.skwibble.skwibblebook.utility.UsefullData;

public class Child_adapter extends ArrayAdapter<Actors> {
	ArrayList<Actors> actorList;
	UsefullData objUsefullData;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	

	public Child_adapter(Context context, int resource, ArrayList<Actors> objects) {
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
			holder.name=(TextView) v.findViewById(R.id.texewsView11);
			holder.sbling_name=(TextView) v.findViewById(R.id.texedfsdgwsView11);
			holder.dob=(TextView) v.findViewById(R.id.texeppew11);

			holder.name_label=(TextView) v.findViewById(R.id.ddddigjm);
			holder.s_name_label=(TextView) v.findViewById(R.id.dddsqjm);
			holder.dob_label=(TextView) v.findViewById(R.id.ddssigjm);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.name.setText(actorList.get(position).getdescription());
		holder.sbling_name.setText(actorList.get(position).getcreated_date());
		if(actorList.get(position).getcomnt().equals("null"))
		{
			holder.dob.setText("");
		}else {
			holder.dob.setText(actorList.get(position).getcomnt());
		}


		holder.name.setTypeface(objUsefullData.get_proxima_light());
		holder.sbling_name.setTypeface(objUsefullData.get_proxima_light());
		holder.dob.setTypeface(objUsefullData.get_proxima_light());
		holder.name_label.setTypeface(objUsefullData.get_proxima_light());
		holder.s_name_label.setTypeface(objUsefullData.get_proxima_light());
		holder.dob_label.setTypeface(objUsefullData.get_proxima_light());

		
		
		return v;

	}

	static class ViewHolder {
		public TextView name,dob,sbling_name,name_label,dob_label,s_name_label;

	}



	
}