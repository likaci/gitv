package com.gala.video.app.epg.ui.multisubject.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import com.gala.video.app.epg.R;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MultiSubjectBgView extends CloudView {
    private CuteImageView mCoreView;

    public MultiSubjectBgView(Context defStyle, AttributeSet attrs, int context) {
        super(defStyle, attrs, context);
        init();
    }

    public MultiSubjectBgView(Context attrs, AttributeSet context) {
        super(attrs, context);
        init();
    }

    public MultiSubjectBgView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setStyle("multisubject/bg.json");
        setFocusable(false);
        setFocusableInTouchMode(false);
        getCoreImageView().setDrawable(ResourceUtil.getDrawable(R.color.setting_night_bg));
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            getCoreImageView().setBitmap(bitmap);
            getCover().setDrawable(ResourceUtil.getDrawable(R.drawable.epg_multisubject_night_cover));
            AnimationUtil.fadeInAnimation(this, 0.0f, 1000);
        }
    }

    private CuteImageView getCoreImageView() {
        if (this.mCoreView == null) {
            this.mCoreView = getImageView("ID_IMAGE_BG");
        }
        return this.mCoreView;
    }

    private CuteImageView getCover() {
        return getImageView("ID_COVER");
    }
}
