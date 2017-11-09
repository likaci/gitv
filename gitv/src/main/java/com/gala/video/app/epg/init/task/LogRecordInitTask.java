package com.gala.video.app.epg.init.task;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.preference.LogRecordPreference;
import com.gala.video.lib.share.ifimpl.logrecord.utils.CrashUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordInitListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.project.Project;
import com.tvos.apps.utils.DateUtil;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogRecordInitTask implements Runnable {
    private static final String TAG = "LogRecordInitTask";

    public void run() {
        LogUtils.d(TAG, "LogRecordInitTask execute");
        new Thread8K(new Runnable() {
            public void run() {
                if (LogRecordPreference.getLogrecordOpen(AppRuntimeEnv.get().getApplicationContext())) {
                    final IFeedbackResultCallback feedbackResultCallback = CreateInterfaceTools.createFeedbackResultListener();
                    feedbackResultCallback.init(AppRuntimeEnv.get().getApplicationContext());
                    GetInterfaceTools.getILogRecordProvider().initialize(new ILogRecordInitListener() {
                        public void completed() {
                            if (Project.getInstance().getBuild().isInitCrashHandler()) {
                                LogRecordInitTask.this.sendCrashReport(AppRuntimeEnv.get().getApplicationContext(), feedbackResultCallback);
                            }
                        }
                    });
                    LogUtils.d(LogRecordInitTask.TAG, "LogRecordFeatureProvider execute");
                    return;
                }
                LogUtils.d(LogRecordInitTask.TAG, "LogRecordPreference.getLogrecordOpen is false");
            }
        }, TAG).start();
    }

    public void sendCrashReport(Context context, IFeedbackResultCallback callback) {
        File file = CrashUtils.getCrashFile();
        int mUploadTimes = LogRecordPreference.getTodayUploadTimes(context);
        DateFormat dateFormat = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10X);
        if (CrashUtils.isNextDay(context, dateFormat)) {
            LogRecordPreference.saveFirstCrashTime(context, dateFormat.format(new Date()));
            mUploadTimes = 0;
            LogRecordPreference.saveTodayUploadTimes(context, 0);
        }
        if (file != null && file.exists() && mUploadTimes <= 5) {
            mUploadTimes++;
            LogRecordPreference.saveTodayUploadTimes(context, mUploadTimes);
            Log.v(TAG, "mUploadTimes = " + mUploadTimes);
            String str = "";
            String str2 = "";
            String str3 = "";
            String crashMeminfo = "";
            try {
                str = LogRecordPreference.getCrashType(AppRuntimeEnv.get().getApplicationContext());
                str2 = LogRecordPreference.getException(AppRuntimeEnv.get().getApplicationContext());
                str3 = LogRecordPreference.getCrashDetail(AppRuntimeEnv.get().getApplicationContext());
                crashMeminfo = LogRecordPreference.getCrashMeminfo(AppRuntimeEnv.get().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v(TAG, "crashType = " + str);
            Log.v(TAG, "exception = " + str2);
            Log.v(TAG, "crashDetail = " + str3);
            Recorder recorder = new RecorderBuilder(RecorderType._CRASH, RecorderLogType.CRASH_REPORT_DEFAULT, LogRecordUtils.getLastApkVersion(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setCrashType(str).setException(str2).setCrashDetail(str3).setFile(file).build();
            UploadOptionMap optionMap = new UploadOptionMap();
            UploadExtraMap extraMap = new UploadExtraMap();
            extraMap.setExtraInfo(CrashUtils.getCrashReport(context) + "\n" + crashMeminfo + "\n");
            UploadExtraInfo mUploadExtraInfo = GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap);
            UploadOption uploadOption = GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap);
            if (callback != null) {
                callback.setRecorderType(recorder.getRecorderType());
                callback.setUploadOption(uploadOption);
            }
            GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(mUploadExtraInfo, uploadOption, recorder, callback.getFeedbackResultListener());
        } else if (file == null || !file.exists()) {
            Log.v(TAG, "file is not exist ");
        } else {
            Log.v(TAG, "uploadTimes is reach max,and mUploadTimes = " + mUploadTimes);
            file.delete();
        }
        LogRecordUtils.saveLastApkVersion(Project.getInstance().getBuild().getVersionString());
        Log.v(TAG, "sendCrashReport complete");
    }
}
