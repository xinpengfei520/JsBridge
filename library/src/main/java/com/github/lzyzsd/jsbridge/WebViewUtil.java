package com.github.lzyzsd.jsbridge;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;


/**
 * Created by x-sir on 2019/08/07 :)
 * Function:WebView 工具类
 */
public class WebViewUtil {

    private static final String TAG = "WebViewUtil";

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
