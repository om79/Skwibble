package com.skwibble.skwibblebook.user_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.baddies_group.Add_budddies;
import com.skwibble.skwibblebook.utility.SaveData;
import com.skwibble.skwibblebook.utility.UsefullData;

public class First_time_login extends AppCompatActivity {

    TextView child_txt,budy_txt,or_txt;
    Button add_child,become_bdy;
    UsefullData objUsefullData;
    SaveData save_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login);
        save_data = new SaveData(First_time_login.this);
        objUsefullData = new UsefullData(First_time_login.this);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_top));
        }



        or_txt=(TextView) findViewById(R.id.textyView4);
        child_txt=(TextView) findViewById(R.id.xa);
        budy_txt=(TextView) findViewById(R.id.xb);
        or_txt.setTypeface(objUsefullData.get_proxima_light());
        child_txt.setTypeface(objUsefullData.get_proxima_light());
        budy_txt.setTypeface(objUsefullData.get_proxima_light());

        add_child=(Button) findViewById(R.id.button2qd);
        become_bdy=(Button) findViewById(R.id.button2);
        add_child.setTypeface(objUsefullData.get_ubntu_regular());
        become_bdy.setTypeface(objUsefullData.get_ubntu_regular());


        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(First_time_login.this, Add_child_first_time.class);
                startActivity(edit);
            }
        });

        become_bdy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(First_time_login.this, Add_budddies.class);
                edit.putExtra("first_time","true");
                startActivity(edit);
            }
        });


    }
}
