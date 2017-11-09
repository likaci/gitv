package com.gala.video.app.epg.web.core;

import android.util.Log;
import com.gala.video.app.epg.web.function.IWebDialog;
import com.gala.video.app.epg.web.function.WebFunContract.IFunDialog;

public class FuntionDialog implements IFunDialog {
    public static final String STATE_0 = "0";
    public static final String STATE_1 = "1";
    private static final String TAG = "FuntionDialog";
    private IWebDialog mIWebDialog;

    public FuntionDialog(IWebDialog webDialog) {
        this.mIWebDialog = webDialog;
    }

    public void setDialogState(String state) {
        Log.i(TAG, "setDialogState state: " + state);
        if (this.mIWebDialog != null) {
            this.mIWebDialog.setDialogState(state);
        }
    }
}
