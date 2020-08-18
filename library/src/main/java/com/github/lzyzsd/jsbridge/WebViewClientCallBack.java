package com.github.lzyzsd.jsbridge;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * Created by x-sir on 2020/7/10 :)
 * Function:
 */
public interface WebViewClientCallBack {
    void onPageStarted();

    void onProgressing(int progress);

    void onPageFinished();

    void onReceivedError(int errorCode, String description, String failingUrl);

    WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request);

    void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);
}
