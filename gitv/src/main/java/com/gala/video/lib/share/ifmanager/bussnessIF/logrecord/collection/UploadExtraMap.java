package com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.util.HashMap;
import java.util.Map;

public class UploadExtraMap {
    private final String ADSLOG = "ADSLOG";
    private final String APKINFO = "APKINFO";
    private final String CLOG = "CLOG";
    private final String EXTRAINFO = "EXTRAINFO";
    private final String OTHERINFO = "OTHERINFO";
    private final String TVAPIRECORD = "TVAPIRECORD";
    private Map<String, Object> mUploadExtraMap = new HashMap();

    public UploadExtraMap() {
        setTvapiRecord(LogRecordUtils.getTVApiRecord());
        setApkInfo(LogRecordUtils.getDevicesInfo(AppRuntimeEnv.get().getApplicationContext()));
    }

    public Map<String, Object> getUploadExtraMap() {
        return this.mUploadExtraMap;
    }

    private void setTvapiRecord(String tvapiRecord) {
        this.mUploadExtraMap.put("TVAPIRECORD", tvapiRecord);
    }

    private void setApkInfo(String apkInfo) {
        this.mUploadExtraMap.put("APKINFO", apkInfo);
    }

    public void setClog(String clog) {
        this.mUploadExtraMap.put("CLOG", clog);
    }

    public void setExtraInfo(String extrainfo) {
        this.mUploadExtraMap.put("EXTRAINFO", extrainfo);
    }

    public void setOtherInfo(String otherInfo) {
        this.mUploadExtraMap.put("OTHERINFO", otherInfo);
    }

    public void setAdsLog(String adsLog) {
        this.mUploadExtraMap.put("ADSLOG", adsLog);
    }
}
