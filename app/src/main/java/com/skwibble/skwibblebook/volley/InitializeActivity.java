package com.skwibble.skwibblebook.volley;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.skwibble.skwibblebook.utility.Definitions;


public class InitializeActivity extends Application implements Application.ActivityLifecycleCallbacks {

    public static Activity current_activity ;
    private static FirebaseAnalytics mFirebaseAnalytics;
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Definitions.initializeApplication(getApplicationContext());
        registerActivityLifecycleCallbacks(this);
        Firebase.setAndroidContext(this);
        Fresco.initialize(this);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        context=InitializeActivity.this;



    }





    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {
        Log.e("----- App create","----");

    }
    @Override
    public void onActivityDestroyed(Activity activity) {


        Log.e("----- App destroy","----");

    }
    @Override
    public void onActivityPaused(Activity activity) {

        Log.e("----- App  Pause","----");


    }
    @Override
    public void onActivityResumed(Activity activity) {

        Log.e("----- App  Resume","----");

        current_activity = activity ;



    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }
    @Override
    public void onActivityStarted(Activity activity) {

    }
    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }


    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }




}
