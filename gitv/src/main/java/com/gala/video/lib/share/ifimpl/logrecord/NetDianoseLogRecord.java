package com.gala.video.lib.share.ifimpl.logrecord;

import android.content.Context;
import android.os.Build;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDWrapperOperate;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseController;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.project.Project;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetDianoseLogRecord {
    private static final String TAG = "NetDianoseLogRecord";
    private INDDoneListener mCdnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> map) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mNetConnListener");
            }
        }
    };
    private INDDoneListener mCollectListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mCollectListener");
            }
            NetDianoseLogRecord.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            NetDianoseLogRecord.this.stopCheck();
        }
    };
    private Context mContext;
    NetDiagnoseController mController;
    private INDDoneListener mDnsListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mDnsListener");
            }
            NetDianoseLogRecord.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
        }
    };
    private INDDoneListener mNetConnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> map) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mNetConnListener");
            }
        }
    };
    private INDDoneListener mNsLookListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mNsLookListener");
            }
            NetDianoseLogRecord.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
        }
    };
    private NetDiagnoseInfo mResult;
    private INDDoneListener mThirdSpeedListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> map) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mThirdSpeedListener");
            }
        }
    };
    private INDDoneListener mTraceRouteListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> map) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mTraceRouteListener");
            }
        }
    };
    private INDUploadCallback mUploadcallback = new INDUploadCallback() {
        public void uploadNetDiagnoseDone() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDianoseLogRecord.TAG, "mUploadcallback uploadNetDiagnoseDone");
            }
        }
    };
    private List<INDWrapperOperate> mWrapperList;
    private String messagePushID;

    public NetDianoseLogRecord(Context context, String pushID) {
        this.mContext = context;
        this.messagePushID = pushID;
        initData();
        startCheck();
    }

    private void initData() {
        NetDiagnoseInfo diagnoseInfo = null;
        if (null == null) {
            diagnoseInfo = new CDNNetDiagnoseInfo(GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), GetInterfaceTools.getIGalaAccountManager().getUID(), GetInterfaceTools.getIGalaAccountManager().getUserType(), 0);
        }
        this.mController = new NetDiagnoseController(this.mContext, diagnoseInfo);
        if (ListUtils.isEmpty(this.mWrapperList)) {
            this.mWrapperList = new ArrayList();
        }
        this.mWrapperList.clear();
        this.mWrapperList.add(NetDiagnoseCheckTools.getNetConnWrapper(diagnoseInfo, this.mNetConnListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getCdnWrapper(diagnoseInfo, 0, this.mCdnListener, this.mUploadcallback));
        this.mWrapperList.add(NetDiagnoseCheckTools.getThirdSpeedWrapper(diagnoseInfo, this.mThirdSpeedListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getTracerouteWrapper(diagnoseInfo, this.mTraceRouteListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getDnsWrapper(diagnoseInfo, this.mDnsListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getNsLookupWrapper(diagnoseInfo, this.mNsLookListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getCollectInfoWrapper(diagnoseInfo, this.mCollectListener));
    }

    private void startCheck() {
        this.mController.startCheckEx(this.mWrapperList);
    }

    private void stopCheck() {
        this.mController.stopCheck();
        uploadAutoResult(this.messagePushID);
    }

    private void setReslut(NetDiagnoseInfo result) {
        this.mResult = result;
    }

    public void uploadAutoResult(String pushID) {
        if (this.mResult != null) {
            GetInterfaceTools.getILogRecordProvider().notifySaveLogFile();
            String mCdnJsonResult = this.mResult.getCollectionResult();
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "uploadResult jsonResult size=" + (mCdnJsonResult != null ? Integer.valueOf(mCdnJsonResult.length()) : "NULL"));
            }
            if (!(mCdnJsonResult == null || this.mController.getNetDoctor() == null)) {
                this.mController.getNetDoctor().sendLogInfo(mCdnJsonResult);
            }
            StringBuilder info = new StringBuilder();
            info.append("---------------3rd Speed Test---------------\r\n");
            info.append(this.mResult.getThirdSpeedTestResult());
            info.append("---------------\r\n\n");
            info.append("---------------IGala CDN Test---------------\r\n");
            info.append("IGALA CDN Speed Test, Average = " + getSpeedDisplay(this.mResult.getCdnDiagnoseAvgSpeed()));
            info.append("\r\n---------------\r\n\n");
            info.append("---------------CDN Result---------------\r\n");
            info.append("CDN Result " + mCdnJsonResult);
            info.append("\r\n---------------\r\n\n");
            info.append("---------------Trace Route Test---------------\r\n");
            info.append(this.mResult.getTracerouteResult());
            info.append("---------------\r\n\n");
            info.append("---------------DNS detect Test---------------\r\n");
            info.append(this.mResult.getDnsResult());
            info.append("---------------\r\n\n");
            info.append("---------------NS look up Test---------------\r\n");
            info.append(this.mResult.getNslookupResult());
            info.append("---------------\r\n\n");
            info.append(this.mResult.getCollectionResult());
            UploadOptionMap optionMap = new UploadOptionMap();
            UploadExtraMap extraMap = new UploadExtraMap();
            extraMap.setExtraInfo(pushID);
            extraMap.setOtherInfo(info.toString());
            optionMap.setIsUploadtrace(true);
            UploadExtraInfo feedbackExtraInfo = GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap);
            UploadOption feedbackOption = GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap);
            LogUtils.d(TAG, ">>>>> sendRecorder RecorderType = ", new RecorderBuilder(RecorderType._FEEDBACK_AUTO, RecorderLogType.LOGRECORD_REPORT_AUTO, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setMessagePushID(pushID).build().getRecorderType().toString(), " , all listener is null");
            GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(feedbackExtraInfo, feedbackOption, recorder, null);
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "uploadAutoResult mResult is null!");
        }
    }

    private String getSpeedDisplay(int kb) {
        if (kb <= 1024) {
            return kb + "Kb/s ";
        }
        return new DecimalFormat("0.0").format((double) (((float) kb) / 1024.0f)) + "Mb/s ";
    }
}
