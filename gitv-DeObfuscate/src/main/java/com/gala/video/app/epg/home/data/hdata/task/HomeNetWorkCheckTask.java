package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.video.app.epg.home.data.core.ApiExceptionModelFactory;
import com.gala.video.app.epg.home.utils.NetWorkStateHelper;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class HomeNetWorkCheckTask extends BaseRequestTask {
    private static int RETRY_TIMES = 2;
    private static final String TAG = "home/HomeNetWorkCheckTask";
    private static int sRetryCount = 0;
    private boolean mIsSuccess = false;
    private final OnNetStateChangedListener mNetStateChangedListener = new C06431();

    class C06431 implements OnNetStateChangedListener {
        C06431() {
        }

        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    GetInterfaceTools.getStartupDataLoader().forceLoad(true);
                    NetWorkManager.getInstance().unRegisterStateChangedListener(HomeNetWorkCheckTask.this.mNetStateChangedListener);
                    LogUtils.m1571e(HomeNetWorkCheckTask.TAG, "network state changed net state = " + newState);
                    return;
                default:
                    return;
            }
        }
    }

    public HomeNetWorkCheckTask() {
        LogUtils.m1568d(TAG, "home network check task");
    }

    public void invoke() {
        LogUtils.m1568d(TAG, "invoke network check task");
        DeviceCheckModel devResult = DeviceCheckModel.getInstance();
        if (ErrorEvent.C_SUCCESS == devResult.getErrorEvent() && devResult.isDevCheckPass()) {
            LogUtils.m1568d(TAG, "device check is success ,do not need to check network");
            this.mIsSuccess = true;
            return;
        }
        CharSequence apiCode = DeviceCheckModel.getInstance().getApiCode();
        LogUtils.m1571e(TAG, "api code : " + apiCode);
        if (StringUtils.isEmpty(apiCode) || NetWorkStateHelper.isNoNetWorkOnBootUp()) {
            new NetWorkStateHelper().checkNetWorkSync();
            ApiExceptionModelFactory.createDevApiExceptionModel();
            if (!NetWorkStateHelper.isNetWorkConnected()) {
                NetWorkManager.getInstance().registerStateChangedListener(this.mNetStateChangedListener);
            } else if (sRetryCount < RETRY_TIMES && NetWorkStateHelper.isNoNetWorkOnBootUp()) {
                GetInterfaceTools.getStartupDataLoader().forceLoad(true);
                sRetryCount++;
            }
            LogUtils.m1571e(TAG, "home network check is finished");
            return;
        }
        ApiExceptionModelFactory.createDevApiExceptionModel();
    }

    public void onOneTaskFinished() {
        if (this.mIsSuccess) {
            GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.DEVICE_CHECK_FISNISHED_EVENT);
        }
        GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_ERROR_EVENT);
    }
}
