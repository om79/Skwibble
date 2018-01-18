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

public class Single_trimmer extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {

    private HgLVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    public  static String done_path;
    UsefullData objUsefullData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);

        objUsefullData = new UsefullData(Single_trimmer.this);

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
            done_path = contentUri.getPath();
            finish();
//        }



    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(Single_trimmer.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
    }


    public void start_trimmer(final String i){

        runOnUiThread(new Runnable() {
            public void run() {
                // your code to update the UI thread here
                    mVideoTrimmer.setVideoURI(Uri.parse(i));
                    mVideoTrimmer.setMaxDuration(90000);
            }
        });
    }

    public boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }
}
