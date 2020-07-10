package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.File;


/**
 * Created by x-sir on 2019/08/07 :)
 * Function:WebView 工具类
 */
public class WebViewUtil {

    private static final String TAG = "WebViewUtil";
    private static final int MAX_PROGRESS = 100;
    private static WebViewClientCallBack mCallback;

    public static void initWebview(BridgeWebView webView, final ProgressBar progressbar) {
        initWebview(webView, progressbar, null);
    }

    /**
     * initialize WebView settings
     *
     * @param webView     BridgeWebView
     * @param progressbar ProgressBar
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebview(BridgeWebView webView, final ProgressBar progressbar, WebViewClientCallBack callBack) {
        mCallback = callBack;
        if (webView == null) return;
        WebSettings webSettings = webView.getSettings();
        String ua = webSettings.getUserAgentString();
        // 设置用户 Agent 标识
        webSettings.setUserAgentString(ua + " android_appName");
        // 设置支持 JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 是否阻塞加载网络图片
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 设置文字编码方式
        webSettings.setDefaultTextEncodingName("UTF-8");
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置定位服务可用
        webSettings.setGeolocationEnabled(true);
        // 禁止使用 File 域
        webSettings.setAllowFileAccess(false);
        // 显式移除有风险的 WebView 系统隐藏接口
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        // 设置默认的 Handler
        webView.setDefaultHandler(new DefaultHandler());
        // 设置 WebChromeClient
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressbar.setProgress(newProgress);
                if (newProgress == MAX_PROGRESS) {
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, true);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
            }
        });

        webView.setWebViewClient(new BridgeWebViewClient(webView));
    }

    /**
     * 设置 JavaScript 是否可用，当页面退出的时候最好禁用
     *
     * @param webView BridgeWebView
     * @param enable  true:可用，false:不可用
     */
    public static void setJavaScriptEnable(BridgeWebView webView, boolean enable) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(enable);
    }

    /**
     * clear WebView cache and databases
     *
     * @param context application context
     */
    public static void clearWebViewCache(Context context) {
        if (context == null) {
            return;
        }
        // 清理 WebView 缓存数据库
        try {
            context.getApplicationContext().deleteDatabase("webview.db");
            context.getApplicationContext().deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // WebView 缓存文件
        File appCacheDir = new File(context.getApplicationContext().getFilesDir().getAbsolutePath() + "webview");
        Log.i(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webViewCacheDir = new File(context.getApplicationContext().getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.i(TAG, "webViewCacheDir path=" + webViewCacheDir.getAbsolutePath());

        // 删除 WebView 缓存目录
        if (webViewCacheDir.exists()) {
            deleteFile(webViewCacheDir);
        }
        // 删除 WebView 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file file
     */
    private static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
            file.delete();
        }
    }

    /**
     * 调用 JavaScript 的方法
     *
     * @param webView webView 对象
     * @param method  调用的方法名
     * @param json    传递的 json 数据
     */
    public static void callJavaScript(BridgeWebView webView, String method, String json) {
        if (webView == null) {
            Log.e(TAG, "webView is null object!");
            return;
        }

        if (TextUtils.isEmpty(method)) {
            Log.e(TAG, "method is null object!");
            return;
        }

        callJavaScript(webView, method, json, null);
    }

    /**
     * 调用 JavaScript 的方法
     *
     * @param webView  webView 对象
     * @param method   调用的方法名
     * @param json     传递的 json 数据
     * @param callBack 调用结果的回调
     */
    public static void callJavaScript(BridgeWebView webView, String method, String json, CallBackFunction callBack) {
        webView.callHandler(method, json, callBack);
    }

}
