package com.gala.video.app.player.pingback.Sender;

import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.data.IVideo;

public class TipPingbackSender implements ITipPingbackParams {
    public void sendAdPingback(IPingbackContext context) {
        if (context != null) {
            PingbackFactory.instance().createPingback(48).addItem(BTSPTYPE.BSTP_1).addItem(QTCURL.QTCURL_TYPE("ad_chgra_tip")).addItem(context.getItem("e")).addItem(BLOCK.BLOCK_TYPE("ad_chgra_tip")).post();
        }
    }

    public void sendLoginTipShow(IPingbackContext context, IVideo video) {
        PingbackFactory.instance().createPingback(54).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(String.valueOf(video.getChannelId()))).addItem(QTCURL.QTCURL_TYPE("player")).addItem(BLOCK.BLOCK_TYPE("ralogtips")).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(video.getChannelId()))).post();
    }

    public void sendLoginTipClick(IPingbackContext context, IVideo video) {
        PingbackFactory.instance().createPingback(55).addItem(BLOCK.BLOCK_TYPE("ralogtips")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE("ralogtips")).addItem(RPAGE.RPAGE_ID("player")).addItem(C1.C1_TYPE(String.valueOf(video.getChannelId()))).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(video.getChannelId()))).addItem(NOW_QPID.NOW_QPID_TYPE(video.getAlbum().qpId)).post();
    }
}
