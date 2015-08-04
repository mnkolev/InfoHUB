package com.example.mkgho_000.infohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleSessionActivity  extends Activity {

    // JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_START = "start_datetime";
    private static final String TAG_END = "end_datetime";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_session);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String start = in.getStringExtra(TAG_START);
        String end = in.getStringExtra(TAG_END);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblStart = (TextView) findViewById(R.id.start_label);
        TextView lblEnd = (TextView) findViewById(R.id.end_label);

        lblName.setText(name);
        lblStart.setText(start);
        lblEnd.setText(end);
    }
}
