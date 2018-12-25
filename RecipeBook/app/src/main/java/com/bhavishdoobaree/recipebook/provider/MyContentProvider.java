package com.bhavishdoobaree.recipebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.bhavishdoobaree.recipebook.RecipeDBHandler;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.bhavishdoobaree.recipebook.provider.MyContentProvider";
    private static final String RECIPE_TABLE = "recipes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECIPE_TABLE);

    public static final int RECIPES = 1;
    public static final int RECIPES_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sURIMatcher.addURI(AUTHORITY, RECIPE_TABLE, RECIPES);
        sURIMatcher.addURI(AUTHORITY, RECIPE_TABLE + "/#",RECIPES_ID);
    }

    private RecipeDBHandler myDB;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType)
        {
            case RECIPES:
                rowsDeleted = sqlDB.delete(RecipeDBHandler.TABLE_RECIPE, selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    rowsDeleted = sqlDB.delete(RecipeDBHandler.TABLE_RECIPE, RecipeDBHandler.COLUMN_ID + "=" + id, null);

                }else {
                    rowsDeleted = sqlDB.delete(RecipeDBHandler.TABLE_RECIPE, RecipeDBHandler.COLUMN_ID + "=" + id + " and " +selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        long id = 0;
        switch (uriType){
            case RECIPES:
                id = sqlDB.insert(RecipeDBHandler.TABLE_RECIPE,null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(RECIPE_TABLE +"/" +id);
    }

    @Override
    public boolean onCreate() {
        myDB = new RecipeDBHandler(getContext(),null,null,1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(RecipeDBHandler.TABLE_RECIPE);
        int uriType = sURIMatcher.match(uri);
        switch (uriType)
        {
            case RECIPES_ID:
                queryBuilder.appendWhere(RecipeDBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case RECIPES:
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
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType)
        {
            case RECIPES:
                rowsUpdated = sqlDB.update(RecipeDBHandler.TABLE_RECIPE, values, selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = sqlDB.update(RecipeDBHandler.TABLE_RECIPE, values, RecipeDBHandler.COLUMN_ID + "=" + id, null);

                }else
                {
                    rowsUpdated = sqlDB.update(RecipeDBHandler.TABLE_RECIPE, values, RecipeDBHandler.COLUMN_ID + "=" + id + " and " + selection , selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
