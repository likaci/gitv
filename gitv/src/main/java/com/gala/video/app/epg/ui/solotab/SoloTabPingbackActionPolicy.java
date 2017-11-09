package com.gala.video.app.epg.ui.solotab;

import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.epg.home.controller.PingbackActionPolicy;
import com.gala.video.lib.share.pingback.SoloTabPingbackUitls;
import com.gala.video.lib.share.uikit.page.Page;

public class SoloTabPingbackActionPolicy extends PingbackActionPolicy {
    private SoloTabInfoModel mInfoModel;

    public SoloTabPingbackActionPolicy(Page page, SoloTabInfoModel infoModel) {
        super(page);
        this.mInfoModel = infoModel;
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        SoloTabPingbackUitls.getInstance().setS2(this.mInfoModel.getFrom());
        SoloTabPingbackUitls.getInstance().setE(this.mInfoModel.getE());
        SoloTabPingbackUitls.getInstance().setTabName(this.mInfoModel.getTabName());
    }

    protected String getCardShowC1Value() {
        return this.mInfoModel.getChannelId() + "";
    }

    protected String getCardShowADCountValue() {
        return "";
    }

    protected String getCardShowEValue(boolean isPageSwitch) {
        return this.mInfoModel.getE();
    }

    protected String getCardShowQTCurlValue(boolean isPageSwitch) {
        return "solo_" + this.mInfoModel.getTabName();
    }

    protected String getCardShowCountValue(boolean isPageSwitch) {
        return "";
    }
}
