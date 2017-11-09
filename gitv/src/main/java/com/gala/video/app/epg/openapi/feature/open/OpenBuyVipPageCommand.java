package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class OpenBuyVipPageCommand extends ServerCommand<Intent> {
    public OpenBuyVipPageCommand(Context context) {
        super(context, 10019, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        GetInterfaceTools.getWebEntry().startPurchasePage(getContext());
        Log.d("myptest", "open Vip Buy Page success");
        increaseAccessCount();
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
