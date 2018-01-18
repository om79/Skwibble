package com.skwibble.skwibblebook.view_pager;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.user_group.LoginActivity;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;

/**
 * Created by POPLIFY on 5/17/2016.
 */
public class Joyride_4 extends Fragment
{


    UsefullData usefullData;
    SaveData saveData;
    public Joyride_4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.joyride, container, false);

        usefullData=new UsefullData(getActivity());
        saveData=new SaveData(getActivity());
        TextView v = (TextView) rootView.findViewById(R.id.text_joyride);
        v.setText(R.string.joride_4);
        v.setTypeface(usefullData.get_proxima_regusr());
        ImageView m = (ImageView) rootView.findViewById(R.id.imageView4_joyride1);
        m.setImageResource(R.mipmap.joyride4);
        TextView vs = (TextView) rootView.findViewById(R.id.start);
        if(saveData.isExist(Definitions.app_install_first)==true) {
            vs.setVisibility(View.GONE);
        }else {
            vs.setVisibility(View.VISIBLE);
        }

        vs.setTypeface(usefullData.get_proxima_regusr());

        vs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData.save(Definitions.app_install_first,"app_installed");
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }




}
