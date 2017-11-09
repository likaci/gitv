package com.gala.video.app.epg.home.component.item.corner;

import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.component.item.widget.ComplexItemCloudView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class TitleInItemCorner extends ItemCorner {
    public TitleInItemCorner(ComplexItemCloudView view) {
        super(view);
    }

    void adjustCornerDolbyAnd3DPlace() {
        this.mCornerLB1Image.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
        this.mCornerLB2Image.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
    }

    void adjustCornerDolbyPlace() {
        this.mCornerLB1Image.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
    }

    void adjustCorner3DPlace() {
        this.mCornerLB1Image.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
    }

    void adjustCornerScorePlace() {
        this.mCornerScoreText.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
    }

    void adjustCornerLBBgPlace() {
        this.mCornerLBBg.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
    }

    void adjustCornerRBDesL1Place() {
        this.mCornerRBDesL1.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
    }

    void adjustCornerRankViewPlace() {
        this.mCornerRankImage.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
    }

    void adjustCornerToBeOnlinePlace() {
        this.mCornerToBeOnlineText.setMarginBottom(ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
    }
}
