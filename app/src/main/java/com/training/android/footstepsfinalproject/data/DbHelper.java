package com.training.android.footstepsfinalproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mwszedybyl on 5/3/15.
 */
public class DbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "footsteps.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WALK_TABLE = "CREATE TABLE " + FootstepsContract.WalkEntry.TABLE_NAME + " (" +
                FootstepsContract.WalkEntry._ID + " INTEGER PRIMARY KEY," +
                FootstepsContract.WalkEntry.COLUMN_STARTING_LAT + " INTEGER NOT NULL, " +
                FootstepsContract.WalkEntry.COLUMN_STARTING_LONG + " INTEGER NOT NULL, " +
                FootstepsContract.WalkEntry.COLUMN_ENDING_LAT + " INTEGER NOT NULL, " +
                FootstepsContract.WalkEntry.COLUMN_ENDING_LONG + " INTEGER NOT NULL, " +
                FootstepsContract.WalkEntry.COLUMN_DISTANCE + " INTEGER NOT NULL, " +
                FootstepsContract.WalkEntry.COLUMN_TEMP + " TEXT" +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_WALK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FootstepsContract.WalkEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
