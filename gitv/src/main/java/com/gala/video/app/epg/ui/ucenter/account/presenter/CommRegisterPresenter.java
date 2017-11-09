package com.gala.video.app.epg.ui.ucenter.account.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.ucenter.account.ui.IRegisterView;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.AccountLoginHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.io.IOException;

public class CommRegisterPresenter extends CommBasePresenter {
    private final String LOG_TAG = "EPG/MyAccount/CommRegisterPresenter";
    private final int MESSAGE_TIME_60 = 60;
    private Context mContext;
    private Handler mHandler;
    private int mLastTime = -1;
    private boolean mMessageClickable;
    private IKeyboardListener mMessageKeyboardListener = new IKeyboardListener() {
        public void onTextChange(String text) {
            CommRegisterPresenter.this.mRegisterView.setMessageCode(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            CommRegisterPresenter.this.callRegisterRequest();
        }
    };
    private IKeyboardListener mPasswordKeyboardListenr = new IKeyboardListener() {
        public void onTextChange(String text) {
            CommRegisterPresenter.this.mRegisterView.setPassword(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            CommRegisterPresenter.this.switchInputVerifyCode();
        }
    };
    private String mPingback_s1;
    private IRegisterView mRegisterView;
    private Runnable mTimeRunnable = new Runnable() {
        public void run() {
            CommRegisterPresenter.this.mLastTime = CommRegisterPresenter.this.mLastTime + 1;
            if (CommRegisterPresenter.this.mLastTime >= 60) {
                CommRegisterPresenter.this.mLastTime = 0;
                CommRegisterPresenter.this.mRegisterView.setMessageBtnTxt("获取验证码");
                CommRegisterPresenter.this.mMessageClickable = true;
                CommRegisterPresenter.this.mHandler.post(new Runnable() {
                    public void run() {
                        CommRegisterPresenter.this.loadVerifyCode();
                    }
                });
                return;
            }
            String time = String.valueOf(60 - CommRegisterPresenter.this.mLastTime);
            Resources resources = CommRegisterPresenter.this.mContext.getResources();
            int i = R.string.comm_regist_timetip;
            Object[] objArr = new Object[1];
            if (StringUtils.isEmpty((CharSequence) time)) {
                time = "";
            }
            objArr[0] = time;
            CommRegisterPresenter.this.mRegisterView.setMessageBtnTxt(resources.getString(i, objArr));
            CommRegisterPresenter.this.mHandler.postDelayed(CommRegisterPresenter.this.mTimeRunnable, 1000);
        }
    };
    private boolean mVerifClickable;
    private IKeyboardListener mVerifyKeyboardListener = new IKeyboardListener() {
        public void onTextChange(String text) {
            CommRegisterPresenter.this.mRegisterView.setVerifyCode(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            CommRegisterPresenter.this.switchInputMessageCode();
        }
    };

    public CommRegisterPresenter(IRegisterView registerView) {
        super(registerView);
        init(registerView);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public CommRegisterPresenter(IRegisterView registerView, Handler handler) {
        super(registerView);
        init(registerView);
        this.mHandler = handler;
    }

    private void init(IRegisterView registerView) {
        this.mRegisterView = registerView;
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mVerifClickable = true;
        this.mMessageClickable = true;
    }

    public void initGALAKeyboard() {
        this.mRegisterView.registerKeyboardListener(this.mAccountKeyboardListener);
        this.mRegisterView.initKeyboardLayout(1, R.id.epg_btn_regist);
    }

    public void switchInputAccount() {
        this.mRegisterView.hidePasswordErrorTipUI();
        this.mRegisterView.hideVerifycodeErrorTipUI();
        this.mRegisterView.hideMessagecodeErrorTipUI();
        this.mRegisterView.stopAccountCursor();
        this.mRegisterView.stopPasswordCursor();
        this.mRegisterView.stopVerifyCursor();
        this.mRegisterView.stopMessageCursor();
        this.mRegisterView.startAccountCursor(650);
        this.mRegisterView.updateTextBuffer(this.mRegisterView.getAccount());
        this.mRegisterView.changeConfirmTextAndDrawable(R.string.OK, 0);
        this.mRegisterView.registerKeyboardListener(this.mAccountKeyboardListener);
    }

    public void switchInputPassword() {
        if (checkAccountAvailability()) {
            this.mRegisterView.hideAccountErrorTipUI();
            this.mRegisterView.hideVerifycodeErrorTipUI();
            this.mRegisterView.hideMessagecodeErrorTipUI();
            this.mRegisterView.stopAccountCursor();
            this.mRegisterView.stopPasswordCursor();
            this.mRegisterView.stopVerifyCursor();
            this.mRegisterView.stopMessageCursor();
            this.mRegisterView.startPasswordCursor(650);
            this.mRegisterView.updateTextBuffer(this.mRegisterView.getPassword());
            this.mRegisterView.changeConfirmTextAndDrawable(R.string.OK, 0);
            this.mRegisterView.registerKeyboardListener(this.mPasswordKeyboardListenr);
        }
    }

    public void switchInputVerifyCode() {
        if (checkAccountAvailability() && checkPasswordAvailability()) {
            this.mRegisterView.hideAccountErrorTipUI();
            this.mRegisterView.hidePasswordErrorTipUI();
            this.mRegisterView.hideMessagecodeErrorTipUI();
            this.mRegisterView.stopAccountCursor();
            this.mRegisterView.stopPasswordCursor();
            this.mRegisterView.stopVerifyCursor();
            this.mRegisterView.stopMessageCursor();
            this.mRegisterView.startVerifyCursor(650);
            this.mRegisterView.updateTextBuffer(this.mRegisterView.getVerifyCode());
            this.mRegisterView.changeConfirmTextAndDrawable(R.string.OK, 0);
            this.mRegisterView.registerKeyboardListener(this.mVerifyKeyboardListener);
        }
    }

    public void switchInputMessageCode() {
        if (checkAccountAvailability() && checkPasswordAvailability() && checkVerifycodeAvailability()) {
            this.mRegisterView.hideAccountErrorTipUI();
            this.mRegisterView.hidePasswordErrorTipUI();
            this.mRegisterView.hideVerifycodeErrorTipUI();
            this.mRegisterView.stopAccountCursor();
            this.mRegisterView.stopPasswordCursor();
            this.mRegisterView.stopVerifyCursor();
            this.mRegisterView.stopMessageCursor();
            this.mRegisterView.startMessageCursor(650);
            this.mRegisterView.updateTextBuffer(this.mRegisterView.getMessageCode());
            this.mRegisterView.changeConfirmTextAndDrawable(R.string.Register, 0);
            this.mRegisterView.registerKeyboardListener(this.mMessageKeyboardListener);
        }
    }

    public void callRegisterRequest() {
        if (checkAccountAvailability() && checkPasswordAvailability() && checkVerifycodeAvailability() && checkMessagecodeAvailability()) {
            this.mRegisterView.hideAccountErrorTipUI();
            this.mRegisterView.hidePasswordErrorTipUI();
            this.mRegisterView.hideVerifycodeErrorTipUI();
            this.mRegisterView.hideMessagecodeErrorTipUI();
            final String phoneNum = this.mRegisterView.getAccount();
            final String password = this.mRegisterView.getPassword();
            final String mesCode = this.mRegisterView.getMessageCode();
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    UserResponseBean respBean = GetInterfaceTools.getIGalaAccountManager().registerByInput(phoneNum, password, mesCode, new ILoginCallback() {
                        public void onLoginSuccess(UserInfoBean bean) {
                            LoginPingbackUtils.getInstance().logSucc(LoginConstant.A_REGISTER_KEYBOARD, CommRegisterPresenter.this.mPingback_s1);
                            CommRegisterPresenter.this.gotoMyCenterFragment(bean);
                        }

                        public void onLoginFail(final ApiException exception) {
                            if (exception != null) {
                                LogUtils.e("EPG/MyAccount/CommRegisterPresenter", ">>>>> Exception -- PassportTVHelper.registerByPhoneNew.call(), errorCode: ", exception.getCode());
                                ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(errorCode);
                                String warning = "";
                                if (errorModel != null) {
                                    warning = errorModel.getContent();
                                } else if (LogUtils.mIsDebug) {
                                    LogUtils.e("EPG/MyAccount/CommRegisterPresenter", ">>>>> ErrorCodeModel -- ErrorCodeModel is null !!");
                                }
                                final String tip = warning;
                                if (CommRegisterPresenter.this.mHandler != null && CommRegisterPresenter.this.mContext != null) {
                                    CommRegisterPresenter.this.mHandler.post(new Runnable() {
                                        public void run() {
                                            QToast.makeTextAndShow(CommRegisterPresenter.this.mContext, StringUtils.isEmpty(tip) ? AccountLoginHelper.getCommErrorTip(exception) : tip, (int) QToast.LENGTH_LONG);
                                            CommRegisterPresenter.this.loadVerifyCode();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private boolean checkMessagecodeAvailability() {
        String messagecode = this.mRegisterView.getMessageCode();
        if (messagecode == null || !StringUtils.isEmpty(messagecode.trim())) {
            return true;
        }
        this.mRegisterView.showMessagecodeErrorTipUI(false, R.string.InputMessagecode);
        switchInputMessageCode();
        return false;
    }

    public void loadVerifyCode() {
        if (this.mVerifClickable) {
            this.mVerifClickable = false;
            this.mRegisterView.setVerifyBitmap(null);
            this.mRegisterView.showProgressBar();
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    try {
                        final Bitmap bitmap = TVApiBase.getTVApiImageTool().downloadImage(PassportTVHelper.getRegisterEMailVCode((int) CommRegisterPresenter.this.mContext.getResources().getDimension(R.dimen.dimen_60dp), (int) CommRegisterPresenter.this.mContext.getResources().getDimension(R.dimen.dimen_129dp), DeviceUtils.getMacAddr()));
                        if (CommRegisterPresenter.this.mHandler != null && CommRegisterPresenter.this.mRegisterView != null) {
                            CommRegisterPresenter.this.mHandler.post(new Runnable() {
                                public void run() {
                                    CommRegisterPresenter.this.mRegisterView.hideProgressBar();
                                    if (bitmap != null) {
                                        CommRegisterPresenter.this.mRegisterView.setVerifyBitmap(bitmap);
                                        return;
                                    }
                                    if (CommRegisterPresenter.this.mContext != null) {
                                        CommRegisterPresenter.this.mRegisterView.setVerifyBitmap(BitmapFactory.decodeResource(CommRegisterPresenter.this.mContext.getResources(), R.drawable.epg_verify_img_default));
                                    }
                                    LogUtils.e("EPG/MyAccount/CommRegisterPresenter", ">>>>> verifycode bitmap is null");
                                }
                            });
                            CommRegisterPresenter.this.mVerifClickable = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void sendMessage() {
        if (!checkAccountAvailability() || !checkPasswordAvailability() || !checkVerifycodeAvailability()) {
            return;
        }
        if (this.mMessageClickable) {
            String cellPhone = this.mRegisterView.getAccount();
            String verifyCode = this.mRegisterView.getVerifyCode();
            PassportTVHelper.checkSendPhoneCodeWithVCode.call(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode code) {
                    CommRegisterPresenter.this.mMessageClickable = false;
                    CommRegisterPresenter.this.mHandler.post(CommRegisterPresenter.this.mTimeRunnable);
                }

                public void onException(ApiException exception) {
                    LoginPingbackUtils.getInstance().errorPingback("tvsignup", exception != null ? exception.getCode() : "", "PassportTVHelper.checkSendPhoneCodeWithVCode", exception);
                    ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(exception.getCode());
                    String warning = "";
                    if (errorModel != null) {
                        warning = errorModel.getContent();
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e("EPG/MyAccount/CommRegisterPresenter", ">>>>> ErrorCodeModel -- ErrorCodeModel is null !!");
                    }
                    final String tip = warning;
                    if (CommRegisterPresenter.this.mHandler != null && CommRegisterPresenter.this.mContext != null) {
                        CommRegisterPresenter.this.mHandler.post(new Runnable() {
                            public void run() {
                                QToast.makeTextAndShow(CommRegisterPresenter.this.mContext, StringUtils.isEmpty(tip) ? ResourceUtil.getStr(R.string.register_error_code) : tip, (int) QToast.LENGTH_LONG);
                                CommRegisterPresenter.this.loadVerifyCode();
                            }
                        });
                    }
                }
            }, "1", cellPhone, verifyCode, DeviceUtils.getMacAddr());
            return;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                QToast.makeTextAndShow(CommRegisterPresenter.this.mContext, R.string.register_later, 3000);
            }
        });
    }

    public void sendDisplayPingback(String qtcurl, String block, String s1) {
        LoginPingbackUtils.getInstance().pageDisplay(qtcurl, block, false, s1);
    }

    public void setPingback_s1(String s1) {
        this.mPingback_s1 = s1;
    }
}
