package com.gala.video.app.player;

import android.annotation.SuppressLint;
import com.gala.sdk.player.IProjectEventReporter;
import com.gala.sdk.player.data.IVideo;

public class SProjectEventReporter implements IProjectEventReporter {
    @SuppressLint({"NewApi"})
    public IProjectEventReporter getInstance() {
        try {
            return (IProjectEventReporter) Class.forName("com.gala.video.app.player.openapi.ProjectEventReporter").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public void onCompleted() {
    }

    public void onError() {
    }

    public void onPaused() {
    }

    public void onPrepared() {
    }

    public void onPreparing(IVideo arg0) {
    }

    public void onStarted() {
    }

    public void onStopped() {
    }

    public void onStopping() {
    }
}
