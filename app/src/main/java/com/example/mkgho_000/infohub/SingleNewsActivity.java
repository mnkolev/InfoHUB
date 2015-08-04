package com.example.mkgho_000.infohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleNewsActivity  extends Activity {

    // JSON node keys
    private static final String TAG_HEADLINE = "headline";
    private static final String TAG_STORY = "story";
    private static final String TAG_DATE = "date";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String headline = in.getStringExtra(TAG_HEADLINE);
        String story = in.getStringExtra(TAG_STORY);
        String date = in.getStringExtra(TAG_DATE);

        // Displaying all values on the screen
        TextView lblHeadline = (TextView) findViewById(R.id.headline_label);
        TextView lblStory = (TextView) findViewById(R.id.story_label);
        TextView lblDate = (TextView) findViewById(R.id.date_label);

        lblHeadline.setText(headline);
        lblStory.setText(story);
        lblDate.setText(date);
    }
}
