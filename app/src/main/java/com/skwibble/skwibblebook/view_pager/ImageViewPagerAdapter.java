package com.skwibble.skwibblebook.view_pager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;


/**
 * Created by nirmal on 12/08/15.
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    SaveData save_data;
    public static int totalPage = 5;

    public ImageViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;
        save_data=new SaveData(_context);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();

        switch (position) {

            case 0:
                f = new Joyride_1();
                break;
            case 1:
                f = new Joyride_2();
                break;
            case 2:
                f = new Joyride_3();
                break;
            case 3:
                f = new Joyride_4();
                break;
            case 4:
                f = new Joyride_5();
                break;
        }

        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}

