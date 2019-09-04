package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.trial5daysworkspace.Model.DrinksDay;
import com.example.android.trial5daysworkspace.Model.InfoOfCustomers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Update extends AppCompatActivity {
    //Init Views.
    LinearLayout LayOutPlaceSpiner, LayOutTotalOfHours;
    Spinner PlaceSpiner;

    // Create Views.
    EditText EditName, EditPhone, EditKitchen, EditDiscount, TxtNote;

    TextView TxtCollege, TxtPlace, TxtStartTime;

    // Buttons.
    Button btnEndTime, UpdateInfo, ActivateAccount;

    // Create Views.
    TextView txtShowEndTime, TotalOfHours, Total;

//    // Strings.
//    String strStartTime, strEndTime;

    // Get Number Of Discount.
    int intDiscount, TotalCash, intKitchen, TotalHours;

    // Store The Old Ke [No. EditPhone In This Case].
    String Key;

    // Init Firebase Database.
    FirebaseDatabase database;

    // Init Ref.
    DatabaseReference refCust;
    DatabaseReference refDrinksDay;


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
        setContentView(R.layout.activity_update);

        // Get Views By IDs.
        LayOutPlaceSpiner = (LinearLayout) findViewById(R.id.layout_place_spiner);
        LayOutTotalOfHours = (LinearLayout) findViewById(R.id.layout_total_of_hours);

        PlaceSpiner = (Spinner) findViewById(R.id.place_spiner);

        EditName = (EditText) findViewById(R.id.edit_name);

        EditPhone = (EditText) findViewById(R.id.edit_phone);

        TxtCollege = (TextView) findViewById(R.id.edit_college);

        EditKitchen = (EditText) findViewById(R.id.total_kitchen);

        EditDiscount = (EditText) findViewById(R.id.total_discount);

        TxtPlace = (TextView) findViewById(R.id.edit_place);

        TxtStartTime = (TextView) findViewById(R.id.start_time);

        // Get Button By ID.
        btnEndTime = (Button) findViewById(R.id.end_time);

        // Get View By ID.
        txtShowEndTime = (TextView) findViewById(R.id.show_end_time);

        // Get View By ID.
        TxtNote = (EditText) findViewById(R.id.edit_note);

        TotalOfHours = (TextView) findViewById(R.id.total_of_hours);

        Total = (TextView) findViewById(R.id.total);

        UpdateInfo = (Button) findViewById(R.id.update_info);

        ActivateAccount = (Button) findViewById(R.id.activate);

        // Get Extras From Intent.
        final Bundle extras = getIntent().getExtras();

        // Check If Extra Not Null.
        if (extras != null) {
            EditName.setText(extras.getString("name"));   // For EditName
            EditPhone.setText(extras.getString("phone"));   // For EditPhone
            TxtCollege.setText(extras.getString("college"));   // For Type Of Customer [VIP Or NOT].
            TxtPlace.setText(extras.getString("place")); // For TxtPlace
            TxtStartTime.setText(extras.getString("startTime"));    // For TxtStartTime

            // Get Key Of User To Allow U Change, Update Or Delete His Data.
            Key = extras.getString("key");
        }

        // Get Instance Of Database.
        database = FirebaseDatabase.getInstance();

        // The Specific Key & Values Need To Update Or Delete.
        refCust = database.getReference("InfoOfCust");
        refDrinksDay = database.getReference("DrinksDay");

        // Call CustTraveled() Method To Know If Cust Traveled Then Make Visibility "GONE" For All Buttons
        // To Prevent User From Edit On Data After Customer Traveling.
        CustTraveled();

        // Click On Button.
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // VISIBLE The txtShowEndTime.
                txtShowEndTime.setVisibility(VISIBLE);

                // Show Current Time From getCurrentTime() Method [As End Time].
                txtShowEndTime.setText(getCurrentTime());

                // Gone The btnEndTime [ Make It INVISIBLE].
                btnEndTime.setVisibility(GONE);
            }
        });

        // Click On Button.
        UpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get All EditText Views.
                final String name = EditName.getText().toString();
                final String phone = EditPhone.getText().toString();
                // If Edit Texts are Not Empty, Make The Strings Equal The Value, Otherwise Make It "0".
                final String kitchen = (!EditKitchen.getText().toString().equals("")) ? EditKitchen.getText().toString() : "0";
                final String discount =(!EditDiscount.getText().toString().equals("")) ? EditDiscount.getText().toString() : "0";

                // Check If Fields Not Empty To Avoid Error.
                if (!name.equals("") && !phone.equals("") && !kitchen.equals("") && !discount.equals("")) {
                    // DisAppear Update Button To Prevent User From Make Edit On Data After Traveling Customer.
                    UpdateInfo.setVisibility(GONE);

                    // Get All Text Views.
                    final String college = TxtCollege.getText().toString();
                    final String place = TxtPlace.getText().toString();
                    final String note = TxtNote.getText().toString();
                    final String startTime = TxtStartTime.getText().toString();
                    final String strEndTime = txtShowEndTime.getText().toString(); // Get String Of End Time

                    // When Press On Button End Time.
                    if (btnEndTime.getVisibility() == GONE) {
                        // Separate Start Time To Hours & Minutes.
                        final String[] separatedStartTime = startTime.split(":");
                        String HourStartTime = separatedStartTime[0];
                        String MinuteStartTime = separatedStartTime[1];

                        // Separate End Time To Hours & Minutes.
                        final String[] separatedEndTime = strEndTime.split(":");
                        String HourEndTime = separatedEndTime[0];
                        String MinuteEndTime = separatedEndTime[1];

                        // Init Var To Use It Into & Out Of Loop.
                        int convertHoursToMinutes;

                        // If HourEndTime Greater That HourStartTime.
                        if (Integer.valueOf(HourEndTime) >= Integer.valueOf(HourStartTime)) {
                            // Convert Current Hours To Minutes + Current Minutes To Get Total Of Minutes.
                            convertHoursToMinutes = (Integer.parseInt(HourEndTime) * 60 + Integer.parseInt(MinuteEndTime))
                                    - (Integer.parseInt(HourStartTime) * 60 + Integer.parseInt(MinuteStartTime));
                        }
                        // Otherwise If HourStartTime Greater That HourEndTime.
                        // That'll Happen When New Day Is Beginning So Increase HourEndTime By 12Hours.
                        else {
                            // Convert Current Hours To Minutes + Current Minutes To Get Total Of Minutes.
                            convertHoursToMinutes = ((Integer.parseInt(HourEndTime) + 12) * 60 + Integer.parseInt(MinuteEndTime))
                                    - (Integer.parseInt(HourStartTime) * 60 + Integer.parseInt(MinuteStartTime));
                        }

                        // Loop To Calculate Time Of Hours
                        // Check If Total Of Time - 60Min >= 0.
                        // 2 For Test.
                        while ((convertHoursToMinutes - 60) >= 0) {
                            // Increase 1 On The Total Of Hours.
                            TotalHours += 1;

                            // Minus 60Min From Total Of Time.
                            // 2 For Test.
                            convertHoursToMinutes -= 60;
                        }

                        // Check If User Take Time Grater Than 30 Min.
                        // 1 For Test.
                        if ((convertHoursToMinutes - 30) >= 0) {
                            // Increase 1 On The Total Of Hours.
                            TotalHours += 1;
                        }

                        // Check Place Is Shared Space &
                        // Hours Greater Than 1 [Start From 30 To 60 min (Check convertHoursToMinutes Var)].
                        if (place.equals("Shared Space") && TotalHours >= 1) {
                            if (TotalHours <= 5)
                                TotalCash = 5 * TotalHours;
                            else
                                // At 6th Hours Make Constant Price = 25.
                                TotalCash = 25;
                        }

                        // Check Place NOT Shared Space &
                        // Hours Greater Than 1 [Start From 30 To 60 min (Check convertHoursToMinutes Var)].
                        else if (!place.equals("Shared Space") && TotalHours >= 1) {

                            // Check When User Become In Room1 To Room5.
                            if (place.equals("Room1") || place.equals("Room2")
                                    || place.equals("Room3") || place.equals("Room4") || place.equals("Room5")) {
                                if (TotalHours <= 8)
                                    TotalCash = 50 * TotalHours;
                                else
                                    // At 5th Hours Make Constant Price = 400.
                                    TotalCash = 400;
                            } else if (place.equals("BigRoom")) {
                                if (TotalHours <= 5)
                                    TotalCash = 75 * TotalHours;
                                else
                                    // At 5th Hours Make Constant Price = 375.
                                    TotalCash = 375;
                            }
                        }
                    }

                    // Get Value Of Discount & Kitchen.
                    intDiscount = Integer.valueOf(discount);
                    intKitchen = Integer.valueOf(kitchen);

                    // Check If (TotalCash + intKitchen - intDiscount) > 0 Else Make total = 0
                    // To Avoid Appear Total Cash By Negative Value.
                    int total = (TotalCash + intKitchen - intDiscount) > 0 ? (TotalCash + intKitchen - intDiscount) : 0;

                    // Set Text Of Total By Total Of Price.
                    Total.setText(String.valueOf(total));

                    // Check If Total > 0 & EndTime Button Is GONE.
                    if (total > 0 && (btnEndTime.getVisibility() == GONE || note.toLowerCase().equals("gone"))) {

                        // Make Listener When Make Change On Single Child.
                        // Need Update Info Of Customer When Press On Btn End Time.
                        refCust.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // Customer Traveled..
                                String custOut = "true";

                                // Make Var From InfoOfCustomers Class To Add Data Into Its Constructor.
                                InfoOfCustomers info;
                                DrinksDay drinksDay;

                                /** HINT:
                                 *  Because We Can't Change NAME OF KEY In Firebase, And We Want Change It.
                                 *  So We Will Make Copy Data Into New Key [New No. Phone] In This Case.
                                 *  And Will Remove Old Key [Old No. Phone] With Old Original Data.
                                 *  Key >> Old No. Phone.
                                 *  phone >> New No. Phone.
                                 */
                                // If The Key [No. Phone In This Case] Is Changed.
                                // Store New Data Into New Key.
                                if (!(phone).equals(Key)) {

                                    // Calculate All Of New & Stored Hours Of This Customers
                                    // To Give Him Offer In Last Of Month.
                                    TotalHours += dataSnapshot.child(Key).child("totalHours").getValue(Integer.class);

                                    // Add Data Into Constructor Of InfoOfCustomers.
                                    // "true" >> Customer Traveled From The Work Space.
                                    info = new InfoOfCustomers(name, phone, college, place,
                                            startTime, strEndTime, note, intDiscount, TotalCash,
                                            intKitchen, TotalHours, custOut);

                                    // Make New Key & Take Copy From Original Data Into It.
                                    refCust.child(phone).setValue(info);

                                    // And Remove old Key With Old Original Data.
                                    refCust.child(Key).removeValue();

                                    // Add Data Into Constructor Of DrinksDay.
                                    drinksDay = new DrinksDay(TotalCash, intKitchen);

                                    // Make Random Key [May Same Customer Come To WorkSpace Twice In Same Day]
                                    // & Take Copy From Original Data Into It.
                                    refDrinksDay.push().setValue(drinksDay);
                                }
                                // If Key [No. Phone In This Case] Not Changed.
                                // Store New Data Into Old Key.
                                else {

                                    // Calculate All Of New & Stored Hours Of This Customers To
                                    // Give Him Offer In Last Of Month.
                                    TotalHours += dataSnapshot.child(Key).child("totalHours").getValue(Integer.class);

                                    // Update Original Data.
                                    // Add Data Into Constructor Of InfoOfCustomers.
                                    // "true" >> Customer Traveled From The Work Space.
                                    info = new InfoOfCustomers(name, phone, college, place,
                                            startTime, strEndTime, note, intDiscount, TotalCash,
                                            intKitchen, TotalHours, custOut);

                                    // Use The Old Key.
                                    refCust.child(Key).setValue(info);

                                    // Add Data Into Constructor Of DrinksDay.
                                    drinksDay = new DrinksDay(TotalCash, intKitchen);

                                    // Make Random Key [Maybe Same Customer Come To WorkSpace Twice In Same Day & Take Drinks]
                                    // & Take Copy From Original Data Into It.
                                    refDrinksDay.push().setValue(drinksDay);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    // Otherwise If Total < 0
                    // User Just Make Update Info.
                    else {
                        // Make Listener When Make Change On Single Child.
                        refCust.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // Customer Not Travel.. Just Make Edit On His Stored Data.
                                String custOut = "false";

                                // Make Var From InfoOfCustomers Class To Add Data Into Its Constructor.
                                InfoOfCustomers info;

                                /** HINT:
                                 *  Because We Can't Change NAME OF KEY In Firebase, And We Want Change It.
                                 *  So We Will Make Copy Data Into New Key [New No. Phone] In This Case.
                                 *  And Will Remove Old Key [Old No. Phone] With Old Original Data.
                                 *  Key >> Old No. Phone.
                                 *  phone >> New No. Phone.
                                 */
                                // If The Key [No. Phone In This Case] Is Changed.
                                // Store New Data Into New Key.
                                if (!(phone).equals(Key)) {

                                    // Calculate All Of New & Stored Hours Of This Customers To
                                    // Give Him Offer In Last Of Month.
                                    TotalHours += dataSnapshot.child(Key).child("totalHours").getValue(Integer.class);

                                    // Add Data Into Constructor Of InfoOfCustomers.
                                    // "false" >> Customer Not Travel From The Work Space.
                                    info = new InfoOfCustomers(name, phone, college, place,
                                            startTime, strEndTime, note, intDiscount, TotalCash,
                                            intKitchen, TotalHours, custOut);

                                    // Make New Key & Take Copy From Original Data Into It.
                                    refCust.child(phone).setValue(info);

                                    // And Remove old Key With Old Original Data.
                                    refCust.child(Key).removeValue();

                                    Toast.makeText(Update.this, "Data Updated", Toast.LENGTH_SHORT).show();

                                    // Close This Activity When Finish.
                                    finish();
                                }
                                // If Key [No. Phone In This Case] Not Changed.
                                // Store New Data Into Old Key.
                                else {

                                    // Calculate All Of New & Stored Hours Of This Customers To
                                    // Give Him Offer In Last Of Month.
                                    TotalHours += dataSnapshot.child(Key).child("totalHours").getValue(Integer.class);

                                    // Update Original Data.
                                    // Add Data Into Constructor Of InfoOfCustomers.
                                    // "false" >> Customer Not Travel From The Work Space.
                                    info = new InfoOfCustomers(name, phone, college, place,
                                            startTime, strEndTime, note, intDiscount, TotalCash,
                                            intKitchen, TotalHours, custOut);

                                    // Use The Old Key.
                                    refCust.child(Key).setValue(info);

                                    Toast.makeText(Update.this, "Data Updated", Toast.LENGTH_SHORT).show();

                                    // Close This Activity When Finish.
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                } else
                    Toast.makeText(Update.this, "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();


            }
        });

        // When Click On ActivateAccount Button.
        ActivateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call ReActivateAccount Method.
                ReActivateAccount();
            }
        });
    }

    // Method For Prevent Update Price After Finish Work.
    private void CustTraveled() {
        // Reference Of Data Of This Customer.
        refCust.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Value Of Key "custOut" To Check If It True Or False.
                String CustOut = dataSnapshot.child("custOut").getValue(String.class);
                String Note = dataSnapshot.child("note").getValue(String.class);
                int TotalOfCash = dataSnapshot.child("totalCash").getValue(Integer.class);

                // Check If Customer Still In The Work Space Or Traveled From It Before Of After 30 Min.
                if (CustOut.equals("true") && (TotalOfCash > 0 || Note.toLowerCase().equals("gone"))) {
                    // Change Visibility Of Each View To Prevent From Making New Update On Stored Data.
                    txtShowEndTime.setVisibility(VISIBLE);
                    ActivateAccount.setVisibility(VISIBLE);
                    LayOutPlaceSpiner.setVisibility(VISIBLE);
                    LayOutTotalOfHours.setVisibility(VISIBLE);
                    btnEndTime.setVisibility(GONE);
                    UpdateInfo.setVisibility(GONE);

                    // Retrieve All Data From Firebase Of Customer Traveled.
                    InfoOfCustomers RetrieveData = dataSnapshot.getValue(InfoOfCustomers.class);

                    // Set All This Data Into Views.
                    EditName.setText(RetrieveData.getName());   // For Name
                    EditPhone.setText(RetrieveData.getPhone());   // For Phone
                    TxtCollege.setText(RetrieveData.getCollege());   // For Type Of Customer [VIP Or NOT].
                    TxtPlace.setText(RetrieveData.getPlace()); // For Place
                    EditKitchen.setText(String.valueOf(RetrieveData.getKitchen()));    // For TotalCash
                    EditDiscount.setText(String.valueOf(RetrieveData.getDiscount()));    // For Discount
                    TxtNote.setText(RetrieveData.getNote());    // For Note
                    TxtStartTime.setText(RetrieveData.getStartTime());    // For TxtStartTime
                    txtShowEndTime.setText(RetrieveData.getEndTime());    // For EndTime
                    TotalOfHours.setText(String.valueOf(RetrieveData.getTotalHours()));    // For TotalOfHours
                    Total.setText(String.valueOf(RetrieveData.getTotalCash() + RetrieveData.getKitchen()));    // For TotalCash + Total Kitchen
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Method For ReActivateAccount When Same Customer ComeBack Again.
    private void ReActivateAccount() {
        // Get Selected New Place Of Customer.
        String selectedNewPlace = PlaceSpiner.getSelectedItem().toString();

        // Reset Values From Firebase.
        refCust.child(Key).child("custOut").setValue("false");
        refCust.child(Key).child("place").setValue(selectedNewPlace);
        refCust.child(Key).child("startTime").setValue(getCurrentTime());
        refCust.child(Key).child("kitchen").setValue(0);
        refCust.child(Key).child("discount").setValue(0);
        refCust.child(Key).child("note").setValue("");
        refCust.child(Key).child("totalCash").setValue(0);

        // Set Visibility.
        txtShowEndTime.setVisibility(GONE);
        ActivateAccount.setVisibility(GONE);
        LayOutPlaceSpiner.setVisibility(GONE);
        LayOutTotalOfHours.setVisibility(GONE);
        btnEndTime.setVisibility(VISIBLE);
        UpdateInfo.setVisibility(VISIBLE);

        // Out From This Activity To Previous Activity.
        finish();
    }

    // Method for get current time.
    public static String getCurrentTime() {
        Calendar now = Calendar.getInstance();
        // % 12 To Convert Time From 24 Hours To 12 Hours.
        String currentDate = (now.get(Calendar.HOUR_OF_DAY) % 12) + ":" + now.get(Calendar.MINUTE);

        return currentDate;
    }

    // When Press On Back Button.. Just make Refresh For Data Into Home TxtCollege.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Update.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
