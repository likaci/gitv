package com.tvos.appmanager.sort;

import com.tvos.appmanager.model.IAppInfo;
import java.util.Collections;
import java.util.List;

public class AppSortUtil {
    public static final int SORT_METHOD_INSTALLTIME = 1;
    public static final int SORT_METHOD_NONE = -1;
    public static final int SORT_METHOD_STARTTIME = 2;

    public static List<IAppInfo> sortAppListByInstallTime(List<IAppInfo> appInfos) {
        Collections.sort(appInfos, new AppComparator(1));
        return appInfos;
    }

    public static List<IAppInfo> sortAppListByStartTime(List<IAppInfo> appInfos) {
        Collections.sort(appInfos, new AppComparator(2));
        return appInfos;
    }
}
