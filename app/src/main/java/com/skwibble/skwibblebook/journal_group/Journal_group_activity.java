package com.skwibble.skwibblebook.journal_group;

import android.content.Intent;
import android.os.Bundle;

import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.TabGroupActivity;

/**
 * Created by POPLIFY on 12/22/2016.
 */

public class Journal_group_activity extends TabGroupActivity {

    SaveData save_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save_data = new SaveData(this);

        if (save_data.getString(Definitions.current_noti_request).equals("Months_details")){


            Intent edit3 = new Intent(this, Months_details.class);
            edit3.putExtra("month_id",save_data.getString(Definitions.current_noti_id));

            startChildActivity("noti_month_Activity", edit3, false);



        }if (save_data.getString(Definitions.current_noti_request).equals("Journal_index_toast")){


            Intent edit3 = new Intent(this, Journal_activitry.class);
            edit3.putExtra("current_event_msg",save_data.getString(Definitions.current_event_msg));
            startChildActivity("invite_story_Activity", edit3, false);




        } else {
            startChildActivity("journalActivity", new Intent(this,Journal_activitry.class), false);
        }
    }

    @Override
    public void onBackPressed() {
        finishFromChild(getParent());
        super.onBackPressed();

    }

}
