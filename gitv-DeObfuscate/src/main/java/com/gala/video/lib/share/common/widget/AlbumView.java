package com.gala.video.lib.share.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.cloudui.Gravity4CuteImage;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.configs.ViewConstant;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class AlbumView extends CloudView {
    private final String TAG = ("AlbumView@" + hashCode());
    private boolean isShowPlaying;
    private CuteImageView mBottomBgGreenColorView;
    private CuteImageView mCornerLB1;
    private CuteImageView mCornerLB2;
    private CuteImageView mCornerLT;
    private CuteImageView mCornerPlaying;
    private CuteImageView mCornerRT;
    private CuteTextView mDescLine1LeftView;
    private CuteTextView mDescLine1RightView;
    private CuteTextView mDescLine3View;
    private boolean mDisableGifAnim;
    private CuteImageView mImageView;
    private CuteImageView mLeftBottomBg;
    private ILiveCornerFactory mLiveCornerFactory;
    private final LiveCornerListener mLiveCornerListener = new C16421();
    protected CuteImageView mRecycleCoverView;
    protected CuteTextView mRecycleTitle2View;
    protected CuteTextView mRecycleTitleView;
    private RequestFocusDelegator mRequestFocusDelegator;
    private CuteTextView mScoreView;
    private CuteTextView mTitleView;
    private AlbumViewType mViewType;

    class C16421 implements LiveCornerListener {
        C16421() {
        }

        public void showBefore() {
            AlbumView.this.setCornerBeforeLiveVisible();
        }

        public void showPlaying() {
            AlbumView.this.setCornerLivingVisible();
        }

        public void showEnd() {
            AlbumView.this.setCornerEndLiveVisible();
        }
    }

    interface RequestFocusDelegator {
        boolean requestFocus(AlbumView albumView, int i, Rect rect);
    }

    public AlbumView(Context context) {
        super(context);
        checkContext(context);
    }

    public AlbumView(Context context, AlbumViewType type) {
        super(context);
        checkContext(context);
        init(type);
    }

    public AlbumViewType getViewType() {
        return this.mViewType;
    }

    public void setViewType(AlbumViewType mViewType) {
        this.mViewType = mViewType;
    }

    private void init(AlbumViewType type) {
        initType(type);
        createItem();
    }

    private void initType(AlbumViewType type) {
        this.mViewType = type;
        if (!(type == AlbumViewType.VERTICAL || type == AlbumViewType.HORIZONTAL || type == AlbumViewType.RECOMMEND_VERTICAL || type == AlbumViewType.RECOMMEND_HORIZONTAL)) {
            this.mDisableGifAnim = Project.getInstance().getControl().disableGifAnimForDetailPage();
        }
        setStyle(ViewConstant.VERTICAL_JSON_PATH);
    }

    private void createItem() {
        setBackgroundDrawable(ImageCacheUtil.RECT_BG_DRAWABLE);
        setTitleUI();
        setTitle(null);
        initListener();
    }

    private void setTitleUI() {
        getTitleView();
        if (this.mViewType == AlbumViewType.PLAYER_HORIZONAL || this.mViewType == AlbumViewType.EXITDIALOG_VERTICAL) {
            this.mTitleView.setNormalColor(ResourceUtil.getColor(C1632R.color.player_ui_text_color_default));
        } else {
            this.mTitleView.setNormalColor(ResourceUtil.getColor(C1632R.color.albumview_normal_color));
        }
    }

    public void releaseData() {
        clearLiveCorner();
        setCornerDujiaDuboLiveGone();
        setCornerDolby3DGone();
        setCornerVIPBuyGone();
        setDescLine1Right(null);
        setTitle(null);
        setFilmScore("");
        setDescLine1Left(null);
    }

    public void releaseCorner() {
        clearLiveCorner();
        setCornerDujiaDuboLiveGone();
        setCornerDolby3DGone();
        setCornerVIPBuyGone();
    }

    public void setImageBitmap(Bitmap b) {
        getImageView();
        if (this.mImageView != null) {
            this.mImageView.setBitmap(b);
        }
    }

    public void setImageDrawable(Drawable d) {
        getImageView();
        if (this.mImageView != null) {
            this.mImageView.setDrawable(d);
        }
    }

    public void setRecycleDrawable(Drawable d) {
        getRecycleCoverView();
        if (this.mRecycleCoverView != null) {
            this.mRecycleCoverView.setDrawable(d);
        }
    }

    public void setRecycleTitle(String title) {
        getRecycleTitleView();
        if (this.mRecycleTitleView != null) {
            this.mRecycleTitleView.setText(title);
        }
    }

    public void setRecycleTitle2(String title) {
        getRecycleTitle2View();
        if (this.mRecycleTitle2View != null) {
            this.mRecycleTitle2View.setText(title);
        }
    }

    public void setRecycleCoverVisible(int visible) {
        getRecycleCoverView();
        if (this.mRecycleCoverView != null) {
            this.mRecycleCoverView.setVisible(visible);
            if (visible == 0) {
                focusRecyclerCover(false);
                if (hasFocus()) {
                    focusRecyclerCover(true);
                }
            } else {
                getRecycleTitleView();
                if (this.mRecycleTitleView != null) {
                    this.mRecycleTitleView.setText("");
                }
                getRecycleTitle2View();
                if (this.mRecycleTitle2View != null) {
                    this.mRecycleTitle2View.setText("");
                }
            }
        }
        CuteImageView imageView = getImageView("ID_RECYCLE_COVER");
        if (imageView != null) {
            imageView.setVisible(visible);
            imageView.setDrawable(ImageCacheUtil.RECYCLE_COVER);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearLiveCorner();
    }

    protected void clearLiveCorner() {
        if (this.mLiveCornerFactory != null) {
            this.mLiveCornerFactory.end();
        }
    }

    public void setCorner(IData info) {
        boolean isVip = info.getCornerStatus(0);
        boolean isSingleBuy = info.getCornerStatus(1);
        boolean isCoupons = info.getCornerStatus(7);
        boolean isDubo = info.getCornerStatus(2);
        boolean isZhuanTi = info.getCornerStatus(6);
        boolean isDujia = info.getCornerStatus(3);
        if (isCoupons) {
            setCornerCouponsVisible();
        } else if (isSingleBuy) {
            setCornerBuyVisible();
        } else if (isVip) {
            setCornerVIPVisible();
        }
        clearLiveCorner();
        if ((info.getData() instanceof ChannelLabel) && !LivePlayingType.DEFAULT.equals(((ChannelLabel) info.getData()).getLivePlayingType())) {
            if (this.mLiveCornerFactory == null) {
                this.mLiveCornerFactory = CreateInterfaceTools.createLiveCornerFactory();
            }
            this.mLiveCornerFactory.start((ChannelLabel) info.getData(), this.mLiveCornerListener);
        } else if (isDujia) {
            setCornerDujiaVisible();
        } else if (isDubo) {
            setCornerDuboVisible();
        } else if (isZhuanTi) {
            setCornerZhuanTiVisible();
        }
        setLeftBottomConner(info);
    }

    public void setLeftBottomConner(IData info) {
        Album album = info.getAlbum();
        if (album != null) {
            AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
            if (isPlaying()) {
                setCornerDolby3DGone();
            } else if (albumType == AlbumKind.SIGLE_VIDEO && GetInterfaceTools.getCornerProvider().isSpecialChannel(StringUtils.parse(info.getField(2), 0))) {
                boolean isDolby = info.getCornerStatus(4);
                boolean is3d = info.getCornerStatus(5);
                if (isDolby && is3d) {
                    setCornerDolby3DVisible();
                } else if (isDolby && !is3d) {
                    setCornerDolbyVisible();
                } else if (!isDolby && is3d) {
                    setCorner3DVisible();
                }
            }
        }
    }

    private void setCornerDujiaDuboLiveGone() {
        getCornerRTView();
        if (this.mCornerRT != null && this.mCornerRT.getDrawable() != null) {
            this.mCornerRT.setDrawable(null);
        }
    }

    private void setCornerBeforeLiveVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ImageCacheUtil.CORNER_NOTICE_DRAWABLE);
        }
    }

    private void setCornerLivingVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ImageCacheUtil.CORNER_LIVING_DRAWABLE);
        }
    }

    private void setCornerEndLiveVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ImageCacheUtil.CORNER_END_LIVING_DRAWABLE);
        }
    }

    private void setCornerDujiaVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ResourceUtil.getDrawable(C1632R.drawable.share_corner_dujia));
        }
    }

    private void setCornerDuboVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ImageCacheUtil.CORNER_DUBO_DRAWABLE);
        }
    }

    private void setCornerZhuanTiVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ResourceUtil.getDrawable(C1632R.drawable.share_corner_zhuanti));
        }
    }

    public void setCornerNoticeVisible() {
        getCornerRTView();
        if (this.mCornerRT != null) {
            this.mCornerRT.setDrawable(ImageCacheUtil.CORNER_NOTICE_DRAWABLE);
        }
    }

    private void setCornerVIPBuyGone() {
        getCornerLTView();
        if (this.mCornerLT != null && this.mCornerLT.getDrawable() != null) {
            this.mCornerLT.setDrawable(null);
        }
    }

    private void setCornerVIPVisible() {
        getCornerLTView();
        if (this.mCornerLT != null) {
            this.mCornerLT.setDrawable(ImageCacheUtil.CORNER_VIP_DRAWABLE);
        }
    }

    private void setCornerBuyVisible() {
        getCornerLTView();
        if (this.mCornerLT != null) {
            this.mCornerLT.setDrawable(ImageCacheUtil.CORNER_FUFEIDIANBO_DRAWABLE);
        }
    }

    private void setCornerCouponsVisible() {
        getCornerLTView();
        if (this.mCornerLT != null) {
            this.mCornerLT.setDrawable(ImageCacheUtil.CORNER_DIANBOQUAN_DRAWABLE);
        }
    }

    private void setCornerDolby3DVisible() {
        getCornerLB1View();
        getCornerLB2View();
        getLeftBottomCornerBgView();
        if (this.mCornerLB1 != null && this.mCornerLB2 != null) {
            this.mCornerLB1.setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
            this.mCornerLB2.setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
            this.mLeftBottomBg.setVisible(0);
            this.mLeftBottomBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
            this.mLeftBottomBg.setWidth(ResourceUtil.getDimen(C1632R.dimen.dimen_96dp));
        }
    }

    private void setCornerDolby3DGone() {
        getCornerLB1View();
        getCornerLB2View();
        getLeftBottomCornerBgView();
        if (this.mCornerLB1 != null && this.mCornerLB2 != null) {
            if (this.mCornerLB1.getDrawable() != null) {
                this.mCornerLB1.setDrawable(null);
            }
            if (this.mCornerLB2.getDrawable() != null) {
                this.mCornerLB2.setDrawable(null);
            }
            if (!this.isShowPlaying) {
                this.mLeftBottomBg.setVisible(8);
            }
        }
    }

    private void setCornerDolbyVisible() {
        getCornerLB1View();
        getCornerLB2View();
        getLeftBottomCornerBgView();
        if (this.mCornerLB1 != null && this.mCornerLB2 != null) {
            this.mCornerLB1.setDrawable(ImageCacheUtil.CORNER_DOLBY_DRAWABLE);
            this.mCornerLB2.setDrawable(null);
            this.mLeftBottomBg.setVisible(0);
            this.mLeftBottomBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
            this.mLeftBottomBg.setWidth(ResourceUtil.getDimen(C1632R.dimen.dimen_65dp));
        }
    }

    private void setCorner3DVisible() {
        getCornerLB1View();
        getCornerLB2View();
        getLeftBottomCornerBgView();
        if (this.mCornerLB1 != null && this.mCornerLB2 != null) {
            this.mCornerLB1.setDrawable(ImageCacheUtil.CORNER_3D_DRAWABLE);
            this.mCornerLB2.setDrawable(null);
            this.mLeftBottomBg.setVisible(0);
            this.mLeftBottomBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
            this.mLeftBottomBg.setWidth(ResourceUtil.getDimen(C1632R.dimen.dimen_48dp));
        }
    }

    public void setDescLine1Left(String str) {
        getDescLine1LeftView();
        if (this.mDescLine1LeftView != null && !StringUtils.equals(this.mDescLine1LeftView.getText(), str)) {
            this.mDescLine1LeftView.setText(str);
            this.mDescLine1LeftView.setBgDrawable(ImageCacheUtil.CORNER_BG_LEFT);
        }
    }

    public void setDescLine1Right(String str) {
        getDescLine1RightView();
        if (this.mDescLine1RightView != null && !StringUtils.equals(this.mDescLine1RightView.getText(), str)) {
            this.mDescLine1RightView.setText(str);
            this.mDescLine1RightView.setBgDrawable(ImageCacheUtil.CORNER_BG_RIGHT);
        }
    }

    public synchronized void setPlaying(boolean isPlaying) {
        getGifView();
        getLeftBottomCornerBgView();
        if (this.mCornerPlaying != null) {
            if (isPlaying) {
                if (this.mDisableGifAnim) {
                    this.mCornerPlaying.setResourceId(C1632R.drawable.share_detail_gif_playing_6);
                } else {
                    this.mCornerPlaying.setResourceId(C1632R.drawable.share_detail_gif_playing);
                    AnimationDrawable drawable = (AnimationDrawable) this.mCornerPlaying.getDrawable();
                    if (drawable != null) {
                        drawable.start();
                    }
                }
                this.mCornerPlaying.setVisible(0);
                this.isShowPlaying = true;
                this.mLeftBottomBg.setVisible(0);
                this.mLeftBottomBg.setDrawable(ImageCacheUtil.CORNER_BG_LEFT);
                this.mLeftBottomBg.setWidth(ResourceUtil.getDimen(C1632R.dimen.dimen_48dp));
            } else {
                this.mCornerPlaying.setResourceId(0);
                this.mCornerPlaying.setVisible(8);
                this.isShowPlaying = false;
                this.mLeftBottomBg.setVisible(8);
            }
        }
    }

    public synchronized boolean isPlaying() {
        return this.isShowPlaying;
    }

    public void setFilmScore(String ratingScore) {
        getScoreView();
        if (this.mScoreView != null) {
            if (!TextUtils.isEmpty(ratingScore)) {
                float parseFloat = 0.0f;
                try {
                    parseFloat = Float.parseFloat(ratingScore);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (parseFloat <= 0.0f || parseFloat > 10.0f) {
                    ratingScore = "";
                }
            }
            this.mScoreView.setText(ratingScore);
            if (TextUtils.isEmpty(ratingScore)) {
                this.mScoreView.setVisible(8);
                this.mScoreView.setBgDrawable(null);
                return;
            }
            this.mScoreView.setVisible(0);
            this.mScoreView.setBgDrawable(ImageCacheUtil.CORNER_BG_RIGHT);
        }
    }

    public void setTitle(String str) {
        getTitleView();
        if (this.mTitleView != null) {
            this.mTitleView.setText(str);
            focusTitleAndBg(hasFocus());
        }
        setContentDescription(str);
    }

    private void initListener() {
        final OnFocusChangeListener onFocusChangeListener = getOnFocusChangeListener();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(v, hasFocus);
                }
                AlbumView.this.focusRecyclerCover(hasFocus);
                AlbumView.this.focusTitleAndBg(hasFocus);
            }
        });
    }

    protected void focusRecyclerCover(boolean hasFocus) {
    }

    private void focusTitleAndBg(boolean hasFocus) {
        if (TextUtils.isEmpty(this.mTitleView.getText())) {
            changeLine1UI(hasFocus);
        } else if (hasFocus) {
            this.mTitleView.setLines(2);
            if (this.mTitleView.getRealLineCount() == 1) {
                setBgPaddingBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_19dp));
                this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_17dp));
                changeBottomBgLine1(hasFocus);
            } else if (this.mTitleView.getRealLineCount() == 2) {
                setBgPaddingBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_2dp));
                this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_8dp));
                changeBottomBgLine2();
            }
        } else {
            changeLine1UI(hasFocus);
        }
    }

    private void changeLine1UI(boolean hasFocus) {
        setBgPaddingBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_19dp));
        this.mTitleView.setLines(1);
        this.mTitleView.setMarginBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_17dp));
        changeBottomBgLine1(hasFocus);
    }

    private void changeBottomBgLine1(boolean flag) {
        getBottomBgView();
        this.mBottomBgGreenColorView.setWidth(0);
        this.mBottomBgGreenColorView.setHeight(ResourceUtil.getPx(54));
        this.mBottomBgGreenColorView.setGravity(Gravity4CuteImage.CENTER_OF_BOTTOM);
        this.mBottomBgGreenColorView.setMarginBottom(ResourceUtil.getPx(26));
        Drawable unfocused = getUnfocusedBottomBgDrawable();
        CuteImageView cuteImageView = this.mBottomBgGreenColorView;
        if (flag) {
            unfocused = ImageCacheUtil.TITLE_FOCUS_DRAWABLE;
        }
        cuteImageView.setDrawable(unfocused);
    }

    protected Drawable getUnfocusedBottomBgDrawable() {
        return (this.mViewType == AlbumViewType.PLAYER_HORIZONAL || this.mViewType == AlbumViewType.EXITDIALOG_VERTICAL) ? ImageCacheUtil.UNCOVER_COLOR_UNFOCUS_DRAWABLE_FOR_PLAYER : ImageCacheUtil.UNCOVER_COLOR_UNFOCUS_DRAWABLE;
    }

    private void changeBottomBgLine2() {
        this.mBottomBgGreenColorView.setWidth(0);
        this.mBottomBgGreenColorView.setHeight(ResourceUtil.getDimen(C1632R.dimen.dimen_53dp));
        this.mBottomBgGreenColorView.setGravity(Gravity4CuteImage.CENTER_OF_BOTTOM);
        this.mBottomBgGreenColorView.setMarginBottom(ResourceUtil.getDimen(C1632R.dimen.dimen_0dp));
        this.mBottomBgGreenColorView.setDrawable(ImageCacheUtil.TITLE_FOCUS_DRAWABLE);
    }

    private CuteImageView getCornerRTView() {
        if (this.mCornerRT == null) {
            this.mCornerRT = getImageView("ID_CORNER_R_T");
        }
        return this.mCornerRT;
    }

    private CuteImageView getCornerLTView() {
        if (this.mCornerLT == null) {
            this.mCornerLT = getImageView("ID_CORNER_L_T");
        }
        return this.mCornerLT;
    }

    private CuteImageView getCornerLB1View() {
        if (this.mCornerLB1 == null) {
            this.mCornerLB1 = getImageView("ID_CORNER_L_B_1");
        }
        return this.mCornerLB1;
    }

    private CuteImageView getCornerLB2View() {
        if (this.mCornerLB2 == null) {
            this.mCornerLB2 = getImageView("ID_CORNER_L_B_2");
        }
        return this.mCornerLB2;
    }

    public CuteImageView getImageView() {
        if (this.mImageView == null) {
            this.mImageView = getImageView("ID_IMAGE");
        }
        return this.mImageView;
    }

    public CuteImageView getRecycleCoverView() {
        if (this.mRecycleCoverView == null) {
            this.mRecycleCoverView = getImageView(ViewConstant.ID_RECYCLE);
        }
        return this.mRecycleCoverView;
    }

    public CuteTextView getRecycleTitleView() {
        if (this.mRecycleTitleView == null) {
            this.mRecycleTitleView = getTextView(ViewConstant.ID_RECYCLE_TITLE);
        }
        return this.mRecycleTitleView;
    }

    public CuteTextView getRecycleTitle2View() {
        if (this.mRecycleTitle2View == null) {
            this.mRecycleTitle2View = getTextView(ViewConstant.ID_RECYCLE_TITLE2);
        }
        return this.mRecycleTitle2View;
    }

    private CuteImageView getGifView() {
        if (this.mCornerPlaying == null) {
            this.mCornerPlaying = getImageView(ViewConstant.ID_PLAYING);
        }
        return this.mCornerPlaying;
    }

    protected CuteTextView getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getTextView("ID_TITLE");
            this.mTitleView.setNormalColor(ResourceUtil.getColor(C1632R.color.albumview_normal_color));
        }
        return this.mTitleView;
    }

    private CuteImageView getBottomBgView() {
        if (this.mBottomBgGreenColorView == null) {
            this.mBottomBgGreenColorView = getImageView(ViewConstant.ID_BOTTOM_BG);
        }
        return this.mBottomBgGreenColorView;
    }

    private CuteTextView getDescLine1LeftView() {
        if (this.mDescLine1LeftView == null) {
            this.mDescLine1LeftView = getTextView(ViewConstant.ID_DESC_1_L);
        }
        return this.mDescLine1LeftView;
    }

    private CuteTextView getDescLine1RightView() {
        if (this.mDescLine1RightView == null) {
            this.mDescLine1RightView = getTextView(ViewConstant.ID_DESC_1_R);
        }
        return this.mDescLine1RightView;
    }

    private CuteTextView getDescLine3View() {
        if (this.mDescLine3View == null) {
            this.mDescLine3View = getTextView(ViewConstant.ID_DESC_3);
        }
        return this.mDescLine3View;
    }

    private CuteTextView getScoreView() {
        if (this.mScoreView == null) {
            this.mScoreView = getTextView("ID_SCORE");
        }
        return this.mScoreView;
    }

    private CuteImageView getLeftBottomCornerBgView() {
        if (this.mLeftBottomBg == null) {
            this.mLeftBottomBg = getImageView("ID_CORNER_BG_LEFT");
        }
        return this.mLeftBottomBg;
    }

    private void checkContext(Context context) {
        if (context instanceof Activity) {
            throw new IllegalArgumentException("Please use application context for creating AlbumView !");
        }
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (this.mRequestFocusDelegator != null) {
            return this.mRequestFocusDelegator.requestFocus(this, direction, previouslyFocusedRect);
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    void setRequestFocusDelegator(RequestFocusDelegator delegator) {
        this.mRequestFocusDelegator = delegator;
    }

    boolean superRequestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    public String toString() {
        return this.TAG;
    }
}
