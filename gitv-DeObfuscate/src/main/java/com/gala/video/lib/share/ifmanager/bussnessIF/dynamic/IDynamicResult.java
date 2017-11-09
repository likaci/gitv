package com.gala.video.lib.share.ifmanager.bussnessIF.dynamic;

import com.gala.tvapi.tv2.model.VipGuideInfo;
import java.util.List;

public interface IDynamicResult {
    public static final int TAG_DYNAMICQ_1080p_GUIDE_BG_IMG_URL = 28;
    public static final int TAG_DYNAMICQ_4K_GUIDE_BG_IMG_URL = 27;
    public static final int TAG_DYNAMICQ_BOOTURL = 2;
    public static final int TAG_DYNAMICQ_BUG_VIP_TIP_PIC = 25;
    public static final int TAG_DYNAMICQ_CAROUSEL_COVER_URL = 17;
    public static final int TAG_DYNAMICQ_CODEURL = 11;
    public static final int TAG_DYNAMICQ_DEFURL = 4;
    public static final int TAG_DYNAMICQ_DETAIL_FREE_VIDEO_BG_IMG_URL = 30;
    public static final int TAG_DYNAMICQ_DETAIL_MONTH_BG_IMG_URL = 29;
    public static final int TAG_DYNAMICQ_GALAURL = 12;
    public static final int TAG_DYNAMICQ_HDR_GUIDE_BG_IMG_URL = 26;
    public static final int TAG_DYNAMICQ_HEADURL = 3;
    public static final int TAG_DYNAMICQ_HEAD_LOGO_URL = 23;
    public static final int TAG_DYNAMICQ_IMAGE_EXIT_APP_Ad_DEFAULT = 22;
    public static final int TAG_DYNAMICQ_IMAGE_LIVE_PURCHASE_GUIDE_TIP = 20;
    public static final int TAG_DYNAMICQ_IMAGE_PLAYER_BACKGROUND = 18;
    public static final int TAG_DYNAMICQ_IMAGE_PURCHASE_GUIDE_TIP = 19;
    public static final int TAG_DYNAMICQ_IMAGE_SCREEN_WECHAT_INTERACTIVE = 21;
    public static final int TAG_DYNAMICQ_ISEEURL = 8;
    public static final int TAG_DYNAMICQ_JSTVURL = 13;
    public static final int TAG_DYNAMICQ_PLAYERBACKCOLOR = 10;
    public static final int TAG_DYNAMICQ_PLAYERLOGO = 9;
    public static final int TAG_DYNAMICQ_PLAYLOADING = 1;
    public static final int TAG_DYNAMICQ_PLAYNEWURL = 15;
    public static final int TAG_DYNAMICQ_PLAYURL = 5;
    public static final int TAG_DYNAMICQ_PPSURL = 14;
    public static final int TAG_DYNAMICQ_SERVURL = 6;
    public static final int TAG_DYNAMICQ_STARTLOADING = 0;
    public static final int TAG_DYNAMICQ_START_URL = 24;
    public static final int TAG_DYNAMICQ_VIP_HEADURL = 16;
    public static final int TAG_DYNAMICQ_WATERURL = 7;

    public enum OperationImageType {
        NONE("none"),
        START("start"),
        EXIT("video"),
        SCREENSAVER("screensaver");
        
        private String value;

        private OperationImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    boolean disableHcdnDaemon();

    boolean enablePlayerLocalServerF4v2Hls();

    boolean enablePlayerLocalServerStream();

    List<String> get1080pGuideBgImgPaths();

    String get1080pGuideBgImgUrls();

    List<String> get4kGuideBgImgPaths();

    String get4kGuideBgImgUrls();

    String get4kGuideBottomDesc();

    String getABTest();

    String getAcrossCode();

    String getAd();

    String getAdGuideBecomeVipText();

    String getAdInfo();

    int getAdSkipFrequency();

    int getAllTagPosition();

    int getAppCard();

    List<String> getBootUrl();

    String getBootUrlString();

    List<String> getBugVipTipPicPath();

    String getBugVipTipPicUrl();

    String getCServer();

    String getCanntJumpAdvertising();

    String getCarouselLoadingInfo();

    String getChildAppUrl();

    String getChinaPokerAppUrl();

    List<String> getCodeUrl();

    String getCodeUrlString();

    String getCtime();

    String getDailyIcon();

    String getDailyIds();

    String getDailyInfoCornerMark();

    String getDailyLabels();

    String getDailyName();

    @Deprecated
    List<String> getDefUrl();

    String getDefUrlString();

    List<String> getDefaultCarouselUrl();

    String getDefaultCarouselUrlString();

    String getDesc();

    String getDetailExitDialogResId();

    List<String> getDetailFreeVideoOperatImagePath();

    String getDetailFreeVideoOperateImageUrls();

    String getDevErr();

    boolean getDisableNativePlayerAdvancedMode();

    String getDocument();

    String getExit();

    String getFaq();

    String getFeedbackInfoTip();

    String getFree();

    String getFtinfo();

    List<String> getGalaUrl();

    boolean getHasAppStore();

    boolean getHasRecommend();

    boolean getHasTvTab();

    List<String> getHdrGuideBgImgPaths();

    String getHdrGuideBgImgUrls();

    String getHdrGuideBottomDesc();

    @Deprecated
    List<String> getHeadLogoPath();

    String getHeadLogoUrl();

    List<String> getHeadPath();

    String getHeadUrl();

    String getHomeHeaderVipText();

    String getHomeHeaderVipUrl();

    String getIChn();

    String getISeeUrlString();

    boolean getIsCardSort();

    boolean getIsCheckInFun();

    boolean getIsCheckInRecommend();

    boolean getIsDisableAdCache();

    boolean getIsDisableCrosswalk();

    boolean getIsDisableHCDNPreDeploy();

    boolean getIsDisableNDUpload();

    boolean getIsDisableP2PUpload();

    boolean getIsDisableSafeMode();

    Boolean getIsEnablePlayerLocalServer();

    boolean getIsHomeRequestForLaunchAndEvent();

    boolean getIsHomeRequestOnlyForLaunch();

    boolean getIsOpenAdVipGuide();

    boolean getIsOpenHcdn();

    boolean getIsOpenRootCheck();

    boolean getIsPushVideoByTvPlatform();

    boolean getIsShowExitAppDialog();

    boolean getIsShowGuideLogin();

    boolean getIsSupportCarousel();

    boolean getIsSupportGif();

    boolean getIsSupportH265();

    String getIseUrlString();

    @Deprecated
    List<String> getIseeUrl();

    List<String> getJSTVUrl();

    String getJstvList();

    String getJstvString();

    String getLivePurchaseGuideTipImageUrlString();

    String getLivePurchaseGuideTipText();

    List<String> getLivePurchaseGuideTipUrl();

    String getLogResident();

    String getLoginButtonBelowText();

    String getLoginCode();

    String getLoginPageLeftPicUrl();

    String getModifyPwdQRCode();

    boolean getMsgDialogIsOutAPP();

    int getMsgDialogPos();

    String getMulCtr();

    String getMulVip();

    String getName();

    String getNcinfo();

    boolean getOnLineTab();

    String getOnlyIsee();

    String getOperationImageResourceIds();

    String getOther();

    String getPPSList();

    List<String> getPPSUrl();

    boolean getPayAfterPreview();

    boolean getPayBeforePreview();

    String getPhone();

    String getPhoneTips();

    String getPinfo();

    String getPlatCnt();

    List<String> getPlayBackgroundImagePath();

    String getPlayLoading();

    @Deprecated
    List<String> getPlayLoadingUrl();

    List<String> getPlayNewUrl();

    String getPlayNewUrlString();

    @Deprecated
    List<String> getPlayUrl();

    String getPlayUrlString();

    @Deprecated
    List<String> getPlayerBackColor();

    String getPlayerBackColour();

    String getPlayerBackColourUrlString();

    String getPlayerConfig();

    List<String> getPlayerLogo();

    String getPlayerLogoString();

    String getPlayerTipCollections();

    String getPpsUrlString();

    String getPreOver();

    String getPurchaseButtonTxt();

    String getPurchaseGuideTipText();

    List<String> getPurchaseGuideTipUrl();

    String getPurchaseGuideTipUrlString();

    int getRetryTimesAfterStarted();

    int getRetryTimesBeforeStarted();

    boolean getRunMan3TabAvailable();

    String getScreenWeChatInteractive();

    List<String> getScreenWechatInteractiveImagePath();

    @Deprecated
    List<String> getServUrl();

    String getServUrlString();

    String getSpecifiedOperateImageResId(OperationImageType operationImageType);

    List<String> getStartImagePath();

    String getStartLoading();

    @Deprecated
    List<String> getStartLoadingUrl();

    String getStartUrl();

    String getSubChannelPlayerConfig();

    boolean getSupport4k();

    String getUtime();

    String getVerErr();

    String getVideoSourceUrlString();

    VipGuideInfo getVipGuideInfo();

    String getVipHeadUrlString();

    @Deprecated
    List<String> getVipHeadUrls();

    List<String> getVipMonthOperateImagePath();

    String getVipMonthOperateImageUrls();

    String getVipPushPreviewEndTip();

    String getVipPushPreviewTip();

    String getVipResourceId();

    @Deprecated
    List<String> getWaterUrl();

    String getWaterUrlString();
}
