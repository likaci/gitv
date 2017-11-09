package com.gala.video.app.player.data.task;

import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoItemFactory;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import java.util.ArrayList;
import java.util.List;

public class FetchExitAlbumsTask {
    private static final String TAG = "ExitPlayerPageDialog/Data/FetchExitAlbumsTask";
    private final String IS_FREE = "0";
    private String mDetailExitDialogResId;
    private ITaskResultListener mListener;

    class C14131 implements IVrsCallback<ApiResultChannelLabels> {
        C14131() {
        }

        public void onException(ApiException e) {
            LogUtils.m1568d(FetchExitAlbumsTask.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
            FetchExitAlbumsTask.this.mListener.onFailed(e);
        }

        public void onSuccess(ApiResultChannelLabels result) {
            List<ChannelLabel> channelList = result.data.items;
            List list = new ArrayList();
            IVideoItemFactory videoItemFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory();
            for (ChannelLabel label : channelList) {
                if (label.getType() == ResourceType.ALBUM || label.getType() == ResourceType.VIDEO) {
                    String extraImageUrl = label.itemKvs.extraImage;
                    IVideo video = videoItemFactory.createVideoItem(SourceType.COMMON, label.getVideo(), new MyPlayerProfile());
                    video.setExtraImageUrl(extraImageUrl);
                    list.add(video);
                } else {
                    LogUtils.m1568d(FetchExitAlbumsTask.TAG, "ResourceType.else =" + label.getType());
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(FetchExitAlbumsTask.TAG, ">>onSuccess=" + ListUtils.getCount(list));
            }
            FetchExitAlbumsTask.this.mListener.onSuccess(list);
        }
    }

    public interface ITaskResultListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<IVideo> list);
    }

    public FetchExitAlbumsTask() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        this.mDetailExitDialogResId = model != null ? model.getDetailExitDialogResId() : "";
    }

    public void excute() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> onRun, mDetailExitDialogResId=" + this.mDetailExitDialogResId);
        }
        VrsHelper.channelLabels.call(new C14131(), this.mDetailExitDialogResId, "0");
    }

    public void setTaskResultListener(ITaskResultListener listener) {
        this.mListener = listener;
    }
}
