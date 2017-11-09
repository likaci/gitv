package com.gala.video.app.epg.ui.setting.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tvapi.vrs.CDNHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultF4v;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.systeminfo.SystemInfoHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.proguard.Keep;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.SystemInfo;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Keep
public class SettingAboutFragmentForLauncher extends SettingBaseFragment implements OnClickListener {
    private static final int MAX_SHOW_SIZE = 10;
    private static final String PATTERN = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private static String PUBLIC_IP_NAME = "save_public_ip";
    private static String SETTING_ABOUT_FILE = "about_setting_device";
    private final String LOG_TAG = "EPG/setting/SettingAboutFragmentForLauncher";
    private String getPublicIpName = "";
    private TextView mDeviceName;
    private LinearLayout mLinearLayout;
    private List<String> mList = new ArrayList();
    private View mMainView;
    private TextView mPublicIp;
    private SystemInfo mSystemInfo;

    class C10601 implements IVrsCallback<ApiResultF4v> {
        C10601() {
        }

        public void onSuccess(ApiResultF4v result) {
            SettingAboutFragmentForLauncher.this.onSuccessResult(result);
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e("EPG/setting/SettingAboutFragmentForLauncher", "initCDNIPData()  e" + e);
            }
            SettingAboutFragmentForLauncher.this.refreshPublicIp(SettingAboutFragmentForLauncher.this.getPublicIp());
        }
    }

    public void onClick(View v) {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSettingEvent != null) {
            LogUtils.m1574i("EPG/setting/SettingAboutFragmentForLauncher", "onAttach  mSettingEvent.onAttachActivity(this)");
            this.mSettingEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int size = 10;
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_setting_about_for_launcher, null);
        this.mDeviceName = (TextView) this.mMainView.findViewById(C0508R.id.epg_about_device_name);
        this.mLinearLayout = (LinearLayout) this.mMainView.findViewById(C0508R.id.epg_about_main_layout);
        LayoutParams params = new LayoutParams(-2, getDimen(C0508R.dimen.dimen_40dp));
        params.leftMargin = getDimen(C0508R.dimen.dimen_16dp);
        params.gravity |= 16;
        initData();
        if (!ListUtils.isEmpty(this.mList)) {
            if (this.mList.size() <= 10) {
                size = this.mList.size();
            }
            for (int i = 0; i < size; i++) {
                TextView view = createView((String) this.mList.get(i));
                if (i == 1) {
                    this.mPublicIp = view;
                }
                this.mLinearLayout.addView(view, params);
            }
        }
        String deviceName = SettingSharepreference.getDeviceName(this.mContext);
        if (StringUtils.isEmpty((CharSequence) deviceName)) {
            deviceName = "客厅的电视";
        }
        this.mDeviceName.setText(getString(C0508R.string.setting_about_devicename, deviceName));
        return this.mMainView;
    }

    private TextView createView(String string) {
        TextView view = new TextView(this.mContext);
        view.setText(string);
        view.setSingleLine(true);
        view.setEllipsize(TruncateAt.valueOf("END"));
        view.setTextSize(0, this.mContext.getResources().getDimension(C0508R.dimen.dimen_23dp));
        view.setTextColor(this.mContext.getResources().getColor(C0508R.color.white));
        view.setIncludeFontPadding(false);
        return view;
    }

    private void initData() {
        initCDNIPData();
        this.mSystemInfo = SystemInfoHelper.getSystemInfo(this.mContext);
        String softVersion = Project.getInstance().getBuild().getVersionString();
        this.mList.add(getString(C0508R.string.setting_about_softversion, softVersion));
        this.mList.add(getString(C0508R.string.setting_about_ip_outside, getPublicIp()));
        String mMAC = this.mSystemInfo.getMac();
        int i = C0508R.string.setting_about_netcard_wired;
        Object[] objArr = new Object[1];
        objArr[0] = mMAC == null ? "" : mMAC.toUpperCase();
        this.mList.add(getString(i, objArr));
        if (!StringUtils.isEmpty(this.mSystemInfo.getSystemVersion())) {
            this.mList.add(getString(C0508R.string.setting_about_sysversion, this.mSystemInfo.getSystemVersion()));
        }
        CharSequence mIP = this.mSystemInfo.getIpAddr();
        if (!StringUtils.isEmpty(mIP) && isIP(mIP)) {
            this.mList.add(getString(C0508R.string.setting_about_ip_inside, mIP));
        }
        if (!StringUtils.isEmpty(DeviceUtils.getWifiMAC(this.mContext))) {
            this.mList.add(getString(C0508R.string.setting_about_netcard_wireless, DeviceUtils.getWifiMAC(this.mContext).toUpperCase()));
        }
        List customInfos = CustomSettingProvider.getInstance().getAboutDev();
        if (!ListUtils.isEmpty(customInfos) && this.mList != null) {
            this.mList.addAll(customInfos);
        }
    }

    private void initCDNIPData() {
        CDNHelper.testStress.call(new C10601(), "");
    }

    private void onSuccessResult(ApiResultF4v result) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("EPG/setting/SettingAboutFragmentForLauncher", "onSuccessResult() -> ip:" + result);
        }
        String publicIP = "";
        if (result == null || StringUtils.isEmpty(result.f1347t)) {
            this.getPublicIpName = getPublicIp();
        } else {
            CharSequence ip = getMatchingIP(result.f1347t);
            if (StringUtils.isEmpty(ip)) {
                this.getPublicIpName = getPublicIp();
            } else {
                publicIP = ip;
                savePublicIp(publicIP);
            }
        }
        refreshPublicIp(publicIP);
    }

    private void savePublicIp(String ip) {
        new AppPreference(getActivity(), SETTING_ABOUT_FILE).save(PUBLIC_IP_NAME, ip);
    }

    private String getPublicIp() {
        CharSequence ip = new AppPreference(getActivity(), SETTING_ABOUT_FILE).get(PUBLIC_IP_NAME, "");
        return StringUtils.isEmpty(ip) ? AppRuntimeEnv.get().getDeviceIp() : ip;
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
                    if (SettingAboutFragmentForLauncher.this.mPublicIp != null && SettingAboutFragmentForLauncher.this.mContext != null) {
                        SettingAboutFragmentForLauncher.this.mPublicIp.setText(SettingAboutFragmentForLauncher.this.getString(C0508R.string.setting_about_ip_outside, ip));
                    }
                }
            });
        }
    }

    private boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || StringUtils.isEmpty((CharSequence) addr)) {
            return false;
        }
        return Pattern.compile(PATTERN).matcher(addr).find();
    }
}
