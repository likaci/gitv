package com.gala.multiscreen.dmr.model.msg;

import com.gala.multiscreen.dmr.model.MSMessage;

public class ReturnMessage {
    public String control;
    public String type;
    public DlnaMessage value;
    public String version;

    public ReturnMessage() {
        this.type = "result";
        this.control = "unknow";
        this.version = "";
        this.version = MSMessage.VALUE_TV_VERSION;
    }
}
