package com.gala.video.app.epg.uikit.contract;

public class UCenterItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        String getLoginTips();

        String getStatus();

        String getUid();

        String getUserName();

        boolean isLogin();

        boolean isVip();
    }
}
