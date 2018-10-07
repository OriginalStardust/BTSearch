package com.sunchang.callhtmltest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    public static final String DAYANGD = "http://www.dayangd.com/";

    public static final String LONGBAIDU = "http://www.longbaidu.com/";

    public static final String UOL78 = "http://www.87lou.com/";

//    public static final String HDCHD = "http://www.hdchd.cc/";

    public static final String SY9D = "http://www.sy9d.com/";

//    public static final String VKUGQ = "http://www.vkugq.com/";

    public static final String I080 = "http://www.1080.cn/";

//    public static final String YSLT = "http://www.yslt.cc/";

//    public static final String USER_AGENT_MOBILE = "Mozilla/5.0 (Linux; U; Android 8.0.0; zh-cn; MI 6 Build/OPR1.170623.027)";

    public static final String USER_AGENT_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";

    private long firstBackPressTime = 0;

    private List<WebView> webViewList = new ArrayList<>();

    private WebViewWrapperDaYangD wrapperDaYangD;
    private WebViewWrapper wrapperLongBaidu, wrapper87Lou, wrapperSY9D, wrapper1080;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;

    private SearchView mSearchView;

    private TextView mTvLogout;

    private ListView mSearchList;

    public static List<SearchResultNum> mResultList = new ArrayList<>();

    public static SearchResultNumAdapter mResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            this.initViews();
            this.initEvents();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.clearFocus();
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.main_label);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        WebView webView01 = new WebView(this);
        WebView webView02 = new WebView(this);
        WebView webView03 = new WebView(this);
        WebView webView04 = new WebView(this);
        WebView webView05 = new WebView(this);

        wrapperDaYangD = new WebViewWrapperDaYangD(webView01, DAYANGD);
        wrapperLongBaidu = new WebViewWrapper(webView02, LONGBAIDU);
        wrapper87Lou = new WebViewWrapper(webView03, UOL78);
        wrapperSY9D = new WebViewWrapper(webView04, SY9D);
        wrapper1080 = new WebViewWrapper(webView05, I080);

        webViewList.add(webView01);
        webViewList.add(webView02);
        webViewList.add(webView03);
        webViewList.add(webView04);
        webViewList.add(webView05);

        mTvLogout = findViewById(R.id.tv_logout);

        mSearchView = findViewById(R.id.search_view);
        mSearchList = findViewById(R.id.search_list);
        mResultAdapter = new SearchResultNumAdapter(this, mResultList);
        mSearchList.setAdapter(mResultAdapter);
    }

    private void initEvents() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, "请输入搜索内容！", Toast.LENGTH_SHORT).show();
                } else {
//                    ((InputMethodManager) mSearchView.getContext().getSystemService(INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(),
//                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    mResultList.clear();
                    mResultAdapter.notifyDataSetChanged();
                    mSearchList.setVisibility(View.VISIBLE);

                    if (MyApplication.LOGIN_STATUS[0]) {
                        webViewList.get(0).stopLoading();
                        wrapperDaYangD.startSearch("http://zhannei.baidu.com/cse/search?q=" + query + "&s=3055704869350336262&stp=1&nsid=0");
                    }
                    if (MyApplication.LOGIN_STATUS[1]) {
                        webViewList.get(1).stopLoading();
                        wrapperLongBaidu.startSearch("http://www.longbaidu.com/search.php?mod=forum", query);
                    }
                    if (MyApplication.LOGIN_STATUS[2]) {
                        webViewList.get(2).stopLoading();
                        wrapper87Lou.startSearch("http://www.87lou.com/search.php?mod=forum", query);
                    }
                    if (MyApplication.LOGIN_STATUS[3]) {
                        webViewList.get(3).stopLoading();
                        wrapperSY9D.startSearch("http://www.sy9d.com/search.php?mod=forum", query);
                    }
                    if (MyApplication.LOGIN_STATUS[4]) {
                        webViewList.get(4).stopLoading();
                        wrapper1080.startSearch("http://www.1080.cn/search.php?mod=forum", query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (mResultList.get(position).getBaseUrl()) {
                    case DAYANGD:
                        intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("baseUrl", DAYANGD);
                        intent.putExtra("resultUrl", wrapperDaYangD.getResultUrl());
                        intent.putExtra("resultNum", mResultList.get(position).getResultNum());
                        MainActivity.this.startActivity(intent);
                        break;
                    case LONGBAIDU:
                        intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("baseUrl", LONGBAIDU);
                        intent.putExtra("resultUrl", wrapperLongBaidu.getResultUrl());
                        intent.putExtra("resultNum", mResultList.get(position).getResultNum());
                        MainActivity.this.startActivity(intent);
                        break;
                    case UOL78:
                        intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("baseUrl", UOL78);
                        intent.putExtra("resultUrl", wrapper87Lou.getResultUrl());
                        intent.putExtra("resultNum", mResultList.get(position).getResultNum());
                        MainActivity.this.startActivity(intent);
                        break;
                    case SY9D:
                        intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("baseUrl", SY9D);
                        intent.putExtra("resultUrl", wrapperSY9D.getResultUrl());
                        intent.putExtra("resultNum", mResultList.get(position).getResultNum());
                        MainActivity.this.startActivity(intent);
                        break;
                    case I080:
                        intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        intent.putExtra("baseUrl", I080);
                        intent.putExtra("resultUrl", wrapper1080.getResultUrl());
                        intent.putExtra("resultNum", mResultList.get(position).getResultNum());
                        MainActivity.this.startActivity(intent);
                        break;
                    default:
                }
            }
        });

        mTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        for (WebView view : webViewList) {
            if (view != null){
                view.getSettings().setJavaScriptEnabled(false);
                view.clearHistory();
                view.removeAllViews();
                view.destroy();
                view = null;
            }
        }

        MyApplication.status_editor.putBoolean(MainActivity.DAYANGD, MyApplication.LOGIN_STATUS[0]);
        MyApplication.status_editor.putBoolean(MainActivity.LONGBAIDU, MyApplication.LOGIN_STATUS[1]);
        MyApplication.status_editor.putBoolean(MainActivity.UOL78, MyApplication.LOGIN_STATUS[2]);
        MyApplication.status_editor.putBoolean(MainActivity.SY9D, MyApplication.LOGIN_STATUS[3]);
        MyApplication.status_editor.putBoolean(MainActivity.I080, MyApplication.LOGIN_STATUS[4]);
        MyApplication.status_editor.apply();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.initViews();
                    this.initEvents();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        long secondBackPressTime = System.currentTimeMillis();
        if (secondBackPressTime - firstBackPressTime > 1500) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            firstBackPressTime = secondBackPressTime;
        } else {
            System.exit(0);
        }
    }
}
