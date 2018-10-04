package com.sunchang.callhtmltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ForumContentActivity extends AppCompatActivity {

    private static final int NOT_LOG_IN = 1;

    private static final int LOGGED_IN = 2;

    public WebView webView;

    private WebSettings mWebSettings;

    private ProgressBar progressBar;

    private Toolbar mToolbar;

    private TextView tvAutoReply;

    private String baseUrl;

    private String contentUrl;

    private String loginUrl;

    private String username;

    private String password;

    private boolean isChecked = false;  //是否已检查登录状态

    private boolean isLogin = true;  //是否已经登录

    private boolean loggedIn = false;  //是否处于登录成功界面

    private boolean sy9d1080Replied = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOT_LOG_IN:
                    webView.loadUrl(loginUrl);
                    break;
                case LOGGED_IN:
                    webView.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_content);
        Intent intent = this.getIntent();
        baseUrl = intent.getStringExtra("baseUrl");
        loginUrl = intent.getStringExtra("loginUrl");
        contentUrl = intent.getStringExtra("contentUrl");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        this.initView();
        this.initEvents();

        webView.loadUrl(contentUrl);
        Log.e("contentUrl", contentUrl);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
            mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        tvAutoReply = findViewById(R.id.tv_auto_reply);

        webView = this.findViewById(R.id.web_view);
        webView.setVisibility(View.INVISIBLE);
        mWebSettings = webView.getSettings();
        progressBar = this.findViewById(R.id.progress_bar);

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        webView.addJavascriptInterface(new ForumContentActivity.InJavaScriptLocalObj(), "local_obj");

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
    }

    private void initEvents() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                if (url.contains("thread")) {
//                    if (baseUrl.equals("http://www.87lou.com")) {
//                        view.loadUrl("javascript:(function(){document.getElementsByClassName('hd')[0].style.display='none';" +
//                                "document.getElementsByClassName('pd2')[0].style.display='none';" +
//                                "document.getElementsByClassName('box')[0].style.display='none';" +
//                                "document.getElementsByClassName('tz mtn')[0].style.display='none';})()");
//                    } else if(baseUrl.equals("http://www.vkugq.com")){
//                        view.loadUrl("javascript:(function(){document.getElementById('toptb').style.display='none';" +
//                                "document.getElementById('hd').style.display='none';" +
//                                "document.getElementById('myprompt_menu').style.display='none';" +
//                                "document.getElementById('diynavtop').style.display='none';" +
//                                "document.getElementById('pt').style.display='none';" +
//                                "document.getElementById('pgt').style.display='none';" +
//                                "document.getElementsByClassName('pgs mtm mbm cl')[0].style.display='none';" +
//                                "document.getElementById('diyfastposttop').style.display='none';" +
//                                "document.getElementsByClassName('plc plm')[0].style.display='none';" +
//                                "document.getElementById('ft').style.display='none';" +
//                                "document.getElementById('LELE_ShowDIV_UNION').style.display='none';})()");
//                    } else {
//                        view.loadUrl("javascript:(function(){document.getElementsByClassName('header')[0].style.display='none';})()");
//                    }
//                }
                if (loggedIn) {
                    view.loadUrl(contentUrl);
                    view.setVisibility(View.VISIBLE);
                    loggedIn = false;
                }
                if (!isChecked) {
                    view.loadUrl("javascript:window.local_obj.showSource('<html>'+"
                            + "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
                if (!isLogin) {
                    view.evaluateJavascript("javascript:document.getElementsByName('username')[0].value='" + username + "';",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                }
                            });
                    view.evaluateJavascript("javascript:document.getElementsByName('password')[0].value='" + password + "';",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {}
                            });
                    if (baseUrl.equals(MainActivity.LONGBAIDU) || baseUrl.equals(MainActivity.UOL78)) {
                        view.loadUrl("javascript:document.getElementById('submit').click()");
                    } else {
                        view.loadUrl("javascript:document.getElementsByName('submit')[0].click()");
                    }
                    loggedIn = true;
                    isLogin = true;
                }
                if (sy9d1080Replied) {
                    view.evaluateJavascript("javascript:document.getElementById('needmessage').value='感谢楼主分享';",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {}
                            });
                    view.evaluateJavascript("javascript:document.getElementById('postsubmit').removeAttribute('disable');",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {}
                            });
                    view.loadUrl("javascript:document.getElementById('postsubmit').click();");
                    sy9d1080Replied = false;
                }
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
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
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ForumContentActivity.this.startActivity(intent);
            }
        });

        tvAutoReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseUrl.equals(MainActivity.UOL78)) {
                    webView.evaluateJavascript("javascript:document.getElementById('fastpostmessage').innerHTML='感谢楼主分享';",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {}
                            });
                    webView.loadUrl("javascript:document.getElementById('fastpostsubmit').click()");
                } else if (baseUrl.equals(MainActivity.SY9D)) {
                    long secondClickTime = System.currentTimeMillis();
                    if (secondClickTime - SearchResultActivity.firstReplyTime> 10000) {
//                        webView.loadUrl("javascript:document.getElementsByClassName('replyadd_a bus_colora')[1].click();");
                        webView.loadUrl("javascript:document.getElementsByClassName('y')[0].click();");
                        sy9d1080Replied = true;
                        SearchResultActivity.firstReplyTime = secondClickTime;
                    } else {
                        Toast.makeText(ForumContentActivity.this, "两次回复间隔不得小于10秒", Toast.LENGTH_SHORT).show();
                    }
                } else if (baseUrl.equals(MainActivity.I080)) {
                    long secondClickTime = System.currentTimeMillis();
                    if (secondClickTime - SearchResultActivity.firstReplyTime > 60000) {
                        webView.loadUrl("javascript:document.getElementById('replyid').click()");
                        sy9d1080Replied = true;
                        SearchResultActivity.firstReplyTime = secondClickTime;
                    } else {
                        Toast.makeText(ForumContentActivity.this, "两次回复间隔不得小于60秒", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    webView.evaluateJavascript("javascript:document.getElementById('fastpostmessage').value='感谢楼主分享';",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                }
                            });
                    webView.loadUrl("javascript:document.getElementById('fastpostsubmit').click()");
                }
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumContentActivity.this.finish();
            }
        });
    }

    protected class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String html) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = null;
                    try {
                        document = Jsoup.parse(html);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (baseUrl.equals(MainActivity.SY9D)) {
                        try {
                            document.getElementsByClass("footer").get(0).child(0).child(0);
                            Message msg = new Message();
                            msg.what = LOGGED_IN;
                            handler.sendMessage(msg);
                            isLogin = true;
                        } catch (IndexOutOfBoundsException e) {
                            Message msg = new Message();
                            msg.what = NOT_LOG_IN;
                            handler.sendMessage(msg);
                            isLogin = false;
                        }
                    } else {
                        if (document.getElementsByAttributeValue("title", "登录").size() != 0) {
                            Message msg = new Message();
                            msg.what = NOT_LOG_IN;
                            handler.sendMessage(msg);
                            isLogin = false;
                        } else {
                            Message msg = new Message();
                            msg.what = LOGGED_IN;
                            handler.sendMessage(msg);
                            isLogin = true;
                        }
                    }
                    isChecked = true;
                }
            }).start();
        }

        @JavascriptInterface
        public void getHtml(final String html) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = null;
                    try {
                        document = Jsoup.parse(html);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Elements elements = document.getElementsByAttributeValue("name", "loginsubmit");
                    if (elements.size() != 0) {
                        Log.e("element", elements.get(0).toString());
                    }
                }
            }).start();
        }
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
                webView.loadUrl(contentUrl);
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

    //获取状态栏高度
    private int getStatusBarHeight() {
        int statusBarHeight = dip2px(25);
        try {
            Class<?> clazz = Class.forName("com.android.internal.R.dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    //将dp单位转换为px
    private int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
