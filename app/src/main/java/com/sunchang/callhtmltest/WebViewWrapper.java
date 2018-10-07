package com.sunchang.callhtmltest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Sun Chang on 2018/6/22.
 */

public class WebViewWrapper {

    private WebView webView;

    private String content;

    private String resultUrl;

    private boolean isValueChanged;

    private boolean isSearchClicked;

    private String baseUrl;

    private String resultNum;

    public WebViewWrapper(WebView webView, String baseUrl) {
        this.webView = webView;
        this.baseUrl = baseUrl;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setUserAgentString(MainActivity.USER_AGENT_WINDOWS);
        webView.addJavascriptInterface(new WebViewWrapper.InJavaScriptLocalObj(), "local_obj");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("searchid")) {
                    resultUrl = url;
                    isSearchClicked = true;
                    view.loadUrl(url);
                }
                Log.e("url", url);
                return true;
            }
        });

        this.setMyChromeClient(webView);
    }

    public void setMyChromeClient(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(final WebView view, int newProgress) {
                if (!isValueChanged) {
                    view.loadUrl("javascript:window.local_obj.getSource(document.getElementsByTagName('html')[0].innerHTML);");
                }
                if (isSearchClicked) {
                    view.evaluateJavascript("javascript:window.local_obj.getResultNum(document.getElementsByClassName('sttl mbn')[0].innerHTML);", null);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    protected class InJavaScriptLocalObj {

        @JavascriptInterface
        public void getSource(String html) {
            Document document = null;
            try {
                document = Jsoup.parse(html);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Element editText = document.getElementById("scform_srchtxt");
            Elements resultNum = document.getElementsByClass("sttl mbn");
            if ((editText != null) && (resultNum.size() == 0) && !isValueChanged) {
                isValueChanged = true;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }

        @JavascriptInterface
        public void getResultNum(final String result) {
            if (!"null".equals(result) && isSearchClicked) {
                resultNum = result.replaceAll("<(\\S*?)[^>]*>.*?|<.*? />", "")
                        .replace(" ", "")
                        .replace("\n", "")
                        .replace("结果:", "")
                        .replace("“" + content + "”", "");
                isSearchClicked = false;
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    webView.evaluateJavascript("javascript:document.getElementById('scform_srchtxt').value='" + content + "';", null);
                    webView.loadUrl("javascript:document.getElementById('scform_submit').click()");
                    break;
                case 2:
                    SearchResultNum searchResult = new SearchResultNum();
                    searchResult.setResultNum(resultNum);
                    searchResult.setBaseUrl(baseUrl);
                    MainActivity.mResultList.add(searchResult);
                    MainActivity.mResultAdapter.notifyDataSetChanged();
                    webView.stopLoading();
                    break;
                default:
            }
        }
    };

    public void startSearch(String url, String content) {
        isValueChanged = false;
        isSearchClicked = false;
        this.content = content;
        webView.loadUrl(url);
    }

    public String getResultUrl() {
        return resultUrl;
    }
}
