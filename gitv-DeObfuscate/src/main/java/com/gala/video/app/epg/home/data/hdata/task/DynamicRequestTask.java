package com.gala.video.app.epg.home.data.hdata.task;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.multiscreen.MultiScreenStartTool;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider.ILoadDynamicDataCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;

public class DynamicRequestTask extends BaseRequestTask {
    private static final String NEED_RETURN = "1";
    private static final String TAG = "home/DynamicRequestTask";
    private final int DELAY_ONE_MIN = 60000;
    private Handler mHanlder = new Handler(Looper.getMainLooper());

    class C06381 implements IApiCallback<ApiResultDeviceCheck> {

        class C06371 implements ILoadDynamicDataCallback {
            C06371() {
            }

            public void onSuccess() {
                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT);
            }
        }

        C06381() {
        }

        public void onSuccess(ApiResultDeviceCheck apiResult) {
            LogUtils.m1568d(DynamicRequestTask.TAG, "fetchSwitchData()---onSuccess---");
            DeviceCheck data = apiResult.data;
            if (data != null) {
                GetInterfaceTools.getIDynamicQDataProvider().loadDynamicQData(data, new C06371());
                DynamicRequestTask.this.initializeConfig(data);
                DynamicRequestTask.this.downloadExitImages();
            }
        }

        public void onException(ApiException e) {
            String str;
            LogRecordUtils.setEventID(PingBackUtils.createEventId());
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
            String str2 = "pfec";
            if (e == null) {
                str = "";
            } else {
                str = e.getCode();
            }
            addItem.addItem(str2, str).addItem(Keys.ERRURL, "").addItem(Keys.APINAME, "dynamicQ").addItem("activity", "HomeActivity").addItem(Keys.f2035T, "0").setOthersNull().post();
            if (e != null) {
                LogUtils.m1571e(DynamicRequestTask.TAG, "request dynamic data exception ApiCode=" + e.getCode() + "  HttpCode=" + e.getHttpCode());
            }
        }
    }

    class C06392 implements Runnable {
        C06392() {
        }

        public void run() {
            GetInterfaceTools.getIDynamicQDataProvider().checkImageURLUpdate(22);
        }
    }

    public void invoke() {
        LogUtils.m1568d(TAG, "request dynamic data");
        TVApi.dynamicQ.callSync(new C06381(), Project.getInstance().getBuild().getVersionString(), Build.MODEL.toString(), "1", "1", "1", "1", "1", "1", "1");
    }

    private void downloadExitImages() {
        this.mHanlder.postDelayed(new C06392(), 60000);
    }

    private void initializeConfig(DeviceCheck data) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        SystemConfigPreference.setRecommend(context, data.isHasRecommend());
        SystemConfigPreference.setOpenHCDN(context, data.isOpenCDN());
        SystemConfigPreference.setDisableNativePlayerSafeMode(context, data.isDisableNativePlayerSafeMode());
        if (MultiScreen.get().isSupportMS()) {
            MultiScreenStartTool.start(AppRuntimeEnv.get().getApplicationContext());
        } else {
            MultiScreen.get().stop();
        }
    }

    public void onOneTaskFinished() {
    }
}
