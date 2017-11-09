package com.gala.android.sdk.dlna.keeper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DmrInfor implements Serializable {
    private String descriptionFileXml;
    private String fileMd5;
    private Map<String, String> serverMap;
    private String uuid;

    public DmrInfor() {
        this.uuid = "";
        this.fileMd5 = "";
        this.serverMap = new HashMap();
        this.descriptionFileXml = "";
        this.uuid = "";
        this.fileMd5 = "";
        this.serverMap = new HashMap();
        this.descriptionFileXml = "";
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescriptionFileXml() {
        return this.descriptionFileXml;
    }

    public void setDescriptionFileXml(String descriptionFileXml) {
        this.descriptionFileXml = descriptionFileXml;
    }

    public String getFileMd5() {
        return this.fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Map<String, String> getServerMap() {
        return this.serverMap;
    }

    public void setServerMap(Map<String, String> serverMap) {
        this.serverMap = serverMap;
    }
}
