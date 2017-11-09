package com.gala.video.lib.share.ifimpl.ucenter.history.impl;

import android.os.Message;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.DeviceManager;
import com.gala.video.lib.share.common.configs.DeviceManager.OnStateChangedListener;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryDataBuilder.HistoryCloudRequest;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IHistoryCacheManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import java.util.List;

public class HistoryCacheManager extends Wrapper {
    private OnStateChangedListener mDeviceListener = new C17421();
    private HistoryDataBuilder mHistoryDataBuilder;

    class C17421 implements OnStateChangedListener {
        C17421() {
        }

        public void onStateChanged(int state) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(HistoryInfoHelper.TAG, "mDeviceListener.onStateChanged(" + state + ")");
            }
            if (DeviceManager.instance().isAuthSuccess()) {
                HistoryCacheManager.this.synchronizeHistoryListFromCloudDelay();
            } else {
                HistoryCacheManager.this.mHistoryDataBuilder.removeCallbacksAndMessages(null);
            }
        }
    }

    HistoryCacheManager() {
        DeviceManager.instance().addListener(this.mDeviceListener);
        if (this.mHistoryDataBuilder == null) {
            this.mHistoryDataBuilder = new HistoryDataBuilder();
        }
    }

    public List<HistoryInfo> getLatestLongVideoHistory(int count) {
        return this.mHistoryDataBuilder.getHistoryLongVideoMsg(count);
    }

    public List<HistoryInfo> getLatestVideoHistory(int count) {
        return this.mHistoryDataBuilder.getHistoryVideoMsg(count);
    }

    public void loadHistoryList(int pageIndex, int PageSize, int type, IHistoryResultCallBack callback) {
        int id = this.mHistoryDataBuilder.addHistoryResultCallback(callback);
        Message message = new Message();
        message.arg1 = id;
        message.what = 4;
        message.obj = new HistoryCloudRequest(pageIndex, PageSize, type);
        this.mHistoryDataBuilder.sendMessage(message);
    }

    public void synchronizeHistoryListFromCloud() {
        Message message = new Message();
        message.what = 6;
        this.mHistoryDataBuilder.sendMessage(message);
    }

    public void synchronizeHistoryListFromCloudDelay() {
        Message message = new Message();
        message.what = 6;
        this.mHistoryDataBuilder.sendMessageDelayed(message, 5000);
        Message message1 = Message.obtain();
        message1.what = 8;
        this.mHistoryDataBuilder.sendMessageDelayed(message1, 5000);
    }

    public void synchronizeHistoryListForNoLogin() {
        Message message1 = Message.obtain();
        message1.what = 8;
        this.mHistoryDataBuilder.sendMessage(message1);
    }

    public void mergeDeviceAndCloudHistory() {
        Message message = new Message();
        message.what = 1;
        this.mHistoryDataBuilder.sendMessage(message);
    }

    public void deleteHistory(String qpId, String tvQid) {
        this.mHistoryDataBuilder.removeMessages(7);
        Message cacheMsg = new Message();
        cacheMsg.what = 7;
        cacheMsg.obj = new String[]{qpId, tvQid};
        this.mHistoryDataBuilder.sendMessage(cacheMsg);
    }

    public void uploadHistory(HistoryInfo info) {
        Message message = new Message();
        message.what = 2;
        message.obj = info;
        this.mHistoryDataBuilder.sendMessage(message);
    }

    public HistoryInfo getAlbumHistory(String albumId) {
        return this.mHistoryDataBuilder.getHistoryByAlbumId(albumId);
    }

    public HistoryInfo getTvHistory(String tvId) {
        return this.mHistoryDataBuilder.getHistoryByTvId(tvId);
    }

    public void clear() {
        this.mHistoryDataBuilder.removeMessages(3);
        this.mHistoryDataBuilder.sendEmptyMessage(3);
    }

    public void clearLoginUserDb() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(HistoryInfoHelper.TAG, "clear login user db");
        }
        this.mHistoryDataBuilder.removeMessages(5);
        this.mHistoryDataBuilder.sendEmptyMessage(5);
    }
}
