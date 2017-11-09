package com.gala.video.webview.event;

import android.view.KeyEvent;

public interface IWebBaseEvent {
    void addJavascriptInterface(Object obj);

    boolean canGoBack();

    int getEventType();

    void goBack();

    boolean handleJsKeyEvent(KeyEvent keyEvent);

    void initView();

    boolean isAlready();

    void loadJsMethod(String str);

    void onDestroy();

    void onPause();

    void onResume();

    void showErrorView();

    void showLoading();

    void showResult();
}
