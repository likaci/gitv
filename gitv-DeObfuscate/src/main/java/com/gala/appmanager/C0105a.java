package com.gala.appmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.p003b.C0106a;
import com.gala.appmanager.p004c.C0107a;
import com.gala.appmanager.utils.C0108a;
import com.gala.appmanager.utils.C0109b;
import com.gala.appmanager.utils.C0111d;
import com.gala.appmanager.utils.SerializableList;
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

class C0105a {
    private static boolean f331a = false;
    private static String f332j = "";
    private final int f333a = 5;
    private Context f334a;
    private C0106a f335a;
    private C0107a f336a;
    private SerializableList<String> f337a = new SerializableList();
    private final String f338a = "AppListManager";
    private HashMap<String, AppInfo> f339a = new HashMap();
    private List<AppInfo> f340a = null;
    private String[] f341a = new String[0];
    private int f342b = 0;
    private SerializableList<String> f343b = new SerializableList();
    private final String f344b = SOAP.DELIM;
    private List<String> f345b = new ArrayList();
    private String[] f346b = new String[0];
    private int f347c = 16;
    private final String f348c = "topCount";
    private String[] f349c = new String[0];
    private final int f350d = 3;
    private final String f351d = ItemNode.NAME;
    private final String f352e = "type";
    private final String f353f = Icon.ELEM_NAME;
    private final String f354g = IMediaProfile.FEATURE_NORMAL;
    private final String f355h = "hiden";
    private final String f356i = "fixed";

    public static void m187a(String str) {
        f332j = str;
        f331a = C0108a.m234a(str);
    }

    public C0105a(Context context) {
        this.f334a = context;
        this.f336a = new C0107a(context);
        this.f335a = new C0106a(context);
    }

    public void m213a(String[] strArr) {
        this.f341a = strArr;
    }

    public void m221b(String[] strArr) {
        this.f346b = strArr;
    }

    public void m211a(int i) {
        C0109b.m236a("AppListManager", "setCountShowInHome(), count=" + i);
        if (i >= 3) {
            this.f347c = i;
        }
    }

    public synchronized void m210a() {
        if (this.f340a == null) {
            Log.d("AppListManager", "mApps is null");
            if (f331a) {
                m202f(f332j);
            }
            m188a("package.xml", this.f334a);
            m193b();
        } else {
            Log.d("AppListManager", "mApp is not null, size = " + this.f340a.size());
        }
    }

    public ArrayList<AppInfo> m207a() {
        Log.d("AppListManager", "getAllApps");
        m189a();
        if (this.f340a == null) {
            return null;
        }
        return new ArrayList(this.f340a);
    }

    public ArrayList<AppInfo> m208a(String... strArr) {
        long currentTimeMillis = System.currentTimeMillis();
        m189a();
        C0109b.m236a("AppListManager", "getAllApps total time is " + (System.currentTimeMillis() - currentTimeMillis));
        List asList = f331a ? Arrays.asList(this.f349c) : Arrays.asList(strArr);
        int size = this.f347c > this.f340a.size() ? this.f340a.size() : this.f347c;
        ArrayList<AppInfo> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            AppInfo appInfo = (AppInfo) this.f340a.get(i);
            if (!asList.contains(appInfo.getAppPackageName())) {
                arrayList.add(appInfo);
            }
        }
        C0109b.m236a("AppListManager", "getHomePageApps total time is " + (System.currentTimeMillis() - currentTimeMillis));
        int i2 = this.f347c - 3;
        if (arrayList.size() < i2) {
            return arrayList;
        }
        if (i2 < 0) {
            return new ArrayList();
        }
        return new ArrayList(arrayList.subList(0, i2));
    }

    public ArrayList<AppInfo> m218b(String... strArr) {
        if (f331a) {
            strArr = this.f349c;
        }
        Log.d("AppListManager", strArr.toString());
        if (strArr == null || strArr.length <= 0 || !m191a(strArr)) {
            m189a();
            m198c(strArr);
            if (this.f340a == null) {
                return null;
            }
            return new ArrayList(this.f340a);
        }
        Log.d("AppListManager", "init App");
        m193b();
        for (Object obj : strArr) {
            this.f343b.add(obj);
            this.f340a.remove((AppInfo) this.f339a.remove(obj));
            this.f337a.remove(obj);
        }
        if (this.f340a == null) {
            return null;
        }
        return new ArrayList(this.f340a);
    }

    public AppInfo m205a(int i) {
        if (m190a(i)) {
            return (AppInfo) this.f340a.get(i);
        }
        return null;
    }

    public AppInfo m206a(String str) {
        if (this.f337a.contains(str)) {
            return (AppInfo) this.f339a.get(str);
        }
        return null;
    }

    public void m219b(int i) {
        if (m190a(i)) {
            Log.d("AppListManager", "remove App");
            AppInfo appInfo = (AppInfo) this.f340a.get(i);
            this.f339a.remove(appInfo);
            this.f340a.remove(i);
            this.f337a.remove(i);
            if (appInfo != null) {
                m200d(appInfo.getAppPackageName());
            }
        }
    }

    public void m220b(String str) {
        m219b(m183a(str));
    }

    public AppInfo m217b(String str) {
        if (this.f340a == null) {
            return null;
        }
        ResolveInfo a = m183a(str);
        if (a == null) {
            return null;
        }
        AppInfo a2 = m184a(a);
        this.f339a.put(a2.getAppPackageName(), a2);
        this.f340a.add(a2);
        this.f337a.add(str);
        return a2;
    }

    public void m212a(int i, int i2) {
        if (m190a(i) && m190a(i2)) {
            String str = (String) this.f337a.remove(i);
            AppInfo appInfo = (AppInfo) this.f340a.remove(i);
            if (i2 == this.f340a.size()) {
                this.f337a.add(str);
                this.f340a.add(appInfo);
            } else {
                this.f337a.add(i2, str);
                this.f340a.add(i2, appInfo);
            }
            if (this.f335a.m229a(str)) {
                m201e(str);
            } else {
                m197c(str);
            }
        }
    }

    public boolean m214a(String str) {
        return m183a(str) != -1;
    }

    public AppInfo m222c(String str) {
        boolean z = false;
        ResolveInfo a = m183a(str);
        if (a == null) {
            return null;
        }
        AppInfo a2 = m183a(str);
        if (a2 != null) {
            a2.setUninstalled(false);
            a2.setAppName(a.activityInfo.applicationInfo.loadLabel(this.f334a.getPackageManager()).toString());
            a2.setAppClassName(a.activityInfo.name);
            if ((a.activityInfo.applicationInfo.flags & 128) != 0) {
                z = true;
            }
            a2.setUpdateSystem(z);
        }
        return a2;
    }

    public int m204a(String str) {
        if (this.f340a != null) {
            int size = this.f340a.size();
            for (int i = 0; i < size; i++) {
                if (((AppInfo) this.f340a.get(i)).getAppPackageName().equals(str)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int m203a() {
        if (this.f340a != null) {
            return this.f340a.size();
        }
        return 0;
    }

    public List<String> m209a() {
        return this.f345b;
    }

    public String[] m215a() {
        return this.f349c;
    }

    public static boolean m189a() {
        return f331a;
    }

    public int m216b() {
        return this.f342b;
    }

    private void m193b() {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = this.f334a.getPackageManager().queryIntentActivities(intent, 0);
        if (this.f340a != null) {
            this.f340a.clear();
        } else {
            this.f340a = new ArrayList();
        }
        List<String> arrayList = new ArrayList();
        List<AppInfo> a = this.f336a.m233a(m186a((List) queryIntentActivities));
        Iterator it = this.f337a.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            AppInfo a2 = m185a((List) queryIntentActivities, str);
            if (a2 == null) {
                a2 = m192b(a, str);
                if (a2 == null) {
                    arrayList.add(str);
                } else if (this.f340a.contains(a2)) {
                    C0109b.m236a("AppListManager", a2.getAppPackageName() + " has been add!");
                } else {
                    this.f340a.add(a2);
                    this.f339a.put(str, a2);
                }
            } else if (this.f340a.contains(a2)) {
                C0109b.m236a("AppListManager", a2.getAppPackageName() + " has been add!");
            } else {
                this.f340a.add(a2);
                this.f339a.put(str, a2);
            }
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (!(m194b(resolveInfo.activityInfo.packageName) || m197c(resolveInfo.activityInfo.packageName))) {
                this.f337a.add(resolveInfo.activityInfo.packageName);
                AppInfo a3 = m184a(resolveInfo);
                if (this.f340a.contains(a3)) {
                    C0109b.m236a("AppListManager", a3.getAppPackageName() + " has been add!");
                } else {
                    Log.d("AppListManager", a3.getAppPackageName() + SOAP.DELIM + a3.getAppName());
                    this.f340a.add(a3);
                    this.f339a.put(resolveInfo.activityInfo.packageName, a3);
                }
            }
        }
        for (AppInfo appInfo : a) {
            if (!m194b(appInfo.getAppPackageName())) {
                this.f337a.add(appInfo.getAppPackageName());
                this.f340a.add(appInfo);
                this.f339a.put(appInfo.getAppPackageName(), appInfo);
                Log.d("AppListManager", "preinstall:" + appInfo.getAppName() + SOAP.DELIM + appInfo.getAppPackageName());
            }
        }
        for (String str2 : arrayList) {
            this.f337a.remove(str2);
            m200d(str2);
        }
    }

    private List<String> m186a(List<ResolveInfo> list) {
        List<String> arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : list) {
            arrayList.add(resolveInfo.activityInfo.packageName);
        }
        return arrayList;
    }

    private AppInfo m184a(ResolveInfo resolveInfo) {
        AppInfo appInfo = new AppInfo(resolveInfo);
        appInfo.setAppName(resolveInfo.activityInfo.applicationInfo.loadLabel(this.f334a.getPackageManager()).toString());
        appInfo.setAppIcon(resolveInfo.loadIcon(this.f334a.getPackageManager()));
        return appInfo;
    }

    private boolean m194b(String str) {
        return this.f334a.getApplicationInfo().packageName.equals(str);
    }

    private void m196c() {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> queryIntentActivities = this.f334a.getPackageManager().queryIntentActivities(intent, 0);
        List arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            String str = resolveInfo.activityInfo.packageName;
            if (!str.equals(this.f334a.getApplicationInfo().packageName)) {
                arrayList.add(str);
            }
        }
        int length = this.f341a.length;
        for (int i = 0; i < length; i++) {
            Object obj = this.f341a[i];
            if (i < this.f342b) {
                this.f337a.add(i, obj);
            } else if (arrayList.contains(obj) && !this.f337a.contains(obj)) {
                this.f337a.add(obj);
                arrayList.remove(obj);
            }
        }
    }

    private AppInfo m185a(List<ResolveInfo> list, String str) {
        for (ResolveInfo resolveInfo : list) {
            if (resolveInfo.activityInfo.packageName.equals(str)) {
                list.remove(resolveInfo);
                return m184a(resolveInfo);
            }
        }
        return null;
    }

    private AppInfo m192b(List<AppInfo> list, String str) {
        for (AppInfo appInfo : list) {
            if (str != null && str.equals(appInfo.getAppPackageName())) {
                list.remove(appInfo);
                return appInfo;
            }
        }
        return null;
    }

    private void m188a(String str, Context context) {
        int i = 0;
        this.f337a = new SerializableList();
        this.f335a.m225a();
        this.f337a = this.f335a.m225a();
        m196c();
        for (Object obj : this.f341a) {
            if (!this.f337a.contains(obj)) {
                this.f337a.add(obj);
            }
        }
        if (!m195b(this.f346b)) {
            String[] strArr = this.f346b;
            int length = strArr.length;
            while (i < length) {
                this.f337a.remove(strArr[i]);
                i++;
            }
        }
    }

    private boolean m190a(int i) {
        if (this.f340a == null || i < 0 || i >= this.f340a.size()) {
            return false;
        }
        return true;
    }

    private boolean m191a(String... strArr) {
        int size;
        boolean z = false;
        List arrayList = new ArrayList();
        for (Object add : strArr) {
            arrayList.add(add);
        }
        m188a("package.xml", this.f334a);
        try {
            size = this.f343b.size() - 1;
            while (size >= 0) {
                boolean z2;
                String str = (String) this.f343b.get(size);
                if (arrayList.contains(str)) {
                    z2 = z;
                } else {
                    this.f337a.add(0, str);
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

    private void m198c(String... strArr) {
        for (Object obj : strArr) {
            this.f340a.remove((AppInfo) this.f339a.remove(obj));
            this.f337a.remove(obj);
        }
    }

    private ResolveInfo m183a(String str) {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        List queryIntentActivities = this.f334a.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
            return null;
        }
        return (ResolveInfo) queryIntentActivities.get(0);
    }

    private void m197c(String str) {
        this.f335a.m225a();
        this.f335a.m229a(str);
    }

    private void m200d(String str) {
        this.f335a.m225a();
        this.f335a.m231b(str);
    }

    private void m201e(String str) {
        this.f335a.m225a();
        this.f335a.m232c(str);
    }

    private boolean m199c(String str) {
        if (C0111d.m242a(str) || m195b(this.f346b)) {
            return false;
        }
        for (Object equals : this.f346b) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private boolean m195b(String[] strArr) {
        return strArr == null || strArr.length == 0;
    }

    private void m202f(String str) {
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
                                    C0109b.m236a("AppListManager", "icon path=" + attributeValue);
                                    this.f345b.add(attributeValue);
                                    attributeValue = newPullParser.nextText();
                                    arrayList3.add(attributeValue);
                                    C0109b.m236a("AppListManager", "fixed package:" + attributeValue);
                                    break;
                                }
                                attributeValue = newPullParser.nextText();
                                arrayList2.add(attributeValue);
                                C0109b.m236a("AppListManager", "hiden package:" + attributeValue);
                                break;
                            }
                            attributeValue = newPullParser.nextText();
                            arrayList.add(attributeValue);
                            C0109b.m236a("AppListManager", "normal package:" + attributeValue);
                            break;
                        } else if ("topCount".equals(newPullParser.getName())) {
                            attributeValue = newPullParser.nextText();
                            C0109b.m238b("AppListManager", "topCount=" + attributeValue);
                            try {
                                this.f342b = Integer.valueOf(attributeValue).intValue();
                                break;
                            } catch (Throwable e) {
                                C0109b.m237a("AppListManager", "paseXml", e);
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
            C0109b.m237a("AppListManager", "XmlPullParserException", e2);
        } catch (Throwable e22) {
            C0109b.m237a("AppListManager", str + " file not found", e22);
        } catch (Throwable e222) {
            C0109b.m237a("AppListManager", "ioException", e222);
        }
        this.f341a = (String[]) arrayList.toArray(new String[arrayList.size()]);
        this.f346b = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
        this.f349c = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
        if (this.f342b > this.f341a.length) {
            this.f342b = this.f341a.length;
            C0109b.m238b("AppListManager", "real topCount=" + this.f342b);
        }
        if (this.f342b > 5) {
            this.f342b = 5;
        } else if (this.f342b < 0) {
            this.f342b = 0;
        }
    }
}
