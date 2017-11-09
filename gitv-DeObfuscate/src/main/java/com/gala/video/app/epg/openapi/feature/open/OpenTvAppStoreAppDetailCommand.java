package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.tvos.appdetailpage.config.APIConstants;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity;

public class OpenTvAppStoreAppDetailCommand extends ServerCommand<Intent> {
    private static final String TAG = "OpenTvAppStoreAppDetailCommand";

    public OpenTvAppStoreAppDetailCommand(Context context) {
        super(context, TargetType.TARGET_APP_STORE_APPDETAIL, 20001, 30000);
        setNeedNetwork(false);
    }

    public Bundle onProcess(Bundle params) {
        AppInfo appInfo = ServerParamsHelper.parseAppInfo(params);
        if (appInfo == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        int errorCode;
        try {
            AppStoreDetailActivity.setMixFlag(true);
            Intent intent = new Intent(getContext(), AppStoreDetailActivity.class);
            intent.setFlags(ServerParamsHelper.parseIntentFlag(params));
            intent.putExtra(APIConstants.BUNDLE_EXTRA_DETAILAPP_APP_PKG, appInfo.getPackageName());
            intent.putExtra("appid", appInfo.getId());
            intent.putExtra("uuid", Project.getInstance().getBuild().getVrsUUID());
            intent.putExtra("deviceid", AppRuntimeEnv.get().getDefaultUserId());
            getContext().startActivity(intent);
            increaseAccessCount();
            errorCode = 0;
        } catch (Exception e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "OpenTvAppStoreAppDetailCommand() -> exception");
            }
            e.printStackTrace();
            errorCode = 7;
        }
        Bundle result = OpenApiResultCreater.createResultBundle(errorCode);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
