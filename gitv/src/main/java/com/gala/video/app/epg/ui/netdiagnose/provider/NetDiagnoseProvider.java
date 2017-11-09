package com.gala.video.app.epg.ui.netdiagnose.provider;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import java.util.Map;

public class NetDiagnoseProvider extends NDBaseProvider {
    private static final String TAG = "NetDiagnoseProvider";
    private INDDoneListener mCdnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.i(NetDiagnoseProvider.TAG, "onFinish mCdnListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    };
    private INDDoneListener mCollectListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.i(NetDiagnoseProvider.TAG, "onFinish mCollectListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
            NetDiagnoseProvider.this.uploadResult();
        }
    };
    private INDDoneListener mDnsListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.i(NetDiagnoseProvider.TAG, "onFinish mDnsListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    };
    private INDDoneListener mNetConnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.i(NetDiagnoseProvider.TAG, "onFinish mNetConnListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    };
    private INDDoneListener mNsLookUp = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.i(NetDiagnoseProvider.TAG, "onFinish mNsLookUp success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    };
    private INDDoneListener mThirdSpeedListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
        }
    };

    public NetDiagnoseProvider(Context context) {
        super(context);
    }

    public void initWrapperList() {
        addWrapperJob(NetDiagnoseCheckTools.getNetConnWrapper(this.mNetDiagnoseInfo, this.mNetConnListener));
        if (this.mPlayerType == 0) {
            addWrapperJob(NetDiagnoseCheckTools.getCdnWrapper(this.mNetDiagnoseInfo, 1, this.mCdnListener, this.mUploadcallback));
            addWrapperJob(NetDiagnoseCheckTools.getCdnWrapper(this.mNetDiagnoseInfo, 2, this.mCdnListener, this.mUploadcallback));
        } else {
            addWrapperJob(NetDiagnoseCheckTools.getCdnWrapper(this.mNetDiagnoseInfo, this.mPlayerType, this.mCdnListener, this.mUploadcallback));
        }
        addWrapperJob(NetDiagnoseCheckTools.getThirdSpeedWrapper(this.mNetDiagnoseInfo, this.mThirdSpeedListener));
        addWrapperJob(NetDiagnoseCheckTools.getDnsWrapper(this.mNetDiagnoseInfo, this.mDnsListener));
        addWrapperJob(NetDiagnoseCheckTools.getNsLookupWrapper(this.mNetDiagnoseInfo, this.mNsLookUp, true));
        addWrapperJob(NetDiagnoseCheckTools.getCollectInfoWrapper(this.mNetDiagnoseInfo, this.mCollectListener, true));
    }

    public void uploadResultInfo(StringBuilder info) {
        String cdnResult = this.mResult.getCdnDiagnoseJsonResult();
        LogUtils.d(TAG, "uploadResult jsonResult size=" + (cdnResult != null ? Integer.valueOf(cdnResult.length()) : "NULL"));
        if (!(cdnResult == null || this.mController.getNetDoctor() == null)) {
            this.mController.getNetDoctor().sendLogInfo(cdnResult);
        }
        info.append("\n---------------3rd Speed Test---------------\r\n");
        info.append(this.mResult.getThirdSpeedTestResult());
        info.append(NDBaseProvider.ENDSTRING);
        info.append("---------------gala CDN Test---------------\r\n");
        info.append("CDN Result " + this.mResult.getCdnDiagnoseJsonResult());
        info.append(NDBaseProvider.ENDSTRING);
        info.append("---------------DNS detect Test---------------\r\n");
        info.append(this.mResult.getDnsResult());
        info.append(NDBaseProvider.ENDSTRING);
        info.append("---------------NS look up Test---------------\r\n");
        info.append(this.mResult.getNslookupResult());
        info.append(NDBaseProvider.ENDSTRING);
        info.append("---------------Collection Test---------------\r\n");
        info.append(this.mResult.getCollectionResult());
        info.append(NDBaseProvider.ENDSTRING);
    }
}
