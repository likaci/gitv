package com.gala.video.app.epg.ui.setting.utils;

import android.content.Context;
import android.os.Handler;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ModuleUpdate;
import com.gala.tvapi.tv2.result.ApiResultModuleUpdate;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider.SettingType;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QBaseActivity;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.project.Project;

public class UpgradeSettingUtils {
    private static final int DELAY_THREE_SECOND = 3000;
    private static final String MAIN_APK_UPGRADE_KEY = "pri";
    private static final String TAG = UpgradeSettingUtils.class.getSimpleName();
    private static Handler mHandler = new Handler();
    private static boolean mIsUpgradeChecking;

    public static void onUpgradeClick(Context context) {
        if (Project.getInstance().getBuild().isHomeVersion() && !ListUtils.isEmpty(CustomSettingProvider.getInstance().getItems(SettingType.UPGRADE))) {
            SettingUtils.startUpgradeForLauncher(context);
        } else if (NetWorkManager.getInstance().getNetState() != 1 && NetWorkManager.getInstance().getNetState() != 2) {
            CreateInterfaceTools.createNetworkProvider().makeDialogAsNetworkError(context, context.getString(R.string.no_network)).show();
        } else if (!mIsUpgradeChecking) {
            checkApk(context);
        }
    }

    private static void checkApk(final Context context) {
        mIsUpgradeChecking = true;
        TVApi.moduleUpdate.call(new IApiCallback<ApiResultModuleUpdate>() {
            public void onSuccess(ApiResultModuleUpdate result) {
                if (result == null || ListUtils.isEmpty(result.data)) {
                    LogUtils.d(UpgradeSettingUtils.TAG, "check Apk app upgrade, result is null");
                    UpgradeSettingUtils.showNoneedUpdateToast(context);
                    return;
                }
                for (ModuleUpdate module : result.data) {
                    if (UpgradeSettingUtils.MAIN_APK_UPGRADE_KEY.equals(module.key)) {
                        AppVersion version = new AppVersion();
                        version.setVersion(module.version);
                        version.setTip(module.tip);
                        version.setUrl(module.url);
                        version.setUpgradeType(module.upType);
                        version.setMd5(module.md5);
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(UpgradeSettingUtils.TAG, "check upgrade success version : " + version.toString());
                        }
                        UpdateManager.getInstance().setAppVersion(version);
                        UpgradeSettingUtils.mHandler.post(new Runnable() {
                            public void run() {
                                UpgradeSettingUtils.mIsUpgradeChecking = false;
                                if (UpdateManager.getInstance().isShowingDialog()) {
                                    if (LogUtils.mIsDebug) {
                                        LogUtils.d(UpgradeSettingUtils.TAG, "check Apk upgrade dialog showing, do nothing");
                                    }
                                } else if (UpdateManager.getInstance().hasUpdate()) {
                                    UpgradeSettingUtils.showUpdateDialog(context, false);
                                } else {
                                    UpgradeSettingUtils.showNoUpdateToast(context);
                                }
                            }
                        });
                        return;
                    }
                    LogUtils.d(UpgradeSettingUtils.TAG, "check Apk app upgrade, module key is not equal to MAIN_APK_UPGRADE_KEY");
                    UpgradeSettingUtils.showNoneedUpdateToast(context);
                }
            }

            public void onException(ApiException exception) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(UpgradeSettingUtils.TAG, "check Apk app upgrade request failed, exception = ", exception);
                }
                UpgradeSettingUtils.showNoneedUpdateToast(context);
            }
        }, "{}");
    }

    private static void showNoneedUpdateToast(final Context context) {
        mHandler.post(new Runnable() {
            public void run() {
                UpgradeSettingUtils.mIsUpgradeChecking = false;
                if (NetworkStatePresenter.getInstance().handleNetWork()) {
                    UpgradeSettingUtils.showNoUpdateToast(context);
                }
            }
        });
    }

    private static void showNoUpdateToast(Context context) {
        if (context != null) {
            QToast.makeTextAndShow(context, context.getString(R.string.not_need_update), 3000);
        }
    }

    public static void showUpdateDialog(final Context context, boolean isFetchData) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "show UpdateDialog, isFetchData = " + isFetchData);
        }
        UpdateManager.getInstance().showDialogAndStartDownload(context, true, new UpdateOperation() {
            public void exitApp() {
                if (context != null && (context instanceof QBaseActivity)) {
                    ((QBaseActivity) context).onExitApp();
                }
            }

            public void cancelUpdate() {
            }
        });
    }
}
