package com.bhavishdoobaree.exercisetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewPastListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_historylist);
        Context context = this;

        ///define tablelayout and DBhandler
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        myDBHandler dbHandler = new myDBHandler(this, null, null, 1);

        //define and add rowheaders to table
        TableRow headerRow = new TableRow(context);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headers = {"Date", "Total Distance", "Total Time"};

        //additional parameters
        for (String c : headers) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            tv.setPadding(5, 10, 5, 10);
            tv.setText(c);
            headerRow.addView(tv);
        }
        tableLayout.addView(headerRow);

        //Start SQLite and start transaction
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        db.beginTransaction();

        try {
            //finding log for best time
            String bestTime = "SELECT time FROM " + myDBHandler.TABLE_DBLOGS + " ORDER BY time ASC LIMIT 1";
            Cursor c = db.rawQuery(bestTime, null);

            //acquire all logs
            String selectQuery = "SELECT * FROM " + myDBHandler.TABLE_DBLOGS;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                c.moveToNext();
                long bestT = c.getLong(c.getColumnIndex("time"));
                while (cursor.moveToNext()) {

                    String runTimeStamp = cursor.getString(cursor.getColumnIndex("timeStamp"));
                    String runDistance = cursor.getString(cursor.getColumnIndex("distance"));
                    long runTime = cursor.getLong(cursor.getColumnIndex("time"));

                    //format time for UI
                    long Seconds = (int) (runTime / 1000);
                    long Minutes = Seconds / 60;
                    Seconds = Seconds % 60;
                    long MilliSeconds = (int) (runTime % 1000);
                    String runTimeDisp = "" + Minutes + ":" + String.format("%02d", Seconds) + ":" + String.format("%03d", MilliSeconds);

                    //define table rows
                    TableRow row = new TableRow(context);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    //highlights the best time yellow
                    if (runTime == bestT) {
                        row.setBackgroundColor(Color.parseColor("#FFFF00"));
                    }

                    String[] colText = {runTimeStamp + "", runDistance, runTimeDisp};
                    for (String text : colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                }
            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();

        }
    }

}
