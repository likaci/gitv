package com.gala.video.app.epg.ui.setting.interactor.impl;

import android.content.Context;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.interactor.SettingAboutInteractor;
import com.gala.video.app.epg.ui.setting.systeminfo.SystemInfoHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingAboutInteractorImpl implements SettingAboutInteractor {
    public static final String CABLE_MAC = "cable_mac";
    public static final String DNS = "dns";
    public static final String PRIVATE_IP = "private_ip";
    public static final String PUBLIC_IP = "public_ip";
    private static String PUBLIC_IP_NAME = "save_public_ip";
    private static String SETTING_ABOUT_FILE = "about_setting_device";
    public static final String SOFTWARE_VERSION = "software_version";
    public static final String SYSTEM_VERSION = "system_version";
    public static final String WIRELESS_MAC = "wireless_mac";
    private Context mContext;
    private Map<String, String> mMap;
    private SystemInfo mSystemInfo;

    public SettingAboutInteractorImpl(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mSystemInfo = SystemInfoHelper.getSystemInfo(context);
            this.mMap = new HashMap();
            return;
        }
        throw new IllegalArgumentException("Context not is null.");
    }

    public String getSystemVersion() {
        CharSequence systemVersion = (String) this.mMap.get(SYSTEM_VERSION);
        if (StringUtils.isEmpty(systemVersion)) {
            return this.mSystemInfo.getSystemVersion();
        }
        return systemVersion;
    }

    public String getSoftVersion() {
        CharSequence softwareVersion = (String) this.mMap.get(SOFTWARE_VERSION);
        if (StringUtils.isEmpty(softwareVersion)) {
            return Project.getInstance().getBuild().getVersionString();
        }
        return softwareVersion;
    }

    public String getPrivateIP() {
        CharSequence privateIP = (String) this.mMap.get(PRIVATE_IP);
        if (StringUtils.isEmpty(privateIP)) {
            return this.mSystemInfo.getIpAddr();
        }
        return privateIP;
    }

    public String getPublicIP() {
        CharSequence publicIP = (String) this.mMap.get(PUBLIC_IP);
        if (!StringUtils.isEmpty(publicIP)) {
            publicIP = new AppPreference(this.mContext, SETTING_ABOUT_FILE).get(PUBLIC_IP_NAME, "");
        }
        return StringUtils.isEmpty(publicIP) ? AppRuntimeEnv.get().getDeviceIp() : publicIP;
    }

    public void savePublicIP(String ip) {
        new AppPreference(this.mContext, SETTING_ABOUT_FILE).save(PUBLIC_IP_NAME, ip);
    }

    public String getDNS() {
        CharSequence dns = (String) this.mMap.get(DNS);
        if (StringUtils.isEmpty(dns)) {
            return DeviceUtils.getDNS();
        }
        return dns;
    }

    public String getCableMac() {
        CharSequence cableMac = (String) this.mMap.get(CABLE_MAC);
        if (StringUtils.isEmpty(cableMac)) {
            cableMac = this.mSystemInfo.getMac();
        }
        return StringUtils.isEmpty(cableMac) ? "" : cableMac.toUpperCase();
    }

    public String getWirelessMac() {
        CharSequence wirelessMac = (String) this.mMap.get(WIRELESS_MAC);
        if (StringUtils.isEmpty(wirelessMac)) {
            wirelessMac = DeviceUtils.getWifiMAC(this.mContext);
        }
        return StringUtils.isEmpty(wirelessMac) ? "" : wirelessMac.toUpperCase();
    }

    public Map<String, String> getSystemData() {
        fetchData();
        return this.mMap;
    }

    public List<String> getCustomData() {
        return CustomSettingProvider.getInstance().getAboutDev();
    }

    private void fetchData() {
        String systemVersion = getSystemVersion();
        String softwareVersion = getSoftVersion();
        String privateIP = getPrivateIP();
        String publicIP = getPublicIP();
        String dns = getDNS();
        String cableMac = getCableMac();
        String wirelessMac = getWirelessMac();
        this.mMap.put(SYSTEM_VERSION, systemVersion);
        this.mMap.put(SOFTWARE_VERSION, softwareVersion);
        this.mMap.put(PRIVATE_IP, privateIP);
        this.mMap.put(PUBLIC_IP, publicIP);
        this.mMap.put(DNS, dns);
        this.mMap.put(CABLE_MAC, cableMac);
        this.mMap.put(WIRELESS_MAC, wirelessMac);
    }
}
