package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.ArrayList;
import java.util.List;

public class PersonCard extends BaseCard {
    public PersonCard(CardView cardView) {
        super(cardView);
    }

    protected List<String> getDescStrings(List<String> cardInfos) {
        int viewCount = ListUtils.getCount(this.mCardView.getDescViews());
        int infoCount = ListUtils.getCount((List) cardInfos);
        List<String> descStrings = new ArrayList(viewCount);
        descStrings.add("推荐影片");
        int i = 1;
        while (i < infoCount && i < viewCount) {
            descStrings.add(cardInfos.get(i));
            i++;
        }
        return descStrings;
    }

    public void setImageData(IData info) {
        getSignRBView().setDrawable(this.mCardView.getSIGN_MING_XI_DRAWABLE());
    }

    public void releaseData() {
        super.releaseData();
        getSignRBView().setVisible(0);
        getTitleView().setVisible(0);
        getXinView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getRightDesc3().setVisible(0);
        getRightDesc4().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_18());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getCornerLTView().setVisible(8);
        getCornerRTView().setVisible(8);
        getCornerLB1View().setVisible(8);
        getCornerLB2View().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getLine1BgView().setVisible(8);
        getOfflineCountDown1().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
        getScoreView().setVisible(8);
        getPopupGreenView().setVisible(8);
    }
}
