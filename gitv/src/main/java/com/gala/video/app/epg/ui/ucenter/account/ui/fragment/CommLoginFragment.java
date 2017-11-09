package com.gala.video.app.epg.ui.ucenter.account.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.widget.CursorTextView;
import com.gala.video.app.epg.ui.ucenter.account.presenter.CommLoginPresenter;
import com.gala.video.app.epg.ui.ucenter.account.ui.IBaseLoginView;
import com.gala.video.app.epg.widget.GALAKeyboard;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.utils.AnimationUtil;

public class CommLoginFragment extends BaseLoginFragment implements OnClickListener, OnFocusChangeListener, IBaseLoginView {
    private CursorTextView mAccountCursor;
    private View mAccountErrorView;
    private TextView mAccountTab;
    private View mAccountView;
    private TextView mChangeTab;
    private View mChangeTabView;
    private GALAKeyboard mGALAKeyboard;
    private int mIntentFlag;
    private Button mLoginButton;
    private CommLoginPresenter mLoginPresenter;
    private View mMainView;
    private CursorTextView mPasswordCursor;
    private View mPasswordErrorView;
    private TextView mPasswordTab;
    private View mPasswordView;
    private ProgressBarNewItem mProgressBar;
    private CursorTextView mVerifyCursor;
    private View mVerifyErrorView;
    private ImageView mVerifyImage;
    private TextView mVerifyTab;
    private View mVerifyView;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mLoginEvent != null) {
            this.mLoginPresenter = new CommLoginPresenter(this, this.mHandler);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.mIntentFlag = bundle.getInt(LoginConstant.LOGIN_SUCC_TO, -1);
        }
    }

    @SuppressLint({"InflateParams"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_comm_login, null);
        findViewById();
        initCommUIData();
        initFocusForward();
        registerListener();
        if (this.mLoginPresenter != null) {
            this.mLoginPresenter.setPingback_s1(this.mS1);
            this.mLoginPresenter.initGALAKeyboard();
            this.mLoginPresenter.initAccountCursor();
            this.mLoginPresenter.sendDisplayPingback("account", "tvlogin");
        }
        return this.mMainView;
    }

    private void registerListener() {
        this.mChangeTabView.setOnFocusChangeListener(this);
        this.mAccountView.setOnFocusChangeListener(this);
        this.mPasswordView.setOnFocusChangeListener(this);
        this.mVerifyView.setOnFocusChangeListener(this);
        this.mVerifyImage.setOnFocusChangeListener(this);
        this.mLoginButton.setOnFocusChangeListener(this);
        this.mChangeTabView.setOnClickListener(this);
        this.mAccountView.setOnClickListener(this);
        this.mPasswordView.setOnClickListener(this);
        this.mVerifyView.setOnClickListener(this);
        this.mLoginButton.setOnClickListener(this);
        this.mVerifyImage.setOnClickListener(this);
    }

    private void initFocusForward() {
        this.mAccountView.setNextFocusLeftId(this.mAccountView.getId());
        this.mAccountView.setNextFocusRightId(this.mChangeTabView.getId());
        this.mAccountView.setNextFocusUpId(this.mAccountView.getId());
        this.mAccountView.setNextFocusDownId(this.mPasswordView.getId());
        this.mPasswordView.setNextFocusLeftId(this.mPasswordView.getId());
        this.mPasswordView.setNextFocusRightId(this.mChangeTabView.getId());
        this.mPasswordView.setNextFocusUpId(this.mAccountView.getId());
        this.mPasswordView.setNextFocusDownId(this.mLoginButton.getId());
        this.mVerifyView.setNextFocusLeftId(this.mVerifyView.getId());
        this.mVerifyView.setNextFocusRightId(this.mVerifyImage.getId());
        this.mVerifyView.setNextFocusUpId(this.mPasswordView.getId());
        this.mVerifyView.setNextFocusDownId(this.mLoginButton.getId());
        this.mVerifyImage.setNextFocusUpId(this.mPasswordView.getId());
        this.mVerifyImage.setNextFocusLeftId(this.mVerifyView.getId());
        this.mVerifyImage.setNextFocusRightId(this.mChangeTabView.getId());
        this.mVerifyImage.setNextFocusDownId(this.mLoginButton.getId());
        this.mChangeTabView.setNextFocusDownId(this.mChangeTabView.getId());
        this.mChangeTabView.setNextFocusRightId(this.mChangeTabView.getId());
        this.mChangeTabView.setNextFocusUpId(this.mChangeTabView.getId());
    }

    private void initCommUIData() {
        ((TextView) this.mChangeTabView.findViewById(R.id.epg_text_change_tab)).setText(getStr(R.string.RegisterTip));
        this.mAccountTab.setText(getStr(R.string.Account));
        this.mPasswordTab.setText(getStr(R.string.Password));
        this.mVerifyTab.setText(getStr(R.string.Verifycode));
        this.mAccountCursor.setHint(getStr(R.string.InputAccountHint1));
        this.mPasswordCursor.setHint(getStr(R.string.InputPasswordHint1));
        this.mVerifyCursor.setHint(getStr(R.string.InputVerifycodeHint1));
        this.mAccountCursor.setHintTextColor(getColor(R.color.hot_search));
        this.mPasswordCursor.setHintTextColor(getColor(R.color.hot_search));
        this.mVerifyCursor.setHintTextColor(getColor(R.color.hot_search));
        this.mAccountErrorView.setVisibility(4);
        this.mPasswordErrorView.setVisibility(4);
        this.mVerifyErrorView.setVisibility(4);
        this.mVerifyView.setVisibility(4);
        this.mVerifyImage.setVisibility(4);
        this.mAccountCursor.setTransformationMethod(null);
        this.mVerifyCursor.setTransformationMethod(null);
    }

    private void findViewById() {
        this.mChangeTabView = this.mMainView.findViewById(R.id.epg_view_change_tab);
        this.mAccountView = this.mMainView.findViewById(R.id.epg_input_username);
        this.mPasswordView = this.mMainView.findViewById(R.id.epg_input_password);
        this.mVerifyView = this.mMainView.findViewById(R.id.epg_input_verification_code);
        this.mAccountErrorView = this.mMainView.findViewById(R.id.epg_error_account);
        this.mPasswordErrorView = this.mMainView.findViewById(R.id.epg_error_password);
        this.mVerifyErrorView = this.mMainView.findViewById(R.id.epg_error_verifycode);
        this.mLoginButton = (Button) this.mMainView.findViewById(R.id.epg_btn_login);
        this.mVerifyImage = (ImageView) this.mMainView.findViewById(R.id.epg_image_verify);
        this.mGALAKeyboard = (GALAKeyboard) this.mMainView.findViewById(R.id.epg_keyboard_comm_login);
        this.mAccountTab = (TextView) this.mAccountView.findViewById(R.id.epg_inputbox_tab);
        this.mPasswordTab = (TextView) this.mPasswordView.findViewById(R.id.epg_inputbox_tab);
        this.mVerifyTab = (TextView) this.mVerifyView.findViewById(R.id.epg_inputbox_tab);
        this.mChangeTab = (TextView) this.mChangeTabView.findViewById(R.id.epg_text_change_tab);
        this.mAccountCursor = (CursorTextView) this.mAccountView.findViewById(R.id.epg_inputbox_cursor);
        this.mVerifyCursor = (CursorTextView) this.mVerifyView.findViewById(R.id.epg_inputbox_cursor);
        this.mPasswordCursor = (CursorTextView) this.mPasswordView.findViewById(R.id.epg_inputbox_cursor);
        this.mProgressBar = (ProgressBarNewItem) this.mMainView.findViewById(R.id.epg_verify_pro_login);
    }

    public void onHiddenChanged(boolean hidden) {
        if (!(hidden || this.mGALAKeyboard == null)) {
            this.mGALAKeyboard.restoreFocus(101);
        }
        super.onHiddenChanged(hidden);
    }

    public void onClick(View v) {
        if (this.mLoginEvent != null && this.mLoginPresenter != null) {
            int vId = v.getId();
            if (vId == R.id.epg_view_change_tab) {
                setBack(false);
                this.mLoginEvent.onSwitchFragment(new CommRegisterFragment(), getArguments());
            } else if (vId == R.id.epg_input_username) {
                this.mLoginPresenter.switchInputAccount();
            } else if (vId == R.id.epg_input_password) {
                this.mLoginPresenter.switchInputPassword();
            } else if (vId == R.id.epg_input_verification_code) {
                this.mLoginPresenter.switchInputVerifyCode();
            } else if (vId == R.id.epg_btn_login) {
                this.mLoginPresenter.callLoginRequest();
            } else if (vId == R.id.epg_image_verify) {
                this.mLoginPresenter.loadVerifyCode();
            }
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        int vid = v.getId();
        if (vid == R.id.epg_input_username || vid == R.id.epg_input_password || vid == R.id.epg_input_verification_code || vid == R.id.epg_image_verify || vid == R.id.epg_btn_login || vid == R.id.epg_view_change_tab) {
            if (vid == R.id.epg_input_username || vid == R.id.epg_input_password || vid == R.id.epg_input_verification_code) {
                setHintColor(vid, hasFocus ? R.color.gala_write : R.color.hot_search);
            }
            if (vid == R.id.epg_view_change_tab) {
                this.mChangeTab.setTextColor(getColor(hasFocus ? R.color.item_name_focus : R.color.change_tab_color_normal));
            }
            viewBringToFront(v, vid);
            AnimationUtil.zoomAnimation(v, hasFocus, 1.02f, 200);
        }
    }

    private void viewBringToFront(View v, int vid) {
        if (vid == R.id.epg_input_verification_code || vid == R.id.epg_image_verify) {
            v.bringToFront();
        }
    }

    public String getAccount() {
        return this.mAccountCursor.getText().toString();
    }

    public String getPassword() {
        return this.mPasswordCursor.getText().toString();
    }

    public String getVerifyCode() {
        return this.mVerifyCursor.getText().toString();
    }

    public void updateTextBuffer(String text) {
        this.mGALAKeyboard.updateTextBuffer(text);
    }

    public void registerKeyboardListener(IKeyboardListener listener) {
        this.mGALAKeyboard.setKeyListener(listener);
    }

    public void changeConfirmTextAndDrawable(int stringId, int resId) {
        this.mGALAKeyboard.setConfirmTextAndDrawable(stringId, resId);
    }

    public void initKeyboardLayout(int stringId, int resId) {
        this.mGALAKeyboard.initKeyLayout(stringId, resId);
    }

    public void startAccountCursor(long time) {
        this.mAccountCursor.startCursor(time);
    }

    public void startPasswordCursor(long time) {
        this.mPasswordCursor.startCursor(time);
    }

    public void startVerifyCursor(long time) {
        this.mVerifyCursor.startCursor(time);
    }

    public void stopAccountCursor() {
        this.mAccountCursor.stopCursor();
    }

    public void stopPasswordCursor() {
        this.mPasswordCursor.stopCursor();
    }

    public void stopVerifyCursor() {
        this.mVerifyCursor.stopCursor();
    }

    public void setAccount(String str) {
        this.mAccountCursor.setText(str);
    }

    public void setPassword(String str) {
        this.mPasswordCursor.setText(str);
    }

    public void setVerifyCode(String str) {
        this.mVerifyCursor.setText(str);
    }

    public void showAccountErrorTipUI(boolean showIcon, String res) {
        int i = 4;
        if (this.mAccountErrorView != null && this.mAccountErrorView.getVisibility() == 4) {
            ImageView tipIcon = (ImageView) this.mAccountErrorView.findViewById(R.id.epg_error_tip_icon);
            TextView tipText = (TextView) this.mAccountErrorView.findViewById(R.id.epg_error_tip_text);
            if (showIcon) {
                i = 0;
            }
            tipIcon.setVisibility(i);
            tipText.setText(res);
            this.mAccountErrorView.setVisibility(0);
        }
    }

    public void showPasswordErrorTipUI(boolean showIcon, String res) {
        int i = 4;
        if (this.mPasswordErrorView != null && this.mPasswordErrorView.getVisibility() == 4) {
            ImageView tipIcon = (ImageView) this.mPasswordErrorView.findViewById(R.id.epg_error_tip_icon);
            TextView tipText = (TextView) this.mPasswordErrorView.findViewById(R.id.epg_error_tip_text);
            if (showIcon) {
                i = 0;
            }
            tipIcon.setVisibility(i);
            tipText.setText(res);
            this.mPasswordErrorView.setVisibility(0);
        }
    }

    public void showVerifycodeErrorTipUI(boolean showIcon, String res) {
        int i = 4;
        if (this.mVerifyErrorView != null && this.mVerifyErrorView.getVisibility() == 4) {
            ImageView tipIcon = (ImageView) this.mVerifyErrorView.findViewById(R.id.epg_error_tip_icon);
            TextView tipText = (TextView) this.mVerifyErrorView.findViewById(R.id.epg_error_tip_text);
            if (showIcon) {
                i = 0;
            }
            tipIcon.setVisibility(i);
            tipText.setText(res);
            this.mVerifyErrorView.setVisibility(0);
        }
    }

    public void hideAccountErrorTipUI() {
        if (this.mAccountErrorView != null && this.mAccountErrorView.getVisibility() == 0) {
            this.mAccountErrorView.setVisibility(4);
        }
    }

    public void hidePasswordErrorTipUI() {
        if (this.mPasswordErrorView != null && this.mPasswordErrorView.getVisibility() == 0) {
            this.mPasswordErrorView.setVisibility(4);
        }
    }

    public void hideVerifycodeErrorTipUI() {
        if (this.mVerifyErrorView != null && this.mVerifyErrorView.getVisibility() == 0) {
            this.mVerifyErrorView.setVisibility(4);
        }
    }

    public void switchToMyCenterPage(UserInfoBean bean) {
        gotoMyCenter(bean, this.mIntentFlag);
    }

    public void showProgressBar() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(0);
        }
    }

    public void hideProgressBar() {
        if (this.mProgressBar != null && this.mProgressBar.getVisibility() == 0) {
            this.mProgressBar.setVisibility(4);
        }
    }

    public void setVerifyBitmap(Bitmap bitmap) {
        if (this.mVerifyImage != null) {
            this.mVerifyImage.setImageBitmap(bitmap);
        }
    }

    public void showVerifyCodeUI() {
        if (this.mVerifyView != null && this.mVerifyImage != null && this.mVerifyView.getVisibility() != 0) {
            this.mVerifyView.setVisibility(0);
            this.mVerifyImage.setVisibility(0);
            this.mPasswordView.setNextFocusDownId(this.mVerifyView.getId());
        }
    }

    public void hideVerifyCodeUI() {
        if (this.mVerifyView != null && this.mVerifyImage != null && this.mVerifyView.getVisibility() == 0) {
            this.mVerifyView.setVisibility(4);
            this.mVerifyImage.setVisibility(4);
            this.mPasswordView.setNextFocusDownId(this.mLoginButton.getId());
        }
    }

    public boolean isVerifyVisible() {
        if (this.mVerifyView == null || this.mVerifyView.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    private void setHintColor(int resId, int colorId) {
        if (resId == R.id.epg_input_username) {
            if (!StringUtils.isEmpty(this.mAccountCursor.getHint().toString())) {
                this.mAccountCursor.setHintTextColor(getColor(colorId));
            }
        } else if (resId == R.id.epg_input_password) {
            if (!StringUtils.isEmpty(this.mPasswordCursor.getHint().toString())) {
                this.mPasswordCursor.setHintTextColor(getColor(colorId));
            }
        } else if (resId == R.id.epg_input_verification_code && !StringUtils.isEmpty(this.mVerifyCursor.getHint().toString())) {
            this.mVerifyCursor.setHintTextColor(getColor(colorId));
        }
    }
}
