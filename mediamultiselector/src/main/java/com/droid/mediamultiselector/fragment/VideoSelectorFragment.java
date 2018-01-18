package com.droid.mediamultiselector.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.droid.mediamultiselector.R;
import com.droid.mediamultiselector.activity.MediaSelectorActivity;
import com.droid.mediamultiselector.adapter.MediaAdapter;
import com.droid.mediamultiselector.model.Media;
import com.droid.mediamultiselector.model.Video;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class VideoSelectorFragment extends BaseFragment {

    private static final String TAG = "VideoSelectorFragment";
    private View rootView;
    private RecyclerView rvVidSelector;

    public static VideoSelectorFragment createIntance(int mode, int selectionLimit, boolean isShowCamera,
                                                      List<String> arrPrevSelectedPath) {
        VideoSelectorFragment fgmt = new VideoSelectorFragment();

        Bundle args = new Bundle();

        ArrayList<Media> arrSelectedMedia = new ArrayList<>();
        for (String path: arrPrevSelectedPath) {
            Media media = new Media(path, "", System.currentTimeMillis());
            arrSelectedMedia.add(media);
        }

        args.putInt(MediaSelectorActivity.EXTRAS_MODE, mode);
        args.putInt(MediaSelectorActivity.EXTRAS_SELECTION_LIMIT, selectionLimit);
        args.putBoolean(MediaSelectorActivity.EXTRAS_SHOW_CAMERA, isShowCamera);
        args.putParcelableArrayList(MediaSelectorActivity.EXTRAS_PREVIOUSLY_SELECTED, arrSelectedMedia);

        fgmt.setArguments(args);

        return fgmt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fgmt_video_selector, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isCameraActionPhoto = false;

        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(1, null, callbackLoaderMngr);
    }

    private void initView() {
        rvVidSelector = (RecyclerView) rootView.findViewById(R.id.fgmt_video_selector_rv);
        rvVidSelector.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvVidSelector.setHasFixedSize(true);

        adapter = new MediaAdapter(getActivity(), MediaSelectorActivity.MEDIA_TYPE_VIDEO,
                this, mode, selectionLimit, isShowCamera);

        rvVidSelector.setAdapter(adapter);
    }

    private LoaderManager.LoaderCallbacks<Cursor> callbackLoaderMngr = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(
                    getActivity(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    VIDEO_PROJECTION,
                    VIDEO_PROJECTION[4]+">0 AND "+ VIDEO_PROJECTION[3]+"=?",
                    new String[]{"video/mp4"},
                    VIDEO_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Observable observable = Observable.just(data)
                    .map(new Func1<Cursor, ArrayList<Media>>() {
                        @Override
                        public ArrayList<Media> call(Cursor cursor) {
                            return getVideos(cursor);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            observable.subscribe(new Subscriber<ArrayList<Media>>() {
                @Override
                public void onCompleted() { }

                @Override
                public void onError(Throwable e) {
                    adapter.setData(new ArrayList<Media>());
                    if (arrPrevMediaPath != null && arrPrevMediaPath.size()>0) {
                        adapter.setSelectedData(arrPrevMediaPath);
                    }
                }

                @Override
                public void onNext(ArrayList<Media> medias) {
                    adapter.setData(medias);
                    if (arrPrevMediaPath != null && arrPrevMediaPath.size()>0) {
                        adapter.setSelectedData(arrPrevMediaPath);
                    }
                }
            });
        }

        private boolean fileExist(String path){
            if(!TextUtils.isEmpty(path)){
                return new File(path).exists();
            }
            return false;
        }

        private ArrayList<Media> getVideos(Cursor data) {

            ArrayList<Media> videos = new ArrayList<>();
            if (data.getCount() > 0) {

                data.moveToFirst();
                do {
                    String path = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));

                    //Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);

                    if(!fileExist(path)) { continue; }

                    Video video = null;
                    if (!TextUtils.isEmpty(name)) {

//                        if(!is_slow_mo(path)) {
                            video = new Video(path, name, dateTime);
                            videos.add(video);
//                        }
                    }

                } while(data.moveToNext());

            }

            return videos;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


//    public boolean is_slow_mo(String path){
//        MediaExtractor extractor = new MediaExtractor();
//        int frameRate = 24; //may be default
//        boolean is_slow=false;
//        try {
//            //Adjust data source as per the requirement if file, URI, etc.
//            extractor.setDataSource(path);
//            int numTracks = extractor.getTrackCount();
//            for (int i = 0; i < numTracks; ++i) {
//                MediaFormat format = extractor.getTrackFormat(i);
//                String mime = format.getString(MediaFormat.KEY_MIME);
//                if (mime.equals("video/mp4v-es")) {
////                    if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
////                        frameRate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
////                        Log.e("-----frameRate-----",""+frameRate);
////                    }
//                    is_slow=true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            //Release stuff
//            extractor.release();
//        }
//        return is_slow;
//    }

    class post_media_work extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;
        protected void onPreExecute() {

            dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);
            dialog.setMessage("Please wait a moment");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            dialog.cancel();
        }
    }

}
