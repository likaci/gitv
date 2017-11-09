package com.gala.sdk.player;

import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.model.User;
import java.util.List;
import java.util.Map;

public interface IPlayerProfile {
    boolean canSeekBeforeStart();

    boolean canSeekBeforeStartForAD();

    boolean checkBlockingOperation();

    void clearPassportPreference(Context context);

    void close2DTo3D();

    List<BitStream> filterDefinitions(List<BitStream> list);

    boolean filterStereo3DKeyEvent(KeyEvent keyEvent);

    String getAdVipGuideTipText();

    PlatformType getApiPlatformType();

    Context getAppContext();

    String getAppVersion();

    BitStream getBitStreamSetting();

    String getCdnDispatchParam();

    Context getContext();

    String getCookie();

    int getCupidAppId();

    String getCupidLocale();

    PlayerCodecType getDecodeType();

    String getDefaultUserId();

    String getDeviceIp();

    String getDomainName();

    boolean getExpired();

    int getFixedSizeTypeForSurfaceHolder();

    String getMacAddress();

    int getMaxMemorySizeForBuffer();

    String getMd5FormatMacAddr();

    String getMediaPlayerTypeConfig();

    String getPassportDeviceId();

    String getPingbackP2();

    Map<String, String> getPingbackParams();

    String getPlayerConfigJson();

    Platform getPlayerPlatform();

    String getPlayerTipCollections();

    String getPushVFlag();

    int getRetryTimesInPlaying();

    int getRetryTimesInPreparing();

    Rect getScreenSize(Context context);

    int getSkipAdCount();

    boolean getStretchPlaybackToFullScreen();

    int getSurfaceFormat();

    String getUid();

    String getUniPlayerDataConfigJson();

    UserType getUserType();

    String getUuid();

    String getVersionCode();

    float getVideoViewScale();

    String getWebViewJsonForAd();

    boolean is2DTo3DModel();

    boolean isCheckPushVipVideo();

    boolean isCollectNetDoctorInfoOnError();

    boolean isDebug();

    boolean isDefaultSettingDolbyOrH211(int i);

    boolean isDeviceSupport4kStream();

    boolean isDeviceSupportDolbyVision();

    boolean isDeviceSupportHDR10();

    boolean isDisableADCache();

    boolean isDisableAdCaster();

    boolean isDisableAdvanceMode();

    boolean isDisableP2PUpload();

    boolean isDisableSafeMode();

    boolean isDolbyByNativePlayer();

    boolean isEnableDolby();

    boolean isEnableH211();

    boolean isEnableHCDNPreDeploy();

    boolean isEnableHcdnMultiProcess();

    boolean isEnableLocalServerConvergeStream();

    boolean isEnableLocalServerF4v2Hls();

    boolean isForceAdvanceMode();

    boolean isH211ByNativePlayer();

    boolean isLogin(Context context);

    boolean isMediaPlayerPauseBeforeSeek();

    boolean isNeedShowFullScreenHint();

    boolean isNetworkAvaliable();

    boolean isOpen4kStream();

    boolean isOpenAdVipGuide();

    boolean isOpenDrmRootCheck();

    boolean isOpenHCDN(Context context);

    boolean isOpenHDRByUserPreference();

    boolean isOpenPluginIOBalance();

    boolean isPayAfterPreview();

    boolean isPayBeforePreview();

    boolean isPingbackDebugMode();

    boolean isPreferNativePlayerSafeModeFor4K();

    boolean isPreferSystemPlayerFor4K();

    boolean isPushVideo();

    boolean isPushVideoByTvPlatform();

    boolean isRomIntegratedVersion();

    boolean isSelectionPanelShown();

    boolean isShowInnerStorage();

    boolean isShowVIP();

    boolean isSkipAdUser(Context context);

    boolean isSupport2DTo3DFor4k();

    boolean isSupport4kH211Stream();

    boolean isSupportAndroidCache();

    boolean isSupportNetDiagnose();

    boolean isSupportWindowPlay();

    void onLoginSuccess(Map<String, String> map);

    void onPurchaseSuccess(Map<String, String> map);

    void onStereo3DBegun();

    void onStereo3DFinished();

    void open2DTo3D();

    void saveVipInfo(User user, Context context);

    void setBitStreamSetting(BitStream bitStream);

    void setScreenSaverEnable(boolean z);

    void setSkipVideoHeaderAndTail(boolean z);

    void setUserTypeJson(String str);

    boolean shouldResetSurface();

    boolean shouldSkipVideoHeaderAndTail();

    boolean shouldUpdateSurfaceViewAfterStart();

    boolean showEpisodeAsGallery(int i);

    void updateSkipAdState(Context context, boolean z);

    boolean useFileDescriptorForLocalPlayback();

    boolean useNativePlayerForCarousel();
}
