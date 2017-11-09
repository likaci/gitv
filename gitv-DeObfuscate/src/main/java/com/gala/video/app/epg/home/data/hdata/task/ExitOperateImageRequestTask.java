package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.provider.ExitOperateImageProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import java.util.Iterator;
import java.util.List;

public class ExitOperateImageRequestTask extends BaseRequestTask {
    private static final String TAG = "home/ExitOperateImageRequestTask";
    private int mLimitBehavior = 0;

    class C06411 implements IVrsCallback<ApiResultChannelLabels> {
        C06411() {
        }

        public void onSuccess(ApiResultChannelLabels result) {
            if (result == null) {
                LogUtils.m1577w(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, onSuccess, ApiResultChannelLabels is null");
                ExitOperateImageRequestTask.this.clearLimitBehavior();
                ExitOperateImageProvider.getInstance().onEmptyExitOperateData();
                return;
            }
            ChannelLabels labels = result.getChannelLabels();
            if (labels == null) {
                LogUtils.m1577w(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, onSuccess, ChannelLabels is null");
                ExitOperateImageRequestTask.this.clearLimitBehavior();
                ExitOperateImageProvider.getInstance().onEmptyExitOperateData();
                return;
            }
            List<ChannelLabel> labelList = labels.getChannelLabelList();
            if (ListUtils.isEmpty((List) labelList)) {
                LogUtils.m1577w(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, onSuccess, ChannelLabel List is empty");
                ExitOperateImageRequestTask.this.clearLimitBehavior();
                ExitOperateImageProvider.getInstance().onEmptyExitOperateData();
                return;
            }
            LogUtils.m1577w(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, onSuccess, ChannelLabel size :" + ListUtils.getCount((List) labelList));
            if (labelList.size() > 15) {
                labelList = labelList.subList(0, 15);
            }
            Iterator<ChannelLabel> iterator = labelList.iterator();
            while (iterator.hasNext()) {
                ChannelLabel label = (ChannelLabel) iterator.next();
                if (ResourceType.LIVE == label.getType() && !label.checkLive()) {
                    iterator.remove();
                    LogUtils.m1568d(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, remove out of date live data, id : " + label.itemId);
                }
            }
            if (1 == ExitOperateImageRequestTask.this.mLimitBehavior) {
                ExitOperateImageProvider.getInstance().setData(labelList);
            } else {
                ExitOperateImageProvider.getInstance().setData(labelList);
                ExitOperateImageProvider.getInstance().download(labelList);
            }
            ExitOperateImageRequestTask.this.clearLimitBehavior();
        }

        public void onException(ApiException e) {
            LogUtils.m1578w(ExitOperateImageRequestTask.TAG, "fetchExitOperateImageData, onException, ApiException e ", e);
            ExitOperateImageRequestTask.this.clearLimitBehavior();
        }
    }

    public ExitOperateImageRequestTask(int behavior) {
        this.mLimitBehavior = behavior;
    }

    public String identifier() {
        return getClass().getName();
    }

    public void invoke() {
        fetchExitOperateImageData();
    }

    public void onOneTaskFinished() {
    }

    private void clearLimitBehavior() {
        if (1 == this.mLimitBehavior) {
            this.mLimitBehavior = 0;
        }
    }

    private void fetchExitOperateImageData() {
        String id = "";
        IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicResult != null) {
            LogUtils.m1577w(TAG, "fetchExitOperateImageData, exit operation image resource id : " + dynamicResult.getSpecifiedOperateImageResId(OperationImageType.EXIT));
            VrsHelper.channelLabelsSize.call(new C06411(), id, "0", "5.0", "60");
            return;
        }
        LogUtils.m1577w(TAG, "fetchExitOperateImageData, get dynamic data, dynamicResult is null");
    }
}
