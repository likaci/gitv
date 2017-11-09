package com.gala.video.app.epg.web.pingback;

import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.web.model.WebInfo;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class WebLoadPingback implements IWebLoadPingback {
    private static final String ENABLE_CROSSWALK = "enableCrosswalk";
    private static final String FILENAME = "filename";
    private static final String H5PAGELOAD = "170629_h5pageload";
    private static final String LOG_TAG = "WebLoadPingback";
    private static final String OTHER = "other";
    private static final String PLOADTIME = "ploadtime";
    private static final String WVCTIME = "wvctime";
    private int enableCrosswalk = 0;
    private String filename;
    private long mBeforeWebViewTime;
    private long mStartLoadUrlTime;
    private long ploadtime;
    private long wvctime;

    public void setBeforeWebViewTime() {
        this.mBeforeWebViewTime = System.currentTimeMillis();
    }

    public void setAfterWebViewTime() {
        this.wvctime = System.currentTimeMillis() - this.mBeforeWebViewTime;
    }

    public void setLoadUrlTime() {
        this.mStartLoadUrlTime = System.currentTimeMillis();
    }

    public void setEventType(int type) {
        this.enableCrosswalk = type;
    }

    public void send(final WebInfo info) {
        if (info != null) {
            this.ploadtime = System.currentTimeMillis() - this.mStartLoadUrlTime;
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    WebLoadPingback.this.filename = WebLoadPingback.getPageType(info.getCurrentPageType(), info.getPageUrl());
                    PingBackParams params = new PingBackParams();
                    params.add("ct", WebLoadPingback.H5PAGELOAD).add(Keys.f2035T, "11").add(WebLoadPingback.FILENAME, WebLoadPingback.this.filename).add(WebLoadPingback.WVCTIME, String.valueOf(WebLoadPingback.this.wvctime)).add(WebLoadPingback.PLOADTIME, String.valueOf(WebLoadPingback.this.ploadtime)).add(WebLoadPingback.ENABLE_CROSSWALK, String.valueOf(WebLoadPingback.this.enableCrosswalk));
                    PingBack.getInstance().postPingBackToLongYuan(params.build());
                }
            });
        }
    }

    private static String getPageType(int pageType, String pageUrl) {
        switch (pageType) {
            case -1:
                return getPageTypeByPageUrl(pageUrl);
            case 0:
                return WebConstants.DEFAULT_WEB_SITE_FAQ;
            case 1:
                return WebConstants.DEFAULT_WEB_SITE_MEMBER_PACKAGE;
            case 2:
                return WebConstants.DEFAULT_WEB_SITE_SUBJECT;
            case 3:
                return WebConstants.DEFAULT_WEB_SITE_ACTIVITY;
            case 4:
                return WebConstants.DEFAULT_WEB_SITE_MEMBER_RIGHTS;
            case 5:
                return WebConstants.DEFAULT_WEB_SITE_MEMBER_GET_GOLD;
            case 6:
                return WebConstants.DEFAULT_WEB_SITE_MULTISCREEN;
            default:
                return "other";
        }
    }

    private static String getPageTypeByPageUrl(String pageUrl) {
        if (StringUtils.isEmpty((CharSequence) pageUrl)) {
            return "other";
        }
        if (pageUrl.contains("faq")) {
            return WebConstants.DEFAULT_WEB_SITE_FAQ;
        }
        if (pageUrl.contains("memberpackage")) {
            return WebConstants.DEFAULT_WEB_SITE_MEMBER_PACKAGE;
        }
        if (pageUrl.contains("memberrights")) {
            return WebConstants.DEFAULT_WEB_SITE_MEMBER_RIGHTS;
        }
        if (pageUrl.contains("vipgift")) {
            return WebConstants.DEFAULT_WEB_SITE_MEMBER_GET_GOLD;
        }
        if (pageUrl.contains("subject")) {
            return WebConstants.DEFAULT_WEB_SITE_MULTISCREEN;
        }
        if (pageUrl.contains("activepage")) {
            return WebConstants.DEFAULT_WEB_SITE_ACTIVITY;
        }
        if (pageUrl.contains(SettingConstants.MULTISCREEN)) {
            return WebConstants.DEFAULT_WEB_SITE_MULTISCREEN;
        }
        return "other";
    }
}
