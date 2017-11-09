package com.gala.video.app.epg.project.config;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.config.IConfigInterface.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.io.File;
import java.util.List;

public class ConfigInterfaceBaseImpl extends Wrapper {
    private static final String TAG = "ConfigInterfaceBaseImpl";
    protected IBuildInterface mBuildProvider;
    protected ISetting mSetting = null;

    protected boolean isLitchi() {
        return this.mBuildProvider.isLitchi();
    }

    protected boolean isHomeVersion() {
        return this.mBuildProvider.isHomeVersion();
    }

    protected boolean isGitvUI() {
        return this.mBuildProvider.isGitvUI();
    }

    private boolean isNoLogoUI() {
        return this.mBuildProvider.isNoLogoUI();
    }

    protected boolean isSupportContentProvider() {
        return this.mBuildProvider.isSupportContentProvider();
    }

    public void initialize(IBuildInterface build) {
        this.mBuildProvider = build;
    }

    public boolean shouldDuplicateUIForStereo3D() {
        return true;
    }

    public boolean setAnimationInXml() {
        return true;
    }

    public void onStereo3DBegun() {
    }

    public void onStereo3DFinished() {
    }

    public void initHomeLogo(ImageView logo) {
        if (logo != null) {
            IDynamicResult dataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            Bitmap loadingImage = null;
            if (!(dataModel == null || ListUtils.isEmpty(dataModel.getHeadPath()))) {
                CharSequence imagePath = (String) dataModel.getHeadPath().get(0);
                if (!StringUtils.isEmpty(imagePath)) {
                    File cacheFile = new File(imagePath);
                    if (cacheFile.exists()) {
                        loadingImage = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                    }
                }
            }
            if (loadingImage != null) {
                logo.setImageBitmap(loadingImage);
            } else if (isLitchi()) {
                logo.setImageResource(R.drawable.share_gitv);
            } else if (isNoLogoUI()) {
                logo.setVisibility(4);
            } else {
                logo.setImageResource(R.drawable.share_gitv);
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean isEnableHardwareAccelerated() {
        return false;
    }

    public void initHomeStart(Activity activity) {
    }

    public void initHomeEnd() {
    }

    public boolean isUsbDeviceAvailable() {
        if (!new File("/mnt/sda1").exists()) {
            return false;
        }
        LogUtils.i(TAG, "usb exsit ");
        return true;
    }

    public boolean filterStereo3DKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean isSkyworthVersion() {
        return false;
    }

    public String getMultiScreenName() {
        return SettingSharepreference.getDeviceName(AppRuntimeEnv.get().getApplicationContext());
    }

    public List<MSIcon> getMultiScreenIconList() {
        return null;
    }

    public float getVideoViewScale() {
        return 1.0f;
    }

    public int getPlayerLoadingViewResId() {
        return R.layout.share_customizable_player_loadingscreen;
    }

    public View getPlayerLoadingView(Context context) {
        View loadingLayout = LayoutInflater.from(context).inflate(getPlayerLoadingViewResId(), null);
        ImageView logo = (ImageView) loadingLayout.findViewById(R.id.share_logo);
        if (isNoLogoUI()) {
            logo.setVisibility(4);
        }
        return loadingLayout;
    }

    public void onScreenOnEvent(Context context) {
    }

    public boolean shouldChangeSurfaceFormat() {
        return false;
    }

    public boolean is4kH265StreamSupported() {
        return true;
    }

    public PlayerCodecType getDecodeType() {
        return PlayerCodecType.ACC_By_SDK;
    }

    public String getCommonSettingJsonRoot() {
        return getSettingJsonRoot();
    }

    public String getPlaySettingJsonPath() {
        return getSettingJsonRoot();
    }

    public String getSettingJsonRoot() {
        String PATH_HOME_ROOT = "setting/home/";
        String PATH_APK_ROOT = "setting/common/";
        String PATH_SKYWORTH_ROOT = "setting/home/skyworth/";
        if (isHomeVersion()) {
            return isSupportContentProvider() ? PATH_HOME_ROOT : PATH_SKYWORTH_ROOT;
        } else {
            return PATH_APK_ROOT;
        }
    }

    public boolean isEnableDolby() {
        return this.mBuildProvider.isEnableDolby();
    }

    public ISetting getSystemSetting() {
        if (this.mSetting == null) {
            this.mSetting = new ISetting() {
                public void setScreenSaverTime(String time) {
                }

                public void setOutputDisplay(String output) {
                }

                public void setDreamTime(String time) {
                }

                public void setDeviceName(String name) {
                }

                public void setDRCMode(String mode) {
                }

                public void setAudioOutputMode(String mode) {
                }

                public void restoreFactory() {
                }

                public void goToPositionSetting() {
                }

                public void goToNetworkSettings() {
                }

                public List<String> getOutputEntries() {
                    return null;
                }

                public SystemInfo getInfo() {
                    return null;
                }

                public List<String> getDRCEntries() {
                    return null;
                }

                public String getCurrScreenSaveTime() {
                    return null;
                }

                public String getCurrOutput() {
                    return null;
                }

                public String getCurrDreamTime() {
                    return null;
                }

                public String getCurrDeviceName() {
                    return null;
                }

                public String getCurrDRCMode() {
                    return null;
                }

                public String getCurrAudioOutputMode() {
                    return null;
                }

                public List<String> getAudioOutputEntries() {
                    return null;
                }

                public List<String> getAllScreenSaveTime() {
                    return null;
                }

                public List<String> getAllDreamTime() {
                    return null;
                }

                public List<String> getAllDeviceName() {
                    return null;
                }

                public boolean goToAutoTest() {
                    return true;
                }
            };
        }
        return this.mSetting;
    }

    public boolean isCheckPushVipVideo() {
        return true;
    }
}
