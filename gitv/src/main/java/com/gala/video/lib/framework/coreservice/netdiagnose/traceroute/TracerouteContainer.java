package com.gala.video.lib.framework.coreservice.netdiagnose.traceroute;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class TracerouteContainer {
    private float elapsedTime;
    private String hostName;
    private String ip;

    public TracerouteContainer(String hostName, String ip, float elapsedTime) {
        this.ip = ip;
        this.elapsedTime = elapsedTime;
        this.hostName = hostName;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public float getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getHostname() {
        return this.hostName;
    }

    public void setHostname(String hostName) {
        this.hostName = hostName;
    }

    public String toString() {
        return "TraceContainer[Host:" + this.hostName + "ip:" + this.ip + "time:" + this.elapsedTime + AlbumEnterFactory.SIGN_STR;
    }
}
