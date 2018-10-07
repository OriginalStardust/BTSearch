package com.sunchang.callhtmltest;

import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Sun Chang on 2018/6/19.
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

        webView.setWebViewClient(new WebViewClient(){});

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(final WebView view, int newProgress) {
                view.evaluateJavascript("javascript:document.getElementsByClassName('support-text-top')[0].innerHTML;",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                if (!"null".equals(value) && !isRead) {
                                    resultNum = value.replace("为您", "")
                                            .replace("\"", "");
                                    SearchResultNum searchResult = new SearchResultNum();
                                    searchResult.setResultNum(resultNum);
                                    searchResult.setBaseUrl(WebViewWrapperDaYangD.this.baseUrl);
                                    MainActivity.mResultList.add(searchResult);
                                    MainActivity.mResultAdapter.notifyDataSetChanged();
                                    isRead = true;
                                    view.stopLoading();
                                }
                            }
                        });
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public void startSearch(String url) {
        webView.loadUrl(url);
        resultUrl = url;
        isRead = false;
    }

    public String getResultUrl() {
        return resultUrl;
    }
}
