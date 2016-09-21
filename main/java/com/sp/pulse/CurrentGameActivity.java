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

public class CurrentGameActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> gamesList;

    // url to get all products list
    //private static String url_all_products = "http://api.androidhive.info/android_connect/get_all_products.php";
    //private static String url_all_games = "http://10.0.2.2/SportPulse/get_current_game_all.php";
    //private static String url_all_icons = "http://10.0.2.2/SportPulse/game_images/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GAMES = "games";
    private static final String TAG_GAME_ID = "game_ID";
    private static final String TAG_GAME_NAME = "game_name";
    private static final String TAG_GAME_CATEGORY ="game_category";
    private static final String TAG_GAME_CATEGORY_ICON ="game_category_icon";
    private static final String TAG_GAME_DESCRIPTION = "game_description";
    private static final String TAG_GAME_DATE = "game_date";
    private static final String TAG_GAME_TIME = "game_time";
    private static final String TAG_GAME_VENUE = "game_venue";
    private static final String TAG_TICKET_STATUS = "ticket_status";
    private static final String TAG_TICKET_PRICE = "ticket_price";
    private static final String TAG_HEALTH_PULSE_DISCOUNT = "health_pulse_discount";

    private ListView listView = null;
    private ListAdapter adapter = null;

    // products JSONArray
    JSONArray games = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_game);

        listView = (ListView)findViewById(R.id.listView);

        // Hashmap for ListView
        gamesList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllGames().execute();

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
    class LoadAllGames extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CurrentGameActivity.this);
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
            JSONObject json = jParser.makeHttpRequest(Config.URL_ALL_GAMES, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Games: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    games = json.getJSONArray(TAG_GAMES);

                    // looping through All Products
                    for (int i = 0; i < games.length(); i++) {
                        JSONObject c = games.getJSONObject(i);

                        // Storing each json item in variable
                        String gameID = c.getString(TAG_GAME_ID);
                        String gameCategory = c.getString(TAG_GAME_CATEGORY);
                        String gameName = c.getString(TAG_GAME_NAME);
                        String gameDescription = c.getString(TAG_GAME_DESCRIPTION);
                        String gameDate = c.getString(TAG_GAME_DATE);
                        String gameTime = c.getString(TAG_GAME_TIME);
                        String gameVenue = c.getString(TAG_GAME_VENUE);
                        String ticketStatus = c.getString(TAG_TICKET_STATUS);
                        String ticketPrice = c.getString(TAG_TICKET_PRICE);
                        String healthPulseDiscount = c.getString(TAG_HEALTH_PULSE_DISCOUNT);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_GAME_ID, gameID);
                        map.put(TAG_GAME_CATEGORY, gameCategory.toUpperCase());
                        map.put(TAG_GAME_CATEGORY_ICON, Config.URL_ALL_GAMES_ICONS + gameCategory.toLowerCase() + ".png");
                        map.put(TAG_GAME_NAME, gameName);
                        map.put(TAG_GAME_DESCRIPTION, gameDescription);
                        map.put(TAG_GAME_VENUE, gameVenue);
                        map.put(TAG_GAME_DATE, gameDate);
                        map.put(TAG_GAME_TIME, gameTime);
                        map.put(TAG_TICKET_STATUS, ticketStatus);
                        map.put(TAG_TICKET_PRICE, ticketPrice);
                        map.put(TAG_HEALTH_PULSE_DISCOUNT, healthPulseDiscount);

                        // adding HashList to ArrayList
                        gamesList.add(map);
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
                    adapter = new GameAdapter(CurrentGameActivity.this, gamesList);
                    // updating listview
                    listView.setAdapter(adapter);
                }
            });
        }
    }
}
