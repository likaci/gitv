package com.gala.video.app.epg.ui.subjectreview;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import java.util.List;

public class SubjectReviewContract {

    interface View {
        void showData(List<ChannelLabel> list);

        void showExceptionView(ApiException apiException);
    }

    interface Presenter {
        void start(String str);
    }
}
