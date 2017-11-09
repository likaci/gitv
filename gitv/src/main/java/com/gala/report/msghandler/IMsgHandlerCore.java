package com.gala.report.msghandler;

import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;

public interface IMsgHandlerCore {
    void sendHostStatus(HOSTMODULE hostmodule, HOSTSTATUS hoststatus);

    void sendPushMessage(String str);
}
