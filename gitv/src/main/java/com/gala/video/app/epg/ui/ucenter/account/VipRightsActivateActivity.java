package com.gala.video.app.epg.ui.ucenter.account;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.feedback.FeedBackActivationStateTimer;
import com.gala.video.app.epg.ui.albumlist.widget.CursorTextView;
import com.gala.video.app.epg.widget.GALAKeyboard;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.IActivationCallback;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Timer;

public class VipRightsActivateActivity extends QMultiScreenActivity implements OnClickListener, OnFocusChangeListener {
    public final String LOG_TAG = "EPG/myaccount/VipRightsActivateActivity";
    private GALAKeyboard mAccountKeyboard;
    private IKeyboardListener mAccountKeyboardInterface = new IKeyboardListener() {
        public void onCommit(String text) {
            VipRightsActivateActivity.this.inputPassword(true);
        }

        public void onTextChange(String keyValue) {
            VipRightsActivateActivity.this.mTxtAccount.setText(keyValue);
        }

        public void onSearchActor(String text) {
        }
    };
    private String mActivationCode;
    private Button mBtnLogin;
    private String mCookice;
    private ImageView mIconErrorBottom;
    private IKeyboardListener mPasswordKeyboardInterface = new IKeyboardListener() {
        public void onCommit(String text) {
            VipRightsActivateActivity.this.activateNow();
        }

        public void onTextChange(String keyValue) {
            VipRightsActivateActivity.this.mTxtPassword.setText(keyValue);
        }

        public void onSearchActor(String text) {
        }
    };
    private String mPreAccountName;
    private ProgressBarNewItem mProgressBar;
    private String mS2;
    public Timer mTimer;
    private TextView mTipBottom;
    private TextView mTipMiddle;
    private TextView mTipTop;
    private CursorTextView mTxtAccount;
    private LinearLayout mTxtAccountContainer;
    private CursorTextView mTxtPassword;
    private LinearLayout mTxtPasswordContainer;
    private boolean mVerifClickable;
    private Bitmap mVerificationBitmap = null;
    private String mVerificationCode;
    private ImageView mVerificationImage;
    private ImageView nIconErrorTop;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_vip_rights_activate_layout);
        preHandleBigPic();
        Intent intent = getIntent();
        if (intent != null) {
            this.mS2 = intent.getStringExtra(LoginConstant.ACTIVATE_S2);
            if (LogUtils.mIsDebug) {
                LogUtils.d("EPG/myaccount/VipRightsActivateActivity", ">>>>> intent.getStringExtra --- s2=", this.mS2);
            }
        }
        initLayout();
        initKeyboard(this.mAccountKeyboard, 0, this.mAccountKeyboardInterface);
        initTxtAccount();
        switchCursor(this.mTxtAccount, false);
        initData();
    }

    private void preHandleBigPic() {
        View container = findViewById(R.id.epg_activate_main);
        Resources resources = getResources();
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.outWidth = 1280;
        options.outHeight = 720;
        SoftReference<Drawable> drawables = new SoftReference(new BitmapDrawable(getResources(), BitmapFactory.decodeStream(resources.openRawResource(R.drawable.epg_login_bg), null, options)));
        if (drawables != null) {
            container.setBackgroundDrawable((Drawable) drawables.get());
        }
    }

    private void initData() {
        this.mCookice = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        if (StringUtils.isEmpty(this.mCookice)) {
            finish();
        }
        this.mVerifClickable = true;
        loadVerificationData();
    }

    private void loadVerificationData() {
        this.mVerificationImage.setImageDrawable(null);
        this.mProgressBar.setVisibility(0);
        loadVerificationBitmap(BOSSHelper.getVerificationCode((int) getResources().getDimension(R.dimen.dimen_133dp), (int) getResources().getDimension(R.dimen.dimen_57dp), this.mCookice));
    }

    private void loadVerificationBitmap(final String url) {
        this.mVerifClickable = false;
        final Bitmap tempBitmap = this.mVerificationBitmap;
        ThreadUtils.execute(new Runnable() {
            public void run() {
                try {
                    Bitmap bitmap = TVApiBase.getTVApiImageTool().downloadImage(url);
                    if (bitmap != null) {
                        LogUtils.i("EPG/myaccount/VipRightsActivateActivity", "onSuccess --- mImageProvider： ", url);
                        VipRightsActivateActivity.this.mVerificationBitmap = bitmap;
                        VipRightsActivateActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    if (VipRightsActivateActivity.this.mVerificationBitmap != null && !VipRightsActivateActivity.this.mVerificationBitmap.isRecycled()) {
                                        LogUtils.d("EPG/myaccount/VipRightsActivateActivity", " --- VerificationTask--- runOnUiThread , mVerificationBitmap = ", VipRightsActivateActivity.this.mVerificationBitmap);
                                        VipRightsActivateActivity.this.mProgressBar.setVisibility(4);
                                        VipRightsActivateActivity.this.mVerificationImage.setImageBitmap(VipRightsActivateActivity.this.mVerificationBitmap);
                                        if (!(tempBitmap == null || tempBitmap.isRecycled())) {
                                            LogUtils.d("EPG/myaccount/VipRightsActivateActivity", " --- VerificationTask--- recycle , tempBitmap = ", tempBitmap);
                                            tempBitmap.recycle();
                                        }
                                        VipRightsActivateActivity.this.mVerifClickable = true;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return;
                    }
                    LogUtils.e("EPG/myaccount/VipRightsActivateActivity", "onFailure --- mImageProvider： ", url);
                    VipRightsActivateActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            VipRightsActivateActivity.this.mProgressBar.setVisibility(4);
                            if (!(tempBitmap == null || tempBitmap.isRecycled())) {
                                LogUtils.d("EPG/myaccount/VipRightsActivateActivity", " --- VerificationTask--- recycle , tempBitmap = ", tempBitmap);
                                tempBitmap.recycle();
                            }
                            VipRightsActivateActivity.this.mVerifClickable = true;
                        }
                    });
                } catch (IOException e1) {
                    LogUtils.e("EPG/myaccount/VipRightsActivateActivity", "IOException --- ", e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initKeyboard(GALAKeyboard keyboard, int i, IKeyboardListener keyboardInterface) {
        keyboard.setKeyListener(keyboardInterface);
        keyboard.initKeyLayout(1);
    }

    private void setTextViewDrawable(TextView tv, int id, int w, int h, int padding) {
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, w, h);
        tv.setCompoundDrawables(d, null, null, null);
        tv.setGravity(17);
        tv.setPadding(padding, 0, padding, 0);
    }

    private void initLayout() {
        this.mBtnLogin = (Button) findViewById(R.id.epg_btn_activate);
        setTextViewDrawable(this.mBtnLogin, R.drawable.epg_ico_login, (int) getResources().getDimension(R.dimen.dimen_40dp), (int) getResources().getDimension(R.dimen.dimen_34dp), (int) getResources().getDimension(R.dimen.dimen_190dp));
        this.mTxtAccount = (CursorTextView) findViewById(R.id.epg_input_login_name);
        this.mTxtPassword = (CursorTextView) findViewById(R.id.epg_input_login_password);
        this.mTxtAccountContainer = (LinearLayout) findViewById(R.id.epg_input_login_name_container);
        this.mTxtPasswordContainer = (LinearLayout) findViewById(R.id.epg_input_login_password_container);
        this.mTipTop = (TextView) findViewById(R.id.epg_txt_tip_top);
        this.mTipMiddle = (TextView) findViewById(R.id.epg_txt_tip_middle);
        this.mTipBottom = (TextView) findViewById(R.id.epg_txt_tip_bottom);
        this.mProgressBar = (ProgressBarNewItem) findViewById(R.id.epg_verification_progressbar);
        this.nIconErrorTop = (ImageView) findViewById(R.id.epg_ico_error_top);
        this.mIconErrorBottom = (ImageView) findViewById(R.id.epg_ico_error_bottom);
        this.mVerificationImage = (ImageView) findViewById(R.id.epg_activate_verification_code);
        this.mAccountKeyboard = (GALAKeyboard) findViewById(R.id.epg_login_keyboard_account);
        this.mTipTop.setVisibility(4);
        this.mTipMiddle.setVisibility(4);
        this.mTipBottom.setVisibility(4);
        this.nIconErrorTop.setVisibility(4);
        this.mIconErrorBottom.setVisibility(4);
        this.mBtnLogin.setOnClickListener(this);
        this.mBtnLogin.setOnFocusChangeListener(this);
        this.mTxtAccount.setOnClickListener(this);
        this.mTxtAccount.setOnFocusChangeListener(this);
        this.mTxtPassword.setOnClickListener(this);
        this.mTxtPassword.setOnFocusChangeListener(this);
        this.mVerificationImage.setOnClickListener(this);
    }

    private void initTxtAccount() {
        this.mAccountKeyboard.updateTextBuffer("");
        this.mTxtAccount.post(new Runnable() {
            public void run() {
                VipRightsActivateActivity.this.mTxtAccount.setText("");
                VipRightsActivateActivity.this.switchCursor(VipRightsActivateActivity.this.mTxtAccount, false);
            }
        });
    }

    private void switchCursor(CursorTextView text, boolean isCommitPress) {
        if (this.mTxtAccount != null) {
            this.mTxtAccount.stopCursor();
        }
        if (this.mTxtPassword != null) {
            this.mTxtPassword.stopCursor();
        }
        text.startCursor(650);
        switchKeyboard(text, isCommitPress);
    }

    public void onClick(View view) {
        int viewID = view.getId();
        if (viewID == R.id.epg_btn_activate) {
            activateNow();
        } else if (viewID == R.id.epg_input_login_name) {
            switchCursor(this.mTxtAccount, false);
        } else if (viewID == R.id.epg_input_login_password) {
            inputPassword(false);
        } else if (viewID == R.id.epg_activate_verification_code && this.mVerifClickable) {
            loadVerificationData();
        }
    }

    private void activateNow() {
        if (checkAccountIsValid() && checkPasswordIsValid()) {
            this.mActivationCode = getAccountTxt();
            this.mVerificationCode = getPasswordTxt();
            sendActivateRequest(this.mActivationCode, this.mVerificationCode);
        }
    }

    private void sendActivateRequest(String activationCode, String verificationCode) {
        GetInterfaceTools.getIGalaAccountManager().buyProductByActivationCodeOTT(activationCode, verificationCode, new IActivationCallback() {
            public void onSuccess(UserResponseBean respBean) {
                VipRightsActivateActivity.this.toastActivationSuccess();
                if (FeedBackActivationStateTimer.get().isRunning()) {
                    FeedBackActivationStateTimer.get().cancel();
                }
                FeedBackActivationStateTimer.get().startFeedbackTask();
                if (respBean.getRespResult()) {
                    LoginPingbackUtils.getInstance().payCode_activateSucc(VipRightsActivateActivity.this.mS2, GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp());
                }
                VipRightsActivateActivity.this.finish();
            }

            public void onException(ApiException exception) {
                LogUtils.e("EPG/myaccount/VipRightsActivateActivity", ">>>>> Exception -- BOSSHelper.buyProductByActivationCodeOTT.call(), errorCode: ", exception.getCode());
                ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(exception.getCode());
                String warning = "";
                if (errorModel != null) {
                    warning = errorModel.getContent();
                } else if (LogUtils.mIsDebug) {
                    LogUtils.e("EPG/myaccount/VipRightsActivateActivity", ">>>>> ErrorCodeModel -- ErrorCodeModel is null !!");
                }
                VipRightsActivateActivity vipRightsActivateActivity = VipRightsActivateActivity.this;
                if (StringUtils.isEmpty((CharSequence) warning)) {
                    warning = VipRightsActivateActivity.this.getString(R.string.update_network_error);
                }
                vipRightsActivateActivity.promptTextDialog(warning);
                if (VipRightsActivateActivity.this.mVerifClickable) {
                    VipRightsActivateActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            VipRightsActivateActivity.this.loadVerificationData();
                        }
                    });
                }
            }
        });
    }

    public void onFocusChange(View view, boolean hasFocus) {
        int viewID = view.getId();
        if (viewID == R.id.epg_btn_activate) {
            if (hasFocus) {
                AnimationUtil.scaleAnimation(view, 1.0f, 1.02f, 200);
            } else {
                AnimationUtil.scaleAnimation(view, 1.02f, 1.0f, 200);
            }
        } else if (viewID == R.id.epg_input_login_name) {
            updateBackground(this.mTxtAccountContainer, hasFocus);
            if (hasFocus) {
                AnimationUtil.scaleAnimation(this.mTxtAccountContainer, 1.0f, 1.02f, 200);
                this.mTxtAccount.setHintTextColor(-921103);
                return;
            }
            AnimationUtil.scaleAnimation(this.mTxtAccountContainer, 1.02f, 1.0f, 200);
            this.mTxtAccount.setHintTextColor(-6710887);
        } else if (viewID == R.id.epg_input_login_password) {
            updateBackground(this.mTxtPasswordContainer, hasFocus);
            if (hasFocus) {
                AnimationUtil.scaleAnimation(this.mTxtPasswordContainer, 1.0f, 1.02f, 200);
                this.mTxtPassword.setHintTextColor(-921103);
                return;
            }
            AnimationUtil.scaleAnimation(this.mTxtPasswordContainer, 1.02f, 1.0f, 200);
            this.mTxtPassword.setHintTextColor(-6710887);
        }
    }

    private void updateBackground(View layout, boolean visible) {
        layout.setBackgroundResource(visible ? R.drawable.share_btn_focus : R.drawable.epg_inputbox_normal);
    }

    private void inputPassword(boolean isCommitPress) {
        if (checkAccountIsValid()) {
            switchCursor(this.mTxtPassword, isCommitPress);
        }
    }

    private boolean checkAccountIsValid() {
        if (StringUtils.isEmpty(getAccountTxt())) {
            showTipText(R.string.vip_rights_input_account);
            switchCursor(this.mTxtAccount, false);
            return false;
        } else if (StringUtils.isEmpty(getAccountTxt().trim())) {
            showTipText(R.string.vip_rights_input_account);
            switchCursor(this.mTxtAccount, false);
            return false;
        } else {
            showTipText(-1);
            if (this.mPreAccountName == null) {
                this.mPreAccountName = getAccountTxt();
            } else if (!this.mPreAccountName.equals(getAccountTxt())) {
                this.mPreAccountName = getAccountTxt();
            }
            return true;
        }
    }

    private boolean checkPasswordIsValid() {
        if (StringUtils.isEmpty(getPasswordTxt())) {
            showTipText(R.string.vip_rights_input_password);
            switchCursor(this.mTxtPassword, false);
            return false;
        }
        showTipText(-1);
        return true;
    }

    private void switchKeyboard(TextView textView, boolean isCommitPress) {
        this.mAccountKeyboard.updateTextBuffer(textView.getText().toString());
        int viewID = textView.getId();
        if (viewID == R.id.epg_input_login_name) {
            this.mAccountKeyboard.setConfirmTextAndDrawable(R.string.OK, 0);
            this.mAccountKeyboard.setKeyListener(this.mAccountKeyboardInterface);
        } else if (viewID == R.id.epg_input_login_password) {
            this.mAccountKeyboard.setConfirmTextAndDrawable(R.string.keyboard_get, 0);
            this.mAccountKeyboard.setKeyListener(this.mPasswordKeyboardInterface);
        }
        if (isCommitPress) {
            this.mAccountKeyboard.restoreFocus(this.mAccountKeyboard.getCommitId());
        } else {
            this.mAccountKeyboard.restoreFocus(101);
        }
    }

    private void showTipText(int strId) {
        this.mTipTop.setVisibility(4);
        this.mTipMiddle.setVisibility(4);
        this.mTipBottom.setVisibility(4);
        this.nIconErrorTop.setVisibility(4);
        this.mIconErrorBottom.setVisibility(4);
        if (strId > 0) {
            TextView text = this.mTipMiddle;
            if (strId == R.string.vip_rights_input_account) {
                text = this.mTipTop;
                this.nIconErrorTop.setVisibility(0);
            } else if (strId == R.string.vip_rights_input_password) {
                text = this.mTipBottom;
                this.mIconErrorBottom.setVisibility(4);
            } else {
                this.nIconErrorTop.setVisibility(0);
                this.mIconErrorBottom.setVisibility(4);
            }
            text.setText(strId);
            text.setVisibility(0);
            AnimationUtil.alphaAnimation(text, 0.0f, 1.0f, 100);
        }
    }

    public void onPause() {
        super.onPause();
    }

    private String getAccountTxt() {
        return this.mTxtAccount.getText().toString();
    }

    private String getPasswordTxt() {
        return this.mTxtPassword.getText().toString();
    }

    private void toastActivationSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                QToast.makeTextAndShow(VipRightsActivateActivity.this, R.string.login_msg_vip_rights_activate_success, 2000);
            }
        });
    }

    private void promptTextDialog(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                GlobalDialog globalDialog = new GlobalDialog(VipRightsActivateActivity.this);
                globalDialog.setParams(str);
                globalDialog.show();
            }
        });
    }
}
