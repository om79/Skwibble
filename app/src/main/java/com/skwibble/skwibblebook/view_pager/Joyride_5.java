package com.skwibble.skwibblebook.view_pager;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.UsefullData;

/**
 * Created by POPLIFY on 5/17/2016.
 */
public class Joyride_5 extends Fragment
{


    UsefullData usefullData;
    TextView term,pricay;
    Button close,email;
    public Joyride_5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.joyride_5, container, false);

        usefullData=new UsefullData(getActivity());
        TextView v = (TextView) rootView.findViewById(R.id.text_joyridde);
        v.setTypeface(usefullData.get_proxima_regusr());
        TextView t = (TextView) rootView.findViewById(R.id.textterm_joyridde);
        t.setTypeface(usefullData.get_proxima_regusr());

        term = (TextView) rootView.findViewById(R.id.text_pjoyridde);
        term.setTypeface(usefullData.get_ubntu_regular());
        pricay = (TextView) rootView.findViewById(R.id.text_jtoyridde);
        pricay.setTypeface(usefullData.get_ubntu_regular());
        close = (Button) rootView.findViewById(R.id.shareqss_btn);
        close.setTypeface(usefullData.get_ubntu_regular());
        email = (Button) rootView.findViewById(R.id.sharwe_bsstn);
        email.setTypeface(usefullData.get_ubntu_regular());

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse("https://skwibble.com/terms"));
                getActivity().startActivity(i);

            }
        });
        pricay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse("https://skwibble.com/privacy_policy"));
                getActivity().startActivity(i);

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEmail();
            }
        });
        return rootView;
    }


    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(new Uri.Builder().scheme("mailto").build())

                .putExtra(Intent.EXTRA_EMAIL, new String[]{ "skwibble <hello@skwibble.com>" })
                .putExtra(Intent.EXTRA_SUBJECT, "skwibble")
                .putExtra(Intent.EXTRA_TEXT, "skwibble")
                ;

        ComponentName emailApp = intent.resolveActivity(getActivity().getPackageManager());
        ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
        if (emailApp != null && !emailApp.equals(unsupportedAction))
            try {
                // Needed to customise the chooser dialog title since it might default to "Share with"
                // Note that the chooser will still be skipped if only one app is matched
                Intent chooser = Intent.createChooser(intent, "Send email with");
                chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(chooser);
                return;
            }
            catch (ActivityNotFoundException ignored) {
            }

        Toast
                .makeText(getActivity(), "Couldn't find an email app", Toast.LENGTH_LONG)
                .show();
    }

}
