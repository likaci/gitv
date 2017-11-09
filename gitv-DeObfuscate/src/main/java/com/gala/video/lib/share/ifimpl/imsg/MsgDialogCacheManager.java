package com.gala.video.lib.share.ifimpl.imsg;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgDialogCacheManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.MsgDialogParams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MsgDialogCacheManager implements IMsgDialogCacheManager {
    private static final String TAG = "imsg/MsgDialogCacheManager";
    private MsgDialogCache inAppCache = new MsgDialogCache(true);
    private MsgDialogCache outAppCache = new MsgDialogCache(false);

    MsgDialogCacheManager() {
    }

    public int getCount(boolean isOutApp) {
        if (isOutApp) {
            return this.outAppCache.getCount();
        }
        return this.inAppCache.getCount();
    }

    public IMsgContent[] getAllContent(boolean isOutApp) {
        if (isOutApp) {
            return this.outAppCache.getAllMsg();
        }
        return this.inAppCache.getAllMsg();
    }

    public void onInAppMsg(IMsgContent content) {
        this.inAppCache.classifyMsg(content);
    }

    public void onOutAppMsg(IMsgContent content) {
        this.outAppCache.classifyMsg(content);
    }

    public void setHomeActivatyFlag(boolean isHomeActivity) {
        if (isHomeActivity) {
            GetInterfaceTools.getMsgCenter().deleteOldIMsg();
            readDB();
        }
    }

    private synchronized void readDB() {
        if (this.inAppCache.getCount() == 0) {
            List<IMsgContent> needShow = GetInterfaceTools.getMsgCenter().getNeedShowList();
            if (needShow != null && needShow.size() > 0) {
                for (IMsgContent content : needShow) {
                    this.inAppCache.classifyMsg(content);
                }
            }
        }
    }

    public MsgDialogParams getDialogParams(boolean isOutApp) {
        MsgDialogCache cache;
        if (isOutApp) {
            cache = this.outAppCache;
        } else {
            cache = this.inAppCache;
        }
        if (cache.remindList != null && cache.remindList.size() > 0) {
            return readContents(true, cache.remindList, isOutApp);
        }
        if ((cache.sysList == null || cache.sysList.size() <= 0) && (cache.appList == null || cache.appList.size() <= 0)) {
            return null;
        }
        List<IMsgContent> tempList = new ArrayList();
        if (cache.sysList != null && cache.sysList.size() > 0) {
            tempList.addAll(cache.sysList);
            cache.sysList.clear();
        }
        if (cache.appList != null && cache.appList.size() > 0) {
            tempList.addAll(cache.appList);
            cache.appList.clear();
        }
        return readContents(false, tempList, isOutApp);
    }

    private MsgDialogParams readContents(boolean isSubscribe, List<IMsgContent> list, boolean isOutApp) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        LogUtils.m1568d(TAG, "getDialogFromList, list.size = " + list.size() + ", [0]= " + ((IMsgContent) list.get(0)).toString());
        List<IMsgContent> copy = copyList(list);
        MsgDialogParams msgDialog = buildParams(isSubscribe, isOutApp, (IMsgContent[]) copy.toArray(new IMsgContent[copy.size()]));
        list.clear();
        return msgDialog;
    }

    private MsgDialogParams buildParams(boolean isSubscribe, boolean isOutApp, IMsgContent... contents) {
        int i = contents.length - 1;
        String url = "";
        String showName = "";
        int style = 0;
        if (contents.length <= 1) {
            IMsgContent content = contents[0];
            showName = content.msg_title;
            if (content.style == 1) {
                url = content.url_window;
            } else if (content.style == 2 && content.msg_type == 3 && content.url != null) {
                url = content.url;
            }
            style = content.style;
        } else if (isSubscribe) {
            showName = "您预订的《" + contents[i].content + "》等影片上线啦";
        } else {
            showName = " 收到" + (i + 1) + "条新消息~ \r\n" + contents[i].msg_title;
        }
        return new MsgDialogParams(IMsgUtils.getDialogPos(isOutApp), url, showName, style, contents);
    }

    private List<IMsgContent> copyList(List<IMsgContent> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<IMsgContent> newList = new ArrayList();
        Collections.addAll(newList, new IMsgContent[list.size()]);
        Collections.copy(newList, list);
        return newList;
    }
}
