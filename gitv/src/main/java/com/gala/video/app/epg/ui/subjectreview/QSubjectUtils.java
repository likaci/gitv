package com.gala.video.app.epg.ui.subjectreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.video.lib.share.utils.PageIOUtils;

public class QSubjectUtils {
    public static final String FROM = "open_from";
    public static final String FROM_OPEN_API = "from_openApi";
    public static final String ID = "id";
    public static final String SUBJECT_DATA = "subject_data";

    public static void startQSubjectActivity(Context context, String id, String from) {
        if (context != null) {
            Intent intent = new Intent(context, QSubjectReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString(FROM, from);
            intent.putExtras(bundle);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public static void startQSubjectActivityOpenApi(Context context, int intentFlag) {
        if (context != null) {
            Intent intent = new Intent(context, QSubjectReviewActivity.class);
            intent.putExtra(FROM, FROM_OPEN_API);
            if (intentFlag > 0) {
                intent.setFlags(intentFlag);
            }
        }
    }
}
