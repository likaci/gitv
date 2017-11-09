package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.screensaver.ScreenSaverImageProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import java.util.Iterator;
import java.util.List;

public class ScreenSaverOperateImageRequestTask extends BaseRequestTask {
    private static final String TAG = "home/ScreenSaverOperateImageRequestTask";
    private int mLimitBehavior = 0;

    public ScreenSaverOperateImageRequestTask(int behavior) {
        this.mLimitBehavior = behavior;
    }

    public String identifier() {
        return getClass().getName();
    }

    public void invoke() {
        fetchScreenSaverImageData();
    }

    public void onOneTaskFinished() {
    }

    private void clearLimitBehavior() {
        if (1 == this.mLimitBehavior) {
            this.mLimitBehavior = 0;
        }
    }

    private void fetchScreenSaverImageData() {
        String id = "";
        IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicResult != null) {
            LogUtils.w(TAG, "fetchScreenSaverImageData, screensaver operation image resource id : " + dynamicResult.getSpecifiedOperateImageResId(OperationImageType.SCREENSAVER));
            VrsHelper.channelLabelsSize.call(new IVrsCallback<ApiResultChannelLabels>() {
                public void onSuccess(ApiResultChannelLabels result) {
                    if (result == null) {
                        LogUtils.w(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, onSuccess, ApiResultChannelLabels is null");
                        ScreenSaverOperateImageRequestTask.this.clearLimitBehavior();
                        ScreenSaverImageProvider.getInstance().onEmptyScreenSaverData();
                        return;
                    }
                    ChannelLabels labels = result.getChannelLabels();
                    if (labels == null) {
                        LogUtils.w(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, onSuccess, ChannelLabels is null");
                        ScreenSaverOperateImageRequestTask.this.clearLimitBehavior();
                        ScreenSaverImageProvider.getInstance().onEmptyScreenSaverData();
                        return;
                    }
                    List<ChannelLabel> labelList = labels.getChannelLabelList();
                    if (ListUtils.isEmpty((List) labelList)) {
                        LogUtils.w(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, onSuccess, ChannelLabel List is empty");
                        ScreenSaverOperateImageRequestTask.this.clearLimitBehavior();
                        ScreenSaverImageProvider.getInstance().onEmptyScreenSaverData();
                        return;
                    }
                    LogUtils.d(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, onSuccess, ChannelLabel size : " + ListUtils.getCount((List) labelList));
                    if (labelList.size() > 15) {
                        labelList = labelList.subList(0, 15);
                    }
                    Iterator<ChannelLabel> iterator = labelList.iterator();
                    while (iterator.hasNext()) {
                        ChannelLabel label = (ChannelLabel) iterator.next();
                        if (ResourceType.LIVE == label.getType() && !label.checkLive()) {
                            iterator.remove();
                            LogUtils.d(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, remove out of date live data, id : " + label.itemId);
                        }
                    }
                    if (1 == ScreenSaverOperateImageRequestTask.this.mLimitBehavior) {
                        ScreenSaverImageProvider.getInstance().setData(labelList);
                    } else {
                        ScreenSaverImageProvider.getInstance().setData(labelList);
                        ScreenSaverImageProvider.getInstance().download(labelList);
                    }
                    ScreenSaverOperateImageRequestTask.this.clearLimitBehavior();
                }

                public void onException(ApiException e) {
                    LogUtils.w(ScreenSaverOperateImageRequestTask.TAG, "fetchScreenSaverImageData, onException, ApiException e ", e);
                    ScreenSaverOperateImageRequestTask.this.clearLimitBehavior();
                }
            }, id, "0", "5.0", "60");
            return;
        }
        LogUtils.w(TAG, "fetchScreenSaverImageData, get dynamic data, dynamicResult is null");
    }
}
