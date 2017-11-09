package com.gala.video.app.player.openapi.feature.open;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class OpenDailyNewsCommand extends ServerCommand<Void> {
    public OpenDailyNewsCommand(Context context) {
        super(context, 10006, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        NewsDetailPlayParamBuilder builder = new NewsDetailPlayParamBuilder();
        builder.setBuySource("openAPI").setFrom("openAPI").setTabSource("其他");
        builder.setChannelName("");
        GetInterfaceTools.getPlayerPageProvider().startNewsDetailPlayerPage(context, builder);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
