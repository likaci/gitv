package com.gala.video.app.epg.home.ads;

import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import com.gala.video.app.epg.home.ads.model.ExitAppAdModel;
import com.gala.video.app.epg.home.ads.task.AdImageResRequestTask;
import com.gala.video.app.epg.home.ads.task.ExitAppDisplayAdInfoRequestTask;
import com.gala.video.app.epg.home.ads.task.ExitAppDisplayAdInfoRequestTask.OnExitAdRequestListener;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.ExitOperateImageProvider;
import com.gala.video.app.epg.home.data.provider.StartOperateImageProvider;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AdImageResTaskStrategy {
    private static final int AD_EXIT_AD_LOAD = 103;
    private static final long AD_EXIT_AD_REQUEST_TASK_INTERVAL = 60000;
    private static final int AD_IMAGE_OPERATE_LOAD = 102;
    private static final long AD_IMAGE_OPERATE_LOAD_INTERVAL = 60000;
    private static final int AD_IMAGE_RES_LOAD = 101;
    private static final long AD_IMAGE_RES_LOAD_INTERVAL = 60000;
    private static final long EXIT_AD_IMAGE_LIMIT_TIME_OUT = 3600000;
    private static final int INSTALLED_APK_INFO = 105;
    private static final long INSTALLED_APK_INFO_INTERVAL = 300000;
    private static final int START_OPERATE_IMAGE_LOAD = 104;
    private static final long START_OPERATE_IMAGE_LOAD_INTERVAL = 60000;
    private static final String TAG = "ads/AdImageResTaskStrategy";
    private static final AdImageResTaskStrategy sInstance = new AdImageResTaskStrategy();
    private TimeHandler mAdTimeHandler = new TimeHandler();
    private OnExitAdRequestListener mExitAdRequestListener = new C05582();
    private volatile boolean mHasExitAd = false;
    private volatile boolean mHasRequestExitAd = false;

    class C05571 implements Runnable {
        C05571() {
        }

        public void run() {
            String competeApkValue = "";
            List<String> competeApkPackageNameList = new ArrayList();
            competeApkPackageNameList.add("net.myvst.v2");
            competeApkPackageNameList.add("com.letv.tv");
            competeApkPackageNameList.add("com.cibn.tv");
            competeApkPackageNameList.add("com.sohuott.tv.vod");
            competeApkPackageNameList.add("com.pplive.androidxl");
            competeApkPackageNameList.add("com.ktcp.video");
            competeApkPackageNameList.add("com.starcor.mango");
            competeApkPackageNameList.add("com.moretv.android");
            competeApkPackageNameList.add("com.togic.livevideo");
            competeApkPackageNameList.add("com.shafa.market");
            competeApkPackageNameList.add("com.dangbeimarket");
            competeApkPackageNameList.add("com.molitv.android");
            competeApkPackageNameList.add("com.js.litchi");
            competeApkPackageNameList.add("com.bilibili.tv");
            competeApkPackageNameList.add("com.sanbuapp.pangzhetv1");
            competeApkPackageNameList.add("cn.com.wasu.main");
            competeApkPackageNameList.add("com.sohuott.tv.vod.xiaomi");
            competeApkPackageNameList.add("com.ktcp.tvvideo");
            competeApkPackageNameList.add("cn.beevideo");
            List<PackageInfo> packages = AppRuntimeEnv.get().getApplicationContext().getPackageManager().getInstalledPackages(8192);
            if (!ListUtils.isEmpty((List) packages)) {
                for (PackageInfo packageInfo : packages) {
                    String packageName = packageInfo.packageName;
                    if (competeApkPackageNameList.contains(packageName)) {
                        StringBuilder append = new StringBuilder().append(competeApkValue);
                        if (!StringUtils.isEmpty((CharSequence) competeApkValue)) {
                            packageName = ";" + packageName;
                        }
                        competeApkValue = append.append(packageName).toString();
                    }
                }
            }
            HomePingbackFactory.instance().createPingback(CommonPingback.LOCAL_INSTALLED_APP_INFO_PINGBACK).addItem(Keys.f2035T, "11").addItem("ct", "170626_competitor").addItem(Keys.COMPETEAPK, competeApkValue).post();
        }
    }

    class C05582 implements OnExitAdRequestListener {
        C05582() {
        }

        public void onSuccess(ExitAppAdModel exitAppAdInfo, Bitmap bitmap) {
            LogUtils.m1568d(AdImageResTaskStrategy.TAG, "result: request exit ad data success.");
            AdImageResTaskStrategy.this.mHasExitAd = true;
            AdImageResTaskStrategy.this.mHasRequestExitAd = true;
        }

        public void onFailed() {
            AdImageResTaskStrategy.this.mHasExitAd = false;
            AdImageResTaskStrategy.this.mHasRequestExitAd = true;
            LogUtils.m1568d(AdImageResTaskStrategy.TAG, "result: request exit ad data failed.");
        }

        public void onNoAdData() {
            AdImageResTaskStrategy.this.mHasExitAd = false;
            AdImageResTaskStrategy.this.mHasRequestExitAd = true;
            LogUtils.m1568d(AdImageResTaskStrategy.TAG, "result:  no exit ad  data");
        }

        public void onTimeOut() {
            AdImageResTaskStrategy.this.mHasExitAd = false;
            AdImageResTaskStrategy.this.mHasRequestExitAd = true;
            LogUtils.m1568d(AdImageResTaskStrategy.TAG, "result: request exit ad data time out.");
        }
    }

    private static class TimeHandler extends Handler {
        private final WeakReference<AdImageResTaskStrategy> mStrategy;

        private TimeHandler(AdImageResTaskStrategy strategy) {
            this.mStrategy = new WeakReference(strategy);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    LogUtils.m1568d(AdImageResTaskStrategy.TAG, "start load Ad image resource");
                    new AdImageResRequestTask().execute();
                    return;
                case 102:
                    LogUtils.m1568d(AdImageResTaskStrategy.TAG, "start load exit operate image");
                    ((AdImageResTaskStrategy) this.mStrategy.get()).downloadExitOperateImage();
                    return;
                case 103:
                    LogUtils.m1568d(AdImageResTaskStrategy.TAG, "start request exit ad data");
                    ExitAppDisplayAdInfoRequestTask task = new ExitAppDisplayAdInfoRequestTask(3600000);
                    task.setOnExitAdRequestListener(((AdImageResTaskStrategy) this.mStrategy.get()).mExitAdRequestListener);
                    task.execute();
                    return;
                case 104:
                    LogUtils.m1568d(AdImageResTaskStrategy.TAG, "start download, start operate image");
                    ((AdImageResTaskStrategy) this.mStrategy.get()).downloadStartOperateImage();
                    return;
                case 105:
                    LogUtils.m1568d(AdImageResTaskStrategy.TAG, "start statistics installed APK");
                    ((AdImageResTaskStrategy) this.mStrategy.get()).sendInstalledAppInfoPingback();
                    return;
                default:
                    return;
            }
        }
    }

    private AdImageResTaskStrategy() {
    }

    public static AdImageResTaskStrategy getInstance() {
        return sInstance;
    }

    public boolean hasExitAd() {
        return this.mHasExitAd;
    }

    public boolean hasRequestExitAd() {
        return this.mHasRequestExitAd;
    }

    public void fetchAdImageRes() {
        LogUtils.m1568d(TAG, "fetchAdImageRes");
        this.mAdTimeHandler.removeCallbacksAndMessages(null);
        this.mAdTimeHandler.sendEmptyMessageDelayed(101, 60000);
        this.mAdTimeHandler.sendEmptyMessageDelayed(102, 60000);
        this.mAdTimeHandler.sendEmptyMessageDelayed(103, 60000);
        this.mAdTimeHandler.sendEmptyMessageDelayed(104, 60000);
        this.mAdTimeHandler.sendEmptyMessageDelayed(105, 300000);
    }

    private void sendInstalledAppInfoPingback() {
        new Thread(new C05571()).start();
    }

    private void downloadExitOperateImage() {
        ExitOperateImageProvider.getInstance().download();
    }

    private void downloadStartOperateImage() {
        StartOperateImageProvider.getInstance().download();
    }

    public void stopTask() {
        this.mAdTimeHandler.removeCallbacksAndMessages(null);
    }
}
