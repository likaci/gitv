package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.cloudui.Gravity4CuteText;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class RecommendView extends CloudView {
    private static final int IMAGE_PADDING = ResourceUtil.getDimen(R.dimen.dimen_28dp);
    private static final String TAG = "EPG/RecommendView";
    private CuteImageView mCoreImageView;
    private CuteImageView mCornerImageLT;
    private CuteImageView mCornerImageRT;
    private float mFocusScale = 1.0f;
    private int mHeight;
    private ILiveCornerFactory mLiveCornerFactory;
    private final LiveCornerListener mLiveCornerListener = new LiveCornerListener() {
        public void showBefore() {
            RecommendView.this.setRTCornerimage(R.drawable.share_corner_notice);
        }

        public void showPlaying() {
            RecommendView.this.setRTCornerimage(R.drawable.share_corner_living);
        }

        public void showEnd() {
            RecommendView.this.setRTCornerimage(R.drawable.share_corner_end_living);
        }
    };
    private CuteTextView mNameView;
    private OnSelectedListener mOnSelectedListener;
    private int mPhotoType = 1;
    private String mTitle;
    private CuteImageView mTitleBgView;
    private CuteTextView mTitleView;
    private int mWidth;

    public interface OnSelectedListener {
        void onSelected(View view, boolean z);
    }

    public RecommendView(Context context) {
        super(context);
    }

    public RecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, 0);
    }

    public void setViewParams(int type, int width, int height) {
        setFocusable(true);
        this.mPhotoType = type;
        this.mWidth = width;
        this.mHeight = height;
        setBackgroundResource(R.drawable.share_item_rect_selector_recommendview);
        initView();
        initListener();
        initTitleBg();
    }

    private void initTitleBg() {
        if (this.mPhotoType == 1) {
            this.mTitleBgView.setHeight(ResourceUtil.getDimen(R.dimen.dimen_36dp));
        } else if (this.mPhotoType == 2) {
            this.mTitleBgView.setHeight(ResourceUtil.getDimen(R.dimen.dimen_60dp));
            this.mTitleView.setGravity(Gravity4CuteText.CENTER_NONE);
            this.mTitleView.setMarginBottom(ResourceUtil.getDimen(R.dimen.dimen_28dp));
            this.mNameView.setMarginBottom(ResourceUtil.getDimen(R.dimen.dimen_6dp));
        } else {
            this.mTitleBgView.setHeight(ResourceUtil.getDimen(R.dimen.dimen_36dp));
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "initTitleBg no mPhotoType");
            }
        }
    }

    private void initView() {
        LayoutParams lp = new LayoutParams(this.mWidth + IMAGE_PADDING, this.mHeight + IMAGE_PADDING);
        lp.leftMargin = ResourceUtil.getDimen(R.dimen.dimen_24dp);
        lp.topMargin = ResourceUtil.getDimen(R.dimen.dimen_2dp);
        setLayoutParams(lp);
        this.mTitleView = getTitleView();
        this.mTitleBgView = getFreeImageView1();
        this.mCoreImageView = getCoreImageView();
        this.mNameView = getNameView();
        this.mCornerImageRT = getCornerRTView();
        this.mCornerImageLT = getCornerLTView();
        this.mCoreImageView.setDrawable(ResourceUtil.getDrawable(R.drawable.share_default_image));
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearLiveCorner();
    }

    private void clearLiveCorner() {
        if (this.mLiveCornerFactory != null) {
            this.mLiveCornerFactory.end();
        }
    }

    private void initListener() {
        final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
                AnimationUtil.zoomAnimation(v, hasFocus ? RecommendView.this.mFocusScale : 1.0f, 250);
                RecommendView.this.focusCarouselChannelBg(hasFocus);
                if (RecommendView.this.mOnSelectedListener != null) {
                    RecommendView.this.mOnSelectedListener.onSelected(v, hasFocus);
                }
            }
        });
    }

    public void setFocusScale(float scale) {
        this.mFocusScale = scale;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mOnSelectedListener = listener;
    }

    private void focusCarouselChannelBg(boolean hasFocus) {
        if (!TextUtils.isEmpty(this.mTitle)) {
            setTextBgDrawable(hasFocus ? ImageCacheUtil.TITLE_FOCUS_DRAWABLE : EpgImageCache.COVER_COLOR_UNFOCUS_DRAWABLE_FOR_RECOMMENDVIEW);
        }
    }

    public void setImage(Bitmap bit) {
        if (this.mCoreImageView != null) {
            this.mCoreImageView.setBitmap(bit);
        }
    }

    public void setTextBgDrawable(Drawable drawable) {
        if (this.mTitleBgView != null) {
            this.mTitleBgView.setDrawable(drawable);
        }
    }

    public void setTitle(String str) {
        this.mTitle = str;
        if (!TextUtils.isEmpty(this.mTitle) && this.mTitleView != null) {
            this.mTitleView.setText(this.mTitle);
        }
    }

    public void setNameText(String str) {
        if (!TextUtils.isEmpty(str) && this.mNameView != null) {
            this.mNameView.setText(str);
        }
    }

    public void setCornerImage(IData info) {
        if (info != null) {
            setCornerLTImage(info);
            setCornerRTImage(info);
        }
    }

    private void setCornerLTImage(IData info) {
        boolean isVip = info.getCornerStatus(0);
        boolean isSingleBuy = info.getCornerStatus(1);
        int leftResId = 0;
        if (info.getCornerStatus(7)) {
            leftResId = R.drawable.share_corner_yongquan;
        } else if (isSingleBuy) {
            leftResId = R.drawable.share_corner_fufeidianbo;
        } else if (isVip) {
            leftResId = R.drawable.share_corner_vip;
        }
        if (leftResId != 0 && this.mCornerImageLT != null) {
            this.mCornerImageLT.setDrawable(ResourceUtil.getDrawable(leftResId));
        }
    }

    private void setCornerRTImage(IData info) {
        boolean isDubo = info.getCornerStatus(2);
        boolean isDujia = info.getCornerStatus(3);
        clearLiveCorner();
        int resId = 0;
        if ((info.getData() instanceof ChannelLabel) && !LivePlayingType.DEFAULT.equals(((ChannelLabel) info.getData()).getLivePlayingType())) {
            if (this.mLiveCornerFactory == null) {
                this.mLiveCornerFactory = CreateInterfaceTools.createLiveCornerFactory();
            }
            this.mLiveCornerFactory.start((ChannelLabel) info.getData(), this.mLiveCornerListener);
        } else if (isDujia) {
            resId = R.drawable.share_corner_dujia;
        } else if (isDubo) {
            resId = R.drawable.share_corner_dubo;
        }
        if (resId != 0) {
            setRTCornerimage(resId);
        }
    }

    private void setRTCornerimage(int resId) {
        if (this.mCornerImageRT != null) {
            this.mCornerImageRT.setDrawable(ResourceUtil.getDrawable(resId));
        }
    }

    public CuteImageView getCoreImageView() {
        return getImageView("ID_IMAGE");
    }

    public CuteImageView getCornerRTView() {
        return getImageView("ID_CORNER_R_T");
    }

    public CuteImageView getCornerLTView() {
        return getImageView("ID_CORNER_L_T");
    }

    public CuteImageView getFreeImageView1() {
        return getImageView("ID_FREE_IMAGE_1");
    }

    public CuteTextView getTitleView() {
        return getTextView("ID_TITLE");
    }

    public CuteTextView getNameView() {
        return getTextView("ID_NAME");
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() != 66 && event.getKeyCode() != 23) || event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        performClick();
        return true;
    }
}
