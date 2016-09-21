package com.sp.pulse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySettingActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> gameCategoriesList;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GAME_CATEGORIES = "game_categories";
    private static final String TAG_GAME_CATEGORY_ID = "game_category_ID";
    private static final String TAG_GAME_CATEGORY = "game_category";

    // game categories JSONArray
    JSONArray gameCategories = null;

    private EditText myID = null;
    private RadioGroup favouriteGame = null;
    private RadioButton[] rb = null;
    private LinearLayout buddyGroupLayout = null;
    private CheckBox[] cb = null;
    private Button saveSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_setting);

        myID = (EditText) findViewById(R.id.my_ID);
        favouriteGame = (RadioGroup) findViewById(R.id.favourite_game);
        buddyGroupLayout = (LinearLayout) findViewById(R.id.buddy_group_layout);
        saveSetting = (Button) findViewById(R.id.save_settings);
        saveSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String myIDStr = myID.getText().toString();

                int gameCategorySize = gameCategoriesList.size();
                String gameCategorySelected = "";
                String myFavouriteGame = "";
                boolean firstOne = true;
                for (int i = 0; i < gameCategorySize; i++) {
                    if (rb[i].isChecked()) {
                        myFavouriteGame = rb[i].getText().toString();
                    }

                    if (cb[i].isChecked()) {
                        if (firstOne) {
                            firstOne = false;
                            gameCategorySelected = cb[i].getText().toString();
                        } else {
                            gameCategorySelected += ", " + cb[i].getText().toString();
                        }
                    }
                }
                //Toast.makeText(MySettingActivity.this, myIDStr + " " + myFavouriteGame + " " + gameCategorySelected, Toast.LENGTH_LONG).show();
                OkHttp handler = new OkHttp(myIDStr, myFavouriteGame, gameCategorySelected);
                String result = null;
                try {
                    result = handler.execute(Config.URL_MY_SETTINGS).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        // Hashmap for ListView
        gameCategoriesList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllGameCategories().execute();
    }

    private class OkHttp extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();
        String userID, favouriteGame, categorySelected;

        public OkHttp(String userID, String favouriteGame, String categorySelected) {
            this.userID = userID;
            this.favouriteGame = favouriteGame;
            this.categorySelected = categorySelected;
        }

        @Override
        protected String doInBackground(String... params) {

            RequestBody body = new FormBody.Builder()
                    .add("my_ID", userID)
                    .add("favorite_game", favouriteGame)
                    .add("buddy_group_selected", categorySelected)
                    .build();
            Request request = new Request.Builder()
                    .url(params[0]).post(body).build();
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code" + response.toString());
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
     * Background Async Task to Load all game category by making HTTP Request
     */
    class LoadAllGameCategories extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MySettingActivity.this);
            pDialog.setMessage("Loading game categories. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All game categories from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Config.URL_ALL_GAME_CATEGORIES, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Game Categories: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    gameCategories = json.getJSONArray(TAG_GAME_CATEGORIES);

                    // looping through All Products
                    for (int i = 0; i < gameCategories.length(); i++) {
                        JSONObject c = gameCategories.getJSONObject(i);

                        // Storing each json item in variable
                        String gameCategoryID = c.getString(TAG_GAME_CATEGORY_ID);
                        String gameCategory = c.getString(TAG_GAME_CATEGORY);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_GAME_CATEGORY_ID, gameCategoryID);
                        map.put(TAG_GAME_CATEGORY, gameCategory.toUpperCase());

                        // adding HashList to ArrayList
                        gameCategoriesList.add(map);
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
                    if (gameCategoriesList.size() > 0) {
                        int gameListSize = gameCategoriesList.size();
                        rb = new RadioButton[gameListSize];
                        cb = new CheckBox[gameListSize];
                        for (int i = 0; i < gameListSize; i++) {
                            //Add RadioButton dynamically according to th Game Category from server
                            rb[i] = new RadioButton(MySettingActivity.this);
                            favouriteGame.addView(rb[i]);
                            rb[i].setText(gameCategoriesList.get(i).get("game_category"));

                            //Add CheckBox dynamically according to th Game Category from server
                            cb[i] = new CheckBox(MySettingActivity.this);
                            cb[i].setText(gameCategoriesList.get(i).get("game_category"));
                            buddyGroupLayout.addView(cb[i]);
                        }
                    }

                }
            });
        }
    }
}
