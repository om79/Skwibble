package com.skwibble.skwibblebook.playpen_group;

import android.content.Intent;
import android.os.Bundle;

import com.skwibble.skwibblebook.story_group.Stroy_invite_activity;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;

/**
 * Created by POPLIFY on 12/22/2016.
 */

public class Playpen_group_activity extends TabGroupActivity {

    SaveData save_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        save_data = new SaveData(this);
        if (save_data.getString(Definitions.current_noti_request).equals("Playpen_post_details")){


            Intent edit3 = new Intent(this, Playpen_post_detail.class);
            edit3.putExtra("post_id", save_data.getString(Definitions.current_noti_id));
            startChildActivity("noti_playpen_Activity", edit3, false);



        }else if (save_data.getString(Definitions.current_noti_request).equals("Playpen_index_toast")){


            Intent edit3 = new Intent(this, Playpen_show.class);
            edit3.putExtra("current_event_msg",save_data.getString(Definitions.current_event_msg));
            startChildActivity("Playpen_index_toast", edit3, false);



        } else {
            startChildActivity("playpen_Activity", new Intent(this, Playpen_show.class), false);
        }

    }

    @Override
    public void onBackPressed() {
        finishFromChild(getParent());
        super.onBackPressed();

    }
}