package com.gala.video.api;

public interface IPingbackApi {
    void call(IPinbackCallback iPinbackCallback, String str);

    void callSync(IPinbackCallback iPinbackCallback, String str);

    void setDelayDuration(long j);
}
