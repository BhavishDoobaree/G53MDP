package com.bhavishdoobaree.recipebook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class BrowseExisting extends AppCompatActivity {

    private Button searchbutton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_existing);

        searchbutton = (Button)findViewById(R.id.rsearch);

        populateList();
        
    }

    //populate changes on resume activity

    @Override
    protected void onResume()
    {
        populateList();
        super.onResume();
    }

    //launches search activity

    public void searchButton(View view)
    {
        Intent intent = new Intent(BrowseExisting.this, RecipeSearch.class);
        startActivity(intent);
    }

    protected void populateList(){
        Log.d("recipeBook", "Updating listView");

        listView = findViewById(R.id.recipesList);

        //database handler
        RecipeDBHandler dbHandler = new RecipeDBHandler(getApplicationContext(), null, null, 1);

        //retrieve list of recipes from database
        List<Recipe> recipeList = dbHandler.listAllRecipes();

        //sort recipe list in alphabetical order
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recipeList.sort(Recipe.RecipeNameComparator);
        }

        //adapter to associate with listview
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipeList);

        listView.setAdapter(adapter);

        //when user selects a recipe in the list
        listView.setOnItemClickListener((adapterView, view, pos, l) -> {

            Recipe selectedRecipe = (Recipe) listView.getItemAtPosition(pos);

            //launch RecipeDisplay to view recipe
            Intent i = new Intent(getApplicationContext(), RecipeDisplay.class);
            i.putExtra("id", selectedRecipe.get_rcpid());
            startActivity(i);
        });
    }


}
