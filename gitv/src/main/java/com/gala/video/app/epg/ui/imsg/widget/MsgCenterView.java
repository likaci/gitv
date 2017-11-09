package com.gala.video.app.epg.ui.imsg.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MsgCenterView extends CloudView {
    private CuteImageView mIconView;
    private boolean mIsReaded;
    private int mReadColor;
    private Drawable mReadedFocusIcon;
    private Drawable mReadedUnfocusIcon;
    private CuteTextView mTimeView;
    private CuteTextView mTitleView;
    private int mUnreadedColor;
    private Drawable mUnreadedFocusIcon;
    private Drawable mUnreadedUnfocusIcon;

    public static class MessageCenterModel {
        public boolean mIsReaded;
        public String mTime;
        public String mTitle;
    }

    public MsgCenterView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setStyle("messagecenter/messagecenterview.json");
        setBackgroundDrawable(ResourceUtil.getDrawable(R.drawable.share_item_rect_btn_selector));
        this.mUnreadedColor = ResourceUtil.getColor(R.color.message_center_unreaded_color);
        this.mReadColor = ResourceUtil.getColor(R.color.message_center_readed_color);
        this.mReadedFocusIcon = ResourceUtil.getDrawable(R.drawable.epg_message_center_readed_focus);
        this.mReadedUnfocusIcon = ResourceUtil.getDrawable(R.drawable.epg_message_center_readed_unfocus);
        this.mUnreadedFocusIcon = ResourceUtil.getDrawable(R.drawable.epg_message_center_unreaded_focus);
        this.mUnreadedUnfocusIcon = ResourceUtil.getDrawable(R.drawable.epg_message_center_unreaded_unfocus);
    }

    public void setData(MessageCenterModel model) {
        if (model != null) {
            getIconView();
            getTitleView();
            getTimeView();
            this.mTitleView.setText(model.mTitle);
            this.mTimeView.setText(model.mTime);
            updateUIState(model.mIsReaded);
        }
    }

    public void updateUIState(boolean isReaded) {
        this.mIsReaded = isReaded;
        getIconView();
        getTitleView();
        getTimeView();
        setIcon(hasFocus());
        setNormalColor();
    }

    private void setNormalColor() {
        if (this.mIsReaded) {
            this.mTitleView.setNormalColor(this.mReadColor);
            this.mTimeView.setNormalColor(this.mReadColor);
            return;
        }
        this.mTitleView.setNormalColor(this.mUnreadedColor);
        this.mTimeView.setNormalColor(this.mUnreadedColor);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setIcon(gainFocus);
    }

    protected void setIcon(boolean gainFocus) {
        if (this.mIsReaded) {
            this.mIconView.setDrawable(gainFocus ? this.mReadedFocusIcon : this.mReadedUnfocusIcon);
        } else {
            this.mIconView.setDrawable(gainFocus ? this.mUnreadedFocusIcon : this.mUnreadedUnfocusIcon);
        }
    }

    private CuteImageView getIconView() {
        if (this.mIconView == null) {
            this.mIconView = getImageView("ID_IMAGE");
        }
        return this.mIconView;
    }

    private CuteTextView getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getTextView("ID_TITLE");
        }
        return this.mTitleView;
    }

    private CuteTextView getTimeView() {
        if (this.mTimeView == null) {
            this.mTimeView = getTextView("ID_TIME");
        }
        return this.mTimeView;
    }
}
