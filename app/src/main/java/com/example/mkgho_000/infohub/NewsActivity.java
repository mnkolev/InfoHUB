package com.example.mkgho_000.infohub;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NewsActivity extends ListActivity {

    private ProgressDialog pDialog;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    // URL to get contacts JSON
    private static String url = "http://private-ee02a-wsginfohub.apiary-mock.com/api/news";

    // JSON Node names
    private static final String TAG_NEWS = "news";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADLINE = "headline";
    private static final String TAG_STORY = "story";
    private static final String TAG_DATE = "date";
    private static final String TAG_THUMB = "thumb"; //Not used, remove


    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenters);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String headline = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String story = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String date = ((TextView) view.findViewById(R.id.company))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SinglePresenterActivity.class);
                in.putExtra(TAG_HEADLINE, headline);
                in.putExtra(TAG_STORY, story);
                in.putExtra(TAG_DATE, date);
                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetContacts().execute();

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_NEWS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_HEADLINE);
                        String title = c.getString(TAG_STORY);
                        String company = c.getString(TAG_DATE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_HEADLINE, name);
                        contact.put(TAG_STORY, title);
                        contact.put(TAG_DATE, company);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    NewsActivity.this, contactList,
                    R.layout.list_item, new String[]{TAG_HEADLINE, TAG_STORY,
                    TAG_DATE}, new int[]{R.id.name,
                    R.id.title, R.id.company});

            setListAdapter(adapter);
        }

    }
}

