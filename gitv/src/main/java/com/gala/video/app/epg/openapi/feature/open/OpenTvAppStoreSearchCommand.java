package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gitv.tvappstore.AppStoreManager;

public class OpenTvAppStoreSearchCommand extends ServerCommand<Intent> {
    private static final String TAG = "OpenTvAppStoreSearchCommand";

    public OpenTvAppStoreSearchCommand(Context context) {
        super(context, 10018, 20001, 30000);
        setNeedNetwork(false);
    }

    public Bundle onProcess(Bundle params) {
        AppStoreManager.getInstance().openSearchActivity();
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
