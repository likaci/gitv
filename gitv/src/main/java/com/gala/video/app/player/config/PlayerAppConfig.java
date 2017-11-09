package com.gala.video.app.player.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.ui.config.CommonUIStyle;
import com.gala.video.app.player.ui.config.ILoadingViewUiConfig;
import com.gala.video.app.player.ui.config.LoadingViewUiConfig4Normal;
import com.gala.video.app.player.ui.overlay.AbsMediaController;
import com.gala.video.app.player.ui.overlay.AbsMediaControllerStrategy;
import com.gala.video.app.player.ui.overlay.FullScreenMediaControllerStrategy;
import com.gala.video.app.player.ui.overlay.WindowMediaControllerStrategy;
import com.gala.video.app.player.ui.overlay.panels.AbsMenuPanel;
import com.gala.video.app.player.ui.overlay.panels.MenuPanel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import java.util.List;

public class PlayerAppConfig {
    private static final String TAG = "PlayerAppConfig";
    private static CommonUIStyle mCommonUIStyle;

    public static boolean shouldSkipVideoHeaderAndTail() {
        return SettingPlayPreference.getJumpStartEnd(AppRuntimeEnv.get().getApplicationContext());
    }

    public static void setSkipVideoHeaderAndTail(boolean isSkip) {
        SettingPlayPreference.setJumpStartEnd(AppRuntimeEnv.get().getApplicationContext(), isSkip);
    }

    public static boolean handleStartPlayer(Context context, SourceType videoType, Album albumInfo, int playorder, PlayParams params, String from, boolean clearTaskFlag) {
        return false;
    }

    public static boolean handleStartPlayForPush(Context context, MultiScreenParams command, String from, String se) {
        return false;
    }

    public static boolean isShowMenuPanel(SourceType playType) {
        return true;
    }

    public static AbsMediaController getMediaController(Context context, boolean is3D, SourceType playType) {
        return null;
    }

    public static String getAlbumDesc(AlbumInfo albumInfo) {
        return TextUtils.isEmpty(albumInfo.getAlbumDesc()) ? "" : albumInfo.getAlbumDesc().trim();
    }

    public static int getDefaultStreamType() {
        int defaultStreamType = SettingPlayPreference.getStreamType(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(TAG, "getDefaultStreamType=" + defaultStreamType);
        return defaultStreamType;
    }

    public static void setDefaultStreamType(int streamType) {
        LogUtils.d(TAG, "setDefaultStreamType=" + streamType);
        SettingPlayPreference.setStreamType(AppRuntimeEnv.get().getApplicationContext(), streamType);
    }

    public static void setOpenPluginIOBalance(boolean open) {
        SettingPlayPreference.setOpenPluginIOBalance(AppRuntimeEnv.get().getApplicationContext(), open);
    }

    public static boolean isOpenPluginIOBalance() {
        return SettingPlayPreference.isOpenPluginIOBalance(AppRuntimeEnv.get().getApplicationContext());
    }

    public static boolean isOpenHDR() {
        return SettingPlayPreference.isOpenHDR(AppRuntimeEnv.get().getApplicationContext());
    }

    public static void setIsOpenHDR(boolean isOpen) {
        LogUtils.d(TAG, "setIsOpenHDR=" + isOpen);
        SettingPlayPreference.setIsOpenHDR(AppRuntimeEnv.get().getApplicationContext(), isOpen);
    }

    public static void setAudioTypeSetting(int audioType) {
        LogUtils.d(TAG, "setAudioTypeSetting=" + audioType);
        SettingPlayPreference.setAudioType(AppRuntimeEnv.get().getApplicationContext(), audioType);
    }

    public static int getAudioTypeSetting() {
        int typeSetting = SettingPlayPreference.getAudioType(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(TAG, "getAudioTypeSetting=" + typeSetting);
        return typeSetting;
    }

    public static boolean getStretchPlaybackToFullScreen() {
        boolean isStretchPlaybackToFullScreen = SettingPlayPreference.getStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(TAG, "getStretchPlaybackToFullScreen=" + isStretchPlaybackToFullScreen);
        return isStretchPlaybackToFullScreen;
    }

    public static void setStretchPlaybackToFullScreen(boolean isFullScreen) {
        SettingPlayPreference.setStretchPlaybackToFullScreen(AppRuntimeEnv.get().getApplicationContext(), isFullScreen);
    }

    public static boolean isSelectionPanelShown() {
        boolean shown = SettingPlayPreference.getSelectionPanelShown(AppRuntimeEnv.get().getApplicationContext());
        LogUtils.d(TAG, "isSelectionPanelShown=" + shown);
        return shown;
    }

    public static void setSelectionPanelShown(boolean shown) {
        SettingPlayPreference.setSelectionPanelShown(AppRuntimeEnv.get().getApplicationContext(), shown);
    }

    public static String getSelectionPanelShownCount() {
        return SettingPlayPreference.getSelectionPanelShownCount(AppRuntimeEnv.get().getApplicationContext());
    }

    public static void setSelectionPanelShownCount(String count) {
        SettingPlayPreference.setSelectionPanelShownCount(AppRuntimeEnv.get().getApplicationContext(), count);
    }

    public static int getMediaPlayerLayoutId() {
        return R.layout.player_layout_control;
    }

    public static int getLiveMediaPlayerLayoutId() {
        return R.layout.player_layout_control_live;
    }

    public static int getSeekBarLayoutId() {
        if (Project.getInstance().getBuild().isLitchi()) {
            return R.layout.player_layout_playseekbar;
        }
        return R.layout.player_layout_playseekbar;
    }

    public static boolean isNeedShowFullScreenHint() {
        return true;
    }

    public static boolean isStartCheckThread4NativePlayer() {
        return true;
    }

    public static boolean isPreferNativePlayerSafeModeFor4K() {
        return false;
    }

    public static AbsMenuPanel getMenuPanel(Context context) {
        return new MenuPanel(context);
    }

    public static int getBufferViewLayoutId() {
        return R.layout.player_progressbar_center;
    }

    public static int getNativeMediaPlayerMemoryBuffer() {
        return 67108864;
    }

    public static boolean isSupportNetDiagnose() {
        return true;
    }

    public static boolean is2DTo3DModel() {
        return false;
    }

    public static boolean isAdd2DTo3D() {
        return false;
    }

    public static boolean isSupport2DTo3DFor4k() {
        return false;
    }

    public static int get3dFullScreenHintBgResId() {
        return R.drawable.player_bg_fullscreen_hint_3d;
    }

    public static int getPlayerControlLayerTipTextColor(boolean isLive) {
        if (isLive) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getColor(R.color.live_tip_color);
        }
        return AppRuntimeEnv.get().getApplicationContext().getResources().getColor(R.color.video_name_color);
    }

    public static int getPlayerControlLayerTipTextSizePx(boolean isLive) {
        if (isLive) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_23dp);
        }
        return AppRuntimeEnv.get().getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_27dp);
    }

    public static int getAlbumDetailLayoutId() {
        return R.layout.player_activity_album_detail_common;
    }

    public static boolean isShowTipWhenPlayingAd() {
        return true;
    }

    public static int getButtonLeftDrawableSizeForDetailPage(float buttonTextSizePx) {
        return Math.round(1.1f * buttonTextSizePx);
    }

    public static boolean isShowInnerStorage() {
        return true;
    }

    public static boolean isRomIntegratedVersion() {
        return false;
    }

    public static boolean isPlayerExitWhenPressBackOnce() {
        return false;
    }

    public static AbsMediaControllerStrategy getFullScreenStrategy() {
        return new FullScreenMediaControllerStrategy();
    }

    public static AbsMediaControllerStrategy getWindowStrategy() {
        return new WindowMediaControllerStrategy();
    }

    public static List<BitStream> filterDefinitions(List<BitStream> list) {
        return list;
    }

    public static Bitmap getVideoDeriveBitmap() {
        return null;
    }

    public static String getAdPlayerId() {
        return Project.getInstance().getBuild().getAdPlayerId();
    }

    public static boolean isPreferSystemPlayerFor4K() {
        return Project.getInstance().getBuild().isPreferSystemPlayerFor4K();
    }

    public static boolean enableExtraPage() {
        return Project.getInstance().getBuild().enableExtraPage();
    }

    public static ILoadingViewUiConfig getConfig4Loading() {
        if (Project.getInstance().getBuild().isLitchi()) {
            return new LoadingViewUiConfig4Normal(AppRuntimeEnv.get().getApplicationContext());
        }
        return new LoadingViewUiConfig4Normal(AppRuntimeEnv.get().getApplicationContext());
    }

    public static int[] showEpisodeAsGallery() {
        return new int[]{3, 9, 29};
    }

    public static boolean isPingbackDebugMode() {
        return Project.getInstance().getBuild().isPingbackDebug();
    }

    public static CommonUIStyle getUIStyle() {
        if (mCommonUIStyle == null) {
            mCommonUIStyle = new CommonUIStyle();
        }
        return mCommonUIStyle;
    }

    public static int getNewsDetailLayoutId() {
        return R.layout.player_activity_album_detail_news;
    }

    public static boolean isShowVideoDerive() {
        return false;
    }
}
