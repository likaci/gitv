package com.gala.video.app.epg.web.core;

import android.content.Context;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.web.function.ISkipCallback;
import com.gala.video.app.epg.web.function.WebFunContract.IFunSkip;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.app.epg.web.type.ActivationType;
import com.gala.video.app.epg.web.type.AlbumListType;
import com.gala.video.app.epg.web.type.DetailOrPlayType;
import com.gala.video.app.epg.web.type.IWebBaseClickType;
import com.gala.video.app.epg.web.type.MemberPackageType;
import com.gala.video.app.epg.web.type.MemberRightsType;
import com.gala.video.app.epg.web.type.MoreDailyNewsType;
import com.gala.video.app.epg.web.type.PlayForLiveType;
import com.gala.video.app.epg.web.type.SearchResultType;
import com.gala.video.app.epg.web.type.SubjectAction;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;

public class FunctionSkip implements IFunSkip {
    private static final String TAG = "EPG/web/FunctionSkip";
    private Context mContext;
    private ISkipCallback mISkipCallback;
    private WebViewDataImpl mWebViewDataImpl;

    public FunctionSkip(Context context, WebViewDataImpl webViewDataImpl) {
        this.mContext = context;
        this.mWebViewDataImpl = webViewDataImpl;
    }

    public FunctionSkip(Context context, WebViewDataImpl webViewDataImpl, ISkipCallback ISkipCallback) {
        this(context, webViewDataImpl);
        this.mISkipCallback = ISkipCallback;
    }

    public void goBack() {
        if (this.mISkipCallback != null) {
            this.mISkipCallback.goBackEvent();
        }
    }

    public void gotoDetailOrPlay(String paramJson) {
        IWebBaseClickType type = new DetailOrPlayType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setWebViewData(this.mWebViewDataImpl);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void startPlayForLive(String paramJson) {
        IWebBaseClickType type = new PlayForLiveType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setWebViewData(this.mWebViewDataImpl);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void gotoAlbumList(String paramJson) {
        IWebBaseClickType type = new AlbumListType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void gotoSubject(String json) {
        IWebBaseClickType type = new SubjectAction();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(json);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void gotoMemberPackage(String paramJson) {
        LogUtils.d(TAG, "H5 gotoMemberPackage");
        IWebBaseClickType type = new MemberPackageType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void startMemberRightsPage(String paramJson) {
        LogUtils.d(TAG, "H5 startMemberRightsPage");
        IWebBaseClickType type = new MemberRightsType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void gotoMoreDailyNews(String paramJson) {
        IWebBaseClickType type = new MoreDailyNewsType(this.mContext);
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setWebViewData(this.mWebViewDataImpl);
        type.onClick(typeParams);
    }

    public void gotoSearchResult(String paramJson) {
        IWebBaseClickType type = new SearchResultType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void gotoActivation(String paramJson) {
        IWebBaseClickType type = new ActivationType(this.mContext);
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(paramJson);
        type.onClick(typeParams);
    }

    public void gotoVip() {
        AlbumUtils.startChannelVipPage(this.mContext);
    }

    public void gotoHistory() {
        AlbumUtils.startPlayhistoryActivity(this.mContext);
    }

    public void gotoFavorite() {
        AlbumUtils.startFavouriteActivity(this.mContext);
    }

    public void gotoSearch() {
        GetInterfaceTools.getWebEntry().gotoSearch(this.mContext);
    }

    public void gotoCommonWeb(String pageUrl) {
        GetInterfaceTools.getWebEntry().gotoCommonWebActivity(this.mContext, pageUrl);
    }
}
