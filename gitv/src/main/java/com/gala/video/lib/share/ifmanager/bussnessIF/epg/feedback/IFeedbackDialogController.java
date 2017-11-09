package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.view.View.OnClickListener;
import com.gala.sdk.player.IMedia;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IFeedbackDialogController extends IInterfaceWrapper {

    public static abstract class Wrapper implements IFeedbackDialogController {
        public Object getInterface() {
            return this;
        }

        public static IFeedbackDialogController asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IFeedbackDialogController)) {
                return null;
            }
            return (IFeedbackDialogController) wrapper;
        }
    }

    public interface OnFeedBackPrepareListener {
        String onPrepare();
    }

    void clearCurrentDialog();

    void feedback();

    IMedia getMeida();

    void init(Context context, OnFeedBackPrepareListener onFeedBackPrepareListener);

    void setEventID(String str);

    void setMedia(IMedia iMedia);

    void setPrepareListener(OnFeedBackPrepareListener onFeedBackPrepareListener);

    void showQRDialog(FeedBackModel feedBackModel, OnDismissListener onDismissListener);

    void showQRDialog(FeedBackModel feedBackModel, OnDismissListener onDismissListener, String str, OnClickListener onClickListener, String str2, OnClickListener onClickListener2);
}
