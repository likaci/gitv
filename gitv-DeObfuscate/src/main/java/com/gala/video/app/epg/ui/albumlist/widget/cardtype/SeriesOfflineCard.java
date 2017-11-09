package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class SeriesOfflineCard extends BaseCard {
    public SeriesOfflineCard(CardView cardView) {
        super(cardView);
    }

    public void setImageData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                SearchCard searchCard = album.getcard();
                if (searchCard != null) {
                    setCountDownDay(searchCard.getDaysLater());
                    getSignRBView().setDrawable(this.mCardView.getSIGN_YU_GAO_DRAWABLE());
                }
            }
        }
    }

    public void releaseData() {
        super.releaseData();
        getOfflineCountDown1().setVisible(0);
        getOfflineCountDown2().setVisible(0);
        getOfflineCountDown3().setVisible(0);
        getTitleView().setVisible(0);
        getLine1BgView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getSignRBView().setVisible(0);
        getCornerLTView().setVisible(0);
        getCornerRTView().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
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
