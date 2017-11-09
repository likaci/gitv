package com.gala.video.app.epg.ui.subjectreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SubjectView extends CloudView {
    private CuteImageView mCoreImageView;
    private CuteImageView mTitleBG;
    private CuteTextView mTitleView;

    public SubjectView(Context context) {
        super(context);
        init();
    }

    public SubjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubjectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setStyle("subjectreview/subjectview.json");
        setFocusable(true);
        setBackgroundDrawable(ResourceUtil.getDrawable(R.drawable.share_item_rect_selector));
        this.mCoreImageView = getImageView("ID_IMAGE");
        this.mTitleBG = getImageView("ID_TITLE_BG");
        this.mTitleView = getTextView("ID_TITLE");
        setDefaultImage();
        this.mTitleBG.setDrawable(EpgImageCache.CARD_COVER_COLOR_UNFOCUS_DRAWABLE);
    }

    public void setDefaultImage() {
        this.mCoreImageView.setDrawable(ImageCacheUtil.DEFAULT_DRAWABLE);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        invalidateTitleBGState(gainFocus);
    }

    private void invalidateTitleBGState(boolean gainFocus) {
        if (TextUtils.isEmpty(this.mTitleView.getText())) {
            this.mTitleBG.setDrawable(null);
        } else if (gainFocus) {
            this.mTitleBG.setDrawable(ImageCacheUtil.TITLE_FOCUS_DRAWABLE);
        } else {
            this.mTitleBG.setDrawable(EpgImageCache.CARD_COVER_COLOR_UNFOCUS_DRAWABLE);
        }
    }

    public void releaseData() {
        setDefaultImage();
        clearTitle();
    }

    private void clearTitle() {
        this.mTitleView.setText("");
        setContentDescription("");
        invalidateTitleBGState(hasFocus());
    }

    public void setTitle(String title) {
        this.mTitleView.setText(title);
        setContentDescription(title);
        invalidateTitleBGState(hasFocus());
    }

    public void setImage(Bitmap bitmap) {
        this.mCoreImageView.setBitmap(bitmap);
    }
}
