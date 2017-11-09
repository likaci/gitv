package com.gala.multiscreen.dmr.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.multiscreen.dmr.util.NetProfile;

public class NetworkReceiver {
    private boolean isRegister = false;
    private boolean isWaittingToRestart = false;
    private Context mContext;
    private MSHelper mGalaDlna = MSHelper.get();
    private long mLastRestart = 0;
    private int mRatio = 1;
    private Thread mThread;
    private BroadcastReceiver mWifiStateReceiver = new C01562();

    class C01551 implements Runnable {
        C01551() {
        }

        public void run() {
            while (System.currentTimeMillis() - NetworkReceiver.this.mLastRestart < ((long) (NetworkReceiver.this.mRatio * 1000))) {
                try {
                    Thread.sleep((long) (NetworkReceiver.this.mRatio * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            NetworkReceiver.this.mGalaDlna.onNetWorkChange();
            NetworkReceiver.this.isWaittingToRestart = false;
            NetworkReceiver.this.mRatio = 1;
        }
    }

    class C01562 extends BroadcastReceiver {
        C01562() {
        }

        public void onReceive(Context context, Intent intent) {
            MSLog.log("onReceive()!!");
            if (NetProfile.isAvaliable(NetworkReceiver.this.mContext)) {
                NetworkReceiver.this.onNetworkChange();
                return;
            }
            MSLog.log("onNetStateChanged() stop()");
            NetworkReceiver.this.mGalaDlna.stop();
        }
    }

    private Thread getThread() {
        this.mThread = new Thread(new C01551());
        return this.mThread;
    }

    private void onNetworkChange() {
        if (this.mRatio < 60) {
            this.mRatio *= 2;
        } else {
            this.mRatio = 60;
        }
        MSLog.log("on network change, wait " + this.mRatio + "s", LogType.BASE);
        this.mLastRestart = System.currentTimeMillis();
        if (!this.isWaittingToRestart) {
            getThread().start();
            this.isWaittingToRestart = true;
        }
    }

    public void register(Context context) {
        if (!this.isRegister && context != null) {
            this.isRegister = true;
            this.mContext = context;
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.mContext.registerReceiver(this.mWifiStateReceiver, filter);
        }
    }

    public void unregister() {
        if (this.mContext != null) {
            this.isRegister = false;
            this.mContext.unregisterReceiver(this.mWifiStateReceiver);
        }
    }
}
