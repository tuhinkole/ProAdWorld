package in.proadworld.proadworld;



import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class SecondActivity extends AppCompatActivity{
    private WebView webViewurl;
    private ProgressDialog progressBar;
    String newurl;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().hide();


        mInterstitialAd = new InterstitialAd(SecondActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                super.onAdClosed();
                finish();
            }
        });


        try{
            newurl = getIntent().getStringExtra("url");
//            Log.d("url",newurl);
        }catch (Exception e){
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                doubleBackToExitPressedOnce = true;
            }
        }, 30000);


        Toast.makeText(this, "Please wait for 30 Second.", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);



        webViewurl = (WebView) findViewById(R.id.webView1);
        WebSettings settings = webViewurl.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);


//        webViewurl.getSettings().setJavaScriptEnabled(true);

        webViewurl.getSettings().setBuiltInZoomControls(true);
        final Activity activity = this;


        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        webViewurl.loadUrl(newurl);

        webViewurl.setWebViewClient(new WebViewClient() {


            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if (!progressBar.isShowing()) {
//                progressBar.show();
//                }
            }

            public void onPageFinished(WebView view, String url) {
//                if (progressBar.isShowing()) {
//                progressBar.dismiss();
//                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                if (progressBar.isShowing()) {
//                progressBar.dismiss();
//                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            showInterstitial();
            super.onBackPressed();
            return;
        }
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                doubleBackToExitPressedOnce = true;
//            }
//        }, 5000);
//
//
//        Toast.makeText(this, "Please wait for 5 second", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
    }

//    private void loadInterstitialAd() {
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//        mInterstitialAd.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                if(mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//            }
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
//    }

    public void  showInterstitial(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }
}
