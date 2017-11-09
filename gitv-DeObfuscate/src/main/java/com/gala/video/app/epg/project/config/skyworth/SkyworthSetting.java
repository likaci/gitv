package com.gala.video.app.epg.project.config.skyworth;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.mipt.gala.settings.ISettings;
import com.mipt.gala.settings.ISettings.Stub;
import com.push.mqttv3.internal.ClientDefaults;
import java.util.ArrayList;
import java.util.List;

public class SkyworthSetting {
    public static final String ACTION_NETWORK_SETTING = "mipt.gala.settings.action.NETWORK";
    private final String ACTION_SETTING_SERVICE;
    private final String TAG;
    private ServiceConnection conn;
    private Context mContext;
    private ISettings mSettingService;

    class C07721 implements ServiceConnection {
        C07721() {
        }

        public void onServiceDisconnected(ComponentName name) {
            LogUtils.m1568d("SkyworthSetting", "setting service onServiceDisconnected");
            SkyworthSetting.this.mSettingService = null;
            SkyworthSetting.this.bindService();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("SkyworthSetting", "service is connected");
            SkyworthSetting.this.mSettingService = Stub.asInterface(service);
        }
    }

    public enum MachineModel {
        I71,
        I71C,
        I71S
    }

    private static class SingletonHolder {
        public static SkyworthSetting instance = new SkyworthSetting();

        private SingletonHolder() {
        }
    }

    private SkyworthSetting() {
        this.TAG = "SkyworthSetting";
        this.ACTION_SETTING_SERVICE = "gala.settings.aidl.action.AIDL_SETTINGS_SERVICE";
        this.conn = new C07721();
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        bindService();
    }

    public static SkyworthSetting getInstance() {
        return SingletonHolder.instance;
    }

    public List<String> getAllOutputDisplay() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] outputs = this.mSettingService.getAllOutputDisplay();
                if (outputs != null) {
                    int length = outputs.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + outputs[i]);
                        }
                        if (outputs[i] != null) {
                            results.add(outputs[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "OutputDisplay is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getAllOutputDisplay", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public List<String> getDRCEntries() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] drcs = this.mSettingService.getDRCEntries();
                if (drcs != null) {
                    int length = drcs.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + drcs[i]);
                        }
                        if (drcs[i] != null) {
                            results.add(drcs[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "drcs is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getDRCEntries", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public List<String> getAudioOutputEntries() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] audios = this.mSettingService.getAudioOutputEntries();
                if (audios != null) {
                    int length = audios.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + audios[i]);
                        }
                        if (audios[i] != null) {
                            results.add(audios[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "drcs is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getAudioOutputEntries", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public List<String> getAllScreenSaveTime() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] times = this.mSettingService.getAllScreenSaveTime();
                if (times != null) {
                    int length = times.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + times[i]);
                        }
                        if (times[i] != null) {
                            results.add(times[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "drcs is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getAllScreenSaveTime", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public List<String> getAllDreamTime() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] times = this.mSettingService.getAllDreamTme();
                if (times != null) {
                    int length = times.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + times[i]);
                        }
                        if (times[i] != null) {
                            results.add(times[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "drcs is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getAllDreamTime", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public List<String> getAllDeviceName() {
        List<String> results = new ArrayList();
        if (this.mSettingService != null) {
            try {
                String[] names = this.mSettingService.getAllDeviceName();
                if (names != null) {
                    int length = names.length;
                    for (int i = 0; i < length; i++) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d("SkyworthSetting", "output[" + i + "]:" + names[i]);
                        }
                        if (names[i] != null) {
                            results.add(names[i].toString());
                        }
                    }
                } else {
                    LogUtils.m1571e("SkyworthSetting", "drcs is null!");
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getAllDeviceName", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return results;
    }

    public SystemInfo getInfo() {
        String[] infos = new String[0];
        String deviceName = getCurrDeviceName();
        String model = "";
        String version = "";
        String systemVersion = "";
        String ip = "";
        String mac = "";
        String wirless = "";
        if (this.mSettingService != null) {
            try {
                infos = this.mSettingService.getInfo();
                if (infos != null) {
                    if (infos.length > 5) {
                        model = infos[0];
                        version = infos[1];
                        systemVersion = infos[2];
                        ip = infos[3];
                        mac = infos[4];
                        wirless = infos[5];
                    }
                    String msg = "";
                    for (String info : infos) {
                        msg = msg + info + ",";
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d("SkyworthSetting", "about:" + msg);
                    }
                }
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getInfo", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return new SystemInfo(deviceName, model, version, systemVersion, ip, mac, wirless);
    }

    public String getCurrOutput() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrOutput();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrOutput", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public String getCurrDRCMode() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrDRCMode();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrDRCMode", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public String getCurrAudioOutputMode() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrAudioOutputMode();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrAudioOutputMode", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public String getCurrScreenSaveTime() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrScreenSaveTime();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrScreenSaveTime", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public String getCurrDreamTime() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrDreamTime();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrDreamTime", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public String getCurrDeviceName() {
        String curr = "";
        if (this.mSettingService != null) {
            try {
                curr = this.mSettingService.getCurrDeviceName();
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call getCurrDeviceName", e);
            }
        } else {
            LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
        }
        return curr;
    }

    public void setOutputDisplay(String output) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setOutputDisplay(output);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setOutputDisplay", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void setAudioOutputMode(String mode) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setAudioOutputMode(mode);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setAudioOutputMode", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void setDRCMode(String mode) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setDRCMode(mode);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setDRCMode", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void setScreenSaverTime(String time) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setScreenSaverTime(time);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setScreenSaverTime", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void setDreamTime(String time) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setDreamTime(time);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setDreamTime", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void setDeviceName(String name) {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.setDeviceName(name);
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call setDeviceName", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void restoreFactory() {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.restoreFactory();
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call restoreFactory", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void goToPositionSetting() {
        if (this.mSettingService != null) {
            try {
                this.mSettingService.goToPositionSetting();
                return;
            } catch (RemoteException e) {
                LogUtils.m1572e("SkyworthSetting", "occurs error when call goToPositionSetting", e);
                return;
            }
        }
        LogUtils.m1571e("SkyworthSetting", "skyworth setting service is null!");
    }

    public void goToNetworkSettings() {
        Intent intent = new Intent(ACTION_NETWORK_SETTING);
        try {
            intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.m1572e("SkyworthSetting", "activity not fuound!", e);
        }
    }

    public boolean goToAutoTest(MachineModel model) {
        switch (model) {
            case I71:
            case I71C:
                return goToI71AutoTest();
            case I71S:
                return goToI71SAutoTest();
            default:
                return true;
        }
    }

    private boolean goToI71AutoTest() {
        try {
            Intent intent = new Intent();
            intent.setAction("com.mipt.autotestfora8.intent.action.AUTOTEST");
            intent.putExtra("canExit", "true");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            this.mContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            LogUtils.m1571e("AboutSettings", e.getMessage() + "");
            return false;
        }
    }

    private boolean goToI71SAutoTest() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.skyworthdigital.autotest", "com.skyworthdigital.autotest.AutoWelcomeActivity"));
            intent.putExtra("canExit", "true");
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            this.mContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e("SkyworthSetting", e.getMessage() + "");
            return false;
        }
    }

    private void bindService() {
        try {
            this.mContext.bindService(new Intent("gala.settings.aidl.action.AIDL_SETTINGS_SERVICE"), this.conn, 1);
        } catch (Exception e) {
            LogUtils.m1572e("SkyworthSetting", "bindService", e);
        }
    }
}
