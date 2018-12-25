package com.bhavishdoobaree.recipebook;

import com.bhavishdoobaree.recipebook.provider.MyContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentResolver;

import java.util.ArrayList;
import java.util.List;


public class RecipeDBHandler extends SQLiteOpenHelper {

    private ContentResolver myCR;

    //variables and constructors

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipeDB.db";
    public static final String TABLE_RECIPE = "recipes";

    public static final String COLUMN_ID = "_rcpid";
    public static final String COLUMN_RN = "rname";
    public static final String COLUMN_RD = "rdetails";

    public RecipeDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECIPES_TABLE = "CREATE TABLE " +
                TABLE_RECIPE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RN
                + " TEXT," + COLUMN_RD + " TEXT" + ")";
        db.execSQL(CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        onCreate(db);

    }

    public void addRecipe(Recipe recipe)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RN, recipe.get_rname());
        values.put(COLUMN_RD, recipe.get_rdetails());
        myCR.insert(MyContentProvider.CONTENT_URI, values);
    }

    public void updateRecipe(Recipe recipe, int id)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RN, recipe.get_rname());
        values.put(COLUMN_RD, recipe.get_rdetails());
        myCR.update(MyContentProvider.CONTENT_URI, values, COLUMN_ID + " = " + Integer.toString(id), null);
    }

    public Recipe findRecipe(String recipename)
    {
//        String query = "SELECT * FROM " + TABLE_RECIPE + " WHERE " +
//                COLUMN_RN + " = \"" + recipename + "\"";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        Recipe recipe = new Recipe();
//        if (cursor.moveToFirst()){
//            cursor.moveToFirst();
//            recipe.set_rcpid(Integer.parseInt(cursor.getString(0)));
//            recipe.set_rname(cursor.getString(1));
//            recipe.set_rdetails(cursor.getString(2));
//            cursor.close();
//        }else{
//            recipe = null;
//        }
//        db.close();
//        return recipe;

        String[] projection = {COLUMN_ID,
                COLUMN_RN, COLUMN_RD };
        String selection = COLUMN_RN + " = \"" + recipename + "\"";
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI,
                projection, selection, null,
                null);
        Recipe recipe = new Recipe();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipe.set_rcpid(Integer.parseInt(cursor.getString(0)));
            recipe.set_rname(cursor.getString(1));
            recipe.set_rdetails(cursor.getString(2));
            cursor.close();
        } else {
            recipe = null;
        }
        return recipe;


    }

    public Recipe findRecipe(int recipeID){
        String[] projection = {COLUMN_ID, COLUMN_RN, COLUMN_RD};
        String selection = COLUMN_ID + " = " + Integer.toString(recipeID);
        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI, projection, selection, null, null);
        Recipe recipe = new Recipe();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            recipe.set_rcpid(Integer.parseInt(cursor.getString(0)));
            recipe.set_rname(cursor.getString(1));
            recipe.set_rdetails(cursor.getString(2));
            cursor.close();
        } else {
            recipe = null;
        }
        return recipe;
    }

    //delete recipe by name
    public boolean deleteRecipe(String recipename)
    {
//        boolean result = false;
//        String query = "SELECT * FROM " + TABLE_RECIPE + " WHERE " +
//                COLUMN_RN + " = \"" +recipename + "\"";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        Recipe recipe = new Recipe();
//        if (cursor.moveToFirst()){
//            recipe.set_rcpid(Integer.parseInt(cursor.getString(0)));
//            db.delete(TABLE_RECIPE, COLUMN_ID + " = ?", new String[] { String.valueOf(recipe.get_rcpid())});
//            cursor.close();
//            result= true;
//        }
//        db.close();
//        return result;

        boolean result = false;
        String selection = "recipename = \"" + recipename + "\"";
        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0)
            result = true;
        return result;

    }

    //delete recipe by ID

    public boolean deleteRecipe(int recipeID){
        boolean result = false;
        String selection = COLUMN_ID + " = " + Integer.toString(recipeID);
        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0)
            result = true;
        return result;
    }

    //list all recipes for list display on BrowseExisting

    public List<Recipe> listAllRecipes(){
        List<Recipe> recipeList = new ArrayList<>();

        String[] projection = {COLUMN_ID, COLUMN_RN, COLUMN_RD};
        String selection = null;
        Cursor c = myCR.query(MyContentProvider.CONTENT_URI, projection, selection, null, null);

        int id;
        String name, text;

        if(c != null){
            if(c.moveToFirst()){
                do {
                    id = c.getInt(c.getColumnIndex(COLUMN_ID));
                    name = c.getString(c.getColumnIndex(COLUMN_RN));
                    text = c.getString(c.getColumnIndex(COLUMN_RD));

                    Recipe r = new Recipe(id, name, text);
                    recipeList.add(r);

                } while (c.moveToNext());
            }
        }

        return recipeList;
    }

}
