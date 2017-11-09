package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.applist.AppListActivity;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.utils.PageIOUtils;

public class OpenAppListCommand extends ServerCommand<Intent> {
    public OpenAppListCommand(Context context) {
        super(context, 10013, 20001, 30000);
    }

    protected Bundle onProcess(Bundle inParams) {
        Intent startAppListIntent = new Intent(getContext(), AppListActivity.class);
        startAppListIntent.addFlags(ServerParamsHelper.parseIntentFlag(inParams));
        PageIOUtils.activityIn(getContext(), startAppListIntent);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
