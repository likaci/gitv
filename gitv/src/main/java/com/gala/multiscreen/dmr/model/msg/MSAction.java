package com.gala.multiscreen.dmr.model.msg;

import com.gala.multiscreen.dmr.model.type.Action;

public class MSAction extends DlnaMessage {
    public String action = "";
    public String id = "";

    public Action getAction() {
        return Action.findAction(this.action);
    }
}
