package com.example.mkgho_000.infohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SinglePresenterActivity  extends Activity {

    // JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_THUMB = "thumb";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_presenter);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String title = in.getStringExtra(TAG_TITLE);
        String company = in.getStringExtra(TAG_COMPANY);
        String thumb = in.getStringExtra(TAG_THUMB);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        TextView lblCompany = (TextView) findViewById(R.id.company_label);
        TextView lblThumb = (TextView) findViewById(R.id.thumb_label);

        lblName.setText(name);
        lblTitle.setText(title);
        lblCompany.setText(company);
        lblThumb.setText(thumb);
    }
}
