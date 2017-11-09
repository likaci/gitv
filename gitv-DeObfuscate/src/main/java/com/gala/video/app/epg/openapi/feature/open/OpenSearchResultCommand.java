package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class OpenSearchResultCommand extends ServerCommand<Intent> {
    public OpenSearchResultCommand(Context context) {
        super(context, TargetType.TARGET_SEARCH_RESULT, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        AlbumUtils.startSearchResultPageOpenApi(getContext(), 0, ServerParamsHelper.parseKeyword(params), 1, "", ServerParamsHelper.parseIntentFlag(params), null);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
