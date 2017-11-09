package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;

public class OpenVipPageCommand extends ServerCommand<Intent> {
    public OpenVipPageCommand(Context context) {
        super(context, 10014, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        AlbumUtils.startChannelNewVipPageOpenApi(getContext(), ServerParamsHelper.parseIntentFlag(params));
        increaseAccessCount();
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
