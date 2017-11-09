package com.gala.video.widget.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeMonitorHelper {
    private Context mContext;
    private OnHomePressedListener mOnHomePressedListener;
    private HomeKeyEventBroadCastReceiver mReceiver = new HomeKeyEventBroadCastReceiver();

    public interface OnHomePressedListener {
        void onHomePressed();
    }

    private class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
        static final String SYSTEM_HOME_KEY = "homekey";
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_RECENT_APPS = "recentapps";

        private HomeKeyEventBroadCastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason == null) {
                    return;
                }
                if (reason.equals(SYSTEM_HOME_KEY)) {
                    HomeMonitorHelper.this.mOnHomePressedListener.onHomePressed();
                } else {
                    reason.equals(SYSTEM_RECENT_APPS);
                }
            }
        }
    }

    public HomeMonitorHelper(OnHomePressedListener onHomePressedListener, Context context) {
        this.mContext = context;
        this.mOnHomePressedListener = onHomePressedListener;
        context.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void onDestory() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }
}
