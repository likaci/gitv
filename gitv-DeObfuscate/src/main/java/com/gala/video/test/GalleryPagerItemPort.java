package com.gala.video.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.uiutils.albumcorner.AlbumItemCornerImage;
import com.gala.video.widget.test.R;
import com.gala.video.widget.util.LogUtils;

public class GalleryPagerItemPort extends AbsGalleryPagerItem implements OnFocusChangeListener {
    private static final String TAG = "gridpageview/GalleryPagerItemPort";
    private ImageView m3dCorner;
    private Rect mBgDrawablePaddings;
    private ImageView mDolbyCorner;
    protected AlbumItemCornerImage mImage;
    protected View mImageContainer;
    protected int mItemHeightPadded;
    protected int mItemSpacing;
    protected int mItemWidthPadded;
    protected OnLayoutChangeListener mLayoutListener = new C18731();
    protected View mLayoutView;
    private TextView mOnlineTime;
    protected String mOriginalText;
    private TextView mTime;
    protected TextView mTxtAlbumTitle;
    private TextView mTxtScore;
    private ImageView mVipCorner;
    protected boolean mWasFocused;

    class C18731 implements OnLayoutChangeListener {
        C18731() {
        }

        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int width = right - left;
            LogUtils.m1598d(GalleryPagerItemPort.TAG, "onLayoutChange: width=" + width);
            if (width > 0) {
                CharSequence ellipsized = TextUtils.ellipsize(GalleryPagerItemPort.this.mOriginalText, GalleryPagerItemPort.this.mTxtAlbumTitle.getPaint(), (float) width, TruncateAt.END);
                GalleryPagerItemPort.this.mTxtAlbumTitle.setText(ellipsized);
                LogUtils.m1598d(GalleryPagerItemPort.TAG, "setTitle: ellipsized=" + ellipsized);
            }
        }
    }

    public GalleryPagerItemPort(Context context) {
        super(context);
        initViews(context);
    }

    public GalleryPagerItemPort(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public GalleryPagerItemPort(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    protected void initViews(Context context) {
        this.mLayoutView = LayoutInflater.from(context).inflate(R.layout.gallery_pager_item_port, null, false);
        this.mImageContainer = this.mLayoutView.findViewById(R.id.fl_image_container);
        this.mImage = (AlbumItemCornerImage) this.mLayoutView.findViewById(R.id.image_album_cover);
        this.mTxtAlbumTitle = (TextView) this.mLayoutView.findViewById(R.id.txt_album_title);
        this.mTxtScore = (TextView) this.mLayoutView.findViewById(R.id.txt_score);
        this.mDolbyCorner = (ImageView) this.mLayoutView.findViewById(R.id.port_dolby);
        this.m3dCorner = (ImageView) this.mLayoutView.findViewById(R.id.port_3d);
        this.mVipCorner = (ImageView) this.mLayoutView.findViewById(R.id.port_vip_corner);
        this.mOnlineTime = (TextView) this.mLayoutView.findViewById(R.id.online_time);
        this.mTime = (TextView) this.mLayoutView.findViewById(R.id.txt_album_time);
        LayoutParams titleParams = (LayoutParams) this.mTxtAlbumTitle.getLayoutParams();
        titleParams.topMargin -= getBgDrawablePaddings().top;
        this.mTxtAlbumTitle.setLayoutParams(titleParams);
        this.mTxtAlbumTitle.setLayerType(2, null);
        this.mItemWidthPadded = (this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_133dp) + getBgDrawablePaddings().left) + getBgDrawablePaddings().right;
        this.mItemHeightPadded = ((this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_251dp) + getBgDrawablePaddings().top) + getBgDrawablePaddings().bottom) - getBgDrawablePaddings().bottom;
        this.mItemSpacing = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_36dp);
        LogUtils.m1598d(TAG, "initViews: padded w/h=" + this.mItemWidthPadded + "/" + this.mItemHeightPadded + ", item spacing=" + this.mItemSpacing);
        addView(this.mLayoutView, new LayoutParams(this.mItemWidthPadded, this.mItemHeightPadded));
        setGravity(17);
        sScaleAnimRatio = calculateZoomRatio();
        int outerH = Math.round(sScaleAnimRatio * ((float) this.mItemHeightPadded));
        LogUtils.m1598d(TAG, "initViews: item w/h=" + this.mItemWidthPadded + "/" + outerH);
        setLayoutParams(new AbsListView.LayoutParams(this.mItemWidthPadded, outerH));
    }

    public void setText(String text) {
        this.mOriginalText = text;
        this.mTxtAlbumTitle.setText(text);
        LogUtils.m1598d(TAG, "setTitle: " + text);
        this.mTxtAlbumTitle.addOnLayoutChangeListener(this.mLayoutListener);
    }

    public void setScore(String score) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1598d(TAG, "setScore(" + score + ")");
        }
        if (!StringUtils.isEmpty((CharSequence) score)) {
            this.mTxtScore.setText(score);
            this.mTxtScore.setTypeface(Typeface.DEFAULT, 2);
            this.mTxtScore.setVisibility(0);
        }
    }

    public void setCornerIconResId(int resId) {
        this.mImage.setCornerResId(resId);
    }

    public void setDolbyCornerResId(int resId) {
        this.mDolbyCorner.setImageResource(resId);
        this.mDolbyCorner.setVisibility(0);
    }

    public void set3DCornerResId(int resId) {
        this.m3dCorner.setImageResource(resId);
        this.m3dCorner.setVisibility(0);
    }

    public void setVipCornerResId(int resId) {
        this.mVipCorner.setImageResource(resId);
        this.mVipCorner.setVisibility(0);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mImage.setImageBitmap(bitmap);
    }

    public void setOnlineTime(String online) {
        if (!StringUtils.isEmpty((CharSequence) online)) {
            this.mOnlineTime.setText(online);
            this.mOnlineTime.setVisibility(0);
        }
    }

    public void updateTime(String time) {
        if (!StringUtils.isEmpty((CharSequence) time)) {
            this.mTime.setText(time);
            this.mTime.setVisibility(0);
        }
    }

    public void setImageBitmap(Bitmap bitmap, Animation anim) {
        this.mImage.setImageBitmap(bitmap);
        this.mImage.startAnimation(anim);
    }

    protected Rect getBgDrawablePaddings() {
        if (this.mBgDrawablePaddings != null) {
            LogUtils.m1598d(TAG, "getBgDrawablePaddings: " + this.mBgDrawablePaddings);
            return this.mBgDrawablePaddings;
        }
        this.mBgDrawablePaddings = new Rect();
        Drawable drawable = this.mImageContainer.getBackground();
        if (drawable != null) {
            drawable.getPadding(this.mBgDrawablePaddings);
        }
        LogUtils.m1598d(TAG, "getBgDrawablePaddings: " + this.mBgDrawablePaddings);
        return this.mBgDrawablePaddings;
    }

    protected float calculateZoomRatio() {
        return 1.05f;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        LogUtils.m1598d(TAG, "[" + hashCode() + "] onFocusChange: " + hasFocus + ", view={" + v + "}");
        this.mTxtAlbumTitle.removeOnLayoutChangeListener(this.mLayoutListener);
        if (hasFocus) {
            this.mTxtAlbumTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
            this.mTxtAlbumTitle.setSingleLine(false);
            this.mTxtAlbumTitle.setMaxLines(2);
            this.mTxtAlbumTitle.setText(TextUtils.ellipsize(this.mOriginalText, this.mTxtAlbumTitle.getPaint(), (float) (this.mTxtAlbumTitle.getWidth() * 2), TruncateAt.END));
            this.mImageContainer.setBackgroundDrawable(getFocusBackground());
            zoomIn(v);
        } else if (this.mWasFocused) {
            this.mTxtAlbumTitle.setTextColor(Color.parseColor("#FF999999"));
            this.mTxtAlbumTitle.setSingleLine(true);
            this.mTxtAlbumTitle.setText(TextUtils.ellipsize(this.mOriginalText, this.mTxtAlbumTitle.getPaint(), (float) this.mTxtAlbumTitle.getWidth(), TruncateAt.END));
            this.mImageContainer.setBackgroundDrawable(getNormalBackground());
            zoomOut(v);
        }
        this.mWasFocused = hasFocus;
    }

    protected void zoomIn(View view) {
        AnimationUtil.zoomInAnimation(view, sScaleAnimRatio);
    }

    protected void zoomOut(View view) {
        AnimationUtil.zoomOutAnimation(view, sScaleAnimRatio);
    }

    public Drawable getFocusBackground() {
        return getContext().getResources().getDrawable(R.drawable.bg_focus);
    }

    public Drawable getNormalBackground() {
        return getContext().getResources().getDrawable(R.drawable.bg_unfocus);
    }

    public void setBackgroundResource(int resid) {
        this.mImageContainer.setBackgroundResource(resid);
    }

    public void setIsPlaying(boolean isPlaying) {
    }
}
