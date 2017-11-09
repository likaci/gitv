package com.gala.video.app.epg.ui.multisubject.widget;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MultiSubjectErrorView {
    private String LOG_TAG = "EPG/multisubject/SubjectErrorView";
    private GlobalQRFeedbackPanel mNoResultPanel;
    private LinearLayout mProgressBar;
    private View mRootView;

    public MultiSubjectErrorView(View root) {
        this.mRootView = root;
    }

    private LinearLayout getProgressBar() {
        if (this.mProgressBar == null) {
            this.mProgressBar = (LinearLayout) ((ViewStub) this.mRootView.findViewById(C0508R.id.epg_multi_subject_status_loadding_id)).inflate();
            ((TextView) this.mProgressBar.findViewById(C0508R.id.share_progress_tv)).setTextColor(ResourceUtil.getColor(C0508R.color.search_nothing_text));
        }
        return this.mProgressBar;
    }

    public GlobalQRFeedbackPanel getNoResultPanel() {
        if (this.mNoResultPanel == null) {
            this.mNoResultPanel = (GlobalQRFeedbackPanel) ((ViewStub) this.mRootView.findViewById(C0508R.id.epg_multi_subject_status_layout_id)).inflate();
        }
        return this.mNoResultPanel;
    }

    public void hideNoResultPanel() {
        getNoResultPanel().setVisibility(8);
    }

    public void showNoResultPanel() {
        LogUtils.m1571e(this.LOG_TAG, "showNoResultPanel");
        getNoResultPanel().setVisibility(0);
        pannelResuestFocus();
    }

    public void hideProgressBar() {
        getProgressBar().setVisibility(8);
    }

    public void showProgressBar() {
        getProgressBar().setVisibility(0);
    }

    private void pannelResuestFocus() {
        final Button pannelButton = getNoResultPanel().getButton();
        if (pannelButton != null) {
            pannelButton.post(new Runnable() {
                public void run() {
                    pannelButton.requestFocus();
                }
            });
        }
    }
}
