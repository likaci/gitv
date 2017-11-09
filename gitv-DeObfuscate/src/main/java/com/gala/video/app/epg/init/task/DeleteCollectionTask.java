package com.gala.video.app.epg.init.task;

import android.content.Context;
import android.provider.Settings.System;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class DeleteCollectionTask implements Runnable {
    private static final String IS_FIRST_START = "gitvdemo.tvapi.isfirststart";
    private static final String TAG = "DeleteCollectionTask";
    public Context mContext;

    public DeleteCollectionTask(Context context) {
        this.mContext = context;
    }

    public void run() {
        LogUtils.m1568d(TAG, "start delete collection task");
        TVApiBase.getTVApiProperty().getPassportDeviceId();
        String id = "";
        if (this.mContext != null) {
            id = System.getString(this.mContext.getContentResolver(), IS_FIRST_START);
            if (id == null || id.isEmpty()) {
                final String mac = TVApiBase.getTVApiProperty().getAnonymity();
                UserHelper.clearCollectForAnonymity.call(new IVrsCallback<ApiResultCode>() {
                    public void onSuccess(ApiResultCode result) {
                        System.putString(DeleteCollectionTask.this.mContext.getContentResolver(), DeleteCollectionTask.IS_FIRST_START, mac);
                        LogUtils.m1568d(DeleteCollectionTask.TAG, "start first time, clear collect for anonymity, mac=" + mac);
                    }

                    public void onException(ApiException e) {
                        e.printStackTrace();
                    }
                }, mac);
                return;
            }
            LogUtils.m1568d(TAG, "none first start, id=" + id);
        }
    }
}
