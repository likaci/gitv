package com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload;

import com.alibaba.fastjson.annotation.JSONField;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.io.Serializable;

public class PromotionAppInfo implements Serializable {
    private static final String TAG = "PromotionAppInfo";
    private static final long serialVersionUID = 2893044670749101536L;
    @JSONField(name = "app_download_url")
    private String appDownloadUrl;
    @JSONField(name = "app_package_size")
    private int appPakSize;
    @JSONField(name = "app_package_name")
    private String appPckName;
    private int appType = -1;
    @JSONField(name = "app_ver_code")
    private int appVerCode;
    @JSONField(name = "app_ver_name")
    private String appVerName;
    @JSONField(name = "icon")
    private String icon;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "md5")
    private String md5;

    public int getAppType() {
        return this.appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getAppVerCode() {
        return this.appVerCode;
    }

    public void setAppVerCode(int appVerCode) {
        this.appVerCode = appVerCode;
    }

    public int getAppPakSize() {
        return this.appPakSize;
    }

    public void setAppPakSize(int appPakSize) {
        this.appPakSize = appPakSize;
    }

    public String getAppPckName() {
        return this.appPckName;
    }

    public void setAppPckName(String appPckName) {
        this.appPckName = appPckName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppDownloadUrl() {
        return this.appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public String getAppVerName() {
        return this.appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PromotionAppInfo)) {
            return super.equals(obj);
        }
        return this.md5.equals(((PromotionAppInfo) obj).getMd5());
    }

    public int hashCode() {
        return this.md5.hashCode();
    }

    public boolean validationData() {
        if (StringUtils.isEmpty(getAppPckName())) {
            LogRecordUtils.loge(TAG, "validationData: packageName is null.");
            return false;
        } else if (StringUtils.isEmpty(getAppDownloadUrl())) {
            LogRecordUtils.loge(TAG, "validationData: downloadUrl is null.");
            return false;
        } else if (StringUtils.isEmpty(getMd5())) {
            LogRecordUtils.loge(TAG, "validationData: md5 is null.");
            return false;
        } else if (!StringUtils.isEmpty(getAppVerName())) {
            return true;
        } else {
            LogRecordUtils.loge(TAG, "validationData: version is null.");
            return false;
        }
    }

    public String toString() {
        return "PromotionAppInfo{appVerCode=" + this.appVerCode + ", appPakSize=" + this.appPakSize + ", appPckName='" + this.appPckName + '\'' + ", id=" + this.id + ", appDownloadUrl='" + this.appDownloadUrl + '\'' + ", appVerName='" + this.appVerName + '\'' + ", md5='" + this.md5 + '\'' + ", icon='" + this.icon + '\'' + '}';
    }
}
