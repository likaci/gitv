package com.gala.video.app.epg.web;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.app.epg.web.pingback.IWebLoadPingback;
import com.gala.video.app.epg.web.pingback.IWebLoadPingback.ILoadStateListener;
import com.gala.video.app.epg.web.pingback.WebLoadPingback;
import com.gala.video.app.epg.web.widget.IGaLaWebView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class WebBaseActivity extends QMultiScreenActivity implements ILoadStateListener {
    private static final String TAG = "EPG/WebBaseActivity";
    protected WebInfo mBaseWebInfo;
    protected IGaLaWebView mGaLaWebView;
    private IWebLoadPingback mIWebLoadPingback;
    protected String mWebUrl = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activity_webview);
        initIntentModel();
        initViews();
        initWebView();
    }

    private void initViews() {
        this.mIWebLoadPingback = new WebLoadPingback();
        ImageProviderApi.getImageProvider().stopAllTasks();
        this.mGaLaWebView = (IGaLaWebView) findViewById(C0508R.id.epg_webview);
        this.mGaLaWebView.setLoadStateListener(this);
    }

    private void initIntentModel() {
        WebIntentModel intentModel = null;
        Intent intent = getIntent();
        if (intent != null) {
            intentModel = (WebIntentModel) intent.getSerializableExtra("intent_model");
        }
        this.mBaseWebInfo = new WebInfo(intentModel);
        this.mWebUrl = getWebUrl();
        LogUtils.m1568d(TAG, this.mBaseWebInfo);
    }

    public boolean handleJsKeyEvent(KeyEvent event) {
        if (event.getAction() == 1 || (event.getRepeatCount() > 0 && event.getKeyCode() == 23)) {
            return super.handleKeyEvent(event);
        }
        if (this.mGaLaWebView == null || !this.mGaLaWebView.dispatchKeyEvent(event)) {
            return super.handleKeyEvent(event);
        }
        return true;
    }

    protected void initWebView() {
        initGalaWebView();
        showUrl(this.mWebUrl);
    }

    protected void initGalaWebView() {
        WebViewDataImpl jsonParam = generateJsonParams();
        this.mIWebLoadPingback.setBeforeWebViewTime();
        this.mGaLaWebView.init(jsonParam, this.mWebUrl);
        this.mIWebLoadPingback.setEventType(this.mGaLaWebView.getEventType());
        this.mIWebLoadPingback.setAfterWebViewTime();
        LogUtils.m1571e(TAG, "initGalaWebView() -> jsonParam:" + jsonParam);
    }

    protected void showUrl(String url) {
        this.mGaLaWebView.show(url);
        this.mIWebLoadPingback.setLoadUrlTime();
    }

    protected String getWebUrl() {
        return null;
    }

    protected WebViewDataImpl generateJsonParams() {
        return GetInterfaceTools.getWebJsonParmsProvider().getDefaultDataImpl();
    }

    protected void onStart() {
        super.onStart();
        isLoaderWEBActivity = true;
    }

    protected void onStop() {
        super.onStop();
        isLoaderWEBActivity = false;
    }

    protected void onPause() {
        super.onPause();
        this.mGaLaWebView.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.mGaLaWebView.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mBaseWebInfo = null;
        if (this.mGaLaWebView != null) {
            this.mGaLaWebView.onDestroy();
        }
        this.mGaLaWebView = null;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C0508R.anim.share_page_exit);
    }

    public void onPingbackCompleted() {
        if (this.mIWebLoadPingback != null) {
            this.mIWebLoadPingback.send(this.mBaseWebInfo);
        }
    }
}
