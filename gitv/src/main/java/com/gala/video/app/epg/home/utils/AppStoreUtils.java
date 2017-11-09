package com.gala.video.app.epg.home.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.applist.activity.AppLauncherActivity;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.downloadapp.DownloadListener;
import com.gala.video.lib.framework.core.network.downloadapp.DownloadManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.action.data.AppActionData;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.lib.share.utils.Precondition;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gitv.tvappstore.AppStoreManager;
import com.tvos.appdetailpage.config.APIConstants;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity;

public class AppStoreUtils {
    private static final int DOWNLOAD_CANCEL = 3;
    private static String TAG = "HomeItemUtils/AppStoreUtils";
    private static String mApkDownloadUrl;
    private static Handler mBaseHandler = new Handler(Looper.getMainLooper());
    private static DownloadListener mDownloadListener = new DownloadListener() {
        private Context mContext = AppRuntimeEnv.get().getApplicationContext();

        public void onStart() {
            AppStoreUtils.refreshToast(this.mContext, ResourceUtil.getStr(R.string.dialog_app_start_download));
        }

        public void onPause() {
        }

        public void onStop() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AppStoreUtils.TAG, "download stop");
            }
        }

        public void onComplete(final int code, final String path, final String name) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AppStoreUtils.TAG, "download complete,  " + code + " , path: " + path);
            }
            AppStoreUtils.runOnUiThread(new Runnable() {
                public void run() {
                    if (code == 0) {
                        AnonymousClass1.this.refreshDownloadSuccess(code, path, name);
                    } else {
                        AnonymousClass1.this.refreshDownloadFailed(code, path, name);
                    }
                }
            });
        }

        public void onProgress(int progress) {
        }

        private void refreshDownloadSuccess(int code, String path, String name) {
            try {
                AppStoreManager.getInstance().install(path);
            } catch (Exception e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(AppStoreUtils.TAG, "install app failed,,exception = ", e);
                }
            }
        }

        private void refreshDownloadFailed(int code, String path, String name) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(AppStoreUtils.TAG, "download failed -> " + code + " , path: " + path);
            }
            if (code != 3) {
                QToast.makeTextAndShow(this.mContext, ResourceUtil.getStr(R.string.dialog_app_download_error), (int) QToast.LENGTH_4000);
            }
        }
    };

    public static void onAppLocalClick(Context context, String appPackageName) {
        Intent intent = new Intent(context, AppLauncherActivity.class);
        intent.putExtra("package_name", appPackageName);
        intent.putExtra("start_app_form", "start_app_package_name");
        PageIOUtils.activityIn(context, intent);
    }

    public static void onAppOnlineClick(Context context, String appPackageName, int appId) {
        try {
            AppStoreDetailActivity.setMixFlag(true);
            Intent intent = new Intent(context, AppStoreDetailActivity.class);
            intent.putExtra(APIConstants.BUNDLE_EXTRA_DETAILAPP_APP_PKG, appPackageName);
            intent.putExtra("appid", appId);
            intent.putExtra("uuid", Project.getInstance().getBuild().getVrsUUID());
            intent.putExtra("deviceid", AppRuntimeEnv.get().getDefaultUserId());
            PageIOUtils.activityIn(context, intent);
        } catch (Exception e) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "onAppOnlineClick() -> exception");
            }
            e.printStackTrace();
        }
    }

    public static int checkApps() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!Precondition.isNull(result)) {
            if (result.getAppCard() == 3) {
                return 21;
            }
            if (result.getAppCard() == 2) {
                return 22;
            }
            if (result.getAppCard() == 1) {
                return 20;
            }
        }
        if (Project.getInstance().getBuild().isHomeVersion()) {
            return 20;
        }
        return -1;
    }

    public static void onAppStoreClick(Context context, AppActionData data) {
        if (null == null) {
            DownloadManager.getInstance().init(context);
        }
        AppStoreManager appStoreManager = null;
        if (null == null) {
            appStoreManager = AppStoreManager.getInstance();
        }
        if (!isInstalled(context, data.getAppPackageName())) {
            startDownloadAppStore(context, data.getAppDownloadUrl());
        } else if (appStoreManager != null) {
            appStoreManager.openAppStore();
        }
    }

    private static boolean isInstalled(Context context, String appPackageName) {
        if (!StringUtils.isEmpty((CharSequence) appPackageName)) {
            try {
                if (context.getPackageManager().getApplicationInfo(appPackageName, 8192) != null) {
                    return true;
                }
                return false;
            } catch (NameNotFoundException e) {
                if (!LogUtils.mIsDebug) {
                    return false;
                }
                LogUtils.d(TAG, "packageName is not found, NameNotFoundException:", e);
                return false;
            }
        } else if (!LogUtils.mIsDebug) {
            return false;
        } else {
            LogUtils.d(TAG, "packageName is null ");
            return false;
        }
    }

    private static void refreshToast(Context context, String str) {
        QToast.makeTextAndShow(context, (CharSequence) str, 2000);
    }

    private static void startDownloadAppStore(Context context, String apkDownloadUrl) {
        if (!NetworkStatePresenter.getInstance().checkStateIllegal() || !NetworkStatePresenter.getInstance().handleNetWork()) {
            return;
        }
        if (StringUtils.isEmpty((CharSequence) apkDownloadUrl)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "appDownloadUrl is null");
            }
            refreshToast(context, context.getString(R.string.dialog_app_nodata));
            return;
        }
        mApkDownloadUrl = apkDownloadUrl;
        int state = DownloadManager.getInstance().start("应用商店", mApkDownloadUrl, mDownloadListener);
        if (state == 2) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "init failed");
            }
        } else if (state == 1) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "file exist");
            }
            AppStoreManager.getInstance().install(DownloadManager.getInstance().getApkFilePath(mApkDownloadUrl));
        } else if (state == 3) {
            refreshToast(context, context.getString(R.string.dialog_app_downloading));
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "downloading");
            }
        }
    }

    protected static void runOnUiThread(Runnable runnable) {
        mBaseHandler.post(runnable);
    }
}
