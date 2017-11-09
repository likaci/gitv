package com.gala.video.app.epg.home.controller.activity;

public interface IActivityLifeCycle {
    void onActivityDestroy();

    void onActivityPause();

    void onActivityResume();

    void onActivityStart();

    void onActivityStop();
}
