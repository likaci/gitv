package com.gala.video.app.player.controller;

import android.os.Bundle;

public interface IActivityEventListener {
    void onCreated(Bundle bundle);

    void onDestroyed();

    void onFinishing();

    void onPaused();

    void onResumed(int i);

    void onStarted();

    void onStopped();
}
