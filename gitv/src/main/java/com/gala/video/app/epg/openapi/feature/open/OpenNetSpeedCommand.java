package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.netspeed.QNetSpeedActivity;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;

public class OpenNetSpeedCommand extends ServerCommand<Intent> {
    public OpenNetSpeedCommand(Context context) {
        super(context, 10005, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        Intent intent = new Intent(IntentUtils.getActionName(QNetSpeedActivity.class.getName()));
        intent.putExtra("source", "openAPI");
        intent.putExtra("fromWhere", "openAPI");
        intent.setFlags(ServerParamsHelper.parseIntentFlag(params));
        increaseAccessCount();
        PageIOUtils.activityIn(getContext(), intent);
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
