package com.example.zkirtaem.licencjat;


import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by zkirtaem on 01.11.15.
 */

public class mySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "licencjatdb.db";
    private static final int DATABASE_VERSION = 1;




    private static final String ZAJECIA_CREATE = "create table zajecia (" +
            "id integer primary key autoincrement, " +
            "nazwa text not null)";

    private static final String STUDENCI_CREATE = "create table studenci (" +
            "id integer primary key autoincrement, " +
            "imie_i_nazwisko text not null, " +
            "rfid text, " +
            "indeks integer)";


    private static final String ODBYTE_ZAJECIA_CREATE = "create table odbyte_zajecia (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "zajecia_id INTEGER, " +
            "dataigodzina TIMESTAMP NOT NULL DEFAULT current_timestamp)";


    private static final String OBECNOSCI_CREATE = "create table obecnosci (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "odbyte_zajecia_id INTEGER, " +
            "student_id INTEGER, " +
            "dataigodzina TIMESTAMP NOT NULL DEFAULT current_timestamp)";



    public mySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(ZAJECIA_CREATE);
        database.execSQL(ODBYTE_ZAJECIA_CREATE);
        database.execSQL(STUDENCI_CREATE);
        database.execSQL(OBECNOSCI_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(mySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS zajecia");
        db.execSQL("DROP TABLE IF EXISTS odbyte_zajecia");
        db.execSQL("DROP TABLE IF EXISTS studenci");
        db.execSQL("DROP TABLE IF EXISTS obecnosci");
        onCreate(db);
    }

}