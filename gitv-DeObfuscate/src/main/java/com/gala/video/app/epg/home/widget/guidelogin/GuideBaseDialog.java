package com.gala.video.app.epg.home.widget.guidelogin;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import com.gala.video.app.epg.C0508R;

public abstract class GuideBaseDialog extends AlertDialog {
    public Context mContext;

    public abstract void onCreate();

    public GuideBaseDialog(Context context) {
        super(context, C0508R.style.guide_login_dialog);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mContext = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
    }
}
