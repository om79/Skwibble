package com.skwibble.skwibblebook.story_group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;

/**
 * Created by POPLIFY on 12/13/2016.
 */

public class Story_group_activity  extends TabGroupActivity {

    SaveData save_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        save_data = new SaveData(this);

        Log.e("-noti_request-",save_data.getString(Definitions.current_noti_request));


        if (save_data.getString(Definitions.current_noti_request).equals("Story_post_details")) {

            Intent edit3 = new Intent(this, Post_details.class);
            edit3.putExtra("post_id", save_data.getString(Definitions.current_noti_id));
            edit3.putExtra("from", 0);
            edit3.putExtra("call", "story");
            startChildActivity("storyBuddiesActivity", edit3, false);



        } else if (save_data.getString(Definitions.current_noti_request).equals("Story_invite_page")){


            Intent edit3 = new Intent(this, Stroy_invite_activity.class);
            startChildActivity("noti_story_Activity", edit3, false);



        } else if (save_data.getString(Definitions.current_noti_request).equals("Story_index_toast")){


            Intent edit3 = new Intent(this, Story_activity.class);
            edit3.putExtra("current_event_msg",save_data.getString(Definitions.current_event_msg));
            startChildActivity("index_story_Activity", edit3, true);



        }else if (save_data.getString(Definitions.current_noti_request).equals("Story_invite_toast")){


            Intent edit3 = new Intent(this, Stroy_invite_activity.class);
            edit3.putExtra("current_event_msg",save_data.getString(Definitions.current_event_msg));
            startChildActivity("invite_story_Activity", edit3, false);



        }else {
            startChildActivity("storyActivity", new Intent(this,Story_activity.class), true);
        }



    }

    @Override
    public void onBackPressed() {
           finishFromChild(getParent());
        super.onBackPressed();

    }

}
