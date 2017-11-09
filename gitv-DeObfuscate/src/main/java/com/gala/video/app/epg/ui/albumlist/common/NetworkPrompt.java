package com.gala.video.app.epg.ui.albumlist.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;

public class NetworkPrompt {
    private static final String TAG = "EPG/album4/NetworkPrompt";
    private Context mContext;
    private OnNetStateChangedListener mNetStateListener = new C08101();
    private NetWorkManager mNetworManager = NetWorkManager.getInstance();
    private INetworkStateListener mOuterStateListener;
    private GlobalDialog mPromptDialog;

    class C08101 implements OnNetStateChangedListener {
        C08101() {
        }

        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    if (NetworkPrompt.this.mOuterStateListener != null) {
                        NetworkPrompt.this.mOuterStateListener.onConnected(oldState != newState);
                    }
                    NetworkPrompt.this.dismissPrompt();
                    return;
                default:
                    return;
            }
        }
    }

    class C08112 implements OnClickListener {
        C08112() {
        }

        public void onClick(View v) {
            boolean isIntentSafe = false;
            NetworkPrompt.this.dismissPrompt();
            PackageManager packageManager = ((Activity) NetworkPrompt.this.mContext).getPackageManager();
            Intent NetworkSettingsIntent = new Intent(Project.getInstance().getControl().getNetWorkSettingAction());
            if (!packageManager.queryIntentActivities(NetworkSettingsIntent, 0).isEmpty()) {
                isIntentSafe = true;
            }
            if (isIntentSafe) {
                PageIOUtils.activityIn(NetworkPrompt.this.mContext, NetworkSettingsIntent);
                return;
            }
            PageIOUtils.activityIn(NetworkPrompt.this.mContext, new Intent("android.settings.WIFI_SETTINGS"));
        }
    }

    public interface INetworkStateListener {
        void onConnected(boolean z);
    }

    public NetworkPrompt(Context context) {
        this.mContext = context;
    }

    public void registerNetworkListener(INetworkStateListener listener) {
        LogUtils.m1568d(TAG, "registerNetworkListener !!");
        this.mOuterStateListener = listener;
        this.mNetworManager.registerStateChangedListener(this.mNetStateListener);
    }

    public void unregisterNetworkListener() {
        LogUtils.m1568d(TAG, "unregisterNetworkListener !!");
        this.mOuterStateListener = null;
        this.mNetworManager.unRegisterStateChangedListener(this.mNetStateListener);
    }

    public void showPrompt() {
        if (this.mPromptDialog == null || !this.mPromptDialog.isShowing()) {
            String okBtnText = this.mContext.getString(C0508R.string.albumlist_networksetting);
            OnClickListener onClickListener = new C08112();
            this.mPromptDialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
            this.mPromptDialog.setParams(this.mContext.getString(C0508R.string.albumlist_no_network), okBtnText, onClickListener, null, null);
            this.mPromptDialog.getContentTextView().setGravity(17);
            this.mPromptDialog.show();
        }
    }

    public void dismissPrompt() {
        if (this.mPromptDialog != null && this.mPromptDialog.isShowing()) {
            this.mPromptDialog.dismiss();
        }
    }
}
