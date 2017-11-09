package com.gala.video.app.epg.pingback;

import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IEpgPingback.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class EpgPingback extends Wrapper {
    public void onLoadUser(boolean v) {
        if (v) {
            HomePingbackFactory.instance().createPingback(CommonPingback.PLAY_HISTORY_DOWNLOAD_PINGBACK).addItem(Keys.LDTYPE, "his_download").addItem(Keys.f2035T, "11").addItem("ct", "160602_load").addItem("st", "1").setOthersNull().post();
        } else {
            HomePingbackFactory.instance().createPingback(CommonPingback.PLAY_HISTORY_DOWNLOAD_PINGBACK).addItem(Keys.LDTYPE, "his_download").addItem(Keys.f2035T, "11").addItem("ct", "160602_load").addItem("st", "0").setOthersNull().post();
        }
    }

    public void onSaveTvHistory(boolean v) {
        if (v) {
            HomePingbackFactory.instance().createPingback(CommonPingback.PLAY_HISTORY_UPLOAD_PINGBACK).addItem(Keys.LDTYPE, "his_upload").addItem(Keys.f2035T, "11").addItem("ct", "160602_load").addItem("st", "1").setOthersNull().post();
        } else {
            HomePingbackFactory.instance().createPingback(CommonPingback.PLAY_HISTORY_UPLOAD_PINGBACK).addItem(Keys.LDTYPE, "his_upload").addItem(Keys.f2035T, "11").addItem("ct", "160602_load").addItem("st", "0").setOthersNull().post();
        }
    }
}
