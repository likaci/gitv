package com.gala.video.app.epg.feedback;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.MonkeyUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedbackData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackKeyProcess.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider;
import com.gala.video.lib.share.project.Project;

class FeedbackKeyProcessor extends Wrapper {
    private static final int COUNT_TIMES = 5;
    private static final String TAG = "FeedbackKeyProcessor";
    private static int tempCount = 1;
    private static long tempTimes = 0;

    FeedbackKeyProcessor() {
    }

    public void dispatchKeyEvent(KeyEvent event, Context context) {
        ILogRecordProvider provider = GetInterfaceTools.getILogRecordProvider();
        if (provider.isLogRecordEnable() && event.getKeyCode() == 82 && event.getAction() == 1) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - tempTimes > 500 || tempTimes == 0) {
                tempCount = 1;
                Log.v(TAG, "tempCount = " + tempCount);
            } else {
                tempCount++;
                Log.v(TAG, "tempCount = " + tempCount);
                if (tempCount == 5) {
                    if (!provider.isLogRecordFeatureReady()) {
                        LogRecordUtils.showLogRecordNotAlreadyToast(context);
                    } else if (Project.getInstance().getBuild().isCheckMonkeyOpen()) {
                        boolean isMonkeyRunning = MonkeyUtils.isMonkeyRunning();
                        Log.v(TAG, "isMonkeyRunning = " + isMonkeyRunning);
                        if (!isMonkeyRunning) {
                            sendFeedback(context);
                        }
                    } else {
                        sendFeedback(context);
                    }
                }
            }
            tempTimes = currentTime;
        }
    }

    private void sendFeedback(Context context) {
        ILogRecordProvider provider = GetInterfaceTools.getILogRecordProvider();
        provider.notifySaveLogFile();
        IFeedbackResultCallback feedbackResultCallback = CreateInterfaceTools.createFeedbackResultListener();
        feedbackResultCallback.init(context);
        FeedbackData model = feedbackResultCallback.makeFeedbackData(null, false);
        feedbackResultCallback.setRecorderType(model.getRecorder().getRecorderType());
        feedbackResultCallback.setPageType(3);
        provider.sendRecorder(context, model.getUploadExtraInfo(), model.getUploadOption(), model.getRecorder(), feedbackResultCallback.getFeedbackResultListener());
    }
}
