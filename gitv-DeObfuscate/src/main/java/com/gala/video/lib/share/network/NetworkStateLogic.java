package com.gala.video.lib.share.network;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class NetworkStateLogic {
    private static String TAG = "EPG/home/NetworkStateLogic";
    private int LAUNCH_DELAY = 10000;
    private Context mContext;
    private final DeviceCheckModel mDeviceCheckProvider = DeviceCheckModel.getInstance();
    private GlobalDialog mDialog;
    private FeedBackModel mFeedbackModel;
    private boolean mFirstCheckNetwork = false;
    private OnNetStateChangedListener mNetStateListener = new C17753();
    private NetWorkManager mNetworManager = NetWorkManager.getInstance();
    private OnClickListener mOkBtnListener = new C17731();
    private NetworkStateUIListener mUIListener = null;

    class C17731 implements OnClickListener {
        C17731() {
        }

        public void onClick(View v) {
            NetworkStateLogic.this.dismissDialog();
            NetworkStateLogic.this.mDialog = Project.getInstance().getControl().getGlobalDialog(NetworkStateLogic.this.mContext);
            NetworkStateLogic.this.mDialog.setParams(QLocalHostUtils.getLoaclHostInfo(NetworkStateLogic.this.mContext)).show();
        }
    }

    class C17742 implements StateCallback {
        C17742() {
        }

        public void getStateResult(int state) {
            NetworkStateLogic.this.mFirstCheckNetwork = false;
            NetworkStateLogic.this.showNetErrorByNetState(state);
        }
    }

    class C17753 implements OnNetStateChangedListener {
        C17753() {
        }

        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 0:
                    if (!NetworkStateLogic.this.mFirstCheckNetwork) {
                        NetworkStateLogic.this.mUIListener.setNetImageNull();
                        return;
                    }
                    return;
                case 1:
                    NetworkStateLogic.this.mUIListener.onWifiNetworkNormal();
                    return;
                case 2:
                    NetworkStateLogic.this.mUIListener.onWiredNetworkNormal();
                    return;
                case 3:
                    if (!NetworkStateLogic.this.mFirstCheckNetwork) {
                        NetworkStateLogic.this.mUIListener.setNetImageWifi();
                        return;
                    }
                    return;
                case 4:
                    if (!NetworkStateLogic.this.mFirstCheckNetwork) {
                        NetworkStateLogic.this.mUIListener.setNetImageWired();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void init(Context context, NetworkStateUIListener listener) {
        LogUtils.m1568d(TAG, "init()");
        if (context == null) {
            throw new NullPointerException("Null arguments for NetworkIconController()!");
        }
        this.mContext = context;
        this.mUIListener = listener;
        this.mNetworManager.unRegisterStateChangedListener(this.mNetStateListener);
        this.mNetworManager.registerStateChangedListener(this.mNetStateListener);
    }

    public void dealCode(ErrorEvent startupEvent) {
        LogUtils.m1568d(TAG, " start up event : " + startupEvent);
        if (ErrorEvent.C_SUCCESS != startupEvent && ErrorEvent.C_ERROR_MAC != startupEvent) {
            boolean isNetNone;
            this.mFeedbackModel = CreateInterfaceTools.createFeedbackFactory().createFeedBack(this.mDeviceCheckProvider);
            boolean hasFeedBckBtn = (ErrorEvent.C_ERROR_INTERNET == startupEvent || ErrorEvent.C_ERROR_NONET == startupEvent) ? false : true;
            if (ErrorEvent.C_ERROR_NONET == startupEvent) {
                isNetNone = true;
            } else {
                isNetNone = false;
            }
            showFirstErrorDialog(hasFeedBckBtn, isNetNone);
        }
    }

    public boolean checkStateIllegal() {
        LogUtils.m1568d(TAG, "checkStateIllegal()");
        if (this.mDeviceCheckProvider.getErrorEvent() == null || ErrorEvent.C_ERROR_MAC == this.mDeviceCheckProvider.getErrorEvent()) {
            LogUtils.m1571e(TAG, TAG + "---mDeviceCheckProvider event is null");
            handleNoData();
            return false;
        } else if (ErrorEvent.C_SUCCESS != this.mDeviceCheckProvider.getErrorEvent()) {
            dealCode(this.mDeviceCheckProvider.getErrorEvent());
            return false;
        } else {
            int state = NetWorkManager.getInstance().getNetState();
            if (state == 1 || state == 2) {
                return true;
            }
            if (state == 0 && NetWorkManager.getInstance().isReceiveSystemNetWorkConnectionMessage()) {
                showNetErrorDialog(C1632R.string.no_network);
                return false;
            } else if (state != 3 && state != 4) {
                return true;
            } else {
                showNetErrorDialog(Project.getInstance().getResProvider().getCannotConnInternet());
                return false;
            }
        }
    }

    public void handleNoData() {
        showExceptionTip(C1632R.string.click_recommand_tip, false);
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "handleNoData() request data update");
        }
        GetInterfaceTools.getStartupDataLoader().load(false);
    }

    public boolean handleNetWork() {
        LogUtils.m1568d(TAG, "handleNetWork()");
        int netState = NetWorkManager.getInstance().getNetState();
        if (netState == 0 && NetWorkManager.getInstance().isReceiveSystemNetWorkConnectionMessage()) {
            showNetErrorDialog(C1632R.string.no_network);
            return false;
        } else if (netState != 3 && netState != 4) {
            return true;
        } else {
            showNetErrorDialog(Project.getInstance().getResProvider().getCannotConnInternet());
            return false;
        }
    }

    public void onStart() {
    }

    public void onDestroy() {
        this.mNetworManager.unRegisterStateChangedListener(this.mNetStateListener);
        dismissDialog();
        this.mContext = null;
    }

    public void onStop() {
        dismissDialog();
    }

    private void showExceptionTip(int resid, boolean showBtn) {
        if (this.mContext != null) {
            showExceptionTip(this.mContext.getString(resid), showBtn);
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void showExceptionTip(String message, boolean showBtn) {
        dismissDialog();
        if (this.mContext != null) {
            this.mDialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
            if (showBtn) {
                this.mDialog.setParams(message, this.mContext.getString(C1632R.string.show_native_information), this.mOkBtnListener);
            } else {
                this.mDialog.setParams(message);
                this.mUIListener.postCancelCallback();
            }
            this.mDialog.show();
        }
    }

    protected void showNetErrorDialog(int resId) {
        LogUtils.m1568d(TAG, "showNetErrorDialog()---resId = " + resId);
        dismissDialog();
        if (this.mContext != null) {
            this.mDialog = CreateInterfaceTools.createNetworkProvider().makeDialogAsNetworkError(this.mContext, this.mContext.getString(resId));
            this.mDialog.show();
            this.mUIListener.removeCancelCallback();
        }
    }

    private void showFirstErrorDialog(boolean hasFeedBckBtn, boolean isNetNone) {
        LogUtils.m1568d(TAG, "showFirstErrorDialog()---hasFeedBckBtn = " + hasFeedBckBtn + ",isNetNone = " + isNetNone + ",mContext = " + this.mContext);
        dismissDialog();
        if (this.mContext == null) {
            return;
        }
        if (hasFeedBckBtn) {
            this.mFeedbackModel = CreateInterfaceTools.createFeedbackFactory().createFeedBack(this.mDeviceCheckProvider);
            IFeedbackDialogController feedBackController = CreateInterfaceTools.createFeedbackDialogController();
            feedBackController.init(this.mContext, null);
            feedBackController.setEventID(LogRecordUtils.getEventID());
            feedBackController.showQRDialog(this.mFeedbackModel, null);
            return;
        }
        showNetErrorDialog(isNetNone);
    }

    private void showNetErrorDialog(boolean isNetNone) {
        if (!isNetNone) {
            showNetErrorDialog(Project.getInstance().getResProvider().getCannotConnInternet());
        } else if (!Project.getInstance().getBuild().isHomeVersion() || this.LAUNCH_DELAY <= 0) {
            showNetErrorDialog(C1632R.string.no_network);
        } else {
            this.mFirstCheckNetwork = true;
            this.mUIListener.showNetErrorAnimation(this.LAUNCH_DELAY);
            this.LAUNCH_DELAY = 0;
        }
    }

    protected void dismissDialog() {
        if (this.mContext != null && this.mDialog != null && !((Activity) this.mContext).isFinishing()) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    protected void checkNetworkStateFirst() {
        this.mNetworManager.checkNetWork(new C17742());
    }

    private void showNetErrorByNetState(int state) {
        LogUtils.m1568d(TAG, "showNetErrorByNetState()---state = " + state);
        if (state == 0) {
            this.mUIListener.setNetImageNull();
            showNetErrorDialog(C1632R.string.no_network);
        } else if (state == 3) {
            this.mUIListener.setNetImageWifi();
            showNetErrorDialog(Project.getInstance().getResProvider().getCannotConnInternet());
        } else if (state == 4) {
            this.mUIListener.setNetImageWired();
            showNetErrorDialog(Project.getInstance().getResProvider().getCannotConnInternet());
        }
    }

    protected void onNetworkAvailable() {
        if (!(this.mDialog == null || this.mDialog.getContentTextView() == null || !Boolean.TRUE.equals(this.mDialog.getContentTextView().getTag()))) {
            dismissDialog();
        }
        this.mFirstCheckNetwork = false;
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "onNetworkAvailable() start data request, has internet");
        }
        LogUtils.m1571e(TAG, "force execute start up service error : " + this.mDeviceCheckProvider.getErrorEvent());
        if (ErrorEvent.C_ERROR_JSON == this.mDeviceCheckProvider.getErrorEvent() || ErrorEvent.C_ERROR_HTTP == this.mDeviceCheckProvider.getErrorEvent()) {
            GetInterfaceTools.getStartupDataLoader().forceLoad(true);
        }
    }
}
