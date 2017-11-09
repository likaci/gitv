package com.gala.video.app.epg.ui.subjectreview;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.subjectreview.data.SubjectReviewCallback;
import com.gala.video.app.epg.ui.subjectreview.data.TasksRepository;
import java.lang.ref.WeakReference;
import java.util.List;

public class SubjectReviewPresenter implements Presenter {
    private static final String LOG_TAG = "SubjectReviewPresenter";
    private TasksRepository mTasksRepository;
    private WeakReference<View> mView;

    class C11201 implements SubjectReviewCallback {
        C11201() {
        }

        public void onSuccess(List<ChannelLabel> list) {
            View outer = (View) SubjectReviewPresenter.this.mView.get();
            if (outer != null) {
                outer.showData(list);
            }
        }

        public void onFail(ApiException e) {
            View outer = (View) SubjectReviewPresenter.this.mView.get();
            if (outer != null) {
                outer.showExceptionView(e);
                QAPingback.error(SubjectReviewPresenter.LOG_TAG, "专题回顾", "", e);
            }
        }
    }

    public SubjectReviewPresenter(View view, TasksRepository tasksRepository) {
        this.mView = new WeakReference(view);
        this.mTasksRepository = tasksRepository;
    }

    public void start(String id) {
        this.mTasksRepository.getDataList(id, new C11201());
    }
}
