package com.gala.video.app.epg.web.core;

import android.util.Log;
import android.webkit.JavascriptInterface;
import com.gala.video.app.epg.web.function.WebFunContract.IFunBase;
import com.gala.video.app.epg.web.function.WebFunContract.IFunDialog;
import com.gala.video.app.epg.web.function.WebFunContract.IFunLoad;
import com.gala.video.app.epg.web.function.WebFunContract.IFunPlayer;
import com.gala.video.app.epg.web.function.WebFunContract.IFunSkip;
import com.gala.video.app.epg.web.function.WebFunContract.IFunUser;
import com.gala.video.lib.framework.core.utils.JsonUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WebFunManager implements IFunBase, IFunLoad, IFunPlayer, IFunSkip, IFunUser, IFunDialog {
    private static final String TAG = "EPG/web/WebFunManager";
    private IFunSkip mFunSkip;
    private IFunUser mFunUser;
    private IFunBase mIFunBase;
    private IFunDialog mIFunDialog;
    private IFunLoad mIFunLoad;
    private IFunPlayer mIFunPlayer;

    public IFunBase getIFunBase() {
        return this.mIFunBase;
    }

    public WebFunManager setIFunBase(IFunBase funBase) {
        this.mIFunBase = funBase;
        return this;
    }

    public IFunLoad getIFunLoad() {
        return this.mIFunLoad;
    }

    public WebFunManager setIFunLoad(IFunLoad funLoad) {
        this.mIFunLoad = funLoad;
        return this;
    }

    public IFunPlayer getIFunPlayer() {
        return this.mIFunPlayer;
    }

    public WebFunManager setIFunPlayer(IFunPlayer funPlayer) {
        this.mIFunPlayer = funPlayer;
        return this;
    }

    public IFunSkip getFunSkip() {
        return this.mFunSkip;
    }

    public void setFunSkip(IFunSkip mFunSkip) {
        this.mFunSkip = mFunSkip;
    }

    public IFunUser getFunUser() {
        return this.mFunUser;
    }

    public void setFunUser(IFunUser funUser) {
        this.mFunUser = funUser;
    }

    public IFunDialog getIFunDialog() {
        return this.mIFunDialog;
    }

    public void setIFunDialog(IFunDialog IFunDialog) {
        this.mIFunDialog = IFunDialog;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void onLoadCompleted() {
        Log.d(TAG, "H5 onLoadCompleted");
        if (getIFunLoad() != null) {
            getIFunLoad().onLoadCompleted();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void onLoadFailed(String errorInfo) {
        Log.d(TAG, "H5 onLoadFailed");
        if (getIFunLoad() != null) {
            getIFunLoad().onLoadFailed(errorInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public String getParams() {
        if (getIFunBase() != null) {
            return getIFunBase().getParams();
        }
        return "";
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public String getUserInfoParams(String info) {
        if (getIFunBase() != null) {
            return getIFunBase().getUserInfoParams(info);
        }
        return "";
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public String getSupportMethodList(String paramJson) {
        Log.d(TAG, "H5 getSupportMethodList paramJson: " + paramJson);
        return JsonUtils.toJson(getMethodList());
    }

    private List<String> getMethodList() {
        List<String> list = new ArrayList();
        Method[] methods = getClass().getMethods();
        int len = methods.length;
        for (int i = 0; i < len; i++) {
            if (methods[i].getAnnotation(org.xwalk.core.JavascriptInterface.class) != null) {
                list.add(methods[i].getName());
            }
        }
        return list;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void onAlbumSelected(String albumInfo) {
        if (getIFunPlayer() != null) {
            getIFunPlayer().onAlbumSelected(albumInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void startWindowPlay(String playInfo) {
        if (getIFunPlayer() != null) {
            getIFunPlayer().startWindowPlay(playInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void switchPlay(String playInfo) {
        if (getIFunPlayer() != null) {
            getIFunPlayer().switchPlay(playInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void switchScreenMode(String mode) {
        LogUtils.d(TAG, "switchScreenMode");
        if (getIFunPlayer() != null) {
            getIFunPlayer().switchScreenMode(mode);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void checkLiveInfo(String albumInfo) {
        if (getIFunPlayer() != null) {
            getIFunPlayer().checkLiveInfo(albumInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void goBack() {
        LogUtils.d(TAG, "goBack");
        if (getFunSkip() != null) {
            getFunSkip().goBack();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoDetailOrPlay(String paramJson) {
        LogUtils.d(TAG, "gotoDetailOrPlay");
        if (getFunSkip() != null) {
            getFunSkip().gotoDetailOrPlay(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void startPlayForLive(String paramJson) {
        LogUtils.d(TAG, "startPlayForLive");
        if (getFunSkip() != null) {
            getFunSkip().startPlayForLive(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoAlbumList(String paramJson) {
        if (getFunSkip() != null) {
            getFunSkip().gotoAlbumList(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoSubject(String json) {
        if (getFunSkip() != null) {
            getFunSkip().gotoSubject(json);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoMemberPackage(String paramJson) {
        if (getFunSkip() != null) {
            getFunSkip().gotoMemberPackage(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void startMemberRightsPage(String paramJson) {
        if (getFunSkip() != null) {
            getFunSkip().startMemberRightsPage(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoMoreDailyNews(String paramJson) {
        if (getFunSkip() != null) {
            getFunSkip().gotoMoreDailyNews(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoSearchResult(String paramJson) {
        if (getFunSkip() != null) {
            getFunSkip().gotoSearchResult(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void onLoginSuccess(String userInfo) {
        if (getFunUser() != null) {
            getFunUser().onLoginSuccess(userInfo);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoActivation(String paramJson) {
        LogUtils.d(TAG, "gotoActivation");
        if (getFunSkip() != null) {
            getFunSkip().gotoActivation(paramJson);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoVip() {
        if (getFunSkip() != null) {
            getFunSkip().gotoVip();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoHistory() {
        if (getFunSkip() != null) {
            getFunSkip().gotoHistory();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoFavorite() {
        if (getFunSkip() != null) {
            getFunSkip().gotoFavorite();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoSearch() {
        if (getFunSkip() != null) {
            getFunSkip().gotoSearch();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void gotoCommonWeb(String pageUrl) {
        if (getFunSkip() != null) {
            getFunSkip().gotoCommonWeb(pageUrl);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void setActivityResult(String result, int resultCode) {
        if (getFunUser() != null) {
            getFunUser().setActivityResult(result, resultCode);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void onPushMsg(String msg) {
        if (getFunUser() != null) {
            getFunUser().onPushMsg(msg);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void finish() {
        if (getIFunBase() != null) {
            getIFunBase().finish();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    public void setDialogState(String state) {
        if (getIFunDialog() != null) {
            getIFunDialog().setDialogState(state);
        }
    }
}
