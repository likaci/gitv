package com.gala.video.app.epg.ui.solotab;

import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import java.io.Serializable;

public class SoloTabInfoModel implements Serializable {
    private int channelId;
    private String f1953e = PingBackUtils.createEventId();
    private String from;
    private boolean isVip;
    private String sourceId;
    private String tabName;
    private String tabSrc;

    public String getTabSrc() {
        return this.tabSrc;
    }

    public void setTabSrc(String tabSrc) {
        this.tabSrc = tabSrc;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean isVip() {
        return this.isVip;
    }

    public void setVip(boolean vip) {
        this.isVip = vip;
    }

    public String getTabName() {
        return this.tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getE() {
        return this.f1953e;
    }

    public void setE(String e) {
        this.f1953e = e;
    }
}
