package com.qiyi.tv.client.impl.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.playstate.OnPlayStateChangedListener;
import com.qiyi.tv.client.feature.playstate.PlayStateManager;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class p implements PlayStateManager {
    private BroadcastReceiver a = new BroadcastReceiver(this) {
        private /* synthetic */ p a;

        {
            this.a = r1;
        }

        public final void onReceive(Context context, Intent intent) {
            Log.d("PlayStateManagerImpl", "onReceive(" + intent + ") mStarted=" + this.a.f856a);
            if (Extras.ACTION_PLAY_STATE.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                int parsePlayState = ParamsHelper.parsePlayState(extras);
                Media parseMedia = ParamsHelper.parseMedia(extras);
                synchronized (this.a.f856a) {
                    if (this.a.f856a != null) {
                        this.a.f856a.onPlayStateChanged(parsePlayState, parseMedia);
                    }
                }
            }
        }
    };
    private Context f855a;
    private OnPlayStateChangedListener f856a = null;
    private boolean f857a;

    public p(Context context) {
        this.f855a = context;
    }

    public final synchronized void setOnPlayStateChangedListener(OnPlayStateChangedListener listener) {
        Log.d("PlayStateManagerImpl", "setOnPlayStateChangedListener(" + listener + ")");
        this.f856a = listener;
    }

    public final synchronized void start() {
        Log.d("PlayStateManagerImpl", "start() mStarted=" + this.f857a);
        if (!this.f857a) {
            this.f857a = true;
            this.f855a.registerReceiver(this.a, new IntentFilter(Extras.ACTION_PLAY_STATE));
        }
    }

    public final synchronized void stop() {
        Log.d("PlayStateManagerImpl", "stop() mStarted=" + this.f857a);
        if (this.f857a) {
            this.f855a.unregisterReceiver(this.a);
            this.f857a = false;
        }
    }

    public final boolean isRunning() {
        return this.f857a;
    }
}
