package com.qiyi.tv.client.impl.p035a;

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

public final class C2016p implements PlayStateManager {
    private BroadcastReceiver f2120a = new C20151(this);
    private Context f2121a;
    private OnPlayStateChangedListener f2122a = null;
    private boolean f2123a;

    class C20151 extends BroadcastReceiver {
        private /* synthetic */ C2016p f2119a;

        C20151(C2016p c2016p) {
            this.f2119a = c2016p;
        }

        public final void onReceive(Context context, Intent intent) {
            Log.m1620d("PlayStateManagerImpl", "onReceive(" + intent + ") mStarted=" + this.f2119a.f2122a);
            if (Extras.ACTION_PLAY_STATE.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                int parsePlayState = ParamsHelper.parsePlayState(extras);
                Media parseMedia = ParamsHelper.parseMedia(extras);
                synchronized (this.f2119a.f2122a) {
                    if (this.f2119a.f2122a != null) {
                        this.f2119a.f2122a.onPlayStateChanged(parsePlayState, parseMedia);
                    }
                }
            }
        }
    }

    public C2016p(Context context) {
        this.f2121a = context;
    }

    public final synchronized void setOnPlayStateChangedListener(OnPlayStateChangedListener listener) {
        Log.m1620d("PlayStateManagerImpl", "setOnPlayStateChangedListener(" + listener + ")");
        this.f2122a = listener;
    }

    public final synchronized void start() {
        Log.m1620d("PlayStateManagerImpl", "start() mStarted=" + this.f2123a);
        if (!this.f2123a) {
            this.f2123a = true;
            this.f2121a.registerReceiver(this.f2120a, new IntentFilter(Extras.ACTION_PLAY_STATE));
        }
    }

    public final synchronized void stop() {
        Log.m1620d("PlayStateManagerImpl", "stop() mStarted=" + this.f2123a);
        if (this.f2123a) {
            this.f2121a.unregisterReceiver(this.f2120a);
            this.f2123a = false;
        }
    }

    public final boolean isRunning() {
        return this.f2123a;
    }
}
