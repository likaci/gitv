package com.gala.video.app.epg.openapi.feature.history;

import android.content.Context;
import com.qiyi.tv.client.impl.Params.OperationType;

public class DeleteAnonymousHistoryCommand extends ClearHistoryCommand {
    public DeleteAnonymousHistoryCommand(Context context) {
        super(context, 10001, OperationType.OP_DELETE_ANONYMOUS_HISTORY, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return false;
    }
}
