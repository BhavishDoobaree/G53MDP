package com.bhavishdoobaree.exercisetracker.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.bhavishdoobaree.exercisetracker.myDBHandler;

public class MyContentProvider extends ContentProvider {
    public MyContentProvider() {
    }

    public static final int EXELOGS = 1;
    private static final String AUTHORITY = "com.bhavishdoobaree.exercisetracker.provider.MyContentProvider";
    private static final String EXELOGS_TABLE = "exeLogs";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +EXELOGS_TABLE);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, EXELOGS_TABLE, EXELOGS);
    }

    private myDBHandler myDB;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case EXELOGS:
                id = sqlDB.insert(myDBHandler.TABLE_DBLOGS, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(EXELOGS_TABLE + "/" + id);

    }

    @Override
    public boolean onCreate() {
        myDB = new myDBHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(myDBHandler.TABLE_DBLOGS);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case EXELOGS:
                queryBuilder.appendWhere(myDBHandler.COLUMN_TIMESTAMP + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
