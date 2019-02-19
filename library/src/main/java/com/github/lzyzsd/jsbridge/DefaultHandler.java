package com.github.lzyzsd.jsbridge;

import android.util.Log;

public class DefaultHandler implements BridgeHandler {

    private static final String TAG = "DefaultHandler";

    @Override
    public void handler(String data, CallBackFunction function) {
        Log.i(TAG, "DefaultHandler handler():data===" + data);
        if (function != null) {
            function.onCallBack("DefaultHandler response data");
        }
    }

}
