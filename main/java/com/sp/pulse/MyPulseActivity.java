package com.sp.pulse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyPulseActivity extends AppCompatActivity {
    private String userID = "";
    private WebView healthPulseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_health_pulse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        healthPulseView = (WebView)findViewById(R.id.health_pulse_view);
        healthPulseView.setWebViewClient(new MyBrowser());
    }

    @Override
    protected void onResume() {
        userID = "S12345A";

        healthPulseView.getSettings().setLoadsImagesAutomatically(true);
        healthPulseView.getSettings().setJavaScriptEnabled(true);
        healthPulseView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        healthPulseView.loadUrl(Config.HEALTH_PULSE_URL + userID);

        super.onResume();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
