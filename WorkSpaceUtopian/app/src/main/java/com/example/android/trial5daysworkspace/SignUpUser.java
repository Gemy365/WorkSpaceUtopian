package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.trial5daysworkspace.Model.InfoOfCustomers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpUser extends AppCompatActivity {

    // Init FireBase Database.
    FirebaseDatabase database;

    // Init FireBase Reference.
    DatabaseReference reference;

    // Init Adapter Class [Setter & Getter].
    InfoOfCustomers info;

    // EditText.
    EditText Name, Phone, College;

    // TextViews.
    TextView Place, StartTime;

    // Buttons.
    Button Start;

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
        setContentView(R.layout.activity_sign_up_user);


        // Active Firebase Database.
        database = FirebaseDatabase.getInstance();

        // Make reference Has Key InfoOfCust.
        reference = database.getReference("InfoOfCust");

        // Get EditText By ID.
        Name = (EditText) findViewById(R.id.reg_name);
        Phone = (EditText) findViewById(R.id.reg_phone);
        College = (EditText) findViewById(R.id.reg_college);

        // Get TextViews By IDs.
        Place = (TextView) findViewById(R.id.edit_place);
        StartTime = (TextView) findViewById(R.id.reg_start_time);

        // Get Button By ID.
        Start = (Button) findViewById(R.id.reg_start);

        // Check The TxtPlace Of Customer & Store It Into TxtPlace TextView.
        PlaceOfCutomers();

        // Click On Button.
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store EditName [Written From User] Into name String.
                final String name = Name.getText().toString();

                // Store EditPhone [Written From User] Into phone String.
                final String phone = Phone.getText().toString();

                // Store EditPhone [Written From User] Into phone String.
                final String college = College.getText().toString();

                // Store TextPlace From Extra Of Intent Into place String.
                final String place = Place.getText().toString();

                // Store CurrentTime From Method getCurrentTime().
                final String startTime = getCurrentTime(); // Get Current Time.

                if (!name.equals("") && !phone.equals("") && !college.equals("")) {
                    // .orderByKey().equalTo(phone) Means >> Where Key Equal To [Phone].
                    reference.orderByKey().equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Check If Phone Not Exists In Keys Of Firebase.
                            if (!dataSnapshot.exists()) {
                                // Add All Info Of Customer Into Constructor Of Adapter Class [Setter & Getter].
                                // First "true" >> The Same Customer In Work Space, In Next Time Don't Need To Sign Up Him Again, I Signed Him Once.
                                // Second "false" >> Customer Traveled From The Work Space.
                                info = new InfoOfCustomers(name, phone, college, place, startTime, "false");

                                // Set All This Info Into Firebase Database By Key [EditPhone No.] To Be Unique For Every Customer.
                                reference.child(phone).setValue(info);

                                Toast.makeText(SignUpUser.this, "Data Added", Toast.LENGTH_SHORT).show();

                                // Reset EditViews.
                                Name.setText("");
                                Phone.setText("");
                                College.setText("");

                                // GoTo Home Group.
                                Intent intent = new Intent(SignUpUser.this, Home.class);
                                startActivity(intent);

                            } else
                                Toast.makeText(SignUpUser.this, "This phone is already exists!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                // OtherWise.
                else
                    Toast.makeText(SignUpUser.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method for get current time.
    public static String getCurrentTime() {
        Calendar now = Calendar.getInstance();
        // % 12 To Convert Time From 24 Hours To 12 Hours.
        String currentDate = (now.get(Calendar.HOUR_OF_DAY) % 12) + ":" + now.get(Calendar.MINUTE);

        return currentDate;

//        // Init Calendar.
//        Calendar now = Calendar.getInstance();
//
//        // 'Cause JANUARY is 0 index We Need To Store Month Into String Then Increase 1 For Current Month.
//        // DATE is The Day.
//        String currentDate = now.get(Calendar.YEAR) + ":" + (now.get(Calendar.MONTH) + 1) + ":" + now.get(Calendar.DATE);
//
//        Toast.makeText(this, ""+ currentDate, Toast.LENGTH_LONG).show();
    }

    // Method To Know TxtPlace Of Customers.
    private void PlaceOfCutomers() {
        // Get Extras From Intent.
        Bundle extras = getIntent().getExtras();

        // Check If extras Not Null.
        assert extras != null;
        // Check If extras Contains Key Like ("...").
        if (extras.containsKey("Shared Space"))
            // Set Text For TxtPlace.
            Place.setText(extras.getString("Shared Space"));

        else if (extras.containsKey("Room1"))
            Place.setText(extras.getString("Room1"));

        else if (extras.containsKey("Room2"))
            Place.setText(extras.getString("Room2"));

        else if (extras.containsKey("Room3"))
            Place.setText(extras.getString("Room3"));

        else if (extras.containsKey("Room4"))
            Place.setText(extras.getString("Room4"));

        else if (extras.containsKey("Room5"))
            Place.setText(extras.getString("Room5"));

        else if (extras.containsKey("BigRoom"))
            Place.setText(extras.getString("BigRoom"));
    }
}
