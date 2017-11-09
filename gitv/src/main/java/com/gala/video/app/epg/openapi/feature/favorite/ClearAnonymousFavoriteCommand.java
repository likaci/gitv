package com.gala.video.app.epg.openapi.feature.favorite;

import android.content.Context;
import com.qiyi.tv.client.impl.Params.OperationType;

public class ClearAnonymousFavoriteCommand extends ClearFavoriteCommand {
    public ClearAnonymousFavoriteCommand(Context context) {
        super(context, 10003, OperationType.OP_CLEAR_ANONYMOUS_FAVORITE, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return false;
    }
}
