package com.sunchang.callhtmltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoginDialog extends AppCompatActivity {

    private WebView loginWebview;

    private WebSettings mWebSettings;

    private ProgressBar mProgressBar;

    private Button mBtnClose;

    private String loginUrl;

    private String baseUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (baseUrl) {
                case MainActivity.DAYANGD:
                    LoginDialog.this.setResult(1);
                    LoginDialog.this.finish();
                    break;
                case MainActivity.LONGBAIDU:
                    LoginDialog.this.setResult(2);
                    LoginDialog.this.finish();
                    break;
                case MainActivity.UOL78:
                    LoginDialog.this.setResult(3);
                    LoginDialog.this.finish();
                    break;
                case MainActivity.SY9D:
                    LoginDialog.this.setResult(4);
                    LoginDialog.this.finish();
                    break;
                case MainActivity.I080:
                    LoginDialog.this.setResult(5);
                    LoginDialog.this.finish();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dialog);

        Intent intent = getIntent();
        loginUrl = intent.getStringExtra("loginUrl");
        baseUrl = intent.getStringExtra("baseUrl");

        this.initViews();
        this.initEvents();
        loginWebview.loadUrl(loginUrl);
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_bar_login);
        loginWebview = findViewById(R.id.webview_login);
        mWebSettings = loginWebview.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        loginWebview.setHorizontalScrollBarEnabled(false);
        loginWebview.setVerticalScrollBarEnabled(false);
        loginWebview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        mBtnClose = findViewById(R.id.btn_login_close);
    }

    private void initEvents() {
        loginWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(loginUrl)) {
                    return false;
                } else if (url.equals(baseUrl + "?mobile=2")) {
                    Message msg = new Message();
                    handler.sendMessage(msg);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.local_obj.getSource('<html>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        loginWebview.setWebChromeClient(new WebChromeClient() {
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
                LoginDialog.this.finish();
            }
        });
    }

    protected class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(final String html) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = null;
                    try {
                        document = Jsoup.parse(html);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (baseUrl.equals(MainActivity.UOL78)) {
                        if (document.getElementById("messagetext") != null) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    } else {
                        if (document.getElementsByClass("jump_c").size() != 0) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
