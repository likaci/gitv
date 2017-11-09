package com.qiyi.tv.client.impl.p035a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.qiyi.tv.client.feature.p034b.C1991a;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class C2010l implements C1991a {

    class C20091 extends BroadcastReceiver {
        C20091(C2010l c2010l) {
        }

        public final void onReceive(Context context, Intent intent) {
            Log.m1620d("FavoriteManagerImpl", "onReceive(" + intent + ") mStarted=false, action = " + intent.getAction());
            if (Extras.ACTION_FAVORITE_CHANGED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                int parseFavoriteChangedAction = ParamsHelper.parseFavoriteChangedAction(extras);
                Log.m1620d("FavoriteManagerImpl", "action = " + parseFavoriteChangedAction + ", media = " + ParamsHelper.parseMedia(extras) + ", favoriteChangedListener = " + null);
                synchronized (null) {
                    try {
                    } catch (Throwable th) {
                    }
                }
            }
        }
    }

    public C2010l() {
        C20091 c20091 = new C20091(this);
    }

    public final synchronized void mo4357a() {
        Log.m1620d("FavoriteManagerImpl", "stop() mStarted=false");
    }
}
