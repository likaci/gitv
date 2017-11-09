package com.gala.video.app.epg.openapi.feature.favorite;

import android.content.Context;
import com.qiyi.tv.client.impl.Params.OperationType;

public class DeleteAnonymousFavoriteCommand extends ClearFavoriteCommand {
    public DeleteAnonymousFavoriteCommand(Context context) {
        super(context, 10003, OperationType.OP_DELETE_ANONYMOUS_FAVORITE, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return false;
    }
}
