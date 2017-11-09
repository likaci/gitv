package com.gala.video.app.epg.home.ads.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.ads.controller.ExitAppController;
import com.gala.video.app.epg.home.ads.presenter.ExitAppAdPresenter.OnAdImageClickListener;
import com.gala.video.app.epg.home.ads.presenter.ExitOperateImagePresenter.OnOperateImageClickListener;
import com.gala.video.app.epg.home.controller.exit.ExitDialogStatusDispatcher;
import com.gala.video.app.epg.home.data.model.ExitOperateImageModel;
import com.gala.video.app.epg.home.utils.ResourceOperateImageUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.MonkeyUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;

public class GlobalExitAppDialog extends AlertDialog implements OnShowListener {
    private static final String TAG = "ui/widget/GlobalExitAppAdDialog";
    private View mAdImageQrLayout;
    private ImageView mAdImv;
    private TextView mAdLabelTxt;
    private View mContentLayout;
    private Context mContext;
    private ExitAppController mExitAppController;
    private OnFocusChangeListener mLeftBtnFocusListener = new C05894();
    private View mLeftBtnLayout;
    private TextView mLeftBtnTxt;
    private OnAdImageClickListener mOnAdImageClickListener = new C05927();
    private OnOperateImageClickListener mOnOperateImageClickListener = new C05916();
    private TextView mQrDescTxv;
    private ImageView mQrImv;
    private View mQrLayout;
    private TextView mQrTitleTxv;
    private OnFocusChangeListener mRightBtnFocusListener = new C05905();
    private View mRightBtnLayout;
    private TextView mRightBtnTxt;

    class C05894 implements OnFocusChangeListener {
        C05894() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                GlobalExitAppDialog.this.mLeftBtnTxt.setTextColor(GlobalExitAppDialog.this.mContext.getResources().getColor(C0508R.color.dialog_text_color_sel));
            } else {
                GlobalExitAppDialog.this.mLeftBtnTxt.setTextColor(GlobalExitAppDialog.this.mContext.getResources().getColor(C0508R.color.dialog_text_color_unsel));
                GlobalExitAppDialog.this.mExitAppController.setNextFocusDownId(v.getId());
            }
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, true);
        }
    }

    class C05905 implements OnFocusChangeListener {
        C05905() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                GlobalExitAppDialog.this.mRightBtnTxt.setTextColor(GlobalExitAppDialog.this.mContext.getResources().getColor(C0508R.color.dialog_text_color_sel));
            } else {
                GlobalExitAppDialog.this.mRightBtnTxt.setTextColor(GlobalExitAppDialog.this.mContext.getResources().getColor(C0508R.color.dialog_text_color_unsel));
                GlobalExitAppDialog.this.mExitAppController.setNextFocusDownId(v.getId());
            }
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, true);
        }
    }

    class C05916 implements OnOperateImageClickListener {
        C05916() {
        }

        public void onClick(ExitOperateImageModel model) {
            if (model == null) {
                LogUtils.m1577w(GlobalExitAppDialog.TAG, "operate image on click, data is null");
                return;
            }
            PingBackCollectionFieldUtils.setIncomeSrc("others");
            if (ResourceOperateImageUtils.isSupportJump(model.getChannelLabel()) && ResourceOperateImageUtils.isSupportResType(model.getChannelLabel())) {
                GlobalExitAppDialog.this.dismiss();
            }
            GlobalExitAppDialog.this.mExitAppController.sendExitAppPageClickPingback(ScreenSaverPingBack.SEAT_KEY_OK, ResourceOperateImageUtils.getRValue(model.getChannelLabel()), "");
        }
    }

    class C05927 implements OnAdImageClickListener {
        C05927() {
        }

        public void onClick(boolean isEnableJumping) {
            PingBackCollectionFieldUtils.setIncomeSrc("others");
            if (isEnableJumping) {
                GlobalExitAppDialog.this.mExitAppController.sendExitAppPageClickPingback(ScreenSaverPingBack.SEAT_KEY_OK, "", "");
                GlobalExitAppDialog.this.dismiss();
            }
        }
    }

    public GlobalExitAppDialog(Context context) {
        super(context, C0508R.style.Theme_Dialog_Exit_App_Translucent_NoTitle);
        init(context);
    }

    public GlobalExitAppDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public GlobalExitAppDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_global_dialog_exit_app_ad_layout);
        initWindow();
        initView();
        this.mLeftBtnLayout.setOnFocusChangeListener(this.mLeftBtnFocusListener);
        this.mLeftBtnLayout.setNextFocusLeftId(this.mLeftBtnLayout.getId());
        this.mRightBtnLayout.setOnFocusChangeListener(this.mRightBtnFocusListener);
        this.mRightBtnLayout.setNextFocusRightId(this.mRightBtnLayout.getId());
        this.mExitAppController.sendPageShowPingbackOnCreate();
        setOnShowListener(this);
    }

    public void init(Context context) {
        this.mContext = context;
        this.mExitAppController = new ExitAppController(this.mContext);
        this.mExitAppController.setOnOperateImageClickListener(this.mOnOperateImageClickListener);
        this.mExitAppController.setOnAdImageClickListener(this.mOnAdImageClickListener);
    }

    private void initView() {
        this.mAdImv = (ImageView) findViewById(C0508R.id.epg_exit_app_ad_imv_image);
        this.mQrImv = (ImageView) findViewById(C0508R.id.epg_exit_app_ad_imv_qr);
        this.mAdLabelTxt = (TextView) findViewById(C0508R.id.epg_exit_app_ad_txv_ad_label);
        this.mQrTitleTxv = (TextView) findViewById(C0508R.id.epg_exit_app_ad_txv_qr_title);
        this.mQrDescTxv = (TextView) findViewById(C0508R.id.epg_exit_app_ad_txv_qr_desc);
        this.mAdImageQrLayout = findViewById(C0508R.id.epg_exit_app_ad_layout_image_qr);
        this.mQrLayout = findViewById(C0508R.id.epg_exit_app_ad_layout_qr);
        this.mLeftBtnLayout = findViewById(C0508R.id.epg_exit_app_ad_layout_btn_left);
        this.mContentLayout = findViewById(C0508R.id.epg_exit_app_layout_content);
        this.mRightBtnLayout = findViewById(C0508R.id.epg_exit_app_ad_layout_btn_right);
        this.mLeftBtnTxt = (TextView) findViewById(C0508R.id.epg_exit_app_ad_txt_left);
        this.mRightBtnTxt = (TextView) findViewById(C0508R.id.epg_exit_app_ad_txt_right);
    }

    private void initWindow() {
        getWindow().setLayout(-1, -1);
        getWindow().setAttributes(getWindow().getAttributes());
        getWindow().setBackgroundDrawable(this.mContext.getResources().getDrawable(C0508R.color.exit_app_dialog_background_color));
    }

    public void setOnBtnClickListener(final OnClickListener leftListener, final OnClickListener rightListener) {
        if (this.mLeftBtnLayout == null || this.mRightBtnLayout == null) {
            show();
        }
        this.mLeftBtnLayout.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 || keyEvent.getKeyCode() == 23) {
                    if (keyEvent.getAction() == 1) {
                        LogUtils.m1568d(GlobalExitAppDialog.TAG, "exit app");
                        if (Project.getInstance().getBuild().isSupportMonkeyTest() && MonkeyUtils.isMonkeyRunning()) {
                            LogUtils.m1568d(GlobalExitAppDialog.TAG, "must not exit app, current status is running monkey");
                            return true;
                        }
                        leftListener.onClick(GlobalExitAppDialog.this.mLeftBtnLayout);
                        GlobalExitAppDialog.this.dismiss();
                    } else {
                        LogUtils.m1568d(GlobalExitAppDialog.TAG, "exit app for pingback");
                        PingBackParams params = new PingBackParams();
                        params.add(Keys.f2035T, "14").add("r", Values.value00001);
                        PingBack.getInstance().postPingBackToLongYuan(params.build());
                        GlobalExitAppDialog.this.onExitDialogOk();
                    }
                }
                return false;
            }
        });
        this.mLeftBtnLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LogUtils.m1568d(GlobalExitAppDialog.TAG, "exit app onclick");
                PingBackParams params = new PingBackParams();
                params.add(Keys.f2035T, "14").add("r", Values.value00001);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                GlobalExitAppDialog.this.onExitDialogOk();
                leftListener.onClick(GlobalExitAppDialog.this.mLeftBtnLayout);
                GlobalExitAppDialog.this.dismiss();
            }
        });
        this.mRightBtnLayout.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 || keyEvent.getKeyCode() == 23) {
                    if (keyEvent.getAction() == 1) {
                        rightListener.onClick(GlobalExitAppDialog.this.mRightBtnLayout);
                        GlobalExitAppDialog.this.dismiss();
                        return true;
                    }
                    GlobalExitAppDialog.this.onExitDialogCanceled();
                }
                return false;
            }
        });
    }

    private void onExitDialogOk() {
        this.mExitAppController.sendExitAppPageClickPingback("exit", "", "");
    }

    private void onExitDialogCanceled() {
        this.mExitAppController.sendExitAppPageClickPingback("wait", "", "");
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.mExitAppController.sendExitAppPageClickPingback("back", "", "");
    }

    public void dismiss() {
        super.dismiss();
        ExitDialogStatusDispatcher.get().onDismiss();
    }

    public void onShow(DialogInterface dialog) {
        ExitDialogStatusDispatcher.get().onShow();
        LogUtils.m1568d(TAG, "exit dialog show ");
        this.mLeftBtnLayout.requestFocus();
        this.mExitAppController.setWidgets(this.mContentLayout, this.mAdImageQrLayout, this.mQrLayout, this.mAdImv, this.mQrImv, this.mQrTitleTxv, this.mQrDescTxv, this.mAdLabelTxt, this.mLeftBtnLayout, this.mRightBtnLayout);
        this.mExitAppController.show();
    }
}
