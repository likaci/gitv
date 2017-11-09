package com.gala.sdk.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public interface IActivityHooker {
    boolean dispatchKeyEvent(KeyEvent keyEvent);

    Activity getActivity();

    void onCreate(Bundle bundle);

    void onDestroy();

    void onNewIntent(Intent intent);

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void onWindowFocusChanged(boolean z);

    void setActivity(Activity activity);
}
