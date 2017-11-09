package com.gala.video.app.epg.ui.multisubject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.video.app.epg.ui.multisubject.model.MultiSubjectInfoModel;
import com.gala.video.app.epg.uikit.ui.multisubject.MultiSubjectActivity;
import com.gala.video.lib.share.utils.PageIOUtils;

public class MultiSubjectEnterUtils {
    public static final String PLAY_TYPE_FROM_MULTI = "multitopic";
    private static final String TAG = "EPG/multisubject/MultiSubjectEnterUtils";

    public static void start(Context context, String itemId, String buysource, String from) {
        Log.e(TAG, "start multisubject activity, itemId=" + itemId + ",buysource=" + buysource + ",from=" + from);
        Intent it = new Intent(context, MultiSubjectActivity.class);
        MultiSubjectInfoModel multiSubjectInfoModel = new MultiSubjectInfoModel();
        multiSubjectInfoModel.setItemId(itemId);
        multiSubjectInfoModel.setFrom(from);
        multiSubjectInfoModel.setBuysource(buysource);
        multiSubjectInfoModel.setPlayType(PLAY_TYPE_FROM_MULTI);
        multiSubjectInfoModel.setEnterType(13);
        multiSubjectInfoModel.setBuyFrom("rec");
        it.putExtra("intent_model", multiSubjectInfoModel);
        it.addFlags(67108864);
        PageIOUtils.activityIn(context, it);
    }
}
