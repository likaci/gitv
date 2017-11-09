package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.SearchCard;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.ArrayList;
import java.util.List;

public class SourceCard extends BaseCard {
    public SourceCard(CardView cardView) {
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
                        getTitleView().setText(title);
                        this.mCardView.setContentDescription(title);
                        getDivideLineView().setDrawable(this.mCardView.getDIVIDE_LINE_DRAWABLE());
                        String desc = (String) cardInfos.get(1);
                        boolean isDescEmpty = TextUtils.isEmpty(desc);
                        int descRealLineCount = 0;
                        CuteTextView rightDescView1 = getRightDesc1();
                        CuteImageView xinView = getXinView();
                        if (!isDescEmpty) {
                            descRealLineCount = rightDescView1.getRealLineCount(desc);
                            if (descRealLineCount != 0) {
                                if (descRealLineCount == 1) {
                                    rightDescView1.setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
                                    rightDescView1.setText(desc);
                                } else if (descRealLineCount >= 2) {
                                    rightDescView1.setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2());
                                    rightDescView1.setText(desc);
                                }
                            }
                        }
                        if ((getcard.isNew == 1) && descRealLineCount != 0) {
                            int lastLineWidth = rightDescView1.getLastLineWidth();
                            int xinMarLeft = getDimenSize(C0508R.dimen.dimen_6dp);
                            int xinMarBot = getDimenSize(C0508R.dimen.dimen_2dp);
                            if (descRealLineCount == 1) {
                                xinView.setMarginLeft((rightDescView1.getMarginLeft() + lastLineWidth) + xinMarLeft);
                                xinView.setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1() + xinMarBot);
                            } else if (descRealLineCount == 2) {
                                xinView.setMarginLeft((rightDescView1.getMarginLeft() + lastLineWidth) + xinMarLeft);
                                xinView.setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES2() + xinMarBot);
                            }
                            xinView.setDrawable(this.mCardView.getXIN_DRAWABLE());
                        }
                        List<String> descStrings = new ArrayList(2);
                        for (int i = 2; i < 4; i++) {
                            if (ListUtils.isLegal(cardInfos, i)) {
                                descStrings.add(cardInfos.get(i));
                            }
                        }
                        int viewIndex = 0;
                        if (descRealLineCount == 0) {
                            viewIndex = 0;
                        } else if (descRealLineCount == 1) {
                            viewIndex = 1;
                        } else if (descRealLineCount == 2) {
                            viewIndex = 2;
                        }
                        setDescViewText(descStrings, viewIndex);
                    }
                }
            }
        }
    }

    public void setImageData(IData info) {
        setTopCorner(info);
    }

    public void releaseData() {
        super.releaseData();
        getCornerLTView().setVisible(0);
        getCornerRTView().setVisible(0);
        getTitleView().setVisible(0);
        getXinView().setVisible(0);
        getRightDesc1().setVisible(0);
        getRightDesc2().setVisible(0);
        getRightDesc3().setVisible(0);
        getRightDesc4().setVisible(0);
        getRightDesc1().setLines(2);
        getRightDesc1().setSize(this.mCardView.getRIGHT_DESC_TEXT_VIEW_SIZE_19());
        getRightDesc1().setMarginBottom(this.mCardView.getDESC_CHANGE1_MARGIN_BOTTOM_TITLE1_LINES1());
        getCornerLB1View().setVisible(8);
        getCornerLB2View().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getLine1BgView().setVisible(8);
        getOfflineCountDown1().setVisible(8);
        getOfflineCountDown2().setVisible(8);
        getOfflineCountDown3().setVisible(8);
        getSignRBView().setVisible(8);
        getScoreView().setVisible(8);
        getCornerLBBgView().setVisible(8);
        getPopupGreenView().setVisible(8);
    }
}
