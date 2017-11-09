package com.gala.video.app.epg.ui.subjectreview.data;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import java.util.List;

public interface SubjectReviewCallback {
    void onFail(ApiException apiException);

    void onSuccess(List<ChannelLabel> list);
}
