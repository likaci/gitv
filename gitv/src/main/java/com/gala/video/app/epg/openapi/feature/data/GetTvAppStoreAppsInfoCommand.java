package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.epg.home.data.hdata.task.AppOperateRequestTask;
import com.gala.video.app.epg.home.data.model.AppDataModel;
import com.gala.video.app.epg.home.data.provider.AppsProvider;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.List;

public class GetTvAppStoreAppsInfoCommand extends ServerCommand<AppInfo> {
    private static final String TAG = "GetTvAppStoreAppsInfoCommand";

    public GetTvAppStoreAppsInfoCommand(Context context) {
        super(context, TargetType.TARGET_APP_STORE_APPINFO, 20003, DataType.DATA_APP_INFO_LIST);
        setNeedNetwork(true);
    }

    protected Bundle onProcess(Bundle params) {
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        int category = ServerParamsHelper.parseAppCategory(params);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "process() maxCount=" + maxCount + ", category=" + category);
        }
        if (maxCount <= 0) {
            maxCount = 0;
        }
        Bundle result = getAppStoreAppsInfo(category, maxCount);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }

    private Bundle getAppStoreAppsInfo(int category, int maxCount) {
        int i = 0;
        ResultListHolder<AppInfo> resultListHolder = new ResultListHolder();
        new AppOperateRequestTask(true).invoke();
        boolean gotData = false;
        int dataCount = 0;
        List<AppDataModel> appsList = AppsProvider.getInstance().getAppsList();
        if (appsList != null && appsList.size() > 0) {
            gotData = true;
            for (AppDataModel appModel : appsList) {
                int flag = appModel.getFlag();
                if (flag != 0 && (category == 0 || flag == category)) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setId(appModel.getId());
                    appInfo.setName(appModel.getName());
                    appInfo.setPackageName(appModel.getPackageName());
                    appInfo.setImageUrl(appModel.getImageUrl());
                    appInfo.setDownloadUrl(appModel.getDownloadUrl());
                    appInfo.setCategory(appModel.getFlag());
                    resultListHolder.add(appInfo);
                    dataCount++;
                }
                if (maxCount > 0 && dataCount >= maxCount) {
                    break;
                }
            }
        }
        if (OpenApiNetwork.isNetworkAvaliable()) {
            resultListHolder.setNetworkValid(true);
            if (!gotData) {
                i = 7;
            }
            resultListHolder.setCode(i);
            increaseAccessCount();
        } else {
            resultListHolder.setNetworkValid(false);
            resultListHolder.setCode(4);
        }
        return resultListHolder.getResult();
    }
}
