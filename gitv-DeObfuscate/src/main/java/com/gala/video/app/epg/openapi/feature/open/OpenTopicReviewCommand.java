package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.app.epg.ui.subjectreview.QSubjectUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;

public class OpenTopicReviewCommand extends ServerCommand<Intent> {
    public OpenTopicReviewCommand(Context context) {
        super(context, 10000, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        QSubjectUtils.startQSubjectActivityOpenApi(getContext(), ServerParamsHelper.parseIntentFlag(params));
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        return result;
    }
}
