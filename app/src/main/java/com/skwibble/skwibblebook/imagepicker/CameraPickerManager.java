package com.skwibble.skwibblebook.imagepicker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.MediaStore;


/**
 * Created by Mickael on 10/10/2016.
 */

public class CameraPickerManager extends PickerManager {

    public CameraPickerManager(Activity activity) {
        super(activity);
    }

    protected void sendToExternalApp()
    {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mProcessingPhotoUri =  getImageFile();

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            mProcessingPhotoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
//            fileUri = Uri.fromFile(fileTemp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mProcessingPhotoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            activity.startActivityForResult(intent, 0);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
//            Toast.makeText(this, "ddd", Toast.LENGTH_LONG).show();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mProcessingPhotoUri);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }
}
