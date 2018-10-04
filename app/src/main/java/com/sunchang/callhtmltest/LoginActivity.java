package com.sunchang.callhtmltest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LogoutWebView logoutWebView01;
    private LogoutWebView logoutWebView02;
    private LogoutWebView logoutWebView03;
    private LogoutWebView logoutWebView04;
    private LogoutWebView logoutWebView05;

    public LoginStatusCard loginStatusCard01;
    public LoginStatusCard loginStatusCard02;
    public LoginStatusCard loginStatusCard03;
    public LoginStatusCard loginStatusCard04;
    public LoginStatusCard loginStatusCard05;

    private List<LoginStatusCard> loginStatusCards = new ArrayList<>();

    private Button btnConfirm;

    private Button btnAutoLogout;

    private boolean logined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_FULLSCREEN | layoutParams.flags);
        }

        this.initViews();
        this.initEvents();
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btn_confirm);
        btnAutoLogout = findViewById(R.id.btn_auto_logout);

        loginStatusCard01 = new LoginStatusCard(this, (CardView) findViewById(R.id.cardview_login_01),
                (LinearLayout) findViewById(R.id.ll_login_01), (TextView) findViewById(R.id.tv_login_01),
                (TextView)findViewById(R.id.tv_signup_01));
        loginStatusCard02 = new LoginStatusCard(this, (CardView) findViewById(R.id.cardview_login_02),
                (LinearLayout) findViewById(R.id.ll_login_02), (TextView) findViewById(R.id.tv_login_02),
                (TextView)findViewById(R.id.tv_signup_02));
        loginStatusCard03 = new LoginStatusCard(this, (CardView) findViewById(R.id.cardview_login_03),
                (LinearLayout) findViewById(R.id.ll_login_03), (TextView) findViewById(R.id.tv_login_03),
                (TextView)findViewById(R.id.tv_signup_03));
        loginStatusCard04 = new LoginStatusCard(this, (CardView) findViewById(R.id.cardview_login_04),
                (LinearLayout) findViewById(R.id.ll_login_04), (TextView) findViewById(R.id.tv_login_04),
                (TextView)findViewById(R.id.tv_signup_04));
        loginStatusCard05 = new LoginStatusCard(this, (CardView) findViewById(R.id.cardview_login_05),
                (LinearLayout) findViewById(R.id.ll_login_05), (TextView) findViewById(R.id.tv_login_05),
                (TextView)findViewById(R.id.tv_signup_05));

        loginStatusCards.add(loginStatusCard01);
        loginStatusCards.add(loginStatusCard02);
        loginStatusCards.add(loginStatusCard03);
        loginStatusCards.add(loginStatusCard04);
        loginStatusCards.add(loginStatusCard05);

        logoutWebView01 = new LogoutWebView(new WebView(this));
        logoutWebView02 = new LogoutWebView(new WebView(this));
        logoutWebView03 = new LogoutWebView(new WebView(this));
        logoutWebView04 = new LogoutWebView(new WebView(this));
        logoutWebView05 = new LogoutWebView(new WebView(this));

        for (int i = 0; i < MyApplication.LOGIN_STATUS.length; i++) {
            if (MyApplication.LOGIN_STATUS[i]) {
                loginStatusCards.get(i).setLoginViewStyles();
            }
        }
        checkLoginStatus();
    }

    private void initEvents() {
        btnConfirm.setOnClickListener(this);
        btnAutoLogout.setOnClickListener(this);

        loginStatusCards.get(0).setOnLoginClickListener(MainActivity.DAYANGD,
                "http://www.dayangd.com/member.php?mod=logging&action=login");

        loginStatusCards.get(0).setOnSignupClickListener(0, logoutWebView01, MainActivity.DAYANGD,
                "http://www.dayangd.com/member.php?mod=logging&action=login", "http://www.dayangd.com/member.php?mod=register");

        loginStatusCards.get(1).setOnLoginClickListener(MainActivity.LONGBAIDU,
                "http://www.longbaidu.com/member.php?mod=logging&action=login");

        loginStatusCards.get(1).setOnSignupClickListener(1, logoutWebView02, MainActivity.LONGBAIDU,
                "http://www.longbaidu.com/member.php?mod=logging&action=login", "http://www.longbaidu.com/member.php?mod=register");

        loginStatusCards.get(2).setOnLoginClickListener(MainActivity.UOL78,
                "http://www.87lou.com/member.php?mod=logging&action=login&mobile=yes");

        loginStatusCards.get(2).setOnSignupClickListener(2, logoutWebView03, MainActivity.UOL78,
                "http://www.87lou.com/member.php?mod=logging&action=login&mobile=yes", "http://www.87lou.com/member.php?mod=register");

        loginStatusCards.get(3).setOnLoginClickListener(MainActivity.SY9D,
                "http://www.sy9d.com/member.php?mod=logging&action=login");

        loginStatusCards.get(3).setOnSignupClickListener(3, logoutWebView04, MainActivity.SY9D,
                "http://www.sy9d.com/member.php?mod=logging&action=login", "http://www.sy9d.com/member.php?mod=register");

        loginStatusCards.get(4).setOnLoginClickListener(MainActivity.I080,
                "http://www.1080.cn/member.php?mod=logging&action=login");

        loginStatusCards.get(4).setOnSignupClickListener(4, logoutWebView05, MainActivity.I080,
                "http://www.1080.cn/member.php?mod=logging&action=login", "http://www.1080.cn/member.php?mod=register");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (logined) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btn_auto_logout:
                for (int i = 0; i < MyApplication.LOGIN_STATUS.length; i++) {
                    if (MyApplication.LOGIN_STATUS[i]) {
                        MyApplication.LOGIN_STATUS[i] = false;
                        loginStatusCards.get(i).setLogoutViewStyles();
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        MyApplication.status_editor.putBoolean(MainActivity.DAYANGD, MyApplication.LOGIN_STATUS[0]);
        MyApplication.status_editor.putBoolean(MainActivity.LONGBAIDU, MyApplication.LOGIN_STATUS[1]);
        MyApplication.status_editor.putBoolean(MainActivity.UOL78, MyApplication.LOGIN_STATUS[2]);
        MyApplication.status_editor.putBoolean(MainActivity.SY9D, MyApplication.LOGIN_STATUS[3]);
        MyApplication.status_editor.putBoolean(MainActivity.I080, MyApplication.LOGIN_STATUS[4]);
        MyApplication.status_editor.apply();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                MyApplication.LOGIN_STATUS[0] = true;
                loginStatusCards.get(0).setLoginViewStyles();
                break;
            case 2:
                MyApplication.LOGIN_STATUS[1] = true;
                loginStatusCards.get(1).setLoginViewStyles();
                break;
            case 3:
                MyApplication.LOGIN_STATUS[2] = true;
                loginStatusCards.get(2).setLoginViewStyles();
                break;
            case 4:
                MyApplication.LOGIN_STATUS[3] = true;
                loginStatusCards.get(3).setLoginViewStyles();

                break;
            case 5:
                MyApplication.LOGIN_STATUS[4] = true;
                loginStatusCards.get(4).setLoginViewStyles();
                break;
            default:
        }
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        logined = false;
        for (boolean b : MyApplication.LOGIN_STATUS) {
            if (b) {
                logined = true;
            }
        }
        if (logined) {
            btnConfirm.setBackground(getDrawable(R.drawable.theme_button_selector));
            btnAutoLogout.setBackground(getDrawable(R.drawable.theme_button_selector));
        } else {
            btnConfirm.setBackgroundColor(getResources().getColor(R.color.logout_card_bg));
            btnAutoLogout.setBackgroundColor(getResources().getColor(R.color.logout_card_bg));
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public class LogoutWebView {

        private static final int LOGINED = 0;

        private static final int NOT_LOGIN = 1;

        private WebView webView;

        private String baseUrl = "";

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOGINED:
                        if (baseUrl.equals(MainActivity.SY9D)) {
                            webView.loadUrl(MainActivity.SY9D);
                        } else if (baseUrl.equals(MainActivity.UOL78)) {
                            webView.loadUrl("javascript:document.getElementsByClassName('pd2')[0].children[8].click();");
                        } else {
                            webView.loadUrl("javascript:document.getElementsByClassName('dialog')[0].click();");
                        }
                        break;
                    case NOT_LOGIN:
                        if (baseUrl.equals(MainActivity.SY9D)) {
                            MyApplication.LOGIN_STATUS[3] = false;
                            loginStatusCards.get(3).setLogoutViewStyles();
                        }
                        break;
                    default:
                }
                switch (baseUrl) {
                    case MainActivity.DAYANGD:
                        MyApplication.LOGIN_STATUS[0] = false;
                        loginStatusCards.get(0).setLogoutViewStyles();
                        break;
                    case MainActivity.LONGBAIDU:
                        MyApplication.LOGIN_STATUS[1] = false;
                        loginStatusCards.get(1).setLogoutViewStyles();
                        break;
                    case MainActivity.UOL78:
                        MyApplication.LOGIN_STATUS[2] = false;
                        loginStatusCards.get(2).setLogoutViewStyles();
                        break;
                    case MainActivity.I080:
                        MyApplication.LOGIN_STATUS[4] = false;
                        loginStatusCards.get(4).setLogoutViewStyles();
                        break;
                    default:
                }
                checkLoginStatus();
                baseUrl = "";
            }
        };

        public LogoutWebView(WebView webView) {
            this.webView = webView;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
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
                    super.onPageFinished(view, url);
                    if (url.equals(MainActivity.SY9D)) {
                        view.loadUrl("javascript:document.getElementsByClassName('dialog')[0].click();");
                        MyApplication.LOGIN_STATUS[3] = false;
                        loginStatusCards.get(3).setLogoutViewStyles();
                        checkLoginStatus();
                        baseUrl = "";
                    } else {
                        view.loadUrl("javascript:window.local_obj.showSource('<html>'+" +
                                "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }
            });
        }

        public void startLoad(String url) {
            webView.loadUrl(url);
        }

        public void setBaseUrl(String url) {
            baseUrl = url;
        }

        protected class InJavaScriptLocalObj{
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
                        if (baseUrl.equals(MainActivity.UOL78)) {
                            if (document.getElementById("messagetext") != null) {
                                Message msg = new Message();
                                msg.what = LOGINED;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = NOT_LOGIN;
                                handler.sendMessage(msg);
                            }
                        } else {
                            if (document.getElementsByClass("jump_c").size() != 0) {
                                Message msg = new Message();
                                msg.what = LOGINED;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = NOT_LOGIN;
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }).start();
            }
        }
    }
}
