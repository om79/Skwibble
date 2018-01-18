package com.skwibble.skwibblebook.user_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.facebook.Auth;
import com.skwibble.skwibblebook.facebook.FacebookAuth;
import com.skwibble.skwibblebook.facebook.SocialProfile;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.Tab_activity;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity implements Auth.OnAuthListener {



     EditText mEmailView;
     EditText mname;
     EditText mPasswordView;
     UsefullData objUsefullData;
     TextView login,or,fb_txt,txt_label;
     FacebookAuth facebookAuth;
     SaveData save_data;
     LinearLayout fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        objUsefullData = new UsefullData(SignupActivity.this);
        save_data = new SaveData(SignupActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_top));
        }
        facebookAuth = new FacebookAuth(this ,this);
        mEmailView = (EditText) findViewById(R.id.editText_signup);
        mname = (EditText) findViewById(R.id.editText_name);
        mname.setTypeface(objUsefullData.get_proxima_regusr());
        mEmailView.setTypeface(objUsefullData.get_proxima_regusr());
        or = (TextView) findViewById(R.id.textView45);
        or.setTypeface(objUsefullData.get_proxima_regusr());
        mPasswordView = (EditText) findViewById(R.id.editText2_sighup);
        fb_txt = (TextView) findViewById(R.id.textView95);
        fb_txt.setTypeface(objUsefullData.get_proxima_regusr());

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email_sign_in_button || id == EditorInfo.IME_ACTION_DONE) {

                    if(objUsefullData.isNetworkConnected()==true)
                    {


                        // Reset errors.
                        mEmailView.setError(null);
                        mPasswordView.setError(null);
                        mname.setError(null);

                        // Store values at the time of the login attempt.
                        String name = mname.getText().toString();
                        String email = mEmailView.getText().toString();
                        String password = mPasswordView.getText().toString();

                        boolean cancel = false;
                        View focusView = null;

                        if (TextUtils.isEmpty(name)) {
                            mname.setError(getString(R.string.error_field_required));
                            focusView = mname;
                            cancel = true;
                        }
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
                        } else if (objUsefullData.emailValidator(email)==false) {
                            mEmailView.setError(getString(R.string.error_invalid_email));
                            focusView = mEmailView;
                            cancel = true;
                        }




                        if (cancel==true) {
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                        } else {
                            if(!email.equals("")&&!password.equals("")&&!name.equals("")) {
                                if (email.charAt(0) == ' ' || password.charAt(0) == ' ' || name.charAt(0) == ' ') {
                                    objUsefullData.make_toast("Space is Removed");
                                    mEmailView.setText(mEmailView.getText().toString().trim());
                                    mEmailView.setSelection(mEmailView.getText().length());
                                    mPasswordView.setText(mPasswordView.getText().toString().trim());
                                    mPasswordView.setSelection(mPasswordView.getText().length());
                                    mname.setText(mname.getText().toString().trim());
                                    mname.setSelection(mname.getText().length());
                                }
                            }
                                attemptsignup(mname.getText().toString(), mEmailView.getText().toString(), mPasswordView.getText().toString());

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
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(objUsefullData.isNetworkConnected()==true)
                {
                    objUsefullData.hideKeyboardFrom(getApplicationContext(),view);

                    // Reset errors.
                    mEmailView.setError(null);
                    mPasswordView.setError(null);
                    mname.setError(null);

                    // Store values at the time of the login attempt.
                    String name = mname.getText().toString();
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();

                    boolean cancel = false;
                    View focusView = null;

                    if (TextUtils.isEmpty(name)) {
                        mname.setError(getString(R.string.error_field_required));
                        focusView = mname;
                        cancel = true;
                    }
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
                    } else if (objUsefullData.emailValidator(email)==false) {
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        focusView = mEmailView;
                        cancel = true;
                    }



                    if (cancel==true) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {
                        if(!email.equals("")&&!password.equals("")&&!name.equals("")) {
                            if (email.charAt(0) == ' ' || password.charAt(0) == ' ' || name.charAt(0) == ' ') {
                                objUsefullData.make_toast("Space is Removed");
                                mEmailView.setText(mEmailView.getText().toString().trim());
                                mEmailView.setSelection(mEmailView.getText().length());
                                mPasswordView.setText(mPasswordView.getText().toString().trim());
                                mPasswordView.setSelection(mPasswordView.getText().length());
                                mname.setText(mname.getText().toString().trim());
                                mname.setSelection(mname.getText().length());
                            }
                        }
                        attemptsignup(mname.getText().toString(), mEmailView.getText().toString(), mPasswordView.getText().toString());


                    }
                }else {
                    Snackbar.make(view, "Please check your internet connection and try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        mEmailSignInButton.setTypeface(objUsefullData.get_proxima_regusr());
        login = (TextView) findViewById(R.id.textView_login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        login.setTypeface(objUsefullData.get_proxima_regusr());
        txt_label= (TextView) findViewById(R.id.textView_login_txt);
        txt_label.setTypeface(objUsefullData.get_proxima_regusr());
        fb = (LinearLayout) findViewById(R.id.buttonfb_signup);

        fb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookAuth.login();

            }
        });

    }


    private void attemptsignup(String name,String email,String password) {



            objUsefullData.showProgress();
            JSONObject request = new JSONObject();
            JSONObject user_info = new JSONObject();

            try {
                user_info.put("name", name);
                user_info.put("email", email);
                user_info.put("role_id", "1");
                user_info.put("password", password);
                user_info.put("password_confirmation", password);
                request.put("user", user_info);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            UserAPI.post_JsonReq_JsonResp("/users", request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            objUsefullData.dismissProgress();
                            Log.e("-----response--",""+response);


                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Almost There...")
                                    .setContentText("Please activate your account by following the confirmation link sent to your email address.")
                                    .setConfirmText("Got it!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();


                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 422:

                                        objUsefullData.showMsgOnUI("Email address already exists");


                                        break;
                                    case 500:

                                        objUsefullData.showMsgOnUI("Something went wrong");
                                        break;

                                }
                            }

                        }
                    });


    }





    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        facebookAuth.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(SocialProfile profile) {
        //save on shared preferences
        saveAuthenticatedUser(profile);

//        Intent intent = new Intent(this, Playpen_post_detail.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    @Override
    public void onLoginError(String message) {
        Log.e("teste", message);
    }

    @Override
    public void onLoginCancel() {}

    @Override
    public void onRevoke() {}

    private void saveAuthenticatedUser(SocialProfile profile){

        objUsefullData.showProgress();
        Log.e("data", ""+profile.getsocial_id());

        Log.e("data", ""+profile.getImage());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }


        UserAPI.post_JsonReq_JsonResp("/users/signin_facebook", request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        objUsefullData.dismissProgress();
                        objUsefullData.firebase_analytics("facebookLogin");
                        Log.e("-----response--",""+response);
                        save_data.save(Definitions.facebook_login,true);
                        store_values(response);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.showMsgOnUI("Unsuccessful Signup");
                        Log.e("-----response--",""+error);
                    }
                });

    }

    private void store_values(JSONObject response)
    {
        Log.e("response----",""+response);
        try {
            Log.e("response----",""+response.optString("name"));
            save_data.save(Definitions.user_name, response.optString("name"));
            save_data.save(Definitions.auth_token, response.optString("authentication_token"));
            save_data.save(Definitions.user_image, response.optString("image_url"));
            save_data.save(Definitions.user_email, response.optString("email"));
            save_data.save(Definitions.user_role_id, response.optInt("role_id"));
            save_data.save(Definitions.user_dob, response.optString("dob"));
            save_data.save(Definitions.id, response.optInt("id"));


            save_data.save(Definitions.has_first_post, response.optBoolean("has_first_posts"));
            save_data.save(Definitions.has_child, response.optBoolean("has_child"));
            save_data.save(Definitions.show_child_form, response.optBoolean("show_child_form"));

            if(save_data.getBoolean(Definitions.show_child_form)==false){
                Intent intent = new Intent(SignupActivity.this, Tab_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {

                Intent intent = new Intent(SignupActivity.this, First_time_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

