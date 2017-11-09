package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;

public class OpenSearchCommand extends ServerCommand<Intent> {
    public OpenSearchCommand(Context context) {
        super(context, 10008, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        SearchEnterUtils.startSearchActivityOpenApi(getContext(), ServerParamsHelper.parseIntentFlag(params));
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
