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

public class ShearingArea extends AppCompatActivity {
    // Buttons.
    Button CreateCustomer, TotalNow;

    // String To Store TxtPlace Of Customer.
    String SharedSpace = "Shared Space";

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
        setContentView(R.layout.activity_shearing_area);

        // GetInstance From Firebase Database.
        database = FirebaseDatabase.getInstance();
        // Make Reference.
        refExpiredTime = database.getReference("ExpiredTime");

        // Get Buttons By IDs.
        CreateCustomer = (Button) findViewById(R.id.create_new_individual);
        TotalNow = (Button) findViewById(R.id.show_infos_individual);

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

        // Click On Buttons.
        CreateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShearingArea.this, SignUpUser.class);
                // PutExtra To Travel To Another Group By Key & Value.
                intent.putExtra("Shared Space", SharedSpace);
                startActivity(intent);
            }
        });

        TotalNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(ShearingArea.this, Home.class);
                    startActivity(intent);

            }
        });
    }
}
