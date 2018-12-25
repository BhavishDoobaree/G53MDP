package com.bhavishdoobaree.exercisetracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bhavishdoobaree.exercisetracker.provider.MyContentProvider;

public class myDBHandler extends SQLiteOpenHelper {

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "exerciseLogsDB.db";
    public static final String TABLE_DBLOGS = "exerciseLogs";

    public static final String COLUMN_TIMESTAMP = "timeStamp";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_TIMERAN = "time";

    public myDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ExerciseTrackerDB", "onCreate");
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_DBLOGS + "(" + COLUMN_TIMESTAMP +
                " TEXT," + COLUMN_DISTANCE + " TEXT," + COLUMN_TIMERAN + " REAL" + ")";
        db.execSQL(CREATE_EXERCISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_DBLOGS);
        onCreate(db);

    }

    public void addLogRecord(exeLog ExeLog){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, ExeLog.getTimestamp());
        values.put(COLUMN_DISTANCE, ExeLog.getDistance());
        values.put(COLUMN_TIMERAN, ExeLog.getTime());
        myCR.insert(MyContentProvider.CONTENT_URI, values);
    }


}
