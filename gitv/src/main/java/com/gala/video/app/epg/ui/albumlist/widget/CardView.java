package com.gala.video.app.epg.ui.albumlist.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.AnimeCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.FilmOfflineCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.FilmOnlineCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.ICard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.LiveCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.PersonCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.PlaylistCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.SeriesOfflineCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.SeriesOnlineCard;
import com.gala.video.app.epg.ui.albumlist.widget.cardtype.SourceCard;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class CardView extends CloudView implements ICard {
    private static final String LOG_TAG = "q/album4/CardView";
    private int DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1;
    private int DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2;
    private Drawable DIVIDE_LINE_DRAWABLE;
    private Drawable DU_JIA_DRAWABLE;
    private Drawable GALA_GREEN;
    private Drawable LINE_1_BG_DRAWABLE;
    private int RIGHT_DESC_TEXT_VIEW_SIZE_18;
    private int RIGHT_DESC_TEXT_VIEW_SIZE_19;
    private Drawable SIGN_MING_XI_DRAWABLE;
    private Drawable SIGN_YU_GAO_DRAWABLE;
    private Drawable SIGN_ZHUAN_TI_DRAWABLE;
    private int TITLE_MARGIN_BOTTOM_LINE_1;
    private int TITLE_MARGIN_BOTTOM_LINE_2;
    private Drawable XIN_DRAWABLE;
    private boolean isAnimeCard;
    private boolean isFilmOfflineCard;
    private boolean isFilmOnlineCard;
    private boolean isLiveCard;
    private boolean isPersonCard;
    private boolean isPlaylistCard;
    private boolean isSeriesOfflineCard;
    private boolean isSeriesOnlineCard;
    private boolean isSourceCard;
    private ICard mCard;
    private CuteImageView mCornerLB1View;
    private CuteImageView mCornerLB2View;
    private CuteImageView mCornerLBBgView;
    private CuteImageView mCornerLTView;
    private CuteImageView mCornerRTView;
    private List<CuteTextView> mDescViews;
    private CuteImageView mDivideLineView;
    private CuteImageView mImageView;
    private CuteImageView mLine1BgView;
    private ILiveCornerFactory mLiveCornerFactory;
    private CuteTextView mOfflineCountDown1View;
    private CuteTextView mOfflineCountDown2View;
    private CuteTextView mOfflineCountDown3View;
    private CuteTextView mPopupGreenView;
    private CuteTextView mRightDesc1View;
    private CuteTextView mRightDesc2View;
    private CuteTextView mRightDesc3View;
    private CuteTextView mRightDesc4View;
    private CuteImageView mRightFocusBg;
    private CuteTextView mScoreView;
    private CuteImageView mSignRBView;
    private CuteTextView mTitleView;
    private int mViewType;
    private CuteImageView mXinView;

    public CardView(Context context) {
        super(context);
        checkContext(context);
        prepareResValues();
        setStyle(ViewConstant.CARD_JSON_PATH);
        setBackgroundDrawable(ImageCacheUtil.RECT_BG_DRAWABLE);
    }

    private void prepareResValues() {
        this.mDescViews = new ArrayList(4);
        this.TITLE_MARGIN_BOTTOM_LINE_1 = getDimenSize(R.dimen.dimen_175dp);
        this.TITLE_MARGIN_BOTTOM_LINE_2 = getDimenSize(R.dimen.dimen_164dp);
        this.DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1 = getDimenSize(R.dimen.dimen_102dp);
        this.DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2 = getDimenSize(R.dimen.dimen_79dp);
        this.RIGHT_DESC_TEXT_VIEW_SIZE_19 = getDimenSize(R.dimen.dimen_20dp);
        this.RIGHT_DESC_TEXT_VIEW_SIZE_18 = getDimenSize(R.dimen.dimen_20dp);
        this.DIVIDE_LINE_DRAWABLE = getDrawable(R.drawable.epg_card_divide_line);
        this.XIN_DRAWABLE = getDrawable(R.drawable.epg_corner_xin);
        this.SIGN_YU_GAO_DRAWABLE = getDrawable(R.drawable.epg_corner_yugao);
        this.SIGN_ZHUAN_TI_DRAWABLE = getDrawable(R.drawable.epg_corner_card_zhuanti);
        this.SIGN_MING_XI_DRAWABLE = getDrawable(R.drawable.epg_corner_mingxing);
        this.LINE_1_BG_DRAWABLE = getDrawable(R.drawable.epg_card_desc_bg);
        this.DU_JIA_DRAWABLE = getDrawable(R.drawable.share_corner_dujia);
        this.GALA_GREEN = new ColorDrawable(ResourceUtil.getColor(R.color.gala_green));
    }

    public ILiveCornerFactory getLiveCornerFactory() {
        return this.mLiveCornerFactory;
    }

    public void setLiveCornerFactory(ILiveCornerFactory liveCornerFactory) {
        this.mLiveCornerFactory = liveCornerFactory;
    }

    public int getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1() {
        return this.DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1;
    }

    public int getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2() {
        return this.DESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2;
    }

    public List<CuteTextView> getDescViews() {
        return this.mDescViews;
    }

    public Drawable getDIVIDE_LINE_DRAWABLE() {
        return this.DIVIDE_LINE_DRAWABLE;
    }

    public Drawable getXIN_DRAWABLE() {
        return this.XIN_DRAWABLE;
    }

    public Drawable getSIGN_YU_GAO_DRAWABLE() {
        return this.SIGN_YU_GAO_DRAWABLE;
    }

    public Drawable getSIGN_ZHUAN_TI_DRAWABLE() {
        return this.SIGN_ZHUAN_TI_DRAWABLE;
    }

    public Drawable getSIGN_MING_XI_DRAWABLE() {
        return this.SIGN_MING_XI_DRAWABLE;
    }

    public Drawable getLINE_1_BG_DRAWABLE() {
        return this.LINE_1_BG_DRAWABLE;
    }

    public Drawable getDU_JIA_DRAWABLE() {
        return this.DU_JIA_DRAWABLE;
    }

    public int getRIGHT_DESC_TEXT_VIEW_SIZE_19() {
        return this.RIGHT_DESC_TEXT_VIEW_SIZE_19;
    }

    public int getRIGHT_DESC_TEXT_VIEW_SIZE_18() {
        return this.RIGHT_DESC_TEXT_VIEW_SIZE_18;
    }

    public int getTITLE_MARGIN_BOTTOM_LINE_1() {
        return this.TITLE_MARGIN_BOTTOM_LINE_1;
    }

    public int getTITLE_MARGIN_BOTTOM_LINE_2() {
        return this.TITLE_MARGIN_BOTTOM_LINE_2;
    }

    public void setTextData(IData info) {
        if (this.mCard == null) {
            LogUtils.e(LOG_TAG, "setTextData mCard == null, mViewType = ", Integer.valueOf(this.mViewType));
            return;
        }
        this.mCard.setTextData(info);
    }

    public void setImageBitmap(Bitmap bitmap) {
        getImageView().setBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        getImageView().setDrawable(drawable);
    }

    public void setImageData(IData info) {
        if (this.mCard == null) {
            LogUtils.e(LOG_TAG, "setImageData mCard == null, mViewType = ", Integer.valueOf(this.mViewType));
            return;
        }
        this.mCard.setImageData(info);
    }

    public void releaseData() {
        if (this.mCard != null) {
            this.mCard.releaseData();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearLiveCorner();
    }

    public void clearLiveCorner() {
        if (this.mLiveCornerFactory != null) {
            this.mLiveCornerFactory.end();
        }
    }

    public CuteImageView getImageView() {
        if (this.mImageView == null) {
            this.mImageView = getImageView("ID_IMAGE");
        }
        return this.mImageView;
    }

    public CuteImageView getCornerRTView() {
        if (this.mCornerRTView == null) {
            this.mCornerRTView = getImageView("ID_CORNER_R_T");
        }
        return this.mCornerRTView;
    }

    public CuteImageView getCornerLTView() {
        if (this.mCornerLTView == null) {
            this.mCornerLTView = getImageView("ID_CORNER_L_T");
        }
        return this.mCornerLTView;
    }

    public CuteImageView getCornerLB1View() {
        if (this.mCornerLB1View == null) {
            this.mCornerLB1View = getImageView("ID_CORNER_L_B_1");
        }
        return this.mCornerLB1View;
    }

    public CuteImageView getCornerLB2View() {
        if (this.mCornerLB2View == null) {
            this.mCornerLB2View = getImageView("ID_CORNER_L_B_2");
        }
        return this.mCornerLB2View;
    }

    public CuteImageView getCornerLBBgView() {
        if (this.mCornerLBBgView == null) {
            this.mCornerLBBgView = getImageView(ViewConstant.ID_CORNER_L_B_BG);
        }
        return this.mCornerLBBgView;
    }

    public CuteImageView getXinView() {
        if (this.mXinView == null) {
            this.mXinView = getImageView(ViewConstant.ID_XIN);
        }
        return this.mXinView;
    }

    public CuteImageView getSignRBView() {
        if (this.mSignRBView == null) {
            this.mSignRBView = getImageView(ViewConstant.ID_SIGN_R_B);
        }
        return this.mSignRBView;
    }

    public CuteTextView getPopupGreenView() {
        if (this.mPopupGreenView == null) {
            this.mPopupGreenView = getTextView(ViewConstant.ID_RIGHT_POPUP_GREEN);
        }
        return this.mPopupGreenView;
    }

    public CuteTextView getScoreView() {
        if (this.mScoreView == null) {
            this.mScoreView = getTextView("ID_SCORE");
        }
        return this.mScoreView;
    }

    public CuteImageView getLine1BgView() {
        if (this.mLine1BgView == null) {
            this.mLine1BgView = getImageView(ViewConstant.ID_DESC_BG_1);
        }
        return this.mLine1BgView;
    }

    public CuteTextView getTitleView() {
        if (this.mTitleView == null) {
            this.mTitleView = getTextView(ViewConstant.ID_RIGHT_TITLE);
        }
        return this.mTitleView;
    }

    public CuteImageView getDivideLineView() {
        if (this.mDivideLineView == null) {
            this.mDivideLineView = getImageView(ViewConstant.ID_DIVIDE_LINE_1);
        }
        return this.mDivideLineView;
    }

    public CuteTextView getOfflineCountDown1() {
        if (this.mOfflineCountDown1View == null) {
            this.mOfflineCountDown1View = getTextView(ViewConstant.ID_OFFLINE_COUNT_DOWN_1);
        }
        return this.mOfflineCountDown1View;
    }

    public CuteTextView getOfflineCountDown2() {
        if (this.mOfflineCountDown2View == null) {
            this.mOfflineCountDown2View = getTextView(ViewConstant.ID_OFFLINE_COUNT_DOWN_2);
        }
        return this.mOfflineCountDown2View;
    }

    public CuteTextView getOfflineCountDown3() {
        if (this.mOfflineCountDown3View == null) {
            this.mOfflineCountDown3View = getTextView(ViewConstant.ID_OFFLINE_COUNT_DOWN_3);
        }
        return this.mOfflineCountDown3View;
    }

    public CuteTextView getRightDesc1() {
        if (this.mRightDesc1View == null) {
            this.mRightDesc1View = getTextView(ViewConstant.ID_RIGHT_DESC_1);
        }
        return this.mRightDesc1View;
    }

    public CuteImageView getRightFocusBg() {
        if (this.mRightFocusBg == null) {
            this.mRightFocusBg = getImageView(ViewConstant.ID_RIGHT_FOCUS_BG);
        }
        return this.mRightFocusBg;
    }

    public CuteTextView getRightDesc2() {
        if (this.mRightDesc2View == null) {
            this.mRightDesc2View = getTextView(ViewConstant.ID_RIGHT_DESC_2);
        }
        return this.mRightDesc2View;
    }

    public CuteTextView getRightDesc3() {
        if (this.mRightDesc3View == null) {
            this.mRightDesc3View = getTextView(ViewConstant.ID_RIGHT_DESC_3);
        }
        return this.mRightDesc3View;
    }

    public CuteTextView getRightDesc4() {
        if (this.mRightDesc4View == null) {
            this.mRightDesc4View = getTextView(ViewConstant.ID_RIGHT_DESC_4);
        }
        return this.mRightDesc4View;
    }

    private int getDimenSize(int dimen) {
        return ResourceUtil.getDimensionPixelSize(dimen);
    }

    private Drawable getDrawable(int resId) {
        return ResourceUtil.getDrawable(resId);
    }

    public void setViewType(int type) {
        boolean z;
        this.mViewType = type;
        this.isAnimeCard = this.mViewType == 3;
        if (this.mViewType == 8) {
            z = true;
        } else {
            z = false;
        }
        this.isPersonCard = z;
        if (this.mViewType == 6) {
            z = true;
        } else {
            z = false;
        }
        this.isSourceCard = z;
        if (this.mViewType == 7) {
            z = true;
        } else {
            z = false;
        }
        this.isPlaylistCard = z;
        if (this.mViewType == 1) {
            z = true;
        } else {
            z = false;
        }
        this.isFilmOnlineCard = z;
        if (this.mViewType == 2) {
            z = true;
        } else {
            z = false;
        }
        this.isFilmOfflineCard = z;
        if (this.mViewType == 4) {
            z = true;
        } else {
            z = false;
        }
        this.isSeriesOnlineCard = z;
        if (this.mViewType == 5) {
            z = true;
        } else {
            z = false;
        }
        this.isSeriesOfflineCard = z;
        if (this.mViewType == 9) {
            z = true;
        } else {
            z = false;
        }
        this.isLiveCard = z;
        if (this.isFilmOfflineCard) {
            this.mCard = new FilmOfflineCard(this);
        } else if (this.isFilmOnlineCard) {
            this.mCard = new FilmOnlineCard(this);
        } else if (this.isSeriesOfflineCard) {
            this.mCard = new SeriesOfflineCard(this);
        } else if (this.isSeriesOnlineCard) {
            this.mCard = new SeriesOnlineCard(this);
        } else if (this.isAnimeCard) {
            this.mCard = new AnimeCard(this);
        } else if (this.isSourceCard) {
            this.mCard = new SourceCard(this);
        } else if (this.isPlaylistCard) {
            this.mCard = new PlaylistCard(this);
        } else if (this.isPersonCard) {
            this.mCard = new PersonCard(this);
        } else if (this.isLiveCard) {
            this.mCard = new LiveCard(this);
        }
        if (this.mCard == null) {
            LogUtils.e(LOG_TAG, "setViewType mCard == null, mViewType = ", Integer.valueOf(this.mViewType));
        } else {
            this.mCard.releaseData();
        }
    }

    private void checkContext(Context context) {
        if (context instanceof Activity) {
            throw new IllegalArgumentException("Please use application context for creating CardView !");
        }
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        getRightFocusBg();
        if (gainFocus) {
            this.mRightFocusBg.setDrawable(this.GALA_GREEN);
        } else {
            this.mRightFocusBg.setDrawable(null);
        }
    }
}
