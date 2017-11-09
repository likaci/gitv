package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class AnimeCard extends BaseCard {
    public AnimeCard(CardView cardView) {
        super(cardView);
    }

    public void setImageData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            setTopCorner(info);
            setBottomCorner(info);
            setScore(album);
        }
    }

    public void releaseData() {
        super.releaseData();
        getCornerLB1View().setVisible(0);
        getCornerLB2View().setVisible(0);
        getCornerLBBgView().setVisible(0);
        getCornerLTView().setVisible(0);
        getCornerRTView().setVisible(0);
        getLine1BgView().setVisible(0);
        getTitleView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getRightDesc3().setVisible(0);
        getScoreView().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getOfflineCountDown1().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
        getSignRBView().setVisible(8);
        getXinView().setVisible(8);
        getPopupGreenView().setVisible(8);
        getRightDesc4().setVisible(8);
    }
}
