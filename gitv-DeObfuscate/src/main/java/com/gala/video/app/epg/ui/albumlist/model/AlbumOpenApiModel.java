package com.gala.video.app.epg.ui.albumlist.model;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class AlbumOpenApiModel {
    private int channelId;
    private String channelName;
    private String dataTagId;
    private String dataTagName;
    private String dataTagType;
    private int intentFlag;
    private QLayoutKind layoutKind;
    private int loadLimitSize;

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDataTagId() {
        return this.dataTagId;
    }

    public void setDataTagId(String dataTagId) {
        this.dataTagId = dataTagId;
    }

    public String getDataTagName() {
        return this.dataTagName;
    }

    public void setDataTagName(String dataTagName) {
        this.dataTagName = dataTagName;
    }

    public String getDataTagType() {
        return this.dataTagType;
    }

    public void setDataTagType(String dataTagType) {
        this.dataTagType = dataTagType;
    }

    public QLayoutKind getLayoutKind() {
        return this.layoutKind;
    }

    public void setLayoutKind(QLayoutKind layoutKind) {
        this.layoutKind = layoutKind;
    }

    public int getIntentFlag() {
        return this.intentFlag;
    }

    public void setIntentFlag(int intentFlag) {
        this.intentFlag = intentFlag;
    }

    public int getLoadLimitSize() {
        return this.loadLimitSize;
    }

    public void setLoadLimitSize(int loadLimitSize) {
        this.loadLimitSize = loadLimitSize;
    }

    public String toString() {
        return "DataModel4Api [channelId=" + this.channelId + ", channelName=" + this.channelName + ", labelId=" + this.dataTagId + ", labelName=" + this.dataTagName + ", labelType=" + this.dataTagType + ", intentFlag=" + this.intentFlag + ", loadLimitSize=" + this.loadLimitSize + AlbumEnterFactory.SIGN_STR;
    }
}
