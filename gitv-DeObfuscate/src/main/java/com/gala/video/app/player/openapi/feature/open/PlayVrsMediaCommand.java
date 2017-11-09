package com.gala.video.app.player.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;

public class PlayVrsMediaCommand extends ServerCommand<Intent> {
    public PlayVrsMediaCommand(Context context) {
        super(context, TargetType.TARGET_VRS_MEDIA, 20004, 30000);
    }

    public Bundle onProcess(Bundle params) {
        return OpenApiResultCreater.createResultBundle(5);
    }
}
