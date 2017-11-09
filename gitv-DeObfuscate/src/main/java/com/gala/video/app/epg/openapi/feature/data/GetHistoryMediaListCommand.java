package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.MaxCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import java.util.List;

public class GetHistoryMediaListCommand extends MaxCommand<List<Media>> {
    private static final String TAG = "GetHistoryMediaListCommand";
    private Object lock = new Object();

    private class HistoryCallBack extends ResultListHolder<Media> implements IHistoryResultCallBack {
        public HistoryCallBack(int maxCount) {
            super(maxCount);
        }

        public void onSuccess(List<Album> list, int total) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetHistoryMediaListCommand.TAG, "UserListener.onSuccess(" + list + ")");
            }
            for (Album info : list) {
                Media media = OpenApiUtils.createSdkMedia(info);
                if (media != null) {
                    add(media);
                } else {
                    LogUtils.m1577w(GetHistoryMediaListCommand.TAG, "onSuccess() cannot translate to sdk media!!!");
                }
                if (isReachMax()) {
                    LogUtils.m1577w(GetHistoryMediaListCommand.TAG, "onSuccess() reach max size !!!" + this);
                    break;
                }
            }
            synchronized (GetHistoryMediaListCommand.this.lock) {
                GetHistoryMediaListCommand.this.lock.notify();
            }
        }
    }

    public GetHistoryMediaListCommand(Context context, int maxCount) {
        super(context, 10001, 20003, DataType.DATA_MEDIA_LIST, maxCount);
        setNeedNetwork(true);
    }

    public Bundle onProcess(Bundle params) {
        int pageNo = ServerParamsHelper.parsePageNo(params);
        int pageSize = ServerParamsHelper.parsePageSize(params);
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        String onlyLongVideo = ServerParamsHelper.parseOnlyLongVideo(params);
        if (maxCount > getMaxCount()) {
            maxCount = getMaxCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() maxCount=" + maxCount);
        }
        HistoryCallBack listener = new HistoryCallBack(maxCount);
        GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloud();
        GetInterfaceTools.getIHistoryCacheManager().loadHistoryList(pageNo, pageSize, "0".equals(onlyLongVideo) ? 0 : 1, listener);
        try {
            synchronized (this.lock) {
                this.lock.wait();
            }
        } catch (InterruptedException e) {
            LogUtils.m1577w(TAG, "Wait for history result timeout");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() pageNo=" + pageNo + ", pageSize=" + pageSize + ", maxCount=" + maxCount + ",onlyLongVideo =" + onlyLongVideo);
        }
        return listener.getResult();
    }
}
