package com.push.pushservice.data;

import android.text.TextUtils;
import com.push.pushservice.utils.LogUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppListInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private List<AppInfo> appList = new ArrayList();
    private List<String> hostList = new ArrayList();

    public List<String> getHostList() {
        return this.hostList;
    }

    public void setHostList(List<String> list) {
        this.hostList.clear();
        if (list != null && list.size() > 0) {
            this.hostList.addAll(list);
        }
    }

    public List<AppInfo> getAppList() {
        return this.appList;
    }

    public void setAppList(List<AppInfo> list) {
        this.appList.clear();
        if (list != null && list.size() > 0) {
            this.appList.addAll(list);
        }
    }

    public boolean isExistAppid(short appid, String deviceId, String appVer) {
        for (AppInfo info : this.appList) {
            if (info.getAppid() == appid && info.getDeviceId().equals(deviceId) && info.getAppVer() != null && info.getAppVer().equals(appVer)) {
                return true;
            }
        }
        return false;
    }

    public void addAppInfo(short appid, String deviceId, String packageName, String appVer) {
        if (appid >= (short) 0 && !TextUtils.isEmpty(deviceId) && !isExistAppid(appid, deviceId, appVer)) {
            try {
                if (this.appList.size() > 0) {
                    for (AppInfo app : this.appList) {
                        if (app != null && app.getAppid() == appid) {
                            this.appList.remove(app);
                            LogUtils.logd("版本发生变化，从日志里面删除掉 app_id:" + appid + " appVer:" + app.getAppVer() + " 记录");
                        }
                    }
                }
            } catch (Exception e) {
            }
            this.appList.add(new AppInfo(appid, deviceId, packageName, appVer));
        }
    }

    public AppInfo getAppInfoSame(String packageName) {
        for (AppInfo info : this.appList) {
            if (info.getPackageName().contains(packageName)) {
                return info;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (this.appList != null) {
            str.append("appList: \n");
            for (AppInfo info : this.appList) {
                str.append("appid = " + info.getAppid());
                str.append(", deviceID = " + info.getDeviceId());
                str.append(", packageName = " + info.getPackageName());
                str.append(", appVer = " + info.getAppVer());
                str.append(", isRegister = " + info.getIsRegister());
                str.append("\n");
            }
        }
        if (this.hostList != null) {
            str.append("hostList: \n");
            for (String host : this.hostList) {
                str.append("host = " + host);
                str.append("\n");
            }
        }
        return str.toString();
    }
}
