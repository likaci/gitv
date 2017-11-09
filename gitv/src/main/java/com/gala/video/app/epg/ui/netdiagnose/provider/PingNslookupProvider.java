package com.gala.video.app.epg.ui.netdiagnose.provider;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import java.util.Map;

public class PingNslookupProvider extends NDBaseProvider {
    private static final String TAG = "PingNslookupProvider";
    private INDDoneListener mNsLookUp;
    private String[] mNsLookupUrls;
    private INDDoneListener mPingCallBack;
    private String[] mPingUrl;

    public PingNslookupProvider(Context context) {
        super(context);
        this.mPingCallBack = new INDDoneListener() {
            public void onFinish(Map<String, Object> resultMap) {
                PingNslookupProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
                LogUtils.i(PingNslookupProvider.TAG, "onFinish mDnsListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
            }
        };
        this.mNsLookUp = new INDDoneListener() {
            public void onFinish(Map<String, Object> resultMap) {
                PingNslookupProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
                LogUtils.i(PingNslookupProvider.TAG, "onFinish mNsLookUp success: " + ((Boolean) resultMap.get("success")).booleanValue());
                PingNslookupProvider.this.uploadResult();
            }
        };
    }

    public PingNslookupProvider(Context context, String[] pingUrl) {
        this(context);
        this.mPingUrl = pingUrl;
    }

    public void setPingUrl(String[] pingUrl) {
        this.mPingUrl = pingUrl;
    }

    public void setNsLookupUrls(String[] nsLookupUrls) {
        this.mNsLookupUrls = nsLookupUrls;
    }

    public void initWrapperList() {
        addWrapperJob(NetDiagnoseCheckTools.getPingWrapper(this.mNetDiagnoseInfo, this.mPingCallBack, true, this.mPingUrl));
        addWrapperJob(NetDiagnoseCheckTools.getDynamicNsLookupWrapper(this.mNetDiagnoseInfo, this.mNsLookUp, true, this.mNsLookupUrls));
    }

    public void uploadResultInfo(StringBuilder info) {
        info.append("\n---------------ping Test---------------\r\n");
        info.append(this.mResult.getPingResult());
        info.append(NDBaseProvider.ENDSTRING);
        info.append("---------------NS look up Test---------------\r\n");
        info.append(this.mResult.getNslookupResult());
        info.append(NDBaseProvider.ENDSTRING);
    }
}
