package com.gala.video.lib.share.ifimpl.imsg;

import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgCenter;

public class MsgCenterCreator {
    public static IMsgCenter create() {
        return new MsgCenter();
    }
}
