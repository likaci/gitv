package com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.config;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import java.util.List;

public interface IConfigInterface extends IInterfaceWrapper {

    public static abstract class Wrapper implements IConfigInterface {
        public Object getInterface() {
            return this;
        }

        public static IConfigInterface asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IConfigInterface)) {
                return null;
            }
            return (IConfigInterface) wrapper;
        }
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    boolean filterStereo3DKeyEvent(KeyEvent keyEvent);

    String getCommonSettingJsonRoot();

    PlayerCodecType getDecodeType();

    List<MSIcon> getMultiScreenIconList();

    String getMultiScreenName();

    String getPlaySettingJsonPath();

    View getPlayerLoadingView(Context context);

    int getPlayerLoadingViewResId();

    String getSettingJsonRoot();

    ISetting getSystemSetting();

    float getVideoViewScale();

    void initHomeEnd();

    void initHomeLogo(ImageView imageView);

    void initHomeStart(Activity activity);

    void initialize(IBuildInterface iBuildInterface);

    boolean is4kH265StreamSupported();

    boolean isCheckPushVipVideo();

    boolean isEnableDolby();

    boolean isEnableHardwareAccelerated();

    boolean isSkyworthVersion();

    boolean isUsbDeviceAvailable();

    void onScreenOnEvent(Context context);

    void onStereo3DBegun();

    void onStereo3DFinished();

    boolean setAnimationInXml();

    boolean shouldChangeSurfaceFormat();

    boolean shouldDuplicateUIForStereo3D();
}
