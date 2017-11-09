package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.ITVApi;
import com.gala.tvapi.tv3.result.HostUpgradeResult;
import com.gala.tvapi.tv3.result.model.HostUpgrade;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.project.Project;

public class AppUpgradeCheckNewTask extends BaseRequestTask {
    private final String TAG = "AppUpgradeCheckNewTask";

    class C06261 implements IApiCallback<HostUpgradeResult> {
        C06261() {
        }

        public void onSuccess(HostUpgradeResult hostUpgradeResult) {
            if (hostUpgradeResult != null && hostUpgradeResult.updateList != null && hostUpgradeResult.updateList.size() > 0) {
                for (HostUpgrade host : hostUpgradeResult.updateList) {
                    if (host.modType.equals("1")) {
                        AppVersion version = new AppVersion();
                        version.setVersion(host.upVer);
                        version.setTip(host.upTip);
                        version.setUrl(host.upUrl);
                        version.setUpgradeType(host.upType);
                        version.setMd5(host.upFileMd5);
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("AppUpgradeCheckNewTask", "check upgrade success version : " + version.toString());
                        }
                        UpdateManager.getInstance().setAppVersion(version);
                        if (!UpdateManager.getInstance().isShowingDialog() && UpdateManager.getInstance().hasUpdate()) {
                            GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_UPGRADE_EVENT);
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d("AppUpgradeCheckNewTask", "has new apk version");
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
            }
        }

        public void onException(ApiException e) {
        }
    }

    public void invoke() {
        ITVApi.hostUpgradeApi().callSync(new C06261(), Project.getInstance().getBuild().getShowVersion());
    }

    public void onOneTaskFinished() {
    }
}
