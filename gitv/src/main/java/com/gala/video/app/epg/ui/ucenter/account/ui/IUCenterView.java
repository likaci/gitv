package com.gala.video.app.epg.ui.ucenter.account.ui;

import android.graphics.Bitmap;

public interface IUCenterView {
    void hideErrorUI();

    void hideLoadingUI();

    void hideSecurityUI();

    void refreshUserInfoUI();

    void showErrorUI();

    void showExceptionUI(String str);

    void showLoadingUI();

    void showLogoutUI();

    void showQRFailUI();

    void showQRImageUI(Bitmap bitmap);

    void showSecurityUI();
}
