package com.gala.video.app.epg.ui.netdiagnose.provider;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import java.util.Map;

public class NetDiagnoseProvider extends NDBaseProvider {
    private static final String TAG = "NetDiagnoseProvider";
    private INDDoneListener mCdnListener = new C09402();
    private INDDoneListener mCollectListener = new C09413();
    private INDDoneListener mDnsListener = new C09435();
    private INDDoneListener mNetConnListener = new C09391();
    private INDDoneListener mNsLookUp = new C09446();
    private INDDoneListener mThirdSpeedListener = new C09424();

    class C09391 implements INDDoneListener {
        C09391() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.m1574i(NetDiagnoseProvider.TAG, "onFinish mNetConnListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    }

    class C09402 implements INDDoneListener {
        C09402() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.m1574i(NetDiagnoseProvider.TAG, "onFinish mCdnListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    }

    class C09413 implements INDDoneListener {
        C09413() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.m1574i(NetDiagnoseProvider.TAG, "onFinish mCollectListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
            NetDiagnoseProvider.this.uploadResult();
        }
    }

    class C09424 implements INDDoneListener {
        C09424() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
        }
    }

    class C09435 implements INDDoneListener {
        C09435() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.m1574i(NetDiagnoseProvider.TAG, "onFinish mDnsListener success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    }

    class C09446 implements INDDoneListener {
        C09446() {
        }

        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseProvider.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            LogUtils.m1574i(NetDiagnoseProvider.TAG, "onFinish mNsLookUp success: " + ((Boolean) resultMap.get("success")).booleanValue());
        }
    }

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
        LogUtils.m1568d(TAG, "uploadResult jsonResult size=" + (cdnResult != null ? Integer.valueOf(cdnResult.length()) : "NULL"));
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
