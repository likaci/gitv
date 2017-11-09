package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.PingConfig;

public class NsLookUpJob extends NetDiagnoseJob {
    private final String TAG;
    private String mNsLookUpUrls;
    protected StringBuilder nsLookupResult;

    public NsLookUpJob(NetDiagnoseInfo data) {
        super(data);
        this.TAG = "NetDiagnoseJob/NsLookup@" + hashCode();
        this.nsLookupResult = new StringBuilder();
    }

    public NsLookUpJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
        this.TAG = "NetDiagnoseJob/NsLookup@" + hashCode();
        this.nsLookupResult = new StringBuilder();
    }

    public NsLookUpJob(NetDiagnoseInfo data, String nsLookUpUrls) {
        this(data);
        this.mNsLookUpUrls = nsLookUpUrls;
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        LogUtils.m1568d(this.TAG, ">> onRun");
        try {
            onRunLookUp();
            ((NetDiagnoseInfo) getData()).setNslookupResult(this.nsLookupResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mIsJobComplete = true;
        notifyJobSuccess(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< onRun");
        }
    }

    protected void onRunLookUp() {
        LogUtils.m1574i(this.TAG, "onRun mNsLookUpUrls: " + this.mNsLookUpUrls);
        if (StringUtils.isTrimEmpty(this.mNsLookUpUrls)) {
            checkDefaultNsLookUp();
        } else if (NetDiagnoseCheckTools.NO_CHECK_FLAG.equals(this.mNsLookUpUrls.trim())) {
            this.nsLookupResult.append("--------no NsLookUp job-------\r\n");
        } else {
            String[] urls = NetDiagnoseCheckTools.getParseUrls(this.mNsLookUpUrls);
            if (StringUtils.isEmpty(urls)) {
                checkDefaultNsLookUp();
                return;
            }
            LogUtils.m1574i(this.TAG, "onRun: use online nsloopup domain");
            for (String url : urls) {
                if (!StringUtils.isEmpty(url.trim())) {
                    this.nsLookupResult.append(DigUtils.digHost(url.trim()));
                    this.nsLookupResult.append("\r\n\r\n");
                }
            }
        }
    }

    protected void checkDefaultNsLookUp() {
        LogUtils.m1574i(this.TAG, "checkDefaultNsLookUp: ");
        this.nsLookupResult.append(DigUtils.digHost(PingConfig.DATA2_ITV));
        this.nsLookupResult.append("\r\n\r\n");
        this.nsLookupResult.append(DigUtils.digHost(PingConfig.ITV_VIDEO));
        this.nsLookupResult.append("\r\n\r\n");
        this.nsLookupResult.append(DigUtils.digHost(PingConfig.CACHE_M));
        this.nsLookupResult.append("\r\n\r\n");
        this.nsLookupResult.append(DigUtils.digHost(PingConfig.CACHE_VIDEO));
        this.nsLookupResult.append("\r\n\r\n");
        this.nsLookupResult.append(DigUtils.digHost(PingConfig.PDATA_VIDEO));
        this.nsLookupResult.append("\r\n\r\n");
    }
}
