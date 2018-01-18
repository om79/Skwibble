package com.skwibble.skwibblebook.baddies_group;

import android.content.Intent;
import android.os.Bundle;

import com.skwibble.skwibblebook.story_group.Post_details;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;

/**
 * Created by POPLIFY on 12/20/2016.
 */

public class Buddies_group_activity extends TabGroupActivity {

    SaveData save_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        save_data = new SaveData(this);

        if (save_data.getString(Definitions.current_noti_request).equals("Feed_post_details")) {

            Intent edit3 = new Intent(this, Post_details.class);
            edit3.putExtra("post_id", save_data.getString(Definitions.current_noti_id));
            edit3.putExtra("from", 3);
            edit3.putExtra("call", "feed");
            startChildActivity("BuddiesActivity", edit3,false);



        } else if (save_data.getString(Definitions.current_noti_request).equals("Feed_invite_page")){


            Intent edit3 = new Intent(this, Buddies_invite_activity.class);
            startChildActivity("noti_budies_Activity", edit3,false);



        } else {
            startChildActivity("BuddiesActivity", new Intent(this, Buddies_activity.class),false);
        }

    }

    @Override
    public void onBackPressed() {
        finishFromChild(getParent());
        super.onBackPressed();

    }
}
