package com.sp.pulse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthProgrammeActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> programsList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROGRAMS = "programs";
    private static final String TAG_PROGRAM_ID = "health_program_ID";
    private static final String TAG_PROGRAM_CATEGORY ="program_category";
    private static final String TAG_PROGRAM_CATEGORY_ICON ="program_category_icon";
    private static final String TAG_PROGRAM_NAME = "program_name";
    private static final String TAG_PROGRAM_DESCRIPTION = "program_description";
    private static final String TAG_PROGRAM_VENUE = "program_venue";
    private static final String TAG_PROGRAM_AGE_GROUP = "age_group";
    private static final String TAG_PROGRAM_START_DATE = "start_date";
    private static final String TAG_PROGRAM_END_DATE = "end_date";
    private static final String TAG_PROGRAM_START_TIME = "start_time";
    private static final String TAG_PROGRAM_END_TIME = "end_time";
    private static final String TAG_HEALTH_PULSE_REWARD = "health_pulse_reward";
    private static final String TAG_PROGRAM_REMARKS = "remarks";

    private ListView listView = null;
    private ListAdapter adapter = null;

    // products JSONArray
    JSONArray programs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_programme);
        listView = (ListView)findViewById(R.id.programListView);

        // Hashmap for ListView
        programsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllPrograms().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     */
    class LoadAllPrograms extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HealthProgrammeActivity.this);
            pDialog.setMessage("Loading games. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.URL_ALL_PROGRAMS, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Programs: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    programs = json.getJSONArray(TAG_PROGRAMS);

                    // looping through All Products
                    for (int i = 0; i < programs.length(); i++) {
                        JSONObject c = programs.getJSONObject(i);

                        // Storing each json item in variable
                        String programID = c.getString(TAG_PROGRAM_ID);
                        String programCategory = c.getString(TAG_PROGRAM_CATEGORY);
                        String programName = c.getString(TAG_PROGRAM_NAME);
                        String programAgeGroup = c.getString(TAG_PROGRAM_AGE_GROUP);
                        String programDescription = c.getString(TAG_PROGRAM_DESCRIPTION);
                        String programStartDate = c.getString(TAG_PROGRAM_START_DATE);
                        String programEndDate = c.getString(TAG_PROGRAM_END_DATE);
                        String programStartTime = c.getString(TAG_PROGRAM_START_TIME);
                        String programEndTime = c.getString(TAG_PROGRAM_END_TIME);
                        String programVenue = c.getString(TAG_PROGRAM_VENUE);
                        String healthPulseReward = c.getString(TAG_HEALTH_PULSE_REWARD);
                        String programRemarks = c.getString(TAG_PROGRAM_REMARKS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PROGRAM_ID, programID);
                        map.put(TAG_PROGRAM_CATEGORY, programCategory.toUpperCase());
                        map.put(TAG_PROGRAM_CATEGORY_ICON, Config.URL_ALL_PROGRAMS_ICONS + programCategory.toLowerCase() + ".png");
                        map.put(TAG_PROGRAM_NAME, programName);
                        map.put(TAG_PROGRAM_DESCRIPTION, programDescription);
                        map.put(TAG_PROGRAM_AGE_GROUP, programAgeGroup);
                        map.put(TAG_PROGRAM_VENUE, programVenue);
                        map.put(TAG_PROGRAM_START_DATE, programStartDate);
                        map.put(TAG_PROGRAM_END_DATE, programEndDate);
                        map.put(TAG_PROGRAM_START_TIME, programStartTime);
                        map.put(TAG_PROGRAM_END_TIME, programEndTime);
                        map.put(TAG_HEALTH_PULSE_REWARD, healthPulseReward);
                        map.put(TAG_PROGRAM_REMARKS, programRemarks);

                        // adding HashList to ArrayList
                        programsList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    adapter = new ProgrammeAdapter(HealthProgrammeActivity.this, programsList);
                    // updating listview
                    listView.setAdapter(adapter);
                }
            });
        }
    }

}
