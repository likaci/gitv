package com.gala.video.app.epg.web.function;

import com.gala.video.webview.core.WebSDKFunContract;

public interface WebFunContract extends WebSDKFunContract {

    public interface IFunBase extends com.gala.video.webview.core.WebSDKFunContract.IFunBase {
    }

    public interface IFunLoad extends com.gala.video.webview.core.WebSDKFunContract.IFunLoad {
    }

    public interface IFunPlayer {
        void checkLiveInfo(String str);

        void onAlbumSelected(String str);

        void startWindowPlay(String str);

        void switchPlay(String str);

        void switchScreenMode(String str);
    }

    public interface IFunSkip {
        void goBack();

        void gotoActivation(String str);

        void gotoAlbumList(String str);

        void gotoCommonWeb(String str);

        void gotoDetailOrPlay(String str);

        void gotoFavorite();

        void gotoHistory();

        void gotoMemberPackage(String str);

        void gotoMoreDailyNews(String str);

        void gotoSearch();

        void gotoSearchResult(String str);

        void gotoSubject(String str);

        void gotoVip();

        void startMemberRightsPage(String str);

        void startPlayForLive(String str);
    }

    public interface IFunUser extends com.gala.video.webview.core.WebSDKFunContract.IFunUser {
        void onPushMsg(String str);
    }

    public interface IFunDialog {
        void setDialogState(String str);
    }
}
