package com.qiyi.tv.client.impl.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.qiyi.tv.client.feature.b.a;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class l implements a {
    public l() {
        AnonymousClass1 anonymousClass1 = new BroadcastReceiver(this) {
            public final void onReceive(Context context, Intent intent) {
                Log.d("FavoriteManagerImpl", "onReceive(" + intent + ") mStarted=false, action = " + intent.getAction());
                if (Extras.ACTION_FAVORITE_CHANGED_ACTION.equals(intent.getAction())) {
                    Bundle extras = intent.getExtras();
                    int parseFavoriteChangedAction = ParamsHelper.parseFavoriteChangedAction(extras);
                    Log.d("FavoriteManagerImpl", "action = " + parseFavoriteChangedAction + ", media = " + ParamsHelper.parseMedia(extras) + ", favoriteChangedListener = " + null);
                    synchronized (null) {
                        try {
                        } catch (Throwable th) {
                        }
                    }
                }
            }
        };
    }

    public final synchronized void a() {
        Log.d("FavoriteManagerImpl", "stop() mStarted=false");
    }
}
