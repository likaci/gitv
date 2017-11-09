package com.gala.video.app.player.config.playerconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.config.PlayerConfigJsFunc2Java;
import com.gala.video.lib.framework.core.utils.StringUtils;

public abstract class AbsPlayerConfig implements IPlayerConfig {
    private static final int FORMAT_TYPE_RGBA8888 = 1;
    protected String TAG;
    protected boolean mCanSeekBeforeStart = true;
    protected boolean mCanSeekBeforeStartForAD = true;
    protected boolean mDeviceSupport4KH211;
    protected boolean mDisable4KH264;
    protected boolean mDisableADCache = false;
    protected boolean mDisableAdCaster = false;
    protected boolean mDisableHcdnMultiProcess = false;
    protected boolean mEnablePlayerLocalServerConvergeStream = true;
    protected boolean mEnablePlayerLocalServerF4v2Hls = true;
    protected boolean mEnablePlayerMultiProcess = false;
    protected int mFixedSizeTypeForSurfaceHolder = 101;
    protected boolean mIsMediaPlayerPauseBeforeSeek = false;
    protected String mJsonConfigStr;
    protected boolean mOpenPluginIOBalance = true;
    protected int mPlayerTypeConfig = 2;
    protected boolean mShouldUpdateSurfaceViewAfterStart = false;
    protected boolean mSupport4K = false;
    protected boolean mSupportAnimation = true;
    protected boolean mSupportDolbyVision = false;
    protected boolean mSupportH211 = false;
    protected boolean mSupportHDR10 = false;
    protected boolean mSupportSmallWindowPlay = true;
    protected int mSurfaceFormat = 0;
    protected String mUniPlayerConfigJson = "";
    protected boolean mUseFdForLocalPlayback = false;
    protected boolean mUseNativePlayerCarousel = false;
    protected boolean mdisableGifAnimForDetailPage = false;
    protected boolean mdisableGifViewForCarouselChannel = false;

    public boolean isSupport4K() {
        return this.mSupport4K;
    }

    public boolean isSupportH211() {
        return this.mSupportH211;
    }

    public boolean isSupportSmallWindowPlay() {
        return this.mSupportSmallWindowPlay;
    }

    public boolean isSupportAnimation() {
        return this.mSupportAnimation;
    }

    public boolean isDisableADCache() {
        return this.mDisableADCache;
    }

    public boolean isMediaPlayerPauseBeforeSeek() {
        return this.mIsMediaPlayerPauseBeforeSeek;
    }

    public boolean canSeekBeforeStart() {
        return this.mCanSeekBeforeStart;
    }

    public boolean canSeekBeforeStartAD() {
        return this.mCanSeekBeforeStartForAD;
    }

    public boolean isUseNativePlayerCarousel() {
        return this.mUseNativePlayerCarousel;
    }

    public boolean disableGifViewForCarouselChannel() {
        return this.mdisableGifViewForCarouselChannel;
    }

    public boolean disableGifAnimForDetailPage() {
        return this.mdisableGifAnimForDetailPage;
    }

    public int getPlayerTypeConfig() {
        return this.mPlayerTypeConfig;
    }

    public int getSurfaceFormat() {
        return this.mSurfaceFormat;
    }

    public int getFixedSizeTypeForSurfaceHolder() {
        return this.mFixedSizeTypeForSurfaceHolder;
    }

    public boolean enablePlayerMultiProcess() {
        return this.mEnablePlayerMultiProcess;
    }

    public boolean enablePlayerLocalServerF4v2Hls() {
        return this.mEnablePlayerLocalServerF4v2Hls;
    }

    public boolean enablePlayerLocalServerConvergeStream() {
        return this.mEnablePlayerLocalServerConvergeStream;
    }

    public boolean shouldUpdateSurfaceViewAfterStart() {
        return this.mShouldUpdateSurfaceViewAfterStart;
    }

    public boolean useFileDescriptorForLocalPlayback() {
        return this.mUseFdForLocalPlayback;
    }

    public boolean isDisableAdCaster() {
        return this.mDisableAdCaster;
    }

    public boolean isSupportDolbyVision() {
        return this.mSupportDolbyVision;
    }

    public boolean isSupportHDR10() {
        return this.mSupportHDR10;
    }

    public String getJsonConfig() {
        return this.mJsonConfigStr;
    }

    public boolean disableHcdnMultiProcess() {
        return this.mDisableHcdnMultiProcess;
    }

    public String getUniPlayerDataConfigJson() {
        return this.mUniPlayerConfigJson;
    }

    public boolean isDisable4KH264() {
        return this.mDisable4KH264;
    }

    public boolean isDeviceSupport4KH211() {
        return this.mDeviceSupport4KH211;
    }

    protected void parseJsResult(String jsonResult) {
        this.mJsonConfigStr = jsonResult;
        if (!StringUtils.isEmpty((CharSequence) jsonResult)) {
            JSONObject jo = JSON.parseObject(jsonResult);
            if (jo != null) {
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEADCACHE)) {
                    this.mDisableADCache = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEADCACHE);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEGIFFORCAROUSEL)) {
                    this.mdisableGifViewForCarouselChannel = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEGIFFORCAROUSEL);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEGIFFORDETAILPAGE)) {
                    this.mdisableGifAnimForDetailPage = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLEGIFFORDETAILPAGE);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_MULTI_PROC_SWITCH)) {
                    this.mEnablePlayerMultiProcess = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_MULTI_PROC_SWITCH);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_PAUSE_BEFORE_SEEK)) {
                    this.mIsMediaPlayerPauseBeforeSeek = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_PAUSE_BEFORE_SEEK);
                }
                if (jo.containsKey("playerType")) {
                    this.mPlayerTypeConfig = jo.getIntValue("playerType");
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SEEK_BEFORE_START)) {
                    this.mCanSeekBeforeStart = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SEEK_BEFORE_START);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SETFIXEDSIZE)) {
                    this.mFixedSizeTypeForSurfaceHolder = jo.getIntValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SETFIXEDSIZE);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SMALLWINDOW)) {
                    this.mSupportSmallWindowPlay = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SMALLWINDOW);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_4K)) {
                    this.mSupport4K = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_4K);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_ANIMATION)) {
                    this.mSupportAnimation = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_ANIMATION);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_H211)) {
                    this.mSupportH211 = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_H211);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SURFACEFORMAT)) {
                    this.mSurfaceFormat = getPixelFormatBaseOnType(jo.getIntValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SURFACEFORMAT));
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_USE_NATIVEPLAYER_CAROUSEL)) {
                    this.mUseNativePlayerCarousel = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_USE_NATIVEPLAYER_CAROUSEL);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_ENABLE_LOCAL_SERVER_F4V2HLS)) {
                    this.mEnablePlayerLocalServerF4v2Hls = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_ENABLE_LOCAL_SERVER_F4V2HLS);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_ENABLE_LOCAL_SERVER_STREAM)) {
                    this.mEnablePlayerLocalServerConvergeStream = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_ENABLE_LOCAL_SERVER_STREAM);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_UPDATE_SURFACEVIEW_AFTER_START)) {
                    this.mShouldUpdateSurfaceViewAfterStart = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_UPDATE_SURFACEVIEW_AFTER_START);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_USE_FD_LOCAL_PLAYBACK)) {
                    this.mUseFdForLocalPlayback = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_USE_FD_LOCAL_PLAYBACK);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_AD_CASTER)) {
                    this.mDisableAdCaster = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_AD_CASTER);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_HDR10)) {
                    this.mSupportHDR10 = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_HDR10);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_DOLBYVISION)) {
                    this.mSupportDolbyVision = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_DOLBYVISION);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SEEK_BEFORE_START_AD)) {
                    this.mCanSeekBeforeStartForAD = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SEEK_BEFORE_START_AD);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_OPEN_PLUGIN_IO_BALANCE)) {
                    this.mOpenPluginIOBalance = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_OPEN_PLUGIN_IO_BALANCE);
                    PlayerAppConfig.setOpenPluginIOBalance(this.mOpenPluginIOBalance);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_HCDN_MULTI_PROCESS)) {
                    this.mDisableHcdnMultiProcess = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_HCDN_MULTI_PROCESS);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_UNIPLAYER_DATA_CONFIG_JSON)) {
                    this.mUniPlayerConfigJson = jo.getString(PlayerConfigJsFunc2Java.CONFIG_KEY_UNIPLAYER_DATA_CONFIG_JSON);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_4K_H264)) {
                    this.mDisable4KH264 = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_DISABLE_4K_H264);
                }
                if (jo.containsKey(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_4K_H211)) {
                    this.mDeviceSupport4KH211 = jo.getBooleanValue(PlayerConfigJsFunc2Java.CONFIG_KEY_SUPPORT_4K_H211);
                }
            }
        }
    }

    private int getPixelFormatBaseOnType(int formatType) {
        switch (formatType) {
            case 1:
                return 1;
            default:
                return 0;
        }
    }

    public String toString() {
        return "[" + this.TAG + AlbumEnterFactory.SIGN_STR;
    }
}
