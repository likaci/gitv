package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.ArrayList;
import java.util.List;

public class FilmOnlineCard extends BaseCard {
    public FilmOnlineCard(CardView cardView) {
        super(cardView);
    }

    public void setTextData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                SearchCard getcard = album.getcard();
                if (getcard != null) {
                    List cardInfos = getcard.getCardInfo();
                    if (ListUtils.isLegal(cardInfos, 0)) {
                        String title = (String) cardInfos.get(0);
                        if (isSingleSeriesAndUnit(album)) {
                            title = GetInterfaceTools.getCornerProvider().getSubTitle(album);
                            String pianchang = (String) cardInfos.get(2);
                            String leixing = (String) cardInfos.get(5);
                            List<String> descStrings = new ArrayList(2);
                            descStrings.add(pianchang);
                            descStrings.add(leixing);
                            setDescViewText(descStrings, 0);
                        } else {
                            setDescViewText(getDescStrings(cardInfos), 0);
                        }
                        getTitleView().setText(title);
                        this.mCardView.setContentDescription(title);
                        getDivideLineView().setDrawable(this.mCardView.getDIVIDE_LINE_DRAWABLE());
                    }
                }
            }
        }
    }

    private boolean isSingleSeriesAndUnit(Album album) {
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        return albumType == AlbumKind.SIGLE_SERIES || albumType == AlbumKind.SIGLE_UNIT;
    }

    public void setImageData(IData info) {
        if (info != null) {
            Album album = info.getAlbum();
            setTopCorner(info);
            if (!isSingleSeriesAndUnit(album)) {
                setBottomCorner(info);
                setScore(album);
            }
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
        getRightDesc4().setVisible(0);
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
    }
}
