package com.gala.video.app.epg.ui.subjectreview.data;

import android.text.TextUtils;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import java.util.ArrayList;
import java.util.List;

public class TasksRepository implements IDataSource {
    private static final String LOG_TAG = "TasksRepository";

    public void getDataList(String id, final SubjectReviewCallback callback) {
        VrsHelper.channelLabelsLive.call(new IVrsCallback<ApiResultChannelLabels>() {
            public void onSuccess(ApiResultChannelLabels apiResultChannelLabels) {
                List<ChannelLabel> originalChannelLabelList = null;
                if (apiResultChannelLabels != null) {
                    ChannelLabels channelLabels = apiResultChannelLabels.getChannelLabels();
                    if (channelLabels != null) {
                        originalChannelLabelList = channelLabels.getChannelLabelList();
                    }
                }
                callback.onSuccess(TasksRepository.this.fiterSubjectData(originalChannelLabelList));
            }

            public void onException(ApiException e) {
                callback.onFail(e);
            }
        }, id, "0", "2.0");
    }

    private List<ChannelLabel> fiterSubjectData(List<ChannelLabel> originalData) {
        LogUtils.i(LOG_TAG, "fiterSubjectData --- ListUtils.getCount(originalData)", Integer.valueOf(ListUtils.getCount((List) originalData)));
        if (ListUtils.isEmpty((List) originalData)) {
            return null;
        }
        List newChannelLabels = new ArrayList();
        int size = ListUtils.getCount((List) originalData);
        boolean begin = false;
        for (int i = 0; i < size; i++) {
            ChannelLabel channelLabel = (ChannelLabel) originalData.get(i);
            if (channelLabel != null) {
                if (begin || !TextUtils.equals(channelLabel.itemKvs.tvfunction, ItemDataType.PLST_GROUP.getValue())) {
                    ResourceType type = channelLabel.getType();
                    if (begin && (type == ResourceType.COLLECTION || type == ResourceType.RESOURCE_GROUP)) {
                        newChannelLabels.add(channelLabel);
                    }
                } else {
                    begin = true;
                }
            }
        }
        LogUtils.i(LOG_TAG, "fiterSubjectData --- ListUtils.getCount(newChannelLabels)", Integer.valueOf(ListUtils.getCount(newChannelLabels)));
        return newChannelLabels;
    }
}
