package com.gala.video.lib.share.common.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IAppDownloadManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.TraceEx;

@SuppressLint({"DefaultLocale", "NewApi"})
public abstract class QBaseActivity extends FragmentActivity {
    private static final String TAG = "QBaseActivity";
    protected static boolean isHomeStarted = false;
    public static boolean isLoaderWEBActivity = false;
    private MyObserver mAppDownloadObserver = new MyObserver() {
        public void update(String event) {
            LogRecordUtils.logd(QBaseActivity.TAG, "AppDownloadObserver: event -> " + event);
            QBaseActivity.this.mIsAppDownloadComplete = true;
            QBaseActivity.this.startInstallApplication();
        }
    };
    private Context mAttachContext;
    protected boolean mIsAppDownloadComplete = false;
    private LayoutInflater mLayoutInflater;
    protected PingbackPage mPingbackPage;
    private MyObserver mUpgradeObserver = new MyObserver() {
        public void update(String event) {
            LogUtils.d(QBaseActivity.TAG, "receive upgrade event");
            QBaseActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    QBaseActivity.this.showUpdateDialog(false);
                }
            });
        }
    };

    protected View getBackgroundContainer() {
        return null;
    }

    public PingbackPage getPingbackPage() {
        return this.mPingbackPage;
    }

    public void setPingbackPage(PingbackPage pingbackPage) {
        this.mPingbackPage = pingbackPage;
    }

    protected void onStart() {
        LogUtils.d(TAG, "onStart() " + this);
        super.onStart();
        IScreenSaverOperate ioperate = GetInterfaceTools.getIScreenSaver();
        if (!(ioperate == null || ioperate.isApplicationBroughtToBackground())) {
            ioperate.reStart();
        }
        GetInterfaceTools.getIActionManager().onActivityStart(getIntent().getAction());
    }

    @TargetApi(14)
    protected void onCreate(Bundle savedInstanceState) {
        GetInterfaceTools.getIActionManager().onActivityCreate(getIntent().getAction());
        LogUtils.d(TAG, "onCreate() " + this);
        super.onCreate(savedInstanceState);
        AppRuntimeEnv.get().addActivity(this);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        TVApiBase.createRegisterKey(DeviceUtils.getMacAddr(), Project.getInstance().getBuild().getVrsUUID(), Project.getInstance().getBuild().getVersionString());
        if (Project.getInstance().getBuild().isIsSupportScreenSaver() && !Project.getInstance().getBuild().isHomeVersion()) {
            getWindow().addFlags(128);
        }
        CreateInterfaceTools.createUpdateManager().setLimitNotifyCount(isHomeStarted);
        GetInterfaceTools.getActiveStateDispatcher().notifyKeyEvent();
    }

    public void finish() {
        LogUtils.d(TAG, "finish() " + this);
        super.finish();
    }

    protected void onDestroy() {
        GetInterfaceTools.getIActionManager().onActivityDestory(getIntent().getAction());
        GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
        LogUtils.d(TAG, "onDestroy() " + this);
        super.onDestroy();
        Drawable drawable = Project.getInstance().getControl().getBackgroundDrawable();
        if (drawable != null) {
            drawable.setCallback(null);
        }
        if (Project.getInstance().getBuild().isIsSupportScreenSaver() && !Project.getInstance().getBuild().isHomeVersion()) {
            getWindow().clearFlags(128);
        }
        AppRuntimeEnv.get().removeActivity(this);
    }

    public void keepScreenOn() {
        LogUtils.d(TAG, "keepScreenOn");
        getWindow().addFlags(128);
    }

    public void clearScreenOn() {
        LogUtils.d(TAG, "clearScreenOn->isHome=" + Project.getInstance().getBuild().isHomeVersion());
        if (Project.getInstance().getBuild().isHomeVersion()) {
            getWindow().clearFlags(128);
        }
    }

    protected void onResume() {
        GetInterfaceTools.getIActionManager().onActivityResume(getIntent().getAction());
        LogUtils.d(TAG, "onResume() " + this);
        super.onResume();
        View container = getBackgroundContainer();
        if (!(container == null || isUsingSystemWallPaper())) {
            LogUtils.e(TAG, "container setBackground---");
            setBackground(container);
        }
        if (!isHomeStarted && needCheckUpdate()) {
            GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.STARTUP_UPGRADE_EVENT, this.mUpgradeObserver);
        }
        GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.APP_DOWNLOAD_COMPLETE, this.mAppDownloadObserver);
        GetInterfaceTools.getIScreenSaver().setCurrentActivity(this);
    }

    protected void onPause() {
        GetInterfaceTools.getIActionManager().onActivityPause(getIntent().getAction());
        LogUtils.d(TAG, "onPause() " + this);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.APP_DOWNLOAD_COMPLETE, this.mAppDownloadObserver);
        super.onPause();
    }

    private void cleanMemoryDownApi16() throws Exception {
        try {
            Class<?> windowManager = Class.forName("android.view.WindowManagerImpl");
            windowManager.getDeclaredMethod("startTrimMemory", new Class[]{Integer.TYPE}).invoke(windowManager, new Object[]{Integer.valueOf(80)});
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getWindowManager exception ");
        }
    }

    private void cleanMemoryApi17TO19() throws Exception {
        try {
            Class<?> windowManager = Class.forName("android.view.WindowManagerGlobal");
            Object owner = windowManager.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            windowManager.getDeclaredMethod("startTrimMemory", new Class[]{Integer.TYPE}).invoke(owner, new Object[]{Integer.valueOf(80)});
            LogUtils.d(TAG, "Api17TO19 startTrimMemory");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getWindowManager exception ");
        }
    }

    private void cleanMemoryApiUp20() throws Exception {
        try {
            Class<?> windowManager = Class.forName("android.view.WindowManagerGlobal");
            Object owner = windowManager.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            windowManager.getDeclaredMethod("trimMemory", new Class[]{Integer.TYPE}).invoke(owner, new Object[]{Integer.valueOf(80)});
            LogUtils.d(TAG, "ApiUp20 trimMemory");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getWindowManager exception ");
        }
    }

    protected void cleanMemory() throws Exception {
        if (VERSION.SDK_INT >= 20) {
            cleanMemoryApiUp20();
        } else if (VERSION.SDK_INT < 17 || VERSION.SDK_INT > 19) {
            cleanMemoryDownApi16();
        } else {
            cleanMemoryApi17TO19();
        }
    }

    protected void onStop() {
        GetInterfaceTools.getIActionManager().onActivityStop(getIntent().getAction());
        LogUtils.d(TAG, "onStop()" + this);
        super.onStop();
        if (!isHomeStarted && needCheckUpdate()) {
            GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.STARTUP_UPGRADE_EVENT, this.mUpgradeObserver);
        }
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        if (iOperate.isApplicationBroughtToBackground()) {
            iOperate.stop();
        }
    }

    protected boolean isUsingSystemWallPaper() {
        return false;
    }

    public void setBackground(View container) {
        Drawable drawable = Project.getInstance().getControl().getBackgroundDrawable();
        Drawable background = container.getBackground();
        if (drawable != null && background != drawable) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "container setBackground 2---");
            }
            if (VERSION.SDK_INT >= 16) {
                container.setBackground(null);
                container.setBackground(drawable);
                return;
            }
            container.setBackgroundDrawable(null);
            container.setBackgroundDrawable(drawable);
        }
    }

    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (!isUsingSystemWallPaper() && getBackgroundContainer() != null) {
            getWindow().setBackgroundDrawable(null);
        }
    }

    protected boolean consumeKeyEvent(KeyEvent event) {
        return false;
    }

    public final boolean dispatchKeyEvent(KeyEvent event) {
        TraceEx.beginSection("QBaseActivity.dispatchKeyEvent");
        if (event.getAction() != 0) {
            GetInterfaceTools.getIScreenSaver().reStart();
            GetInterfaceTools.getIFeedbackKeyProcess().dispatchKeyEvent(event, this);
            GetInterfaceTools.getActiveStateDispatcher().notifyKeyEvent();
        }
        if (consumeKeyEvent(event)) {
            boolean ret = super.dispatchKeyEvent(event);
            TraceEx.endSection();
            return ret;
        }
        boolean handled = handleKeyEvent(event);
        if (MultiScreen.get().isPhoneKey() && event.getKeyCode() != 66) {
            MultiScreen.get().setIsPhoneKey(false);
        }
        if (LogUtils.mIsDebug || GetInterfaceTools.getILogRecordProvider().isLogRecordEnable()) {
            String logStr = "[TID " + Process.myTid() + "] " + "dispatchKeyEvent(keyCode=" + KeyEvent.keyCodeToString(event.getKeyCode()) + "), return " + handled + " activityName = " + this;
            if (LogUtils.mIsDebug) {
                Log.d(TAG, logStr);
            }
        }
        TraceEx.endSection();
        return handled;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    public void forceExitApp() {
        finish();
        Process.killProcess(Process.myPid());
    }

    public void onExitApp() {
        finish();
    }

    protected boolean needCheckUpdate() {
        return true;
    }

    protected void showUpdateDialog(boolean isFetchData) {
        LogUtils.d(TAG, "show upgrade dialog, is fetch data " + isFetchData);
        CreateInterfaceTools.createUpdateManager().showDialogAndStartDownload(this, false, new UpdateOperation() {
            public void exitApp() {
                QBaseActivity.this.forceExitApp();
            }

            public void cancelUpdate() {
            }
        });
    }

    protected void startInstallApplication() {
        startInstallApp();
    }

    public void startInstallApp() {
        LogRecordUtils.logd(TAG, "startInstallApp");
        if (this.mIsAppDownloadComplete) {
            IAppDownloadManager downloadManager = CreateInterfaceTools.createAppDownloadManager();
            if (downloadManager != null && downloadManager.isComplete()) {
                downloadManager.startInstall();
                return;
            }
            return;
        }
        LogRecordUtils.logd(TAG, "app not download complete.");
    }

    protected boolean isCoolActivity() {
        return false;
    }

    protected void setScreenSaverEnable(boolean enable) {
        GetInterfaceTools.getIScreenSaver().setScreenSaverEnable(enable);
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public Context getBaseContext() {
        return super.getBaseContext();
    }

    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }
}
