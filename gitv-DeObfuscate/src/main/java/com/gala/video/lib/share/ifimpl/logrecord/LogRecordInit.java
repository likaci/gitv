package com.gala.video.lib.share.ifimpl.logrecord;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.gala.report.ILogRecordFeature;
import com.gala.report.LogRecord;
import com.gala.report.core.log.ILogCore;
import com.gala.report.core.multiprocess.IMultiProcess;
import com.gala.report.core.upload.IUploadCore;
import com.gala.report.core.upload.config.GlobalConfig;
import com.gala.report.core.upload.config.LogRecordConfigUtils;
import com.gala.report.msghandler.IMsgHandlerCore;
import com.gala.report.msghandler.IMsgHandlerListener;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.common.configs.PluginConstants;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.PingConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordInitListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;

class LogRecordInit {
    private static final String LOAD_PLUGIN_THREAD = "load-logrecordplugin";
    private static final int MSG_LOAD_PLUGINB = 1;
    private static final String TAG = "LogRecordInit";
    private int FIRST_LOGRECORD_LOAD = 1;
    private int NOT_FIRST_LOGRECORD_LOAD = 2;
    private boolean isAddToLogcat = false;
    private ILogCore mLogCore;
    private ILogRecordFeature mLogRecordFeature;
    private ILogRecordInitListener mLogRecordInitListener;
    private IMsgHandlerCore mMsgHandlerCore;
    private IMultiProcess mMultiProcess;
    private IUploadCore mUploadCore;
    private WorkHandler mWorkHandler;

    private interface OnStateChangedListener {
        void onFailed();

        void onSuccess();
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            OnStateChangedListener listener = null;
            if (msg.obj != null && (msg.obj instanceof OnStateChangedListener)) {
                listener = msg.obj;
            }
            Log.d(LogRecordInit.TAG, "handle message, logrecordFeature = " + LogRecordInit.this.mLogRecordFeature + ", listener = " + listener);
            if (LogRecordInit.this.mLogRecordFeature == null) {
                LogRecordInit.this.loadLogRecordFeature(msg.arg1);
            }
            if (LogRecordInit.this.mLogRecordFeature != null && listener != null) {
                Log.d(LogRecordInit.TAG, "handle message success");
                listener.onSuccess();
            } else if (listener != null) {
                Log.d(LogRecordInit.TAG, "handle message failed");
                listener.onFailed();
            }
        }
    }

    public LogRecordInit(ILogRecordInitListener listener) {
        HandlerThread loadPluginThread = new HandlerThread(LOAD_PLUGIN_THREAD);
        loadPluginThread.start();
        this.mWorkHandler = new WorkHandler(loadPluginThread.getLooper());
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = this.FIRST_LOGRECORD_LOAD;
        this.mWorkHandler.sendMessage(msg);
        this.mLogRecordInitListener = listener;
    }

    public ILogRecordFeature getLogRecordFeature() {
        if (this.mLogRecordFeature == null) {
            final ConditionVariable lock = new ConditionVariable();
            loadLogRecordPluginAsync(1, new OnStateChangedListener() {
                public void onSuccess() {
                    Log.d(LogRecordInit.TAG, "get LogRecordInit success, lock open");
                    lock.open();
                }

                public void onFailed() {
                    Log.d(LogRecordInit.TAG, "get LogRecordInit fail, lock open");
                    lock.open();
                }
            });
            Log.d(TAG, " get LogRecordInit, lock block");
            lock.block();
        }
        return this.mLogRecordFeature;
    }

    private void loadLogRecordPluginAsync(int what, OnStateChangedListener listener) {
        Log.d(TAG, "loadLogRecordPluginAsync what=" + what + ", listener=" + listener);
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = listener;
        msg.arg1 = this.NOT_FIRST_LOGRECORD_LOAD;
        this.mWorkHandler.sendMessage(msg);
    }

    private void loadLogRecordFeature(int loadCount) {
        Log.d(TAG, "load logrecord feature, mLogRecordFeature = " + this.mLogRecordFeature);
        ILogRecordFeature feature = null;
        try {
            long start = SystemClock.uptimeMillis();
            Result<AbsPluginProvider> result = PluginManager.instance().loadProvider(new HostPluginInfo(PluginConstants.LOGRECORDPLUGIN_ID, Project.getInstance().getBuild().getVersionString()));
            new LogRecordPingback(PluginManager.instance(), loadCount).sendLoadPluginPingback(result, PluginConstants.LOGRECORDPLUGIN_ID, SystemClock.uptimeMillis() - start);
            if (result.getData() != null) {
                feature = (ILogRecordFeature) ((AbsPluginProvider) result.getData()).getFeature(10);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadPluginLogRecordFeature() fail!", e);
        }
        this.mLogRecordFeature = feature;
        Log.d(TAG, "loadPluginLogRecordFeature: mLogRecordFeature=" + this.mLogRecordFeature);
        initializeLogRecord(AppRuntimeEnv.get().getApplicationContext(), this.mLogRecordFeature);
    }

    private void initializeLogRecord(final Context context, ILogRecordFeature feature) {
        Log.d(TAG, "initializeLogRecord begin: " + feature);
        if (feature != null) {
            this.mLogCore = feature.getLogCore();
            this.mUploadCore = feature.getUploadCore();
            this.mUploadCore.init(context, Project.getInstance().getBuild().getPackageName());
            this.mMultiProcess = feature.getMultiProcess();
            this.mMsgHandlerCore = feature.getMsgHandlerCore(this.mUploadCore, new IMsgHandlerListener() {
                public void executeNetdinose(String s) {
                    NetDianoseLogRecord netDianoseLogRecord = new NetDianoseLogRecord(context, s);
                }
            });
            LogRecord.setLogCore(this.mLogCore);
            LogRecord.setMsgHandlerCore(this.mMsgHandlerCore);
            initLogRecord(AppRuntimeEnv.get().getApplicationContext());
            Log.d(TAG, "initializeLogRecord end: " + feature);
        }
    }

    private void initLogRecord(Context context) {
        this.mLogCore.init(context, GetInterfaceTools.getIInit().isMainProcess(), new LogListener(context), this.isAddToLogcat);
        PingConfig.config(Project.getInstance().getBuild().getDomainName());
        GlobalConfig config = new GlobalConfig();
        config.setUuid(Project.getInstance().getBuild().getVrsUUID()).setAndroidModel(Build.MODEL.replace(" ", "-")).setAndroidVerion(VERSION.RELEASE).setAppVersion(Project.getInstance().getBuild().getVersionString()).setHardwareInfo(Build.MODEL).setHcdnStatus(SystemConfigPreference.isOpenHCDN(AppRuntimeEnv.get().getApplicationContext())).setMac(DeviceUtils.getMacAddr());
        LogRecordConfigUtils.setGlobalConfig(config);
        LogRecordUtils.setLogRecordInit(true);
        String nowMsg = LogRecordUtils.getMsg();
        if (!nowMsg.equals("")) {
            Log.v(TAG, "LogRecordMsgEntity.getInstance().getMsg() is not empty = " + nowMsg);
            this.mMsgHandlerCore.sendPushMessage(nowMsg);
            this.mMsgHandlerCore.sendHostStatus(HOSTMODULE.LOGMSGPUSH, HOSTSTATUS.END);
        }
        this.mMsgHandlerCore.sendHostStatus(HOSTMODULE.APP, HOSTSTATUS.START);
        this.mLogRecordInitListener.completed();
    }

    public IMsgHandlerCore getMsgHandlerCore() {
        return this.mMsgHandlerCore;
    }

    public ILogCore getLogCore() {
        return this.mLogCore;
    }

    public IUploadCore getUploadCore() {
        return this.mUploadCore;
    }

    public IMultiProcess getMultiProcess() {
        return this.mMultiProcess;
    }
}
