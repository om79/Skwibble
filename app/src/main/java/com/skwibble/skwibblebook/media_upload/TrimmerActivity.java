package com.skwibble.skwibblebook.media_upload;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.story_group.Post_to_skwibble;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.videoTrimmer.HgLVideoTrimmer;
import com.skwibble.skwibblebook.videoTrimmer.interfaces.OnHgLVideoListener;
import com.skwibble.skwibblebook.videoTrimmer.interfaces.OnTrimVideoListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {

    private HgLVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    UsefullData  objUsefullData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);

        objUsefullData = new UsefullData(TrimmerActivity.this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));
        mVideoTrimmer = ((HgLVideoTrimmer) findViewById(R.id.timeLine));
        mVideoTrimmer.setOnTrimVideoListener(this);
        mVideoTrimmer.setOnHgLVideoListener(this);
        mVideoTrimmer.setVideoInformationVisibility(true);

        Intent extraIntent = getIntent();
        //setting progressbar
        if (mVideoTrimmer != null && extraIntent != null) {
            start_trimmer(extraIntent.getStringExtra(Definitions.EXTRA_VIDEO_PATH));

        }
    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri contentUri) {
        mProgressDialog.cancel();


//        int dur = 0;
//
//        try {
//            dur = Integer.parseInt(""+objUsefullData.getMediaDuration(contentUri));
//        } catch(NumberFormatException nfe) {
//            System.out.println("Could not parse " + nfe);
//        }
//
//        if(dur<5000){
//            objUsefullData.make_toast("Not allowed");
//        }else {

        Post_to_skwibble.trimmer_done_list.add(contentUri.getPath());
        finish();

//        }


    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(TrimmerActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private void playUriOnVLC(Uri uri) {

        int vlcRequestCode = 42;
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
        vlcIntent.putExtra("title", "Kung Fury");
        vlcIntent.putExtra("from_start", false);
        vlcIntent.putExtra("position", 90000l);
        startActivityForResult(vlcIntent, vlcRequestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("tg", "resultCode = " + resultCode + " data " + data);
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        finish();


    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();


    }

    @Override
    public void onVideoPrepared() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                         }
//        });
    }


    public void start_trimmer(final String path){

        runOnUiThread(new Runnable() {
            public void run() {
                    mVideoTrimmer.setVideoURI(Uri.parse(path));
                    mVideoTrimmer.setMaxDuration(90000);
            }
        });
    }
}
