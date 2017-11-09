package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetPictureUrlCommand extends ServerCommand<String> {
    private static final String TAG = "GetPictureUrlCommand";

    public GetPictureUrlCommand(Context context) {
        super(context, TargetType.TARGET_PICTURE, 20003, DataType.DATA_URL);
        setNeedNetwork(false);
    }

    public Bundle onProcess(Bundle params) {
        ArrayList<String> newPictureUrls = exchangePictureUrl(ServerParamsHelper.parsePictureSize(params), ServerParamsHelper.parsePictureUrl(params));
        if (LogUtils.mIsDebug) {
            Iterator it = newPictureUrls.iterator();
            while (it.hasNext()) {
                LogUtils.m1568d(TAG, "newPictureUrl = " + ((String) it.next()));
            }
        }
        Bundle bundle = OpenApiResultCreater.createResultBundleOfString(0, newPictureUrls);
        increaseAccessCount();
        return bundle;
    }

    private ArrayList<String> exchangePictureUrl(int pictureSize, List<String> urls) {
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < urls.size(); i++) {
            list.add(PicSizeUtils.exchangePictureUrl((String) urls.get(i), pictureSize));
        }
        return list;
    }
}
