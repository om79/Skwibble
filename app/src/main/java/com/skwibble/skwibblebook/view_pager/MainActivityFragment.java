package com.skwibble.skwibblebook.view_pager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private ImageView _btn1, _btn2, _btn3,_btn4,_btn5;
    LinearLayout cross;
    SaveData save_data;

    public MainActivityFragment() {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        save_data= new SaveData(getActivity());
        setUpView();
        setTab();
        onCircleButtonClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void onCircleButtonClick() {

        _btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mViewPager.setCurrentItem(0);
            }
        });

        _btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mViewPager.setCurrentItem(1);
            }
        });
        _btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mViewPager.setCurrentItem(2);
            }
        });
        _btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mViewPager.setCurrentItem(3);
            }
        });
        _btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mViewPager.setCurrentItem(4);
            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();

            }
        });


    }

    private void setUpView() {
        _mViewPager = (ViewPager) getView().findViewById(R.id.imageviewPager);
        _adapter = new ImageViewPagerAdapter(getActivity(), getFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int savePosition;
            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                _btn1.setImageResource(R.mipmap.grey_dot);
                _btn2.setImageResource(R.mipmap.grey_dot);
                _btn3.setImageResource(R.mipmap.grey_dot);
                _btn4.setImageResource(R.mipmap.grey_dot);
                _btn5.setImageResource(R.mipmap.grey_dot);
                    btnAction(position);


            }

        });

    }

    private void btnAction(int action) {


        switch (action) {

            case 0:
                _btn1.setImageResource(R.mipmap.orange_dot);
                break;
            case 1:
                _btn2.setImageResource(R.mipmap.orange_dot);
                break;
            case 2:
                _btn3.setImageResource(R.mipmap.orange_dot);
                break;
            case 3:
                _btn4.setImageResource(R.mipmap.orange_dot);
                break;
            case 4:
                _btn5.setImageResource(R.mipmap.orange_dot);
                break;

        }



    }

    private void initButton() {
        _btn1 = (ImageView) getView().findViewById(R.id.btn1);
        _btn2 = (ImageView) getView().findViewById(R.id.btn2);
        _btn3 = (ImageView) getView().findViewById(R.id.btn3);
        _btn4 = (ImageView) getView().findViewById(R.id.btn4);
        _btn5 = (ImageView) getView().findViewById(R.id.btn5);
        _btn1.setImageResource(R.mipmap.orange_dot);
        cross = (LinearLayout) getView().findViewById(R.id.back_play);
    }

    private void setButton(Button btn, String text, int h, int w) {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }


}
