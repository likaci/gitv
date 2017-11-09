package com.gala.video.app.epg.openBroadcast;

import com.gala.video.lib.share.common.configs.IntentConfig.BroadcastAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.ActionHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.IOpenBroadcastActionHolder.Wrapper;

public class OpenBroadcastEpgActionHolder extends Wrapper {
    public ActionHolder[] getActionHolder() {
        return new ActionHolder[]{new ActionHolder(BroadcastAction.ACTION_ALBUMLIST, new OpenAlbumListAction()), new ActionHolder(BroadcastAction.ACTION_PERSON_CENTER, new OpenPersonCenter()), new ActionHolder(BroadcastAction.ACTION_SEARCH, new OpenSearchAction()), new ActionHolder(BroadcastAction.ACTION_SEARCHRESULT, new OpenSearchResultAction()), new ActionHolder(BroadcastAction.ACTION_PURCHASE, new OpenPurchaseAction()), new ActionHolder(BroadcastAction.ACTION_HOME, new OpenHomeAction()), new ActionHolder(BroadcastAction.ACTION_SUBJECT, new OpenSubjectAction())};
    }
}
