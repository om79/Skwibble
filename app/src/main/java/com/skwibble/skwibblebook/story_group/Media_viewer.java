package com.skwibble.skwibblebook.story_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.ClickableViewPager;

public class Media_viewer extends AppCompatActivity {

    private ClickableViewPager vp_slider;
    private LinearLayout ll_dots;
    Media_viewer_adapter Adapter;
    Intent i;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_viewer);

        i=getIntent();
        index=i.getIntExtra("position",0);

        vp_slider = (ClickableViewPager) findViewById(R.id.media_viewer);

        Adapter = new Media_viewer_adapter(Media_viewer.this, Post_details.slider_image_list);
        vp_slider.setAdapter(Adapter);
        vp_slider.setCurrentItem(index);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        vp_slider.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // your code
//                if(Post_details.slider_image_list.get(pos).getpara_1()) {
//                    Intent i = new Intent(Media_viewer.this, Videoplayer.class);
//                    i.putExtra("url",Post_details.slider_image_list.get(pos).gettitle());
//                    startActivity(i);
//                }
//
//            }
//        });

//        vp_slider.setOnTouchListener(new OnSwipeTouchListener(this) {
//
//
//            @Override
//            public void onClick() {
//                super.onClick();
////                    Toast.makeText(Media_viewer.this, "Single Tap", Toast.LENGTH_SHORT).show();
//                if(Post_details.slider_image_list.get(pos).getpara_1()) {
//                    Intent i = new Intent(Media_viewer.this, Videoplayer.class);
//                    i.putExtra("url",Post_details.slider_image_list.get(pos).gettitle());
//                    startActivity(i);
//                }
//
//                // your on click here
//            }
//
//            @Override
//            public void onSwipeDown() {
////                    Toast.makeText(CustomPlayerControlActivity.this, "Left", Toast.LENGTH_SHORT).show();
//          finish();
//            }
//
//
//            @Override
//            public void onSwipeUp() {
////                    Toast.makeText(CustomPlayerControlActivity.this, "Right", Toast.LENGTH_SHORT).show();
//               finish();
//            }
//        });


    }





//    public class SliderPagerAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//        Activity activity;
//        ArrayList<Actors> image_arraylist;
//        UsefullData usefullData;
//
//
//
//        public SliderPagerAdapter(Activity activity, ArrayList<Actors> image_arraylist) {
//            this.activity = activity;
//            this.image_arraylist = image_arraylist;
//            usefullData=new UsefullData(activity);
//
//
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, final int position) {
//            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(R.layout.item, container, false);
//            final SimpleDraweeView img_slider = (SimpleDraweeView) view.findViewById(R.id.img_item);
//            final LinearLayout thumb = (LinearLayout) view.findViewById(R.id.video_thumb);
//
//            if(usefullData.checkURL(image_arraylist.get(position).getpicture())){
//                if(image_arraylist.get(position).getpara_1()){
//                    thumb.setVisibility(View.VISIBLE);
//                }else {
//                    thumb.setVisibility(View.GONE);
//                }
//                img_slider.setImageURI(image_arraylist.get(position).getpicture());
//            }else {
//                File imgFile = new  File(image_arraylist.get(position).getpicture());
//
//                if(imgFile.exists()){
//                    boolean type=is_image(imgFile);
//                    if(type){
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        img_slider.setImageBitmap(myBitmap);
//                        thumb.setVisibility(View.GONE);
//
//                    }else {
//                        thumb.setVisibility(View.VISIBLE);
//                        img_slider.setImageBitmap(ThumbnailUtils.createVideoThumbnail(image_arraylist.get(position).getpicture(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
//
//                    }
//                }
//            }
//
//
//            img_slider.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(Post_details.slider_image_list.get(position).getpara_1()) {
//                        Intent i = new Intent(activity, Videoplayer.class);
//                        i.putExtra("url",Post_details.slider_image_list.get(position).gettitle());
//                        activity.startActivity(i);
//                    }
//
//
//                }
//            });
//
//
//
//
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return image_arraylist.size();
//        }
//
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//
//
//
//
//
//        public boolean is_image(File file)
//        {
//            final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
//
//            for (String extension : okFileExtensions)
//            {
//                if (file.getName().toLowerCase().endsWith(extension))
//                {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }
}

