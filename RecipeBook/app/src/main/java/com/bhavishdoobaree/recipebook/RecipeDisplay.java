package com.bhavishdoobaree.recipebook;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class RecipeDisplay extends AppCompatActivity {

    int id;
    TextView recipeName, recipeDetails;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);

        recipeName = findViewById(R.id.nameDisplayView);
        recipeDetails = findViewById(R.id.detailsDisplayView);

        deleteButton = findViewById(R.id.deleteRecipe);

        id = getIntent().getExtras().getInt("id");

        displayRecipe();

    }

    //display on resume

    @Override
    protected void onResume()
    {
        displayRecipe();
        super.onResume();
    }

    //uses ID of recipe and display on new activity

    protected void displayRecipe()
    {
        RecipeDBHandler dbHandler = new RecipeDBHandler(this, null, null, 1);

        Recipe r = dbHandler.findRecipe(id);
        String name = r.get_rname();
        String text = r.get_rdetails();

        recipeName.setText(name);
        recipeDetails.setText(text);


        deleteButton.setOnClickListener(view -> {
            //display alert dialog to confirm delete action
            AlertDialog.Builder alert = new AlertDialog.Builder(RecipeDisplay.this);

            alert
                    .setTitle("Are you sure?")
                    .setMessage("Action will delete recipe")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        dbHandler.deleteRecipe(id);
                        Toast.makeText(getApplicationContext(),"Recipe deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        });
    }


}
