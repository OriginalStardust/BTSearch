package com.sunchang.callhtmltest;

import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/6/19.
 */

public class WebViewWrapperDaYangD {

    private WebView webView;

    private String baseUrl;

    private String resultUrl;

    private boolean isRead;

    private String resultNum;

    public WebViewWrapperDaYangD(WebView webView, String baseUrl) {
        this.webView = webView;
        this.baseUrl = baseUrl;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setUserAgentString(MainActivity.USER_AGENT_WINDOWS);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        webView.setWebViewClient(new WebViewClient(){});

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress > 50 && !isRead){
                    isRead = true;
                    view.loadUrl("javascript:window.local_obj.getResultNum(" +
                            "document.getElementsByClassName('support-text-top')[0].innerHTML);");
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getResultNum(final String result) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    resultNum = result.replace("为您", "");
                    Message msg = new Message();
                    handler.sendMessage(msg);
                }
            }).start();

        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchResultNum searchResult = new SearchResultNum();
            searchResult.setResultNum(resultNum);
            searchResult.setBaseUrl(baseUrl);
            MainActivity.mResultList.add(searchResult);
            MainActivity.mResultAdapter.notifyDataSetChanged();
            webView.stopLoading();
        }
    };

    public void startSearch(String url) {
        webView.loadUrl(url);
        resultUrl = url;
        isRead = false;
    }

    public String getResultUrl() {
        return resultUrl;
    }
}
