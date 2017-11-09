package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class SeriesOnlineCard extends BaseCard {
    public SeriesOnlineCard(CardView cardView) {
        super(cardView);
    }

    public void setImageData(IData info) {
        setTopCorner(info);
    }

    public void releaseData() {
        super.releaseData();
        getCornerLTView().setVisible(0);
        getCornerRTView().setVisible(0);
        getTitleView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getRightDesc3().setVisible(0);
        getRightDesc4().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getOfflineCountDown1().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
        getCornerLB1View().setVisible(8);
        getCornerLB2View().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getLine1BgView().setVisible(8);
        getScoreView().setVisible(8);
        getSignRBView().setVisible(8);
        getXinView().setVisible(8);
        getPopupGreenView().setVisible(8);
    }
}
