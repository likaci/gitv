package com.gala.video.app.epg.ui.setting;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.gala.tvapi.vrs.CDNHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultF4v;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.systeminfo.SystemInfoHelper;
import com.gala.video.app.epg.ui.setting.utils.SettingPingbackUtils;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutActivity extends QMultiScreenActivity implements OnClickListener {
    private static final int MENU_DEFAULT_VALUE = 0;
    private static final int MENU_MAX_CLICK = 5;
    private static final String PATTERN = "(2[0-4][0-9]|25[0-5] |1[0-9][0-9]|[1-9]?[0-9])(\\.(2[0-4][0-9]|25[0-5] |1[0-9][0-9]|[1-9]?[0-9])){3}";
    private static String PUBLIC_IP_NAME = "save_public_ip";
    private static String SETTING_ABOUT_FILE = "about_setting_device";
    private final String LOG_TAG = "EPG/setting/AboutActivity";
    private String PLAYERTYPEKEY = "player_type";
    private Context mContext;
    private String mDNS;
    private TextView mDNSTxt;
    private String mDeviceModel;
    private TextView mDeviceModelTxt;
    private String mDeviceName;
    private TextView mDeviceNameTxt;
    private String mHardInfo;
    private TextView mHardWareInfoTxt;
    private String mIP;
    private TextView mIpInsideTxt;
    private TextView mIpPublicTxt;
    private String mMAC;
    private String mMACWifi;
    private int mMenuCount;
    private long mMenuPreTime;
    private TextView mNetcardWiredTxt;
    private TextView mNetcardWirelessTxt;
    private Button mResetBtn;
    private String mSoftVersion;
    private TextView mSoftVersionTxt;
    private String mSysVersion;
    private TextView mSysVersionTxt;
    private SystemInfo mSystemInfo;
    private boolean mTempLogRecordEnable = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_fragment_setting_about);
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mDeviceNameTxt = (TextView) findViewById(R.id.epg_about_device_name);
        this.mDeviceModelTxt = (TextView) findViewById(R.id.epg_about_device_model);
        this.mSysVersionTxt = (TextView) findViewById(R.id.epg_about_system_version);
        this.mSoftVersionTxt = (TextView) findViewById(R.id.epg_about_soft_version);
        this.mIpInsideTxt = (TextView) findViewById(R.id.epg_about_ip_inside);
        this.mIpPublicTxt = (TextView) findViewById(R.id.epg_about_ip_outside);
        this.mDNSTxt = (TextView) findViewById(R.id.epg_about_dns);
        this.mNetcardWiredTxt = (TextView) findViewById(R.id.epg_about_networkcard_wired);
        this.mNetcardWirelessTxt = (TextView) findViewById(R.id.epg_about_networkcard_wireless);
        this.mHardWareInfoTxt = (TextView) findViewById(R.id.epg_about_hardware_info);
        this.mResetBtn = (Button) findViewById(R.id.epg_about_reset_btn);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mResetBtn.requestFocus();
            this.mResetBtn.setOnClickListener(this);
        } else {
            this.mDeviceModelTxt.setVisibility(8);
            this.mSysVersionTxt.setVisibility(8);
            this.mResetBtn.setVisibility(8);
        }
        ThreadUtils.execute(new Runnable() {
            public void run() {
                AboutActivity.this.mSoftVersion = AboutActivity.this.getSoftVersion();
                if (Project.getInstance().getBuild().isHomeVersion()) {
                    AboutActivity.this.mSystemInfo = Project.getInstance().getConfig().getSystemSetting().getInfo();
                    AboutActivity.this.mDeviceName = Project.getInstance().getConfig().getSystemSetting().getCurrDeviceName();
                    AboutActivity.this.mSoftVersion = AboutActivity.this.getSoftVersionROM();
                } else if (AboutActivity.this.mContext != null) {
                    AboutActivity.this.mSystemInfo = SystemInfoHelper.getSystemInfo(AboutActivity.this.mContext);
                    AboutActivity.this.mDeviceName = SettingSharepreference.getDeviceName(AboutActivity.this.mContext);
                }
                if (AboutActivity.this.mSystemInfo != null) {
                    AboutActivity.this.mDeviceModel = AboutActivity.this.mSystemInfo.getDeviceModel();
                    AboutActivity.this.mSysVersion = AboutActivity.this.mSystemInfo.getSystemVersion();
                    AboutActivity.this.mMAC = AboutActivity.this.mSystemInfo.getMac();
                    AboutActivity.this.mMACWifi = AboutActivity.this.mSystemInfo.getMacWifi();
                    AboutActivity.this.mIP = AboutActivity.this.mSystemInfo.getIpAddr();
                    AboutActivity.this.mHardInfo = AboutActivity.this.getHwver();
                    if (!(Project.getInstance().getBuild().isHomeVersion() || AboutActivity.this.mContext == null)) {
                        AboutActivity.this.mIP = SettingUtils.getIPV4Addr(AboutActivity.this.mContext);
                    }
                    if (StringUtils.isEmpty(AboutActivity.this.mHardInfo)) {
                        AboutActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                AboutActivity.this.mHardWareInfoTxt.setVisibility(8);
                            }
                        });
                    }
                }
                if (AboutActivity.this.mContext != null) {
                    AboutActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            TextView access$1400 = AboutActivity.this.mDeviceNameTxt;
                            Context access$500 = AboutActivity.this.mContext;
                            int i = R.string.setting_about_devicename;
                            Object[] objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mDeviceName) ? "" : AboutActivity.this.mDeviceName;
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mDeviceModelTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_devicemodel;
                            objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mDeviceModel) ? "" : AboutActivity.this.mDeviceModel;
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mSysVersionTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_sysversion;
                            objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mSysVersion) ? "" : AboutActivity.this.mSysVersion;
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mSoftVersionTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_softversion;
                            objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mSoftVersion) ? "" : AboutActivity.this.mSoftVersion;
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mIpInsideTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_ip_inside;
                            objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mIP) ? "" : AboutActivity.this.mIP;
                            access$1400.setText(access$500.getString(i, objArr));
                            if (StringUtils.isEmpty(AboutActivity.this.mIpPublicTxt.getText())) {
                                AboutActivity.this.mIpPublicTxt.setText(AboutActivity.this.mContext.getString(R.string.setting_about_ip_outside_default));
                            }
                            access$1400 = AboutActivity.this.mNetcardWiredTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_netcard_wired;
                            objArr = new Object[1];
                            objArr[0] = AboutActivity.this.mMAC == null ? "" : AboutActivity.this.mMAC.toUpperCase();
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mNetcardWirelessTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_netcard_wireless;
                            objArr = new Object[1];
                            objArr[0] = AboutActivity.this.mMACWifi == null ? "" : AboutActivity.this.mMACWifi.toUpperCase();
                            access$1400.setText(access$500.getString(i, objArr));
                            access$1400 = AboutActivity.this.mHardWareInfoTxt;
                            access$500 = AboutActivity.this.mContext;
                            i = R.string.setting_about_hardware_info;
                            objArr = new Object[1];
                            objArr[0] = AboutActivity.this.mHardInfo == null ? "" : AboutActivity.this.mHardInfo;
                            access$1400.setText(access$500.getString(i, objArr));
                        }
                    });
                }
                PingBackParams params1 = new PingBackParams();
                params1.add(Keys.T, "21").add("bstp", "1").add("qtcurl", Values.about).add("block", Values.about);
                PingBack.getInstance().postPingBackToLongYuan(params1.build());
                AboutActivity.this.mDNS = DeviceUtils.getDNS();
                AboutActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (AboutActivity.this.mDNSTxt != null && AboutActivity.this.mContext != null) {
                            TextView access$2300 = AboutActivity.this.mDNSTxt;
                            Context access$500 = AboutActivity.this.mContext;
                            int i = R.string.setting_about_dns;
                            Object[] objArr = new Object[1];
                            objArr[0] = StringUtils.isEmpty(AboutActivity.this.mDNS) ? "" : AboutActivity.this.mDNS;
                            access$2300.setText(access$500.getString(i, objArr));
                        }
                    }
                });
            }
        });
    }

    protected void onResume() {
        super.onResume();
        LogUtils.i("EPG/setting/AboutActivity", "onResume() --- setDefaultValue is 0");
        this.mMenuCount = 0;
        this.mMenuPreTime = 0;
        this.mTempLogRecordEnable = GetInterfaceTools.getILogRecordProvider().isLogRecordEnable();
        GetInterfaceTools.getILogRecordProvider().setLogRecordEnable(false);
        initCDNIPData();
    }

    public void onStop() {
        super.onStop();
        GetInterfaceTools.getILogRecordProvider().setLogRecordEnable(this.mTempLogRecordEnable);
    }

    private String getSoftVersionROM() {
        if (this.mSystemInfo != null) {
            CharSequence romVersion = StringUtils.isEmpty(this.mSystemInfo.getSoftwareVersion()) ? "" : this.mSystemInfo.getSoftwareVersion();
            if (!StringUtils.isEmpty(romVersion)) {
                this.mSoftVersion = romVersion + "(" + this.mSoftVersion + ")";
            }
        }
        return this.mSoftVersion;
    }

    private String getSoftVersion() {
        StringBuilder builder = new StringBuilder(Project.getInstance().getBuild().getVersionString());
        CharSequence uuid = Project.getInstance().getBuild().getVrsUUID();
        if (!StringUtils.isEmpty(uuid)) {
            int length = uuid.length();
            if (length >= 5) {
                LogUtils.d("EPG/setting/AboutActivity", ">>>>>getSoftVersion()---uuid=", uuid);
                builder.append("(").append(uuid.substring(length - 5, length));
            }
        }
        CharSequence playerMode = getPlayerMode();
        if (!StringUtils.isEmpty(playerMode)) {
            builder.append("_").append(playerMode);
        }
        builder.append(")");
        return builder.toString();
    }

    private String getPlayerMode() {
        return new AppPreference(this.mContext, this.PLAYERTYPEKEY).get(this.PLAYERTYPEKEY);
    }

    private String getHwver() {
        return Build.MODEL.replace(" ", "-");
    }

    public void onClick(View v) {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            Project.getInstance().getConfig().getSystemSetting().restoreFactory();
        }
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        switch (event.getKeyCode()) {
            case 82:
                if (this.mMenuCount == 5 && Project.getInstance().getBuild().isHomeVersion()) {
                    LogUtils.i("EPG/setting/AboutActivity", "onClick Menu ---- Project.get().getConfig().getSystemSetting().goToAutoTest()");
                    this.mMenuCount = 0;
                    this.mMenuPreTime = 0;
                    Project.getInstance().getConfig().getSystemSetting().goToAutoTest();
                } else {
                    long curTime = System.currentTimeMillis();
                    if (this.mMenuPreTime == 0 || curTime - this.mMenuPreTime <= 1000) {
                        this.mMenuCount++;
                        LogUtils.i("EPG/setting/AboutActivity", "onClick Menu ---- count++ 【" + this.mMenuCount + "】");
                    } else {
                        LogUtils.e("EPG/setting/AboutActivity", "onClick Menu ---- time > 1s --- reset count 0");
                        this.mMenuCount = 0;
                        curTime = 0;
                    }
                    this.mMenuPreTime = curTime;
                }
                return true;
            default:
                return super.handleKeyEvent(event);
        }
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_about_setting_new);
    }

    private void savePublicIp(String ip) {
        new AppPreference(this.mContext, SETTING_ABOUT_FILE).save(PUBLIC_IP_NAME, ip);
    }

    private String getPublicIp() {
        CharSequence ip = new AppPreference(this.mContext, SETTING_ABOUT_FILE).get(PUBLIC_IP_NAME, "");
        return StringUtils.isEmpty(ip) ? AppRuntimeEnv.get().getDeviceIp() : ip;
    }

    private void initCDNIPData() {
        CDNHelper.testStress.call(new IVrsCallback<ApiResultF4v>() {
            public void onSuccess(ApiResultF4v result) {
                AboutActivity.this.onSuccessResult(result);
            }

            public void onException(ApiException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e("EPG/setting/AboutActivity", "initCDNIPData()  e" + e);
                }
                SettingPingbackUtils.apiError(e, "CDNHelper.testStress");
                AboutActivity.this.refreshPublicIp(AboutActivity.this.getPublicIp());
            }
        }, "");
    }

    private void onSuccessResult(ApiResultF4v result) {
        String publicIP;
        if (LogUtils.mIsDebug) {
            LogUtils.d("EPG/setting/AboutActivity", "onSuccessResult() -> ip:" + result);
        }
        if (result == null || StringUtils.isEmpty(result.t)) {
            publicIP = getPublicIp();
        } else {
            CharSequence ip = getMatchingIP(result.t);
            if (LogUtils.mIsDebug) {
                LogUtils.d("EPG/setting/AboutActivity", "onSuccessResult() -> ip:" + ip);
            }
            if (StringUtils.isEmpty(ip)) {
                publicIP = getPublicIp();
            } else {
                publicIP = ip;
                savePublicIp(publicIP);
            }
        }
        refreshPublicIp(publicIP);
    }

    private String getMatchingIP(String address) {
        Matcher m = Pattern.compile(PATTERN).matcher(address);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private void refreshPublicIp(final String ip) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (AboutActivity.this.mIpPublicTxt != null && AboutActivity.this.mContext != null) {
                    AboutActivity.this.mIpPublicTxt.setText(AboutActivity.this.mContext.getString(R.string.setting_about_ip_outside, new Object[]{ip}));
                }
            }
        });
    }
}
