package com.bhavishdoobaree.recipebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RecipeSearch extends AppCompatActivity {

    private EditText recipesearch;

    private TextView displayID;
    private TextView rnamedisplay;
    private TextView rdetailsdisplay;

    private Button searchbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_search);

        recipesearch = (EditText)findViewById(R.id.namesearch);
        displayID = (TextView)findViewById(R.id.riddisplay);
        rnamedisplay = (TextView)findViewById(R.id.rnameDisplay);
        rdetailsdisplay = (TextView)findViewById(R.id.rdetailsDisplay);
        searchbutton = (Button)findViewById(R.id.searchFunc);

    }

    //simple search through database and display on textview

    public void searchRecipe(View view)
    {
        RecipeDBHandler dbHandler = new RecipeDBHandler(this,null,null,1);
        Recipe recipe = dbHandler.findRecipe(recipesearch.getText().toString());

        if (recipe != null)
        {
            displayID.setText(String.valueOf(recipe.get_rcpid()));
            rnamedisplay.setText(String.valueOf(recipe.get_rname()));
            rdetailsdisplay.setText(String.valueOf(recipe.get_rdetails()));
        }else
        {
            displayID.setText("No match found");
            rnamedisplay.setText("No recipe name");
            rdetailsdisplay.setText("No details found");
            recipesearch.setText("");
        }

    }


}
