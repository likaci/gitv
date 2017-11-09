package com.gala.appmanager.p004c;

import android.content.Context;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.utils.C0110c;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class C0107a {
    private Context f369a = null;

    public C0107a(Context context) {
        this.f369a = context;
    }

    public List<AppInfo> m233a(List<String> list) {
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
                            AppInfo a = C0110c.m240a(this.f369a, file2.getAbsolutePath());
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
