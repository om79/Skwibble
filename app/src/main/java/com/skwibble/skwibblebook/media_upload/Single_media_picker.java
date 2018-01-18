package com.skwibble.skwibblebook.media_upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.droid.mediamultiselector.activity.MediaSelectorActivity;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by POPLIFY on 10/7/2017.
 */

public class Single_media_picker extends Activity
{

    public static ArrayList<String> singlearrayMediaPath = new ArrayList<>();
    UsefullData objUsefullData;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objUsefullData = new UsefullData(this);
        singlearrayMediaPath.clear();
        i=getIntent();
        MediaSelectorActivity.startActivityForResult(this, Definitions.REQUEST_CODE_MEDIA_SELECT,
                MediaSelectorActivity.SELECTION_MODE_SINGLE, Definitions.SINGLE_MEDIA_COUNT, MediaSelectorActivity.MEDIA_TYPE_ALL,
                true, true, singlearrayMediaPath);

    }



    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode ==  Definitions.REQUEST_CODE_MEDIA_SELECT) {

            singlearrayMediaPath = data.getStringArrayListExtra(MediaSelectorActivity.RESULTS_SELECTED_MEDIA);
            for(String obj : singlearrayMediaPath) {
                String type = objUsefullData.getMimeType(obj);

                if(type!= null ) {
                    if (type.equalsIgnoreCase("video/mp4")) {
                        Intent intent = new Intent(this, Single_trimmer.class);
                        intent.putExtra(Definitions.EXTRA_VIDEO_PATH, obj);
                        startActivity(intent);
                        singlearrayMediaPath.clear();
                        finish();
                    } else {

                        if(i.getStringExtra(Definitions.IMAGE_REQUEST).equals("Playpen")){
                            finish();
                        }else {
                            singlearrayMediaPath.clear();
                            beginCrop(Uri.parse("file://"+obj));
                        }


                    }

                }else {

                    finish();
                }
            }
         }else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }else {
            finish();
        }
    }



    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
//            Log.e("---",""+Crop.getOutput(result).getPath());
            singlearrayMediaPath.add(Crop.getOutput(result).getPath());
            finish();
        } else if (resultCode == Crop.RESULT_ERROR) {
            objUsefullData.make_toast(Crop.getError(result).getMessage());
            finish();
        }else {
            finish();
        }
    }

}
