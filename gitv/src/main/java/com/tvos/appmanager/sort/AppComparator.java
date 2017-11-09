package com.tvos.appmanager.sort;

import com.tvos.appmanager.model.IAppInfo;
import com.tvos.appmanager.util.TimeUtil;
import java.text.ParseException;
import java.util.Comparator;

public class AppComparator implements Comparator<IAppInfo> {
    private int mSortMethod = -1;

    public AppComparator(int sortMethod) {
        this.mSortMethod = sortMethod;
    }

    public int compare(IAppInfo appInfo1, IAppInfo appInfo2) {
        switch (this.mSortMethod) {
            case 1:
                return sortByInstallTime(appInfo1, appInfo2);
            case 2:
                return sortByStartTime(appInfo1, appInfo2);
            default:
                return 0;
        }
    }

    private int sortByInstallTime(IAppInfo appInfo1, IAppInfo appInfo2) {
        String installTimeStr1 = appInfo1.getAppInstalledTime();
        String installTimeStr2 = appInfo2.getAppInstalledTime();
        try {
            long installTime1 = TimeUtil.StringToLong(installTimeStr1);
            long installTime2 = TimeUtil.StringToLong(installTimeStr2);
            if (installTime1 < installTime2) {
                return 1;
            }
            if (installTime1 != installTime2) {
                return -1;
            }
            return 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int sortByStartTime(IAppInfo appInfo1, IAppInfo appInfo2) {
        long startTime1 = appInfo1.getStartTime();
        long startTime2 = appInfo2.getStartTime();
        if (startTime1 < startTime2) {
            return 1;
        }
        if (startTime1 == startTime2) {
            return 0;
        }
        return -1;
    }
}
