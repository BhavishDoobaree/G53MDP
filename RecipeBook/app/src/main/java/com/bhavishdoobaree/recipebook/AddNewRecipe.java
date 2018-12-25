package com.bhavishdoobaree.recipebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewRecipe extends AppCompatActivity {

    private EditText newrecipename;
    private EditText newrecipedetails;
    private TextView recipefeedback;

    private Button savenew;
    private Button cancelnew;

    //initialise on create

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_recipe);

        newrecipename = (EditText)findViewById(R.id.newRecipeName);
        newrecipedetails = (EditText)findViewById(R.id.newRecipeDetails);
        recipefeedback = (TextView)findViewById(R.id.recipeFeedback);

        savenew = (Button)findViewById(R.id.savenewrecipe);
        cancelnew = (Button)findViewById(R.id.cancelnewrecipe);
    }

    //add new recipe using dbhandler and clearing text box after save

    public void newRecipe(View view){
        RecipeDBHandler dbHandler = new RecipeDBHandler(this, null,null,1);
        String newrep = newrecipename.getText().toString();
        String newrepdet = newrecipedetails.getText().toString();

        Recipe recipe = new Recipe(newrep, newrepdet);
        dbHandler.addRecipe(recipe);
        newrecipename.setText("");
        newrecipedetails.setText("");
        //save feedback
        recipefeedback.setText("Recipe Saved");
    }

    public void cancelRecipe(View view)
    {
        finish();
    }
}
