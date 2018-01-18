package com.skwibble.skwibblebook.journal_group;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khizar1556.mkvideoplayer.MKPlayer;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.UsefullData;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by khizar1556 on 10/6/2017.
 */

public class Videoplayer extends AppCompatActivity {
    private MKPlayer player;
//    UsefullData usefullData;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);
        player=new MKPlayer(this);
//        usefullData=new UsefullData(this);





        player.onComplete(new Runnable() {
            @Override
            public void run() {
                //callback when video is finish
                finish();
            }
        }).onInfo(new MKPlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //download speed
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
                        break;
                }
            }
        }).onError(new MKPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {

//                usefullData.showMsgOnUI("video play error");
            }
        });
        player.setPlayerCallbacks(new MKPlayer.playerCallbacks() {
            @Override
            public void onNextClick() {
//                try {
//                    if(Show_image.current_video_url!=null) {
//                        player.play(url);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onPreviousClick() {

//                try {
//                    if(Show_image.current_video_url!=null) {
//                        player.play(url);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                player.setTitle(url);
                /*String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
                MKPlayerActivity.configPlayer(Videoplayer.this).setTitle(url).play(url);*/
            }
        });

        try {
            Intent i=getIntent();

            if(i.getStringExtra("url")!=null) {

               player.play(i.getStringExtra("url"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

   }