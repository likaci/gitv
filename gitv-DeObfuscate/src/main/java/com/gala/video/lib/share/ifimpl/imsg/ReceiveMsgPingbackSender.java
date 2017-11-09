package com.gala.video.lib.share.ifimpl.imsg;

import android.util.Log;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

class ReceiveMsgPingbackSender {
    public String content = "";
    public String isShow = "0";
    public String jumpType = "";
    public String f2024r = "";
    public String rLink = "";

    ReceiveMsgPingbackSender() {
    }

    void setContent(IMsgContent con) {
        if (con == null) {
            Log.e("MsgPingbackSender", "setContent --- IMsgContent is null");
            return;
        }
        String str = con.is_detailpage == 0 ? "1" : con.is_detailpage == 1 ? "2" : "";
        this.rLink = str;
        switch (con.page_jumping) {
            case 1:
                this.jumpType = "H5页";
                break;
            case 2:
                this.jumpType = "专题页";
                break;
            case 3:
                this.jumpType = "详情页";
                break;
            case 4:
                this.jumpType = "播放页";
                break;
            case 5:
                this.jumpType = "其他";
                break;
            default:
                this.jumpType = "";
                break;
        }
        this.f2024r = String.valueOf(con.msg_id);
    }

    void sendPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11").add("ct", "160810_rcvmsg").add(Keys.ISSHOW, this.isShow).add("rlink", this.rLink).add(Keys.JUMP_TYPE, this.jumpType).add("r", this.f2024r).add("content", this.content);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
