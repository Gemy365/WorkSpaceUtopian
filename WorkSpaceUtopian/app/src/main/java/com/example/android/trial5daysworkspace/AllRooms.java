package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.trial5daysworkspace.Model.ExpiredTrial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllRooms extends AppCompatActivity {
    // Init Buttons Views.
    Button BtnRoom1, BtnRoom2, BtnRoom3, BtnRoom4, BtnRoom5, BtnBigRoom;

    // Strings To Check Extra Keys From Intent.
    String Room1 = "Room1";
    String Room2 = "Room2";
    String Room3 = "Room3";
    String Room4 = "Room4";
    String Room5 = "Room5";
    String BigRoom = "BigRoom";

    FirebaseDatabase database;

    DatabaseReference refExpiredTime;

    ExpiredTrial expired;

    // To Make All App Like As The Same Font Make Sure To Put This Code Before onCreate Method
    // In Every Group Press Ctrl + O.
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To Make All App Like As The Same Font Make Sure To Put This Code Before setContentView Method
        // In Every Group.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Century Gothic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_all_rooms);

        // GetInstance From Firebase Database.
        database = FirebaseDatabase.getInstance();
        // Make Reference.
        refExpiredTime = database.getReference("ExpiredTime");

        // Get Views By IDs.
        BtnRoom1 = (Button) findViewById(R.id.room1);
        BtnRoom2 = (Button) findViewById(R.id.room2);
        BtnRoom3 = (Button) findViewById(R.id.room3);
        BtnRoom4 = (Button) findViewById(R.id.room4);
        BtnRoom5 = (Button) findViewById(R.id.room5);
        BtnBigRoom = (Button) findViewById(R.id.big_room);

        // Init Calendar For Day, Month, and Year.
        final Calendar now = Calendar.getInstance();

        // Connect With Data Into Firebase DataBase To Check If Trial Of 7 Day Is Over Or Not.
        refExpiredTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Get Values From Firebase Database & Stored Into ExpiredTrial Class To Use Getter & Setter Methods.
                expired = dataSnapshot.getValue(ExpiredTrial.class);

                // Get Current Day.
                int currentDay = now.get(Calendar.DATE);

                // Get Current Month.
                // 'Cause JANUARY is 0 index We Need To Store Month Into String Then Increase 1 For Current Month.
                int currentMonth = (now.get(Calendar.MONTH) + 1);

                // Get Current Year.
                int currentYear = now.get(Calendar.YEAR);

                if (expired.getIsBegan().equals("true")) {

                    int getDay = expired.getDay();
                    int getMonth = expired.getMonth();
                    int getYear = expired.getYear();

                    // Check If Current [Day Or Month Or Year] Is Greater Than [The Started Day + 1] To Check How Many [Days, Month, Year] Are Being Used The App.
                    if ((currentDay >= getDay + 5) || (currentMonth >= getMonth + 1) || (currentYear >= getYear + 1)) {

                        // Out Of This Activity.
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // When Click On Button.
        BtnRoom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go To SelectedRoom TxtCollege.
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                // Take Key & Value With U.
                intent.putExtra("Room1", Room1);
                startActivity(intent);
            }
        });

        BtnRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                intent.putExtra("Room2", Room2);
                startActivity(intent);
            }
        });

        BtnRoom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                intent.putExtra("Room3", Room3);
                startActivity(intent);
            }
        });

        BtnRoom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                intent.putExtra("Room4", Room4);
                startActivity(intent);
            }
        });

        BtnRoom5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                intent.putExtra("Room5", Room5);
                startActivity(intent);
            }
        });

        BtnBigRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllRooms.this, SelectedRoom.class);
                intent.putExtra("BigRoom", BigRoom);
                startActivity(intent);
            }
        });
    }
}
