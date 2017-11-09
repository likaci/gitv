package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.AppsProvider;
import com.gala.video.app.epg.preference.AppStorePreference;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gitv.tvappstore.AppStoreManager;
import com.gitv.tvappstore.AppStoreManager.OnGetUrlListener;
import retrofit.RetrofitError;

public class AppStoreRequestTask extends BaseRequestTask {
    private static final String TAG = "home/AppStoreRequestTask";
    private AppStoreManager mAppStoreManager;
    private OnGetUrlListener mDownloadApkUrlListener = new C06251();

    class C06251 implements OnGetUrlListener {
        C06251() {
        }

        public void onSuccess(String downloadUrl) {
            if (!StringUtils.isEmpty((CharSequence) downloadUrl)) {
                AppsProvider.getInstance().setDownloadUrl(downloadUrl);
                AppStorePreference.saveApkDownloadUrl(AppRuntimeEnv.get().getApplicationContext(), downloadUrl);
                HomeDataObservable.getInstance().post(HomeDataType.APP_STORE, WidgetChangeStatus.DataChange, null);
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AppStoreRequestTask.TAG, "mDownloadApkUrlListener() -> downloadUrl == null");
            }
        }

        public void onFailure(RetrofitError e) {
            boolean z;
            String str;
            LogUtils.m1572e(AppStoreRequestTask.TAG, "mDownloadApkUrlListener() -> onFailure() RetrofitError e:", e);
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
            String str2 = "pfec";
            StringBuilder append = new StringBuilder().append("isNetworkError");
            if (e == null) {
                z = false;
            } else {
                z = e.isNetworkError();
            }
            addItem = addItem.addItem(str2, append.append(z).toString());
            str2 = Keys.ERRURL;
            if (e == null) {
                str = "";
            } else {
                str = e.getUrl();
            }
            addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "AppStore");
            str2 = Keys.ERRDETAIL;
            if (e == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.f2035T, "0").setOthersNull().post();
        }
    }

    public AppStoreRequestTask() {
        try {
            this.mAppStoreManager = AppStoreManager.getInstance();
            this.mAppStoreManager.init(AppRuntimeEnv.get().getApplicationContext(), Project.getInstance().getBuild().getAppStorePkgName(), AppRuntimeEnv.get().getDefaultUserId());
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "QAppStoreDataRequest() -> mAppStoreManager e:", e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invoke() {
        /*
        r4 = this;
        r1 = "home/AppStoreRequestTask";
        r2 = "requestDataInThread()";
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r1, r2);
        r1 = r4.mAppStoreManager;	 Catch:{ Exception -> 0x0025 }
        if (r1 == 0) goto L_0x001b;
    L_0x000d:
        r1 = com.gala.video.app.epg.home.data.provider.AppsProvider.getInstance();	 Catch:{ Exception -> 0x0025 }
        r1.clearAppsList();	 Catch:{ Exception -> 0x0025 }
        r1 = r4.mAppStoreManager;	 Catch:{ Exception -> 0x0025 }
        r2 = r4.mDownloadApkUrlListener;	 Catch:{ Exception -> 0x0025 }
        r1.fetchDownloadUrl(r2);	 Catch:{ Exception -> 0x0025 }
    L_0x001b:
        r1 = "home/AppStoreRequestTask";
        r2 = "requestDataFromServer() -> mCountDownLatch end";
        com.gala.video.lib.framework.core.utils.LogUtils.m1571e(r1, r2);
    L_0x0024:
        return;
    L_0x0025:
        r0 = move-exception;
        r1 = "home/AppStoreRequestTask";
        r2 = "requestDataInThread() -> e :";
        com.gala.video.lib.framework.core.utils.LogUtils.m1572e(r1, r2, r0);	 Catch:{ all -> 0x0039 }
        r1 = "home/AppStoreRequestTask";
        r2 = "requestDataFromServer() -> mCountDownLatch end";
        com.gala.video.lib.framework.core.utils.LogUtils.m1571e(r1, r2);
        goto L_0x0024;
    L_0x0039:
        r1 = move-exception;
        r2 = "home/AppStoreRequestTask";
        r3 = "requestDataFromServer() -> mCountDownLatch end";
        com.gala.video.lib.framework.core.utils.LogUtils.m1571e(r2, r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.home.data.hdata.task.AppStoreRequestTask.invoke():void");
    }

    public void onOneTaskFinished() {
        LogUtils.m1568d(TAG, "request appstore data Task Finished");
    }
}
