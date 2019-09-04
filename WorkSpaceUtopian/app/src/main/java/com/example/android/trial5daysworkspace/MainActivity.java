package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.trial5daysworkspace.Model.ExpiredTrial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    // Buttons.
    TextView ShearingAreaBtn, RoomsBtn, UtopianNow, OtherPayment;

    LinearLayout ActiveMain, DeActiveMain;

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
        setContentView(R.layout.activity_main);

        // Get Buttons By IDs.
        UtopianNow = (TextView) findViewById(R.id.show_total_now);
        ShearingAreaBtn = (TextView) findViewById(R.id.shared_space);
        RoomsBtn = (TextView) findViewById(R.id.rooms);
        OtherPayment = (TextView) findViewById(R.id.other_method_payment);

        ActiveMain = (LinearLayout) findViewById(R.id.layout_active_main);
        DeActiveMain = (LinearLayout) findViewById(R.id.layout_deActive_main);

        // GetInstance From Firebase Database.
        database = FirebaseDatabase.getInstance();
        // Make Reference.
        refExpiredTime = database.getReference("ExpiredTime");

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

                // Check If There's No Data As ["isBegan"] Exists Into Firebase.
                if (!dataSnapshot.child("isBegan").exists()) {

                    String BeganApp = "true";

                    // Call Constructor Of ExpiredTrial & Send Params.
                    expired = new ExpiredTrial(BeganApp, currentDay, currentMonth, currentYear);

                    // Set Data Into Firebase Database.
                    refExpiredTime.setValue(expired);

                    // Message.
                    Toast.makeText(MainActivity.this, "Trial For 5 Day Only..", Toast.LENGTH_SHORT).show();
                }
                // Otherwise If The User Used The App Before.
                else if (expired.getIsBegan().equals("true")) {

                    int getDay = expired.getDay();
                    int getMonth = expired.getMonth();
                    int getYear = expired.getYear();

                    // Check If Current [Day Or Month Or Year] Is Greater Than [The Started Day + 1] To Check How Many [Days, Month, Year] Are Being Used The App.
                    if ((currentDay >= getDay + 5) || (currentMonth >= getMonth + 1) || (currentYear >= getYear + 1)) {

                        // De Activate The App & Appear My Number If User Wants To Buy My App [Upgrade].
                        ActiveMain.setVisibility(View.GONE);
                        DeActiveMain.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Trial is over..", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Click On Buttons.
        UtopianNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        ShearingAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShearingArea.class);
                startActivity(intent);
            }
        });

        RoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AllRooms.class);
                startActivity(intent);
            }
        });

        OtherPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, OtherPaymentActivity.class);
                startActivity(intent);
            }
        });
    }

//    String GetStatusOfDoctorOpenedOrClosed(int[] doctorDays, int[] fromHour, int[] toHour) {
//        // Init Calendar For Day, Month, and Year.
//        final Calendar now = Calendar.getInstance();
//
//        // Current Date.
//        int currentDay = now.get(Calendar.DAY_OF_WEEK);
//
//        // Current Time 24h Format.
//        int currentTime = now.get(Calendar.HOUR_OF_DAY);
//
//        // Variables To Get Values From Arrays.
//        int fHour = 0, tHour = 0;
//
//        // Init Counter For Loops.
//        int counter = 0;
//
//        // Check If counter Less Than doctorDays.length To Avoid Crashing.
//        while (counter < doctorDays.length) {
//            // Check If Current Time Is For Working Or For Holiday.
//            if (currentDay == doctorDays[counter]) {
//                // Get Values From Arrays.
//                fHour = fromHour[counter];
//                tHour = toHour[counter];
//
//                // Exit For While Loop.
//                break;
//            }
//            // Check Next Index Of Arrays.
//            counter++;
//        }
//
//        // If Doctor works.
//        if (currentTime >= fHour && currentTime <= tHour) {
//
//            return "Opened: Closes at " + tHour;
//        } else {
//            // Get The Next Day.
//            now.roll(Calendar.DATE, 1);
//
//            // Calendar.SHORT Means The First 3 Letters Of Name Of Day OF Week.
//            String nameOfDate = now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
//
//            return "Closed: Opens at " + nameOfDate + " " + fHour;
//        }
//
//    }
}
