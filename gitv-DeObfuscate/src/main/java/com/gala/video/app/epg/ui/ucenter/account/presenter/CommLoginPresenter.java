package com.gala.video.app.epg.ui.ucenter.account.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.ucenter.account.ui.IBaseLoginView;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.AccountLoginHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;
import java.io.IOException;

public class CommLoginPresenter extends CommBasePresenter {
    private final String LOG_TAG = "EPG/MyAccount/CommLoginPresenter";
    private Context mContext;
    private Handler mHandler;
    private IBaseLoginView mLoginView;
    private IKeyboardListener mPasswordKeyboardListenr = new C11613();
    private String mPingback_s1;
    private boolean mVerifClickable;
    private IKeyboardListener mVerifyKeyboardListener = new C11602();

    class C11602 implements IKeyboardListener {
        C11602() {
        }

        public void onTextChange(String text) {
            CommLoginPresenter.this.mLoginView.setVerifyCode(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            CommLoginPresenter.this.callLoginRequest();
        }
    }

    class C11613 implements IKeyboardListener {
        C11613() {
        }

        public void onTextChange(String text) {
            CommLoginPresenter.this.mLoginView.setPassword(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            if (CommLoginPresenter.this.mLoginView.isVerifyVisible()) {
                CommLoginPresenter.this.switchInputVerifyCode();
            } else {
                CommLoginPresenter.this.callLoginRequest();
            }
        }
    }

    class C11634 implements Runnable {
        C11634() {
        }

        public void run() {
            try {
                final Bitmap bitmap = TVApiBase.getTVApiImageTool().downloadImage(PassportTVHelper.getRegisterEMailVCode((int) CommLoginPresenter.this.mContext.getResources().getDimension(C0508R.dimen.dimen_60dp), (int) CommLoginPresenter.this.mContext.getResources().getDimension(C0508R.dimen.dimen_129dp), DeviceUtils.getMacAddr()));
                if (CommLoginPresenter.this.mHandler != null && CommLoginPresenter.this.mLoginView != null) {
                    CommLoginPresenter.this.mHandler.post(new Runnable() {
                        public void run() {
                            CommLoginPresenter.this.mLoginView.hideProgressBar();
                            if (bitmap != null) {
                                CommLoginPresenter.this.mLoginView.setVerifyBitmap(bitmap);
                                return;
                            }
                            if (CommLoginPresenter.this.mContext != null) {
                                CommLoginPresenter.this.mLoginView.setVerifyBitmap(BitmapFactory.decodeResource(CommLoginPresenter.this.mContext.getResources(), C0508R.drawable.epg_verify_img_default));
                            }
                            LogUtils.m1571e("EPG/MyAccount/CommLoginPresenter", ">>>>> verifycode bitmap is null");
                        }
                    });
                    CommLoginPresenter.this.mVerifClickable = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CommLoginPresenter(IBaseLoginView loginView) {
        super(loginView);
        init(loginView);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    private void init(IBaseLoginView loginView) {
        this.mLoginView = loginView;
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mVerifClickable = true;
    }

    public CommLoginPresenter(IBaseLoginView loginView, Handler handler) {
        super(loginView);
        init(loginView);
        this.mHandler = handler;
    }

    public void initGALAKeyboard() {
        this.mLoginView.registerKeyboardListener(this.mAccountKeyboardListener);
        this.mLoginView.initKeyboardLayout(1, C0508R.id.epg_btn_login);
    }

    public void switchInputAccount() {
        this.mLoginView.hidePasswordErrorTipUI();
        this.mLoginView.hideVerifycodeErrorTipUI();
        this.mLoginView.stopAccountCursor();
        this.mLoginView.stopPasswordCursor();
        this.mLoginView.stopVerifyCursor();
        this.mLoginView.startAccountCursor(650);
        this.mLoginView.updateTextBuffer(this.mLoginView.getAccount());
        this.mLoginView.changeConfirmTextAndDrawable(C0508R.string.OK, 0);
        this.mLoginView.registerKeyboardListener(this.mAccountKeyboardListener);
    }

    public void switchInputPassword() {
        if (checkAccountAvailability()) {
            this.mLoginView.hideAccountErrorTipUI();
            this.mLoginView.hideVerifycodeErrorTipUI();
            this.mLoginView.stopAccountCursor();
            this.mLoginView.stopPasswordCursor();
            this.mLoginView.stopVerifyCursor();
            this.mLoginView.startPasswordCursor(650);
            this.mLoginView.updateTextBuffer(this.mLoginView.getPassword());
            this.mLoginView.changeConfirmTextAndDrawable(this.mLoginView.isVerifyVisible() ? C0508R.string.OK : C0508R.string.Login, 0);
            this.mLoginView.registerKeyboardListener(this.mPasswordKeyboardListenr);
        }
    }

    public void switchInputVerifyCode() {
        if (checkAccountAvailability() && checkPasswordAvailability()) {
            this.mLoginView.hideAccountErrorTipUI();
            this.mLoginView.hidePasswordErrorTipUI();
            this.mLoginView.stopAccountCursor();
            this.mLoginView.stopPasswordCursor();
            this.mLoginView.stopVerifyCursor();
            this.mLoginView.startVerifyCursor(650);
            this.mLoginView.updateTextBuffer(this.mLoginView.getVerifyCode());
            this.mLoginView.changeConfirmTextAndDrawable(C0508R.string.Login, 0);
            this.mLoginView.registerKeyboardListener(this.mVerifyKeyboardListener);
        }
    }

    public void callLoginRequest() {
        if (!checkAccountAvailability() || !checkPasswordAvailability()) {
            return;
        }
        if ((this.mLoginView.isVerifyVisible() && checkVerifycodeAvailability()) || !this.mLoginView.isVerifyVisible()) {
            this.mLoginView.hideAccountErrorTipUI();
            this.mLoginView.hidePasswordErrorTipUI();
            this.mLoginView.hideVerifycodeErrorTipUI();
            final String account = this.mLoginView.getAccount();
            final String password = this.mLoginView.getPassword();
            final String code = this.mLoginView.getVerifyCode();
            ThreadUtils.execute(new Runnable() {

                class C11581 implements ILoginCallback {
                    C11581() {
                    }

                    public void onLoginSuccess(UserInfoBean bean) {
                        LogUtils.m1568d("pingback test", ">>>>> login success by keyboard pingback");
                        LoginPingbackUtils.getInstance().logSucc("7", CommLoginPresenter.this.mPingback_s1);
                        CommLoginPresenter.this.gotoMyCenterFragment(bean);
                    }

                    public void onLoginFail(final ApiException exception) {
                        if (exception != null) {
                            final String errorCode = exception.getCode();
                            ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(errorCode);
                            String warning = "";
                            if (errorModel != null) {
                                warning = errorModel.getContent();
                            } else if (LogUtils.mIsDebug) {
                                LogUtils.m1571e("EPG/MyAccount/CommLoginPresenter", ">>>>> ErrorCodeModel -- ErrorCodeModel is null !!");
                            }
                            final String tip = warning;
                            if (CommLoginPresenter.this.mHandler != null && CommLoginPresenter.this.mContext != null) {
                                CommLoginPresenter.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        QToast.makeTextAndShow(CommLoginPresenter.this.mContext, StringUtils.isEmpty(tip) ? AccountLoginHelper.getCommErrorTip(exception) : tip, (int) QToast.LENGTH_LONG);
                                        if ("P00107".equals(errorCode)) {
                                            CommLoginPresenter.this.mLoginView.showVerifyCodeUI();
                                            CommLoginPresenter.this.mLoginView.changeConfirmTextAndDrawable(CommLoginPresenter.this.mLoginView.isVerifyVisible() ? C0508R.string.OK : C0508R.string.Login, 0);
                                        }
                                        CommLoginPresenter.this.loadVerifyCode();
                                    }
                                });
                            }
                        }
                    }
                }

                public void run() {
                    GetInterfaceTools.getIGalaAccountManager().loginByKeyInput(account, password, code, new C11581());
                }
            });
        }
    }

    public void loadVerifyCode() {
        if (this.mVerifClickable && this.mLoginView.isVerifyVisible()) {
            this.mVerifClickable = false;
            this.mLoginView.setVerifyBitmap(null);
            this.mLoginView.showProgressBar();
            ThreadUtils.execute(new C11634());
        }
    }

    public void sendDisplayPingback(String qtcurl, String block) {
        LoginPingbackUtils.getInstance().pageDisplay(qtcurl, block, false, this.mPingback_s1);
    }

    public void setPingback_s1(String s1) {
        this.mPingback_s1 = s1;
    }
}
