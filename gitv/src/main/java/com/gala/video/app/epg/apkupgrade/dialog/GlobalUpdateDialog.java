package com.gala.video.app.epg.apkupgrade.dialog;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.common.widget.GlobalDialog;

public class GlobalUpdateDialog extends GlobalDialog {
    private BackPressedListener mBackPressedListener = null;
    protected TextView mContentTextDownloadingState;
    protected TextView mContentTextMessage;
    protected TextView mContentTextTitle;
    private boolean mForbidBackPress = false;
    private boolean mNoRemind = true;

    public interface BackPressedListener {
        void onBackKeyPressed();
    }

    public GlobalUpdateDialog(Context context) {
        super(context);
    }

    protected void initLayout() {
        LayoutParams params = new LayoutParams(this.mContentLayout.getLayoutParams());
        params.setMargins(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_0dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_0dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_0dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_0dp));
        this.mContentLayout.setLayoutParams(params);
        this.mContentTextView = (TextView) this.mContentLayout.findViewById(R.id.epg_gdutv_dialog_text);
        this.mContentTextView.setVisibility(0);
        this.mContentTextTitle = (TextView) this.mContentLayout.findViewById(R.id.epg_gdutv_dialog_text_title);
        this.mContentTextTitle.setVisibility(8);
        this.mContentTextMessage = (TextView) this.mContentLayout.findViewById(R.id.epg_gdutv_dialog_text_message);
        this.mContentTextMessage.setVisibility(8);
        this.mContentTextDownloadingState = (TextView) this.mContentLayout.findViewById(R.id.epg_gdutv_dialog_text_downloading_state);
        this.mContentTextDownloadingState.setVisibility(8);
    }

    protected void initParams() {
        super.initParams();
        this.mContentResId = R.layout.epg_global_dialog_update_text_view;
    }

    public void setContentTextTitle(CharSequence contentText) {
        if (this.mContentTextTitle == null) {
            show();
        }
        if (this.mContentTextTitle != null) {
            this.mContentTextTitle.setText(contentText);
            this.mContentTextTitle.setVisibility(0);
        }
    }

    public void setContentTextTitleGravity(int gravity) {
        if (this.mContentTextTitle != null) {
            this.mContentTextTitle.setGravity(gravity);
        }
    }

    public void setContentTextTitleVisible(int visibility) {
        if (this.mContentTextTitle != null) {
            this.mContentTextTitle.setVisibility(visibility);
        }
    }

    public void setContentTextMessage(CharSequence contentText) {
        if (this.mContentTextMessage == null) {
            show();
        }
        if (this.mContentTextMessage != null) {
            this.mContentTextMessage.setText(contentText);
            this.mContentTextMessage.setVisibility(0);
        }
    }

    public void setContentTextMessageGravity(int gravity) {
        if (this.mContentTextMessage != null) {
            this.mContentTextMessage.setGravity(gravity);
        }
    }

    public void setContentTextMessageVisible(int visibility) {
        if (this.mContentTextMessage != null) {
            this.mContentTextMessage.setVisibility(visibility);
        }
    }

    public void setContentTextDownloadingState(CharSequence contentText) {
        if (this.mContentTextDownloadingState == null) {
            show();
        }
        if (this.mContentTextDownloadingState != null) {
            this.mContentTextDownloadingState.setText(contentText);
            this.mContentTextDownloadingState.setVisibility(0);
        }
    }

    public void setContentTextDownloadingStateGravity(int gravity) {
        if (this.mContentTextDownloadingState != null) {
            this.mContentTextDownloadingState.setGravity(gravity);
        }
    }

    public void setContentTextDownloadingStateVisible(int visibility) {
        if (this.mContentTextDownloadingState != null) {
            this.mContentTextDownloadingState.setVisibility(visibility);
        }
    }

    public void setBackPressedListener(BackPressedListener listener) {
        this.mBackPressedListener = listener;
    }

    public void onBackPressed() {
        if (!this.mForbidBackPress) {
            super.onBackPressed();
            if (this.mBackPressedListener != null) {
                this.mBackPressedListener.onBackKeyPressed();
            }
        }
    }

    protected void destoryProc() {
        this.mContentTextTitle = null;
        this.mContentTextMessage = null;
        this.mContentTextDownloadingState = null;
    }

    public void setBackPressForbid(boolean forbid) {
        this.mForbidBackPress = forbid;
    }

    public void setNoRemindFlag(boolean noRemind) {
        this.mNoRemind = noRemind;
    }

    public boolean getNoRemindFlag() {
        return this.mNoRemind;
    }
}
