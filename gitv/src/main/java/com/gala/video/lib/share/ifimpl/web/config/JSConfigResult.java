package com.gala.video.lib.share.ifimpl.web.config;

import com.alibaba.fastjson.JSONObject;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigResult;
import java.io.Serializable;

class JSConfigResult implements IJSConfigResult, Serializable {
    private static final String TAG = "EPG/web/JSConfigResult";
    private static final long serialVersionUID = 1799289361357494630L;
    private boolean isCrosswalkEnable = false;
    public boolean isEnableHardware = false;
    private boolean isLogRecordEnable = true;
    private JSONObject mJsonObject;
    private int mmemorylevel = 2;

    JSConfigResult() {
    }

    public void init(JSONObject jsonObject) {
        this.mJsonObject = jsonObject;
        initMemoryConfig();
        initLogRecordConfig();
    }

    private String getValue(String key) {
        String string;
        synchronized (this) {
            string = this.mJsonObject == null ? "" : this.mJsonObject.getString(key);
        }
        return string;
    }

    public String getValueByKey(String key) {
        return getValue(key);
    }

    public String getUrlRoleActivity() {
        return getValue("weburl_role_activity");
    }

    public int getMemoryLevel() {
        return this.mmemorylevel;
    }

    public void setMemoryLevel(int level) {
        this.mmemorylevel = level;
    }

    public String getUrlGetGold() {
        return getValue("weburl_getGold");
    }

    public String getUrlMultiscreen() {
        return getValue("weburl_multiscreen");
    }

    public String getUrlMemberRights() {
        return getValue("weburl_member_rights");
    }

    public String getUrlMemberPackage() {
        return getValue("weburl_member_package");
    }

    public String getUrlFAQ() {
        return getValue("weburl_faq");
    }

    public String getUrlSignIn() {
        return getValue("weburl_signin");
    }

    public String getUrlCoupon() {
        return getValue("weburl_coupon");
    }

    public String getUrlSubject() {
        return getValue("weburl_subject");
    }

    public String getThridSpeedSecordURL() {
        return getValue("third_speed_url_second");
    }

    public String getThridSpeedFirstURL() {
        return getValue("third_speed_url_first");
    }

    public String getPingDomains() {
        return getValue("ping_domains");
    }

    public String getTracertDomains() {
        return getValue("tracert_domains");
    }

    public String getNsLookDomains() {
        return getValue("nslook_domains");
    }

    public boolean isOpenHardWardEnable() {
        this.isEnableHardware = "1".equals(getValue("enableHardware"));
        return this.isEnableHardware;
    }

    public boolean isCrosswalkEnable() {
        this.isCrosswalkEnable = "1".equals(getValue("enableCrosswalk"));
        return this.isCrosswalkEnable;
    }

    public boolean isAccelerateExclude(String url) {
        String webSolutionExcludeUrls = getValue("web_solution_exclude_url");
        if (StringUtils.isEmpty((CharSequence) url) || StringUtils.isEmpty(new String[0])) {
            return false;
        }
        String[] excludeUrls = StringUtils.split(webSolutionExcludeUrls, ",");
        for (String indexOf : excludeUrls) {
            if (url.indexOf(indexOf) >= 0) {
                return true;
            }
        }
        return false;
    }

    private void initMemoryConfig() {
        String memorylevel = getValue("memorylevel");
        LogUtils.d(TAG, "get  memorylevel:" + memorylevel);
        if (memorylevel != null) {
            try {
                this.mmemorylevel = Integer.parseInt(memorylevel);
            } catch (NumberFormatException e) {
                this.mmemorylevel = 2;
                e.printStackTrace();
            }
        }
        LogUtils.d(TAG, "get  mmemorylevel:" + this.mmemorylevel);
    }

    private void initLogRecordConfig() {
        GetInterfaceTools.getILogRecordProvider().getLogCore().setJSCongfig(this.mJsonObject.toJSONString());
        String enableLogRecord = getValue("enableLogRecord");
        LogUtils.d(TAG, "setLogRecordConfig() -> enableLogRecord :" + enableLogRecord);
        this.isLogRecordEnable = "1".equals(enableLogRecord);
        LogUtils.d(TAG, "setLogRecordConfig() -> logRecordOpen :" + this.isLogRecordEnable);
        GetInterfaceTools.getILogRecordProvider().setLogRecordEnable(this.isLogRecordEnable);
    }

    public String toString() {
        return this.mJsonObject != null ? this.mJsonObject.toJSONString() : "mJsonObject is null!";
    }
}
