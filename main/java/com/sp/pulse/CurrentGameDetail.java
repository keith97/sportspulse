package com.sp.pulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentGameDetail extends AppCompatActivity {
    // Declare Variables
    private String gameID;
    private String gameName;
    private String gameCategory;
    private String gameCategoryIcon;
    private String gameDescription;
    private String gameDate;
    private String gameTime;
    private String gameVenue;
    private String ticketStatus;
    private String ticketPrice;
    private String healthPulseDiscount;

    private String position;
    private ImageLoader imageLoader = new ImageLoader(this);

    private WebView photoView;
    private WebView videoView;
    //private String url = "http://172.21.8.255/WonderwallMobileGallery/";
    //private String url2 = "http://172.21.8.255/VideoGallery/";
    //private String photoURL = "http://www.sportpulse.tk/WonderwallMobileGallery/";
    //private String videoURL = "http://www.sportpulse.tk/VideoGallery/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.current_game_detail);

        Intent i = getIntent();
        gameID = i.getStringExtra("game_ID");
        gameName = i.getStringExtra("game_name");
        gameCategory = i.getStringExtra("game_category");
        gameCategoryIcon = i.getStringExtra("game_category_icon");
        gameDescription = i.getStringExtra("game_description");
        gameDate = i.getStringExtra("game_date");
        gameTime = i.getStringExtra("game_time");
        gameVenue = i.getStringExtra("game_venue");
        ticketStatus = i.getStringExtra("ticket_status");
        ticketPrice = i.getStringExtra("ticket_price");
        healthPulseDiscount = i.getStringExtra("health_pulse_discount");

        // Locate the TextViews in singleitemview.xml
        TextView gmCategory = (TextView) findViewById(R.id.game_category);
        TextView gmName = (TextView) findViewById(R.id.game_name);
        TextView gmDate = (TextView) findViewById(R.id.game_date);
        TextView gmVenue = (TextView) findViewById(R.id.game_venue);
        TextView gmTicketStatus = (TextView) findViewById(R.id.ticket_status);
        TextView gmTicketPrice = (TextView) findViewById(R.id.ticket_price);
        TextView gmHealthPulseDiscount = (TextView) findViewById(R.id.health_pulse_discount);

        // Locate the ImageView in singleitemview.xml
        ImageView imgGameCategory = (ImageView) findViewById(R.id.game_icon);

        // Set results to the TextViews
        gmCategory.setText(gameCategory + " - " + gameDescription);
        gmName.setText(gameName);
        gmDate.setText(gameDate + " " + gameTime);
        gmVenue.setText("Venue: " + gameVenue);
        gmTicketStatus.setText("Ticket: " + ticketStatus);
        gmTicketPrice.setText("Price: " + ticketPrice);
        gmHealthPulseDiscount.setText("Discount: " + healthPulseDiscount);
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(gameCategoryIcon, imgGameCategory);

        photoView = (WebView)findViewById(R.id.photo_view);
        photoView.setWebViewClient(new MyBrowser());

        videoView = (WebView)findViewById(R.id.video_view);
        videoView.setWebViewClient(new MyBrowser());
    }

    @Override
    protected void onResume() {
        photoView.getSettings().setLoadsImagesAutomatically(true);
        photoView.getSettings().setJavaScriptEnabled(true);
        photoView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        photoView.loadUrl(Config.SHARED_PHOTO_URL + gameID);

        videoView.getSettings().setLoadsImagesAutomatically(true);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        videoView.loadUrl(Config.SHARED_VIDEO_URL + gameID);

        super.onResume();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent shareIntent = new Intent(CurrentGameDetail.this, PhotoVideoShare.class);
            shareIntent.putExtra("GAME_ID", gameID);
            startActivity(shareIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
