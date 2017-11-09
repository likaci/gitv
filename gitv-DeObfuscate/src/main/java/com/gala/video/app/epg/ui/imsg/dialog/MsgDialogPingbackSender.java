package com.gala.video.app.epg.ui.imsg.dialog;

import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class MsgDialogPingbackSender {
    public static String ct = "161015_msgpush";
    public String f1934a = "";
    public String block = "";
    public String msgLevel = "";
    public String msgid = "";
    public String msgtype = "";
    public String pushposition = "";
    public String qtcurl = "";
    public String rpage = "";
    public String rseat = "";
    public String scrposition = "";

    public void block() {
        this.scrposition = "";
        this.pushposition = "";
        this.f1934a = "conflict";
        send();
    }

    public void click(int code) {
        this.f1934a = "20";
        this.rpage = "msgpush";
        this.block = "msgpush";
        if (code == 66 || code == 23) {
            this.rseat = ScreenSaverPingBack.SEAT_KEY_OK;
        } else if (code == 4) {
            this.rseat = "back";
        } else if (code == 19) {
            this.rseat = ScreenSaverPingBack.SEAT_KEY_UP;
        } else if (code == 20) {
            this.rseat = ScreenSaverPingBack.SEAT_KEY_DOWN;
        } else if (code == 21) {
            this.rseat = ScreenSaverPingBack.SEAT_KEY_LEFT;
        } else if (code == 22) {
            this.rseat = ScreenSaverPingBack.SEAT_KEY_RIGHT;
        }
        send();
    }

    public void show() {
        this.f1934a = "21";
        this.qtcurl = "msgpush";
        this.block = "msgpush";
        send();
    }

    private void send() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "11").add("a", this.f1934a).add("qtcurl", this.qtcurl).add("rpage", this.rpage).add("block", this.block).add("rseat", this.rseat).add("msgtype", this.msgtype).add(Keys.MSG_LEVEL, this.msgLevel).add(Keys.MSG_ID, this.msgid).add(Keys.PUSH_POSITION, this.pushposition).add(Keys.SCR_POSITION, this.scrposition).add("ct", ct);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public MsgDialogPingbackSender(boolean isOutApp, String pos, IMsgContent... contents) {
        if (contents != null) {
            if (contents.length == 1) {
                IMsgContent content = contents[0];
                switch (content.msg_type) {
                    case 1:
                        this.msgtype = "promotion";
                        break;
                    case 2:
                        this.msgtype = "rec";
                        break;
                    case 3:
                        this.msgtype = "order";
                        break;
                }
                this.msgLevel = String.valueOf(content.msg_level);
            }
            for (int i = 0; i < contents.length; i++) {
                this.msgid += contents[i].msg_id;
                if (i < contents.length - 1) {
                    this.msgid += ",";
                }
            }
        }
        switch (IMsgUtils.getDialogPos(isOutApp)) {
            case 1:
                this.scrposition = "bottomleft";
                break;
            case 2:
                this.scrposition = "bottom";
                break;
            case 3:
                this.scrposition = "bottomright";
                break;
            case 4:
                this.scrposition = "topright";
                break;
        }
        this.pushposition = isOutApp ? "outside" : "inside";
    }
}
