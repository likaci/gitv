package com.gala.video.app.epg.widget.dialog;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.widget.dialog.GlobalVipCloudView.GlobalVipCloudViewCallBack;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.utils.ImageCacheUtil;

public class GlobalVipDialog extends GlobalDialog {
    private static final String TAG = "GlobalVipDialog";
    protected int mButtonView = R.id.share_dialog_btn2;
    protected GlobalVipCloudView mCloudView1;
    protected GlobalVipCloudView mCloudView2;
    protected GlobalVipCloudView mCloudView3;
    protected int mFocusCloudViewId = R.id.epg_logout_img_left;
    private GlobalVipCloudViewCallBack mGlobalVipCloudViewCallBack;

    public GlobalVipDialog(Context context) {
        super(context, R.style.alert_dialog);
        init(context);
    }

    public GlobalVipDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public GlobalVipDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void changeFramLayoutWidth() {
        LayoutParams layoutParams = (LayoutParams) this.mContentLayout.getLayoutParams();
        layoutParams.width = this.mDialogWidth;
        layoutParams.topMargin = getDimen(R.dimen.dimen_83dp);
        layoutParams.bottomMargin = getDimen(R.dimen.dimen_10dp);
        this.mContentLayout.setLayoutParams(layoutParams);
    }

    public void setGlobalVipCloudViewCallBack(GlobalVipCloudViewCallBack globalVipCloudViewCallBack) {
        this.mGlobalVipCloudViewCallBack = globalVipCloudViewCallBack;
    }

    protected void initLayout() {
        changeFramLayoutWidth();
        this.mContentTextView = (TextView) this.mContentLayout.findViewById(R.id.epg_dialog_text);
        this.mCloudView1 = (GlobalVipCloudView) this.mContentLayout.findViewById(R.id.epg_logout_img_left);
        this.mCloudView2 = (GlobalVipCloudView) this.mContentLayout.findViewById(R.id.epg_logout_img_center);
        this.mCloudView3 = (GlobalVipCloudView) this.mContentLayout.findViewById(R.id.epg_logout_img_right);
        this.mCloudView1.setOnFocusChangeListener(this);
        this.mCloudView2.setOnFocusChangeListener(this);
        this.mCloudView3.setOnFocusChangeListener(this);
        this.mCloudView1.setPosition(1);
        this.mCloudView2.setPosition(2);
        this.mCloudView3.setPosition(3);
        this.mCloudView1.setGlobalVipCloudViewCallBack(this.mGlobalVipCloudViewCallBack);
        this.mCloudView2.setGlobalVipCloudViewCallBack(this.mGlobalVipCloudViewCallBack);
        this.mCloudView3.setGlobalVipCloudViewCallBack(this.mGlobalVipCloudViewCallBack);
        this.mContentTextView.setVisibility(0);
        this.mContentTextView.setGravity(1);
    }

    protected void initParams() {
        this.mDialogLayoutResId = R.layout.share_global_dialog_layout;
        this.mContentResId = R.layout.epg_global_dialog_vip_view;
        this.mDialogWidth = getDimen(R.dimen.dimen_724dp);
        this.mOnlyOneBtnHeight = getDimen(R.dimen.dimen_73dp);
        this.mOnlyOneBtnWidth = getDimen(R.dimen.dimen_699dp);
        this.mFirstBtnWidth = getDimen(R.dimen.dimen_350dp);
        this.mFirstBtnHeight = this.mOnlyOneBtnHeight;
        this.mSecondBtnWidth = getDimen(R.dimen.dimen_350dp);
        this.mSecondBtnHeight = this.mOnlyOneBtnHeight;
    }

    public GlobalVipDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener, boolean rightBtnRequestFocus, BitmapAlbum bmpalbum1, BitmapAlbum bmpalbum2, BitmapAlbum bmpalbum3) {
        setRightBtnRequestFocus(rightBtnRequestFocus);
        return setParams(contentText, okBtnText, okListener, cancelBtnText, cancelListener, bmpalbum1, bmpalbum2, bmpalbum3);
    }

    public GlobalVipDialog setParams(CharSequence contentText, String okBtnText, OnClickListener okListener, String cancelBtnText, OnClickListener cancelListener, BitmapAlbum bmpalbum1, BitmapAlbum bmpalbum2, BitmapAlbum bmpalbum3) {
        boolean z = cancelBtnText == null && okBtnText == null;
        this.mQuickCancel = z;
        setContentText(contentText);
        setVipImage(bmpalbum1, bmpalbum2, bmpalbum3);
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

    private void setVipImage(BitmapAlbum bmpalbum1, BitmapAlbum bmpalbum2, BitmapAlbum bmpalbum3) {
        Resources resources = AppRuntimeEnv.get().getApplicationContext().getResources();
        Drawable defaultDrawable = ImageCacheUtil.DEFAULT_DRAWABLE;
        this.mCloudView1.setData(bmpalbum1, resources, defaultDrawable);
        this.mCloudView2.setData(bmpalbum2, resources, defaultDrawable);
        this.mCloudView3.setData(bmpalbum3, resources, defaultDrawable);
    }

    public void setVipStyle(boolean isVipStyle) {
        this.mCloudView1.setVipStyle(isVipStyle);
        this.mCloudView2.setVipStyle(isVipStyle);
        this.mCloudView3.setVipStyle(isVipStyle);
        Resources resources = AppRuntimeEnv.get().getApplicationContext().getResources();
        if (isVipStyle) {
            setBackGround(this.mButtonOK, resources.getDrawable(R.drawable.epg_global_dialog_btn_vip_selector));
            setBackGround(this.mButtonCancel, resources.getDrawable(R.drawable.epg_global_dialog_btn_vip_selector));
            setBackGround(this.mDialogTopView, resources.getDrawable(R.drawable.epg_global_dialog_gradient_vip_line));
            return;
        }
        setBackGround(this.mButtonOK, resources.getDrawable(R.drawable.share_global_dialog_btn_selector));
        setBackGround(this.mButtonCancel, resources.getDrawable(R.drawable.share_global_dialog_btn_selector));
        setBackGround(this.mDialogTopView, resources.getDrawable(R.drawable.share_global_dialog_gradient_line));
    }

    public void changeDialogDetailParams() {
        Log.v(TAG, "changeDialogDetailParams");
        LayoutParams layoutParams = (LayoutParams) this.mDialogTopView.getLayoutParams();
        layoutParams.height = getDimen(R.dimen.dimen_2dp);
        this.mDialogTopView.setLayoutParams(layoutParams);
    }

    public void setBackGround(View view, Drawable drawable) {
        if (VERSION.SDK_INT > 15) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof GlobalVipCloudView) {
            if (hasFocus) {
                v.setNextFocusDownId(this.mButtonView);
                this.mFocusCloudViewId = v.getId();
                this.mBtnLine.setVisibility(0);
            }
            ((GlobalVipCloudView) v).onFocusChange(v, hasFocus);
            return;
        }
        if ((v.getId() == R.id.share_dialog_btn2 || v.getId() == R.id.share_dialog_btn1) && hasFocus) {
            v.setNextFocusUpId(this.mFocusCloudViewId);
            this.mButtonView = v.getId();
            this.mBtnLine.setVisibility(8);
        }
        super.onFocusChange(v, hasFocus);
    }
}
