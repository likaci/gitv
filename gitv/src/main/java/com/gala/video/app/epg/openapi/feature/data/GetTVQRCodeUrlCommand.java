package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetTVQRCodeUrlCommand extends ServerCommand<String> {
    private static final String TAG = "GetTVQRCodeUrlCommand";

    public GetTVQRCodeUrlCommand(Context context) {
        super(context, TargetType.TARGET_TV_QR_CODE, 20003, DataType.DATA_URL);
    }

    protected Bundle onProcess(Bundle inParams) {
        return OpenApiResultCreater.createResultBundle(0);
    }
}
