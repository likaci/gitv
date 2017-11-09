package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.RefreshTime;
import com.gala.tvapi.tv2.result.ApiResultRefreshTime;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.provider.DataRefreshPeriodism;

public class DataRequestRefreshTask extends BaseRequestTask {
    private static final String TAG = "home/DataRequestRefreshTask";

    public DataRequestRefreshTask(int taskId) {
        this.mTaskId = taskId;
    }

    public void invoke() {
        LogUtils.d(TAG, "invoke task refresh time request Task");
        TVApi.refreshTime.callSync(new IApiCallback<ApiResultRefreshTime>() {
            public void onSuccess(ApiResultRefreshTime result) {
                if (result != null && result.getData() != null) {
                    for (RefreshTime time : result.getData()) {
                        if (time != null) {
                            DataRefreshPeriodism.instance().setRules(time.level, time.rules);
                            if (time.ext != null) {
                                DataRefreshPeriodism.instance().setCategory(time.level, time.ext.resgid);
                                LogUtils.d(DataRequestRefreshTask.TAG, "home data refresh time rules= " + time.rules + ",group id = " + time.ext.resgid + ", level = " + time.level);
                            }
                        }
                    }
                    DataRefreshPeriodism.instance().saveToNativeCache();
                    int fastGroupRefreshTime = DataRefreshPeriodism.instance().getRefreshInterval(1);
                    LogUtils.d(DataRequestRefreshTask.TAG, "fastGroupRefreshTime = " + fastGroupRefreshTime);
                    AlbumProviderApi.getAlbumProvider().setChannelCacheTime((long) fastGroupRefreshTime);
                }
            }

            public void onException(ApiException e) {
                String str;
                LogUtils.e(DataRequestRefreshTask.TAG, "fetch refresh time exception", e);
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
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "refreshTime");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.CRASHTYPE, "").addItem(Keys.T, "0").setOthersNull().post();
            }
        }, Project.getInstance().getBuild().getVersionString());
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "get current server time : " + DeviceUtils.getServerTimeMillis() + " current day : " + DeviceUtils.getServerCurrentDate());
    }
}
