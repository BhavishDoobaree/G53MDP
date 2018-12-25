package com.bhavishdoobaree.asyncdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AsyncDemoActivity extends AppCompatActivity {

    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_demo);

        myTextView = (TextView)findViewById(R.id.myTextView);
    }

    public void buttonClick(View view)
    {
        AsyncTask task = new MyTask().execute();
    }


    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            int i=0;
            while (i<=20)
            {
                publishProgress(i);
                try
                {
                    Thread.sleep(1000);
                    publishProgress(i);
                    i++;
                }
                catch (Exception e){
                    return (e.getLocalizedMessage());

                }
            }
            return "Button Pressed";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            myTextView.setText("Counter = " + values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            myTextView.setText(result);
        }
    }
}
