package com.skwibble.skwibblebook.utility;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skwibble.skwibblebook.R;

/**
 * Created by rajneesh on 3/11/14.
 */
public class GridViewAdaterForAttachementMenu extends BaseAdapter{


    private Context mContext;
    public UsefullData usefullData;
    private List<MenuPopupWindowItems> items=new ArrayList<MenuPopupWindowItems>();

        // Constructor
        public GridViewAdaterForAttachementMenu(Context c,List<MenuPopupWindowItems>mItems) {
            mContext = c;
            items=mItems;
        }

        public int getCount() {
            return items.size();
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        View popupView;
        MenuPopupWindowItems items=(MenuPopupWindowItems)getItem(position);
            if (convertView == null) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                 popupView = layoutInflater.inflate(R.layout.grid_itemfor_menu, null);


               // popupView.setLayoutParams(new GridView.LayoutParams(200, 200));


            } else {
                popupView = (View) convertView;
            }
            LinearLayout ll=(LinearLayout)popupView;
        if(position==0||position==3){
            ll.setGravity(Gravity.CENTER);
        }
           
            final TextView text=(TextView)popupView.findViewById(R.id.menu_grid_text);
            usefullData=new UsefullData(mContext);
            text.setTypeface(usefullData.get_proxima_regusr());
            text.setText(items.getMenu_text());

            return popupView;
        }

}
