package com.gala.video.lib.share.common.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;

public class GlobalDialog extends AlertDialog implements OnFocusChangeListener {
    protected static int DEFAULT_DURATION = 200;
    protected static float DEFAULT_SCALE = 1.1f;
    private static final String TAG = "GlobalDialog";
    protected View mBtnLine;
    protected View mButtonCancel;
    protected View mButtonLayout;
    protected View mButtonOK;
    protected ViewGroup mContainerView;
    protected FrameLayout mContentLayout;
    protected int mContentResId;
    protected TextView mContentTextView;
    protected Context mContext;
    protected int mDialogLayoutResId;
    protected View mDialogLine;
    protected View mDialogTopView;
    protected int mDialogWidth;
    protected int mFirstBtnHeight;
    protected int mFirstBtnWidth;
    protected final OnDismissListener mInnerDismissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GlobalDialog.TAG, "mInnerDismissListener.onDismiss(" + dialog + ")");
            }
            VoiceManager.instance().onDialogDismiss(dialog);
            if (GlobalDialog.this.mOuterDismissListener != null) {
                GlobalDialog.this.mOuterDismissListener.onDismiss(dialog);
                GlobalDialog.this.mOuterDismissListener = null;
            }
            GlobalDialog.this.destory();
        }
    };
    protected final OnShowListener mInnerShownListener = new OnShowListener() {
        public void onShow(DialogInterface dialog) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GlobalDialog.TAG, "mInnerShownListener.onShow(" + dialog + ")");
            }
            VoiceManager.instance().onDialogShow(dialog);
            if (GlobalDialog.this.mOuterShownListener != null) {
                GlobalDialog.this.mOuterShownListener.onShow(dialog);
                GlobalDialog.this.mOuterShownListener = null;
            }
        }
    };
    protected LayoutInflater mLayoutInflater;
    protected OnKeyListener mOnKeyListener = new OnKeyListener() {
        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
            if (keyCode != 82) {
                if (event.getAction() == 0 && (keyCode == 19 || keyCode == 20 || keyCode == 21 || keyCode == 22 || keyCode == 82)) {
                    if (GlobalDialog.this.mQuickCancel) {
                        GlobalDialog.this.dismiss();
                        return true;
                    }
                } else if (event.getAction() == 0 && ((keyCode == 23 || keyCode == 66) && GlobalDialog.this.mButtonOK.getVisibility() == 8)) {
                    GlobalDialog.this.dismiss();
                    return true;
                }
                return false;
            } else if (!GlobalDialog.this.mQuickCancel) {
                return true;
            } else {
                GlobalDialog.this.dismiss();
                return true;
            }
        }
    };
    protected int mOnlyOneBtnHeight;
    protected int mOnlyOneBtnWidth;
    protected OnDismissListener mOuterDismissListener;
    protected OnShowListener mOuterShownListener;
    protected boolean mQuickCancel = false;
    protected boolean mRightBtnRequestFocus;
    protected int mSecondBtnHeight;
    protected int mSecondBtnWidth;
    protected TextView mTextCancel;
    protected TextView mTextOK;

    public GlobalDialog(Context context) {
        super(context, R.style.alert_dialog);
        init(context);
    }

    public GlobalDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public GlobalDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        View containerView = this.mLayoutInflater.inflate(this.mDialogLayoutResId, null);
        setContentView(containerView, new LayoutParams(this.mDialogWidth, -2));
        this.mContainerView = (ViewGroup) containerView;
        this.mContentLayout = (FrameLayout) findViewById(R.id.share_dialog_content_layout);
        this.mBtnLine = this.mContainerView.findViewById(R.id.share_dialog_btn_line);
        this.mLayoutInflater.inflate(this.mContentResId, this.mContentLayout, true);
        initLayout();
        this.mButtonOK = findViewById(R.id.share_dialog_btn1);
        this.mButtonCancel = findViewById(R.id.share_dialog_btn2);
        this.mTextOK = (TextView) findViewById(R.id.share_txt_btn1);
        this.mTextCancel = (TextView) findViewById(R.id.share_txt_btn2);
        this.mButtonLayout = findViewById(R.id.share_dialog_btn_layout);
        this.mDialogLine = findViewById(R.id.share_dialog_line);
        this.mDialogTopView = findViewById(R.id.share_dialog_top_line);
        this.mButtonOK.setVisibility(8);
        this.mButtonCancel.setVisibility(8);
        this.mButtonOK.setOnFocusChangeListener(this);
        this.mButtonCancel.setOnFocusChangeListener(this);
        changeDialogDetailParams();
        setRightBtnRequestFocus(containerView);
        setOnKeyListener(this.mOnKeyListener);
        super.setOnShowListener(this.mInnerShownListener);
        super.setOnDismissListener(this.mInnerDismissListener);
    }

    protected void changeDialogDetailParams() {
    }

    protected void initLayout() {
        this.mContentTextView = (TextView) this.mContentLayout.findViewById(R.id.share_dialog_text);
        this.mBtnLine.setVisibility(8);
        this.mContentTextView.setVisibility(0);
        this.mContentTextView.setGravity(1);
    }

    protected void initParams() {
        this.mDialogLayoutResId = R.layout.share_global_dialog_layout;
        this.mContentResId = R.layout.share_global_dialog_text_view;
        this.mDialogWidth = getDimen(R.dimen.dimen_570dp);
        this.mOnlyOneBtnHeight = getDimen(R.dimen.dimen_73dp);
        this.mOnlyOneBtnWidth = getDimen(R.dimen.dimen_546dp);
        this.mFirstBtnWidth = getDimen(R.dimen.dimen_273dp);
        this.mFirstBtnHeight = this.mOnlyOneBtnHeight;
        this.mSecondBtnWidth = getDimen(R.dimen.dimen_273dp);
        this.mSecondBtnHeight = this.mOnlyOneBtnHeight;
    }

    public void setRightBtnRequestFocus(boolean rightBtnRequestFocus) {
        this.mRightBtnRequestFocus = rightBtnRequestFocus;
    }

    private void setRightBtnRequestFocus(View view) {
        if (view instanceof GlobalDialogPanel) {
            ((GlobalDialogPanel) view).setForceRequestView(this.mRightBtnRequestFocus ? this.mButtonCancel : this.mButtonOK);
        }
    }

    public GlobalDialog setParams(CharSequence contentText) {
        return setParams(contentText, null, null, null, null);
    }

    public GlobalDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener) {
        return setParams(contentText, okBtnText, okListener, null, null);
    }

    public GlobalDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener, boolean rightBtnRequestFocus) {
        setRightBtnRequestFocus(rightBtnRequestFocus);
        return setParams(contentText, okBtnText, okListener, cancelBtnText, cancelListener);
    }

    public GlobalDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener) {
        boolean z = cancelBtnText == null && okBtnText == null;
        this.mQuickCancel = z;
        setContentText(contentText);
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

    public GlobalDialog setParams(CharSequence content, Bitmap bitmap, String ok, OnClickListener okListener, String cancel, OnClickListener cancelListener) {
        return setParams(content, ok, okListener, cancel, cancelListener);
    }

    protected GlobalDialog setOkBtnParams(String text, OnClickListener listener) {
        if (this.mButtonOK == null || this.mTextOK == null) {
            show();
        }
        if (!StringUtils.isEmpty((CharSequence) text)) {
            this.mTextOK.setText(text);
        }
        if (listener == null || StringUtils.isEmpty((CharSequence) text)) {
            this.mButtonOK.setFocusable(false);
            this.mButtonOK.setVisibility(8);
        } else {
            this.mButtonOK.setFocusable(true);
            this.mButtonOK.setVisibility(0);
            setTextOkParams();
            this.mButtonOK.setOnClickListener(listener);
        }
        return this;
    }

    protected GlobalDialog setCancelBtnParams(String text, OnClickListener listener) {
        if (this.mButtonCancel == null) {
            show();
        }
        if (this.mButtonOK.getVisibility() != 0) {
            this.mButtonCancel.setVisibility(8);
        } else {
            this.mTextCancel.setText(text);
            if (listener == null) {
                this.mButtonCancel.setFocusable(false);
                this.mButtonCancel.setVisibility(8);
            } else {
                this.mButtonOK.setFocusable(true);
                this.mButtonCancel.setVisibility(0);
                setTextCancelParams();
                this.mButtonCancel.setOnClickListener(listener);
            }
        }
        return this;
    }

    protected void layoutNoButtonUI() {
        this.mButtonLayout.setVisibility(8);
        this.mDialogLine.setVisibility(4);
    }

    protected void layoutOneButton(String text, OnClickListener listener) {
        MarginLayoutParams txtParams = (MarginLayoutParams) this.mTextOK.getLayoutParams();
        txtParams.width = this.mOnlyOneBtnWidth;
        txtParams.height = this.mOnlyOneBtnHeight;
        ((MarginLayoutParams) this.mButtonLayout.getLayoutParams()).setMargins(0, 0, 0, 0);
    }

    protected void setTextOkParams() {
        LayoutParams txtParams = this.mTextOK.getLayoutParams();
        txtParams.width = this.mFirstBtnWidth;
        txtParams.height = this.mFirstBtnHeight;
    }

    protected void setTextCancelParams() {
        LayoutParams txtParams = this.mTextCancel.getLayoutParams();
        txtParams.width = this.mSecondBtnWidth;
        txtParams.height = this.mSecondBtnHeight;
    }

    protected GlobalDialog setContentText(CharSequence contentText) {
        if (this.mContentTextView == null) {
            show();
        }
        if (this.mContentTextView != null) {
            this.mContentTextView.setText(contentText);
        }
        return this;
    }

    public TextView getContentTextView() {
        return this.mContentTextView;
    }

    public void setContentTextViewFillHorizontal() {
        if (this.mContentTextView != null) {
            this.mContentTextView.setGravity(7);
        }
    }

    public void setGravity(int gravity) {
        if (this.mContentTextView != null) {
            this.mContentTextView.setGravity(gravity);
        }
    }

    public void show() {
        try {
            if (!(getContext() instanceof Activity)) {
                super.show();
            } else if (!((Activity) getContext()).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    public void dismiss() {
        try {
            if (!(getContext() instanceof Activity)) {
                super.dismiss();
            } else if (!((Activity) getContext()).isFinishing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
    }

    public void setOnShowListener(OnShowListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnShowListener(" + listener + ")");
        }
        this.mOuterShownListener = listener;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnDismissListener(" + listener + ")");
        }
        this.mOuterDismissListener = listener;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v != null) {
            TextView textView = null;
            if (v.getId() == R.id.share_dialog_btn1) {
                textView = this.mTextOK;
            } else if (v.getId() == R.id.share_dialog_btn2) {
                textView = this.mTextCancel;
            }
            if (!(textView == null || getContext() == null)) {
                if (hasFocus) {
                    textView.setTextColor(getColor(Project.getInstance().getBuild().isLitchi() ? R.color.dialog_text_color_sel : R.color.dialog_text_color_sel));
                } else {
                    textView.setTextColor(getColor(R.color.dialog_text_color_unsel));
                }
            }
            if (hasFocus) {
                v.bringToFront();
                ViewParent view = v.getParent();
                if (view != null) {
                    ViewGroup parent = (ViewGroup) view;
                    if (parent != null) {
                        parent.invalidate();
                    }
                } else {
                    return;
                }
            }
            AnimationUtil.zoomAnimation(textView, hasFocus, DEFAULT_SCALE, DEFAULT_DURATION, true);
        }
    }

    protected int getDimen(int dimen) {
        return (int) this.mContext.getResources().getDimension(dimen);
    }

    protected String getString(int resId) {
        return this.mContext.getResources().getString(resId);
    }

    protected int getColor(int resId) {
        return this.mContext.getResources().getColor(resId);
    }

    protected Drawable getDrawable(int resId) {
        return this.mContext.getResources().getDrawable(resId);
    }

    protected void destoryProc() {
    }

    private void destory() {
        if (this.mContainerView != null) {
            this.mContainerView.removeAllViewsInLayout();
            this.mContainerView = null;
        }
        destoryProc();
        this.mLayoutInflater = null;
        this.mContentLayout = null;
        this.mContentTextView = null;
        this.mTextOK = null;
        this.mTextCancel = null;
        this.mButtonOK = null;
        this.mButtonCancel = null;
        this.mButtonLayout = null;
        this.mDialogLine = null;
        this.mOuterDismissListener = null;
        this.mOuterShownListener = null;
        this.mContext = null;
        VoiceManager.instance().onDialogDismiss(null);
        VoiceManager.instance().onDialogShow(null);
    }
}
