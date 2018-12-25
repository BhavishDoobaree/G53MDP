package com.example.explicitintents;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ActivityA extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
    }


    public void onClick(View view) {
        Intent i = new Intent(this, ActivityB.class);
        final EditText editText1 = (EditText)
                findViewById(R.id.editText1);
        String myString = editText1.getText().toString();
        i.putExtra("qString", myString);
        startActivityForResult(i, 5);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == 5)  && (resultCode == RESULT_OK))
        {
            TextView textView1 = (TextView) findViewById(R.id.textView1);

            String returnString = data.getExtras().getString("returnData");

            textView1.setText(returnString);

        }
    }
}
