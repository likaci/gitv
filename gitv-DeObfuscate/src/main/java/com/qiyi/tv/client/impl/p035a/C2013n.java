package com.qiyi.tv.client.impl.p035a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.history.HistoryManager;
import com.qiyi.tv.client.feature.history.OnHistoryChangedListener;
import com.qiyi.tv.client.feature.p033a.C1989a;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.List;

public final class C2013n implements HistoryManager {
    private BroadcastReceiver f2114a = new C20121(this);
    private Context f2115a = null;
    private OnHistoryChangedListener f2116a = null;
    private boolean f2117a;

    class C20121 extends BroadcastReceiver {
        private /* synthetic */ C2013n f2113a;

        C20121(C2013n c2013n) {
            this.f2113a = c2013n;
        }

        public final void onReceive(Context context, Intent intent) {
            Log.m1620d("HistoryManagerImpl", "onReceive(" + intent + ") mStarted=" + this.f2113a.f2116a + ", action = " + intent.getAction());
            if (Extras.ACTION_HISTORY_CHANGED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                int parseHistoryChangedAction = ParamsHelper.parseHistoryChangedAction(extras);
                Media parseMedia = ParamsHelper.parseMedia(extras);
                Log.m1620d("HistoryManagerImpl", "action = " + parseHistoryChangedAction + ", media = " + parseMedia + ", historyChangedListener = " + this.f2113a.f2116a);
                synchronized (this.f2113a.f2116a) {
                    if (this.f2113a.f2116a != null) {
                        this.f2113a.f2116a.onHistoryChanged(parseHistoryChangedAction, parseMedia);
                    }
                }
            }
        }
    }

    public C2013n(Context context) {
        this.f2115a = context;
    }

    public final Result<List<Media>> getHistoryList(int maxCount) {
        return getHistoryList(maxCount, true);
    }

    public final Result<List<Media>> getHistoryList(int maxCount, boolean onlyLongVideo) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMaxCount(bundle, maxCount);
        ParamsHelper.setOnlyLongVide(bundle, onlyLongVideo);
        try {
            bundle = C1989a.m1618a(bundle, C1994b.m1630b(this.f2115a, 10001, DataType.DATA_MEDIA_LIST));
            return new Result(ParamsHelper.parseResultCode(bundle), (List) ParamsHelper.parseResultData(bundle));
        } catch (Exception e) {
            return new Result(Utils.parseErrorCode(e), null);
        }
    }

    public final synchronized void setOnHistoryChangedListener(OnHistoryChangedListener historyChangedListener) {
        this.f2116a = historyChangedListener;
    }

    public final synchronized void start() {
        Log.m1620d("HistoryManagerImpl", "start() mStarted=" + this.f2117a);
        if (!this.f2117a) {
            this.f2117a = true;
            this.f2115a.registerReceiver(this.f2114a, new IntentFilter(Extras.ACTION_HISTORY_CHANGED_ACTION));
        }
    }

    public final synchronized void stop() {
        Log.m1620d("HistoryManagerImpl", "stop() mStarted=" + this.f2117a);
        if (this.f2117a) {
            this.f2115a.unregisterReceiver(this.f2114a);
            this.f2117a = false;
        }
    }

    public final boolean isRunning() {
        return this.f2117a;
    }

    public final int clearHistory() {
        return ParamsHelper.parseResultCode(C1989a.m1618a(null, C1994b.m1628a(this.f2115a, 10001, OperationType.OP_CLEAR_HISTORY)));
    }

    public final int clearAnonymousHistory() {
        return ParamsHelper.parseResultCode(C1989a.m1618a(null, C1994b.m1628a(this.f2115a, 10001, OperationType.OP_CLEAR_ANONYMOUS_HISTORY)));
    }

    public final int deleteHistory(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1628a(this.f2115a, 10001, OperationType.OP_DELETE_HISTORY)));
    }

    public final int deleteAnonymousHistory(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1628a(this.f2115a, 10001, OperationType.OP_DELETE_ANONYMOUS_HISTORY)));
    }
}
