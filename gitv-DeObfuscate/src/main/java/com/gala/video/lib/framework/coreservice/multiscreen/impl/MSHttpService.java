package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import android.content.Context;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import java.util.Map;

class MSHttpService {
    private IGalaMSExpand mCallback = null;
    private Context mContext;

    public MSHttpService(Context context, IGalaMSExpand callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public String handleRequest(String uri, Map<String, String> params) {
        if (uri == null || "".equals(uri)) {
            return "failed";
        }
        uri = uri.replace("/", "");
        if (MultiScreenParams.DLNA_PHONE_CONTROLL.equals(uri)) {
            return handleControl(params);
        }
        if ("push".equals(uri)) {
            return handlePush(params);
        }
        if ("heart".equals(uri)) {
            return handleHeartbeat();
        }
        return "failed";
    }

    private String handleControl(Map<String, String> params) {
        String type = (String) params.get("type");
        KeyKind kind = null;
        if (ScreenSaverPingBack.SEAT_KEY_UP.equals(type)) {
            kind = KeyKind.UP;
        } else if (ScreenSaverPingBack.SEAT_KEY_DOWN.equals(type)) {
            kind = KeyKind.DOWN;
        } else if (ScreenSaverPingBack.SEAT_KEY_LEFT.equals(type)) {
            kind = KeyKind.LEFT;
        } else if (ScreenSaverPingBack.SEAT_KEY_RIGHT.equals(type)) {
            kind = KeyKind.RIGHT;
        } else if ("back".equals(type)) {
            kind = KeyKind.BACK;
        } else if ("menu".equals(type)) {
            kind = KeyKind.MENU;
        } else if ("home".equals(type)) {
            kind = KeyKind.HOME;
        } else if ("click".equals(type)) {
            kind = KeyKind.CLICK;
        }
        if (kind == null) {
            return "failed";
        }
        new MsSendKeyUtils().sendSysKey(this.mContext, kind);
        return ScreenSaverPingBack.SEAT_KEY_OK;
    }

    private String handlePush(Map<String, String> params) {
        PushVideo pushVideo = new PushVideo();
        pushVideo.tvid = (String) params.get("tvId");
        pushVideo.aid = (String) params.get("albumId");
        int history = -1;
        try {
            history = Integer.parseInt((String) params.get("history"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pushVideo.history = String.valueOf(history);
        if (StringUtils.isEmpty(pushVideo.tvid) || StringUtils.isEmpty(pushVideo.aid)) {
            return "failed";
        }
        LogUtils.m1574i("", "http server handlePush");
        this.mCallback.onPushVideoEvent(pushVideo);
        return ScreenSaverPingBack.SEAT_KEY_OK;
    }

    private String handleHeartbeat() {
        return ScreenSaverPingBack.SEAT_KEY_OK;
    }
}
