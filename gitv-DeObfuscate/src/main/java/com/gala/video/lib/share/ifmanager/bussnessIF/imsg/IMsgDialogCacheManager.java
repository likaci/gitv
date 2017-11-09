package com.gala.video.lib.share.ifmanager.bussnessIF.imsg;

public interface IMsgDialogCacheManager {
    IMsgContent[] getAllContent(boolean z);

    int getCount(boolean z);

    MsgDialogParams getDialogParams(boolean z);

    void onInAppMsg(IMsgContent iMsgContent);

    void onOutAppMsg(IMsgContent iMsgContent);

    void setHomeActivatyFlag(boolean z);
}
