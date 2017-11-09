package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.List;

public class PlaylistCard extends BaseCard {
    public PlaylistCard(CardView cardView) {
        super(cardView);
    }

    public void setTextData(IData info) {
        if (info != null) {
            super.setTextData(info);
            Album album = info.getAlbum();
            if (album != null) {
                SearchCard getcard = album.getcard();
                if (getcard != null) {
                    List cardInfo = getcard.getCardInfo();
                    if (ListUtils.isLegal(cardInfo, 0)) {
                        String title = (String) cardInfo.get(0);
                        if (!TextUtils.isEmpty(title)) {
                            int count = getcard.tvsets;
                            if (count > 0) {
                                String countText;
                                CuteTextView titleView = getTitleView();
                                CuteTextView popupGreenView = getPopupGreenView();
                                int TITLE_MARGIN_BOTTOM_LINE_1 = this.mCardView.getTITLE_MARGIN_BOTTOM_LINE_1();
                                int TITLE_MARGIN_BOTTOM_LINE_2 = this.mCardView.getTITLE_MARGIN_BOTTOM_LINE_2();
                                if (count <= 9999) {
                                    countText = "共" + count + "个视频";
                                } else {
                                    countText = "共999+个视频";
                                }
                                if (count > 9999) {
                                    count = 9999;
                                }
                                int countLength = String.valueOf(count).length();
                                title = titleView.subText(title, getDimenSize(C0508R.dimen.dimen_397dp) + ((4 - countLength) * getDimenSize(C0508R.dimen.dimen_10dp)));
                                int popupNeedWidth = getDimenSize(C0508R.dimen.dimen_87dp) + ((countLength - 1) * getDimenSize(C0508R.dimen.dimen_10dp));
                                int titleRealLineCount = titleView.getRealLineCount(title);
                                int titleLastLineWidth = titleView.getLastLineWidth(title);
                                int popupMarLeft = getDimenSize(C0508R.dimen.dimen_8dp);
                                if (titleRealLineCount == 1) {
                                    if (titleLastLineWidth + popupNeedWidth < getDimenSize(C0508R.dimen.dimen_263dp)) {
                                        titleView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_2);
                                        popupGreenView.setMarginLeft((titleView.getMarginLeft() + titleLastLineWidth) + popupMarLeft);
                                        popupGreenView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_1);
                                    } else {
                                        titleView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_1);
                                        popupGreenView.setMarginLeft(titleView.getMarginLeft() + getDimenSize(C0508R.dimen.dimen_10dp));
                                        popupGreenView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_2 - 1);
                                    }
                                } else if (titleRealLineCount == 2) {
                                    titleView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_2);
                                    popupGreenView.setMarginLeft((titleView.getMarginLeft() + titleLastLineWidth) + popupMarLeft);
                                    popupGreenView.setMarginBottom(TITLE_MARGIN_BOTTOM_LINE_2 - 1);
                                }
                                popupGreenView.setText(countText);
                                popupGreenView.setBgDrawable(EpgImageCache.CARD_POPUP_GREEN_BG);
                            }
                        }
                        this.mCardView.setContentDescription(title);
                        getTitleView().setText(title);
                        getDivideLineView().setDrawable(this.mCardView.getDIVIDE_LINE_DRAWABLE());
                    }
                }
            }
        }
    }

    public void setImageData(IData info) {
        getSignRBView().setDrawable(this.mCardView.getSIGN_ZHUAN_TI_DRAWABLE());
    }

    public void releaseData() {
        super.releaseData();
        getTitleView().setVisible(0);
        getSignRBView().setVisible(0);
        getPopupGreenView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getRightDesc3().setVisible(0);
        getRightDesc1().setLines(1);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getRightDesc4().setVisible(8);
        getOfflineCountDown1().setVisible(8);
        getCornerLB1View().setVisible(8);
        getCornerLB2View().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getLine1BgView().setVisible(8);
        getCornerLTView().setVisible(8);
        getScoreView().setVisible(8);
        getCornerRTView().setVisible(8);
        getXinView().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
    }
}
