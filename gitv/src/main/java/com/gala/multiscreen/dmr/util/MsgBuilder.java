package com.gala.multiscreen.dmr.util;

import com.alibaba.fastjson.JSON;
import com.gala.multiscreen.dmr.model.MSMessage;
import com.gala.multiscreen.dmr.model.msg.BaseResault;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.ResultPosition;
import com.gala.multiscreen.dmr.model.msg.ReturnMessage;
import com.gala.multiscreen.dmr.model.type.MSType;

public class MsgBuilder {
    public static String buildResultMsg(Boolean isSuccess) {
        ReturnMessage msg = new ReturnMessage();
        BaseResault res = new BaseResault();
        res.result = String.valueOf(isSuccess);
        res.session = MSMessage.VALUE_SESSION;
        res.key = MSMessage.VALUE_KEY;
        msg.value = res;
        return JSON.toJSONString(msg);
    }

    public static String buildResultMsg(long pos) {
        ReturnMessage msg = new ReturnMessage();
        ResultPosition res = new ResultPosition();
        res.time_stamp = String.valueOf(pos);
        res.key = MSMessage.VALUE_KEY;
        res.session = MSMessage.VALUE_SESSION;
        msg.value = res;
        return JSON.toJSONString(msg);
    }

    public static String buildResultMsg(Notify notify) {
        ReturnMessage msg = new ReturnMessage();
        msg.value = notify;
        msg.type = MSType.SYNC.getType();
        msg.control = "play";
        return JSON.toJSONString(msg);
    }
}
