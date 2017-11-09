package com.gala.video.lib.share.ifimpl.ads;

import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.UserType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.UrlUtils;
import com.gala.video.lib.framework.core.utils.io.HttpUtil;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi.Wrapper;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import java.util.UUID;

class AdApi extends Wrapper implements IAdApi {
    private static String ADDRESS_IMAGE_RESOURCE_AD = null;
    private static final String ADDRESS_IMAGE_RESOURCE_AD_T = "10.11.75.199";
    private static String AD_IMAGE_RESOURCE_URL = null;
    private static String AD_URL = (WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s");
    private static String AD_URL_EXIT_APP_DIALOG = null;
    private static String AD_URL_HOME_FOCUS_IMAGE = null;
    private static String AD_URL_SCREEN_SAVER = null;
    private static String AD_URL_START_SCREEN = null;
    private static String MIXER = null;
    private static final String MIXER_T = "106.120.150.10";
    private static String TAG = "AdApi";

    public java.lang.String getScreenVideoDownLoadUrl(java.lang.String r14) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r13 = this;
        r1 = 0;
        if (r14 == 0) goto L_0x000e;
    L_0x0003:
        r10 = "pv=0.2";
        r10 = r14.contains(r10);
        if (r10 == 0) goto L_0x000e;
    L_0x000c:
        r2 = r14;
    L_0x000d:
        return r2;
    L_0x000e:
        r10 = TAG;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11.<init>();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r12 = "get screen video url = ";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.append(r14);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        com.gala.video.lib.framework.core.utils.LogUtils.d(r10, r11);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r2 = "";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = android.text.TextUtils.isEmpty(r14);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r10 == 0) goto L_0x0036;
    L_0x0030:
        if (r1 == 0) goto L_0x000d;
    L_0x0032:
        r1.disconnect();
        goto L_0x000d;
    L_0x0036:
        r7 = new java.net.URL;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r7.<init>(r14);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = r7.openConnection();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r0 = r10;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r1 = r0;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r1.setConnectTimeout(r10);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = "User-Agent";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r1.setRequestProperty(r10, r11);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = "GET";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r1.setRequestMethod(r10);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r1.connect();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = r1.getResponseCode();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r10 != r11) goto L_0x00ab;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x0062:
        r4 = r1.getInputStream();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r9 = com.gala.video.lib.framework.core.utils.ConvertUtil.getStringFromInputStream(r4);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r9 == 0) goto L_0x00ab;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x006c:
        r10 = "{";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r8 = r9.indexOf(r10);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = "}";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = r9.lastIndexOf(r10);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r5 = r10 + 1;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r10 = r9.substring(r8, r5);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = com.gala.video.lib.share.ifmanager.bussnessIF.ads.model.VideoDynamicUrl.class;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r6 = com.alibaba.fastjson.JSON.parseObject(r10, r11);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r6 = (com.gala.video.lib.share.ifmanager.bussnessIF.ads.model.VideoDynamicUrl) r6;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r6 == 0) goto L_0x0092;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x008a:
        r10 = r6.data;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r10 == 0) goto L_0x0092;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x008e:
        r10 = r6.data;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r2 = r10.l;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x0092:
        r10 = TAG;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11.<init>();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r12 = "video download url = ";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.append(r2);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        com.gala.video.lib.framework.core.utils.LogUtils.d(r10, r11);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
    L_0x00ab:
        if (r1 == 0) goto L_0x000d;
    L_0x00ad:
        r1.disconnect();
        goto L_0x000d;
    L_0x00b2:
        r3 = move-exception;
        r10 = "TAG";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r11 = "get screen video exception ";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        com.gala.video.lib.framework.core.utils.LogUtils.d(r10, r11, r3);	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        r2 = "";	 Catch:{ Exception -> 0x00b2, all -> 0x00c6 }
        if (r1 == 0) goto L_0x000d;
    L_0x00c1:
        r1.disconnect();
        goto L_0x000d;
    L_0x00c6:
        r10 = move-exception;
        if (r1 == 0) goto L_0x00cc;
    L_0x00c9:
        r1.disconnect();
    L_0x00cc:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.ads.AdApi.getScreenVideoDownLoadUrl(java.lang.String):java.lang.String");
    }

    static {
        MIXER = "mixer.cupid.ptqy.gitv.tv";
        AD_URL_START_SCREEN = WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&s=%s&t=%s&u=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&avd=%s&avn=%s";
        AD_URL_SCREEN_SAVER = WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
        AD_URL_EXIT_APP_DIALOG = WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
        AD_URL_HOME_FOCUS_IMAGE = WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
        ADDRESS_IMAGE_RESOURCE_AD = "resource.cupid.ptqy.gitv.tv";
        AD_IMAGE_RESOURCE_URL = WebConstants.WEB_SITE_BASE_HTTP + ADDRESS_IMAGE_RESOURCE_AD + "/creativeCache?" + "player_id=%s&template_type=%s&resolution=%s&dpi=%s&app_version=%s";
        String domainName = Project.getInstance().getBuild().getDomainName();
        LogUtils.d(TAG, "Project AppConfig, domainName = " + domainName);
        MIXER = "mixer.cupid." + (!StringUtils.isEmpty((CharSequence) domainName) ? domainName : BuildDefaultDocument.APK_DOMAIN_NAME);
        StringBuilder append = new StringBuilder().append("resource.cupid.");
        if (StringUtils.isEmpty((CharSequence) domainName)) {
            domainName = BuildDefaultDocument.APK_DOMAIN_NAME;
        }
        ADDRESS_IMAGE_RESOURCE_AD = append.append(domainName).toString();
        AD_URL_START_SCREEN = generateAdUrlStartScreen();
        AD_URL_SCREEN_SAVER = generateAdUrlScreenSaver();
        AD_URL_EXIT_APP_DIALOG = generateAdUrlExitAppDialog();
        AD_IMAGE_RESOURCE_URL = generateAdImageResourceUrl();
        AD_URL_HOME_FOCUS_IMAGE = generateAdUrlHomeFocusImage();
        LogUtils.i(TAG, "start screen ad url template = " + AD_URL_START_SCREEN);
        LogUtils.i(TAG, "screen saver ad url template = " + AD_URL_SCREEN_SAVER);
        LogUtils.i(TAG, "exit app dialog ad url template = " + AD_URL_EXIT_APP_DIALOG);
        LogUtils.i(TAG, "focus image ad url template = " + AD_URL_HOME_FOCUS_IMAGE);
        LogUtils.i(TAG, "ad image resource ad url template  = " + AD_IMAGE_RESOURCE_URL);
    }

    private static void useTestServer(boolean isTest) {
        if (isTest) {
            LogUtils.d(TAG, "generate Ad url with IP address directly, then do not need to configure hosts");
            AD_IMAGE_RESOURCE_URL = AD_IMAGE_RESOURCE_URL.replace(ADDRESS_IMAGE_RESOURCE_AD, ADDRESS_IMAGE_RESOURCE_AD_T);
            AD_URL_START_SCREEN = AD_URL_START_SCREEN.replace(MIXER, MIXER_T);
            AD_URL_SCREEN_SAVER = AD_URL_SCREEN_SAVER.replace(MIXER, MIXER_T);
            AD_URL_EXIT_APP_DIALOG = AD_URL_EXIT_APP_DIALOG.replace(MIXER, MIXER_T);
            AD_URL_HOME_FOCUS_IMAGE = AD_URL_HOME_FOCUS_IMAGE.replace(MIXER, MIXER_T);
        }
    }

    private static String generateAdUrlStartScreen() {
        return WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&s=%s&t=%s&u=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&avd=%s&avn=%s";
    }

    private static String generateAdUrlScreenSaver() {
        return WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
    }

    private static String generateAdUrlExitAppDialog() {
        return WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
    }

    private static String generateAdUrlHomeFocusImage() {
        return WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
    }

    private static String generateAdURL() {
        return WebConstants.WEB_SITE_BASE_HTTP + MIXER + "/mixer?" + "a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s&j=%s&k=%s&l=%s&m=%s&n=%s&o=%s&p=%s" + "&q=%s&r=%s&v=%s&y=%s&z=%s&ai=%s&bd=%s&ea=%s&nw=%s&vd=%s&vn=%s&pi=%s&pc=%s&azt=%s";
    }

    private static String generateAdImageResourceUrl() {
        return WebConstants.WEB_SITE_BASE_HTTP + ADDRESS_IMAGE_RESOURCE_AD + "/creativeCache?" + "player_id=%s&template_type=%s&resolution=%s&dpi=%s&app_version=%s";
    }

    AdApi() {
    }

    public String fetchBannerAd(String adSdkVersion, String channelId) {
        return fetchBannerAd(adSdkVersion, channelId, "", "");
    }

    public String fetchBannerAd(String adSdkVersion, String channelId, String albumId, String tvQid) {
        CharSequence albumId2;
        CharSequence tvQid2;
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        String azt = "606";
        if (StringUtils.isEmpty((CharSequence) albumId)) {
            albumId2 = "";
        }
        if (StringUtils.isEmpty((CharSequence) tvQid)) {
            tvQid2 = "";
        }
        if (!(StringUtils.isEmpty(albumId2) || StringUtils.isEmpty(tvQid2))) {
            azt = "607";
        }
        String url = UrlUtils.urlFormat(AD_URL, TVApiBase.getTVApiProperty().getPassportDeviceId(), tvQid2, "", Project.getInstance().getBuild().getAdPlayerId(), Project.getInstance().getBuild().getVersionString(), Values.value16, "", albumId2, "gtv", "", channelId, Project.getInstance().getBuild().getVrsUUID(), Build.MODEL, DeviceUtils.getMd5FormatMacAddr(), uuid, "0", "9", adSdkVersion, isVip, "", "1", "", "1", CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo(), "", "", pi, pc, azt);
        String response = new HttpUtil(url).get();
        LogUtils.d(TAG, "banner ad request url = " + url);
        return response;
    }

    public String getScreenAd(String adSdkVersion) {
        String uuid = UUID.randomUUID().toString();
        String url = UrlUtils.urlFormat(AD_URL_START_SCREEN, TVApiBase.getTVApiProperty().getPassportDeviceId(), "", "", Project.getInstance().getBuild().getAdPlayerId(), Project.getInstance().getBuild().getVersionString(), Values.value16, "", "", "gtv", "", "", Project.getInstance().getBuild().getVrsUUID(), Build.MODEL, DeviceUtils.getMd5FormatMacAddr(), uuid, "0", "9", adSdkVersion, "1000000000397", "0", "0", "", "1", "", "1", CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo(), "", "", "", "", "", "");
        String response = new HttpUtil(url).get();
        LogUtils.d(TAG, "getScreenAd url = " + url);
        return response;
    }

    public String getScreenSaverAds(String adSdkVersion) {
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        LogUtils.d(TAG, "getScreenSaverAds() --- pi = " + pi + " pc = " + pc + " isVip = " + isVip);
        String url = UrlUtils.urlFormat(AD_URL_SCREEN_SAVER, TVApiBase.getTVApiProperty().getPassportDeviceId(), "", "", Project.getInstance().getBuild().getAdPlayerId(), Project.getInstance().getBuild().getVersionString(), Values.value16, "", "", "gtv", "", "", Project.getInstance().getBuild().getVrsUUID(), Build.MODEL, DeviceUtils.getMd5FormatMacAddr(), uuid, "0", "9", adSdkVersion, isVip, "", "1", "", "1", CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo(), "", "", pi, pc, "403");
        try {
            String response = new HttpUtil(url).get();
            LogUtils.d(TAG, "getScreenSaverAds() Ads Url = " + url);
            return response;
        } catch (Exception e) {
            LogUtils.d("TAG", "getScreenSaverAds() exception ", e);
            return "";
        }
    }

    public String getExitAppDialogAds(String adSdkVersion) {
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        LogUtils.d(TAG, "getExitAppDialogAds --- pi = " + pi + " pc = " + pc + " isVip = " + isVip);
        String url = UrlUtils.urlFormat(AD_URL_EXIT_APP_DIALOG, TVApiBase.getTVApiProperty().getPassportDeviceId(), "", "", Project.getInstance().getBuild().getAdPlayerId(), Project.getInstance().getBuild().getVersionString(), Values.value16, "", "", "gtv", "", "", Project.getInstance().getBuild().getVrsUUID(), Build.MODEL, DeviceUtils.getMd5FormatMacAddr(), uuid, "0", "9", adSdkVersion, isVip, "", "1", "", "1", CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo(), "", "", pi, pc, "601");
        try {
            String response = new HttpUtil(url).get();
            LogUtils.d(TAG, "getExitAppDialogAds Ads Url = " + url);
            return response;
        } catch (Exception e) {
            LogUtils.d("TAG", "getExitAppDialogAds exception ", e);
            return "";
        }
    }

    public String getAdImageResourceJSON() {
        DisplayMetrics dm = AppRuntimeEnv.get().getApplicationContext().getResources().getDisplayMetrics();
        LogUtils.d(TAG, "getAdImageResourceJSON, resolution = " + (dm.widthPixels + "," + dm.heightPixels));
        String url = UrlUtils.urlFormat(AD_IMAGE_RESOURCE_URL, "qc_100001_100145", "interstitial,exit", resolution, String.valueOf(dm.densityDpi), Project.getInstance().getBuild().getVersionString());
        LogUtils.d(TAG, "getAdImageResourceJSON, resource url = " + url);
        try {
            String response = new HttpUtil(url).get();
            LogUtils.d(TAG, "getAdImageResourceJSON, response = " + response);
            return response;
        } catch (Exception e) {
            LogUtils.d("TAG", "getScreenSaverAds() exception ", e);
            return "";
        }
    }

    public String getHomeFocusImageAds(String adSdkVersion) {
        long startTime = SystemClock.elapsedRealtime();
        String uuid = UUID.randomUUID().toString();
        String pi = GetInterfaceTools.getIGalaAccountManager().getUID();
        String pc = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        String isVip = userType == null ? "0" : (userType.isLitchi() || userType.isPlatinum()) ? "1" : "0";
        LogUtils.d(TAG, "getHomeFocusImageAds --- pi = " + pi + " pc = " + pc + " isVip = " + isVip);
        String url = UrlUtils.urlFormat(AD_URL_HOME_FOCUS_IMAGE, TVApiBase.getTVApiProperty().getPassportDeviceId(), "", "", Project.getInstance().getBuild().getAdPlayerId(), Project.getInstance().getBuild().getVersionString(), Values.value16, "", "", "gtv", "", "", Project.getInstance().getBuild().getVrsUUID(), Build.MODEL, DeviceUtils.getMd5FormatMacAddr(), uuid, "0", "9", adSdkVersion, isVip, "", "1", "", "1", CreateInterfaceTools.createBannerAdProvider().getAdNetworkInfo(), "", "", pi, pc, "602");
        LogUtils.d(TAG, "getHomeFocusImageAds Ads Url = " + url);
        return new HttpUtil(url).get();
    }
}
