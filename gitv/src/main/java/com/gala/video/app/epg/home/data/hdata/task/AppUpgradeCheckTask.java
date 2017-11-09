package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ModuleUpdate;
import com.gala.tvapi.tv2.result.ApiResultModuleUpdate;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;

public class AppUpgradeCheckTask extends BaseRequestTask {
    private final String MAIN_APK_UPGRADE_KEY = "pri";
    private final String TAG = AppUpgradeCheckTask.class.getName();

    public String identifier() {
        return String.valueOf(hashCode());
    }

    public void invoke() {
        UpdateManager.getInstance().setAppVersion(new AppVersion());
        LogUtils.d(this.TAG, "invoke app upgrade task");
        TVApi.moduleUpdate.callSync(new IApiCallback<ApiResultModuleUpdate>() {
            public void onSuccess(ApiResultModuleUpdate result) {
                if (result != null && result.data != null) {
                    for (ModuleUpdate module : result.data) {
                        if ("pri".equals(module.key)) {
                            AppVersion version = new AppVersion();
                            version.setVersion(module.version);
                            version.setTip(module.tip);
                            version.setUrl(module.url);
                            version.setUpgradeType(module.upType);
                            version.setMd5(module.md5);
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(AppUpgradeCheckTask.this.TAG, "check upgrade success version : " + version.toString());
                            }
                            UpdateManager.getInstance().setAppVersion(version);
                            if (!UpdateManager.getInstance().isShowingDialog() && UpdateManager.getInstance().hasUpdate()) {
                                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_UPGRADE_EVENT);
                                if (LogUtils.mIsDebug) {
                                    LogUtils.d(AppUpgradeCheckTask.this.TAG, "has new apk version");
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                    }
                }
            }

            public void onException(ApiException exception) {
                LogUtils.d(AppUpgradeCheckTask.this.TAG, "app upgrade request failed!", exception);
            }
        }, "{}");
    }

    public void onOneTaskFinished() {
    }
}
