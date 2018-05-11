package com.example.richardantonios.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final TextView titletxt = findViewById(R.id.titletxt);
        final TextView idtxt = findViewById(R.id.idtxt);
        final TextView useridtxt = findViewById(R.id.useridtxt);
        final TextView completedtxt = findViewById(R.id.completedtxt);

        // get values from intent
        Intent getIntent = getIntent();
        String title = getIntent.getStringExtra("title");
        String id = getIntent.getIntExtra("id", 0) + "";
        String userid = getIntent.getIntExtra("userid", 0) + "";
        String completed = getIntent.getBooleanExtra("completed", false) + "";

        // set textviews text
        titletxt.setText(title);
        idtxt.setText(id);
        useridtxt.setText(userid);
        completedtxt.setText(completed);
    }
}
