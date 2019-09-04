package com.example.android.trial5daysworkspace.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Class For SQLite DATABASE.
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Vienna.db";
    private static final String CUSTCHASHTABLE = "CUSTCHASHTABLE";
    private static final String CUSTINFOTABLE = "CUSTHOURTABLE";
    private static final String MANAGERTABLE = "MANAGERTABLE";
    private static final String ID_CASH = "ID";
    private static final String ID_HOUR = "ID";
    private static final String ID_MANAGER = "ID";
    private static final String TOTALOFDAY = "TOTALOFDAY";
    private static final String TOTALOFDRINKS = "TOTALOFDRINKS";
    private static final String CUSTNAME = "CUSTNAME";
    private static final String CUSTPHONE = "CUSTPHONE";
    private static final String CUSTCOLLEGE = "CUSTCOLLEGE";
    private static final String CUSTHOURS = "CUSTHOURS";
    private static final String MANAGERNOTE = "MANAGERNOTE";

    private static final String CREATE_CASH_TABLE = "CREATE TABLE " + CUSTCHASHTABLE + " (" +
            ID_CASH + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TOTALOFDAY + " TEXT, " +
            TOTALOFDRINKS + " TEXT )";

    private static final String CREATE_HOUR_TABLE = "CREATE TABLE " + CUSTINFOTABLE + " (" +
            ID_HOUR + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CUSTNAME + " TEXT, " +
            CUSTPHONE + " TEXT, " +
            CUSTCOLLEGE + " TEXT, " +
            CUSTHOURS + " INTEGER)";

    private static final String CREATE_MANAGER_TABLE = "CREATE TABLE " + MANAGERTABLE + " (" +
            ID_MANAGER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MANAGERNOTE + " TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CASH_TABLE);
        sqLiteDatabase.execSQL(CREATE_HOUR_TABLE);
        sqLiteDatabase.execSQL(CREATE_MANAGER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CUSTCHASHTABLE);
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CUSTINFOTABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MANAGERTABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Method To Insert Data.
    public boolean insertCash(String totalOfDay, String totalOfDrinks) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TOTALOFDAY, totalOfDay);
        contentValues.put(TOTALOFDRINKS, totalOfDrinks);


        long result = db.insert(CUSTCHASHTABLE, null, contentValues);

        return result != -1; // if result = -1 data doesn't insert.
    }

    // Method To Insert Data.
    public boolean insertAllInfo(String custName, String custPhone, String custCollege, int custHour) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTNAME, custName);
        contentValues.put(CUSTPHONE, custPhone);
        contentValues.put(CUSTCOLLEGE, custCollege);
        contentValues.put(CUSTHOURS, custHour);

        long result = db.insert(CUSTINFOTABLE, null, contentValues);

        return result != -1; // if result = -1 data doesn't insert.
    }

    // Method To Insert Data.
    public boolean insertManagerNote(String managerNote) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(MANAGERNOTE, managerNote);

        long result = db.insert(MANAGERTABLE, null, contentValues);

        return result != -1; // if result = -1 data doesn't insert.
    }



    // Method To View Data.
    public Cursor viewCash() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM \"" + CUSTCHASHTABLE + "\";";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    // Method To View Data.
    public Cursor viewAllInfo() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + CUSTNAME + "," + CUSTPHONE + "," + CUSTCOLLEGE + "," + CUSTHOURS
                + " FROM " + CUSTINFOTABLE + " ORDER BY " + CUSTHOURS + " DESC;";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    // Method To View Data.
    public Cursor viewManagerNote() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + MANAGERTABLE + " ORDER BY " + MANAGERNOTE + " DESC;";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
