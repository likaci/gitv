package com.gala.video.lib.share.ifmanager.bussnessIF.imsg;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import java.util.List;

public interface IMsgCenter extends IInterfaceWrapper {

    public static abstract class Wrapper implements IMsgCenter {
        public Object getInterface() {
            return this;
        }

        public static IMsgCenter asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IMsgCenter)) {
                return null;
            }
            return (IMsgCenter) wrapper;
        }
    }

    void deleteOldIMsg();

    List<IMsgContent> getIMsgList();

    List<IMsgContent> getIMsgListForType(int i);

    IMsgDialogCacheManager getMsgDialogCache();

    List<IMsgContent> getNeedShowList();

    int getUnreadIMsgListCount();

    void init();

    void insertIMsg(IMsgContent iMsgContent);

    boolean isMsgExist(IMsgContent iMsgContent);

    void pushMsgToApp(Context context, IMsgContent iMsgContent);

    void setDiaLogFlag(boolean z);

    void updateIsReadFlag(int i);

    void updateIsReadFlag(IMsgContent iMsgContent);

    void updateIsShowFlag(int i, IMsgContent... iMsgContentArr);
}
