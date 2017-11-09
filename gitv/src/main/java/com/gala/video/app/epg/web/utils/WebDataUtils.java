package com.gala.video.app.epg.web.utils;

import android.annotation.SuppressLint;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.project.Project;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebDataUtils {
    private static final String TAG = "EPG/WebDataUtils";

    public static String getInfoJson() {
        return new WebViewDataImpl().getUserInfoJson();
    }

    public static String parseWebUrl(String pageUrl) {
        CharSequence domainName = Project.getInstance().getBuild().getDomainName();
        StringBuilder sbBuilder = new StringBuilder();
        LogUtils.e(TAG, "parseWebUrl domain pageUrl:" + pageUrl + ",domainName" + domainName);
        if (pageUrl.startsWith(WebConstants.WEB_SITE_BASE_HTTP) || pageUrl.startsWith(WebConstants.WEB_SITE_BASE_HTTPS)) {
            sbBuilder = sbBuilder.append(pageUrl);
        } else {
            if (StringUtils.isEmpty(domainName)) {
                sbBuilder = sbBuilder.append(WebConstants.WEB_SITE_BASE_PTQY);
            } else if (domainName.contains("i.com")) {
                sbBuilder = sbBuilder.append(WebConstants.WEB_SITE_BASE_WWW).append(domainName).append(WebConstants.WEB_SITE_BASE_COMMON_TV);
            } else {
                sbBuilder = sbBuilder.append(WebConstants.WEB_SITE_BASE_CMS).append(domainName).append(WebConstants.WEB_SITE_BASE_COMMON_TV);
            }
            sbBuilder = sbBuilder.append(pageUrl);
        }
        pageUrl = sbBuilder.toString();
        LogUtils.e(TAG, "parseWebUrl domain pageUrl end:" + pageUrl);
        return pageUrl;
    }

    public static JSONObject toJSON(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return (JSONObject) JSON.toJSON(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WebViewDataImpl generateJsonParams(WebViewDataImpl webJSONObject, WebInfo info, int pageType) {
        if (webJSONObject == null) {
            webJSONObject = new WebViewDataImpl();
        }
        webJSONObject.putFrom(info.getFrom());
        webJSONObject.putPageType(info.getPageType());
        webJSONObject.putEnterType(info.getEnterType());
        webJSONObject.putIncomeSrc(info.getIncomesrc());
        webJSONObject.putBuyFrom(info.getBuyFrom());
        switch (pageType) {
            case 1:
                webJSONObject.putEventid(info.getEventId());
                webJSONObject.putState(info.getState());
                webJSONObject.putAlbum(info.getAlbumJson());
                webJSONObject.putBuyVip(info.getBuyVip());
                break;
            case 8:
                webJSONObject.putCouponActivityCode(info.getCouponActivityCode());
                webJSONObject.putCouponSignKey(info.getCouponSignKey());
                break;
        }
        return webJSONObject;
    }

    public static String getWebUrl(int pageType) {
        String pageUrl = "";
        switch (pageType) {
            case -1:
                return pageUrl;
            case 0:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlFAQ();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_FAQ;
                }
                break;
            case 1:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlMemberPackage();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_MEMBER_PACKAGE;
                }
                break;
            case 3:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlRoleActivity();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_ACTIVITY;
                }
                break;
            case 4:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlMemberRights();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_MEMBER_RIGHTS;
                }
                break;
            case 5:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlGetGold();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_MEMBER_GET_GOLD;
                }
                break;
            case 6:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlMultiscreen();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_MULTISCREEN;
                }
                break;
            case 7:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlSignIn();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_SIGNIN;
                }
                break;
            case 8:
                pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlCoupon();
                if (StringUtils.isEmpty((CharSequence) pageUrl)) {
                    pageUrl = WebConstants.DEFAULT_WEB_SITE_COUPON;
                }
                break;
        }
        return parseWebUrl(pageUrl);
    }

    @SuppressLint({"SimpleDateFormat"})
    private static String timeStamp2Date(long seconds) {
        return new SimpleDateFormat("yyyy").format(new Date(seconds));
    }

    public static boolean isSSLProceed(SslError error) {
        if (error == null) {
            LogUtils.e(TAG, "onReceivedSslError error is null!");
            return false;
        }
        SslCertificate mSslCertificate = error.getCertificate();
        if ((mSslCertificate == null || mSslCertificate.getValidNotAfterDate() == null) && mSslCertificate.getValidNotBeforeDate() == null) {
            return false;
        }
        long nowTime = System.currentTimeMillis();
        LogUtils.d(TAG, "onReceivedSslError current date:" + nowTime);
        long beforeTime = mSslCertificate.getValidNotBeforeDate().getTime();
        LogUtils.d(TAG, "onReceivedSslError before date:" + beforeTime);
        long afterTime = mSslCertificate.getValidNotAfterDate().getTime();
        LogUtils.d(TAG, "onReceivedSslError after date:" + afterTime);
        if (nowTime < beforeTime || nowTime > afterTime) {
            return true;
        }
        return false;
    }

    public static String getAlbumInfo(IVideo video) {
        Album nextAlbum = video.getAlbum().copy();
        nextAlbum.focus = nextAlbum.focus == null ? "" : nextAlbum.focus.replace("\"", "'");
        nextAlbum.name = nextAlbum.name == null ? "" : nextAlbum.name.replace("\"", "'");
        nextAlbum.tvName = nextAlbum.tvName == null ? "" : nextAlbum.tvName.replace("\"", "'");
        nextAlbum.desc = "";
        return JSON.toJSONString(nextAlbum).replace("'", "\\'").replace("\\r", "").replace("\\n", "");
    }
}
