package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.home.component.item.corner.LiveCornerUtils;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import java.util.List;

public class LiveCard extends BaseCard {
    private static final String TAG = "LiveCard";
    private final LiveCornerListener mLiveCornerListener = new C08811();
    private SearchCard mSearchCard;

    class C08811 implements LiveCornerListener {
        C08811() {
        }

        public void showBefore() {
            LiveCard.this.setBeforeLiveInfo();
        }

        public void showPlaying() {
            LiveCard.this.setLivingInfo();
        }

        public void showEnd() {
            LiveCard.this.setEndLiveInfo();
        }
    }

    public LiveCard(CardView cardView) {
        super(cardView);
    }

    public void setTextData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                SearchCard getcard = album.getcard();
                this.mSearchCard = getcard;
                if (getcard != null) {
                    List cardInfo = getcard.getCardInfo();
                    if (ListUtils.isLegal(cardInfo, 0)) {
                        String title = (String) cardInfo.get(0);
                        this.mCardView.setContentDescription(title);
                        getTitleView().setText(title);
                        getDivideLineView().setDrawable(this.mCardView.getDIVIDE_LINE_DRAWABLE());
                        String desc1 = null;
                        if (ListUtils.isLegal(cardInfo, 1)) {
                            desc1 = (String) cardInfo.get(1);
                        }
                        if (!TextUtils.isEmpty(desc1)) {
                            getRightDesc1().setText(desc1);
                        }
                    }
                }
            }
        }
    }

    public void setImageData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                setTopCorner(info);
                if (!TextUtils.isEmpty(album.tv_livecollection)) {
                    getSignRBView().setDrawable(this.mCardView.getSIGN_ZHUAN_TI_DRAWABLE());
                }
            }
        }
    }

    protected void setRightTopCorner(IData info) {
        if (info != null) {
            Object data = info.getData();
            if (data instanceof ChannelLabel) {
                if (this.mCardView.getLiveCornerFactory() == null) {
                    this.mCardView.setLiveCornerFactory(CreateInterfaceTools.createLiveCornerFactory());
                }
                this.mCardView.getLiveCornerFactory().start((ChannelLabel) data, this.mLiveCornerListener);
            }
        }
    }

    protected void setBeforeLiveInfo() {
        if (this.mSearchCard == null) {
            LogUtils.m1571e(TAG, "setBeforeLiveInfo mSearchCard == null");
            return;
        }
        getCornerRTView().setDrawable(ImageCacheUtil.CORNER_NOTICE_DRAWABLE);
        CuteTextView cuteTextView = getRightDesc1();
        if (TextUtils.isEmpty(cuteTextView.getText())) {
            cuteTextView.setText(LiveCornerUtils.getLiveBoforeDate(this.mSearchCard.sliveTime));
        } else {
            getRightDesc2().setText(LiveCornerUtils.getLiveBoforeDate(this.mSearchCard.sliveTime));
        }
    }

    protected void setLivingInfo() {
        if (this.mSearchCard == null) {
            LogUtils.m1571e(TAG, "setLivingInfo mSearchCard == null");
            return;
        }
        getCornerRTView().setDrawable(ImageCacheUtil.CORNER_LIVING_DRAWABLE);
        CuteTextView cuteTextView = getRightDesc1();
        if (TextUtils.isEmpty(cuteTextView.getText())) {
            cuteTextView.setText(LiveCornerUtils.getLivingCount(this.mSearchCard.viewerShip));
        } else {
            getRightDesc2().setText(LiveCornerUtils.getLivingCount(this.mSearchCard.viewerShip));
        }
    }

    protected void setEndLiveInfo() {
        if (this.mSearchCard == null) {
            LogUtils.m1571e(TAG, "setEndLiveInfo mSearchCard == null");
            return;
        }
        getCornerRTView().setDrawable(ImageCacheUtil.CORNER_END_LIVING_DRAWABLE);
        CuteTextView cuteTextView = getRightDesc1();
        if (TextUtils.isEmpty(cuteTextView.getText())) {
            cuteTextView.setText(LiveCornerUtils.getLiveEndCount(this.mSearchCard.viewerShip));
        } else {
            getRightDesc2().setText(LiveCornerUtils.getLiveEndCount(this.mSearchCard.viewerShip));
        }
    }

    public void releaseData() {
        super.releaseData();
        getTitleView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getCornerLTView().setVisible(0);
        getCornerRTView().setVisible(0);
        getSignRBView().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getLine1BgView().setVisible(8);
        getOfflineCountDown1().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
        getCornerLB1View().setVisible(8);
        getCornerLB2View().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getRightDesc3().setVisible(8);
        getRightDesc4().setVisible(8);
        getScoreView().setVisible(8);
        getXinView().setVisible(8);
        getPopupGreenView().setVisible(8);
    }
}
