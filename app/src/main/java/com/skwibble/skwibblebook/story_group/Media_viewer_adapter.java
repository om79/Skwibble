package com.skwibble.skwibblebook.story_group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.journal_group.Videoplayer;
import com.skwibble.skwibblebook.utility.OnSwipeTouchListener;
import com.skwibble.skwibblebook.utility.UsefullData;

import java.io.File;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by POPLIFY on 10/20/2017.
 */

public class Media_viewer_adapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<Actors> image_arraylist;
    UsefullData usefullData;





    public Media_viewer_adapter(Activity activity, ArrayList<Actors> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        usefullData=new UsefullData(activity);


    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.viewer_item, container, false);
         final ImageViewTouch img_slider = (ImageViewTouch) view.findViewById(R.id.img_item_viewer);
        final LinearLayout thumb = (LinearLayout) view.findViewById(R.id.video_thumb_viewer);
        img_slider.setDisplayType(ImageViewTouchBase.DisplayType.FIT_WIDTH);


        if(usefullData.checkURL(image_arraylist.get(position).getpicture())){
            if(image_arraylist.get(position).getpara_1()){
                thumb.setVisibility(View.VISIBLE);
            }else {
                thumb.setVisibility(View.GONE);
            }
            Glide.with(activity)
                    .load(image_arraylist.get(position).getpicture())
                    .asBitmap()
                    .placeholder(R.mipmap.story_placeholer)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_slider);
//            img_slider.setOnTouchListener(new Touch());
            img_slider.setEnabled(true);

        }else {
            File imgFile = new  File(image_arraylist.get(position).getpicture());

            if(imgFile.exists()){
                boolean type=is_image(imgFile);
                if(type){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img_slider.setImageBitmap(myBitmap);
                    thumb.setVisibility(View.GONE);


                }else {
                    thumb.setVisibility(View.VISIBLE);
                    img_slider.setImageBitmap(ThumbnailUtils.createVideoThumbnail(image_arraylist.get(position).getpicture(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

                }
            }
        }

        img_slider.setOnTouchListener(new OnSwipeTouchListener(activity) {


            @Override
            public void onClick() {
                super.onClick();
//                    Toast.makeText(Media_viewer.this, "Single Tap", Toast.LENGTH_SHORT).show();
                if(Post_details.slider_image_list.get(position).getpara_1()) {
                    Intent i = new Intent(activity, Videoplayer.class);
                    i.putExtra("url",Post_details.slider_image_list.get(position).gettitle());
                    activity.startActivity(i);
                }

                // your on click here
            }


        });






        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }





    public boolean is_image(File file)
    {
        final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

        for (String extension : okFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }
}
