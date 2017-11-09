package com.gala.video.app.epg.ui.setting.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingAboutFragment extends SettingBaseFragment implements OnClickListener {
    private static final int MENU_DEFAULT_VALUE = 0;
    private static final int MENU_MAX_CLICK = 5;
    private static final String PATTERN = "(2[0-4][0-9]|25[0-5] |1[0-9][0-9]|[1-9]?[0-9])(\\.(2[0-4][0-9]|25[0-5] |1[0-9][0-9]|[1-9]?[0-9])){3}";
    private static String PUBLIC_IP_NAME = "save_public_ip";
    private static String SETTING_ABOUT_FILE = "about_setting_device";
    private final String LOG_TAG = "EPG/setting/SettingAboutFragment";
    private String mDNS;
    private TextView mDNSTxt;
    private String mDeviceModel;
    private TextView mDeviceModelTxt;
    private String mDeviceName;
    private TextView mDeviceNameTxt;
    private String mIP;
    private TextView mIpInsideTxt;
    private TextView mIpPublicTxt;
    private String mMAC;
    private String mMACWifi;
    private View mMainView;
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

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSettingEvent != null) {
            LogUtils.i("EPG/setting/SettingAboutFragment", "onAttach --- mSettingEvent.onAttachActivity(this)");
            this.mSettingEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_setting_about, null);
        this.mDeviceNameTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_device_name);
        this.mDeviceModelTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_device_model);
        this.mSysVersionTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_system_version);
        this.mSoftVersionTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_soft_version);
        this.mIpInsideTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_ip_inside);
        this.mIpPublicTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_ip_outside);
        this.mDNSTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_dns);
        this.mNetcardWiredTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_networkcard_wired);
        this.mNetcardWirelessTxt = (TextView) this.mMainView.findViewById(R.id.epg_about_networkcard_wireless);
        this.mResetBtn = (Button) this.mMainView.findViewById(R.id.epg_about_reset_btn);
        this.mSoftVersion = getSoftVersion();
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mSystemInfo = Project.getInstance().getConfig().getSystemSetting().getInfo();
            this.mDeviceName = Project.getInstance().getConfig().getSystemSetting().getCurrDeviceName();
            this.mResetBtn.requestFocus();
            this.mResetBtn.setOnClickListener(this);
            this.mSoftVersion = getSoftVersionROM();
        } else {
            this.mDeviceModelTxt.setVisibility(8);
            this.mSysVersionTxt.setVisibility(8);
            this.mResetBtn.setVisibility(8);
            if (this.mContext != null) {
                this.mSystemInfo = SystemInfoHelper.getSystemInfo(this.mContext);
                this.mDeviceName = SettingSharepreference.getDeviceName(this.mContext);
            }
        }
        if (this.mSystemInfo != null) {
            this.mDeviceModel = this.mSystemInfo.getDeviceModel();
            this.mSysVersion = this.mSystemInfo.getSystemVersion();
            this.mMAC = this.mSystemInfo.getMac();
            this.mMACWifi = this.mSystemInfo.getMacWifi();
            this.mIP = this.mSystemInfo.getIpAddr();
            if (!(Project.getInstance().getBuild().isHomeVersion() || this.mContext == null)) {
                this.mIP = SettingUtils.getIPV4Addr(this.mContext);
            }
        }
        if (this.mContext != null) {
            TextView textView = this.mDeviceNameTxt;
            Context context = this.mContext;
            int i = R.string.setting_about_devicename;
            Object[] objArr = new Object[1];
            objArr[0] = StringUtils.isEmpty(this.mDeviceName) ? "" : this.mDeviceName;
            textView.setText(context.getString(i, objArr));
            textView = this.mDeviceModelTxt;
            context = this.mContext;
            i = R.string.setting_about_devicemodel;
            objArr = new Object[1];
            objArr[0] = StringUtils.isEmpty(this.mDeviceModel) ? "" : this.mDeviceModel;
            textView.setText(context.getString(i, objArr));
            textView = this.mSysVersionTxt;
            context = this.mContext;
            i = R.string.setting_about_sysversion;
            objArr = new Object[1];
            objArr[0] = StringUtils.isEmpty(this.mSysVersion) ? "" : this.mSysVersion;
            textView.setText(context.getString(i, objArr));
            textView = this.mSoftVersionTxt;
            context = this.mContext;
            i = R.string.setting_about_softversion;
            objArr = new Object[1];
            objArr[0] = StringUtils.isEmpty(this.mSoftVersion) ? "" : this.mSoftVersion;
            textView.setText(context.getString(i, objArr));
            textView = this.mIpInsideTxt;
            context = this.mContext;
            i = R.string.setting_about_ip_inside;
            objArr = new Object[1];
            objArr[0] = StringUtils.isEmpty(this.mIP) ? "" : this.mIP;
            textView.setText(context.getString(i, objArr));
            this.mIpPublicTxt.setText(this.mContext.getString(R.string.setting_about_ip_outside_default));
            textView = this.mNetcardWiredTxt;
            context = this.mContext;
            i = R.string.setting_about_netcard_wired;
            objArr = new Object[1];
            objArr[0] = this.mMAC == null ? "" : this.mMAC.toUpperCase();
            textView.setText(context.getString(i, objArr));
            textView = this.mNetcardWirelessTxt;
            context = this.mContext;
            i = R.string.setting_about_netcard_wireless;
            objArr = new Object[1];
            objArr[0] = this.mMACWifi == null ? "" : this.mMACWifi.toUpperCase();
            textView.setText(context.getString(i, objArr));
        }
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SettingAboutFragment.this.mDNS = DeviceUtils.getDNS();
                if (SettingAboutFragment.this.getActivity() != null && !SettingAboutFragment.this.getActivity().isFinishing()) {
                    SettingAboutFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (SettingAboutFragment.this.mDNSTxt != null && SettingAboutFragment.this.mContext != null) {
                                TextView access$100 = SettingAboutFragment.this.mDNSTxt;
                                Context context = SettingAboutFragment.this.mContext;
                                int i = R.string.setting_about_dns;
                                Object[] objArr = new Object[1];
                                objArr[0] = StringUtils.isEmpty(SettingAboutFragment.this.mDNS) ? "" : SettingAboutFragment.this.mDNS;
                                access$100.setText(context.getString(i, objArr));
                            }
                        }
                    });
                }
            }
        });
        return this.mMainView;
    }

    public void onResume() {
        super.onResume();
        LogUtils.i("EPG/setting/SettingAboutFragment", "onResume() --- setDefaultValue is 0");
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
                LogUtils.d("EPG/setting/SettingAboutFragment", ">>>>>getSoftVersion()---uuid=", uuid);
                builder.append("(").append(uuid.substring(length - 5, length)).append(")");
            }
        }
        return builder.toString();
    }

    public void onClick(View v) {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            Project.getInstance().getConfig().getSystemSetting().restoreFactory();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 82:
                if (this.mMenuCount == 5 && Project.getInstance().getBuild().isHomeVersion()) {
                    LogUtils.i("EPG/setting/SettingAboutFragment", "onClick Menu ---- Project.get().getConfig().getSystemSetting().goToAutoTest()");
                    this.mMenuCount = 0;
                    this.mMenuPreTime = 0;
                    Project.getInstance().getConfig().getSystemSetting().goToAutoTest();
                } else {
                    long curTime = System.currentTimeMillis();
                    if (this.mMenuPreTime == 0 || curTime - this.mMenuPreTime <= 1000) {
                        this.mMenuCount++;
                        LogUtils.i("EPG/setting/SettingAboutFragment", "onClick Menu ---- count++ 【" + this.mMenuCount + "】");
                    } else {
                        LogUtils.e("EPG/setting/SettingAboutFragment", "onClick Menu ---- time > 1s --- reset count 0");
                        this.mMenuCount = 0;
                        curTime = 0;
                    }
                    this.mMenuPreTime = curTime;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void savePublicIp(String ip) {
        new AppPreference(getActivity(), SETTING_ABOUT_FILE).save(PUBLIC_IP_NAME, ip);
    }

    private String getPublicIp() {
        CharSequence ip = new AppPreference(getActivity(), SETTING_ABOUT_FILE).get(PUBLIC_IP_NAME, "");
        return StringUtils.isEmpty(ip) ? AppRuntimeEnv.get().getDeviceIp() : ip;
    }

    private void initCDNIPData() {
        CDNHelper.testStress.call(new IVrsCallback<ApiResultF4v>() {
            public void onSuccess(ApiResultF4v result) {
                SettingAboutFragment.this.onSuccessResult(result);
            }

            public void onException(ApiException e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e("EPG/setting/SettingAboutFragment", "initCDNIPData()  e" + e);
                }
                SettingPingbackUtils.apiError(e, "CDNHelper.testStress");
                SettingAboutFragment.this.refreshPublicIp(SettingAboutFragment.this.getPublicIp());
            }
        }, "");
    }

    private void onSuccessResult(ApiResultF4v result) {
        String publicIP;
        if (LogUtils.mIsDebug) {
            LogUtils.d("EPG/setting/SettingAboutFragment", "onSuccessResult() -> ip:" + result);
        }
        if (result == null || StringUtils.isEmpty(result.t)) {
            publicIP = getPublicIp();
        } else {
            CharSequence ip = getMatchingIP(result.t);
            if (LogUtils.mIsDebug) {
                LogUtils.d("EPG/setting/SettingAboutFragment", "onSuccessResult() -> ip:" + ip);
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
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (SettingAboutFragment.this.mIpPublicTxt != null && SettingAboutFragment.this.mContext != null) {
                        SettingAboutFragment.this.mIpPublicTxt.setText(SettingAboutFragment.this.mContext.getString(R.string.setting_about_ip_outside, new Object[]{ip}));
                    }
                }
            });
        }
    }
}
