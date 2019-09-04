package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.android.trial5daysworkspace.Database.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecoveryData extends AppCompatActivity {

    // Object From DBHelper.
    DBHelper db;

    // ArrayList For ListView.
    ArrayList<String> listItem;

    // ArrayAdapter For ListView.
    ArrayAdapter adapter;

    // ListView.
    ListView UserList;

    // Button.
    Button StoreDataIntoFile;

    // Get Extra From Intent.
    Bundle extras;

    // To Make All App Like As The Same Font Make Sure To Put This Code Before onCreate Method
    // In Every Activity Press Ctrl + O.
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To Make All App Like As The Same Font Make Sure To Put This Code Before setContentView Method
        // In Every Activity.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Century Gothic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_recovery_data);

        // Get Extra From Intent.
        extras = getIntent().getExtras();

        // Get Button By ID.
        StoreDataIntoFile = (Button) findViewById(R.id.store_into_file);

        // Get ListView By ID.
        UserList = (ListView) findViewById(R.id.list_user_recovery_view);


        // Create Constructor Take This Activity As A Parameter.
        db = new DBHelper(this);

        // Create New ArrayList.
        listItem = new ArrayList<>();

        // Get Extras From Intent.
        final Bundle extras = getIntent().getExtras();

        // Check If Extra Not Null.
        if (extras != null && extras.getString("key").equals("cash")) {
            // Call Method.
            showListItemCash();
        } else if (extras != null && extras.getString("key").equals("info"))
            // Call Method.
            showAllInformation();
        else
            // Call Method.
            showListManagerNote();


        // Store Items Of Listview Into Text File When Click On This Button.
        StoreDataIntoFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Global File Var.
                File locationFile;

                try {
                    // Check If Extra Not Null.
                    if (extras.getString("key").equals("cash"))
                        // Store Items Of Listview Into Text File.
                        locationFile = new File("/sdcard/UtopianTotalCashRecovery.txt");
                     else if (extras.getString("key").equals("info"))
                        // Store Items Of Listview Into Text File.
                        locationFile = new File("/sdcard/UtopianInformationRecovery.txt");
                     else
                        // Store Items Of Listview Into Text File.
                        locationFile = new File("/sdcard/UtopianExtraCashNoteRecovery.txt");

                    // Make New File.
                    locationFile.createNewFile();

                    // Open File.
                    FileOutputStream fOut = new FileOutputStream(locationFile);

                    // Start To Write Into File.
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    // Write Lines Of Words Into File.
                    // Items of Listview have [,] Between Each One. In This Case I Don't Need [,] So Replace with 2 New Lines.
                    myOutWriter.append(listItem.toString().replace(",", "\n\n"));

                    // Close File.
                    myOutWriter.close();
                    fOut.close();

                    // Message.
                    Toast.makeText(getBaseContext(), "Data is saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method To Show List Of Cash Into ListView.
    private void showListItemCash() {
        // Get viewRecoveryData() Method From DBHelper Class.
        Cursor cursor = db.viewCash();

        // If Count Of Rows = 0.
        if (cursor.getCount() == 0)
            Toast.makeText(this, "No Data To Show", Toast.LENGTH_SHORT).show();

            // OtherWise.
        else {
            // Cursor Starts From Index -1, So Move Next [Index 0] If Available.
            while (cursor.moveToNext()) {
                // Add Total Of Days & DrinksDay Into List.
                // 'Cause I Selected All, ID Take Index 0 [Check viewAllInfo() Method Into DBHelper.java].
                listItem.add("TotalOfDay: " + cursor.getString(1) + "\n" + // 1 For TotalOfDay
                        "TotalOfDrinks: " + cursor.getString(2) + "\n"); // 2 For TotalOfDrinks
            }
        }
        // Make Adapter For ListView.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
        UserList.setAdapter(adapter);
    }

    // Method To Show List Of Hour Into ListView.
    private void showAllInformation() {
        // To Make Refresh ArrayList.
        listItem.clear();

        // Get viewRecoveryData() Method From DBHelper Class.
        Cursor cursor = db.viewAllInfo();

        // If Count Of Rows = 0.
        if (cursor.getCount() == 0)
            Toast.makeText(this, "No Data To Show", Toast.LENGTH_SHORT).show();

            // OtherWise.
        else {
            // Cursor Starts From Index -1, So Move Next [Index 0] If Available.
            while (cursor.moveToNext()) {
                // Add Total Of Days & DrinksDay Into List.
                // 'Cause I Didn't Select All, ID Didn't Take Index 0 [Check viewAllInfo() Method Into DBHelper.java].
                listItem.add("CUSTName: " + cursor.getString(0) + "\n" + // 1 For CUSTName.
                        "CUSTPhone: " + cursor.getString(1) + "\n" + // 2 For CUSTPhone.
                        "CUSTCollege: " + cursor.getString(2) + "\n" + // 3 For CUSTCollege.
                        "CUSTHours: " + cursor.getString(3) + "\n"); // 4 For CUSTHours.
            }
        }
        // Make Adapter For ListView.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
        UserList.setAdapter(adapter);
    }

    // Note For Manager To Know About Extra Cash Into OtherPaymentActivity.java.
    private void showListManagerNote() {

        // Get viewRecoveryData() Method From DBHelper Class.
        Cursor cursor = db.viewManagerNote();

        // If Count Of Rows = 0.
        if (cursor.getCount() == 0)
            Toast.makeText(this, "No Data To Show", Toast.LENGTH_SHORT).show();

            // OtherWise.
        else {
            // Cursor Starts From Index -1, So Move Next [Index 0] If Available.
            while (cursor.moveToNext()) {
                // Add Total Of Days & DrinksDay Into List.
                listItem.add("Note: " + cursor.getString(1) + "\n" ); // 1 For Notes.
            }
        }
        // Make Adapter For ListView.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
        UserList.setAdapter(adapter);
    }


}
