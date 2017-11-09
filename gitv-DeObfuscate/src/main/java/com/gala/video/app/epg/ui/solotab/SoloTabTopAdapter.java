package com.gala.video.app.epg.ui.solotab;

import android.content.Context;
import android.view.View;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.epg.home.data.actionbar.ActionBarItemInfo;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAdapter;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import java.util.List;

public class SoloTabTopAdapter extends ActionBarAdapter {
    private final SoloTabInfoModel mInfoModel;
    private final SoloTabManage mSoloTabManage;

    public SoloTabTopAdapter(List<ActionBarItemInfo> itemInfos, Context mContext, SoloTabInfoModel infoModel, SoloTabManage soloTabManage) {
        super(itemInfos, mContext);
        this.from = "top_solo_tab";
        this.entertype = 23;
        this.buy_from = "top";
        this.mInfoModel = infoModel;
        this.mSoloTabManage = soloTabManage;
    }

    public void onChildFocusChanged(View v, boolean hasFocus) {
        super.onChildFocusChanged(v, hasFocus);
        if (this.mSoloTabManage != null && this.mSoloTabManage.getEngine() != null && this.mSoloTabManage.getEngine().getPage() != null && this.mSoloTabManage.getEngine().getPage().getRoot() != null) {
            BlocksView root = this.mSoloTabManage.getEngine().getPage().getRoot();
            root.setNextFocusUpId(getPreFocusId());
            setNextFocusDownId(root.getId());
        }
    }

    public void onClickCheckInBtn(String rseat, int position) {
        onCheckInJump();
        sendPingback(rseat, "");
    }

    public void onClickSearchBtn(String rseat, int position) {
        SearchEnterUtils.startSearchActivity(this.mContext);
        sendPingback(rseat, "");
    }

    public void onClickMyBtn(String rseat, int position) {
        GetInterfaceTools.getLoginProvider().startUcenterActivityFromSoloTab(this.mContext);
        sendPingback(rseat, "");
    }

    public void onClickVipBtn(String rseat, int position) {
        onVipBtnJump();
        sendPingback(rseat, this.copy);
    }

    private void sendPingback(String rseat, String copy) {
        HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "solo_" + this.mInfoModel.getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("copy", copy).setOthersNull().post();
    }
}
