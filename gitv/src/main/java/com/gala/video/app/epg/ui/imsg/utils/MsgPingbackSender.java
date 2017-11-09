package com.gala.video.app.epg.ui.imsg.utils;

import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.IMsgType;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class MsgPingbackSender {
    public static void sendMsgClickPingback(IMsgContent msgContent, int labelPos, int msgPos) {
        if (msgContent != null) {
            String rt = "i";
            String rpage = "消息中心";
            String block = IMsgType.getTypeName(labelPos);
            String rseat = String.valueOf(msgPos + 1);
            String r = String.valueOf(msgContent.msg_id);
            String rlink = msgContent.isHasDetail() ? "2" : "1";
            String jumptype = getJumpType(msgContent);
            String isread = msgContent.isRead ? "1" : "0";
            PingBackParams params = new PingBackParams();
            params.add(Keys.T, "20").add("rt", "i").add("r", r).add("block", block).add("rt", rt).add("rseat", rseat).add("rpage", rpage).add("rlink", rlink).add(Keys.JUMP_TYPE, jumptype).add(Keys.ISREAD, isread);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    private static String getJumpType(IMsgContent msgContent) {
        if (msgContent == null) {
            return "";
        }
        String jumpType = "";
        switch (msgContent.page_jumping) {
            case 1:
                return "H5页";
            case 2:
                return "专题页";
            case 3:
                return "详情页";
            case 4:
                return "播放页";
            case 5:
                return "其他";
            default:
                return "";
        }
    }

    public static void sendMsgsShowPingback(int labelPos, long td) {
        String qtcurl = "消息中心";
        String block = IMsgType.getTypeName(labelPos);
        String tabsrc = PingBackUtils.getTabSrc();
        LogUtils.i("pageshow", "sendMsgsShowPingback --- qtcurl = " + qtcurl + " block = " + block);
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("bstp", "1").add("s2", "msg").add("qtcurl", qtcurl).add("block", block).add("td", String.valueOf(td)).add("tabsrc", tabsrc);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void sendMsgTopClickPingback(String rseat, String copy) {
        String r = rseat;
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("rt", "i").add("r", r).add("block", "top").add("rt", "i").add("rseat", rseat).add("rpage", "消息中心").add("copy", copy);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
