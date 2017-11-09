package com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection;

import java.util.HashMap;
import java.util.Map;

public class UploadOptionMap {
    private final String ISUPLOADADSLOG = "ISUPLOADADSLOG";
    private final String ISUPLOADGALABUFFER = "ISUPLOADGALABUFFER";
    private final String ISUPLOADLOGCAT = "ISUPLOADLOGCAT";
    private final String ISUPLOADTRACE = "ISUPLOADTRACE";
    private Map<String, Object> mUploadOptionMap = new HashMap();

    public Map<String, Object> getUploadOptionMap() {
        return this.mUploadOptionMap;
    }

    public void setIsUploadlogcat(boolean isuploadlogcat) {
        this.mUploadOptionMap.put("ISUPLOADLOGCAT", Boolean.valueOf(isuploadlogcat));
    }

    public void setIsUploadGalabuffer(boolean isUploadGalabuffer) {
        this.mUploadOptionMap.put("ISUPLOADGALABUFFER", Boolean.valueOf(isUploadGalabuffer));
    }

    public void setIsUploadtrace(boolean isUploadtrace) {
        this.mUploadOptionMap.put("ISUPLOADTRACE", Boolean.valueOf(isUploadtrace));
    }

    public void setIsUploadAdsLog(boolean isUploadAdsLog) {
        this.mUploadOptionMap.put("ISUPLOADADSLOG", Boolean.valueOf(isUploadAdsLog));
    }
}
