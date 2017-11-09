package com.gala.video.app.epg.ui.ucenter.account.ui;

import android.graphics.Bitmap;
import com.gala.video.app.epg.widget.IKeyboardListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;

public interface IBaseLoginView {
    void changeConfirmTextAndDrawable(int i, int i2);

    String getAccount();

    String getPassword();

    String getVerifyCode();

    void hideAccountErrorTipUI();

    void hidePasswordErrorTipUI();

    void hideProgressBar();

    void hideVerifyCodeUI();

    void hideVerifycodeErrorTipUI();

    void initKeyboardLayout(int i, int i2);

    boolean isVerifyVisible();

    void registerKeyboardListener(IKeyboardListener iKeyboardListener);

    void setAccount(String str);

    void setPassword(String str);

    void setVerifyBitmap(Bitmap bitmap);

    void setVerifyCode(String str);

    void showAccountErrorTipUI(boolean z, String str);

    void showPasswordErrorTipUI(boolean z, String str);

    void showProgressBar();

    void showVerifyCodeUI();

    void showVerifycodeErrorTipUI(boolean z, String str);

    void startAccountCursor(long j);

    void startPasswordCursor(long j);

    void startVerifyCursor(long j);

    void stopAccountCursor();

    void stopPasswordCursor();

    void stopVerifyCursor();

    void switchToMyCenterPage(UserInfoBean userInfoBean);

    void updateTextBuffer(String str);
}
