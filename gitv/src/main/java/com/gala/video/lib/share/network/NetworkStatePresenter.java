package com.gala.video.lib.share.network;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.R;

public class NetworkStatePresenter extends NetworkStateLogic implements NetworkStateUIListener {
    private static String TAG = "EPG/home/NetworkStatePresenter";
    private static NetworkStatePresenter sInstance = new NetworkStatePresenter();
    private Runnable mCheckRunable;
    private NetworkStateUIActualOperateListener mUIActual;

    class NetworkStateJavaUI implements NetworkStateUIActualOperateListener {
        private static final int DIALOG_DISMISS_DELAY = 3000;
        private static final String TAG = "EPG/home/NetworkStateJavaUI";
        private Runnable mCancelDialogRunnable = new Runnable() {
            public void run() {
                NetworkStatePresenter.this.dismissDialog();
            }
        };
        private ImageView mNetImage;
        private ImageView mPhoneImage;

        NetworkStateJavaUI() {
        }

        public void init(ImageView phone, ImageView network) {
            this.mPhoneImage = phone;
            this.mNetImage = network;
        }

        public void onDestroy() {
            this.mPhoneImage = null;
            this.mNetImage = null;
        }

        public void onStop() {
            if (this.mNetImage != null) {
                this.mNetImage.removeCallbacks(NetworkStatePresenter.this.mCheckRunable);
            }
        }

        public void onWiredNetworkNormal() {
            setNetImageWired();
            NetworkStatePresenter.this.onNetworkAvailable();
        }

        public void onWifiNetworkNormal() {
            setNetImageWifi();
            NetworkStatePresenter.this.onNetworkAvailable();
        }

        public void setNetImageWired() {
            this.mNetImage.setImageResource(R.drawable.share_wired_connected);
        }

        public void setNetImageNull() {
            this.mNetImage.setImageResource(R.drawable.share_wifi_state_none);
            this.mPhoneImage.setVisibility(8);
        }

        public void setNetImageWifi() {
            this.mNetImage.setImageResource(new int[]{R.drawable.share_wifi_state_zero, R.drawable.share_wifi_state_one, R.drawable.share_wifi_state_two, R.drawable.share_wifi_state_three, R.drawable.share_wifi_state_four}[NetWorkManager.getInstance().getWifiStrength() % 5]);
        }

        public void updatePhoneState() {
            this.mPhoneImage.setVisibility(MultiScreen.get().isPhoneConnected() ? 0 : 8);
        }

        public void postCancelCallback() {
            this.mNetImage.postDelayed(this.mCancelDialogRunnable, 3000);
        }

        public void removeCancelCallback() {
            this.mNetImage.removeCallbacks(this.mCancelDialogRunnable);
        }

        public void showNetErrorAnimation(int delay) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "showNetErrorDialog() launcher net state check delay 10s");
            }
            this.mNetImage.setImageResource(R.drawable.share_net_search);
            AnimationDrawable ad = (AnimationDrawable) this.mNetImage.getDrawable();
            if (ad != null) {
                ad.start();
            }
            this.mNetImage.postDelayed(NetworkStatePresenter.this.mCheckRunable, (long) delay);
        }
    }

    private NetworkStatePresenter() {
        this.mUIActual = null;
        this.mCheckRunable = new Runnable() {
            public void run() {
                NetworkStatePresenter.this.checkNetworkStateFirst();
            }
        };
        this.mUIActual = new NetworkStateJavaUI();
    }

    public static NetworkStatePresenter getInstance() {
        return sInstance;
    }

    public void init(Context context, ImageView phone, ImageView network) {
        if (context == null || phone == null || network == null) {
            throw new NullPointerException("Null arguments for NetworkIconController()!");
        }
        this.mUIActual.init(phone, network);
        super.init(context, this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mUIActual.onDestroy();
    }

    public void onStop() {
        this.mUIActual.onStop();
    }

    public void onWiredNetworkNormal() {
        this.mUIActual.onWiredNetworkNormal();
    }

    public void onWifiNetworkNormal() {
        this.mUIActual.onWifiNetworkNormal();
    }

    public void setNetImageWired() {
        this.mUIActual.setNetImageWired();
    }

    public void setNetImageNull() {
        this.mUIActual.setNetImageNull();
    }

    public void setNetImageWifi() {
        this.mUIActual.setNetImageWifi();
    }

    public void updatePhoneState() {
        this.mUIActual.updatePhoneState();
    }

    public void postCancelCallback() {
        this.mUIActual.postCancelCallback();
    }

    public void removeCancelCallback() {
        this.mUIActual.removeCancelCallback();
    }

    public void showNetErrorAnimation(int delay) {
        this.mUIActual.showNetErrorAnimation(delay);
    }
}
