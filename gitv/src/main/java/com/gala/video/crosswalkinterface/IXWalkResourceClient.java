package com.gala.video.crosswalkinterface;

import android.net.http.SslError;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;

public interface IXWalkResourceClient {
    void onDocumentLoadedInFrame(IXWalkView iXWalkView, long j);

    void onLoadFinished(IXWalkView iXWalkView, String str);

    void onLoadStarted(IXWalkView iXWalkView, String str);

    void onProgressChanged(IXWalkView iXWalkView, int i);

    void onReceivedLoadError(IXWalkView iXWalkView, int i, String str, String str2);

    void onReceivedSslError(IXWalkView iXWalkView, ValueCallback<Boolean> valueCallback, SslError sslError);

    WebResourceResponse shouldInterceptLoadRequest(IXWalkView iXWalkView, String str);

    boolean shouldOverrideUrlLoading(IXWalkView iXWalkView, String str);
}
