package com.gala.video.app.epg.widget;

import android.content.Context;
import android.view.View.OnClickListener;

public class DialogUtils {
    public static ProgressIndicator buildUpdateIndicator(Context context, OnClickListener onCancelDownLoadListener) {
        ProgressIndicator progressBar = new ProgressIndicator(context);
        progressBar.setCancelListener(onCancelDownLoadListener);
        return progressBar;
    }
}
