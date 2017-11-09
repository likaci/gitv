package com.gala.video.app.epg.home.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.presenter.storage.StorageStatePresenter;
import com.gala.video.app.epg.home.presenter.time.TimeState;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class StatusPresenter {
    private static final String TAG = "home/StatusPresenter";
    private final View homeStatueBar;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final NetworkStatePresenter mNetworkIconController;
    private final View mRootView;
    private TimeState mTimeSate;

    class C06801 implements Runnable {
        C06801() {
        }

        public void run() {
            StatusPresenter.this.mNetworkIconController.updatePhoneState();
        }
    }

    public StatusPresenter(View root, Context context) {
        this.mRootView = root;
        this.homeStatueBar = root.findViewById(C0508R.id.epg_home_state_bar);
        this.mTimeSate = new TimeState((TextView) root.findViewById(C0508R.id.epg_time));
        ImageView mNetStateView = (ImageView) root.findViewById(C0508R.id.epg_net_state);
        ImageView mPhoneConnectedView = (ImageView) root.findViewById(C0508R.id.epg_phone_connect);
        StorageStatePresenter storageStatePresenter = new StorageStatePresenter(AppRuntimeEnv.get().getApplicationContext(), (ImageView) root.findViewById(C0508R.id.epg_usb_storage_state));
        this.mNetworkIconController = NetworkStatePresenter.getInstance();
        this.mNetworkIconController.init(context, mPhoneConnectedView, mNetStateView);
    }

    public void onStart() {
        if (this.mRootView != null) {
            Project.getInstance().getConfig().initHomeLogo((ImageView) this.mRootView.findViewById(C0508R.id.epg_logo_id));
        }
        this.mTimeSate.onStart();
        this.mNetworkIconController.onStart();
    }

    public void onStop() {
        this.mTimeSate.onStop();
    }

    public void onDestroy() {
        this.mTimeSate.onStop();
        this.mNetworkIconController.onDestroy();
    }

    public void onDlnaActionNotifyEvent(RequestKind kind) {
        if (kind != RequestKind.PULLVIDEO) {
            this.mHandler.post(new C06801());
        } else {
            LogUtils.m1571e(TAG, "unhandled dlna notify event kind(" + kind + ")");
        }
    }

    public void onStartUpEvent(ErrorEvent event) {
        this.mNetworkIconController.dealCode(event);
    }
}
