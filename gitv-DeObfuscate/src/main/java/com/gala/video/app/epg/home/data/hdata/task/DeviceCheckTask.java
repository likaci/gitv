package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.tvapi.tv3.ITVApi;
import com.gala.tvapi.tv3.result.RegisterResult;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.feedback.FeedBackActivationStateTimer;
import com.gala.video.app.epg.home.data.core.ApiExceptionModelFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.gala.video.lib.share.utils.DevicesInfo;

public class DeviceCheckTask extends BaseRequestTask {
    private static final String TAG = "Task/DeviceCheckTask";
    private final DeviceCheckModel mDeviceCheckResult = DeviceCheckModel.getInstance();

    class C06341 implements IApiCallback<ApiResultDeviceCheck> {
        C06341() {
        }

        public void onSuccess(ApiResultDeviceCheck apiResult) {
            LogUtils.m1568d(DeviceCheckTask.TAG, "onDeviceCheck-onSuccess");
            DeviceCheckTask.this.onDevCheckSuccess(apiResult);
        }

        public void onException(ApiException e) {
            String str;
            LogUtils.m1571e(DeviceCheckTask.TAG, "onDeviceCheck-onException e=" + e);
            LogRecordUtils.setEventID(PingBackUtils.createEventId());
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
            String str2 = "pfec";
            if (e == null) {
                str = "";
            } else {
                str = e.getCode();
            }
            addItem = addItem.addItem(str2, str);
            str2 = Keys.ERRURL;
            if (e == null) {
                str = "";
            } else {
                str = e.getUrl();
            }
            addItem.addItem(str2, str).addItem(Keys.APINAME, "deviceCheckP").addItem("activity", "HomeActivity").addItem(Keys.f2035T, "0").setOthersNull().post();
            ApiExceptionModelFactory.saveDevApiExceptionMsg(e);
            DeviceCheckTask.this.complete(DeviceCheckTask.this.mDeviceCheckResult.getErrorEvent());
            PingBackParams params = new PingBackParams();
            PingBackParams add = params.add(Keys.f2035T, Values.value16).add("r", Values.value00001).add("rt", "13");
            str2 = "st";
            if (e == null) {
                str = "";
            } else {
                str = e.getCode();
            }
            add.add(str2, str);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    class C06352 implements com.gala.tvapi.tv3.IApiCallback<RegisterResult> {
        C06352() {
        }

        public void onSuccess(RegisterResult registerResult) {
            LogUtils.m1568d(DeviceCheckTask.TAG, "new device check successfully");
        }

        public void onException(com.gala.tvapi.tv3.ApiException e) {
            LogUtils.m1571e(DeviceCheckTask.TAG, "new device check failed");
        }
    }

    class C06363 implements IApiCallback<ApiResultCode> {
        C06363() {
        }

        public void onSuccess(ApiResultCode apiResult) {
            LogUtils.m1568d(DeviceCheckTask.TAG, "queryState onSuccess result=" + apiResult.code);
            String result = apiResult.code;
            PingBackInitParams params;
            if ("N100001".equals(result)) {
                GetInterfaceTools.getIGalaVipManager().setActivationFeedbackState(1);
                params = PingBack.getInstance().getPingbackInitParams();
                params.sIsVipAct = "0";
                PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), params);
                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.UPDADE_ACTION_BAR);
            } else if ("N100002".equals(result)) {
                GetInterfaceTools.getIGalaVipManager().setActivationFeedbackState(0);
                params = PingBack.getInstance().getPingbackInitParams();
                params.sIsVipAct = "1";
                PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), params);
            }
        }

        public void onException(ApiException e) {
            LogUtils.m1571e(DeviceCheckTask.TAG, "queryState onException e=" + e);
            LogUtils.m1571e(DeviceCheckTask.TAG, "getActivationState.get().getActivationState()=" + GetInterfaceTools.getIGalaVipManager().getActivationState());
        }
    }

    public DeviceCheckTask(int taskId) {
        this.mTaskId = taskId;
    }

    public void invoke() {
        LogUtils.m1568d(TAG, "invoke device check task");
        if (DeviceCheckModel.getInstance().isApiKeyValid()) {
            LogUtils.m1568d(TAG, "device check has been success!,do not need to check repeated");
            if (GetInterfaceTools.getIGalaVipManager().needQueryActivationStateFromServer() && Project.getInstance().getBuild().isSupportVipRightsActivation()) {
                queryActivationState();
                return;
            }
            return;
        }
        TVApi.deviceCheckP.callSync(new C06341(), DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
        ITVApi.registerApi().callSync(new C06352(), new String[0]);
    }

    public void onOneTaskFinished() {
        LogUtils.m1568d(TAG, "device check finished");
    }

    private void complete(ErrorEvent resultEvent) {
        LogUtils.m1568d(TAG, "complete()-----resultEvent=" + resultEvent);
        this.mDeviceCheckResult.setErrorEvent(resultEvent);
        if (resultEvent != ErrorEvent.C_SUCCESS) {
            this.mDeviceCheckResult.setApiKey(null);
        }
    }

    private void onDevCheckSuccess(ApiResultDeviceCheck apiResult) {
        DeviceCheck devCheck = apiResult.data;
        this.mDeviceCheckResult.setDevCheck(devCheck);
        LogUtils.m1568d(TAG, "device check success, api code : " + apiResult.code);
        if (devCheck == null) {
            LogUtils.m1568d(TAG, "device check is success,but there is no result");
            complete(ErrorEvent.C_ERROR_DATAISNULL);
            return;
        }
        AppRuntimeEnv.get().setDeviceIp(devCheck.ip);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, Values.value16).add("r", Values.value00001).add("rt", "13").add("st", "0").add(Keys.RI, "1");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        this.mDeviceCheckResult.setApiKey(devCheck.apiKey);
        this.mDeviceCheckResult.setHomeResId(devCheck.resIds);
        this.mDeviceCheckResult.setIpLoc(devCheck.getIpRegion());
        if (Project.getInstance().getBuild().isSupportVipRightsActivation()) {
            if (GetInterfaceTools.getIGalaVipManager().getActivationState() == -1) {
                queryActivationState();
            } else if (GetInterfaceTools.getIGalaVipManager().getActivationFeedbackState() == 0 && 1 == GetInterfaceTools.getIGalaVipManager().getAccountActivationState()) {
                if (FeedBackActivationStateTimer.get().isRunning()) {
                    FeedBackActivationStateTimer.get().cancel();
                }
                FeedBackActivationStateTimer.get().startFeedbackTask();
            }
        }
        complete(ErrorEvent.C_SUCCESS);
    }

    private void queryActivationState() {
        TVApi.queryState.call(new C06363(), new String[0]);
    }
}
