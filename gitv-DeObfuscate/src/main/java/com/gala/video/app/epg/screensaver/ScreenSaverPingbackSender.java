package com.gala.video.app.epg.screensaver;

import android.view.KeyEvent;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

class ScreenSaverPingbackSender {
    private static final String TAG = "ScreenSaverPingbackSender";

    ScreenSaverPingbackSender() {
    }

    public void sendPingback(KeyEvent event, ScreenSaverWindow window, ScreenSaverAdModel adModel) {
        String r = "";
        String rseat = getRSeatByKeyEvent(event);
        String c1 = "";
        String block = getBlock(window, adModel);
        String e = "";
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "20").add("rseat", rseat).add("rpage", "screensaver").add("block", block).add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private String getRSeatByKeyEvent(KeyEvent event) {
        String rseat = "";
        switch (event.getKeyCode()) {
            case 3:
                return "home";
            case 4:
                return "back";
            case 19:
                return ScreenSaverPingBack.SEAT_KEY_UP;
            case 20:
                return ScreenSaverPingBack.SEAT_KEY_DOWN;
            case 21:
                return ScreenSaverPingBack.SEAT_KEY_LEFT;
            case 22:
                return ScreenSaverPingBack.SEAT_KEY_RIGHT;
            case 23:
                return ScreenSaverPingBack.SEAT_KEY_OK;
            case 24:
                return ScreenSaverPingBack.SEAT_KEY_VOLUP;
            case 25:
                return ScreenSaverPingBack.SEAT_KEY_VOLDOWN;
            case 82:
                return "menu";
            default:
                return "unknown";
        }
    }

    private String getBlock(ScreenSaverWindow window, ScreenSaverAdModel adModel) {
        String block = "";
        if (window.isShowingAd() && adModel != null) {
            switch (adModel.getAdClickType()) {
                case IMAGE:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_IMAGE;
                case DEFAULT:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_DEFAULT;
                case H5:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_H5;
                case PLAY_LIST:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_PLID;
                case VIDEO:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_VIDEO;
                case CAROUSEL:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_CAROUSEL;
                case NONE:
                    return ScreenSaverPingBack.BLOCK_SCREENSAVER_AD_NONE;
                default:
                    return block;
            }
        } else if (window.isShowingImage()) {
            return "screensaver";
        } else {
            return block;
        }
    }
}
