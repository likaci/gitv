package com.gala.sdk.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;

public class ActivityHooker implements IActivityHooker {
    private WeakReference<Activity> a;

    public void onCreate(Bundle bundle) {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
    }

    public void onNewIntent(Intent intent) {
    }

    public void onWindowFocusChanged(boolean z) {
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return false;
    }

    public void setActivity(Activity activity) {
        this.a = new WeakReference(activity);
    }

    public Activity getActivity() {
        return (Activity) this.a.get();
    }
}
