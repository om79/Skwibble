package com.skwibble.skwibblebook.user_group;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.facebook.Auth;
import com.skwibble.skwibblebook.facebook.FacebookAuth;
import com.skwibble.skwibblebook.facebook.SocialProfile;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;
import com.vistrav.ask.Ask;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Auth.OnAuthListener{


    private EditText mEmailView;
    private EditText mPasswordView;
    UsefullData objUsefullData;
    TextView signup,fb_txt,forgot_txt,or;
    SaveData save_data;
    LinearLayout fb;
    FacebookAuth facebookAuth;
    RelativeLayout mEmailSignInButton;
    Button sign_in;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ask.on(LoginActivity.this)
                .forPermissions(Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("Camera and storage Permission need for Image to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();

        save_data = new SaveData(LoginActivity.this);
        objUsefullData = new UsefullData(LoginActivity.this);

        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_top));
        }


        facebookAuth = new FacebookAuth(this,this);
        mEmailView = (EditText) findViewById(R.id.editText_email);
        mEmailView.setTypeface(objUsefullData.get_proxima_regusr());

        sign_in = (Button) findViewById(R.id.emabutton);
        sign_in.setTypeface(objUsefullData.get_proxima_regusr());
        mPasswordView = (EditText) findViewById(R.id.editText2_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email_sign_in_button || id == EditorInfo.IME_ACTION_DONE) {


                    if(objUsefullData.isNetworkConnected())
                    {

                        mEmailView.setError(null);
                        mPasswordView.setError(null);

                        // Store values at the time of the login attempt.
                        String email = mEmailView.getText().toString();
                        String password = mPasswordView.getText().toString();

                        boolean cancel = false;
                        View focusView = null;

                        // Check for a valid password, if the user entered one.
                        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
                            mPasswordView.setError(getString(R.string.error_invalid_password));
                            focusView = mPasswordView;
                            cancel = true;
                        }

                        // Check for a valid email address.
                        if (TextUtils.isEmpty(email)) {
                            mEmailView.setError(getString(R.string.error_field_required));
                            focusView = mEmailView;
                            cancel = true;
                        } else if (!objUsefullData.emailValidator(email)) {
                            mEmailView.setError(getString(R.string.error_invalid_email));
                            focusView = mEmailView;
                            cancel = true;
                        }

                        if (cancel) {
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();

                        } else {



                            if(!email.equals("")&&!password.equals("")) {
                                if (email.charAt(0) == ' ' || password.charAt(0) == ' ') {
                                    objUsefullData.make_toast("Space is Removed");
                                    mEmailView.setText(mEmailView.getText().toString().trim());
                                    mEmailView.setSelection(mEmailView.getText().length());
                                    mPasswordView.setText(mPasswordView.getText().toString().trim());
                                    mPasswordView.setSelection(mPasswordView.getText().length());
                                }
                            }
                            if (save_data.isExist(Definitions.firebase_token)) {
                                attemptLogin(mEmailView.getText().toString(),mPasswordView.getText().toString());
                            }else {
                                objUsefullData.make_toast(getResources().getString(R.string.wrong));
                            }




                        }
                    }else {
                        objUsefullData.make_toast("Please check your internet connection and try again");
                    }
                    return true;
                }
                return false;
            }
        });

        mPasswordView.setTypeface(objUsefullData.get_proxima_regusr());
        mEmailSignInButton = (RelativeLayout) findViewById(R.id.email_sign_in_button);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(objUsefullData.isNetworkConnected())
                {
                    objUsefullData.hideKeyboardFrom(getApplicationContext(),v);
                    mEmailView.setError(null);
                    mPasswordView.setError(null);

                    // Store values at the time of the login attempt.
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();

                    boolean cancel = false;
                    View focusView = null;

                    // Check for a valid password, if the user entered one.
                    if (TextUtils.isEmpty(password) || !isPasswordValid(password) ) {
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        focusView = mPasswordView;
                        cancel = true;
                    }

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(email)) {
                        mEmailView.setError(getString(R.string.error_field_required));
                        focusView = mEmailView;
                        cancel = true;
                    } else if (!objUsefullData.emailValidator(email)) {
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        focusView = mEmailView;
                        cancel = true;
                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();

                    } else {

                        if(!email.equals("")&&!password.equals("")) {
                            if (email.charAt(0) == ' ' || password.charAt(0) == ' ') {
                                objUsefullData.make_toast("Space is Removed");
                                mEmailView.setText(mEmailView.getText().toString().trim());
                                mEmailView.setSelection(mEmailView.getText().length());
                                mPasswordView.setText(mPasswordView.getText().toString().trim());
                                mPasswordView.setSelection(mPasswordView.getText().length());
                            }
                        }

                        if (save_data.isExist(Definitions.firebase_token)) {
                            attemptLogin(mEmailView.getText().toString(),mPasswordView.getText().toString());
                        }else {
                            objUsefullData.make_toast(getResources().getString(R.string.wrong));
                        }





                    }
                }else {
                    Snackbar.make(v, "Please check your internet connection and try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


        signup = (TextView) findViewById(R.id.textView_for_signup);
        or = (TextView) findViewById(R.id.textView4);
        or.setTypeface(objUsefullData.get_proxima_regusr());
        signup.setTypeface(objUsefullData.get_proxima_regusr());
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                try {
//                    PackageInfo info = getPackageManager().getPackageInfo(
//                            "com.skwibble.skwibblebook",
//                            PackageManager.GET_SIGNATURES);
//                    for (Signature signature : info.signatures) {
//                        MessageDigest md = MessageDigest.getInstance("SHA");
//                        md.update(signature.toByteArray());
//                       Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                        Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                        Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//
//                } catch (NoSuchAlgorithmException e) {
//
//                }


            }
        });

        forgot_txt = (TextView) findViewById(R.id.textView3);
        forgot_txt.setTypeface(objUsefullData.get_proxima_regusr());
        forgot_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot=new Intent(getApplicationContext(),Forgot_password.class);
                startActivity(forgot);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



            }
        });

        fb_txt = (TextView) findViewById(R.id.textView5);
        fb_txt.setTypeface(objUsefullData.get_proxima_regusr());
        fb = (LinearLayout) findViewById(R.id.buttonfb);
        fb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                facebookAuth.login();

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Definitions.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Definitions.TOPIC_GLOBAL);



                } else if (intent.getAction().equals(Definitions.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");




                }
            }
        };






    }

    @Override
    protected void onStart() {


        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onStart();
    }


    @Override
    protected void onResume () {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Definitions.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Definitions.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    protected void onPause () {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
    }












    private void attemptLogin(String email,String password) {


        objUsefullData.showProgress();
        JSONObject request = new JSONObject();
        JSONObject user_info = new JSONObject();
        Log.e("-----token--",""+save_data.getString(Definitions.firebase_token));
        try {
            user_info.put("email", email);
            user_info.put("password", password);
            user_info.put("operating_system", "android");
            user_info.put("token", save_data.getString(Definitions.firebase_token));
            request.put("user", user_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        UserAPI.post_JsonReq_JsonResp("users/signin_api", request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.e("-----response--",""+response);
                        save_data.save(Definitions.facebook_login,false);
                        store_values(response);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {

                                case 500:

                                    objUsefullData.showMsgOnUI("Something went wrong");
                                    break;
                                case 401:

                                    objUsefullData.showMsgOnUI("Invalid email address or password");
                                    break;
                                case 406:

                                    objUsefullData.showMsgOnUI("Please confirm your account before Signing in");
                                    break;
                            }
                        }
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        facebookAuth.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(SocialProfile profile) {
        //save on shared preferences
        saveAuthenticatedUser(profile);

    }

    @Override
    public void onLoginError(String message) {
        Log.e("teste", message);
        objUsefullData.showMsgOnUI("Please check your internet connection and try again");
    }

    @Override
    public void onLoginCancel() {}

    @Override
    public void onRevoke() {}

    private void saveAuthenticatedUser(SocialProfile profile){
        Log.e("data", ""+profile.getImage());
        objUsefullData.showProgress();
        if (save_data.isExist(Definitions.firebase_token)) {
            JSONObject request = new JSONObject();
            JSONObject user_info = new JSONObject();
            try {
                user_info.put("name", profile.getName());
                user_info.put("email", profile.getEmail());
                user_info.put("role_id", "1");
                user_info.put("token", save_data.getString(Definitions.firebase_token));
                user_info.put("user_id", profile.getsocial_id());
                user_info.put("image_url", profile.getImage());
                user_info.put("operating_system", "android");
                request.put("user", user_info);
                Log.e("data", ""+request.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserAPI.post_JsonReq_JsonResp("/users/signin_facebook", request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            objUsefullData.dismissProgress();
                            Log.e("-----response--",""+response);
                            save_data.save(Definitions.facebook_login,true);

                            objUsefullData.firebase_analytics("facebookLogin");

                            store_values(response);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            objUsefullData.showMsgOnUI("Unsuccessful login");
                            Log.e("-----response--",""+error);//
                        }
                    });
        }else {
            objUsefullData.make_toast(getResources().getString(R.string.wrong));
        }

    }
    private void store_values(JSONObject response)
    {
        Log.e("response----",""+response);
        try {

            Log.e("response----",""+response.getString("name"));
            save_data.save(Definitions.user_name, response.optString("name"));
            save_data.save(Definitions.auth_token, response.optString("authentication_token"));
            save_data.save(Definitions.user_email, response.optString("email"));
            save_data.save(Definitions.user_image, response.optString("image_url"));
            save_data.save(Definitions.show_child_form, response.optBoolean("show_child_form"));
            save_data.save(Definitions.user_role_id, response.optInt("role_id"));
            save_data.save(Definitions.id, response.optInt("id"));
            save_data.save(Definitions.user_dob, response.optString("dob"));
            save_data.save(Definitions.has_first_post, response.optBoolean("has_first_posts"));
            save_data.save(Definitions.has_child, response.optBoolean("has_child"));



            if(!save_data.getBoolean(Definitions.show_child_form)){
                Intent intent = new Intent(LoginActivity.this, Tab_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }else {

                Intent intent = new Intent(LoginActivity.this, First_time_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        objUsefullData.dismissProgress();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
