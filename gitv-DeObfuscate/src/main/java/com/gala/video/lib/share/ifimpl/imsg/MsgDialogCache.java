package com.gala.video.lib.share.ifimpl.imsg;

import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.ArrayList;
import java.util.List;

class MsgDialogCache {
    public List<IMsgContent> appList = new ArrayList();
    private boolean isInApp = false;
    public List<IMsgContent> remindList = new ArrayList();
    public List<IMsgContent> sysList = new ArrayList();

    MsgDialogCache(boolean isInApp) {
        this.isInApp = isInApp;
    }

    void classifyMsg(IMsgContent content) {
        if (content != null) {
            if (content.msg_type == 3 && content.msg_level == 5) {
                this.remindList.add(content);
            } else if (content.msg_level == 1 || content.msg_level == 6) {
                this.sysList.add(content);
            } else if (content.msg_level == 2 && this.isInApp) {
                this.appList.add(content);
            }
        }
    }

    void clear() {
        this.remindList.clear();
        this.sysList.clear();
        this.appList.clear();
    }

    int getCount() {
        return (this.remindList.size() + this.sysList.size()) + this.appList.size();
    }

    IMsgContent[] getAllMsg() {
        List<IMsgContent> list = new ArrayList();
        if (this.remindList != null && this.remindList.size() > 0) {
            list.addAll(this.remindList);
        }
        if (this.sysList != null && this.sysList.size() > 0) {
            list.addAll(this.sysList);
        }
        if (this.appList != null && this.appList.size() > 0) {
            list.addAll(this.appList);
        }
        if (list == null || list.size() <= 0) {
            return null;
        }
        return (IMsgContent[]) list.toArray(new IMsgContent[list.size()]);
    }
}
