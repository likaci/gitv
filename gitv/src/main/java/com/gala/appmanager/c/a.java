package com.gala.appmanager.c;

import android.content.Context;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.utils.c;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class a {
    private Context a = null;

    public a(Context context) {
        this.a = context;
    }

    public List<AppInfo> a(List<String> list) {
        List<AppInfo> arrayList = new ArrayList();
        String str = "";
        str = "";
        File file = new File("/data/preinstalled");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isFile()) {
                        String path = file2.getPath();
                        if (".apk".equals(path.substring(path.length() - ".apk".length()))) {
                            AppInfo a = c.a(this.a, file2.getAbsolutePath());
                            if (a != null) {
                                if (list.contains(a.getAppPackageName())) {
                                    Log.d("delete", file2.delete() + "");
                                } else {
                                    a.setApkAbsolutePath(file2.getAbsolutePath());
                                    arrayList.add(a);
                                }
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
