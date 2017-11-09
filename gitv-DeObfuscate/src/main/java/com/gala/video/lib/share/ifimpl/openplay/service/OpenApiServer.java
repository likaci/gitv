package com.gala.video.lib.share.ifimpl.openplay.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiDebug;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.IQiyiService.Stub;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.Utils;

public class OpenApiServer extends Service {
    private static final String TAG = "OpenApiServer";
    private static OpenApiBinder sBinder;

    private static class OpenApiBinder extends Stub {
        private static final int MAX_PAGE_SIZE = 60;
        private static final String TAG = "OpenApiBinder";

        public OpenApiBinder(Context context) {
        }

        public Bundle invoke(Bundle params) throws RemoteException {
            Bundle outParams = new Bundle();
            if (params == null) {
                ServerParamsHelper.setResultCode(outParams, 6);
                LogUtils.m1571e(TAG, "OpenAPI call fail caused by client version not match! server version = 66430");
                return outParams;
            }
            params.setClassLoader(OpenApiBinder.class.getClassLoader());
            if (LogUtils.mIsDebug) {
                OpenApiDebug.dumpBundle(TAG, "invoke() begin.", params);
            }
            int target = ServerParamsHelper.parseOperationTarget(params);
            int operation = ServerParamsHelper.parseOperationType(params);
            int data = ServerParamsHelper.parseOperationDataType(params);
            if (target == TargetType.TARGET_AUTH && OpenApiManager.instance().authLocal(params)) {
                ServerParamsHelper.setPageMaxSize(outParams, 60);
                if (CreateInterfaceTools.createDeviceCheckProxy().isDevCheckPass()) {
                    ServerParamsHelper.setResultCode(outParams, 0);
                    if (LogUtils.mIsDebug) {
                        OpenApiDebug.dumpBundle(TAG, "invoke() end.", outParams);
                    }
                    return outParams;
                }
            }
            ServerCommand<?> find = OpenApiManager.instance().findCommand(target, operation, data);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "invoke() find command=" + find);
            }
            if (find != null) {
                Bundle processResult = null;
                if (!OpenApiManager.instance().isAllowedClient()) {
                    ServerParamsHelper.setResultCode(outParams, 2);
                    if (LogUtils.mIsDebug) {
                        OpenApiDebug.dumpBundle(TAG, "invoke() end.", outParams);
                    }
                    return outParams;
                } else if (find.isNeedNetWork() && !OpenApiNetwork.isNetworkAvaliable()) {
                    ServerParamsHelper.setResultCode(outParams, 4);
                    return outParams;
                } else if (find.isAllowedAccess()) {
                    try {
                        processResult = find.process(params);
                    } catch (Exception e) {
                        LogUtils.m1578w(TAG, "invoke() process command error!", e);
                        ServerParamsHelper.setResultCode(outParams, Utils.parseErrorCode(e));
                    }
                    if (processResult != null) {
                        processResult.putAll(outParams);
                        outParams = processResult;
                    } else {
                        LogUtils.m1577w(TAG, "invoke() why get null result from command " + find);
                    }
                } else {
                    ServerParamsHelper.setResultCode(outParams, 9);
                    return outParams;
                }
            }
            ServerParamsHelper.setResultCode(outParams, 5);
            if (LogUtils.mIsDebug) {
                OpenApiDebug.dumpBundle(TAG, "invoke() end.", outParams);
            }
            return outParams;
        }

        public void clear() {
            OpenApiManager.instance().clearAuth();
        }
    }

    public void onCreate() {
        super.onCreate();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onCreate()");
        }
    }

    public synchronized IBinder onBind(Intent intent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onBind(" + intent + ")");
        }
        if (LogUtils.mIsDebug) {
            OpenApiDebug.dumpBundle(TAG, "onBind()", intent.getExtras());
        }
        initBinder(intent);
        return sBinder;
    }

    public synchronized boolean onUnbind(Intent intent) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onUnbind(" + intent + ")");
        }
        if (sBinder != null) {
            sBinder.clear();
        }
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onDestroy()");
        }
        super.onDestroy();
    }

    private void initBinder(Intent intent) {
        if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.OPENAPI, HOSTSTATUS.START);
        }
        if (sBinder == null) {
            sBinder = new OpenApiBinder(this);
            GetInterfaceTools.getStartupDataLoader().load(false);
        }
    }
}
