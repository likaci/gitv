package com.gala.video.app.epg.apkupgrade;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.apkupgrade.dialog.GlobalUpdateDialog;
import com.gala.video.app.epg.apkupgrade.dialog.GlobalUpdateDialog.BackPressedListener;
import com.gala.video.app.epg.apkupgrade.downloader.ApkDownloader;
import com.gala.video.app.epg.apkupgrade.downloader.ApkDownloader.ApkCheckDownloadListener;
import com.gala.video.app.epg.apkupgrade.downloader.ApkDownloader.ApkDownloadListener;
import com.gala.video.app.epg.apkupgrade.downloader.ApkDownloader.DownloadSpeed;
import com.gala.video.app.epg.apkupgrade.install.ApkInstaller;
import com.gala.video.app.epg.widget.DialogUtils;
import com.gala.video.app.epg.widget.ProgressIndicator;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.mcto.ads.internal.net.TrackingConstants;

public class UpdateManager extends Wrapper {
    private static final boolean DIALOG_DISPLAY_DOWNINGSTATE = false;
    private static final boolean DIALOG_DISPLAY_TITLE = false;
    private static final boolean FORBID_NORMAL_BACK = false;
    private static final boolean LIMIT_DOWNLOAD_DEBUG = false;
    private static final int MAX_DLG_SHOWING_RETRY_TIMES = 1;
    private static final int MAX_RETRY_TIMES = 3;
    private static final int ON_CANCELED = 104;
    private static final int ON_ERROR = 102;
    private static final int ON_FINISHED = 103;
    private static final int ON_PROGRESS = 101;
    private static final int RESTART_DOWNLOAD = 100;
    private static final int RETRY_DLG_SHOWING_INTERVAL_TIME = 3000;
    private static final int RETRY_INTERVAL_TIME = 600000;
    private static final int UPDATE_DIALOG_MAX_COUNT = 5;
    private static UpdateManager mUpdateManager;
    private final String TAG = InterfaceKey.EPG_UM;
    private OnClickListener mBackDownLoadListener = new OnClickListener() {
        public void onClick(View v) {
            UpdateManager.this.mDownloadType = DownloadType.BACK_DOWNLOAD;
            UpdateManager.this.cancelDownloadProgressing();
            UpdateManager.this.mDownloader.setLimitSpeed(UpdateManager.this.getDownloadSpeedMode());
            UpdateManager.this.resetManualFlag();
        }
    };
    private OnClickListener mCancelDownLoadListener = new OnClickListener() {
        public void onClick(View v) {
            if (UpdateManager.this.mDownloader != null) {
                UpdateManager.this.mDownloader.stopDownloadUpdatePackage();
            }
            UpdateManager.this.cancelDownloadProgressing();
            if (UpdateManager.this.isForceUpdate(UpdateManager.this.mVersion)) {
                UpdateManager.this.exitApplication();
            } else {
                UpdateManager.this.cancelUpdate();
            }
            UpdateManager.this.resetManualFlag();
        }
    };
    private Context mContext = null;
    private DialogType mCurrentDlgType = DialogType.NONE;
    private int mDlgShowingDownloadFailTimes = 0;
    ApkDownloadListener mDownloadListener = new ApkDownloadListener() {
        public void onStart() {
            if (UpdateManager.this.mCurrentDlgType == DialogType.NONE && !UpdateManager.this.mIsManual) {
                SystemConfigPreference.setUpdateDialogCount(UpdateManager.this.mContext, 1);
            }
        }

        public void onProgress(int progress) {
            Message msg_progress = Message.obtain();
            msg_progress.what = 101;
            msg_progress.arg1 = progress;
            UpdateManager.this.mHandler.removeMessages(101);
            UpdateManager.this.mHandler.sendMessage(msg_progress);
        }

        public void onError(int errCode) {
            LogUtils.e(InterfaceKey.EPG_UM, "onError()--errCode=" + errCode);
            UpdateManager.this.mDownloaded = false;
            UpdateManager.this.mDownloadType = DownloadType.NONE;
            Message msg_progress = Message.obtain();
            msg_progress.what = 102;
            msg_progress.arg1 = errCode;
            UpdateManager.this.mHandler.removeMessages(102);
            UpdateManager.this.mHandler.sendMessage(msg_progress);
        }

        public void onExist() {
            LogUtils.d(InterfaceKey.EPG_UM, "onExist()");
            UpdateManager.this.mDownloaded = true;
            UpdateManager.this.mHandler.removeMessages(103);
            UpdateManager.this.mHandler.sendEmptyMessage(103);
        }

        public void onFinish() {
            UpdateManager.this.mDownloaded = true;
            UpdateManager.this.mExcuteOnFinishFlag = true;
            UpdateManager.this.mHandler.removeMessages(103);
            UpdateManager.this.mHandler.sendEmptyMessage(103);
        }

        public void onCancelled() {
            UpdateManager.this.mDownloaded = false;
            UpdateManager.this.clearDownloadFlagsForFinished();
            UpdateManager.this.mHandler.removeMessages(104);
            UpdateManager.this.mHandler.sendEmptyMessage(104);
        }
    };
    private DownloadType mDownloadType = DownloadType.NONE;
    private boolean mDownloaded = false;
    private ApkDownloader mDownloader;
    private boolean mExcuteOnFinishFlag = false;
    private OnClickListener mExitBtnListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                UpdateManager.this.dismissAlertDialog();
                UpdateManager.this.exitApplication();
            } catch (Exception e) {
            }
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    LogUtils.d(InterfaceKey.EPG_UM, "handleMessage()---RESTART_DOWNLOAD");
                    UpdateManager.this.startDownload(UpdateManager.this.mContext);
                    return true;
                case 101:
                    UpdateManager.this.progressingProc(msg.arg1);
                    return true;
                case 102:
                    LogUtils.d(InterfaceKey.EPG_UM, "handleMessage()---ON_ERROR");
                    UpdateManager.this.errorProc(msg.arg1);
                    return true;
                case 103:
                    LogUtils.d(InterfaceKey.EPG_UM, "handleMessage()---ON_FINISHED");
                    UpdateManager.this.downloadCompleteProc();
                    return true;
                case 104:
                    LogUtils.d(InterfaceKey.EPG_UM, "handleMessage()---ON_CANCELED");
                    UpdateManager.this.dismissAlertDialog();
                    return true;
                default:
                    LogUtils.d(InterfaceKey.EPG_UM, "handleMessage()---default");
                    return false;
            }
        }
    });
    private boolean mIsManual = false;
    private boolean mLimitShowCount = true;
    private UpdateOperation mOperation;
    private ProgressIndicator mProgressIndicator;
    private boolean mRequestShow = false;
    private int mSilentDownloadFailTimes = 0;
    private GlobalDialog mUpgradeAlertDialog;
    private AppVersion mVersion;

    private class BaseCancelButtonListener implements OnClickListener {
        private BaseCancelButtonListener() {
        }

        protected void sendPingbackClick() {
        }

        protected void specialProc() {
        }

        public void onClick(View v) {
            sendPingbackClick();
            UpdateManager.this.dismissAlertDialog();
            if (UpdateManager.this.isForceUpdate(UpdateManager.this.mVersion)) {
                UpdateManager.this.exitApplication();
            } else {
                UpdateManager.this.cancelUpdate();
            }
            UpdateManager.this.cancelUpdate();
            UpdateManager.this.resetManualFlag();
            specialProc();
        }
    }

    private class UpdateCancelButtonListener extends BaseCancelButtonListener implements OnClickListener {
        private boolean mNoRemind;

        private UpdateCancelButtonListener() {
            super();
            this.mNoRemind = false;
        }

        public void setDispNoRemind(boolean noRemind) {
            this.mNoRemind = noRemind;
        }

        protected void sendPingbackClick() {
            if (this.mNoRemind) {
                UpdateManager.this.pingbackPageClick(ClickType.NO_REMIND);
            } else {
                UpdateManager.this.pingbackPageClick(ClickType.NEXT_TIME);
            }
        }

        protected void specialProc() {
            if (this.mNoRemind) {
                UpdateManager.this.updateDialogCount(UpdateManager.this.mContext);
            }
            if (UpdateManager.this.mDownloader != null && UpdateManager.this.mDownloader.isDownloading()) {
                UpdateManager.this.mDownloader.setLimitSpeed(UpdateManager.this.getDownloadSpeedMode());
            }
        }
    }

    private class BackCancelButtonListener extends UpdateCancelButtonListener implements OnClickListener {
        private BackCancelButtonListener() {
            super();
        }

        protected void sendPingbackClick() {
        }

        protected void specialProc() {
            super.specialProc();
        }
    }

    private enum ClickType {
        NONE,
        UPDATE,
        NEXT_TIME,
        NO_REMIND,
        BACK_KEY,
        LEAVE
    }

    private enum DialogType {
        NONE,
        FORCE_DOWNLOAD,
        NORMAL_DOWNLOAD,
        EXIT_REMIND,
        FAIL,
        RETRY,
        PROGRESSING
    }

    private enum DownloadType {
        NONE,
        BACK_DOWNLOAD
    }

    private class UpdateButtonListener implements OnClickListener {
        private Context mContext;
        private boolean mSendPingback = true;

        public UpdateButtonListener(Context context) {
            this.mContext = context;
        }

        public void setSendPingback(boolean send) {
            this.mSendPingback = send;
        }

        public void onClick(View view) {
            try {
                if (this.mSendPingback) {
                    UpdateManager.this.pingbackPageClick(ClickType.UPDATE);
                }
                if (UpdateManager.this.mDownloaded) {
                    LogUtils.d(InterfaceKey.EPG_UM, "UpdateButtonListener.onClick()---start install");
                    UpdateManager.this.dismissAlertDialog();
                    if (!UpdateManager.this.startInstall(this.mContext, false)) {
                        LogUtils.d(InterfaceKey.EPG_UM, "UpdateButtonListener.onClick()---install fail, restart download");
                        UpdateManager.this.requestShowDialog(this.mContext, DialogType.PROGRESSING);
                        UpdateManager.this.startDownload(this.mContext);
                        return;
                    }
                    return;
                }
                UpdateManager.this.dismissAlertDialog();
                UpdateManager.this.requestShowDialog(this.mContext, DialogType.PROGRESSING);
                UpdateManager.this.startDownload(this.mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private UpdateManager() {
    }

    public static synchronized UpdateManager getInstance() {
        UpdateManager updateManager;
        synchronized (UpdateManager.class) {
            if (mUpdateManager == null) {
                mUpdateManager = new UpdateManager();
            }
            updateManager = mUpdateManager;
        }
        return updateManager;
    }

    private boolean isForceUpdate(AppVersion version) {
        if (version != null) {
            return version.isForceUpdate();
        }
        return false;
    }

    public boolean isManualUpdate() {
        if (this.mVersion != null) {
            return this.mVersion.isManualUpdate();
        }
        return false;
    }

    public void setLimitNotifyCount(boolean limit) {
        LogUtils.d(InterfaceKey.EPG_UM, "limit show dialog count = " + limit);
        this.mLimitShowCount = limit;
    }

    private boolean isNormalUpdate() {
        if (this.mVersion != null) {
            return this.mVersion.isNormalUpdate();
        }
        return false;
    }

    public void reset() {
        this.mRequestShow = false;
        this.mDownloaded = false;
    }

    public boolean isShowingDialog() {
        return this.mCurrentDlgType != DialogType.NONE;
    }

    public void setAppVersion(AppVersion version) {
        this.mVersion = version;
    }

    private void resetManualFlag() {
        this.mIsManual = false;
    }

    private void notifyUserNewIcon() {
        if (isManualUpdate() || isNormalUpdate()) {
            GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.CHECK_UPGRADE_EVENT);
        }
    }

    public boolean isNeedShowNewIcon() {
        if (this.mVersion == null || StringUtils.isEmpty(this.mVersion.getVersion())) {
            return false;
        }
        return true;
    }

    public boolean isNeedShowExitUpdateDialog() {
        if (this.mDownloaded && this.mExcuteOnFinishFlag) {
            return true;
        }
        return false;
    }

    public void showExitUpdateDialog(Context context, UpdateOperation operation) {
        LogUtils.d(InterfaceKey.EPG_UM, "showExitUpdateDialog()");
        if (operation == null) {
            throw new IllegalArgumentException("showExitUpdateDialog  operation must be not null !");
        }
        this.mOperation = operation;
        if (context == null) {
            LogUtils.e(InterfaceKey.EPG_UM, "showExitUpdateDialog()-----context is null");
            return;
        }
        this.mContext = context;
        requestShowDialog(context, DialogType.EXIT_REMIND);
    }

    public void showDialogAndStartDownload(Context context, boolean isManual, UpdateOperation operation) {
        LogUtils.d(InterfaceKey.EPG_UM, "showDialogAndStartDownload()");
        if (operation == null) {
            throw new IllegalArgumentException("showDialog  operation must be not null !");
        }
        this.mIsManual = isManual;
        this.mOperation = operation;
        if (LogUtils.mIsDebug) {
            LogUtils.i(InterfaceKey.EPG_UM, "showDialogAndStartDownload()---version = " + this.mVersion);
        }
        if (this.mVersion == null) {
            LogUtils.e(InterfaceKey.EPG_UM, "showDialogAndStartDownload()---version is null");
        } else if (context == null) {
            LogUtils.e(InterfaceKey.EPG_UM, "showDialogAndStartDownload()-----context is null");
        } else {
            this.mContext = context;
            if (!this.mDownloaded) {
                startDownload(context);
            }
            showUpdateDialog(context, this.mDownloaded);
            notifyUserNewIcon();
        }
    }

    private void initDownloader(Context context) {
        if (this.mDownloader == null) {
            this.mDownloader = new ApkDownloader(context);
        }
    }

    private void startDownload(Context context) {
        LogUtils.d(InterfaceKey.EPG_UM, "startDownload()");
        initDownloader(context);
        if (this.mDownloader.isDownloading()) {
            this.mDownloader.setLimitSpeed(getDownloadSpeedMode());
            return;
        }
        this.mDownloader.startDownloadApk(this.mVersion, getDownloadSpeedMode(), this.mDownloadListener);
        this.mDownloader.checkApkDownloadedAndDeleteInvalidFile(this.mVersion);
    }

    private void showUpdateDialog(final Context context, boolean downloaded) {
        LogUtils.d(InterfaceKey.EPG_UM, "showUpdateDialog()");
        if (isForceUpdate(this.mVersion)) {
            requestShowDialog(context, DialogType.FORCE_DOWNLOAD);
        } else if (this.mIsManual) {
            requestShowDialog(context, DialogType.NORMAL_DOWNLOAD);
        } else if (downloaded && this.mRequestShow) {
            requestShowDialog(context, DialogType.NORMAL_DOWNLOAD);
        } else if (downloaded && isNormalUpdate()) {
            showHomeNormalUpdateDialog(context);
        } else if (!checkDiskSpace()) {
            initDownloader(context);
            if (this.mDownloader != null) {
                this.mDownloader.onlyCheckApkDownloadSuccess(this.mVersion, new ApkCheckDownloadListener() {
                    public void onDownloaded(final boolean downloaded) {
                        UpdateManager.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (!downloaded && UpdateManager.this.isNormalUpdate()) {
                                    UpdateManager.this.showHomeNormalUpdateDialog(context);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private void showHomeNormalUpdateDialog(Context context) {
        int count = SystemConfigPreference.getUpdateDialogCount(context);
        LogUtils.d(InterfaceKey.EPG_UM, "showHomeNormalUpdateDialog()--count=" + count + ",limit show count = " + this.mLimitShowCount);
        if (!this.mLimitShowCount) {
            requestShowDialog(context, DialogType.NORMAL_DOWNLOAD);
        } else if (count <= 5) {
            requestShowDialog(context, DialogType.NORMAL_DOWNLOAD);
        }
    }

    private String getPingbackBlockWithCurrentState() {
        String block = "";
        if (this.mCurrentDlgType == DialogType.EXIT_REMIND) {
            return "exit";
        }
        if (isForceUpdate(this.mVersion)) {
            if (this.mIsManual) {
                return "checkforce";
            }
            return "force";
        } else if (this.mIsManual) {
            if (this.mDownloaded) {
                return TrackingConstants.TRACKING_EVENT_DOWNLOADED;
            }
            return "downloading";
        } else if (this.mExcuteOnFinishFlag) {
            return "running";
        } else {
            if (this.mUpgradeAlertDialog == null || !((GlobalUpdateDialog) this.mUpgradeAlertDialog).getNoRemindFlag()) {
                return "open_next";
            }
            return "open_noremind";
        }
    }

    private String getPingbackRseatWithCurrentState(ClickType clickType) {
        String rseat = "";
        if (clickType == ClickType.UPDATE) {
            return "update";
        }
        if (clickType == ClickType.BACK_KEY) {
            return "back";
        }
        if (clickType == ClickType.NEXT_TIME) {
            return "nexttime";
        }
        if (clickType == ClickType.NO_REMIND) {
            return "noremind";
        }
        if (clickType == ClickType.LEAVE) {
            return "leave";
        }
        return rseat;
    }

    private void pingbackPageShow() {
        String block = getPingbackBlockWithCurrentState();
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("bstp", "1").add("qtcurl", "update_dlg").add("block", block);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private void pingbackPageClick(ClickType clickType) {
        String block = getPingbackBlockWithCurrentState();
        String rseat = getPingbackRseatWithCurrentState(clickType);
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("rseat", rseat).add("rpage", "update_dlg").add("block", block).add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private void requestShowDialog(Context context, DialogType type) {
        LogUtils.d(InterfaceKey.EPG_UM, "requestShowDialog() type=" + type);
        clearDownloadFlagsForFinished();
        this.mRequestShow = false;
        if (this.mCurrentDlgType != type) {
            this.mCurrentDlgType = type;
            clearRestartDownload();
            IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
            if (iOperate.isShowScreenSaver()) {
                iOperate.hideScreenSaver();
            }
            if (type == DialogType.FORCE_DOWNLOAD) {
                initForceDownloadDialog(context);
                pingbackPageShow();
            } else if (type == DialogType.NORMAL_DOWNLOAD) {
                initNormalDownloadDialog(context);
                pingbackPageShow();
            } else if (type == DialogType.EXIT_REMIND) {
                initExitRemindDialog(context);
                pingbackPageShow();
            } else if (type == DialogType.FAIL) {
                initUpdateFailDialog(context);
                this.mUpgradeAlertDialog.show();
            } else if (type == DialogType.RETRY) {
                initRetryDialog(context);
                this.mUpgradeAlertDialog.show();
            } else if (type == DialogType.PROGRESSING) {
                initDownloadProgressing(context);
            } else {
                clearShowingFlag();
            }
        }
    }

    private boolean checkDiskSpace() {
        if (this.mDownloader == null || !StringUtils.isEmpty(this.mDownloader.getFilePath())) {
            return true;
        }
        return false;
    }

    private String makeLineBreakManaul(String tip) {
        LogUtils.d(InterfaceKey.EPG_UM, "UpdateManagertip=" + this.mVersion.getTip());
        String characters = "\\n";
        String contentMessage = "";
        while (tip.indexOf(characters) >= 0) {
            contentMessage = (contentMessage + tip.substring(0, tip.indexOf(characters))) + "\n";
            tip = tip.substring(tip.indexOf(characters) + characters.length());
        }
        return contentMessage + tip;
    }

    private void setUpdateTextInfo(Context context) {
        if (this.mUpgradeAlertDialog != null && (this.mUpgradeAlertDialog instanceof GlobalUpdateDialog)) {
            ((GlobalUpdateDialog) this.mUpgradeAlertDialog).setContentTextMessage(makeLineBreakManaul(this.mVersion.getTip()));
            if (checkDiskSpace() || this.mDownloaded) {
                updateProgressNormalDownloadDialog(this.mDownloaded ? 100 : 0);
            } else {
                dialogShowDownloadError();
            }
        }
    }

    private String getDialogTitle(Context context, DialogType type) {
        String title = "";
        return context.getString(R.string.update_normal_title) + context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    private void initForceDownloadDialog(Context context) {
        this.mUpgradeAlertDialog = new GlobalUpdateDialog(context);
        this.mUpgradeAlertDialog.setCancelable(false);
        ((GlobalUpdateDialog) this.mUpgradeAlertDialog).setBackPressedListener(new BackPressedListener() {
            public void onBackKeyPressed() {
                UpdateManager.this.pingbackPageClick(ClickType.BACK_KEY);
                UpdateManager.this.mExitBtnListener.onClick(null);
            }
        });
        this.mUpgradeAlertDialog.setParams(getDialogTitle(context, DialogType.FORCE_DOWNLOAD), context.getString(R.string.update_imm), new UpdateButtonListener(context));
        setUpdateTextInfo(context);
        this.mUpgradeAlertDialog.show();
    }

    private void updateProgressForceDownloadDialog(int progress) {
        updateProgressNormalDownloadDialog(progress);
    }

    private boolean checkDispNoRemind(Context context) {
        int count = SystemConfigPreference.getUpdateDialogCount(context);
        LogUtils.d(InterfaceKey.EPG_UM, "checkDispNoRemind()--count=" + count);
        if (this.mIsManual || count < 5 || !this.mLimitShowCount) {
            return false;
        }
        return true;
    }

    private void initNormalDownloadDialog(Context context) {
        LogUtils.d(InterfaceKey.EPG_UM, "initNormalDownloadDialog()");
        boolean noRemind = false;
        if (checkDispNoRemind(context)) {
            noRemind = true;
        } else {
            updateDialogCount(context);
        }
        this.mUpgradeAlertDialog = new GlobalUpdateDialog(context);
        ((GlobalUpdateDialog) this.mUpgradeAlertDialog).setBackPressedListener(new BackPressedListener() {
            public void onBackKeyPressed() {
                UpdateManager.this.pingbackPageClick(ClickType.BACK_KEY);
                new BackCancelButtonListener().onClick(null);
            }
        });
        String okBtnText = context.getString(R.string.update_imm);
        UpdateButtonListener okListener = new UpdateButtonListener(context);
        UpdateCancelButtonListener cancelListener = new UpdateCancelButtonListener();
        cancelListener.setDispNoRemind(noRemind);
        String cancelText = noRemind ? context.getString(R.string.update_not_notify) : context.getString(R.string.update_next_time);
        ((GlobalUpdateDialog) this.mUpgradeAlertDialog).setNoRemindFlag(noRemind);
        this.mUpgradeAlertDialog.setParams(getDialogTitle(context, DialogType.NORMAL_DOWNLOAD), okBtnText, okListener, cancelText, cancelListener);
        setUpdateTextInfo(context);
        this.mUpgradeAlertDialog.show();
    }

    private void initExitRemindDialog(Context context) {
        LogUtils.d(InterfaceKey.EPG_UM, "initExitRemindDialog()");
        this.mUpgradeAlertDialog = new GlobalUpdateDialog(context);
        ((GlobalUpdateDialog) this.mUpgradeAlertDialog).setBackPressedListener(new BackPressedListener() {
            public void onBackKeyPressed() {
                UpdateManager.this.pingbackPageClick(ClickType.BACK_KEY);
                new BackCancelButtonListener().onClick(null);
            }
        });
        String okBtnText = context.getString(R.string.update_exit_remind);
        this.mUpgradeAlertDialog.setParams(context.getString(R.string.update_exit_title), context.getString(R.string.update_exit_apk), new OnClickListener() {
            public void onClick(View v) {
                UpdateManager.this.pingbackPageClick(ClickType.LEAVE);
                UpdateManager.this.mExitBtnListener.onClick(null);
            }
        }, okBtnText, new UpdateButtonListener(context));
        setUpdateTextInfo(context);
        this.mUpgradeAlertDialog.show();
    }

    private void updateProgressNormalDownloadDialog(int progress) {
    }

    private void dialogShowDownloadError() {
    }

    private void updateDialogCount(Context context) {
        if (!this.mIsManual) {
            int count = SystemConfigPreference.getUpdateDialogCount(context) + 1;
            SystemConfigPreference.setUpdateDialogCount(context, count);
            LogUtils.d(InterfaceKey.EPG_UM, "updateDialogCount()--count=" + count);
        }
    }

    private void initDownloadProgressing(Context context) {
        if (isForceUpdate(this.mVersion)) {
            this.mProgressIndicator = DialogUtils.buildUpdateIndicator(context, this.mCancelDownLoadListener);
        } else {
            this.mProgressIndicator = DialogUtils.buildUpdateIndicator(context, this.mBackDownLoadListener);
            this.mProgressIndicator.setCancelButtonText(context.getString(R.string.dialog_app_download_back));
            this.mProgressIndicator.setUpdateTextMessage(context.getString(R.string.download_progress_message));
        }
        this.mProgressIndicator.show();
    }

    private void cancelDownloadProgressing() {
        this.mProgressIndicator.cancel();
        clearShowingFlag();
        this.mProgressIndicator = null;
    }

    private void initUpdateFailDialog(Context context) {
        this.mDownloaded = false;
        this.mUpgradeAlertDialog = Project.getInstance().getControl().getGlobalDialog(context);
        this.mUpgradeAlertDialog.setCancelable(false);
        this.mUpgradeAlertDialog.setParams(context.getString(R.string.update_access_error), context.getString(R.string.comfirm), new BaseCancelButtonListener(), null, null);
    }

    private void initRetryDialog(Context context) {
        this.mUpgradeAlertDialog = Project.getInstance().getControl().getGlobalDialog(context);
        this.mUpgradeAlertDialog.setCancelable(false);
        BaseCancelButtonListener cancelListener = new BaseCancelButtonListener();
        UpdateButtonListener retryListener = new UpdateButtonListener(context);
        retryListener.setSendPingback(false);
        this.mUpgradeAlertDialog.setParams(context.getString(R.string.update_network_error), context.getString(R.string.reupdate), retryListener, context.getString(R.string.Cancel), cancelListener);
    }

    private void clearShowingFlag() {
        this.mCurrentDlgType = DialogType.NONE;
    }

    private void dismissAlertDialog() {
        if (this.mUpgradeAlertDialog != null) {
            this.mUpgradeAlertDialog.dismiss();
            this.mUpgradeAlertDialog = null;
        }
        clearShowingFlag();
    }

    private boolean startInstall(Context context, boolean showDialog) {
        boolean bRet;
        if (new ApkInstaller().install(context, this.mDownloader.getFilePath(), this.mDownloader.getFileLength())) {
            bRet = true;
            if (isForceUpdate(this.mVersion)) {
                Process.killProcess(Process.myPid());
            }
        } else {
            bRet = false;
            if (showDialog) {
                requestShowDialog(context, DialogType.FAIL);
            }
        }
        return bRet;
    }

    private void clearDownloadFlagsForFinished() {
        this.mDownloadType = DownloadType.NONE;
        this.mSilentDownloadFailTimes = 0;
        this.mDlgShowingDownloadFailTimes = 0;
    }

    private void downloadCompleteProc() {
        if (this.mCurrentDlgType == DialogType.PROGRESSING) {
            cancelDownloadProgressing();
            startInstall(this.mContext, true);
        } else if (this.mCurrentDlgType == DialogType.FORCE_DOWNLOAD) {
            updateProgressForceDownloadDialog(100);
        } else if (this.mCurrentDlgType == DialogType.NORMAL_DOWNLOAD) {
            updateProgressNormalDownloadDialog(100);
        } else if (this.mCurrentDlgType == DialogType.NONE) {
            if (this.mDownloadType == DownloadType.BACK_DOWNLOAD) {
                this.mRequestShow = true;
                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_UPGRADE_EVENT);
            } else if (!this.mIsManual && isNormalUpdate()) {
                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_UPGRADE_EVENT);
            }
        }
        clearDownloadFlagsForFinished();
    }

    private void progressingProc(int progress) {
        if (this.mCurrentDlgType == DialogType.PROGRESSING) {
            this.mProgressIndicator.setDownloadProgress(progress);
        } else if (this.mCurrentDlgType == DialogType.FORCE_DOWNLOAD) {
            updateProgressForceDownloadDialog(progress);
        } else if (this.mCurrentDlgType == DialogType.NORMAL_DOWNLOAD) {
            updateProgressNormalDownloadDialog(progress);
        }
    }

    private void clearRestartDownload() {
        this.mHandler.removeMessages(100);
    }

    private void restartDownload(int delay) {
        this.mHandler.removeMessages(100);
        this.mHandler.sendEmptyMessageDelayed(100, (long) delay);
    }

    private void errorProc(int errCode) {
        LogUtils.d(InterfaceKey.EPG_UM, "errorProc()---errCode=" + errCode);
        if (this.mCurrentDlgType == DialogType.NONE) {
            this.mSilentDownloadFailTimes++;
            LogUtils.d(InterfaceKey.EPG_UM, "errorProc()---mSilentDownloadFailTimes=" + this.mSilentDownloadFailTimes);
            if (this.mSilentDownloadFailTimes > 3) {
                return;
            }
            if (errCode == 4) {
                this.mSilentDownloadFailTimes = 4;
                GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.STARTUP_UPGRADE_EVENT);
                return;
            }
            restartDownload(RETRY_INTERVAL_TIME);
        } else if (this.mCurrentDlgType == DialogType.FORCE_DOWNLOAD || this.mCurrentDlgType == DialogType.NORMAL_DOWNLOAD) {
            this.mDlgShowingDownloadFailTimes++;
            LogUtils.d(InterfaceKey.EPG_UM, "errorProc()---mDlgShowingDownloadFailTimes=" + this.mDlgShowingDownloadFailTimes);
            if (errCode == 4) {
                this.mDlgShowingDownloadFailTimes = 2;
                dialogShowDownloadError();
            } else if (this.mDlgShowingDownloadFailTimes <= 1) {
                restartDownload(3000);
            } else {
                dialogShowDownloadError();
            }
        } else if (this.mCurrentDlgType != DialogType.PROGRESSING) {
        } else {
            if (errCode == 4) {
                cancelDownloadProgressing();
                requestShowDialog(this.mContext, DialogType.FAIL);
                return;
            }
            cancelDownloadProgressing();
            requestShowDialog(this.mContext, DialogType.RETRY);
        }
    }

    private DownloadSpeed getDownloadSpeedMode() {
        DownloadSpeed mode = DownloadSpeed.HIGHEST;
        if (this.mDownloadType == DownloadType.BACK_DOWNLOAD) {
            return DownloadSpeed.NORMAL;
        }
        if (isForceUpdate(this.mVersion)) {
            return DownloadSpeed.HIGHEST;
        }
        if (this.mIsManual) {
            return DownloadSpeed.HIGHEST;
        }
        return DownloadSpeed.HIGHER;
    }

    public boolean hasUpdate() {
        if (this.mVersion == null) {
            return false;
        }
        CharSequence version = this.mVersion.getVersion();
        if (StringUtils.isEmpty(version) || version.equals(Project.getInstance().getBuild().getVersionString())) {
            return false;
        }
        if (!isForceUpdate(this.mVersion)) {
            return true;
        }
        LogUtils.d(InterfaceKey.EPG_UM, "hasUpdate()--force update--");
        return true;
    }

    private void cancelUpdate() {
        if (this.mOperation != null) {
            this.mOperation.cancelUpdate();
        }
    }

    private void exitApplication() {
        if (this.mDownloader != null) {
            this.mDownloader.stopDownloadUpdatePackage();
        }
        if (this.mOperation != null) {
            this.mOperation.exitApp();
        }
    }
}
