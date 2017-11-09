package com.gala.report;

import com.gala.report.core.log.ILogCore;
import com.gala.report.core.multiprocess.IMultiProcess;
import com.gala.report.core.upload.IUploadCore;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.msghandler.IMsgHandlerCore;
import com.gala.report.msghandler.IMsgHandlerListener;

public interface ILogRecordFeature {
    public static final int FEATURE_LOGRECORD = 10;

    ILogCore getLogCore();

    IMsgHandlerCore getMsgHandlerCore(IUploadCore iUploadCore, IMsgHandlerListener iMsgHandlerListener);

    IMultiProcess getMultiProcess();

    IUploadCore getUploadCore();

    UploadExtraInfo getUploadExtraInfo();

    UploadOption getUploadOption();
}
