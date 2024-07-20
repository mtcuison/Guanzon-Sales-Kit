package org.rmj.guanzon.guanzonsaleskit.Activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.rmj.guanzon.guanzonsaleskit.R;

public class Activity_Browser extends AppCompatActivity {
    private static final String TAG = Activity_Browser.class.getSimpleName();

    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout lnLoading;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

    private String urlClipBoard = "";
    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        initWidgets();
        LoadLink();

    }
    void initWidgets(){
        toolbar = findViewById(R.id.toolbar_appBrowser);
        webView = findViewById(R.id.webView_appBrowser);
//        progressBar = findViewById(R.id.progress_bar_appBrowser);
        lnLoading = findViewById(R.id.ln_Loading);
//        progressBar.setMax(100);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getStringExtra("args").equalsIgnoreCase("1")){
            getSupportActionBar().setTitle("Promos");
        }else if(getIntent().getStringExtra("args").equalsIgnoreCase("1")){
            getSupportActionBar().setTitle("Events");
        } else {
            getSupportActionBar().setTitle("Privacy & Policy");
        }
    }

    private void LoadLink(){

        lnLoading.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url_link"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Handle back button press to navigate within the WebView
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
                lnLoading.setVisibility(View.GONE);
            } else {
                redirect = false;
            }

        }
    }


}