package com.skwibble.skwibblebook.user_group;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.UsefullData;
import com.skwibble.skwibblebook.volley.UserAPI;

public class Forgot_password extends AppCompatActivity {


    EditText mEmailView;

    UsefullData objUsefullData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_top));
        }

        objUsefullData = new UsefullData(Forgot_password.this);
        mEmailView = (EditText) findViewById(R.id.editText_email_forgot);
        mEmailView.setTypeface(objUsefullData.get_proxima_regusr());
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button_forgot);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(objUsefullData.isNetworkConnected())
                {
                    objUsefullData.hideKeyboardFrom(getApplicationContext(),view);



                    // Reset errors.
                    mEmailView.setError(null);

                    // Store values at the time of the login attempt.
                    String email = mEmailView.getText().toString();

                    boolean cancel = false;
                    View focusView = null;



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

                        //                    String text = mEmailView.getText().toString();
                        if(!email.equals("")) {
                            if (email.charAt(0) == ' ') {
                                objUsefullData.make_toast("Space is Removed");
                                mEmailView.setText(mEmailView.getText().toString().trim());
                                mEmailView.setSelection(mEmailView.getText().length());
                            }
                        }

                       attemptforgot(email);




                    }

                }else {
                    Snackbar.make(view, "Please check your internet connection and try again", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        mEmailSignInButton.setTypeface(objUsefullData.get_proxima_regusr());
        TextView cancel = (TextView) findViewById(R.id.textView4);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancel.setTypeface(objUsefullData.get_proxima_regusr());
    }

    private void attemptforgot(String email) {



            objUsefullData.showProgress();
            JSONObject request = new JSONObject();
            JSONObject user_info = new JSONObject();

            try {
                user_info.put("email", email);

                request.put("user", user_info);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            UserAPI.post_JsonReq_JsonResp("/users/password", request,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            objUsefullData.dismissProgress();
                            Log.e("-----response--",""+response);

                            objUsefullData.make_toast("Email has been sent to your account");
                            finish();
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

                                    case 422:

                                        objUsefullData.showMsgOnUI("Email is not registered with us");
                                        break;
                                }
                            }
                        }
                    });


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
