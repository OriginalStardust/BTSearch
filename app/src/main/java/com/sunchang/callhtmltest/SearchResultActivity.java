package com.sunchang.callhtmltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchResultActivity extends AppCompatActivity {

    public static long firstReplyTime = 0;

    private Toolbar mToolbar;

    private TextView mTextView;

    public static WebView webView;

    private ProgressBar progressBar;

    private String resultUrl;

    private String resultNum;

    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = this.getIntent();
        baseUrl = intent.getStringExtra("baseUrl");
        resultUrl = intent.getStringExtra("resultUrl");
        resultNum = intent.getStringExtra("resultNum");
        this.initView();
        this.initEvents();
        webView.loadUrl(resultUrl);
    }

    private void initView() {
        mTextView = findViewById(R.id.tv_toolbar_title);
        mTextView.setText(resultNum);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        webView = this.findViewById(R.id.web_view);
        progressBar = this.findViewById(R.id.progress_bar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void initEvents() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("thread")) {
                    Intent intent = new Intent(SearchResultActivity.this, ForumContentActivity.class);
                    intent.putExtra("baseUrl", baseUrl);
                    switch (baseUrl) {
                        case MainActivity.DAYANGD:
                            intent.putExtra("loginUrl", "http://www.dayangd.com/member.php?mod=logging&action=login&mobile=2");
                            intent.putExtra("username", "gungnir");
                            intent.putExtra("password", "icephoenix_72");
                            break;
                        case MainActivity.LONGBAIDU:
                            intent.putExtra("loginUrl", "http://www.longbaidu.com/member.php?mod=logging&action=login&mobile=1");
                            intent.putExtra("username", "gungnir");
                            intent.putExtra("password", "icephoenix_72");
                            break;
                        case MainActivity.UOL78:
                            intent.putExtra("loginUrl", "http://www.87lou.com/member.php?mod=logging&action=login&mobile=yes");
                            intent.putExtra("username", "gungnir01");
                            intent.putExtra("password", "icephoenix_72");
                            break;
                        case MainActivity.SY9D:
                            intent.putExtra("loginUrl", "http://www.sy9d.com/member.php?mod=logging&action=login");
                            intent.putExtra("username", "gungnir");
                            intent.putExtra("password", "icephoenix_72");
                            break;
                        case MainActivity.I080:
                            intent.putExtra("loginUrl", "http://www.1080.cn/member.php?mod=logging&action=login");
                            intent.putExtra("username", "gungnir");
                            intent.putExtra("password", "icephoenix_72");
                            break;
                        default:
                    }
                    intent.putExtra("contentUrl", url);
                    SearchResultActivity.this.startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }).start();
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                if (newProgress > 70) {
                    view.stopLoading();
                    if (baseUrl.equals(MainActivity.DAYANGD)) {
                        view.loadUrl("javascript:(function() {document.getElementById('head').style.display='none';" +
                                "document.getElementById('aside').style.display='none';" +
                                "document.getElementsByClassName('result-sort-game')[0].style.display='none';" +
                                "document.getElementById('container').style.width='auto';" +
                                "document.getElementById('container').style.overflow='hidden';" +
                                "document.getElementById('results').style.width='130%';" +
                                "document.getElementsByClassName('support-text-top')[0].style.fontSize='200%';" +
                                "var itemList = document.getElementsByClassName('result f s3');" +
                                "var titleList = document.getElementsByClassName('c-title');" +
                                "var abstractList = document.getElementsByClassName('c-abstract');" +
                                "var summaryList = document.getElementsByClassName('c-summary-1');" +
                                "for(var i = 0; i < itemList.length; i++){itemList[i].style.lineHeight='1.5';" +
                                "titleList[i].style.fontSize='290%';" +
                                "abstractList[i].style.fontSize='220%';" +
                                "summaryList[i].style.width='100%';summaryList[i].style.fontSize='220%';" +
                                "summaryList[i].style.height='auto';summaryList[i].style.overflow='hidden';}" +
                                "document.getElementById('pageFooter').style.fontSize='220%';" +
                                "document.getElementsByClassName('c-icon c-icon-bear-p')[0].style.display='none';" +
                                "var iconList = document.getElementsByClassName('c-icon c-icon-bear-pn');" +
                                "for(var i = 0; i < iconList.length; i++){iconList[i].style.display='none';}" +
                                "document.getElementById('footer').style.marginLeft='5%';" +
                                "document.getElementById('BottomBox').style.display='none';" +
                                "document.getElementById('bd-feedback').style.display='none';})()");
                    } else {
                        view.loadUrl("javascript:(function() {document.getElementById('toptb').style.display='none';" +
                                "document.getElementById('scform').style.display='none';" +
                                "document.getElementById('ft').style.display='none';" +
                                "document.getElementsByClassName('sttl mbn')[0].style.fontSize='240%';" +
                                "document.getElementById('threadlist').style.width='100%';" +
                                "document.getElementById('threadlist').style.fontSize='260%';" +
                                "var hList = document.getElementsByTagName('h3');" +
                                "for(var i = 0; i < hList.length; i++){hList[i].setAttribute('style', 'font-size: 150% !important');}" +
                                "document.getElementsByClassName('pg')[0].style.fontSize='300%';" +
                                "var pageList = document.getElementsByClassName('pg')[0].children;" +
                                "for(var i = 0; i < pageList.length; i++){pageList[i].style.padding='1% 1%';}" +
                                "pageList[pageList.length-2].style.display='none';" +
                                "pageList[pageList.length-1].style.paddingRight='4%'})()");
                        if (baseUrl.equals(MainActivity.I080)) {
                            view.evaluateJavascript("javascript:document.getElementById('rfu').style.display='none';",
                                    new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {}
                                    });
                        }
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                webView.reload();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            this.finish();
        }
    }
}
