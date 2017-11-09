package com.gala.sdk.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityHookerGroup extends ActivityHooker {
    private final List<IActivityHooker> f722a = new CopyOnWriteArrayList();

    public void add(IActivityHooker hooker) {
        this.f722a.add(hooker);
    }

    public void remove(IActivityHooker hooker) {
        this.f722a.remove(hooker);
    }

    public int size() {
        return this.f722a.size();
    }

    public void onCreate(Bundle savedInstanceState) {
        for (IActivityHooker onCreate : this.f722a) {
            onCreate.onCreate(savedInstanceState);
        }
    }

    public void onStart() {
        for (IActivityHooker onStart : this.f722a) {
            onStart.onStart();
        }
    }

    public void onResume() {
        for (IActivityHooker onResume : this.f722a) {
            onResume.onResume();
        }
    }

    public void onPause() {
        for (IActivityHooker onPause : this.f722a) {
            onPause.onPause();
        }
    }

    public void onStop() {
        for (IActivityHooker onStop : this.f722a) {
            onStop.onStop();
        }
    }

    public void onDestroy() {
        for (IActivityHooker onDestroy : this.f722a) {
            onDestroy.onDestroy();
        }
    }

    public void onNewIntent(Intent intent) {
        for (IActivityHooker onNewIntent : this.f722a) {
            onNewIntent.onNewIntent(intent);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        for (IActivityHooker onWindowFocusChanged : this.f722a) {
            onWindowFocusChanged.onWindowFocusChanged(hasFocus);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        for (IActivityHooker dispatchKeyEvent : this.f722a) {
            if (dispatchKeyEvent.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return false;
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        for (IActivityHooker activity2 : this.f722a) {
            activity2.setActivity(activity);
        }
    }

    public Activity getActivity() {
        return super.getActivity();
    }
}
