package com.gala.video.app.epg.ui.netdiagnose.provider;

import android.content.Context;
import android.os.Build;
import com.gala.report.core.upload.IFeedbackResultListener;
import com.gala.report.core.upload.config.LogRecordConfigUtils;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.sdk.player.ISdkError;
import com.gala.video.app.epg.ui.netdiagnose.provider.NDBaseProvider.INetDiagnoseResultListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.PingConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.project.Project;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetDiagnoseUploader {
    private static final String[] DOMAIN_NAME_101 = new String[]{"cache.video.ptqy.gitv.tv", PingConfig.DATA_VIDEO_PTQY_GITV_TV};
    private static final String[] DOMAIN_NAME_102 = new String[]{"cache.m.ptqy.gitv.tv", PingConfig.DATA_VIDEO_PTQY_GITV_TV};
    private static final String LOG_TAG = "EPG/NetDiagnoseUploader";
    private static NetDiagnoseUploader mInstance;
    private Context mContext = AppRuntimeEnv.get().getApplicationContext();
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private boolean mFilterFlag;
    private INetDiagnoseResultListener mNetDiagnoseResultListener = new INetDiagnoseResultListener() {
        public void onReslut(String info) {
            NetDiagnoseUploader.this.mFilterFlag = false;
            LogUtils.d(NetDiagnoseUploader.LOG_TAG, ">>>>> net diagnose finished");
            NetDiagnoseUploader.this.uploadNetInfoToFeedback(info);
        }
    };

    private NetDiagnoseUploader() {
    }

    public static NetDiagnoseUploader getInstance() {
        if (mInstance == null) {
            mInstance = new NetDiagnoseUploader();
        }
        return mInstance;
    }

    public void doPingNs(final PingNslookupProvider provider) {
        if (provider == null) {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs - return, PingNslookupProvider is null");
        } else if (this.mExecutor == null) {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs - return, ExecutorService is null");
        } else if (this.mFilterFlag) {
            LogUtils.e(LOG_TAG, ">>>>> doPingNs - return, last time Net Diagnose is not finish");
        } else {
            provider.setNetDiagnoseResultListener(this.mNetDiagnoseResultListener);
            if (this.mExecutor.isShutdown()) {
                LogUtils.d(LOG_TAG, ">>>>> ExecutorService has already shutdown");
                this.mExecutor = Executors.newSingleThreadExecutor();
            }
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    NetDiagnoseUploader.this.mFilterFlag = true;
                    provider.startCheckEx();
                }
            });
        }
    }

    private PingNslookupProvider getPingNsProviderByUrl(String url) {
        try {
            String host = new URL(url).getHost();
            LogUtils.d(LOG_TAG, ">>>>> doPingNs[url] - sdkError null, ping URL(url).getHost(), ", host);
            return new PingNslookupProvider(this.mContext, new String[]{host});
        } catch (MalformedURLException e) {
            LogUtils.e(LOG_TAG, ">>>>> doPingNs[all] - sdkError null, URL(url).getHost() error");
            return new PingNslookupProvider(this.mContext);
        }
    }

    private PingNslookupProvider getPingNsProviderByError(String errorcode, String url) {
        if (StringUtils.isEmpty((CharSequence) errorcode) && StringUtils.isEmpty((CharSequence) url)) {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs[all] - sdkError null, errorcode is null, url is null");
            return new PingNslookupProvider(this.mContext);
        } else if (!"-50".equals(errorcode) && !"-100".equals(errorcode) && !"HTTP_ERR_-50".equals(errorcode) && !"HTTP_ERR_-100".equals(errorcode)) {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs[no] - sdkError null, errorcode is ", errorcode);
            return null;
        } else if (!StringUtils.isEmpty((CharSequence) url)) {
            return getPingNsProviderByUrl(url);
        } else {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs[all] - sdkError null, errorcode is -50 / -100, url is null");
            return new PingNslookupProvider(this.mContext);
        }
    }

    private PingNslookupProvider getPingNsProviderBySdkError(ISdkError sdkError) {
        int module = sdkError.getModule();
        LogUtils.d(LOG_TAG, ">>>>> doPingNs - sdkError module is ", Integer.valueOf(module));
        if (module == 101) {
            return new PingNslookupProvider(this.mContext, DOMAIN_NAME_101);
        }
        if (module == 102) {
            return new PingNslookupProvider(this.mContext, DOMAIN_NAME_102);
        }
        return new PingNslookupProvider(this.mContext);
    }

    public void doTotalNetDiagnose() {
        if (this.mExecutor == null) {
            LogUtils.e(LOG_TAG, ">>>>> doTotalNetDiagnose - return, ExecutorService is null");
        } else if (this.mFilterFlag) {
            LogUtils.e(LOG_TAG, ">>>>> doTotalNetDiagnose - return, last time Net Diagnose is not finish");
        } else {
            LogUtils.d(LOG_TAG, ">>>>> doTotalNetDiagnose");
            final NetDiagnoseProvider provider = new NetDiagnoseProvider(this.mContext);
            provider.setNetDiagnoseResultListener(this.mNetDiagnoseResultListener);
            if (this.mExecutor.isShutdown()) {
                LogUtils.d(LOG_TAG, ">>>>> doTotalNetDiagnose - ExecutorService has already shutdown");
                this.mExecutor = Executors.newSingleThreadExecutor();
            }
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    NetDiagnoseUploader.this.mFilterFlag = true;
                    provider.startCheckEx();
                }
            });
        }
    }

    public PingNslookupProvider getPingNsProvider(ISdkError sdkError, String errorcode, String url) {
        if (sdkError == null) {
            return getPingNsProviderByError(errorcode, url);
        }
        return getPingNsProviderBySdkError(sdkError);
    }

    public void doNetDiagnoseToAutoTracker(final PingNslookupProvider provider, INetDiagnoseResultListener listener) {
        if (provider == null) {
            LogUtils.d(LOG_TAG, ">>>>> doPingNs - return, PingNslookupProvider is null");
        } else if (this.mExecutor == null) {
            LogUtils.d(LOG_TAG, ">>>>> doNetDiagnoseToAutoTracker - return, ExecutorService is null");
        } else if (this.mFilterFlag) {
            LogUtils.e(LOG_TAG, ">>>>> doNetDiagnoseToAutoTracker - return, last time Net Diagnose is not finish");
        } else {
            provider.setNetDiagnoseResultListener(listener);
            if (this.mExecutor.isShutdown()) {
                LogUtils.d(LOG_TAG, ">>>>> doNetDiagnoseToAutoTracker - ExecutorService has already shutdown");
                this.mExecutor = Executors.newSingleThreadExecutor();
            }
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    NetDiagnoseUploader.this.mFilterFlag = true;
                    provider.startCheckEx();
                }
            });
        }
    }

    private void uploadNetInfoToFeedback(String info) {
        UploadExtraMap extraMap = new UploadExtraMap();
        UploadOptionMap optionMap = new UploadOptionMap();
        extraMap.setOtherInfo(info);
        optionMap.setIsUploadtrace(false);
        optionMap.setIsUploadlogcat(false);
        GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap), GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap), new RecorderBuilder(RecorderType._FEEDBACK, RecorderLogType.LOGRECORD_NETDINOSE_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setIddRecord(LogRecordConfigUtils.getGlobalConfig().getString()).build(), new IFeedbackResultListener() {
            public void beginsendLog() {
            }

            public void lastsendNotComplete() {
            }

            public void sendReportSuccess(String s, String s1) {
                LogUtils.d(NetDiagnoseUploader.LOG_TAG, ">>>>> send net diagnose info to feedback success! shutdown ThreadPoolExecutor");
                GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                NetDiagnoseUploader.this.mExecutor.shutdown();
            }

            public void sendReportFailed(String s) {
                LogUtils.e(NetDiagnoseUploader.LOG_TAG, ">>>>> send net diagnose info to feedback failed!  shutdown ThreadPoolExecutor");
                GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                NetDiagnoseUploader.this.mExecutor.shutdown();
            }
        });
    }

    public void setFilterFlag(boolean value) {
        this.mFilterFlag = value;
    }

    public void shutDownExecutor() {
        if (this.mExecutor != null && !this.mExecutor.isShutdown()) {
            this.mExecutor.shutdown();
        }
    }
}
