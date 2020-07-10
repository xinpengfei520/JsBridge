package com.github.lzyzsd.jsbridge;

/**
 * Created by x-sir on 2020/7/10 :)
 * Function:
 */
public interface WebViewClientCallBack {
    void onPageStarted();

    void onProgressing(int progress);

    void onPageFinished();

    void onReceivedError(int errorCode, String description, String failingUrl);
}
