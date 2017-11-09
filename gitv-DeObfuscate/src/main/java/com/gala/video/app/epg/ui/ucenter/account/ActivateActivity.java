package com.gala.video.app.epg.ui.ucenter.account;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.CursorTextView;
import com.gala.video.app.epg.widget.GALAKeyboard;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ItemSize;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.IActivationCallback;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class ActivateActivity extends QMultiScreenActivity implements OnClickListener, OnFocusChangeListener {
    public final String LOG_TAG = "EPG/myaccount/ActivateActivity";
    private GALAKeyboard mAccountKeyboard;
    private IKeyboardListener mAccountKeyboardInterface = new C11414();
    private String mActivationCode;
    private Button mBtnLogin;
    private String mCode;
    private String mCookice;
    private ImageView mIconErrorBottom;
    private IKeyboardListener mPasswordKeyboardInterface = new C11425();
    private String mPreAccountName;
    private ProgressBarNewItem mProgressBar;
    private String mS2;
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

    class C11382 implements Runnable {
        C11382() {
        }

        public void run() {
            if (StringUtils.isEmpty(ActivateActivity.this.mCode)) {
                ActivateActivity.this.mTxtAccount.setText("");
                ActivateActivity.this.switchCursor(ActivateActivity.this.mTxtAccount, false);
                return;
            }
            LogUtils.m1570d("EPG/myaccount/ActivateActivity", ">>>>> code from openAPI: ", ActivateActivity.this.mCode);
            LogUtils.m1570d("EPG/myaccount/ActivateActivity", ">>>>> code from openAPI handled: ", ActivateActivity.this.handleCode(ActivateActivity.this.mCode));
            ActivateActivity.this.mTxtAccount.setText(ActivateActivity.this.handleCode(showOpenapi));
            ActivateActivity.this.switchCursor(ActivateActivity.this.mTxtPassword, false);
        }
    }

    class C11403 implements IActivationCallback {

        class C11391 implements Runnable {
            C11391() {
            }

            public void run() {
                ActivateActivity.this.loadVerificationData();
            }
        }

        C11403() {
        }

        public void onSuccess(UserResponseBean respBean) {
            ActivateActivity.this.toastActivationSuccess();
            if (respBean.getRespResult()) {
                LoginPingbackUtils.getInstance().payCode_activateSucc(ActivateActivity.this.mS2, GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp());
            }
            ActivateActivity.this.finish();
        }

        public void onException(ApiException exception) {
            LogUtils.m1573e("EPG/myaccount/ActivateActivity", ">>>>> Exception -- BOSSHelper.buyProductByActivationCode.call(), errorCode: ", exception.getCode());
            ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(exception.getCode());
            String warning = "";
            if (errorModel != null) {
                warning = errorModel.getContent();
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1571e("EPG/myaccount/ActivateActivity", ">>>>> ErrorCodeModel -- ErrorCodeModel is null !!");
            }
            ActivateActivity activateActivity = ActivateActivity.this;
            if (StringUtils.isEmpty((CharSequence) warning)) {
                warning = ActivateActivity.this.getString(C0508R.string.update_network_error);
            }
            activateActivity.promptTextDialog(warning);
            if (ActivateActivity.this.mVerifClickable) {
                ActivateActivity.this.runOnUiThread(new C11391());
            }
        }
    }

    class C11414 implements IKeyboardListener {
        C11414() {
        }

        public void onCommit(String text) {
            ActivateActivity.this.inputPassword(true);
        }

        public void onTextChange(String keyValue) {
            ActivateActivity.this.mTxtAccount.setText(keyValue);
        }

        public void onSearchActor(String text) {
        }
    }

    class C11425 implements IKeyboardListener {
        C11425() {
        }

        public void onCommit(String text) {
            ActivateActivity.this.activateNow();
        }

        public void onTextChange(String keyValue) {
            ActivateActivity.this.mTxtPassword.setText(keyValue);
        }

        public void onSearchActor(String text) {
        }
    }

    class C11436 implements Runnable {
        C11436() {
        }

        public void run() {
            QToast.makeTextAndShow(ActivateActivity.this, C0508R.string.login_msg_activate_success, 3000);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activate_layout);
        preHandleBigPic();
        Intent intent = getIntent();
        if (intent != null) {
            this.mS2 = intent.getStringExtra(LoginConstant.ACTIVATE_S2);
            this.mCode = intent.getStringExtra(LoginConstant.ACTIVATE_CODE);
            if (LogUtils.mIsDebug) {
                LogUtils.m1570d("EPG/myaccount/ActivateActivity", ">>>>> intent.getStringExtra --- s2=", this.mS2);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1570d("EPG/myaccount/ActivateActivity", ">>>>> intent.getStringExtra --- code=", this.mCode);
            }
        }
        initLayout();
        initKeyboard(this.mAccountKeyboard, 0, this.mAccountKeyboardInterface);
        initTxtAccount();
        if (StringUtils.isEmpty(this.mCode)) {
            switchCursor(this.mTxtAccount, false);
        } else {
            switchCursor(this.mTxtPassword, false);
        }
        initData();
    }

    private void preHandleBigPic() {
        View container = findViewById(C0508R.id.epg_activate_main);
        Resources resources = getResources();
        Options options = new Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.outWidth = 640;
        options.outHeight = ItemSize.ITEM_360;
        SoftReference<Drawable> drawables = new SoftReference(new BitmapDrawable(getResources(), BitmapFactory.decodeStream(resources.openRawResource(C0508R.drawable.epg_login_bg), null, options)));
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
        loadVerificationBitmap(BOSSHelper.getVerificationCode((int) getResources().getDimension(C0508R.dimen.dimen_133dp), (int) getResources().getDimension(C0508R.dimen.dimen_57dp), this.mCookice));
    }

    private void loadVerificationBitmap(final String url) {
        this.mVerifClickable = false;
        final Bitmap tempBitmap = this.mVerificationBitmap;
        ThreadUtils.execute(new Runnable() {

            class C11351 implements Runnable {
                C11351() {
                }

                public void run() {
                    try {
                        if (ActivateActivity.this.mVerificationBitmap != null && !ActivateActivity.this.mVerificationBitmap.isRecycled()) {
                            LogUtils.m1570d("EPG/myaccount/ActivateActivity", " --- VerificationTask--- runOnUiThread , mVerificationBitmap = ", ActivateActivity.this.mVerificationBitmap);
                            ActivateActivity.this.mProgressBar.setVisibility(4);
                            ActivateActivity.this.mVerificationImage.setImageBitmap(ActivateActivity.this.mVerificationBitmap);
                            if (!(tempBitmap == null || tempBitmap.isRecycled())) {
                                LogUtils.m1570d("EPG/myaccount/ActivateActivity", " --- VerificationTask--- recycle , tempBitmap = ", tempBitmap);
                                tempBitmap.recycle();
                            }
                            ActivateActivity.this.mVerifClickable = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            class C11362 implements Runnable {
                C11362() {
                }

                public void run() {
                    ActivateActivity.this.mProgressBar.setVisibility(4);
                    if (!(tempBitmap == null || tempBitmap.isRecycled())) {
                        LogUtils.m1570d("EPG/myaccount/ActivateActivity", " --- VerificationTask--- recycle , tempBitmap = ", tempBitmap);
                        tempBitmap.recycle();
                    }
                    ActivateActivity.this.mVerifClickable = true;
                }
            }

            public void run() {
                try {
                    Bitmap bitmap = TVApiBase.getTVApiImageTool().downloadImage(url);
                    if (bitmap != null) {
                        LogUtils.m1576i("EPG/myaccount/ActivateActivity", "onSuccess --- mImageProvider： ", url);
                        ActivateActivity.this.mVerificationBitmap = bitmap;
                        ActivateActivity.this.runOnUiThread(new C11351());
                        return;
                    }
                    LogUtils.m1573e("EPG/myaccount/ActivateActivity", "onFailure --- mImageProvider： ", url);
                    ActivateActivity.this.runOnUiThread(new C11362());
                } catch (IOException e1) {
                    LogUtils.m1573e("EPG/myaccount/ActivateActivity", "IOException --- ", e1.getMessage());
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
        this.mBtnLogin = (Button) findViewById(C0508R.id.epg_btn_activate);
        setTextViewDrawable(this.mBtnLogin, C0508R.drawable.epg_ico_login, (int) getResources().getDimension(C0508R.dimen.dimen_40dp), (int) getResources().getDimension(C0508R.dimen.dimen_34dp), (int) getResources().getDimension(C0508R.dimen.dimen_190dp));
        this.mTxtAccount = (CursorTextView) findViewById(C0508R.id.epg_input_login_name);
        this.mTxtPassword = (CursorTextView) findViewById(C0508R.id.epg_input_login_password);
        this.mTxtAccountContainer = (LinearLayout) findViewById(C0508R.id.epg_input_login_name_container);
        this.mTxtPasswordContainer = (LinearLayout) findViewById(C0508R.id.epg_input_login_password_container);
        this.mTipTop = (TextView) findViewById(C0508R.id.epg_txt_tip_top);
        this.mTipMiddle = (TextView) findViewById(C0508R.id.epg_txt_tip_middle);
        this.mTipBottom = (TextView) findViewById(C0508R.id.epg_txt_tip_bottom);
        this.mProgressBar = (ProgressBarNewItem) findViewById(C0508R.id.epg_verification_progressbar);
        this.nIconErrorTop = (ImageView) findViewById(C0508R.id.epg_ico_error_top);
        this.mIconErrorBottom = (ImageView) findViewById(C0508R.id.epg_ico_error_bottom);
        this.mVerificationImage = (ImageView) findViewById(C0508R.id.epg_activate_verification_code);
        this.mAccountKeyboard = (GALAKeyboard) findViewById(C0508R.id.epg_login_keyboard_account);
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
        this.mTxtAccount.post(new C11382());
    }

    private String handleCode(String code) {
        int length = code.length();
        StringBuffer strBuff = new StringBuffer("");
        if (length <= 8) {
            return code;
        }
        String pre = code.substring(0, 4);
        String nxt = code.substring(length - 4, length);
        strBuff.append(pre);
        for (int i = 0; i < length - 8; i++) {
            strBuff.append(NetDiagnoseCheckTools.NO_CHECK_FLAG);
        }
        strBuff.append(nxt);
        return strBuff.toString();
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
        if (viewID == C0508R.id.epg_btn_activate) {
            activateNow();
        } else if (viewID == C0508R.id.epg_input_login_name) {
            switchCursor(this.mTxtAccount, false);
        } else if (viewID == C0508R.id.epg_input_login_password) {
            inputPassword(false);
        } else if (viewID == C0508R.id.epg_activate_verification_code && this.mVerifClickable) {
            loadVerificationData();
        }
    }

    private void activateNow() {
        if (checkAccountIsValid() && checkPasswordIsValid()) {
            this.mActivationCode = getAccountTxt();
            this.mVerificationCode = getPasswordTxt();
            if (!StringUtils.isEmpty(this.mCode)) {
                this.mActivationCode = this.mCode;
            }
            LogUtils.m1570d("EPG/myaccount/ActivateActivity", ">>>>> start activate code is: ", this.mActivationCode);
            sendActivateRequest(this.mActivationCode, this.mVerificationCode);
        }
    }

    private void sendActivateRequest(String activationCode, String verificationCode) {
        GetInterfaceTools.getIGalaAccountManager().buyProductByActivationCode(activationCode, verificationCode, new C11403());
    }

    public void onFocusChange(View view, boolean hasFocus) {
        int viewID = view.getId();
        if (viewID == C0508R.id.epg_btn_activate) {
            if (hasFocus) {
                AnimationUtil.scaleAnimation(view, 1.0f, 1.02f, 200);
            } else {
                AnimationUtil.scaleAnimation(view, 1.02f, 1.0f, 200);
            }
        } else if (viewID == C0508R.id.epg_input_login_name) {
            updateBackground(this.mTxtAccountContainer, hasFocus);
            if (hasFocus) {
                AnimationUtil.scaleAnimation(this.mTxtAccountContainer, 1.0f, 1.02f, 200);
                this.mTxtAccount.setHintTextColor(-921103);
                return;
            }
            AnimationUtil.scaleAnimation(this.mTxtAccountContainer, 1.02f, 1.0f, 200);
            this.mTxtAccount.setHintTextColor(-6710887);
        } else if (viewID == C0508R.id.epg_input_login_password) {
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
        layout.setBackgroundResource(visible ? C0508R.drawable.share_btn_focus : C0508R.drawable.epg_inputbox_normal);
    }

    private void inputPassword(boolean isCommitPress) {
        if (checkAccountIsValid()) {
            switchCursor(this.mTxtPassword, isCommitPress);
        }
    }

    private boolean checkAccountIsValid() {
        if (StringUtils.isEmpty(getAccountTxt())) {
            showTipText(C0508R.string.InputAccount);
            switchCursor(this.mTxtAccount, false);
            return false;
        } else if (StringUtils.isEmpty(getAccountTxt().trim())) {
            showTipText(C0508R.string.InputAccount);
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
            showTipText(C0508R.string.InputPassword);
            switchCursor(this.mTxtPassword, false);
            return false;
        }
        showTipText(-1);
        return true;
    }

    private void switchKeyboard(TextView textView, boolean isCommitPress) {
        this.mAccountKeyboard.updateTextBuffer(textView.getText().toString());
        int viewID = textView.getId();
        if (viewID == C0508R.id.epg_input_login_name) {
            this.mAccountKeyboard.setConfirmTextAndDrawable(C0508R.string.OK, 0);
            this.mAccountKeyboard.setKeyListener(this.mAccountKeyboardInterface);
        } else if (viewID == C0508R.id.epg_input_login_password) {
            this.mAccountKeyboard.setConfirmTextAndDrawable(C0508R.string.keyboard_login, 0);
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
            if (strId == C0508R.string.InputAccount) {
                text = this.mTipTop;
                this.nIconErrorTop.setVisibility(0);
            } else if (strId == C0508R.string.InputPassword) {
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
        new Handler(Looper.getMainLooper()).post(new C11436());
    }

    private void promptTextDialog(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                GlobalDialog globalDialog = new GlobalDialog(ActivateActivity.this);
                globalDialog.setParams(str);
                globalDialog.show();
            }
        });
    }
}
