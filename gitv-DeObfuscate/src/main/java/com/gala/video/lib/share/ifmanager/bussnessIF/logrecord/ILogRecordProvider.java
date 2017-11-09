package com.gala.video.lib.share.ifmanager.bussnessIF.logrecord;

import android.content.Context;
import com.gala.report.core.log.ILogCore;
import com.gala.report.core.multiprocess.IMultiProcess;
import com.gala.report.core.upload.IFeedbackResultListener;
import com.gala.report.core.upload.IUploadCore;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.msghandler.IMsgHandlerCore;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.utils.TraceEx;

public interface ILogRecordProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements ILogRecordProvider {
        public Object getInterface() {
            return this;
        }

        public static ILogRecordProvider asInterface(Object wrapper) {
            TraceEx.beginSection("ILogRecordProvider.asInterface");
            if (wrapper == null || !(wrapper instanceof ILogRecordProvider)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (ILogRecordProvider) wrapper;
        }
    }

    ILogCore getLogCore();

    IMsgHandlerCore getMsgHandlerCore();

    IMultiProcess getMultiProcess();

    IUploadCore getUploadCore();

    UploadExtraInfo getUploadExtraInfoAndParse(UploadExtraMap uploadExtraMap);

    UploadOption getUploadOptionInfoAndParse(UploadOptionMap uploadOptionMap);

    void initialize(ILogRecordInitListener iLogRecordInitListener);

    boolean isLogRecordEnable();

    boolean isLogRecordFeatureReady();

    void notifySaveLogFile();

    void sendRecorder(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, IFeedbackResultListener iFeedbackResultListener);

    void setLogRecordEnable(boolean z);
}
