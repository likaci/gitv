package com.gala.video.lib.share.ifimpl.dynamic;

import com.gala.tvapi.tv2.model.VipGuideInfo;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.lib.framework.core.cache.DynamicCache;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.ApiConstants;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import com.gala.video.webview.utils.WebSDKConstants;
import com.mcto.ads.internal.net.PingbackConstants;
import com.tvos.appdetailpage.client.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.cybergarage.soap.SOAP;

class DynamicResult implements IDynamicResult, Serializable {
    public static final String DNMC_CFG_URLS = "DynamicConfigImagePaths";
    private static final String TAG = "Startup/DynamicResult";
    private static final long serialVersionUID = 1;

    void setStartLoading(String v) {
        DynamicCache.get().putString("startLoading", v);
    }

    public String getStartLoading() {
        return DynamicCache.get().getString("startLoading", "");
    }

    void setPlayLoading(String v) {
        DynamicCache.get().putString("playLoading", v);
    }

    public String getPlayLoading() {
        return DynamicCache.get().getString("playLoading", "");
    }

    void setBootUrlString(String v) {
        DynamicCache.get().putString("bootUrl", v);
    }

    public String getBootUrlString() {
        return DynamicCache.get().getString("bootUrl", "");
    }

    void setStartUrl(String v) {
        DynamicCache.get().putString("startUrl", v);
    }

    public String getStartUrl() {
        return DynamicCache.get().getString("startUrl", "");
    }

    void setBugVipTipPicUrl(String v) {
        DynamicCache.get().putString("bug_vip_tip_pic_url", v);
    }

    public String getBugVipTipPicUrl() {
        return DynamicCache.get().getString("bug_vip_tip_pic_url", "");
    }

    void setHeadUrl(String v) {
        DynamicCache.get().putString("headUrl", v);
    }

    public String getHeadUrl() {
        return DynamicCache.get().getString("headUrl", "");
    }

    void setHeadLogoUrl(String v) {
        DynamicCache.get().putString("headLogoUrl", v);
    }

    public String getHeadLogoUrl() {
        return DynamicCache.get().getString("headLogoUrl", "");
    }

    void setPlayUrlString(String v) {
        DynamicCache.get().putString("playUrl", v);
    }

    public String getPlayUrlString() {
        return DynamicCache.get().getString("playUrl", "");
    }

    void setDefUrlString(String v) {
        DynamicCache.get().putString("defUrl", v);
    }

    public String getDefUrlString() {
        return DynamicCache.get().getString("defUrl", "");
    }

    void setServUrlString(String v) {
        DynamicCache.get().putString("servUrl", v);
    }

    public String getServUrlString() {
        return DynamicCache.get().getString("servUrl", "");
    }

    void setWaterUrlString(String v) {
        DynamicCache.get().putString("waterUrl", v);
    }

    public String getWaterUrlString() {
        return DynamicCache.get().getString("waterUrl", "");
    }

    void setISeeUrlString(String v) {
        DynamicCache.get().putString("iseeUrl", v);
    }

    public String getISeeUrlString() {
        return DynamicCache.get().getString("iseeUrl", "");
    }

    void setMulCtr(String v) {
        DynamicCache.get().putString("mulCtr", v);
    }

    public String getMulCtr() {
        return DynamicCache.get().getString("mulCtr", "");
    }

    void setMulVip(String v) {
        DynamicCache.get().putString("mulVip", v);
    }

    public String getMulVip() {
        return DynamicCache.get().getString("mulVip", "");
    }

    void setPlatCnt(String v) {
        DynamicCache.get().putString("platCnt", v);
    }

    public String getPlatCnt() {
        return DynamicCache.get().getString("platCnt", "");
    }

    void setIChn(String v) {
        DynamicCache.get().putString("iChn", v);
    }

    public String getIChn() {
        return DynamicCache.get().getString("iChn", "");
    }

    void setFaq(String v) {
        DynamicCache.get().putString("faq", v);
    }

    public String getFaq() {
        return DynamicCache.get().getString("faq", "");
    }

    void setName(String v) {
        DynamicCache.get().putString(WebSDKConstants.PARAM_KEY_PL_NAME, v);
    }

    public String getName() {
        return DynamicCache.get().getString(WebSDKConstants.PARAM_KEY_PL_NAME, "");
    }

    void setDesc(String v) {
        DynamicCache.get().putString(Constants.USERGAME_ORDER_DESC, v);
    }

    public String getDesc() {
        return DynamicCache.get().getString(Constants.USERGAME_ORDER_DESC, "");
    }

    void setOther(String v) {
        DynamicCache.get().putString(ISearchConstant.TVSRCHSOURCE_OTHER, v);
    }

    public String getOther() {
        return DynamicCache.get().getString(ISearchConstant.TVSRCHSOURCE_OTHER, "");
    }

    void setExit(String v) {
        DynamicCache.get().putString("exit", v);
    }

    public String getExit() {
        return DynamicCache.get().getString("exit", "");
    }

    void setVerErr(String v) {
        DynamicCache.get().putString("verErr", v);
    }

    public String getVerErr() {
        return DynamicCache.get().getString("verErr", "");
    }

    void setDevErr(String v) {
        DynamicCache.get().putString("devErr", v);
    }

    public String getDevErr() {
        return DynamicCache.get().getString("devErr", "");
    }

    void setPreOver(String v) {
        DynamicCache.get().putString("preOver", v);
    }

    public String getPreOver() {
        return DynamicCache.get().getString("preOver", "");
    }

    void setUtime(String v) {
        DynamicCache.get().putString("utime", v);
    }

    public String getUtime() {
        return DynamicCache.get().getString("utime", "");
    }

    void setCtime(String v) {
        DynamicCache.get().putString("ctime", v);
    }

    public String getCtime() {
        return DynamicCache.get().getString("ctime", "");
    }

    void setPinfo(String v) {
        DynamicCache.get().putString("pinfo", v);
    }

    public String getPinfo() {
        return DynamicCache.get().getString("pinfo", "");
    }

    void setCServer(String v) {
        DynamicCache.get().putString("cserver", v);
    }

    public String getCServer() {
        return DynamicCache.get().getString("cserver", "");
    }

    void setNcinfo(String v) {
        DynamicCache.get().putString("ncinfo", v);
    }

    public String getNcinfo() {
        return DynamicCache.get().getString("ncinfo", "");
    }

    void setFree(String v) {
        DynamicCache.get().putString("free", v);
    }

    public String getFree() {
        return DynamicCache.get().getString("free", "");
    }

    void setFtinfo(String v) {
        DynamicCache.get().putString("ftinfo", v);
    }

    public String getFtinfo() {
        return DynamicCache.get().getString("ftinfo", "");
    }

    void setPhone(String v) {
        DynamicCache.get().putString("phone", v);
    }

    public String getPhone() {
        return DynamicCache.get().getString("phone", "");
    }

    void setAd(String v) {
        DynamicCache.get().putString(PingbackConstants.AD_EVENTS, v);
    }

    public String getAd() {
        return DynamicCache.get().getString(PingbackConstants.AD_EVENTS, "");
    }

    void setPlayerLogoString(String v) {
        DynamicCache.get().putString("playerLogo", v);
    }

    public String getPlayerLogoString() {
        return DynamicCache.get().getString("playerLogo", "");
    }

    void setPlayerBackColour(String v) {
        DynamicCache.get().putString("playerBackColour", v);
    }

    public String getPlayerBackColour() {
        return DynamicCache.get().getString("playerBackColour", "");
    }

    void setPhoneTips(String v) {
        DynamicCache.get().putString("phoneTips", v);
    }

    public String getPhoneTips() {
        return DynamicCache.get().getString("phoneTips", "");
    }

    void setDocument(String v) {
        DynamicCache.get().putString("document", v);
    }

    public String getDocument() {
        return DynamicCache.get().getString("document", "");
    }

    void setCodeUrlString(String v) {
        DynamicCache.get().putString("codeURL", v);
    }

    public String getCodeUrlString() {
        return DynamicCache.get().getString("codeURL", "");
    }

    void setFeedbackInfoTip(String v) {
        DynamicCache.get().putString("feedbackInfoTip", v);
    }

    public String getFeedbackInfoTip() {
        return DynamicCache.get().getString("feedbackInfoTip", "");
    }

    void setIsDisableSafeMode(boolean v) {
        DynamicCache.get().putBoolean("isDisableSafeMode", v);
    }

    public boolean getIsDisableSafeMode() {
        return DynamicCache.get().getBoolean("isDisableSafeMode", false);
    }

    void setVideoSourceUrlString(String v) {
        DynamicCache.get().putString("videoSourceUrl", v);
    }

    public String getVideoSourceUrlString() {
        return DynamicCache.get().getString("videoSourceUrl", "");
    }

    void setJstvString(String v) {
        DynamicCache.get().putString("jstvUrl", v);
    }

    public String getJstvString() {
        return DynamicCache.get().getString("jstvUrl", "");
    }

    void setPpsUrlString(String v) {
        DynamicCache.get().putString("ppsUrl", v);
    }

    public String getPpsUrlString() {
        return DynamicCache.get().getString("ppsUrl", "");
    }

    void setJstvList(String v) {
        DynamicCache.get().putString("jstvList", v);
    }

    public String getJstvList() {
        return DynamicCache.get().getString("jstvList", "");
    }

    void setPPSList(String v) {
        DynamicCache.get().putString("ppsList", v);
    }

    public String getPPSList() {
        return DynamicCache.get().getString("ppsList", "");
    }

    void setDailyLabels(String v) {
        DynamicCache.get().putString("dailyLabels", v);
    }

    public String getDailyLabels() {
        return DynamicCache.get().getString("dailyLabels", "");
    }

    void setDailyIds(String v) {
        DynamicCache.get().putString("dailyIds", v);
    }

    public String getDailyIds() {
        return DynamicCache.get().getString("dailyIds", "");
    }

    void setDailyIcon(String v) {
        DynamicCache.get().putString("dailyIcon", v);
    }

    public String getDailyIcon() {
        return DynamicCache.get().getString("dailyIcon", "");
    }

    void setPlayNewUrlString(String v) {
        DynamicCache.get().putString("playNewUrl", v);
    }

    public String getPlayNewUrlString() {
        return DynamicCache.get().getString("playNewUrl", "");
    }

    public List<String> getPlayNewUrl() {
        return checkAndGetUrl(15);
    }

    void setHdrGuideBgImageUrls(String v) {
        DynamicCache.get().putString("guide_bg_hdr_url", v);
    }

    public String getHdrGuideBgImgUrls() {
        return DynamicCache.get().getString("guide_bg_hdr_url", "");
    }

    public List<String> getHdrGuideBgImgPaths() {
        return checkAndGetUrl(26);
    }

    void setHdrGuideBottomDesc(String v) {
        DynamicCache.get().putString("guide_hdr_bottom_desc", v);
    }

    public String getHdrGuideBottomDesc() {
        return DynamicCache.get().getString("guide_hdr_bottom_desc", "");
    }

    void set4kGuideBgImageUrls(String v) {
        DynamicCache.get().putString("guide_bg_4k_url", v);
    }

    public String get4kGuideBgImgUrls() {
        return DynamicCache.get().getString("guide_bg_4k_url", "");
    }

    public List<String> get4kGuideBgImgPaths() {
        return checkAndGetUrl(27);
    }

    void set4kGuideBottomDesc(String v) {
        DynamicCache.get().putString("guide_4k_bottom_desc", v);
    }

    public String get4kGuideBottomDesc() {
        return DynamicCache.get().getString("guide_4k_bottom_desc", "");
    }

    void set1080pGuideBgImageUrls(String v) {
        DynamicCache.get().putString("guide_bg_1080p_url", v);
    }

    public String get1080pGuideBgImgUrls() {
        return DynamicCache.get().getString("guide_bg_1080p_url", "");
    }

    public List<String> get1080pGuideBgImgPaths() {
        return checkAndGetUrl(28);
    }

    void setVipMonthOperateImageUrls(String v) {
        DynamicCache.get().putString("detail_month_vip_bg", v);
    }

    public String getVipMonthOperateImageUrls() {
        return DynamicCache.get().getString("detail_month_vip_bg", "");
    }

    public List<String> getVipMonthOperateImagePath() {
        return checkAndGetUrl(29);
    }

    void setDetailFreeVideoOperateImageUrls(String v) {
        DynamicCache.get().putString("detail_free_video_bg", v);
    }

    public String getDetailFreeVideoOperateImageUrls() {
        return DynamicCache.get().getString("detail_free_video_bg", "");
    }

    public List<String> getDetailFreeVideoOperatImagePath() {
        return checkAndGetUrl(30);
    }

    void setOperationImageResourceIds(String v) {
        DynamicCache.get().putString("operation_pic_resource_ids", v);
    }

    public String getOperationImageResourceIds() {
        return DynamicCache.get().getString("operation_pic_resource_ids", "");
    }

    public String getSpecifiedOperateImageResId(OperationImageType type) {
        if (type == null) {
            LogUtils.w(TAG, "current operation image type is null");
            return "";
        }
        String ids = getOperationImageResourceIds();
        String[] idArr = null;
        if (ids != null) {
            idArr = ids.split(",");
        }
        String specId = "";
        switch (type) {
            case START:
                if (idArr == null || idArr.length <= 0) {
                    return specId;
                }
                return idArr[0];
            case SCREENSAVER:
                if (idArr == null || idArr.length <= 1) {
                    return specId;
                }
                return idArr[1];
            case EXIT:
                if (idArr == null || idArr.length <= 2) {
                    return specId;
                }
                return idArr[2];
            default:
                return specId;
        }
    }

    void setDailyName(String v) {
        DynamicCache.get().putString("dailyName", v);
    }

    public String getDailyName() {
        return DynamicCache.get().getString("dailyName", "");
    }

    void setPayBeforePreview(boolean v) {
        DynamicCache.get().putBoolean("payBeforePreview", v);
    }

    public boolean getPayBeforePreview() {
        return DynamicCache.get().getBoolean("payBeforePreview", false);
    }

    void setPayAfterPreview(boolean v) {
        DynamicCache.get().putBoolean("payAfterPreview", v);
    }

    public boolean getPayAfterPreview() {
        return DynamicCache.get().getBoolean("payAfterPreview", false);
    }

    void setVipHeadUrlString(String v) {
        DynamicCache.get().putString("vipHead", v);
    }

    public String getVipHeadUrlString() {
        return DynamicCache.get().getString("vipHead", "");
    }

    void setAcrossCode(String v) {
        DynamicCache.get().putString("acrossCode", v);
    }

    public String getAcrossCode() {
        return DynamicCache.get().getString("acrossCode", "");
    }

    void setLoginCode(String v) {
        DynamicCache.get().putString("loginCode", v);
    }

    public String getLoginCode() {
        return DynamicCache.get().getString("loginCode", "");
    }

    void setIseUrlString(String v) {
        DynamicCache.get().putString("iseUrl", v);
    }

    public String getIseUrlString() {
        return DynamicCache.get().getString("iseUrl", "");
    }

    void setOnlyIsee(String v) {
        DynamicCache.get().putString("onlyIsee", v);
    }

    public String getOnlyIsee() {
        return DynamicCache.get().getString("onlyIsee", "");
    }

    void setCanntJumpAdvertising(String v) {
        DynamicCache.get().putString("canntJumpAdvertising", v);
    }

    public String getCanntJumpAdvertising() {
        return DynamicCache.get().getString("canntJumpAdvertising", "");
    }

    void setIsEnablePlayerLocalServer(boolean v) {
        DynamicCache.get().putBoolean("enablePlayerLocalServer", v);
    }

    public Boolean getIsEnablePlayerLocalServer() {
        return Boolean.valueOf(DynamicCache.get().getBoolean("enablePlayerLocalServer", false));
    }

    void setAdSkipFrequency(int v) {
        DynamicCache.get().putInt("adSkipFrequency", v);
    }

    public int getAdSkipFrequency() {
        return DynamicCache.get().getInt("adSkipFrequency", 0);
    }

    void setIsPushVideoByTvPlatform(boolean v) {
        DynamicCache.get().putBoolean("isPushVideoByTvPlatform", v);
    }

    public boolean getIsPushVideoByTvPlatform() {
        return DynamicCache.get().getBoolean("isPushVideoByTvPlatform", false);
    }

    void setIsDisableAdCache(boolean v) {
        DynamicCache.get().putBoolean("isDisableAdCache", v);
    }

    public boolean getIsDisableAdCache() {
        return DynamicCache.get().getBoolean("isDisableAdCache", false);
    }

    void setVipResourceId(String v) {
        DynamicCache.get().putString("vipResourceId", v);
    }

    public String getVipResourceId() {
        return DynamicCache.get().getString("vipResourceId", "");
    }

    void setVipPushPreviewTip(String v) {
        DynamicCache.get().putString("vipPushPreviewTip", v);
    }

    public String getVipPushPreviewTip() {
        return DynamicCache.get().getString("vipPushPreviewTip", "");
    }

    void setVipPushPreviewEndTip(String v) {
        DynamicCache.get().putString("vipPushPreviewEndTip", v);
    }

    public String getVipPushPreviewEndTip() {
        return DynamicCache.get().getString("vipPushPreviewEndTip", "");
    }

    void setPurchaseGuideTipText(String v) {
        DynamicCache.get().putString("purchase_guide_tip_text", v);
    }

    public String getPurchaseGuideTipText() {
        return DynamicCache.get().getString("purchase_guide_tip_text", "");
    }

    void setPurchaseGuideTipUrlString(String v) {
        DynamicCache.get().putString("purchase_guide_tip_image_url", v);
    }

    public String getPurchaseGuideTipUrlString() {
        return DynamicCache.get().getString("purchase_guide_tip_image_url", "");
    }

    void setPurchaseButtonTxt(String v) {
        DynamicCache.get().putString(WebSDKConstants.PARAM_KEY_PURCHASE_BUTTON_TXT, v);
    }

    public String getPurchaseButtonTxt() {
        return DynamicCache.get().getString(WebSDKConstants.PARAM_KEY_PURCHASE_BUTTON_TXT, "");
    }

    void setLivePurchaseGuideTipImageUrlString(String v) {
        DynamicCache.get().putString("live_purchase_guide_tip_image_url", v);
    }

    public String getLivePurchaseGuideTipImageUrlString() {
        return DynamicCache.get().getString("live_purchase_guide_tip_image_url", "");
    }

    void setLivePurchaseGuideTipText(String v) {
        DynamicCache.get().putString("live_purchase_guide_tip_text", v);
    }

    public String getLivePurchaseGuideTipText() {
        return DynamicCache.get().getString("live_purchase_guide_tip_text", "");
    }

    void setDisableNativePlayerAdvancedMode(boolean v) {
        DynamicCache.get().putBoolean("disableNativePlayerAdvancedMode", v);
    }

    public boolean getDisableNativePlayerAdvancedMode() {
        return DynamicCache.get().getBoolean("disableNativePlayerAdvancedMode", false);
    }

    void setIsOpenHcdn(boolean v) {
        DynamicCache.get().putBoolean("isOpenHcdn", v);
    }

    public boolean getIsOpenHcdn() {
        return DynamicCache.get().getBoolean("isOpenHcdn", true);
    }

    void setHasAppStore(boolean v) {
        DynamicCache.get().putBoolean("hasAppStore", v);
    }

    public boolean getHasAppStore() {
        return DynamicCache.get().getBoolean("hasAppStore", false);
    }

    void setHasTvTab(boolean v) {
        DynamicCache.get().putBoolean("hasTvTab", v);
    }

    public boolean getHasTvTab() {
        return DynamicCache.get().getBoolean("hasTvTab", false);
    }

    void setOnLineTab(boolean v) {
        DynamicCache.get().putBoolean("onLineTab", v);
    }

    public boolean getOnLineTab() {
        return DynamicCache.get().getBoolean("onLineTab", false);
    }

    void setHasRecommend(boolean v) {
        DynamicCache.get().putBoolean("hasRecommend", v);
    }

    public boolean getHasRecommend() {
        return DynamicCache.get().getBoolean("hasRecommend", false);
    }

    void setIsSupportCarousel(boolean v) {
        DynamicCache.get().putBoolean("isSupportCarousel", v);
    }

    public boolean getIsSupportCarousel() {
        return DynamicCache.get().getBoolean("isSupportCarousel", false);
    }

    void setDefaultCarouselUrlString(String v) {
        DynamicCache.get().putString("carouselDefaultPic", v);
    }

    public String getDefaultCarouselUrlString() {
        return DynamicCache.get().getString("carouselDefaultPic", "");
    }

    void setDailyInfoCornerMark(String v) {
        DynamicCache.get().putString("dailyInfoCornerMark", v);
    }

    public String getDailyInfoCornerMark() {
        return DynamicCache.get().getString("dailyInfoCornerMark", "");
    }

    void setRunMan3TabAvailable(boolean v) {
        DynamicCache.get().putBoolean("runMan3TabAvailable", v);
    }

    public boolean getRunMan3TabAvailable() {
        return DynamicCache.get().getBoolean("runMan3TabAvailable", false);
    }

    void setPlayerBackColourUrlString(String v) {
        DynamicCache.get().putString("playerBackColourUrl", v);
    }

    public String getPlayerBackColourUrlString() {
        return DynamicCache.get().getString("playerBackColourUrl", "");
    }

    void setModifyPwdQRCode(String v) {
        DynamicCache.get().putString("modifyPwdQRCode", v);
    }

    public String getModifyPwdQRCode() {
        return DynamicCache.get().getString("modifyPwdQRCode", "");
    }

    void setPlayerConfig(String v) {
        DynamicCache.get().putString(InterfaceKey.PLAYER_CP, v);
    }

    public String getPlayerConfig() {
        return DynamicCache.get().getString(InterfaceKey.PLAYER_CP, "");
    }

    void setABTest(String v) {
        DynamicCache.get().putString("abTest", v);
    }

    public String getABTest() {
        return DynamicCache.get().getString("abTest", "");
    }

    void setLogResident(String v) {
        DynamicCache.get().putString("log_Resident", v);
    }

    public String getLogResident() {
        return DynamicCache.get().getString("log_Resident", "");
    }

    void setVipGuideInfo(VipGuideInfo v) {
        DynamicCache.get().put("vipGuideInfo", v);
    }

    public VipGuideInfo getVipGuideInfo() {
        VipGuideInfo v = null;
        try {
            return (VipGuideInfo) DynamicCache.get().get("vipGuideInfo");
        } catch (Exception e) {
            e.printStackTrace();
            return v;
        }
    }

    void setIsHomeRequestOnlyForLaunch(boolean v) {
        DynamicCache.get().putBoolean("isHomeRequestOnlyForLaunch", v);
    }

    public boolean getIsHomeRequestOnlyForLaunch() {
        return DynamicCache.get().getBoolean("isHomeRequestOnlyForLaunch", false);
    }

    void setIsHomeRequestForLaunchAndEvent(boolean v) {
        DynamicCache.get().putBoolean("isHomeRequestForLaunchAndEvent", v);
    }

    public boolean getIsHomeRequestForLaunchAndEvent() {
        return DynamicCache.get().getBoolean("isHomeRequestForLaunchAndEvent", false);
    }

    void setIsDisableCrosswalk(boolean v) {
        DynamicCache.get().putBoolean("isDisableCrosswalk", v);
    }

    public boolean getIsDisableCrosswalk() {
        return DynamicCache.get().getBoolean("isDisableCrosswalk", false);
    }

    void setCarouselLoadingInfo(String v) {
        DynamicCache.get().putString("carouselLoadingInfo", v);
    }

    public String getCarouselLoadingInfo() {
        return DynamicCache.get().getString("carouselLoadingInfo", "");
    }

    void setScreenWeChatInteractive(String v) {
        DynamicCache.get().putString("screenWeChatInteractive", v);
    }

    public String getScreenWeChatInteractive() {
        return DynamicCache.get().getString("screenWeChatInteractive", "");
    }

    void setAdInfo(String v) {
        DynamicCache.get().putString("adInfo", v);
    }

    public String getAdInfo() {
        return DynamicCache.get().getString("adInfo", "");
    }

    void setRetryTimesBeforeStarted(int v) {
        DynamicCache.get().putInt("retry_times_before_started", v);
    }

    public int getRetryTimesBeforeStarted() {
        return DynamicCache.get().getInt("retry_times_before_started", 0);
    }

    void setRetryTimesAfterStarted(int v) {
        DynamicCache.get().putInt("retry_times_after_started", v);
    }

    public int getRetryTimesAfterStarted() {
        return DynamicCache.get().getInt("retry_times_after_started", 0);
    }

    void setHomeHeaderVipText(String v) {
        DynamicCache.get().putString("home_header_vip_text", v);
    }

    public String getHomeHeaderVipText() {
        return DynamicCache.get().getString("home_header_vip_text", "");
    }

    void setHomeHeaderVipUrl(String v) {
        DynamicCache.get().putString("home_header_vip_url", v);
    }

    public String getHomeHeaderVipUrl() {
        return DynamicCache.get().getString("home_header_vip_url", "");
    }

    void setSupport4k(boolean v) {
        DynamicCache.get().putBoolean("support4k", v);
    }

    public boolean getSupport4k() {
        return DynamicCache.get().getBoolean("support4k", false);
    }

    void setIsDisableP2PUpload(boolean v) {
        DynamicCache.get().putBoolean("isDisableP2PUpload", v);
    }

    public boolean getIsDisableP2PUpload() {
        return DynamicCache.get().getBoolean("isDisableP2PUpload", false);
    }

    void setIsDisableNDUpload(boolean v) {
        DynamicCache.get().putBoolean("isDisableNDUpload", v);
    }

    public boolean getIsDisableNDUpload() {
        return DynamicCache.get().getBoolean("isDisableNDUpload", false);
    }

    void setDetailExitDialogResId(String v) {
        DynamicCache.get().putString("detailExitDialogResId", v);
    }

    public String getDetailExitDialogResId() {
        return DynamicCache.get().getString("detailExitDialogResId", "");
    }

    void setAppCard(int v) {
        DynamicCache.get().putInt("appCard", v);
    }

    public int getAppCard() {
        return DynamicCache.get().getInt("appCard", 0);
    }

    void setMsgDialogPos(int v) {
        DynamicCache.get().putInt("msgDialogPos", v);
    }

    public int getMsgDialogPos() {
        return DynamicCache.get().getInt("msgDialogPos", 4);
    }

    void setMsgDialogIsOutAPP(boolean v) {
        DynamicCache.get().putBoolean("msgDialogIsOutAPP", v);
    }

    public boolean getMsgDialogIsOutAPP() {
        return DynamicCache.get().getBoolean("msgDialogIsOutAPP", false);
    }

    void setIsShowExitAppDialog(boolean v) {
        DynamicCache.get().putBoolean("isShowExitAppDialog", v);
    }

    public boolean getIsShowExitAppDialog() {
        return DynamicCache.get().getBoolean("isShowExitAppDialog", false);
    }

    void setIsDisableHCDNPreDeploy(boolean v) {
        DynamicCache.get().putBoolean("isDisableHCDNPreDeploy", v);
    }

    public boolean getIsDisableHCDNPreDeploy() {
        return DynamicCache.get().getBoolean("isDisableHCDNPreDeploy", false);
    }

    void setIsCardSort(boolean v) {
        DynamicCache.get().putBoolean("isCardSort", v);
    }

    public boolean getIsCardSort() {
        return DynamicCache.get().getBoolean("isCardSort", true);
    }

    void setAllTagPosition(int v) {
        DynamicCache.get().putInt("allTagPosition", v);
    }

    public int getAllTagPosition() {
        return DynamicCache.get().getInt("allTagPosition", 0);
    }

    void setIsSupportGif(boolean v) {
        DynamicCache.get().putBoolean("isSupportGif", v);
    }

    public boolean getIsSupportGif() {
        return DynamicCache.get().getBoolean("isSupportGif", true);
    }

    void setIsOpenRootCheck(boolean v) {
        DynamicCache.get().putBoolean("isOpenRootCheck", v);
    }

    public boolean getIsOpenRootCheck() {
        return DynamicCache.get().getBoolean("isOpenRootCheck", false);
    }

    void setIsShowGuideLogin(boolean v) {
        DynamicCache.get().putBoolean("isShowGuideLogin", v);
    }

    public boolean getIsShowGuideLogin() {
        return DynamicCache.get().getBoolean("isShowGuideLogin", false);
    }

    void setIsOpenAdVipGuide(boolean v) {
        DynamicCache.get().putBoolean("isOpenAdVipGuide", v);
    }

    public boolean getIsOpenAdVipGuide() {
        return DynamicCache.get().getBoolean("isOpenAdVipGuide", true);
    }

    void setAdGuideBecomeVipText(String v) {
        DynamicCache.get().putString("ad_guide_become_vip_text", v);
    }

    public String getAdGuideBecomeVipText() {
        return DynamicCache.get().getString("ad_guide_become_vip_text", "");
    }

    void setIsSupportH265(boolean v) {
        DynamicCache.get().putBoolean("isSupportH265", v);
    }

    public boolean getIsSupportH265() {
        return DynamicCache.get().getBoolean("isSupportH265", true);
    }

    public void setEnablePlayerLocalServerF4v2Hls(boolean flag) {
        DynamicCache.get().putBoolean("enablePlayerLocalServerF4v2Hls", flag);
    }

    public boolean enablePlayerLocalServerF4v2Hls() {
        return DynamicCache.get().getBoolean("enablePlayerLocalServerF4v2Hls", false);
    }

    public void setEnablePlayerLocalServerStream(boolean flag) {
        DynamicCache.get().putBoolean("enablePlayerLocalServerStream", flag);
    }

    public boolean enablePlayerLocalServerStream() {
        return DynamicCache.get().getBoolean("enablePlayerLocalServerStream", false);
    }

    public void setDisableHcdnDaemon(boolean flag) {
        DynamicCache.get().putBoolean("disableHcdnDaemon", flag);
    }

    public boolean disableHcdnDaemon() {
        return DynamicCache.get().getBoolean("disableHcdnDaemon", false);
    }

    public void setSubChannelPlayerConfig(String config) {
        DynamicCache.get().putString("subChannelPlayerConfig", config);
    }

    public String getSubChannelPlayerConfig() {
        return DynamicCache.get().getString("subChannelPlayerConfig", "");
    }

    public void setChildAppUrl(String url) {
        DynamicCache.get().putString("childAppUrl", url);
    }

    public String getChildAppUrl() {
        return DynamicCache.get().getString("childAppUrl", "");
    }

    public void setChinaPokerAppUrl(String url) {
        DynamicCache.get().putString("chinaPokerAppUrl", url);
    }

    public String getChinaPokerAppUrl() {
        return DynamicCache.get().getString("chinaPokerAppUrl", "");
    }

    public void setLoginButtonBelowText(String str) {
        DynamicCache.get().putString("loginButtonBelowText", str);
    }

    public String getLoginButtonBelowText() {
        return DynamicCache.get().getString("loginButtonBelowText", "");
    }

    public void setLoginPageLeftPicUrl(String str) {
        DynamicCache.get().putString("loginPageLeftPicUrl", str);
    }

    public String getLoginPageLeftPicUrl() {
        return DynamicCache.get().getString("loginPageLeftPicUrl", "");
    }

    private Map<Integer, List<String>> initImagePaths() {
        Map<Integer, List<String>> imagePaths = new ConcurrentHashMap();
        Object imagePathsObj = DynamicCache.get().get(DNMC_CFG_URLS);
        if (imagePathsObj == null) {
            DynamicCache.get().put(DNMC_CFG_URLS, imagePaths);
            return imagePaths;
        } else if (imagePathsObj.getClass().isInstance(imagePaths)) {
            return (Map) DynamicCache.get().get(DNMC_CFG_URLS);
        } else {
            LogUtils.w(TAG, "local image path is not the type : Map<Integer, List<String>>");
            return imagePaths;
        }
    }

    public DynamicResult() {
        initImagePaths();
    }

    private Map<Integer, List<String>> getImagePaths() {
        Map<Integer, List<String>> map = (Map) DynamicCache.get().get(DNMC_CFG_URLS);
        if (map == null) {
            return initImagePaths();
        }
        return map;
    }

    private List<String> checkAndGetUrl(int key) {
        Map<Integer, List<String>> imagePaths = getImagePaths();
        LogUtils.d(TAG, "get " + key + " image local path =  " + imagePaths.get(Integer.valueOf(key)));
        GetInterfaceTools.getIDynamicQDataProvider().checkImageURLUpdate(key);
        return (List) imagePaths.get(Integer.valueOf(key));
    }

    @Deprecated
    public List<String> getStartLoadingUrl() {
        return checkAndGetUrl(0);
    }

    @Deprecated
    public List<String> getPlayLoadingUrl() {
        return checkAndGetUrl(1);
    }

    public List<String> getBootUrl() {
        return checkAndGetUrl(2);
    }

    public List<String> getStartImagePath() {
        return checkAndGetUrl(24);
    }

    public List<String> getBugVipTipPicPath() {
        return checkAndGetUrl(25);
    }

    public List<String> getHeadPath() {
        return checkAndGetUrl(3);
    }

    @Deprecated
    public List<String> getHeadLogoPath() {
        return checkAndGetUrl(23);
    }

    @Deprecated
    public List<String> getDefUrl() {
        return checkAndGetUrl(4);
    }

    @Deprecated
    public List<String> getPlayUrl() {
        return checkAndGetUrl(5);
    }

    @Deprecated
    public List<String> getServUrl() {
        return checkAndGetUrl(6);
    }

    @Deprecated
    public List<String> getWaterUrl() {
        return checkAndGetUrl(7);
    }

    @Deprecated
    public List<String> getIseeUrl() {
        return checkAndGetUrl(8);
    }

    public List<String> getPlayerLogo() {
        return checkAndGetUrl(9);
    }

    @Deprecated
    public List<String> getPlayerBackColor() {
        return checkAndGetUrl(10);
    }

    public List<String> getCodeUrl() {
        return checkAndGetUrl(11);
    }

    public List<String> getGalaUrl() {
        return checkAndGetUrl(12);
    }

    public List<String> getJSTVUrl() {
        return checkAndGetUrl(13);
    }

    public List<String> getPPSUrl() {
        return checkAndGetUrl(14);
    }

    @Deprecated
    public List<String> getVipHeadUrls() {
        return checkAndGetUrl(16);
    }

    public List<String> getDefaultCarouselUrl() {
        return checkAndGetUrl(17);
    }

    public List<String> getPlayBackgroundImagePath() {
        return checkAndGetUrl(18);
    }

    public List<String> getPurchaseGuideTipUrl() {
        return checkAndGetUrl(19);
    }

    public List<String> getLivePurchaseGuideTipUrl() {
        return checkAndGetUrl(20);
    }

    public void setIsCheckInFun(boolean v) {
        DynamicCache.get().putBoolean("isCheckInFun", v);
    }

    public void setIsCheckInRecommend(boolean v) {
        DynamicCache.get().putBoolean("isCheckInRecommend", v);
    }

    public boolean getIsCheckInFun() {
        return DynamicCache.get().getBoolean("isCheckInFun", false);
    }

    public boolean getIsCheckInRecommend() {
        return DynamicCache.get().getBoolean("isCheckInRecommend", false);
    }

    public String getPlayerTipCollections() {
        return DynamicCache.get().getString("player_tip_collections", "");
    }

    void setPlayerTipCollections(String str) {
        DynamicCache.get().putString("player_tip_collections", str);
    }

    public List<String> getScreenWechatInteractiveImagePath() {
        List<String> list = (List) getImagePaths().get(Integer.valueOf(21));
        LogUtils.d(TAG, "getScreenWechatInteractiveImagePath, screen wechat interactive image local path = " + list);
        GetInterfaceTools.getIDynamicQDataProvider().checkImageURLUpdate(21);
        return list;
    }

    void removeImagePath(int tag) {
        Map<Integer, List<String>> imagePaths = getImagePaths();
        List<String> imagePath = (List) imagePaths.get(Integer.valueOf(tag));
        LogUtils.d(TAG, "removeImagePath, tag = " + tag + "imagePath = " + imagePath);
        if (imagePath != null) {
            imagePaths.remove(Integer.valueOf(tag));
        }
    }

    void addImagePaths(int tag, List<String> path) {
        getImagePaths().put(Integer.valueOf(tag), path);
    }

    List<String> getImagePaths(int tag) {
        Map imagePaths = getImagePaths();
        List<String> paths = new ArrayList();
        if (ListUtils.isEmpty(imagePaths)) {
            LogUtils.d(TAG, "getImagePaths, total image paths are empty");
        } else if (imagePaths.get(Integer.valueOf(tag)) != null) {
            paths.addAll((Collection) imagePaths.get(Integer.valueOf(tag)));
        } else {
            LogUtils.d(TAG, "getImagePaths, imagePaths.get " + tag + " = null");
        }
        return paths;
    }

    synchronized void saveDataToLocal() {
        Map<Integer, List<String>> map = (Map) DynamicCache.get().get(DNMC_CFG_URLS);
        Set<Entry<String, Object>> entries = DynamicCache.get().getDynamicMap().entrySet();
        LogUtils.d(TAG, "saveDataToLocal");
        if (entries == null) {
            LogUtils.d(TAG, "DynamicCache, current cache is empty");
        } else {
            for (Entry<String, Object> entry : entries) {
                LogUtils.d(TAG, "DynamicCache," + ((String) entry.getKey()) + SOAP.DELIM + entry.getValue());
            }
        }
        try {
            SerializableUtils.write(DynamicCache.get(), ApiConstants.DynamicQ_DATA_FILENAME);
        } catch (Exception e) {
            LogUtils.e(TAG, "saveDataToLocal, e = " + e);
        }
        LogUtils.d(TAG, "saveDataToLocal finished");
    }

    synchronized void getSerializableData() {
        try {
            DynamicCache.get().putAll((DynamicCache) SerializableUtils.read(ApiConstants.DynamicQ_DATA_FILENAME));
        } catch (Exception e) {
            LogUtils.e(TAG, "getSerializableData, e = " + e);
        }
    }
}
