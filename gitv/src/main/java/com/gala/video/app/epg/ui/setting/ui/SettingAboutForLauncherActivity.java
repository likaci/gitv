package com.gala.video.app.epg.ui.setting.ui;

import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tvapi.vrs.CDNHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultF4v;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.interactor.impl.SettingAboutInteractorImpl;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingAboutForLauncherActivity extends QMultiScreenActivity {
    private static final String CUSTOM_KEY = "custom";
    private static final int MAX_SHOW_SIZE = 10;
    private static final String PATTERN = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private static final String TAG = "SettingAboutForLauncherActivity";
    private Map<String, String> mData;
    private SettingAboutInteractorImpl mInteractorImpl;
    private LinearLayout mLlyt;
    private TextView mTvDeviceName;
    private Map<String, View> mViews = new HashMap();

    static class Order {
        String mKey;
        int mNameId;
        String mValue;

        public Order(String key, String value) {
            this.mKey = key;
            this.mValue = value;
        }

        public Order(String key, int nameId) {
            this.mKey = key;
            this.mNameId = nameId;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_fragment_setting_about_for_launcher);
        this.mLlyt = (LinearLayout) findViewById(R.id.epg_about_main_layout);
        this.mTvDeviceName = (TextView) findViewById(R.id.epg_about_device_name);
        initData();
        fillData();
        getDNS();
        initCDNIPData();
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_setting_about_content);
    }

    private void initData() {
        String deviceName = SettingSharepreference.getDeviceName(this);
        if (StringUtils.isEmpty((CharSequence) deviceName)) {
            deviceName = "客厅的电视";
        }
        this.mTvDeviceName.setText(getString(R.string.setting_about_devicename, new Object[]{deviceName}));
        this.mInteractorImpl = new SettingAboutInteractorImpl(this);
        this.mData = this.mInteractorImpl.getSystemData();
    }

    public List<Order> getShowOrder() {
        String[] keys = new String[]{SettingAboutInteractorImpl.SYSTEM_VERSION, SettingAboutInteractorImpl.SOFTWARE_VERSION, SettingAboutInteractorImpl.PRIVATE_IP, SettingAboutInteractorImpl.PUBLIC_IP, SettingAboutInteractorImpl.DNS, SettingAboutInteractorImpl.CABLE_MAC, SettingAboutInteractorImpl.WIRELESS_MAC};
        int[] nameId = new int[]{R.string.setting_about_sysversion, R.string.setting_about_softversion, R.string.setting_about_ip_private, R.string.setting_about_ip_public, R.string.setting_about_dns_launcher, R.string.setting_about_netcard_wired, R.string.setting_about_netcard_wireless};
        List<Order> orderList = new ArrayList();
        int length = Math.min(keys.length, nameId.length);
        for (int i = 0; i < length; i++) {
            orderList.add(new Order(keys[i], nameId[i]));
        }
        return orderList;
    }

    private void fillData() {
        int size = 10;
        this.mLlyt.removeAllViews();
        List<Order> orders = formatData();
        if (orders.size() <= 10) {
            size = orders.size();
        }
        int i = 0;
        while (i < size) {
            Order order = (Order) orders.get(i);
            if (order != null) {
                buildItem(order.mKey, order.mValue);
                i++;
            } else {
                return;
            }
        }
    }

    private List<Order> formatData() {
        List<Order> data = new ArrayList();
        for (Order order : getShowOrder()) {
            if (!StringUtils.isEmpty((String) this.mData.get(order.mKey)) || order.mKey.equals(SettingAboutInteractorImpl.DNS)) {
                LogUtils.i(TAG, order.mValue);
                order.mValue = getString(order.mNameId, new Object[]{value});
                data.add(order);
            }
        }
        data.addAll(getCustomData());
        return data;
    }

    private List<Order> getCustomData() {
        List customData = this.mInteractorImpl.getCustomData();
        List<Order> result = new ArrayList();
        if (!ListUtils.isEmpty(customData)) {
            for (int i = 0; i < customData.size(); i++) {
                result.add(new Order(CUSTOM_KEY + i, (String) customData.get(i)));
            }
        }
        return result;
    }

    private TextView createView(String string) {
        TextView view = new TextView(this);
        view.setText(string);
        view.setSingleLine(true);
        view.setEllipsize(TruncateAt.valueOf("END"));
        view.setTextSize(0, getResources().getDimension(R.dimen.dimen_23sp));
        view.setTextColor(getResources().getColor(R.color.setting_white));
        view.setIncludeFontPadding(false);
        LayoutParams params = new LayoutParams(-2, getDimen(R.dimen.dimen_40dp));
        params.leftMargin = getDimen(R.dimen.dimen_16dp);
        params.gravity |= 16;
        view.setLayoutParams(params);
        return view;
    }

    protected int getDimen(int id) {
        return (int) getResources().getDimension(id);
    }

    private void initCDNIPData() {
        CDNHelper.testStress.call(new IVrsCallback<ApiResultF4v>() {
            public void onSuccess(ApiResultF4v result) {
                SettingAboutForLauncherActivity.this.onSuccessResult(result);
            }

            public void onException(ApiException e) {
                LogUtils.e(SettingAboutForLauncherActivity.TAG, "initCDNIPData()  e" + e);
                SettingAboutForLauncherActivity.this.refreshPublicIP(SettingAboutForLauncherActivity.this.getPublicIP());
            }
        }, "");
    }

    private void onSuccessResult(ApiResultF4v result) {
        LogUtils.d(TAG, "onSuccessResult() -> ip:" + result);
        String publicIP = getPublicIP();
        if (!(result == null || StringUtils.isEmpty(result.t))) {
            CharSequence ip = getMatchingIP(result.t);
            if (!StringUtils.isEmpty(ip)) {
                publicIP = ip;
                savePublicIp(publicIP);
            }
        }
        refreshPublicIP(publicIP);
    }

    private void savePublicIp(String ip) {
        this.mInteractorImpl.savePublicIP(ip);
    }

    private String getPublicIP() {
        return this.mInteractorImpl.getPublicIP();
    }

    private String getMatchingIP(String address) {
        Matcher m = Pattern.compile(PATTERN).matcher(address);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private void getDNS() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SettingAboutForLauncherActivity.this.refreshDNS(SettingAboutForLauncherActivity.this.mInteractorImpl.getDNS());
            }
        });
    }

    private void refreshPublicIP(final String ip) {
        runOnUiThread(new Runnable() {
            public void run() {
                SettingAboutForLauncherActivity.this.buildItem(SettingAboutInteractorImpl.PUBLIC_IP, SettingAboutForLauncherActivity.this.getString(R.string.setting_about_ip_public, new Object[]{ip}));
            }
        });
    }

    private void refreshDNS(final String dns) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (StringUtils.isEmpty(dns)) {
                    TextView tv = (TextView) SettingAboutForLauncherActivity.this.mViews.get(SettingAboutInteractorImpl.DNS);
                    if (tv != null) {
                        SettingAboutForLauncherActivity.this.mLlyt.removeView(tv);
                        SettingAboutForLauncherActivity.this.mViews.remove(tv);
                        return;
                    }
                    return;
                }
                SettingAboutForLauncherActivity.this.buildItem(SettingAboutInteractorImpl.DNS, SettingAboutForLauncherActivity.this.getString(R.string.setting_about_dns_launcher, new Object[]{dns}));
            }
        });
    }

    private void buildItem(String key, String value) {
        TextView tv = (TextView) this.mViews.get(key);
        if (tv == null) {
            tv = createView(value);
            this.mLlyt.addView(tv);
            this.mViews.put(key, tv);
            return;
        }
        tv.setText(value);
    }
}
