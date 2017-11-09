package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model;

import android.os.Build;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tools.ITVApiDataProvider;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.JsonUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.UrlUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.webview.utils.WebSDKConstants;

public class WebViewDataImpl extends AbsWebViewData {
    private static final String TAG = "EPG/WebViewDataImpl";
    private JSONObject mJSONObject = null;

    public /* bridge */ /* synthetic */ void init() {
        super.init();
    }

    public /* bridge */ /* synthetic */ void putABTest(String str) {
        super.putABTest(str);
    }

    public /* bridge */ /* synthetic */ void putAndroidVersion() {
        super.putAndroidVersion();
    }

    public /* bridge */ /* synthetic */ void putApikey(String str) {
        super.putApikey(str);
    }

    public /* bridge */ /* synthetic */ void putAuthid(String str) {
        super.putAuthid(str);
    }

    public /* bridge */ /* synthetic */ void putAuthorization(String str) {
        super.putAuthorization(str);
    }

    public /* bridge */ /* synthetic */ void putChip(String str) {
        super.putChip(str);
    }

    public /* bridge */ /* synthetic */ void putCookie(String str) {
        super.putCookie(str);
    }

    public /* bridge */ /* synthetic */ void putCustomer(String str) {
        super.putCustomer(str);
    }

    public /* bridge */ /* synthetic */ void putDeviceId(String str) {
        super.putDeviceId(str);
    }

    public /* bridge */ /* synthetic */ void putDomain(String str) {
        super.putDomain(str);
    }

    public /* bridge */ /* synthetic */ void putEncryptKey(String str) {
        super.putEncryptKey(str);
    }

    public /* bridge */ /* synthetic */ void putEngine(String str) {
        super.putEngine(str);
    }

    public /* bridge */ /* synthetic */ void putHwver(String str) {
        super.putHwver(str);
    }

    public /* bridge */ /* synthetic */ void putIsDolby(boolean z) {
        super.putIsDolby(z);
    }

    public /* bridge */ /* synthetic */ void putIsH265(boolean z) {
        super.putIsH265(z);
    }

    public /* bridge */ /* synthetic */ void putIsHuawei(boolean z) {
        super.putIsHuawei(z);
    }

    public /* bridge */ /* synthetic */ void putIsLitchi(boolean z) {
        super.putIsLitchi(z);
    }

    public /* bridge */ /* synthetic */ void putIsPlayerMultiProcess(boolean z) {
        super.putIsPlayerMultiProcess(z);
    }

    public /* bridge */ /* synthetic */ void putLowConfig(boolean z) {
        super.putLowConfig(z);
    }

    public /* bridge */ /* synthetic */ void putMac(String str) {
        super.putMac(str);
    }

    public /* bridge */ /* synthetic */ void putMemory(int i) {
        super.putMemory(i);
    }

    public /* bridge */ /* synthetic */ void putMod(String str) {
        super.putMod(str);
    }

    public /* bridge */ /* synthetic */ void putP2(String str) {
        super.putP2(str);
    }

    public /* bridge */ /* synthetic */ void putPurchaseBtnTxt(String str) {
        super.putPurchaseBtnTxt(str);
    }

    public /* bridge */ /* synthetic */ void putSupportSmallWindow(boolean z) {
        super.putSupportSmallWindow(z);
    }

    public /* bridge */ /* synthetic */ void putTVHeight(int i) {
        super.putTVHeight(i);
    }

    public /* bridge */ /* synthetic */ void putTVWidth(int i) {
        super.putTVWidth(i);
    }

    public /* bridge */ /* synthetic */ void putUIType(String str) {
        super.putUIType(str);
    }

    public /* bridge */ /* synthetic */ void putUid(String str) {
        super.putUid(str);
    }

    public /* bridge */ /* synthetic */ void putUserAccount(String str) {
        super.putUserAccount(str);
    }

    public /* bridge */ /* synthetic */ void putUserName(String str) {
        super.putUserName(str);
    }

    public /* bridge */ /* synthetic */ void putUserType(int i) {
        super.putUserType(i);
    }

    public /* bridge */ /* synthetic */ void putUuid(String str) {
        super.putUuid(str);
    }

    public /* bridge */ /* synthetic */ void putVersion(String str) {
        super.putVersion(str);
    }

    public /* bridge */ /* synthetic */ void putVipDate(String str) {
        super.putVipDate(str);
    }

    public /* bridge */ /* synthetic */ void putVipMark(String str) {
        super.putVipMark(str);
    }

    public /* bridge */ /* synthetic */ void putVipTime(long j) {
        super.putVipTime(j);
    }

    public WebViewDataImpl() {
        if (this.mJSONObject == null) {
            this.mJSONObject = new JSONObject();
        }
        this.mJSONObject.clear();
    }

    public WebViewDataImpl(JSONObject jsonObject) {
        this.mJSONObject = jsonObject;
    }

    public void put(String key, Object value) {
        if (isNotNull()) {
            this.mJSONObject.put(key, value);
        } else {
            LogUtils.e(TAG, " mJSONObject put is null.");
        }
    }

    protected String getString(String key) {
        return isNotNull() ? this.mJSONObject.getString(key) : "";
    }

    private boolean isNotNull() {
        return this.mJSONObject != null;
    }

    protected int getIntValue(String key) {
        return isNotNull() ? this.mJSONObject.getIntValue(key) : 0;
    }

    protected long getLong(String key) {
        return this.mJSONObject == null ? 0 : this.mJSONObject.getLong(key).longValue();
    }

    public String getJson() {
        return JsonUtils.toJSONString(this.mJSONObject);
    }

    public String getUserInfoJson() {
        initUserJsonData();
        return getJson();
    }

    public void resetTVApi() {
        putDeviceId(getDeviceId());
        putApikey(getApikey());
        putAuthid(getAuthid());
        putEncryptKey(getEncryptKey());
        putAuthorization(getAuthorization());
    }

    public void initDynamicJsonData() {
        putABTest(getABTest());
        putPurchaseBtnTxt(getPurchaseBtnTxt());
    }

    public void initUserJsonData() {
        putCookie(getCookie());
        putUid(getUid());
        putUserAccount(getUserAccount());
        putUserName(getUserName());
        putUserType(getUserType());
        putIsLitchi(getIsLitchi());
    }

    public String getEncryptKey() {
        return ITVApiDataProvider.getInstance().getEncryptKey();
    }

    public String getAuthorization() {
        return ITVApiDataProvider.getInstance().getAuthorization();
    }

    public String getApikey() {
        return TVApiBase.getTVApiProperty().getApiKey();
    }

    public String getAuthid() {
        return TVApiBase.getTVApiProperty().getAuthId();
    }

    public String getMac() {
        return TVApiBase.getTVApiProperty().getMacAddress();
    }

    public String getVersion() {
        return Project.getInstance().getBuild().getVersionString();
    }

    public String getUuid() {
        return Project.getInstance().getBuild().getVrsUUID();
    }

    public String getCustomer() {
        return Project.getInstance().getBuild().getCustomerName();
    }

    public String getDomain() {
        return Project.getInstance().getBuild().getDomainName();
    }

    public String getUIType() {
        return Project.getInstance().getBuild().getUIVersionName();
    }

    public boolean getIsDolby() {
        return AppClientUtils.isSupportDolby();
    }

    public boolean getIsH265() {
        return AppClientUtils.isSupportH265();
    }

    public boolean getIsHuawei() {
        return false;
    }

    public boolean getLowConfig() {
        return MemoryLevelInfo.isLowConfigDevice();
    }

    public boolean getSupportSmallWindow() {
        return Project.getInstance().getBuild().isSupportSmallWindowPlay();
    }

    public String getHwver() {
        return Build.MODEL.replace(" ", "-");
    }

    public String getP2() {
        return Project.getInstance().getBuild().getPingbackP2();
    }

    public String getDeviceId() {
        return TVApiBase.getTVApiProperty().getPassportDeviceId();
    }

    public boolean getIsPlayerMultiProcess() {
        return Project.getInstance().getBuild().supportPlayerMultiProcess();
    }

    public int getTVWidth() {
        return ResourceUtil.getScreenWidth();
    }

    public int getTVHeight() {
        return ResourceUtil.getScreenHeight();
    }

    public String getChip() {
        return UrlUtils.urlEncode(DeviceUtils.getHardwareInfo());
    }

    public String getMod() {
        return PingBack.getInstance().getPingbackInitParams().sMod;
    }

    public int getMemory() {
        return AppRuntimeEnv.get().getTotalMemory();
    }

    public String getABTest() {
        return GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getABTest();
    }

    public String getPurchaseBtnTxt() {
        return GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getPurchaseButtonTxt();
    }

    public String getEngine() {
        return "webview";
    }

    public String getCookie() {
        return GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
    }

    public String getUid() {
        return GetInterfaceTools.getIGalaAccountManager().getUID();
    }

    public String getUserAccount() {
        return GetInterfaceTools.getIGalaAccountManager().getUserAccount();
    }

    public int getUserType() {
        return GetInterfaceTools.getIGalaAccountManager().getUserTypeForH5();
    }

    public String getUserName() {
        return GetInterfaceTools.getIGalaAccountManager().getUserName();
    }

    public String getVipDate() {
        return GetInterfaceTools.getIGalaAccountManager().getVipDate();
    }

    public long getVipTime() {
        return GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp();
    }

    public String getVipMark() {
        return null;
    }

    public boolean getIsLitchi() {
        return GetInterfaceTools.getIGalaAccountManager().getIsLitchiVipForH5();
    }

    public void clearData() {
        putBuyFrom("");
        putFrom("");
        putEventid("");
        putAlbum("");
        putAlbumList("");
        putState("");
        putBuyVip(0);
        putCouponActivityCode("");
        putCouponSignKey("");
    }

    public void putFrom(String from) {
        put("from", from);
    }

    public String getFrom() {
        return getString("from");
    }

    public void putBuySource(String buy_source) {
        put("buy_source", buy_source);
    }

    public String getBuySource() {
        return getString("buy_source");
    }

    public void putEventid(String eventid) {
        put(WebSDKConstants.PARAM_KEY_EVENTID, eventid);
    }

    public String getEventid() {
        return getString(WebSDKConstants.PARAM_KEY_EVENTID);
    }

    public void putTagLIVE(int tagLive) {
        put(WebConstants.TAG_KEY_LIVE, Integer.valueOf(tagLive));
    }

    public int getTagLIVE() {
        return getIntValue(WebConstants.TAG_KEY_LIVE);
    }

    public void putPLId(String id) {
        put("id", id);
    }

    public String getPLId() {
        return getString("id");
    }

    public void putPLName(String name) {
        put(WebSDKConstants.PARAM_KEY_PL_NAME, name);
    }

    public String getPLName() {
        return getString(WebSDKConstants.PARAM_KEY_PL_NAME);
    }

    public void putAlbumList(String album_list) {
        put(WebSDKConstants.PARAM_KEY_ALBUM_LIST, album_list);
    }

    public String getAlbumList() {
        return getString(WebSDKConstants.PARAM_KEY_ALBUM_LIST);
    }

    public void putAlbum(String album) {
        put("album", album);
    }

    public String getAlbum() {
        return getString("album");
    }

    public void putFlowerList(String flowerList) {
        put(WebSDKConstants.PARAM_KEY_LIVE_FLOWERLIST, flowerList);
    }

    public String getFlowerList() {
        return getString(WebSDKConstants.PARAM_KEY_LIVE_FLOWERLIST);
    }

    public void putPageType(int page_type) {
        put(WebSDKConstants.PARAM_KEY_PAGE_TYPE, Integer.valueOf(page_type));
    }

    public int getPageType() {
        return getIntValue(WebSDKConstants.PARAM_KEY_PAGE_TYPE);
    }

    public void putEnterType(int enter_type) {
        put(WebSDKConstants.PARAM_KEY_ENTER_TYPE, Integer.valueOf(enter_type));
    }

    public int getEnterType() {
        return getIntValue(WebSDKConstants.PARAM_KEY_ENTER_TYPE);
    }

    public void putBuyFrom(String buy_from) {
        put("buy_from", buy_from);
    }

    public String getBuyFrom() {
        return getString("buy_from");
    }

    public void putBuyVip(int buy_vip) {
        put(WebConstants.PARAM_KEY_BUY_VIP, Integer.valueOf(buy_vip));
    }

    public String getBuyVip() {
        return getString(WebConstants.PARAM_KEY_BUY_VIP);
    }

    public void putState(String state) {
        put("state", state);
    }

    public String getState() {
        return getString("state");
    }

    public void putResGroupId(String resgoupId) {
        put(WebConstants.PARAM_KEY_RESGROUP_ID, resgoupId);
    }

    public String getResGroupId() {
        return getString(WebConstants.PARAM_KEY_RESGROUP_ID);
    }

    public void putIncomeSrc(String incomesrc) {
        put("incomesrc", incomesrc);
    }

    public String getIncomeSrc() {
        return getString("incomesrc");
    }

    public void putCouponActivityCode(String couponActivityCode) {
        put("coupon_activity_code", couponActivityCode);
    }

    public void putCouponSignKey(String couponSignKey) {
        put(LoginConstant.COUPON_SIGN_KEY, couponSignKey);
    }
}
