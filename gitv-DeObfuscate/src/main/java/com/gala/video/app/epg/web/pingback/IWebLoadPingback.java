package com.gala.video.app.epg.web.pingback;

import com.gala.video.app.epg.web.model.WebInfo;

public interface IWebLoadPingback {

    public interface ILoadStateListener {
        void onPingbackCompleted();
    }

    void send(WebInfo webInfo);

    void setAfterWebViewTime();

    void setBeforeWebViewTime();

    void setEventType(int i);

    void setLoadUrlTime();
}
