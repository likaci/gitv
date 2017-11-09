package com.qiyi.tv.client.impl.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.feature.history.HistoryManager;
import com.qiyi.tv.client.feature.history.OnHistoryChangedListener;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.List;

public final class n implements HistoryManager {
    private BroadcastReceiver a = new BroadcastReceiver(this) {
        private /* synthetic */ n a;

        {
            this.a = r1;
        }

        public final void onReceive(Context context, Intent intent) {
            Log.d("HistoryManagerImpl", "onReceive(" + intent + ") mStarted=" + this.a.f853a + ", action = " + intent.getAction());
            if (Extras.ACTION_HISTORY_CHANGED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                int parseHistoryChangedAction = ParamsHelper.parseHistoryChangedAction(extras);
                Media parseMedia = ParamsHelper.parseMedia(extras);
                Log.d("HistoryManagerImpl", "action = " + parseHistoryChangedAction + ", media = " + parseMedia + ", historyChangedListener = " + this.a.f853a);
                synchronized (this.a.f853a) {
                    if (this.a.f853a != null) {
                        this.a.f853a.onHistoryChanged(parseHistoryChangedAction, parseMedia);
                    }
                }
            }
        }
    };
    private Context f852a = null;
    private OnHistoryChangedListener f853a = null;
    private boolean f854a;

    public n(Context context) {
        this.f852a = context;
    }

    public final Result<List<Media>> getHistoryList(int maxCount) {
        return getHistoryList(maxCount, true);
    }

    public final Result<List<Media>> getHistoryList(int maxCount, boolean onlyLongVideo) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMaxCount(bundle, maxCount);
        ParamsHelper.setOnlyLongVide(bundle, onlyLongVideo);
        try {
            bundle = a.a(bundle, b.b(this.f852a, 10001, DataType.DATA_MEDIA_LIST));
            return new Result(ParamsHelper.parseResultCode(bundle), (List) ParamsHelper.parseResultData(bundle));
        } catch (Exception e) {
            return new Result(Utils.parseErrorCode(e), null);
        }
    }

    public final synchronized void setOnHistoryChangedListener(OnHistoryChangedListener historyChangedListener) {
        this.f853a = historyChangedListener;
    }

    public final synchronized void start() {
        Log.d("HistoryManagerImpl", "start() mStarted=" + this.f854a);
        if (!this.f854a) {
            this.f854a = true;
            this.f852a.registerReceiver(this.a, new IntentFilter(Extras.ACTION_HISTORY_CHANGED_ACTION));
        }
    }

    public final synchronized void stop() {
        Log.d("HistoryManagerImpl", "stop() mStarted=" + this.f854a);
        if (this.f854a) {
            this.f852a.unregisterReceiver(this.a);
            this.f854a = false;
        }
    }

    public final boolean isRunning() {
        return this.f854a;
    }

    public final int clearHistory() {
        return ParamsHelper.parseResultCode(a.a(null, b.a(this.f852a, 10001, OperationType.OP_CLEAR_HISTORY)));
    }

    public final int clearAnonymousHistory() {
        return ParamsHelper.parseResultCode(a.a(null, b.a(this.f852a, 10001, OperationType.OP_CLEAR_ANONYMOUS_HISTORY)));
    }

    public final int deleteHistory(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.f852a, 10001, OperationType.OP_DELETE_HISTORY)));
    }

    public final int deleteAnonymousHistory(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.f852a, 10001, OperationType.OP_DELETE_ANONYMOUS_HISTORY)));
    }
}
