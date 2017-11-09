package com.gala.video.app.epg.appdownload;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.appdownload.downloader.AppDownloader;
import com.gala.video.app.epg.appdownload.downloader.AppDownloader.AppDownloaderListener;
import com.gala.video.app.epg.appdownload.downloader.AppDownloader.DownloadSpeed;
import com.gala.video.app.epg.appdownload.utils.AppUtils;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.widget.ProgressIndicator;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IAppDownloadManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.io.File;

public class AppDownloadManager extends Wrapper {
    private static final String APP_SAVE_PATH = "app_save_path";
    private static final int DOWNLOAD_TYPE_BACKGROUND = 2;
    private static final int DOWNLOAD_TYPE_NORMAL = 1;
    private static final String KEY_FILE_PATH_NAME = "file_path_name";
    private static final int ON_CANCEL = 4;
    private static final int ON_ERROR = 22;
    private static final int ON_PROGRESS = 2;
    private static final int ON_START = 1;
    private static final int ON_SUCCESS = 8;
    private static final int RETRY_DELAY = 600000;
    private static final int RETRY_MAX_COUNT = 3;
    private static final String SHARE_NAME = "app_download";
    private static final String TAG = "AppDownloadManager";
    private static AppDownloadManager mInstance = null;
    private AppDownloader mAppDownloader;
    private OnClickListener mBackgroundButtonClickListener = new C05285();
    private Context mContext;
    private AppDownloaderListener mDownloadListener = new C05252();
    private int mDownloadType = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (AppDownloadManager.this.mDownloadType == 1) {
                        AppDownloadManager.this.showProgressDialog();
                        return;
                    }
                    return;
                case 2:
                    AppDownloadManager.this.updateProgress(msg.arg1);
                    return;
                case 8:
                    LogRecordUtils.logd("AppDownloadManager", "onSuccess: mDownloadType -> " + AppDownloadManager.this.mDownloadType + ", mIsBackPress -> " + AppDownloadManager.this.mIsBackPress);
                    if (AppDownloadManager.this.mDownloadType == 1) {
                        AppDownloadManager.this.cancelProgressDialog();
                        AppDownloadManager.this.startInstall();
                        return;
                    } else if (AppDownloadManager.this.mIsBackPress) {
                        AppDownloadManager.this.reset();
                        return;
                    } else {
                        GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.APP_DOWNLOAD_COMPLETE);
                        return;
                    }
                case 22:
                    AppDownloadManager.this.onError(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsBackPress = false;
    private boolean mIsComplete = false;
    private boolean mIsStartDownload = false;
    private OnNetStateChangedListener mNetStateListener = new C05263();
    private NetWorkManager mNetworManager = NetWorkManager.getInstance();
    private OnCancelListener mProgressCancelListener = new C05296();
    private ProgressIndicator mProgressIndicator;
    private PromotionAppInfo mPromotionAppInfo;
    private int mRetryCount = 0;
    private Runnable mRetryDownloadRunnable = new C05274();
    private GlobalDialog mRetryGlobalDialog;
    private GlobalDialog mTextGlobalDialog;

    class C05252 implements AppDownloaderListener {
        C05252() {
        }

        public void onStart() {
            LogRecordUtils.logd("AppDownloadManager", "onStart");
            AppDownloadManager.this.saveAppSavePath("");
            AppDownloadManager.this.mIsStartDownload = true;
            AppDownloadManager.this.mHandler.removeMessages(1);
            AppDownloadManager.this.mHandler.sendEmptyMessage(1);
        }

        public void onProgress(int progress) {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.arg1 = progress;
            AppDownloadManager.this.mHandler.removeMessages(2);
            AppDownloadManager.this.mHandler.sendMessage(msg);
        }

        public void onCancel() {
            LogRecordUtils.logd("AppDownloadManager", "onCancel");
            AppDownloadManager.this.reset();
            AppDownloadManager.this.mHandler.removeMessages(4);
            AppDownloadManager.this.mHandler.sendEmptyMessage(4);
        }

        public void onSuccess(File file, String path) {
            LogRecordUtils.logd("AppDownloadManager", "onSuccess");
            AppDownloadManager.this.saveAppSavePath(path);
            AppDownloadManager.this.mIsComplete = true;
            AppDownloadManager.this.mIsStartDownload = false;
            AppDownloadManager.this.mHandler.removeMessages(8);
            AppDownloadManager.this.mHandler.sendEmptyMessage(8);
            AppDownloadManager.this.sendChinaPokerDownloadFinishedClickPingback(8);
        }

        public void onError(int errCode) {
            LogRecordUtils.loge("AppDownloadManager", "onError: errCode -> " + errCode);
            AppDownloadManager.this.mIsStartDownload = false;
            Message msg = Message.obtain();
            msg.what = 22;
            msg.arg1 = errCode;
            AppDownloadManager.this.mHandler.removeMessages(22);
            AppDownloadManager.this.mHandler.sendMessage(msg);
            AppDownloadManager.this.sendChinaPokerDownloadFinishedClickPingback(22);
        }

        public void onExisted(String path) {
            LogRecordUtils.logd("AppDownloadManager", "onExisted: path -> " + path);
            AppDownloadManager.this.saveAppSavePath(path);
            AppDownloadManager.this.mIsStartDownload = false;
            AppDownloadManager.this.mIsComplete = true;
            AppDownloadManager.this.mHandler.removeMessages(8);
            AppDownloadManager.this.mHandler.sendEmptyMessage(8);
        }
    }

    class C05263 implements OnNetStateChangedListener {
        C05263() {
        }

        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    LogRecordUtils.logd("AppDownloadManager", "NetStateListener: connect. State -> " + newState);
                    if (AppDownloadManager.this.mDownloadType != 2 || AppDownloadManager.this.isDownloading()) {
                        AppDownloadManager.this.cancelTextDialog();
                    } else {
                        AppDownloadManager.this.startDownloadApp();
                    }
                    AppDownloadManager.this.mNetworManager.unRegisterStateChangedListener(AppDownloadManager.this.mNetStateListener);
                    return;
                default:
                    return;
            }
        }
    }

    class C05274 implements Runnable {
        C05274() {
        }

        public void run() {
            AppDownloadManager.this.startDownloadApp();
        }
    }

    class C05285 implements OnClickListener {
        C05285() {
        }

        public void onClick(View v) {
            AppDownloadManager.this.mDownloadType = 2;
            AppDownloadManager.this.cancelProgressDialog();
            AppDownloadManager.this.sendChinaPokerDownloadWindowClickPingback();
        }
    }

    class C05296 implements OnCancelListener {
        C05296() {
        }

        public void onCancel(DialogInterface dialog) {
            AppDownloadManager.this.mDownloadType = 2;
            AppDownloadManager.this.mIsBackPress = true;
        }
    }

    class C05307 implements OnClickListener {
        C05307() {
        }

        public void onClick(View v) {
            AppDownloadManager.this.mDownloadType = 1;
            AppDownloadManager.this.mIsBackPress = false;
            AppDownloadManager.this.startDownloadApp();
            AppDownloadManager.this.cancelRetryDialog();
        }
    }

    private AppDownloadManager() {
    }

    public static AppDownloadManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppDownloadManager();
        }
        return mInstance;
    }

    private void initialize() {
        initApkDownloader();
        initializeDialog();
    }

    private void initializeDialog() {
        this.mProgressIndicator = new ProgressIndicator(this.mContext);
        this.mProgressIndicator.setUpdateTextMessage(ResourceUtil.getStr(C0508R.string.download_app_message));
        this.mProgressIndicator.setCancelButtonText(ResourceUtil.getStr(C0508R.string.download_app_btn_background));
        this.mProgressIndicator.setCancelListener(this.mBackgroundButtonClickListener);
        this.mProgressIndicator.setCancelable(true);
        this.mProgressIndicator.setOnCanceledListener(this.mProgressCancelListener);
    }

    private void initApkDownloader() {
        if (this.mAppDownloader == null) {
            this.mAppDownloader = new AppDownloader(this.mContext);
        }
    }

    private void updateProgress(int progress) {
        switch (this.mDownloadType) {
            case 1:
                if (this.mProgressIndicator != null && progress >= 0 && progress <= 100) {
                    this.mProgressIndicator.setDownloadProgress(progress);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private GlobalDialog createTextDialog(String text) {
        GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
        dialog.setParams(text);
        dialog.getContentTextView().setTag(Boolean.TRUE);
        return dialog;
    }

    private GlobalDialog createRetryDialog(String text) {
        GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
        dialog.setParams(text, ResourceUtil.getStr(C0508R.string.download_app_btn_retry), new C05307());
        dialog.getContentTextView().setTag(Boolean.TRUE);
        return dialog;
    }

    private void showProgressDialog() {
        if (this.mProgressIndicator != null) {
            LogUtils.m1568d("AppDownloadManager", "showProgressDialog");
            this.mProgressIndicator.show();
            sendChinaPokerDownloadWindowShowPingback();
        }
    }

    private void sendChinaPokerDownloadWindowShowPingback() {
        if (this.mPromotionAppInfo != null && 2 == this.mPromotionAppInfo.getAppType()) {
            HomePingbackFactory.instance().createPingback(ShowPingback.CHINA_POKER_DOWNLOAD_WINDOW_SHOW_PINGBACK).addItem("qtcurl", "download").addItem("block", "斗地主").post();
        }
    }

    private void sendChinaPokerDownloadWindowClickPingback() {
        if (this.mPromotionAppInfo != null && 2 == this.mPromotionAppInfo.getAppType()) {
            HomePingbackFactory.instance().createPingback(ClickPingback.CHINA_POKER_DOWNLOAD_WINDOW_CLICK_PINGBACK).addItem("rpage", "download").addItem("block", "斗地主").addItem("rseat", "后台下载").post();
        }
    }

    private void sendChinaPokerDownloadFinishedClickPingback(int status) {
        if (this.mPromotionAppInfo != null && 2 == this.mPromotionAppInfo.getAppType()) {
            HomePingbackFactory.instance().createPingback(CommonPingback.CHINA_POKER_LOAD_FINISHED_PINGBACK).addItem(Keys.LDTYPE, "斗地主").addItem("st", status == 8 ? "1" : "0").addItem("ct", "160602_load").addItem(Keys.f2035T, "11").post();
        }
    }

    private void cancelProgressDialog() {
        if (this.mProgressIndicator != null) {
            this.mProgressIndicator.cancel();
        }
    }

    private void showTextDialog(String text) {
        LogRecordUtils.logd("AppDownloadManager", "showTextDialog: text -> " + text + ", dialog -> " + (this.mTextGlobalDialog != null));
        this.mTextGlobalDialog = createTextDialog(text);
        this.mTextGlobalDialog.show();
    }

    private void cancelTextDialog() {
        LogRecordUtils.logd("AppDownloadManager", "cancelTextDialog");
        if (this.mTextGlobalDialog != null) {
            this.mTextGlobalDialog.dismiss();
        }
    }

    private void showRetryDialog() {
        LogRecordUtils.logd("AppDownloadManager", "showRetryDialog: dialog -> " + (this.mRetryGlobalDialog == null));
        this.mRetryGlobalDialog = createRetryDialog(ResourceUtil.getStr(C0508R.string.download_app_fail));
        this.mRetryGlobalDialog.show();
    }

    private void cancelRetryDialog() {
        LogRecordUtils.logd("AppDownloadManager", "cancelRetryDialog");
        if (this.mRetryGlobalDialog != null) {
            this.mRetryGlobalDialog.setParams(null);
            this.mRetryGlobalDialog.dismiss();
        }
    }

    private void onError(int errorCode) {
        switch (errorCode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
                if (this.mDownloadType == 1) {
                    cancelProgressDialog();
                    showRetryDialog();
                    return;
                }
                retryDownload();
                return;
            case 4:
                if (this.mDownloadType == 1) {
                    cancelProgressDialog();
                    showTextDialog(ResourceUtil.getStr(C0508R.string.download_app_no_space));
                    return;
                }
                return;
            case 100:
                if (this.mDownloadType == 1) {
                    cancelProgressDialog();
                    showTextDialog(ResourceUtil.getStr(C0508R.string.no_network));
                }
                if (this.mNetworManager != null) {
                    this.mNetworManager.registerStateChangedListener(this.mNetStateListener);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private boolean startDownloadApp() {
        if (this.mAppDownloader == null) {
            return false;
        }
        LogRecordUtils.logd("AppDownloadManager", "startDownloadApp: start download.");
        this.mAppDownloader.setDownloadListener(this.mDownloadListener);
        boolean isDownload = this.mAppDownloader.startDownload(this.mPromotionAppInfo, DownloadSpeed.NORMAL);
        LogRecordUtils.logd("AppDownloadManager", "startDownloadApp: isDownload -> " + isDownload);
        return isDownload;
    }

    private void retryDownload() {
        LogRecordUtils.logd("AppDownloadManager", "retryDownload: mRetryCount -> " + this.mRetryCount);
        this.mRetryCount++;
        if (this.mRetryCount > 3) {
            LogRecordUtils.logd("AppDownloadManager", "retryDownload: retry over.");
            reset();
            return;
        }
        this.mHandler.removeCallbacks(this.mRetryDownloadRunnable);
        this.mHandler.postDelayed(this.mRetryDownloadRunnable, 600000);
    }

    private boolean isDownloading() {
        if (this.mAppDownloader != null) {
            return this.mAppDownloader.isDownloading();
        }
        return false;
    }

    private void saveAppSavePath(String path) {
        AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), APP_SAVE_PATH).save(getAppPckName(), path);
    }

    private String getAppSavePath() {
        return AppPreference.get(AppRuntimeEnv.get().getApplicationContext(), APP_SAVE_PATH).get(getAppPckName(), "");
    }

    private String getAppSaveFileName(String filePath) {
        String fileName = "";
        if (StringUtils.isEmpty((CharSequence) filePath)) {
            return fileName;
        }
        int start = filePath.lastIndexOf("/") + 1;
        int length = filePath.length();
        if (start < length) {
            return filePath.substring(start, length);
        }
        return fileName;
    }

    private String getAppPckName() {
        if (this.mPromotionAppInfo != null) {
            return this.mPromotionAppInfo.getAppPckName();
        }
        return "";
    }

    private boolean isFileExist() {
        String currFileName = getCurrFileName();
        String saveFilePath = getAppSavePath();
        String saveFileName = getAppSaveFileName(saveFilePath);
        LogRecordUtils.logd("AppDownloadManager", "isFileExist: currFileName -> " + currFileName + ", saveFileName -> " + saveFileName);
        if (!currFileName.equals(saveFileName)) {
            return false;
        }
        File file = new File(saveFilePath);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    private void reset() {
        if (this.mAppDownloader != null) {
            this.mAppDownloader.stopDownload();
        }
        this.mContext = null;
        this.mProgressIndicator = null;
        this.mTextGlobalDialog = null;
        this.mRetryGlobalDialog = null;
        this.mAppDownloader = null;
        this.mPromotionAppInfo = null;
        this.mRetryCount = 0;
        this.mDownloadType = 1;
        this.mIsBackPress = false;
        this.mIsComplete = false;
        this.mIsStartDownload = false;
    }

    private boolean validationData(PromotionAppInfo promotionAppInfo) {
        if (promotionAppInfo == null) {
            LogRecordUtils.loge("AppDownloadManager", "validationData: promotionAppInfo is null.");
            return false;
        } else if (StringUtils.isEmpty(promotionAppInfo.getAppPckName())) {
            LogRecordUtils.loge("AppDownloadManager", "validationData: packageName is null.");
            return false;
        } else if (StringUtils.isEmpty(promotionAppInfo.getAppDownloadUrl())) {
            LogRecordUtils.loge("AppDownloadManager", "validationData: downloadUrl is null.");
            return false;
        } else if (StringUtils.isEmpty(promotionAppInfo.getMd5())) {
            LogRecordUtils.loge("AppDownloadManager", "validationData: md5 is null.");
            return false;
        } else if (!StringUtils.isEmpty(promotionAppInfo.getAppVerName())) {
            return true;
        } else {
            LogRecordUtils.loge("AppDownloadManager", "validationData: version is null.");
            return false;
        }
    }

    private String getCurrFileName() {
        String fileName = "";
        if (this.mPromotionAppInfo == null || !validationData(this.mPromotionAppInfo)) {
            return fileName;
        }
        return this.mPromotionAppInfo.getAppPckName() + "_" + this.mPromotionAppInfo.getAppVerName() + ".apk";
    }

    private void saveFileName() {
        if (this.mAppDownloader != null) {
            LogRecordUtils.logd("AppDownloadManager", "saveFileName: filePath -> " + this.mAppDownloader.getFilePath());
            AppPreference.get(this.mContext, SHARE_NAME).save(KEY_FILE_PATH_NAME, this.mAppDownloader.getFilePath());
        }
    }

    private String getPrevFilePath() {
        return AppPreference.get(this.mContext, SHARE_NAME).get(KEY_FILE_PATH_NAME, "");
    }

    private void deletePrevFileName() {
        CharSequence prevPath = getPrevFilePath();
        String prevName = "";
        if (!StringUtils.isEmpty(prevPath)) {
            int start = prevPath.lastIndexOf("/") + 1;
            int length = prevPath.length();
            if (start < length) {
                prevName = prevPath.substring(start, length);
            }
        }
        String currName = getCurrFileName();
        LogRecordUtils.logd("AppDownloadManager", "currName -> " + currName + ", prevName -> " + prevName + ", prevPath -> " + prevPath);
        if (!currName.equals(prevName)) {
            File file = new File(prevPath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    private void saveUUID() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            FileUtil.writeFile(Environment.getExternalStorageDirectory() + "gala/uuid.txt", CreateInterfaceTools.createBuildInterface().getVrsUUID());
        }
    }

    public void startInstall() {
        if (this.mAppDownloader != null) {
            AppUtils.installApp(this.mContext, this.mAppDownloader.getFilePath());
        }
        reset();
    }

    public boolean isComplete() {
        return this.mIsComplete;
    }

    public void post() {
        GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.APP_DOWNLOAD_COMPLETE);
    }

    public boolean downloadApp(Context context, PromotionAppInfo promotionAppInfo) {
        LogRecordUtils.logd("AppDownloadManager", "startDownloadApp: recommendAppInfo -> " + promotionAppInfo.toString());
        this.mContext = context;
        if (!checkNetWork()) {
            return true;
        }
        if (this.mPromotionAppInfo != null) {
            this.mContext = context;
            LogRecordUtils.logd("AppDownloadManager", "downloadApp: restartDownload, isDownloading -> " + isDownloading() + ", equals -> " + promotionAppInfo.equals(this.mPromotionAppInfo) + ", mIsStartDownload -> " + this.mIsStartDownload);
            if (promotionAppInfo.equals(this.mPromotionAppInfo) && isDownloading()) {
                this.mRetryCount = 0;
                this.mDownloadType = 1;
                this.mIsBackPress = false;
                this.mHandler.removeCallbacks(this.mRetryDownloadRunnable);
                if (!this.mIsStartDownload) {
                    return true;
                }
                showProgressDialog();
                updateProgress(this.mAppDownloader.getLastProgress());
                return true;
            }
        }
        LogRecordUtils.logd("AppDownloadManager", "downloadApp: startDownload");
        reset();
        this.mPromotionAppInfo = promotionAppInfo;
        if (isFileExist()) {
            AppUtils.installApp(context, getAppSavePath());
            return true;
        }
        this.mContext = context;
        initialize();
        return startDownloadApp();
    }

    private boolean checkNetWork() {
        if (isConnectNetWork()) {
            return true;
        }
        showTextDialog(ResourceUtil.getStr(C0508R.string.no_network));
        if (this.mNetworManager != null) {
            this.mNetworManager.registerStateChangedListener(this.mNetStateListener);
        }
        return false;
    }

    private boolean isConnectNetWork() {
        int netState = NetWorkManager.getInstance().getNetState();
        LogRecordUtils.logd("AppDownloadManager", "isConnectNetWork: netState -> " + netState);
        if (netState == 1 || netState == 2) {
            return true;
        }
        return false;
    }
}
