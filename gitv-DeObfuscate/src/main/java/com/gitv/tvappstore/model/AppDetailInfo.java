package com.gitv.tvappstore.model;

import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gitv.tvappstore.utils.StringUtils;
import java.io.Serializable;
import java.util.List;

public class AppDetailInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String adverImageUrl;
    private String appDescription;
    private String appDownloadUrl;
    private Drawable appIconDrawable;
    private String appId;
    private List<String> appImageUrl;
    private boolean appIsSystem = false;
    private String appLogoUrl;
    private String appNum;
    private String appPackageName;
    private String appPublishTime;
    private String appRating;
    private String appTitle;
    private String appType;
    private String appVersion;
    private String apppackageSize;
    private boolean isFromMyApp = false;
    private boolean isFromRecommend = false;
    private String mPosition;

    public boolean isFromRecommend() {
        return this.isFromRecommend;
    }

    public void setFromRecommend(boolean isFromRecommend) {
        this.isFromRecommend = isFromRecommend;
    }

    public String getmPosition() {
        return this.mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public boolean isAppIsSystem() {
        return this.appIsSystem;
    }

    public void setAppIsSystem(boolean appIsSystem) {
        this.appIsSystem = appIsSystem;
    }

    public boolean isFromMyApp() {
        return this.isFromMyApp;
    }

    public void setFromMyApp(boolean isFromMyApp) {
        this.isFromMyApp = isFromMyApp;
    }

    public Drawable getAppIconDrawable() {
        return this.appIconDrawable;
    }

    public void setAppIconDrawable(Drawable appIconDrawable) {
        this.appIconDrawable = appIconDrawable;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppTitle() {
        if (StringUtils.isEmpty(this.appTitle)) {
            return "";
        }
        return this.appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppType() {
        if (StringUtils.isEmpty(this.appType)) {
            return "未分类";
        }
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppPackageName() {
        return this.appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppDescription() {
        if (StringUtils.isEmpty(this.appDescription)) {
            return "未描述信息";
        }
        return this.appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppPublishTime() {
        return this.appPublishTime;
    }

    public void setAppPublishTime(String appPublishTime) {
        this.appPublishTime = appPublishTime;
    }

    public String getApppackageSize() {
        return this.apppackageSize;
    }

    public void setApppackageSize(String apppackageSize) {
        this.apppackageSize = apppackageSize;
    }

    public String getAppLogoUrl() {
        if (StringUtils.isEmpty(this.appLogoUrl)) {
            return null;
        }
        return this.appLogoUrl;
    }

    public void setAppLogoUrl(String appLogoUrl) {
        this.appLogoUrl = appLogoUrl;
    }

    public String getAppDownloadUrl() {
        return this.appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public String getAppRating() {
        if (StringUtils.isEmpty(this.appRating)) {
            return "0";
        }
        return this.appRating;
    }

    public void setAppRating(String appRating) {
        this.appRating = appRating;
    }

    public List<String> getAppImageUrl() {
        if (this.appImageUrl == null || this.appImageUrl.size() == 0) {
            return null;
        }
        return this.appImageUrl;
    }

    public void setAppImageUrl(List<String> appImageUrl) {
        this.appImageUrl = appImageUrl;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAppNum() {
        if (StringUtils.isEmpty(this.appNum)) {
            return "暂无下载次数";
        }
        return this.appNum;
    }

    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAdverImageUrl() {
        return this.adverImageUrl;
    }

    public void setAdverImageUrl(String adverImageUrl) {
        this.adverImageUrl = adverImageUrl;
    }

    public String toString() {
        return "AppInfo [appId=" + this.appId + ", appTitle=" + this.appTitle + ", appType=" + this.appType + ", appPackageName=" + this.appPackageName + ", appDescription=" + this.appDescription + ", appPublishTime=" + this.appPublishTime + ", apppackageSize=" + this.apppackageSize + ", appLogoUrl=" + this.appLogoUrl + ", appDownloadUrl=" + this.appDownloadUrl + ", appRating=" + this.appRating + ", appImageUrl=" + this.appImageUrl + AlbumEnterFactory.SIGN_STR;
    }
}
