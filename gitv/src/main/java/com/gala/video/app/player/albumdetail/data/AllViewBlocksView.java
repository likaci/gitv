package com.gala.video.app.player.albumdetail.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.lib.share.common.activity.QBaseActivity;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.pingback.PingbackUtils;

public class AllViewBlocksView extends BlocksView {
    private static final String TAG = "AllViewBlocksView";
    private OnBackClickedListener mBackClickedListener;
    private boolean mIsPanelShown = false;
    private PingbackPage mSaveOuterPingbackPage;

    public interface OnBackClickedListener {
        void onBackClicked();
    }

    public AllViewBlocksView(Context context) {
        super(context);
    }

    public AllViewBlocksView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllViewBlocksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!this.mIsPanelShown || event.getAction() != 0 || event.getRepeatCount() != 0 || 4 != event.getKeyCode()) {
            return false;
        }
        notifyBackClicked();
        LogRecordUtils.logd(TAG, "handleKeyEvent, handled.");
        return true;
    }

    private void notifyBackClicked() {
        LogRecordUtils.logd(TAG, ">> notifyBackClicked");
        if (this.mBackClickedListener != null) {
            this.mBackClickedListener.onBackClicked();
        }
    }

    public void show() {
        LogRecordUtils.logd(TAG, ">> show(), mIsPanelShown=" + this.mIsPanelShown);
        if (!this.mIsPanelShown) {
            if (getContext() instanceof QBaseActivity) {
                this.mSaveOuterPingbackPage = PingbackUtils.getCurrentPingbackPage();
                PingbackUtils.forceSetSpecialPingbackPage(getContext(), PingbackPage.DetailAll);
            }
            setVisibility(0);
            this.mIsPanelShown = true;
        }
    }

    public void hide() {
        LogRecordUtils.logd(TAG, ">> hide(), mIsPanelShown=" + this.mIsPanelShown);
        if (this.mIsPanelShown) {
            if (getContext() instanceof QBaseActivity) {
                PingbackUtils.forceSetSpecialPingbackPage(getContext(), this.mSaveOuterPingbackPage);
            }
            CardFocusHelper.forceVisible(getContext(), false);
            setVisibility(8);
            this.mIsPanelShown = false;
        }
    }

    public boolean isShown() {
        return this.mIsPanelShown;
    }

    public void setBackClickedListener(OnBackClickedListener backClickedListener) {
        this.mBackClickedListener = backClickedListener;
    }
}
