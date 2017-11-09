package com.gala.video.app.epg.project.config.skyworth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.multiscreen.dmr.util.MSKeyUtils;
import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.video.app.epg.project.config.ConfigInterfaceBaseImpl;
import com.gala.video.app.epg.project.config.skyworth.SkyworthSetting.MachineModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigSkyworthBase extends ConfigInterfaceBaseImpl {
    private static final String TAG = "ConfigSkyworthBase";

    public void initialize(IBuildInterface build) {
        super.initialize(build);
        MSKeyUtils.setHomeKeyCode((short) 172);
    }

    public boolean isUsbDeviceAvailable() {
        if (!new File("/storage/external_storage/sda1").exists()) {
            return false;
        }
        LogUtils.i(TAG, "usb exsit.");
        return true;
    }

    public boolean filterStereo3DKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 67 || event.getAction() != 0) {
            return false;
        }
        AppRuntimeEnv.get().getApplicationContext().sendBroadcast(new Intent("com.skyworth.hotkeys.3dmode"));
        return true;
    }

    public boolean isSkyworthVersion() {
        return true;
    }

    public String getMultiScreenName() {
        String deviceName = "";
        try {
            Cursor c = AppRuntimeEnv.get().getApplicationContext().getContentResolver().query(Uri.parse("content://mipt.ott_setting/conf"), null, "confgroup = \"ott_device_info\" and name = \"ott_device_dlna_name\" ", null, null);
            if (c != null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    deviceName = c.getString(c.getColumnIndexOrThrow("value"));
                }
                c.close();
            }
        } catch (Exception e) {
        }
        if (deviceName.equals("")) {
            return "超清盒子";
        }
        return deviceName;
    }

    public List<MSIcon> getMultiScreenIconList() {
        List<MSIcon> list = new ArrayList();
        list.add(createMultiScreenIcon(LoginConstant.A_REGISTER_KEYBOARD, Opcodes.IF_ACMPEQ, 100, "http://static.ptqy.gitv.tv/tv/app/skyworth/20170401/skyworthicon.png"));
        list.add(createMultiScreenIcon(LoginConstant.A_REGISTER_KEYBOARD, Opcodes.IF_ACMPEQ, 100, "http://static.ptqy.gitv.tv/tv/app/skyworth/20170401/skyworthiconpress.png"));
        return list;
    }

    private MSIcon createMultiScreenIcon(String depth, int width, int height, String url) {
        MSIcon icon = new MSIcon();
        icon.setMimeType("image/jpg");
        icon.setDepth(depth);
        icon.setHeight(height);
        icon.setWidth(width);
        icon.setURL(url);
        return icon;
    }

    public float getVideoViewScale() {
        return 1.0f;
    }

    public void onScreenOnEvent(Context context) {
        Log.d("Skyworth-DeviceAppConfig", "Send android.action.ics.mipt.ota.update");
        context.sendBroadcast(new Intent("android.action.ics.mipt.ota.update"));
    }

    public PlayerCodecType getDecodeType() {
        return PlayerCodecType.ACC_By_MediaCodec;
    }

    public ISetting getSystemSetting() {
        if (this.mSetting == null) {
            final SkyworthSetting skyworth = SkyworthSetting.getInstance();
            this.mSetting = new ISetting() {
                public void setScreenSaverTime(String time) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ConfigSkyworthBase.TAG, "setScreenSaverTime:" + time);
                    }
                    skyworth.setScreenSaverTime(time);
                }

                public void setOutputDisplay(String output) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ConfigSkyworthBase.TAG, "setOutputDisplay:" + output);
                    }
                    skyworth.setOutputDisplay(output);
                }

                public void setDreamTime(String time) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ConfigSkyworthBase.TAG, "setDreamTime:" + time);
                    }
                    skyworth.setDreamTime(time);
                }

                public void setDeviceName(String name) {
                    skyworth.setDeviceName(name);
                }

                public void setDRCMode(String mode) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ConfigSkyworthBase.TAG, "setDRCMode:" + mode);
                    }
                    skyworth.setDRCMode(mode);
                }

                public void setAudioOutputMode(String mode) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ConfigSkyworthBase.TAG, "setAudioOutputMode:" + mode);
                    }
                    skyworth.setAudioOutputMode(mode);
                }

                public void restoreFactory() {
                    skyworth.restoreFactory();
                }

                public void goToPositionSetting() {
                    skyworth.goToPositionSetting();
                }

                public void goToNetworkSettings() {
                    skyworth.goToNetworkSettings();
                }

                public List<String> getOutputEntries() {
                    return skyworth.getAllOutputDisplay();
                }

                public SystemInfo getInfo() {
                    return skyworth.getInfo();
                }

                public List<String> getDRCEntries() {
                    return skyworth.getDRCEntries();
                }

                public String getCurrScreenSaveTime() {
                    return skyworth.getCurrScreenSaveTime();
                }

                public String getCurrOutput() {
                    return skyworth.getCurrOutput();
                }

                public String getCurrDreamTime() {
                    return skyworth.getCurrDreamTime();
                }

                public String getCurrDeviceName() {
                    return skyworth.getCurrDeviceName();
                }

                public String getCurrDRCMode() {
                    return skyworth.getCurrDRCMode();
                }

                public String getCurrAudioOutputMode() {
                    return skyworth.getCurrAudioOutputMode();
                }

                public List<String> getAudioOutputEntries() {
                    return skyworth.getAudioOutputEntries();
                }

                public List<String> getAllScreenSaveTime() {
                    return skyworth.getAllScreenSaveTime();
                }

                public List<String> getAllDreamTime() {
                    return skyworth.getAllDreamTime();
                }

                public List<String> getAllDeviceName() {
                    return skyworth.getAllDeviceName();
                }

                public boolean goToAutoTest() {
                    return skyworth.goToAutoTest(MachineModel.I71);
                }
            };
        }
        return this.mSetting;
    }

    public boolean isCheckPushVipVideo() {
        return false;
    }
}
