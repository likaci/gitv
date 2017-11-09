package com.gala.video.app.epg.ui.ucenter.account.ui;

public interface IRegisterView extends IBaseLoginView {
    String getMessageCode();

    void hideMessagecodeErrorTipUI();

    void setMessageBtnTxt(String str);

    void setMessageCode(String str);

    void showMessagecodeErrorTipUI(boolean z, int i);

    void startMessageCursor(long j);

    void stopMessageCursor();
}
