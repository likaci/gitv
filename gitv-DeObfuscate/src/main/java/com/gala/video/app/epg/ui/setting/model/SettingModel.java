package com.gala.video.app.epg.ui.setting.model;

import java.io.Serializable;
import java.util.List;

public class SettingModel implements Serializable {
    private static final long serialVersionUID = 680435832116093761L;
    public boolean isDebugHelper;
    private List<SettingItem> items;
    private String pingbackBlock;
    private String pingbackQtcurl;
    private String titleDes;
    private String titleIcon;
    private String titleName;
    private String updateClass;

    public String getTitleName() {
        return this.titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleIcon() {
        return this.titleIcon;
    }

    public void setTitleIcon(String titleIcon) {
        this.titleIcon = titleIcon;
    }

    public String getTitleDes() {
        return this.titleDes;
    }

    public void setTitleDes(String titleDes) {
        this.titleDes = titleDes;
    }

    public List<SettingItem> getItems() {
        return this.items;
    }

    public void addHead(SettingItem item) {
        this.items.add(0, item);
    }

    public void setItems(List<SettingItem> items) {
        this.items = items;
    }

    public String getUpdateClass() {
        return this.updateClass;
    }

    public void setUpdateClass(String updateClass) {
        this.updateClass = updateClass;
    }

    public boolean isDebugHelper() {
        return this.isDebugHelper;
    }

    public void setDebugHelper(boolean isDebugHelper) {
        this.isDebugHelper = isDebugHelper;
    }

    public String getPingbackQtcurl() {
        return this.pingbackQtcurl;
    }

    public void setPingbackQtcurl(String pingbackQtcurl) {
        this.pingbackQtcurl = pingbackQtcurl;
    }

    public String getPingbackBlock() {
        return this.pingbackBlock;
    }

    public void setPingbackBlock(String pingbackBlock) {
        this.pingbackBlock = pingbackBlock;
    }
}
