package com.gala.video.app.epg.ui.imsg.adapter;

import android.content.Context;
import com.gala.video.app.epg.home.data.actionbar.ActionBarItemInfo;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAdapter;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarPageType;
import com.gala.video.app.epg.ui.imsg.utils.MsgPingbackSender;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.List;

public class MsgTopAdapter extends ActionBarAdapter {
    public MsgTopAdapter(List<ActionBarItemInfo> itemInfos, Context context) {
        super(itemInfos, context);
        this.mActionBarPageType = ActionBarPageType.MSG_CENTER_PAGE;
        this.from = "msg";
        this.buy_from = "list_top";
        this.entertype = 12;
        this.mContext = context;
    }

    public void onClickSearchBtn(String rseat, int position) {
        SearchEnterUtils.startSearchActivity(this.mContext, 0, "", 67108864);
        MsgPingbackSender.sendMsgTopClickPingback(rseat, "");
    }

    public void onClickMyBtn(String rseat, int position) {
        GetInterfaceTools.getLoginProvider().startLoginForAlbum(this.mContext, 67108864);
        MsgPingbackSender.sendMsgTopClickPingback(rseat, "");
    }

    public void onClickVipBtn(String rseat, int position) {
        onVipBtnJump();
        MsgPingbackSender.sendMsgTopClickPingback(rseat, this.copy);
    }

    public void updateCheckInView() {
        setCheckInMessageVisible();
    }
}
