package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model;

import android.os.Build.VERSION;
import com.gala.video.webview.data.WebBaseContract.IWebDufaultField;
import com.gala.video.webview.utils.WebSDKConstants;

abstract class AbsWebViewData implements IWebDufaultField {
    protected abstract int getIntValue(String str);

    public abstract String getJson();

    protected abstract long getLong(String str);

    protected abstract String getString(String str);

    public abstract void put(String str, Object obj);

    AbsWebViewData() {
    }

    public void init() {
        putApikey(getApikey());
        putAuthid(getAuthid());
        putMac(getMac());
        putVersion(getVersion());
        putUuid(getUuid());
        putCustomer(getCustomer());
        putDomain(getDomain());
        putUIType(getUIType());
        putIsDolby(getIsDolby());
        putIsH265(getIsH265());
        putIsHuawei(getIsHuawei());
        putLowConfig(getLowConfig());
        putSupportSmallWindow(getSupportSmallWindow());
        putHwver(getHwver());
        putP2(getP2());
        putDeviceId(getDeviceId());
        putIsPlayerMultiProcess(getIsPlayerMultiProcess());
        putTVWidth(getTVWidth());
        putTVHeight(getTVHeight());
        putChip(getChip());
        putMod(getMod());
        putMemory(getMemory());
        putAndroidVersion();
        putCookie(getCookie());
        putUid(getUid());
        putUserAccount(getUserAccount());
        putUserType(getUserType());
        putUserName(getUserName());
        putVipMark(getVipMark());
        putIsLitchi(getIsLitchi());
        putABTest(getABTest());
        putPurchaseBtnTxt(getPurchaseBtnTxt());
        putEngine(getEngine());
    }

    public void putApikey(String apikey) {
        put("apikey", apikey);
    }

    public void putAuthid(String authid) {
        put(WebSDKConstants.PARAM_KEY_AUTH_ID, authid);
    }

    public void putMac(String mac) {
        put(WebSDKConstants.PARAM_KEY_MAC, mac);
    }

    public void putVersion(String version) {
        put("version", version);
    }

    public void putUuid(String uuid) {
        put("uuid", uuid);
    }

    public void putCustomer(String customer) {
        put(WebSDKConstants.PARAM_KEY_CUSTOMER, customer);
    }

    public void putDomain(String domain) {
        put(WebSDKConstants.PARAM_KEY_DOMAIN, domain);
    }

    public void putUIType(String ui_type) {
        put(WebSDKConstants.PARAM_KEY_UI_TYPE, ui_type);
    }

    public void putIsDolby(boolean is_dolby) {
        put(WebSDKConstants.PARAM_KEY_SUPPORT_DOLBY, Boolean.valueOf(is_dolby));
    }

    public void putIsH265(boolean is_h265) {
        put(WebSDKConstants.PARAM_KEY_SUPPORT_H265, Boolean.valueOf(is_h265));
    }

    public void putIsHuawei(boolean is_huawei) {
        put(WebSDKConstants.PARAM_KEY_IS_HUAWEI, Boolean.valueOf(is_huawei));
    }

    public void putLowConfig(boolean low_config) {
        put(WebSDKConstants.PARAM_KEY_IS_LOW_CONFIG, Boolean.valueOf(low_config));
    }

    public void putSupportSmallWindow(boolean support_small_window) {
        put(WebSDKConstants.PARAM_KEY_SUPPORT_SMALL_WINDOW, Boolean.valueOf(support_small_window));
    }

    public void putHwver(String hwver) {
        put(WebSDKConstants.PARAM_KEY_HWVER, hwver);
    }

    public void putP2(String p2) {
        put(WebSDKConstants.PARAM_KEY_P2, p2);
    }

    public void putDeviceId(String deviceId) {
        put(WebSDKConstants.PARAM_KEY_DEVICEID, deviceId);
    }

    public void putIsPlayerMultiProcess(boolean is_player_multi_process) {
        put(WebSDKConstants.PARAM_KEY_IS_PLAYER_MULTI_PROCESS, Boolean.valueOf(is_player_multi_process));
    }

    public void putTVWidth(int tv_width) {
        put(WebSDKConstants.PARAM_KEY_WIDTH, Integer.valueOf(tv_width));
    }

    public void putTVHeight(int tv_height) {
        put(WebSDKConstants.PARAM_KEY_HEIGHT, Integer.valueOf(tv_height));
    }

    public void putChip(String chip) {
        put(WebSDKConstants.PARAM_KEY_CHIP, chip);
    }

    public void putMod(String mod) {
        put(WebSDKConstants.PARAM_KEY_MOD, mod);
    }

    public void putMemory(int memory) {
        put(WebSDKConstants.PARAM_KEY_MEMORY, Integer.valueOf(memory));
    }

    public void putCookie(String cookie) {
        put(WebSDKConstants.PARAM_KEY_COOKIE, cookie);
    }

    public void putUid(String uid) {
        put(WebSDKConstants.PARAM_KEY_UID, uid);
    }

    public void putUserAccount(String user_account) {
        put(WebSDKConstants.PARAM_KEY_USER_ACCOUNT, user_account);
    }

    public void putUserType(int user_type) {
        put(WebSDKConstants.PARAM_KEY_USER_TYPE, Integer.valueOf(user_type));
    }

    public void putUserName(String user_name) {
        put(WebSDKConstants.PARAM_KEY_USER_NAME, user_name);
    }

    public void putVipDate(String vip_date) {
        put(WebSDKConstants.PARAM_KEY_VIP_DATE, vip_date);
    }

    public void putVipTime(long vip_time) {
        put(WebSDKConstants.PARAM_KEY_VIP_TIME, Long.valueOf(vip_time));
    }

    public void putVipMark(String vip_mark) {
        put(WebSDKConstants.PARAM_KEY_VIP_MARKE, vip_mark);
    }

    public void putIsLitchi(boolean isLitchi) {
        put(WebSDKConstants.PARAM_KEY_VIP_ISLITCHI, Boolean.valueOf(isLitchi));
    }

    public void putABTest(String ab_test) {
        put(WebSDKConstants.PARAM_KEY_AB_TEST, ab_test);
    }

    public void putPurchaseBtnTxt(String purchase_button_txt) {
        put(WebSDKConstants.PARAM_KEY_PURCHASE_BUTTON_TXT, purchase_button_txt);
    }

    public void putEngine(String engine) {
        put(WebSDKConstants.PARAM_KEY_ENGINE_CORE, engine);
    }

    public void putEncryptKey(String aeskey) {
        put("aeskey", aeskey);
    }

    public void putAuthorization(String auth) {
        put("authorization", auth);
    }

    public void putAndroidVersion() {
        put("androidVerison", Integer.valueOf(VERSION.SDK_INT));
    }
}
