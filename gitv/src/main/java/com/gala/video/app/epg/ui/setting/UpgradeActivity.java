package com.gala.video.app.epg.ui.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.AppVersion;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DevicesInfo;

public class UpgradeActivity extends QMultiScreenActivity {
    private static final int CHECK_VERSION_FAIL = 0;
    private static final int CHECK_VERSION_SUCCESS = 1;
    private static final String TAG = "UpgradeActivity";
    private TextView mCheckingTip;
    private ProgressBarItem mProgressBar;
    private Button mUpdateBtn;
    private Handler mUpdateUIHandler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UpgradeActivity.this.handleSuccess((AppVersion) msg.obj);
                    break;
                default:
                    UpgradeActivity.this.handleException((String) msg.obj);
                    break;
            }
            return false;
        }
    });
    private TextView mUpgradeMessage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_upgrade);
        String clientVersion = Project.getInstance().getBuild().getVersionString();
        ((TextView) findViewById(R.id.epg_upgrade_cur_version)).setText(getResources().getString(R.string.current_version) + clientVersion);
        this.mUpdateBtn = (Button) findViewById(R.id.epg_btn_start_upgrade);
        this.mCheckingTip = (TextView) findViewById(R.id.epg_checking_tip);
        this.mProgressBar = (ProgressBarItem) findViewById(R.id.epg_check_update_progress);
        onStartChecking();
        startCheckUpgrade(clientVersion);
    }

    private void startCheckUpgrade(String clientVersion) {
        TVApi.deviceCheckP.callSync(new IApiCallback<ApiResultDeviceCheck>() {
            public void onSuccess(ApiResultDeviceCheck apiResult) {
                AppVersion ver = new AppVersion();
                DeviceCheck deviceCheck = new DeviceCheck();
                if (deviceCheck != null) {
                    ver.setVersion(deviceCheck.version);
                    ver.setTip(deviceCheck.tip);
                    ver.setUrl(deviceCheck.url);
                    ver.setUpgradeType(deviceCheck.upgradeType);
                }
                Message msg = UpgradeActivity.this.mUpdateUIHandler.obtainMessage(1);
                msg.obj = ver;
                UpgradeActivity.this.mUpdateUIHandler.sendMessage(msg);
            }

            public void onException(ApiException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(UpgradeActivity.TAG, "UpgradeActivity---checkUpdate()---onException()---e=" + e.getCode());
                }
                LogUtils.d(UpgradeActivity.TAG, "code is " + e.getCode() + ",message=" + e.getMessage());
                Message msg = UpgradeActivity.this.mUpdateUIHandler.obtainMessage(0);
                msg.obj = e.getCode();
                UpgradeActivity.this.mUpdateUIHandler.sendMessage(msg);
            }
        }, DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
    }

    private void updateCheckStatus(String status) {
        this.mUpgradeMessage = (TextView) findViewById(R.id.epg_upgrade_message);
        this.mUpgradeMessage.setText(status);
    }

    private void onStartChecking() {
        LogUtils.d(TAG, "onStartChecking");
        this.mCheckingTip.setText(getResources().getString(R.string.checking_update));
    }

    private void onNewVersionFound(final AppVersion version) {
        updateCheckStatus(getResources().getString(R.string.newest_version) + version + "。");
        this.mUpdateBtn.setVisibility(0);
        this.mUpdateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UpgradeActivity.this.onUpgradeClicked(version);
                UpgradeActivity.this.mUpdateBtn.setVisibility(4);
            }
        });
    }

    private void onCheckingFailed() {
        updateCheckStatus(getResources().getString(R.string.check_update_exception));
    }

    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause->>>visibility " + this.mProgressBar.getVisibility());
    }

    private void onUpgradeClicked(AppVersion version) {
        UpdateManager update = UpdateManager.getInstance();
        update.setAppVersion(version);
        update.showDialogAndStartDownload(this, true, new UpdateOperation() {
            public void exitApp() {
                UpgradeActivity.this.finish();
            }

            public void cancelUpdate() {
            }
        });
    }

    private void handleSuccess(AppVersion version) {
        hidenCheckStatus();
        LogUtils.d(TAG, "handleSuccess->>>version is " + version.getVersion());
        if (version == null) {
            return;
        }
        if (shouldUpgrade(version)) {
            onNewVersionFound(version);
        } else {
            updateCheckStatus(getResources().getString(R.string.not_need_update));
        }
    }

    private void handleException(String code) {
        hidenCheckStatus();
        LogUtils.d(TAG, "handleException->>>code is " + code + ",visibility=" + this.mProgressBar.getVisibility());
        if ("E000012".equals(code)) {
            updateCheckStatus(getResources().getString(R.string.not_need_update));
        } else {
            onCheckingFailed();
        }
    }

    private void hidenCheckStatus() {
        this.mProgressBar.setVisibility(4);
        this.mCheckingTip.setVisibility(4);
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_upgrade_setting);
    }

    private boolean shouldUpgrade(AppVersion version) {
        boolean isNeedUpdate = false;
        if (version != null) {
            if (Project.getInstance().getBuild().getVersionString().equals(version.getVersion())) {
                isNeedUpdate = false;
            } else {
                isNeedUpdate = true;
            }
            if (isNeedUpdate) {
                LogUtils.i("UPDATE", String.format("Cur: %s, New: %s", new Object[]{curVer, version.getVersion()}));
            }
        }
        return isNeedUpdate;
    }
}
