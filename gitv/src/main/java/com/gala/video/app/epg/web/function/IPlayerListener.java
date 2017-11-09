package com.gala.video.app.epg.web.function;

public interface IPlayerListener {
    void checkLiveInfo(String str);

    void onAlbumSelected(String str);

    void startWindowPlay(String str);

    void switchPlay(String str);

    void switchScreenMode(String str);
}
