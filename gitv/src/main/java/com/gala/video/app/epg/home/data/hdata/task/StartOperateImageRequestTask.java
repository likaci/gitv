package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.provider.StartOperateImageProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import java.util.Iterator;
import java.util.List;

public class StartOperateImageRequestTask extends BaseRequestTask {
    private static final String TAG = "home/StartOperateImageRequestTask";
    private int mLimitBehavior = 0;

    public StartOperateImageRequestTask(int behavior) {
        this.mLimitBehavior = behavior;
    }

    public String identifier() {
        return getClass().getName();
    }

    public void invoke() {
        fetchStartImageData();
    }

    public void onOneTaskFinished() {
    }

    private void clearLimitBehavior() {
        if (1 == this.mLimitBehavior) {
            this.mLimitBehavior = 0;
        }
    }

    private void fetchStartImageData() {
        String id = "";
        IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicResult != null) {
            LogUtils.w(TAG, "fetchStartImageData, start operation image resource id : " + dynamicResult.getSpecifiedOperateImageResId(OperationImageType.START));
            VrsHelper.channelLabelsSize.call(new IVrsCallback<ApiResultChannelLabels>() {
                public void onSuccess(ApiResultChannelLabels result) {
                    if (result == null) {
                        LogUtils.w(StartOperateImageRequestTask.TAG, "fetchStartImageData, onSuccess, ApiResultChannelLabels is null");
                        StartOperateImageRequestTask.this.clearLimitBehavior();
                        StartOperateImageProvider.getInstance().onEmptyStartOperateData();
                        return;
                    }
                    ChannelLabels labels = result.getChannelLabels();
                    if (labels == null) {
                        LogUtils.w(StartOperateImageRequestTask.TAG, "fetchStartImageData, onSuccess, ChannelLabels is null");
                        StartOperateImageRequestTask.this.clearLimitBehavior();
                        StartOperateImageProvider.getInstance().onEmptyStartOperateData();
                        return;
                    }
                    List<ChannelLabel> labelList = labels.getChannelLabelList();
                    if (ListUtils.isEmpty((List) labelList)) {
                        LogUtils.w(StartOperateImageRequestTask.TAG, "fetchStartImageData, onSuccess, ChannelLabel List is empty");
                        StartOperateImageRequestTask.this.clearLimitBehavior();
                        StartOperateImageProvider.getInstance().onEmptyStartOperateData();
                        return;
                    }
                    LogUtils.w(StartOperateImageRequestTask.TAG, "fetchStartImageData, onSuccess, ChannelLabel size:" + ListUtils.getCount((List) labelList));
                    if (labelList.size() > 15) {
                        labelList = labelList.subList(0, 15);
                    }
                    Iterator<ChannelLabel> iterator = labelList.iterator();
                    while (iterator.hasNext()) {
                        ChannelLabel label = (ChannelLabel) iterator.next();
                        if (ResourceType.LIVE == label.getType() && !label.checkLive()) {
                            iterator.remove();
                            LogUtils.d(StartOperateImageRequestTask.TAG, "fetchStartImageData, remove out of date live data, id : " + label.itemId);
                        }
                    }
                    if (1 == StartOperateImageRequestTask.this.mLimitBehavior) {
                        StartOperateImageProvider.getInstance().setData(labelList);
                    } else {
                        StartOperateImageProvider.getInstance().setData(labelList);
                        StartOperateImageProvider.getInstance().download(labelList);
                    }
                    StartOperateImageRequestTask.this.clearLimitBehavior();
                }

                public void onException(ApiException e) {
                    StartOperateImageRequestTask.this.clearLimitBehavior();
                }
            }, id, "0", "5.0", "60");
            return;
        }
        LogUtils.w(TAG, "fetchStartImageData, get dynamic data, dynamicResult is null");
    }
}
