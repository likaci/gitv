package com.gala.video.app.stub.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HomeMonitorHelper {
    private Context mCotext;
    private OnHomePressedListener mListener;
    private HomeKeyEventBroadCastReceiver mReceiver = new HomeKeyEventBroadCastReceiver();

    private class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
        private HomeKeyEventBroadCastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String reason = intent.getStringExtra("reason");
            Log.d("lzj", "action=" + action + ",reason=" + reason);
            if (action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") && reason != null && reason.equals("homekey") && HomeMonitorHelper.this.mListener != null) {
                HomeMonitorHelper.this.mListener.onHomePressed();
            }
        }
    }

    public interface OnHomePressedListener {
        void onHomePressed();
    }

    public HomeMonitorHelper(OnHomePressedListener onHomePressedListener, Context context) {
        this.mCotext = context;
        this.mListener = onHomePressedListener;
        this.mCotext.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void onDestory() {
        this.mListener = null;
        this.mCotext.unregisterReceiver(this.mReceiver);
    }
}
