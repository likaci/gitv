package com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public class PromotionAppInfoWithDocument implements Serializable {
    private static final long serialVersionUID = -7032241721015473899L;
    @JSONField(name = "app")
    private PromotionAppInfo appInfo;
    @JSONField(name = "entryDocument")
    private String entryDocument;

    public PromotionAppInfo getAppInfo() {
        return this.appInfo;
    }

    public void setAppInfo(PromotionAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public String getEntryDocument() {
        return this.entryDocument;
    }

    public void setEntryDocument(String entryDocument) {
        this.entryDocument = entryDocument;
    }

    public String toString() {
        return "PromotionAppInfoWithDocument{appInfo=" + this.appInfo + ", entryDocument='" + this.entryDocument + '\'' + '}';
    }
}
