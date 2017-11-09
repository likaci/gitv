package com.gala.appmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.utils.SerializableList;
import com.gala.appmanager.utils.b;
import com.gala.appmanager.utils.d;
import com.gala.sdk.player.IMediaProfile;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Icon;
import org.cybergarage.upnp.std.av.server.object.item.ItemNode;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

class a {
    private static boolean a = false;
    private static String j = "";
    private final int f222a = 5;
    private Context f223a;
    private com.gala.appmanager.b.a f224a;
    private com.gala.appmanager.c.a f225a;
    private SerializableList<String> f226a = new SerializableList();
    private final String f227a = "AppListManager";
    private HashMap<String, AppInfo> f228a = new HashMap();
    private List<AppInfo> f229a = null;
    private String[] f230a = new String[0];
    private int b = 0;
    private SerializableList<String> f231b = new SerializableList();
    private final String f232b = SOAP.DELIM;
    private List<String> f233b = new ArrayList();
    private String[] f234b = new String[0];
    private int c = 16;
    private final String f235c = "topCount";
    private String[] f236c = new String[0];
    private final int d = 3;
    private final String f237d = ItemNode.NAME;
    private final String e = "type";
    private final String f = Icon.ELEM_NAME;
    private final String g = IMediaProfile.FEATURE_NORMAL;
    private final String h = "hiden";
    private final String i = "fixed";

    public static void m43a(String str) {
        j = str;
        a = com.gala.appmanager.utils.a.a(str);
    }

    public a(Context context) {
        this.f223a = context;
        this.f225a = new com.gala.appmanager.c.a(context);
        this.f224a = new com.gala.appmanager.b.a(context);
    }

    public void m54a(String[] strArr) {
        this.f230a = strArr;
    }

    public void m61b(String[] strArr) {
        this.f234b = strArr;
    }

    public void m53a(int i) {
        b.a("AppListManager", "setCountShowInHome(), count=" + i);
        if (i >= 3) {
            this.c = i;
        }
    }

    public synchronized void m52a() {
        if (this.f229a == null) {
            Log.d("AppListManager", "mApps is null");
            if (a) {
                f(j);
            }
            a("package.xml", this.f223a);
            b();
        } else {
            Log.d("AppListManager", "mApp is not null, size = " + this.f229a.size());
        }
    }

    public ArrayList<AppInfo> m49a() {
        Log.d("AppListManager", "getAllApps");
        a();
        if (this.f229a == null) {
            return null;
        }
        return new ArrayList(this.f229a);
    }

    public ArrayList<AppInfo> m50a(String... strArr) {
        long currentTimeMillis = System.currentTimeMillis();
        a();
        b.a("AppListManager", "getAllApps total time is " + (System.currentTimeMillis() - currentTimeMillis));
        List asList = a ? Arrays.asList(this.f236c) : Arrays.asList(strArr);
        int size = this.c > this.f229a.size() ? this.f229a.size() : this.c;
        ArrayList<AppInfo> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            AppInfo appInfo = (AppInfo) this.f229a.get(i);
            if (!asList.contains(appInfo.getAppPackageName())) {
                arrayList.add(appInfo);
            }
        }
        b.a("AppListManager", "getHomePageApps total time is " + (System.currentTimeMillis() - currentTimeMillis));
        int i2 = this.c - 3;
        if (arrayList.size() < i2) {
            return arrayList;
        }
        if (i2 < 0) {
            return new ArrayList();
        }
        return new ArrayList(arrayList.subList(0, i2));
    }

    public ArrayList<AppInfo> m59b(String... strArr) {
        if (a) {
            strArr = this.f236c;
        }
        Log.d("AppListManager", strArr.toString());
        if (strArr == null || strArr.length <= 0 || !a(strArr)) {
            a();
            c(strArr);
            if (this.f229a == null) {
                return null;
            }
            return new ArrayList(this.f229a);
        }
        Log.d("AppListManager", "init App");
        b();
        for (Object obj : strArr) {
            this.f231b.add(obj);
            this.f229a.remove((AppInfo) this.f228a.remove(obj));
            this.f226a.remove(obj);
        }
        if (this.f229a == null) {
            return null;
        }
        return new ArrayList(this.f229a);
    }

    public AppInfo m47a(int i) {
        if (a(i)) {
            return (AppInfo) this.f229a.get(i);
        }
        return null;
    }

    public AppInfo m48a(String str) {
        if (this.f226a.contains(str)) {
            return (AppInfo) this.f228a.get(str);
        }
        return null;
    }

    public void b(int i) {
        if (a(i)) {
            Log.d("AppListManager", "remove App");
            AppInfo appInfo = (AppInfo) this.f229a.get(i);
            this.f228a.remove(appInfo);
            this.f229a.remove(i);
            this.f226a.remove(i);
            if (appInfo != null) {
                d(appInfo.getAppPackageName());
            }
        }
    }

    public void m60b(String str) {
        b(a(str));
    }

    public AppInfo m58b(String str) {
        if (this.f229a == null) {
            return null;
        }
        ResolveInfo a = a(str);
        if (a == null) {
            return null;
        }
        AppInfo a2 = a(a);
        this.f228a.put(a2.getAppPackageName(), a2);
        this.f229a.add(a2);
        this.f226a.add(str);
        return a2;
    }

    public void a(int i, int i2) {
        if (a(i) && a(i2)) {
            String str = (String) this.f226a.remove(i);
            AppInfo appInfo = (AppInfo) this.f229a.remove(i);
            if (i2 == this.f229a.size()) {
                this.f226a.add(str);
                this.f229a.add(appInfo);
            } else {
                this.f226a.add(i2, str);
                this.f229a.add(i2, appInfo);
            }
            if (this.f224a.a(str)) {
                e(str);
            } else {
                c(str);
            }
        }
    }

    public boolean m55a(String str) {
        return a(str) != -1;
    }

    public AppInfo m62c(String str) {
        boolean z = false;
        ResolveInfo a = a(str);
        if (a == null) {
            return null;
        }
        AppInfo a2 = a(str);
        if (a2 != null) {
            a2.setUninstalled(false);
            a2.setAppName(a.activityInfo.applicationInfo.loadLabel(this.f223a.getPackageManager()).toString());
            a2.setAppClassName(a.activityInfo.name);
            if ((a.activityInfo.applicationInfo.flags & 128) != 0) {
                z = true;
            }
            a2.setUpdateSystem(z);
        }
        return a2;
    }

    public int m46a(String str) {
        if (this.f229a != null) {
            int size = this.f229a.size();
            for (int i = 0; i < size; i++) {
                if (((AppInfo) this.f229a.get(i)).getAppPackageName().equals(str)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int m45a() {
        if (this.f229a != null) {
            return this.f229a.size();
        }
        return 0;
    }

    public List<String> m51a() {
        return this.f233b;
    }

    public String[] m56a() {
        return this.f236c;
    }

    public static boolean a() {
        return a;
    }

    public int m57b() {
        return this.b;
    }

    private void b() {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = this.f223a.getPackageManager().queryIntentActivities(intent, 0);
        if (this.f229a != null) {
            this.f229a.clear();
        } else {
            this.f229a = new ArrayList();
        }
        List<String> arrayList = new ArrayList();
        List<AppInfo> a = this.f225a.a(a((List) queryIntentActivities));
        Iterator it = this.f226a.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            AppInfo a2 = a((List) queryIntentActivities, str);
            if (a2 == null) {
                a2 = b(a, str);
                if (a2 == null) {
                    arrayList.add(str);
                } else if (this.f229a.contains(a2)) {
                    b.a("AppListManager", a2.getAppPackageName() + " has been add!");
                } else {
                    this.f229a.add(a2);
                    this.f228a.put(str, a2);
                }
            } else if (this.f229a.contains(a2)) {
                b.a("AppListManager", a2.getAppPackageName() + " has been add!");
            } else {
                this.f229a.add(a2);
                this.f228a.put(str, a2);
            }
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (!(b(resolveInfo.activityInfo.packageName) || c(resolveInfo.activityInfo.packageName))) {
                this.f226a.add(resolveInfo.activityInfo.packageName);
                AppInfo a3 = a(resolveInfo);
                if (this.f229a.contains(a3)) {
                    b.a("AppListManager", a3.getAppPackageName() + " has been add!");
                } else {
                    Log.d("AppListManager", a3.getAppPackageName() + SOAP.DELIM + a3.getAppName());
                    this.f229a.add(a3);
                    this.f228a.put(resolveInfo.activityInfo.packageName, a3);
                }
            }
        }
        for (AppInfo appInfo : a) {
            if (!b(appInfo.getAppPackageName())) {
                this.f226a.add(appInfo.getAppPackageName());
                this.f229a.add(appInfo);
                this.f228a.put(appInfo.getAppPackageName(), appInfo);
                Log.d("AppListManager", "preinstall:" + appInfo.getAppName() + SOAP.DELIM + appInfo.getAppPackageName());
            }
        }
        for (String str2 : arrayList) {
            this.f226a.remove(str2);
            d(str2);
        }
    }

    private List<String> a(List<ResolveInfo> list) {
        List<String> arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : list) {
            arrayList.add(resolveInfo.activityInfo.packageName);
        }
        return arrayList;
    }

    private AppInfo a(ResolveInfo resolveInfo) {
        AppInfo appInfo = new AppInfo(resolveInfo);
        appInfo.setAppName(resolveInfo.activityInfo.applicationInfo.loadLabel(this.f223a.getPackageManager()).toString());
        appInfo.setAppIcon(resolveInfo.loadIcon(this.f223a.getPackageManager()));
        return appInfo;
    }

    private boolean b(String str) {
        return this.f223a.getApplicationInfo().packageName.equals(str);
    }

    private void c() {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = this.f223a.getPackageManager().queryIntentActivities(intent, 0);
        List arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            String str = resolveInfo.activityInfo.packageName;
            if (!str.equals(this.f223a.getApplicationInfo().packageName)) {
                arrayList.add(str);
            }
        }
        int length = this.f230a.length;
        for (int i = 0; i < length; i++) {
            Object obj = this.f230a[i];
            if (i < this.b) {
                this.f226a.add(i, obj);
            } else if (arrayList.contains(obj) && !this.f226a.contains(obj)) {
                this.f226a.add(obj);
                arrayList.remove(obj);
            }
        }
    }

    private AppInfo a(List<ResolveInfo> list, String str) {
        for (ResolveInfo resolveInfo : list) {
            if (resolveInfo.activityInfo.packageName.equals(str)) {
                list.remove(resolveInfo);
                return a(resolveInfo);
            }
        }
        return null;
    }

    private AppInfo b(List<AppInfo> list, String str) {
        for (AppInfo appInfo : list) {
            if (str != null && str.equals(appInfo.getAppPackageName())) {
                list.remove(appInfo);
                return appInfo;
            }
        }
        return null;
    }

    private void a(String str, Context context) {
        int i = 0;
        this.f226a = new SerializableList();
        this.f224a.a();
        this.f226a = this.f224a.a();
        c();
        for (Object obj : this.f230a) {
            if (!this.f226a.contains(obj)) {
                this.f226a.add(obj);
            }
        }
        if (!b(this.f234b)) {
            String[] strArr = this.f234b;
            int length = strArr.length;
            while (i < length) {
                this.f226a.remove(strArr[i]);
                i++;
            }
        }
    }

    private boolean a(int i) {
        if (this.f229a == null || i < 0 || i >= this.f229a.size()) {
            return false;
        }
        return true;
    }

    private boolean a(String... strArr) {
        int size;
        boolean z = false;
        List arrayList = new ArrayList();
        for (Object add : strArr) {
            arrayList.add(add);
        }
        a("package.xml", this.f223a);
        try {
            size = this.f231b.size() - 1;
            while (size >= 0) {
                boolean z2;
                String str = (String) this.f231b.get(size);
                if (arrayList.contains(str)) {
                    z2 = z;
                } else {
                    this.f226a.add(0, str);
                    z2 = true;
                }
                size--;
                z = z2;
            }
            return z;
        } catch (Exception e) {
            Log.d("AppListManager", e.getClass() + SOAP.DELIM + e.getMessage());
            return true;
        }
    }

    private void c(String... strArr) {
        for (Object obj : strArr) {
            this.f229a.remove((AppInfo) this.f228a.remove(obj));
            this.f226a.remove(obj);
        }
    }

    private ResolveInfo a(String str) {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        List queryIntentActivities = this.f223a.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
            return null;
        }
        return (ResolveInfo) queryIntentActivities.get(0);
    }

    private void c(String str) {
        this.f224a.a();
        this.f224a.a(str);
    }

    private void d(String str) {
        this.f224a.a();
        this.f224a.b(str);
    }

    private void e(String str) {
        this.f224a.a();
        this.f224a.c(str);
    }

    private boolean m44c(String str) {
        if (d.a(str) || b(this.f234b)) {
            return false;
        }
        for (Object equals : this.f234b) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private boolean b(String[] strArr) {
        return strArr == null || strArr.length == 0;
    }

    private void f(String str) {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        List arrayList3 = new ArrayList();
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new FileReader(str));
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                switch (eventType) {
                    case 2:
                        String attributeValue;
                        if (ItemNode.NAME.equals(newPullParser.getName())) {
                            attributeValue = newPullParser.getAttributeValue(null, "type");
                            if (!IMediaProfile.FEATURE_NORMAL.equalsIgnoreCase(attributeValue)) {
                                if (!"hiden".equalsIgnoreCase(attributeValue)) {
                                    if (!"fixed".equals(attributeValue)) {
                                        break;
                                    }
                                    attributeValue = newPullParser.getAttributeValue(null, Icon.ELEM_NAME);
                                    b.a("AppListManager", "icon path=" + attributeValue);
                                    this.f233b.add(attributeValue);
                                    attributeValue = newPullParser.nextText();
                                    arrayList3.add(attributeValue);
                                    b.a("AppListManager", "fixed package:" + attributeValue);
                                    break;
                                }
                                attributeValue = newPullParser.nextText();
                                arrayList2.add(attributeValue);
                                b.a("AppListManager", "hiden package:" + attributeValue);
                                break;
                            }
                            attributeValue = newPullParser.nextText();
                            arrayList.add(attributeValue);
                            b.a("AppListManager", "normal package:" + attributeValue);
                            break;
                        } else if ("topCount".equals(newPullParser.getName())) {
                            attributeValue = newPullParser.nextText();
                            b.b("AppListManager", "topCount=" + attributeValue);
                            try {
                                this.b = Integer.valueOf(attributeValue).intValue();
                                break;
                            } catch (Throwable e) {
                                b.a("AppListManager", "paseXml", e);
                                break;
                            }
                        } else {
                            continue;
                        }
                    default:
                        break;
                }
            }
        } catch (Throwable e2) {
            b.a("AppListManager", "XmlPullParserException", e2);
        } catch (Throwable e22) {
            b.a("AppListManager", str + " file not found", e22);
        } catch (Throwable e222) {
            b.a("AppListManager", "ioException", e222);
        }
        this.f230a = (String[]) arrayList.toArray(new String[arrayList.size()]);
        this.f234b = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
        this.f236c = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
        if (this.b > this.f230a.length) {
            this.b = this.f230a.length;
            b.b("AppListManager", "real topCount=" + this.b);
        }
        if (this.b > 5) {
            this.b = 5;
        } else if (this.b < 0) {
            this.b = 0;
        }
    }
}
