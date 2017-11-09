package com.gala.video.app.player.albumdetail.ui.overlay;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.ResourceUtil;

public class DetailStarsErrorView {
    private GlobalQRFeedbackPanel mNoResultPanel;
    private LinearLayout mProgressBar;
    private View mRootView;

    public DetailStarsErrorView(View root) {
        this.mRootView = root;
    }

    private LinearLayout getProgressBar() {
        if (this.mProgressBar == null) {
            this.mProgressBar = (LinearLayout) ((ViewStub) this.mRootView.findViewById(C1291R.id.player_stars_status_loadding)).inflate();
            ((TextView) this.mProgressBar.findViewById(C1291R.id.share_progress_tv)).setTextColor(ResourceUtil.getColor(C1291R.color.search_nothing_text));
        }
        return this.mProgressBar;
    }

    public GlobalQRFeedbackPanel getNoResultPanel() {
        if (this.mNoResultPanel == null) {
            this.mNoResultPanel = (GlobalQRFeedbackPanel) ((ViewStub) this.mRootView.findViewById(C1291R.id.player_stars_status_layout)).inflate();
        }
        return this.mNoResultPanel;
    }

    private void hideNoResultPanel() {
        if (this.mNoResultPanel != null && getNoResultPanel().getVisibility() != 8) {
            getNoResultPanel().setVisibility(8);
        }
    }

    private void hideProgressBar() {
        if (this.mProgressBar != null && getProgressBar().getVisibility() != 8) {
            getProgressBar().setVisibility(8);
        }
    }

    public void showProgressBar() {
        getProgressBar().setVisibility(0);
        hideNoResultPanel();
    }

    public void showHasResultPanel() {
        hideNoResultPanel();
        hideProgressBar();
    }

    public void showNoResultPanel() {
        hideProgressBar();
        getNoResultPanel().setVisibility(0);
        pannelResuestFocus();
    }

    private void pannelResuestFocus() {
        final Button pannelButton = getNoResultPanel().getButton();
        if (pannelButton != null && pannelButton.getVisibility() == 0) {
            pannelButton.post(new Runnable() {
                public void run() {
                    pannelButton.requestFocus();
                }
            });
        }
    }
}
