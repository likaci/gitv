package com.gala.video.app.epg.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;

public class GlobalQRFeedBackDialog extends GlobalDialog {
    private static final String TAG = "GlobalQRFeedBackDialog";
    private boolean mIsFeedbackSuccess;
    private RelativeLayout mLayoutQR;
    private LinearLayout mLayoutTxt;
    private TextView mLeftBottomTextView;
    private ProgressBarNewItem mLoadingView;
    private boolean mNeedUpdateDialogParam = true;
    private ImageView mQRCodeImage;
    protected OnKeyListener mQROnKeyListener = new OnKeyListener() {
        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
            if (event.getAction() == 0 && (keyCode == 19 || keyCode == 20 || keyCode == 21 || keyCode == 22)) {
                if (GlobalQRFeedBackDialog.this.mQuickCancel) {
                    GlobalQRFeedBackDialog.this.dismiss();
                    return true;
                }
            } else if (event.getAction() == 0 && ((keyCode == 23 || keyCode == 66) && GlobalQRFeedBackDialog.this.mButtonOK.getVisibility() == 8)) {
                GlobalQRFeedBackDialog.this.dismiss();
                return true;
            }
            return false;
        }
    };
    private TextView mRightBottomTextView;
    private RelativeLayout mRightContentLayout;
    private TextView mRightTopTextView;

    public static class StringModel {
        public String mContentString;
        public boolean mIsFeedbackSuccess;
        public String mLeftBottomString;
        public String mRightBottomString;
        public String mRightTopString;

        public String toString() {
            return "StringModel [mIsFeedbackSuccess=" + this.mIsFeedbackSuccess + ", mRightTopString=" + this.mRightTopString + ", mRightBottomString=" + this.mRightBottomString + ", mLeftBottomString=" + this.mLeftBottomString + ", mContentString=" + this.mContentString + AlbumEnterFactory.SIGN_STR;
        }
    }

    public GlobalQRFeedBackDialog(Context context) {
        super(context);
    }

    public GlobalQRFeedBackDialog(Context context, int theme) {
        super(context, theme);
    }

    public GlobalQRFeedBackDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnKeyListener(this.mQROnKeyListener);
    }

    protected void initLayout() {
        this.mContentTextView = (TextView) this.mContentLayout.findViewById(R.id.epg_global_dialog_error_tv);
        this.mLeftBottomTextView = (TextView) findViewById(R.id.epg_global_dialog_error_left_bottem_tv);
        this.mRightTopTextView = (TextView) findViewById(R.id.epg_global_dialog_error_right_top_tv);
        this.mRightBottomTextView = (TextView) findViewById(R.id.epg_global_dialog_error_right_bottom_tv);
        this.mQRCodeImage = (ImageView) this.mContentLayout.findViewById(R.id.epg_global_dialog_error_qr_iv);
        this.mLoadingView = (ProgressBarNewItem) this.mContentLayout.findViewById(R.id.epg_global_dialog_loading);
        this.mRightContentLayout = (RelativeLayout) this.mContentLayout.findViewById(R.id.epg_global_dialog_error_right_tv_layout);
        this.mLayoutTxt = (LinearLayout) this.mContentLayout.findViewById(R.id.epg_global_dialog_error_layout_txt);
        this.mLayoutQR = (RelativeLayout) this.mContentLayout.findViewById(R.id.epg_global_dialog_error_layout_qr);
    }

    protected void initParams() {
        this.mDialogLayoutResId = R.layout.share_global_dialog_layout;
        this.mContentResId = R.layout.epg_global_dialog_error_view;
        this.mDialogWidth = getDimen(R.dimen.dimen_570dp);
        this.mOnlyOneBtnHeight = getDimen(R.dimen.dimen_73dp);
        this.mOnlyOneBtnWidth = getDimen(R.dimen.dimen_446dp);
        this.mFirstBtnWidth = getDimen(R.dimen.dimen_273dp);
        this.mFirstBtnHeight = this.mOnlyOneBtnHeight;
        this.mSecondBtnWidth = this.mFirstBtnWidth;
        this.mSecondBtnHeight = this.mOnlyOneBtnHeight;
        updateParams();
    }

    public GlobalQRFeedBackDialog setParams(CharSequence contentText, Bitmap codeImage, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener) {
        boolean z;
        if (codeImage == null) {
            this.mNeedUpdateDialogParam = true;
        } else {
            this.mNeedUpdateDialogParam = false;
        }
        setQRImage(codeImage);
        if (cancelBtnText == null) {
            z = true;
        } else {
            z = false;
        }
        this.mQuickCancel = z;
        setContentText(contentText != null ? contentText.toString() : "");
        setOkBtnParams(okBtnText, okListener);
        setCancelBtnParams(cancelBtnText, cancelListener);
        if (StringUtils.isEmpty(okBtnText, cancelBtnText)) {
            layoutNoButtonUI();
        } else if (!((okBtnText != null && !"".equals(okBtnText) && cancelBtnText != null && !"".equals(cancelBtnText)) || okBtnText == null || "".equals(okBtnText))) {
            layoutOneButton(okBtnText, okListener);
            this.mButtonLayout.invalidate();
        }
        return this;
    }

    public GlobalQRFeedBackDialog setParams(StringModel stringModel, Bitmap codeImage, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener) {
        LogUtils.i(TAG, "setParams --- stringModel = ", stringModel);
        if (stringModel != null) {
            boolean z;
            if (codeImage == null) {
                this.mNeedUpdateDialogParam = true;
            } else {
                this.mNeedUpdateDialogParam = false;
            }
            setQRImage(codeImage);
            this.mIsFeedbackSuccess = stringModel.mIsFeedbackSuccess;
            if (this.mIsFeedbackSuccess) {
                setSuccessText(stringModel);
                setContentLayoutParams(stringModel, codeImage);
            } else {
                this.mLeftBottomTextView.setVisibility(8);
                setContentText(stringModel.mContentString);
            }
            if (cancelBtnText == null) {
                z = true;
            } else {
                z = false;
            }
            this.mQuickCancel = z;
            setOkBtnParams(okBtnText, okListener);
            setCancelBtnParams(cancelBtnText, cancelListener);
            if (StringUtils.isEmpty(okBtnText, cancelBtnText)) {
                layoutNoButtonUI();
            } else if (!((okBtnText != null && !"".equals(okBtnText) && cancelBtnText != null && !"".equals(cancelBtnText)) || okBtnText == null || "".equals(okBtnText))) {
                layoutOneButton(okBtnText, okListener);
                this.mButtonLayout.invalidate();
            }
        }
        return this;
    }

    private void setContentLayoutParams(StringModel stringModel, Bitmap codeImage) {
        if (stringModel != null && this.mIsFeedbackSuccess) {
            if (this.mContentLayout == null || this.mContainerView == null) {
                show();
            }
            LayoutParams contentLayoutParams = (LayoutParams) this.mContentLayout.getLayoutParams();
            if (contentLayoutParams != null) {
                contentLayoutParams.topMargin = getDimen(R.dimen.dimen_23dp);
                contentLayoutParams.bottomMargin = getDimen(R.dimen.dimen_20dp);
            }
            ViewGroup.LayoutParams containerViewLayoutParams = this.mContainerView.getLayoutParams();
            this.mDialogWidth = getDimen(R.dimen.dimen_500dp);
            containerViewLayoutParams.width = this.mDialogWidth;
            this.mOnlyOneBtnHeight = getDimen(R.dimen.dimen_73dp);
            this.mOnlyOneBtnWidth = getDimen(R.dimen.dimen_476dp);
            this.mFirstBtnWidth = this.mDialogWidth / 2;
            this.mFirstBtnHeight = this.mOnlyOneBtnHeight;
            this.mSecondBtnWidth = this.mDialogWidth / 2;
            this.mSecondBtnHeight = this.mOnlyOneBtnHeight;
            if (codeImage != null) {
                ((LayoutParams) this.mRightContentLayout.getLayoutParams()).width = -2;
            }
        }
    }

    private void setSuccessText(StringModel stringModel) {
        if (stringModel != null && this.mIsFeedbackSuccess) {
            if (this.mRightTopTextView == null || this.mRightBottomTextView == null || this.mLeftBottomTextView == null) {
                show();
            }
            setText(this.mRightTopTextView, stringModel.mRightTopString);
            setText(this.mRightBottomTextView, stringModel.mRightBottomString);
            setText(this.mLeftBottomTextView, stringModel.mLeftBottomString);
        }
    }

    private void setText(TextView textView, String s) {
        if (textView != null && !StringUtils.isEmpty((CharSequence) s)) {
            textView.setVisibility(0);
            textView.setText(s);
        }
    }

    public GlobalQRFeedBackDialog setParams(CharSequence contentText) {
        return setParams(contentText, null, null, null, null, null);
    }

    public GlobalQRFeedBackDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener) {
        return setParams(contentText, null, okBtnText, okListener, null, null);
    }

    public GlobalQRFeedBackDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener) {
        return setParams(contentText, null, okBtnText, okListener, cancelBtnText, cancelListener);
    }

    public GlobalQRFeedBackDialog addMessageView(View view, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener) {
        if (view == null) {
            return null;
        }
        if (this.mLayoutTxt == null || this.mLayoutQR == null) {
            show();
        }
        this.mLayoutTxt.setVisibility(0);
        this.mLayoutQR.setVisibility(8);
        this.mLayoutTxt.removeAllViewsInLayout();
        this.mLayoutTxt.removeAllViews();
        this.mLayoutTxt.addView(view);
        super.setOkBtnParams(okBtnText, okListener);
        super.setCancelBtnParams(cancelBtnText, cancelListener);
        return this;
    }

    public GlobalQRFeedBackDialog setQRImage(Bitmap codeImage) {
        LogUtils.d(TAG, "setQRImage", new Throwable().fillInStackTrace());
        if (this.mQRCodeImage == null) {
            show();
        }
        if (!(this.mLayoutTxt == null || this.mLayoutQR == null)) {
            this.mLayoutTxt.setVisibility(8);
            this.mLayoutQR.setVisibility(0);
        }
        if (!(codeImage == null || this.mQRCodeImage == null)) {
            this.mQRCodeImage.setImageBitmap(codeImage);
        }
        LayoutParams txtParams = (LayoutParams) this.mRightContentLayout.getLayoutParams();
        if (codeImage == null) {
            txtParams.width = -2;
            txtParams.leftMargin = getDimen(R.dimen.dimen_0dp);
            txtParams.rightMargin = getDimen(R.dimen.dimen_0dp);
            txtParams.topMargin = getDimen(R.dimen.dimen_10dp);
            txtParams.bottomMargin = getDimen(R.dimen.dimen_10dp);
            this.mRightContentLayout.setGravity(1);
            this.mContentTextView.setMaxLines(7);
            this.mQRCodeImage.setVisibility(8);
        } else {
            txtParams.width = getDimen(R.dimen.dimen_242dp);
            txtParams.leftMargin = getDimen(R.dimen.dimen_20dp);
            txtParams.rightMargin = getDimen(R.dimen.dimen_0dp);
            txtParams.topMargin = getDimen(R.dimen.dimen_0dp);
            txtParams.bottomMargin = getDimen(R.dimen.dimen_0dp);
            this.mContentTextView.setMaxLines(11);
            this.mQRCodeImage.setVisibility(0);
        }
        return this;
    }

    private void updateParams() {
        if (this.mNeedUpdateDialogParam) {
            this.mDialogLayoutResId = R.layout.share_global_dialog_layout;
            this.mContentResId = R.layout.epg_global_dialog_error_view;
            this.mDialogWidth = getDimen(R.dimen.dimen_530dp);
            this.mOnlyOneBtnHeight = getDimen(R.dimen.dimen_73dp);
            this.mOnlyOneBtnWidth = getDimen(R.dimen.dimen_506dp);
            this.mFirstBtnWidth = getDimen(R.dimen.dimen_253dp);
            this.mFirstBtnHeight = this.mOnlyOneBtnHeight;
            this.mSecondBtnWidth = this.mFirstBtnWidth;
            this.mSecondBtnHeight = this.mOnlyOneBtnHeight;
        }
    }

    public GlobalQRFeedBackDialog setContentText(String contentText) {
        if (this.mContentTextView == null) {
            show();
        }
        this.mContentTextView.setVisibility(0);
        this.mContentTextView.setText(contentText);
        return this;
    }

    protected void layoutNoButtonUI() {
        super.layoutNoButtonUI();
    }

    protected void layoutOneButton(String text, OnClickListener listener) {
        super.layoutOneButton(text, listener);
    }

    public ImageView getQRImageView() {
        return this.mQRCodeImage;
    }

    public ProgressBarNewItem getLoadingView() {
        return this.mLoadingView;
    }

    public void setLoadingVisible(int visibile) {
        if (this.mLoadingView != null) {
            this.mLoadingView.setVisibility(visibile);
        }
    }

    public void showQRFail() {
        this.mContentLayout.findViewById(R.id.epg_view_failure).setVisibility(0);
    }
}
