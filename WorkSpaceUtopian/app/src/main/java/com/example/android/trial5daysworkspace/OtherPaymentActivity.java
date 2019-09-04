package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.trial5daysworkspace.Database.DBHelper;
import com.example.android.trial5daysworkspace.Model.DrinksDay;
import com.example.android.trial5daysworkspace.Model.ExpiredTrial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OtherPaymentActivity extends AppCompatActivity {

    // Init Views.
    Button AddOtherCash;
    MaterialEditText OtherCash, NoteOtherCash;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference refDrinksDay;

    DatabaseReference refExpiredTime;

    ExpiredTrial expired;

    DBHelper db;

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

        setContentView(R.layout.activity_other_payment);

        // Init Firebase Database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        refDrinksDay = firebaseDatabase.getReference("DrinksDay");

        // Make Reference.
        refExpiredTime = firebaseDatabase.getReference("ExpiredTime");

        // Send This Into DBHelper Class As Param.
        db = new DBHelper(this);

        // Get Views By IDs.
        AddOtherCash = (Button) findViewById(R.id.add_other_cashes);
        OtherCash = (MaterialEditText) findViewById(R.id.other_cashes);
        NoteOtherCash = (MaterialEditText) findViewById(R.id.note_other_cashes);

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

        AddOtherCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Value From Input's User.
                // If Value Into Edit Text Greater Than 0, Store It Into otherCash Variable Otherwise Make It 0.
                int otherCash = (Integer.valueOf(OtherCash.getText().toString()) > 0) ? Integer.valueOf(OtherCash.getText().toString()) : 0;
                String noteOtherCash = NoteOtherCash.getText().toString();

                // otherCash Not Equal 0 Maybe - Or + Cash.
                if ((otherCash > 0 || otherCash < 0) && !noteOtherCash.equals("")){

                    // Add Value To Constructor Of DrinksDay Class.
                    DrinksDay ExtraCash = new DrinksDay(otherCash, noteOtherCash);

                    // Store Value Into Firebase Database refDrinksDay Path.
                    refDrinksDay.push().setValue(ExtraCash);

                    // Store noteOtherCash Into SqlLite Database To Store Large Hour To Make Offers.
                    // To Make BackUp For Manager.
                    db.insertManagerNote(noteOtherCash);

                    Toast.makeText(OtherPaymentActivity.this, "Add (" + otherCash + ") to cash of day", Toast.LENGTH_SHORT).show();

                    // Out Of This Activity.
                    finish();

                }else
                    Toast.makeText(OtherPaymentActivity.this, "Fields Cannot be 0 or Empty", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
