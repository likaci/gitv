package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.video.app.epg.home.data.core.ApiExceptionModelFactory;
import com.gala.video.app.epg.home.utils.NetWorkStateHelper;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class NetWorkCheckTask extends BaseRequestTask {
    private static final int RETRY_TIMES = 2;
    private static final String TAG = "home/NetWorkCheckTask";
    private static int sRetryCount = 0;
    private boolean mIsSuccess = false;
    private final OnNetStateChangedListener mNetStateChangedListener = new OnNetStateChangedListener() {
        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    GetInterfaceTools.getStartupDataLoader().forceLoad(false);
                    NetWorkManager.getInstance().unRegisterStateChangedListener(NetWorkCheckTask.this.mNetStateChangedListener);
                    LogUtils.e(NetWorkCheckTask.TAG, "network state changed net state = " + newState);
                    return;
                default:
                    return;
            }
        }
    };

    public NetWorkCheckTask(int taskId) {
        this.mTaskId = taskId;
        LogUtils.d(TAG, "create network check task");
    }

    public void invoke() {
        LogUtils.d(TAG, "invoke network check task");
        DeviceCheckModel devResult = DeviceCheckModel.getInstance();
        if (ErrorEvent.C_SUCCESS == devResult.getErrorEvent() && devResult.isDevCheckPass()) {
            LogUtils.d(TAG, "device check is success ,do not need to check network");
            this.mIsSuccess = true;
            return;
        }
        CharSequence apiCode = DeviceCheckModel.getInstance().getApiCode();
        LogUtils.d(TAG, "api code : " + apiCode);
        if (StringUtils.isEmpty(apiCode) || NetWorkStateHelper.isNoNetWorkOnBootUp()) {
            LogUtils.e(TAG, "app network check result state : " + new NetWorkStateHelper().checkNetWorkSync());
            ApiExceptionModelFactory.createDevApiExceptionModel();
            if (!NetWorkStateHelper.isNetWorkConnected()) {
                NetWorkManager.getInstance().registerStateChangedListener(this.mNetStateChangedListener);
            } else if (sRetryCount <= 2 && NetWorkStateHelper.isNoNetWorkOnBootUp()) {
                GetInterfaceTools.getStartupDataLoader().forceLoad(false);
                sRetryCount++;
            }
            LogUtils.e(TAG, "network check is finished");
            return;
        }
        ApiExceptionModelFactory.createDevApiExceptionModel();
    }

    protected void next() {
        if (this.mIsSuccess) {
            super.next();
        }
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "app start check network task finished");
    }
}
