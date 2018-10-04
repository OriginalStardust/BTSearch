package com.sunchang.callhtmltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class SignupActivity extends AppCompatActivity {

    private WebView signupWebview;

    private WebSettings mWebSettings;

    private String signupUrl;

    private String baseUrl;

    private ProgressBar mProgressBar;

    private Button mBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_signup);
        Intent intent = getIntent();
        signupUrl = intent.getStringExtra("signupUrl");
        baseUrl = intent.getStringExtra("baseUrl");

        this.initViews();
        this.initEvents();
        signupWebview.loadUrl(signupUrl);
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_bar_signup);
        signupWebview = findViewById(R.id.webview_signup);
        mWebSettings = signupWebview.getSettings();
        mBtnClose = findViewById(R.id.btn_signup_close);

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        signupWebview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        signupWebview.setHorizontalScrollBarEnabled(false);
        signupWebview.setVerticalScrollBarEnabled(false);
//        signupWebview.setVisibility(View.INVISIBLE);
    }

    private void initEvents() {
        signupWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(baseUrl)) {
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
//                view.loadUrl("javascript:(function(){document.getElementById('toptb').style.display='none';" +
//                        "document.getElementById('hd').style.visibility='hidden';" +
//                        "document.getElementById('wp').style.minWidth='0px';" +
//                        "document.getElementById('wp').style.width='120%';" +
//                        "document.getElementById('wp').style.zoom='1';" +
//                        "document.getElementById('main_hnav').style.display='none';" +
//                        "document.getElementById('ft').style.display='none'})()");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                }).start();
            }
        });

        signupWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupActivity.this.finish();
            }
        });
    }

    protected class InJavaScriptLocalObj{
        @JavascriptInterface
        public void showSource(String html) {

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
