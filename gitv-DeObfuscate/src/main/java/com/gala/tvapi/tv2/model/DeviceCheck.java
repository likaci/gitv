package com.gala.tvapi.tv2.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.type.AppLayout;
import com.gala.tvapi.type.HomePageBuildType;
import com.gala.tvapi.type.PayeeType;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import java.util.List;

public class DeviceCheck extends Model {
    private static final long serialVersionUID = 1;
    public String apiKey;
    public String authId;
    public Cnf cnf;
    public List<String> function;
    public String hide;
    public String ip;
    public String ipLoc;
    public List<String> kv;
    public String loading;
    public List<ResId> resIds;
    public int support4k = 0;
    public int supporth265 = 0;
    public String sysTime;
    public List<TabInfo> tab;
    public String tip;
    public int upgradeType;
    public String url;
    public String verMon = "";
    public String verPkg = "";
    public String verTvod = "";
    public String version;
    public int vipMon = 0;
    public int vipPkg = 0;
    public int vipTvod = 0;

    public boolean isSupport4K() {
        return this.support4k == 1;
    }

    public boolean isSupportH265() {
        return this.supporth265 == 1;
    }

    public String[] getIpRegion() {
        if (this.ipLoc == null || this.ipLoc.isEmpty()) {
            return null;
        }
        return this.ipLoc.split("-");
    }

    public int getRetryTimesBerforeStarted() {
        try {
            return Integer.valueOf(this.cnf.retry_times_berfore_started).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getRetryTimesAfterStarted() {
        try {
            return Integer.valueOf(this.cnf.retry_times_after_started).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasUpdateVersion() {
        return !C0214a.m592a(this.url);
    }

    public boolean isMustUpdate() {
        return this.upgradeType == 1;
    }

    public boolean isRunMan3TabAvailable() {
        return getKv("runMan3TabAvailable", false);
    }

    public boolean isHasAppStore() {
        return getKv("appStore", false);
    }

    public boolean isHasRecommend() {
        return getKv("pRecommend", false);
    }

    public boolean isHasTVTab() {
        return getKv("tvTab", false);
    }

    public boolean isOpenCDN() {
        return getKv("hcdn", false);
    }

    public boolean isHasHuaweiQuickMark() {
        return getKv("hw_ewm", false);
    }

    public boolean isDisableNativePlayerSafeMode() {
        return getKv("disableNativePlayerSafeMode", false);
    }

    public boolean isDisableNativePlayerAdvanceMode() {
        return getKv("disableNativePlayerAdvanceMode", false);
    }

    public boolean isOnlineTab() {
        return getKv("onlineTab", false);
    }

    public boolean isPayBeforePreview() {
        return getKv("payBeforePreview", false);
    }

    public boolean isPayAfterPreview() {
        return getKv("payAfterPreview", false);
    }

    public boolean isPackageBeforePay() {
        return getKv("packageBeforePay", false);
    }

    public boolean isPayBeforePackage() {
        return getKv("payBeforePackage", false);
    }

    public boolean isYinHePayee() {
        return getKv("yinHePayee", false);
    }

    public boolean isGalaPayee() {
        return false;
    }

    public PayeeType getPayeeType() {
        if (this.kv != null && this.kv.size() > 0) {
            for (String str : this.kv) {
                String str2;
                if (!isEmpty(str2, "payee")) {
                    str2 = getValue(str2, "sel");
                    if (str2 != null && str2.equals(PayeeType.GALA.toString())) {
                        return PayeeType.GALA;
                    }
                    if (str2 != null && str2.equals(PayeeType.YINHE.toString())) {
                        return PayeeType.YINHE;
                    }
                }
            }
        }
        return PayeeType.GALA;
    }

    public boolean isPushVideoByTVPlatform() {
        return getKv("pushVideoTV", false);
    }

    public boolean isRefeshHomePageOnlyForLaunchAndEvent() {
        if (this.kv != null && this.kv.size() > 0) {
            for (String str : this.kv) {
                if (!isEmpty(str, "homePageRefresh") && getValue(str, "sel").equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRefeshHomePageOnlyForLaunch() {
        if (this.kv != null && this.kv.size() > 0) {
            for (String str : this.kv) {
                if (!isEmpty(str, "homePageRefresh") && getValue(str, "sel").equals("2")) {
                    return true;
                }
            }
        }
        return false;
    }

    public AppLayout getAppLayout() {
        if (isHasAppStore()) {
            return AppLayout.APPSTORE;
        }
        if (isOnlineTab()) {
            return AppLayout.ONLINE;
        }
        return AppLayout.CHAOQIING;
    }

    public boolean isEnableCarousel() {
        return getKv("enableCarousel", false);
    }

    public String getPlayerConfig() {
        return getKv(InterfaceKey.PLAYER_CP, "ruleCode");
    }

    public String getPlayerConfig2() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString(InterfaceKey.PLAYER_CP);
                    if (!(parseObject2 == null || parseObject2.equals(""))) {
                        return parseObject2;
                    }
                }
            }
        }
        return "";
    }

    public String getABTest() {
        return getKv("abTest", "sel");
    }

    public String getLogResident() {
        return getKv("log_Resident", "sel");
    }

    private boolean isEmpty(String s, String key) {
        JSONObject parseObject = JSON.parseObject(s);
        if (parseObject != null) {
            String string = parseObject.getString(key);
            if (!(string == null || string.equals(""))) {
                return false;
            }
        }
        return true;
    }

    private String getValue(String s, String key) {
        JSONObject parseObject = JSON.parseObject(s);
        if (parseObject != null) {
            String string = parseObject.getString(key);
            if (!(string == null || string.equals(""))) {
                return string;
            }
        }
        return "";
    }

    public boolean isSupportVipPkg() {
        return this.vipPkg == 1;
    }

    public boolean isSupportVipTvod() {
        return this.vipTvod == 1;
    }

    public boolean isSupportVipMon() {
        return this.vipMon == 1;
    }

    public VipGuideInfo getVipGuideInfo() {
        VipGuideInfo vipGuideInfo = new VipGuideInfo();
        if (C0214a.m592a(this.cnf.membership_purchase_guide_info)) {
            return null;
        }
        try {
            return (VipGuideInfo) JSON.parseObject(this.cnf.membership_purchase_guide_info, VipGuideInfo.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isDisableCrosswalk() {
        return getKv("disableCrosswalk", false);
    }

    public HomePageBuildType getHomePageBuildType() {
        if (this.kv != null && this.kv.size() > 0) {
            for (String str : this.kv) {
                String str2;
                if (!isEmpty(str2, "homepagebuildtype")) {
                    str2 = getValue(str2, "sel");
                    if (str2 != null && str2.equals(HomePageBuildType.COCOS2D.getValue())) {
                        return HomePageBuildType.COCOS2D;
                    }
                }
            }
        }
        return HomePageBuildType.JAVA;
    }

    public boolean isDisableNDUpload() {
        return getKv("disableNDUpload", false);
    }

    public boolean isDisableP2PUpload() {
        return getKv("disableP2PUpload", false);
    }

    public int getAppCard() {
        return getFuntion("appCard", 1);
    }

    public int getMsgDialogPos() {
        return getFuntion("msg_dialog_pos", 4);
    }

    public boolean isMsgDialogOutAPP() {
        return getKv("msg_dialog_isOutApp", true);
    }

    public boolean isShowExitAppDialog() {
        return getKv("isShowExitAppDialog", true);
    }

    public boolean isDisableHCDNPreDeploy() {
        return getFuntionBoolean("disableHCDNPreDeploy", false);
    }

    public boolean isSupportGif() {
        return getKv("isSupportGif", true);
    }

    public int adSkipFrequency() {
        return getFuntion("ad_skip_frequency", 0);
    }

    public String getDailyNewsIcons() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("dailyInforCornerMark");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        return parseObject2;
                    }
                }
            }
        }
        return "";
    }

    public String getChildAppUrl() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("childapp");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        return parseObject2;
                    }
                }
            }
        }
        return "";
    }

    public String getChinaPokerAppUrl() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("chinapokerapp");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        return parseObject2;
                    }
                }
            }
        }
        return "";
    }

    public String getSubChannelPlayerConfig() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("subChannelPlayerConfig");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        return parseObject2;
                    }
                }
            }
        }
        return "";
    }

    public boolean isOpenRootCheck() {
        return getKv("isOpenRootCheck", false);
    }

    public boolean isOpenAdVipGuide() {
        return getKv("ad_guide_become_vip_close", true);
    }

    private int getFuntion(String key, int def) {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString(key);
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        try {
                            def = Integer.parseInt(parseObject2);
                            break;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return def;
    }

    private boolean getKv(String key, boolean def) {
        if (this.kv == null || this.kv.size() <= 0) {
            return def;
        }
        for (String isEmpty : this.kv) {
            if (!isEmpty(isEmpty, key)) {
                return !def;
            }
        }
        return def;
    }

    private String getKv(String str, String str2) {
        if (this.kv != null && this.kv.size() > 0) {
            for (String parseObject : this.kv) {
                JSONObject parseObject2 = JSON.parseObject(parseObject);
                if (parseObject2 != null) {
                    String string = parseObject2.getString(Album.KEY);
                    if (!(string == null || string.equals(""))) {
                        return parseObject2.getString("key2");
                    }
                }
            }
        }
        return "";
    }

    public boolean isDisableAdCache() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("isDisableAdCache");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        try {
                            return Integer.parseInt(parseObject2) == 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCardSort() {
        if (this.function != null && this.function.size() > 0) {
            for (String parseObject : this.function) {
                String parseObject2;
                JSONObject parseObject3 = JSON.parseObject(parseObject2);
                if (parseObject3 != null) {
                    parseObject2 = parseObject3.getString("card_sort");
                    if (!(parseObject2 == null || parseObject2.isEmpty())) {
                        try {
                            return Integer.parseInt(parseObject2) == 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getAllTagPosition() {
        return getFuntion("alltag_position", 0);
    }

    public boolean isEnablePlayerLocalServer() {
        return getFuntionBoolean("enablePlayerLocalServer", false);
    }

    public boolean enablePlayerLocalServerF4v2Hls() {
        return getFuntionBoolean("enablePlayerLocalServerF4v2Hls", false);
    }

    public boolean enablePlayerLocalServerStream() {
        return getFuntionBoolean("enablePlayerLocalServerStream", false);
    }

    public boolean disableHcdnDaemon() {
        return getFuntionBoolean("disableHcdnDaemon", false);
    }

    public boolean isShowGuideLogin() {
        return getFuntionBoolean("is_show_guide_login", false);
    }

    public boolean isOpenSignin() {
        return getFuntionBoolean("signin", false);
    }

    public boolean isOpenSigninRecommend() {
        return getFuntionBoolean("signin_recommend", true);
    }

    private boolean getFuntionBoolean(String key, boolean def) {
        if (this.function == null || this.function.size() <= 0) {
            return def;
        }
        for (String parseObject : this.function) {
            String parseObject2;
            JSONObject parseObject3 = JSON.parseObject(parseObject2);
            if (parseObject3 != null) {
                parseObject2 = parseObject3.getString(key);
                if (parseObject2 != null) {
                    if ("true".equals(parseObject2)) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return def;
    }
}
