package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectedRoom extends AppCompatActivity {

    // Buttons.
    Button CreateNewSelectedRoom, KAWNow;

    // Strings To Check Extra From Intent.
    String Room1 = "Room1";
    String Room2 = "Room2";
    String Room3 = "Room3";
    String Room4 = "Room4";
    String Room5 = "Room5";
    String BigRoom = "BigRoom";

    // Use To Get Extra From Intent.
    Bundle extras;

    // To Make All App Like As The Same Font Make Sure To Put This Code Before onCreate Method
    // In Every TxtCollege Press Ctrl + O.
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To Make All App Like As The Same Font Make Sure To Put This Code Before setContentView Method
        // In Every TxtCollege.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Century Gothic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_selected_room);

        // Get Extra From Intent.
        extras =  getIntent().getExtras();


        // Get Buttons By IDs.
        CreateNewSelectedRoom = (Button) findViewById(R.id.create_new_selected_room);
        KAWNow = (Button) findViewById(R.id.show_infos_selected_room);


        // Wheck CLick On Button.
        CreateNewSelectedRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GoTo SignUpUser TxtCollege With Put Extra.
                Intent intent = new Intent(SelectedRoom.this, SignUpUser.class);

                // Check If Extra Contains Key Like ("...").
                if(extras.containsKey("Room1"))
                    intent.putExtra("Room1", Room1);

                else if(extras.containsKey("Room2"))
                    intent.putExtra("Room2", Room2);

                else if(extras.containsKey("Room3"))
                    intent.putExtra("Room3", Room3);

                else if(extras.containsKey("Room4"))
                    intent.putExtra("Room4", Room4);

                else if(extras.containsKey("Room5"))
                    intent.putExtra("Room5", Room5);

                else if(extras.containsKey("BigRoom"))
                    intent.putExtra("BigRoom", BigRoom);

                startActivity(intent);
            }
        });

        // Click On Button.
        KAWNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GoTo Home TxtCollege.
                Intent intent = new Intent(SelectedRoom.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
