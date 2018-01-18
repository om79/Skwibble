package com.skwibble.skwibblebook.user_group;

import android.content.Intent;
import android.os.Bundle;

import com.skwibble.skwibblebook.utility.TabGroupActivity;

/**
 * Created by POPLIFY on 12/1/2016.
 */

public class User_group_activitty extends TabGroupActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("childActivity", new Intent(this,Notification.class), false);
    }

    @Override
    public void onBackPressed() {
        finishFromChild(getParent());
        super.onBackPressed();

    }

}
