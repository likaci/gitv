package com.gala.video.app.epg.home.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.tabmanager.TabLoadingView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;

public class TabChangingDialog extends AlertDialog {
    private static final String TAG = "TabChangingDialog";
    private TabLoadingView mLoadingView;

    public TabChangingDialog(Context context) {
        super(context, C0508R.style.Theme_Dialog_Loading_Notitle);
    }

    public TabChangingDialog(Context context, int theme) {
        super(context, theme);
    }

    public TabChangingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_tab_loading_dialog_layout);
        initWindow();
    }

    public void show() {
        super.show();
        LogUtils.m1568d(TAG, DBColumns.IS_NEED_SHOW);
        if (this.mLoadingView != null) {
            this.mLoadingView.startAnimation();
        }
    }

    public void dismiss() {
        super.dismiss();
        LogUtils.m1568d(TAG, "dismiss");
        if (this.mLoadingView != null) {
            this.mLoadingView.stopAnimation();
        }
    }

    private void initWindow() {
        getWindow().setLayout(-1, -1);
        getWindow().getAttributes().dimAmount = 0.0f;
        getWindow().setBackgroundDrawableResource(C0508R.color.tab_manage_loading_background);
        this.mLoadingView = (TabLoadingView) findViewById(C0508R.id.epg_tab_loading_view);
    }
}
