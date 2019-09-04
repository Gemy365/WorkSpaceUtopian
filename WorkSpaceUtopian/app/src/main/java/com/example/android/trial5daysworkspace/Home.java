package com.example.android.trial5daysworkspace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.example.android.trial5daysworkspace.Database.DBHelper;
import com.example.android.trial5daysworkspace.Model.ExpiredTrial;
import com.example.android.trial5daysworkspace.Model.InfoOfCustomers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Object Of DBHelper Class.
    DBHelper db;

    // Initialize Firebase Database.
    FirebaseDatabase database;
    // Initialize Firebase Reference InfoOfCust [Root].
    DatabaseReference refInfoCust;

    // Initialize Firebase Reference refDrinksDay [Root].
    DatabaseReference refDrinksDay;

    // Initialize Firebase Reference refExpiredTime [Root].
    DatabaseReference refExpiredTime;

    // Object Of ExpiredTrial.
    ExpiredTrial expired;

    // RecyclerView To Get Data From Firebase Database.
    RecyclerView RetrieveDataRv;

    // ArrayList Of Adapter Class [InfoOfCustomers] Setter & Getter Methods.
    // Store All Data To Use Them Positions Into ItemClickListener Method.
    ArrayList<InfoOfCustomers> list;

    // FirebaseRecyclerOptions<ClassOfGetter&Setter> [From Lib Firebase UI].
    FirebaseRecyclerOptions<InfoOfCustomers> options;

    // FirebaseRecyclerAdapter is New Feature As ArrayList & ArrayAdapter. [From Lib Firebase UI].
    FirebaseRecyclerAdapter<InfoOfCustomers, UserViewHolder> adapter;

    // Create TextView.
    TextView TotalOfDay, TotalOfDrinks;

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

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Constructor Take This Activity.
        db = new DBHelper(this);

        // Store All Data To Use Them Positions Into ItemClickListener Method.
        list = new ArrayList<InfoOfCustomers>();

        // Start With Instance From Firebase Database Object.
        database = FirebaseDatabase.getInstance();

        // Create InfoOfCust As New Root Name.
        refInfoCust = database.getReference("InfoOfCust");

        // Create HoursOfCust As New Root Name.
        refDrinksDay = database.getReference("DrinksDay");

        // Make Reference.
        refExpiredTime = database.getReference("ExpiredTime");

        // Get RecyclerView By IDs.
        RetrieveDataRv = (RecyclerView) findViewById(R.id.retrieve_data);

        // Fixed Size Between Items Of RV.
        RetrieveDataRv.setHasFixedSize(true);

        // Manage The Items Of RV.
        RetrieveDataRv.setLayoutManager(new LinearLayoutManager(this));

        // Get View By ID For Total Of Money.
        TotalOfDay = (TextView) findViewById(R.id.total_of_cash);
        TotalOfDrinks = (TextView) findViewById(R.id.total_of_drinks);

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

        // Call Method To Show Data Into Recycler View.
        ShowListItem();

        // Call Method To Get Total Of Day & DrinksDay.
        TotalOfDayDrinks();

        /**   Gradle Lib Allowable Click Item Of Recyclerview.
         *     implementation 'com.chootdev:recycleclick:1.0.0'
         *     RecycleClick.addTo(YOUR_RECYCLEVIEW).setOnItemClickListener()
         */
        RecycleClick.addTo(RetrieveDataRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int i, View v) {
                // Get Item By Position And Cast It Into Adapter Class [DrinksDay Getter & Setter].
                // Get list [ArrayList] by Position.
                InfoOfCustomers user = (InfoOfCustomers) list.get(i);

                // Toast.makeText(Home.this, "Clicked On " + i, Toast.LENGTH_SHORT).show();

                // GoTo UpdateDeleteData Group.
                Intent intent = new Intent(Home.this, Update.class);
                // Take Some Of Value To The UpdateDeleteData Group To Use Them There.
                intent.putExtra("name", user.getName());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("college", user.getCollege());
                intent.putExtra("place", user.getPlace());
                intent.putExtra("startTime", user.getStartTime());

                // The Key As The Same No. EditPhone.
                intent.putExtra("key", user.getPhone());
                // Start Intent.
                startActivity(intent);
            }
        });

        /**   Gradle Lib Allowable Click Item Of Recyclerview.
         *     implementation 'com.chootdev:recycleclick:1.0.0'
         *     RecycleClick.addTo(YOUR_RECYCLEVIEW).setOnItemLongClickListener()
         */
        RecycleClick.addTo(RetrieveDataRv).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                // Get Item By Position And Cast It Into Adapter Class [DrinksDay Getter & Setter].
                // Get list [ArrayList] by Position.
                final InfoOfCustomers user = (InfoOfCustomers) list.get(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(Home.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + user.getName() + " ?");

                // Buttons.
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove Customer From Firebase Has This Phone As ID.
                        refInfoCust.child(user.getPhone()).removeValue();
                        // Call ShowListItem() Method As A Refresh It All The Time After Deleting.
                        ShowListItem();
                    }
                });
                // Show AlertDialog.
                adb.show();

                return true;
            }
        });


        // Internal Widgets Of Navigation Drawer Activity.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Internal Widgets Of Navigation Drawer Activity.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Method To Get Total Of Day & DrinksDay From Firebase Database.
    private void TotalOfDayDrinks() {

        // Get Total Of Cash From All Children Have Child Called "TotalCash" & Store It Into TotalOfDay TextView.
        refDrinksDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Strings To Store Total Of Cash From Firebase Database.
                int intTotalOfDay = 0;
                // Strings To Store Total Of Drinks From Firebase Database.
                int intTotalOfDrinks = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    intTotalOfDay += ds.child("totalDay").getValue(Integer.class);
                    intTotalOfDrinks += ds.child("totalDrink").getValue(Integer.class);
                }
                // Set TextViews.
                TotalOfDay.setText(String.valueOf(intTotalOfDay));
                TotalOfDrinks.setText(String.valueOf(intTotalOfDrinks));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        // Get Total Of DrinksDay From All Children Have Child Called "TotalCash" & Store It Into TotalOfDrinks TextView.
//        refInfoCust.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Strings To Store Total Of Drinks From Firebase Database.
//                int strTotalOfDrinks = 0;
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    strTotalOfDrinks += ds.child("totalDrinks").getValue(Integer.class);
//                }
//                TotalOfDrinks.setText(String.valueOf(strTotalOfDrinks));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    // Method To Show List Item On RecyclerView To Show Data.
    private void ShowListItem() {
        // Clear List As A Refresh It All The Time.
        list.clear();

        // FirebaseRecyclerOptions<ClassOfGetter&Setter> [From Lib Firebase UI].
        options = new FirebaseRecyclerOptions.Builder<InfoOfCustomers>()
                .setQuery(refInfoCust, InfoOfCustomers.class).build();

        // FirebaseRecyclerAdapter is New Feature As ArrayList & ArrayAdapter. [From Lib Firebase UI].
        adapter = new FirebaseRecyclerAdapter<InfoOfCustomers, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull InfoOfCustomers model) {
                // holder var of Own UserViewHolder has EditName TextView & Other TextViews.
                // model var of  Own DrinksDay Adapter Class has [Getter & Setter Methods].
                holder.Name.setText(model.getName());
                holder.Place.setText(model.getPlace());
                holder.StartTime.setText(model.getStartTime());

                // false Means Cust Still In Work Space.
                // Set Resource Of Image [Green Circle] Like Customer Active.
                if (model.getCustOut().equals("false"))
                    holder.ActiveCust.setImageResource(R.drawable.active_cust);
                else
                    // Otherwise Remove Resource Of Image Like Customer Not Active.
                    holder.ActiveCust.setImageDrawable(null);

                // Store All Data Into ArrayList To Use Them Positions Into ItemClickListener Method.
                // Check This Line >> list.get(i);
                list.add(model);

            }

            @NonNull
            @Override

            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Always RecyclerViews To Make New One.
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.textviews_retrieve_data, viewGroup, false);

                // Return New Constructor Has view Param.
                return new UserViewHolder(view);
            }
        };

        // Set Adapter Of RecycleView.
        RetrieveDataRv.setAdapter(adapter);
        // Start Adapter Listening.
        adapter.startListening();
    }

    // Method To Search On Specific One Customer [Called From searchView.setOnQueryTextListener].
    // SearchText Param Has Typing Of User [EditPhone No. In THis Case].
    private void FirebaseSearching(String SearchText) {
        // Clear List As A Refresh It All The Time.
        list.clear();

        // Query To Get Customer Own This EditPhone No.
        // orderByChild("phone") Like ['Where EditPhone' In SQL] Is startAt(SearchText) & endAt(SearchText + "\uf8ff") ["\uf8ff"] End Of Firebase.
        Query query = refInfoCust.orderByChild("name").startAt(SearchText).endAt(SearchText + "\uf8ff");

        // FirebaseRecyclerOptions<ClassOfGetter&Setter> [From Lib Firebase UI].
        options = new FirebaseRecyclerOptions.Builder<InfoOfCustomers>()
                .setQuery(query, InfoOfCustomers.class).build();

        // FirebaseRecyclerAdapter is New Feature As ArrayList & ArrayAdapter. [From Lib Firebase UI].
        adapter = new FirebaseRecyclerAdapter<InfoOfCustomers, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull InfoOfCustomers model) {
                // holder var of Own UserViewHolder has EditName TextView & Other TextViews.
                // model var of  Own DrinksDay Adapter Class has [Getter & Setter Methods].
                holder.Name.setText(model.getName());
                holder.Place.setText(model.getPlace());
                holder.StartTime.setText(model.getStartTime());

                // false Means Cust Still In Work Space.
                // Set Resource Of Image [Green Circle] Like Customer Active.
                if (model.getCustOut().equals("false"))
                    holder.ActiveCust.setImageResource(R.drawable.active_cust);
                else
                    // Otherwise Remove Resource Of Image Like Customer Not Active.
                    holder.ActiveCust.setImageDrawable(null);

                // Store All Data Into ArrayList To Use Them Positions Into ItemClickListener Method.
                // Check This Line >> list.get(i);
                list.add(model);

            }

            @NonNull
            @Override

            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Always RecyclerViews To Make New One.
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.textviews_retrieve_data, viewGroup, false);

                // Return New Constructor Has view Param.
                return new UserViewHolder(view);
            }
        };

        // Set Adapter Of RecycleView.
        RetrieveDataRv.setAdapter(adapter);

        // Start Adapter Listening.
        adapter.startListening();
    }


    // When Start.
    @Override
    protected void onStart() {
        super.onStart();

        // Call ShowListItem Method Every Time Start TxtCollege.
        ShowListItem();

        // Clear List As A Refresh It All The Time Start this Group.
        list.clear();

        // Start Adapter Listening.
        adapter.startListening();
    }

    // When Stop.
    @Override
    protected void onStop() {
        super.onStop();
        // Stop Adapter Listening.
        adapter.stopListening();
    }

    // When Press Back.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Make Search View For Filteration Recycler View To Select What I Searched For.
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Create Search View To Search On EditName.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Make Text As Type Of Searching.
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        // Make Hint For Search View.
        searchView.setQueryHint("Search by Name...");

        // When User End Of Him Typing & Press On Search Icon In Keyboard Of Mobile.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String submitText) {
                // Call FirebaseSearching Method & Pass User's Searching As A Param.
                FirebaseSearching(submitText);

                // If User Delete Every Searching, Call ShowListItem Method To Appear All Data.
                if (submitText.equals(""))
                    ShowListItem();

                return false;
            }

            // Always Change Searching If User Still Typing Some Letters Or Numbers To Search.
            @Override
            public boolean onQueryTextChange(String newText) {
                // Call FirebaseSearching Method & Pass User's Searching As A Param.
                FirebaseSearching(newText);

                // If User Delete Every Searching, Call ShowListItem Method To Appear All Data.
                if (newText.equals(""))
                    ShowListItem();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Check If Need To Just Reset Cash Of Day.
        if (id == R.id.action_reset_cash) {

            String strTotalOfDay = TotalOfDay.getText().toString();
            String strTotalOfDrinks = TotalOfDrinks.getText().toString();

            // If Total Of Day Not Equal 0 That's Mean There's Data To Delete It.
            if (!strTotalOfDay.equals("0")) {
                // Add Total Of Days & DrinksDay Into SqlLite Database To Store Total Of Cash Before Delete It From Firebase.
                // To Make BackUp For Manager.
                db.insertCash(strTotalOfDay, strTotalOfDrinks);

                // Remove All Data From refDrinksDay Path Into Firebase Database.
                refDrinksDay.removeValue();

                // Reset Total Of Day When Day Is Done.
                TotalOfDrinks.setText("0");
                TotalOfDay.setText("0");

            } else
                Toast.makeText(this, "There's no Cash to Reset it", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recover_cash) {
            Intent intent = new Intent(Home.this, RecoveryData.class);

            // Key To Use Three Intent Into The Same Activity With Single ArrayList
            // Check nav_recover_cash & nav_recover_best_customers  & nav_recover_manager_note Into Home Activity
            // & ArrayList Into RecoveryData Activity.
            intent.putExtra("key", "cash");
            startActivity(intent);
        }

        // Get Large Time Of Hours Of Customers To Make Offer For Him.
        else if (id == R.id.nav_recover_info_customers) {
            // Get All Info From refInfoCust Path Of Firebase To Get Large Hour To Make Offer For Customer.
            refInfoCust.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Global Vars.
                    String CustName, CustPhone, CustCollege;
                    // Init Value Of Number Of Hours.
                    int CustHour;

                    // Get All Value From Firebase Database.
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Check If Next Hour Is Larger That Current Hour.
                        // Update Value Of Var.
                        CustName = ds.child("name").getValue(String.class);
                        CustPhone = ds.child("phone").getValue(String.class);
                        CustCollege = ds.child("college").getValue(String.class);
                        CustHour = ds.child("totalHours").getValue(Integer.class);

                        // Add CustName & CustPhone & CustHour Into SqlLite Database To Store Large Hour To Make Offers.
                        // To Make BackUp For Manager.
                        db.insertAllInfo(CustName, CustPhone, CustCollege, CustHour);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Intent intent = new Intent(Home.this, RecoveryData.class);

            // Key To Use Three Intent Into The Same Activity With Single ArrayList
            // Check nav_recover_cash & nav_recover_best_customers  & nav_recover_manager_note Into Home Activity
            // & ArrayList Into RecoveryData Activity.
            intent.putExtra("key", "info");
            startActivity(intent);
        }

        // Get Note Of Extra Cash For MANAGER.
        else if (id == R.id.nav_recover_manager_note) {
            Intent intent = new Intent(Home.this, RecoveryData.class);

            // Key To Use Three Intent Into The Same Activity With Single ArrayList
            // Check nav_recover_cash & nav_recover_best_customers  & nav_recover_manager_note Into Home Activity
            // & ArrayList Into RecoveryData Activity.
            intent.putExtra("key", "note");
            startActivity(intent);
        }

        // Get Large Time Of Hours Of Customers To Make Offer For Him.
        else if (id == R.id.nav_delete_all_info) {
            final String strTotalOfDay = TotalOfDay.getText().toString();
            final String strTotalOfDrinks = TotalOfDrinks.getText().toString();

            // If Total Of Day Not Equal 0 That's Mean There's Data To Delete It.
            if (!strTotalOfDay.equals("0")) {
                // Appear Alert For User To Alarm Him.
                AlertDialog.Builder adb = new AlertDialog.Builder(Home.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete all information ?");

                // Buttons.
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Add Total Of Days & DrinksDay Into SqlLite Database To Store Total Of Cash Before Delete It From Firebase.
                        // To Make BackUp For Manager.
                        db.insertCash(strTotalOfDay, strTotalOfDrinks);

                        // Remove All Data From InfoOfCust Path Into Firebase Database.
                        refInfoCust.removeValue();

                        // Remove All Data From refDrinksDay Path Into Firebase Database.
                        refDrinksDay.removeValue();

                        // Reset Total Of Day When Day Is Done.
                        TotalOfDrinks.setText("0");
                        TotalOfDay.setText("0");
                    }
                });
                // Show AlertDialog.
                adb.show();
            } else
                Toast.makeText(this, "There's no Data to Delete it", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
