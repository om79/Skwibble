package com.skwibble.skwibblebook.view_pager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.UsefullData;


/**
 * Created by POPLIFY on 5/17/2016.
 */
public class Joyride_1 extends Fragment  {


    UsefullData usefullData;
    public Joyride_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.joyride, container, false);
        usefullData=new UsefullData(getActivity());
        TextView v = (TextView) rootView.findViewById(R.id.text_joyride);
        v.setText(R.string.joride_1);
        v.setTypeface(usefullData.get_proxima_regusr());
        ImageView m = (ImageView) rootView.findViewById(R.id.imageView4_joyride1);
        m.setImageResource(R.mipmap.joyride1);

        return rootView;
    }






}
