package com.gala.video.app.epg.ui.ucenter.account.presenter;

import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.ucenter.account.ui.IBaseLoginView;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;

public abstract class CommBasePresenter {
    protected IKeyboardListener mAccountKeyboardListener = new C11561();
    private IBaseLoginView mBaseLoginView;

    class C11561 implements IKeyboardListener {
        C11561() {
        }

        public void onTextChange(String text) {
            CommBasePresenter.this.mBaseLoginView.setAccount(text);
        }

        public void onSearchActor(String text) {
        }

        public void onCommit(String text) {
            CommBasePresenter.this.switchInputPassword();
        }
    }

    protected abstract void switchInputAccount();

    protected abstract void switchInputPassword();

    protected abstract void switchInputVerifyCode();

    public CommBasePresenter(IBaseLoginView loginView) {
        this.mBaseLoginView = loginView;
    }

    public void initAccountCursor() {
        this.mBaseLoginView.updateTextBuffer("");
        this.mBaseLoginView.stopAccountCursor();
        this.mBaseLoginView.startAccountCursor(650);
    }

    protected boolean checkAccountAvailability() {
        String account = this.mBaseLoginView.getAccount();
        if (account == null || !StringUtils.isEmpty(account.trim())) {
            return true;
        }
        this.mBaseLoginView.showAccountErrorTipUI(true, AppRuntimeEnv.get().getApplicationContext().getResources().getString(C0508R.string.InputAccount2));
        switchInputAccount();
        return false;
    }

    protected boolean checkPasswordAvailability() {
        String password = this.mBaseLoginView.getPassword();
        if (password == null || !StringUtils.isEmpty(password.trim())) {
            return true;
        }
        this.mBaseLoginView.showPasswordErrorTipUI(true, AppRuntimeEnv.get().getApplicationContext().getResources().getString(C0508R.string.InputPasswordHint1));
        switchInputPassword();
        return false;
    }

    protected boolean checkVerifycodeAvailability() {
        String verifycode = this.mBaseLoginView.getVerifyCode();
        if (verifycode == null || !StringUtils.isEmpty(verifycode.trim())) {
            return true;
        }
        this.mBaseLoginView.showVerifycodeErrorTipUI(false, AppRuntimeEnv.get().getApplicationContext().getResources().getString(C0508R.string.InputVerifycode));
        switchInputVerifyCode();
        return false;
    }

    protected void gotoMyCenterFragment(UserInfoBean bean) {
        if (this.mBaseLoginView != null) {
            this.mBaseLoginView.switchToMyCenterPage(bean);
        }
    }
}
