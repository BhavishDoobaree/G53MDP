package com.bhavishdoobaree.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button addnew;
    private Button browserecipes;
    private Button searchrecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addnew = (Button)findViewById(R.id.addrecipe);
        browserecipes = (Button)findViewById(R.id.browserecipe);
        searchrecipe = (Button)findViewById(R.id.searchrecipe);

    }

    //following methods just give functions to buttons and launches new activities

    public void addNew(View view){

        Intent intent = new Intent(MainActivity.this, AddNewRecipe.class);
        startActivity(intent);

    }

    public void browseRecipe(View view)
    {

        Intent intent = new Intent(MainActivity.this, BrowseExisting.class);
        startActivity(intent);

    }

    public void searchRecipe(View view){

        Intent intent = new Intent(MainActivity.this, RecipeSearch.class);
        startActivity(intent);

    }


}
