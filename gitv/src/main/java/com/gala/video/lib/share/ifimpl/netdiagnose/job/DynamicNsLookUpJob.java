package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;

public class DynamicNsLookUpJob extends NsLookUpJob {
    private final String TAG;
    private String[] mNsLookupUrls;

    public DynamicNsLookUpJob(NetDiagnoseInfo data) {
        super(data);
        this.TAG = "NetDiagnoseJob/DynamicNsLookUpJob@" + hashCode();
    }

    public DynamicNsLookUpJob(NetDiagnoseInfo data, String[] nsLookupUrls) {
        this(data);
        this.mNsLookupUrls = nsLookupUrls;
    }

    public void setNsLookupUrls(String[] nsLookupUrls) {
        this.mNsLookupUrls = nsLookupUrls;
    }

    protected void onRunLookUp() {
        LogUtils.i(this.TAG, "onRunLookUp mNsLookUpUrls: " + this.mNsLookupUrls);
        if (StringUtils.isEmpty(this.mNsLookupUrls)) {
            checkDefaultNsLookUp();
            return;
        }
        for (String url : this.mNsLookupUrls) {
            if (!StringUtils.isEmpty(url.trim())) {
                this.nsLookupResult.append(DigUtils.digHost(url.trim()));
                this.nsLookupResult.append("\r\n\r\n");
            }
        }
    }
}
