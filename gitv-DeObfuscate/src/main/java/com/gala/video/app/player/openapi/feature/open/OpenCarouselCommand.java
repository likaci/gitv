package com.gala.video.app.player.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class OpenCarouselCommand extends ServerCommand<Intent> {
    public OpenCarouselCommand(Context context) {
        super(context, 10017, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
        carouselPlayParamBuilder.setChannel(null);
        carouselPlayParamBuilder.setFrom("");
        carouselPlayParamBuilder.setTabSource("");
        GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(getContext(), carouselPlayParamBuilder);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
