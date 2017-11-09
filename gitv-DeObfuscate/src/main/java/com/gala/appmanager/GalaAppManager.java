package com.gala.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.appinfo.AppOperation;
import com.gala.appmanager.p002a.C0103a;
import com.push.mqttv3.internal.ClientDefaults;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.cybergarage.soap.SOAP;

public class GalaAppManager extends Observable {
    private static int f313a = 0;
    private static LoadAppCallback f314a;
    private static GalaAppManager f315a = null;
    private static boolean f316a = false;
    private static int[] f317a = new int[2];
    private final BroadcastReceiver f318a = new C00974(this);
    private Context f319a;
    private C0105a f320a;
    private boolean f321b = false;

    class C00974 extends BroadcastReceiver {
        final /* synthetic */ GalaAppManager f309a;

        C00974(GalaAppManager galaAppManager) {
            this.f309a = galaAppManager;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String substring = intent.getDataString().substring("package:".length());
            Log.d("GalaAppManager", "action=" + action + ",pageckage=" + substring);
            int a;
            AppInfo c;
            if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                if (this.f309a.f320a.m183a(substring)) {
                    a = this.f309a.f320a.m183a(substring);
                    c = this.f309a.f320a.m197c(substring);
                    if (c != null) {
                        this.f309a.m170a(a, c, 4);
                        return;
                    }
                    return;
                }
                AppInfo b = this.f309a.f320a.m194b(substring);
                if (b != null) {
                    this.f309a.m170a(this.f309a.f320a.m189a() - 1, b, 0);
                }
            } else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                c = this.f309a.f320a.m183a(substring);
                if (c != null && c.isUpdateSystem()) {
                    a = this.f309a.f320a.m183a(substring);
                    c.setUpdateSystem(false);
                    this.f309a.m170a(a, c, 5);
                } else if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    this.f309a.m170a(-1, null, 2);
                } else {
                    a = this.f309a.f320a.m183a(substring);
                    this.f309a.f320a.m194b(substring);
                    this.f309a.m170a(a, null, 1);
                }
            }
        }
    }

    public interface LoadAppCallback {
        void onLoadDone(List<AppInfo> list);
    }

    class C0098a extends AsyncTask<Void, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager f310a;

        C0098a(GalaAppManager galaAppManager) {
            this.f310a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return m161a((Void[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            m162a((List) obj);
        }

        protected List<AppInfo> m161a(Void... voidArr) {
            return new ArrayList(this.f310a.f320a.m189a());
        }

        protected void m162a(List<AppInfo> list) {
            this.f310a.m175a((List) list);
            if (GalaAppManager.f314a != null) {
                GalaAppManager.f314a.onLoadDone(list);
            }
        }
    }

    class C0099b extends AsyncTask<String, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager f311a;

        C0099b(GalaAppManager galaAppManager) {
            this.f311a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return m163a((String[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            m164a((List) obj);
        }

        protected List<AppInfo> m163a(String... strArr) {
            return new ArrayList(this.f311a.f320a.m195b(strArr));
        }

        protected void m164a(List<AppInfo> list) {
            this.f311a.m175a((List) list);
            if (GalaAppManager.f314a != null) {
                GalaAppManager.f314a.onLoadDone(list);
            }
        }
    }

    class C0100c extends AsyncTask<String, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager f312a;

        C0100c(GalaAppManager galaAppManager) {
            this.f312a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return m165a((String[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            m166a((List) obj);
        }

        protected List<AppInfo> m165a(String... strArr) {
            return this.f312a.f320a.m191a(strArr);
        }

        protected void m166a(List<AppInfo> list) {
            this.f312a.m175a((List) list);
            if (GalaAppManager.f314a != null) {
                GalaAppManager.f314a.onLoadDone(list);
            }
        }
    }

    public static void setXmlPath(String xmlPath) {
        C0105a.m183a(xmlPath);
    }

    public static boolean isReadFromXml() {
        return C0105a.m189a();
    }

    @Deprecated
    public static synchronized GalaAppManager createAppManager(Context context, LoadAppCallback callback) {
        GalaAppManager galaAppManager;
        synchronized (GalaAppManager.class) {
            if (f315a != null) {
                f314a = callback;
                galaAppManager = f315a;
            } else {
                f315a = new GalaAppManager(context);
                f314a = callback;
                galaAppManager = f315a;
            }
        }
        return galaAppManager;
    }

    public static synchronized GalaAppManager getInstance(Context context) {
        GalaAppManager galaAppManager;
        synchronized (GalaAppManager.class) {
            if (f315a == null) {
                f315a = new GalaAppManager(context);
            }
            galaAppManager = f315a;
        }
        return galaAppManager;
    }

    public void setDefaultOrder(String[] order) {
        this.f320a.m191a(order);
    }

    public static void setIconSize(int widthPixel, int heightPixel) {
        f317a[0] = widthPixel;
        f317a[1] = heightPixel;
    }

    public static int[] getIconSize() {
        return f317a;
    }

    public void setNotShowPackages(String[] packages) {
        this.f320a.m195b(packages);
    }

    public void setCountShowInHome(int count) {
        this.f320a.m190a(count);
    }

    private GalaAppManager(Context context) {
        this.f320a = new C0105a(context);
        this.f319a = context;
    }

    @Deprecated
    public void getAllAppsSync() {
        new C0098a(this).execute(new Void[0]);
    }

    public void getAllApp(final LoadAppCallback callback) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f302a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(new ArrayList(this.f302a.f320a.m189a()));
                }
            }
        }).start();
    }

    @Deprecated
    public void getAllAppsSync(String... excludes) {
        new C0099b(this).execute(excludes);
    }

    public void getAllApps(final LoadAppCallback callback, final String... excludes) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f304a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(new ArrayList(this.f304a.f320a.m195b(excludes)));
                }
            }
        }).start();
    }

    public List<AppInfo> getAllApps() {
        return new ArrayList(this.f320a.m189a());
    }

    public void getHomePageAppsSync(String... pkgNames) {
        new C0100c(this).execute(pkgNames);
    }

    public void getHomePageApps(final LoadAppCallback callback, final String... pkgNames) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f307a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(this.f307a.f320a.m191a(pkgNames));
                }
            }
        }).start();
    }

    public List<AppInfo> getHomePageApps(String... pkgNames) {
        return this.f320a.m191a(pkgNames);
    }

    public List<String> getLeftImagePath() {
        return this.f320a.m189a();
    }

    public String[] getLeftPackages() {
        return this.f320a.m189a();
    }

    public void startApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a != null) {
            m173a(a);
        }
    }

    public void startApp(String pkgName) {
        AppInfo a = this.f320a.m183a(pkgName);
        if (a != null) {
            m173a(a);
        }
    }

    private void m173a(AppInfo appInfo) {
        this.f319a.startActivity(this.f319a.getPackageManager().getLaunchIntentForPackage(appInfo.getAppPackageName()));
    }

    public void uninstallApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a != null) {
            m176b(a);
        }
    }

    public void uninstallApp(String pkgName) {
        AppInfo a = this.f320a.m183a(pkgName);
        if (a != null) {
            m176b(a);
        }
    }

    private void m176b(AppInfo appInfo) {
        String str = "android.intent.action.DELETE";
        Intent intent = new Intent(str, Uri.parse("package:" + appInfo.getAppPackageName()));
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        this.f319a.startActivity(intent);
    }

    public void stickApp(int position) {
        m169a(position, this.f320a.m193b());
        m170a(position, null, 3);
    }

    public void installApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a != null) {
            m174a(a, false);
        }
    }

    public void installApp(String pkgName) {
        AppInfo a = this.f320a.m183a(pkgName);
        if (a != null) {
            m174a(a, false);
        }
    }

    private void m174a(AppInfo appInfo, boolean z) {
        if (z) {
            try {
                new C0103a(this.f319a).m180a(appInfo.getApkAbsolutePath());
                return;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                return;
            } catch (InvocationTargetException e3) {
                Log.e("GalaAppManager", e3.toString() + "");
                return;
            }
        }
        Uri fromFile = Uri.fromFile(new File(appInfo.getApkAbsolutePath()));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        this.f319a.startActivity(intent);
    }

    public void installSilent(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a != null) {
            m174a(a, true);
        }
    }

    public void installSilent(String pkgName) {
        AppInfo a = this.f320a.m183a(pkgName);
        if (a != null) {
            m174a(a, true);
        }
    }

    public boolean contains(String pkgName) {
        return this.f320a.m183a(pkgName);
    }

    public synchronized void registerReceiver() {
        if (f313a == 0) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
            intentFilter.addDataScheme("package");
            this.f319a.registerReceiver(this.f318a, intentFilter);
            this.f321b = true;
        }
        f313a++;
        Log.d("GalaAppManager", f313a + " registerReceiver,mIsRegistered=" + this.f321b);
    }

    public synchronized void unregisterReceiver() {
        try {
            if (f313a == 1) {
                this.f319a.unregisterReceiver(this.f318a);
                this.f321b = false;
            }
        } catch (IllegalArgumentException e) {
            Log.i("GalaAppManager", e.toString());
        }
        if (f313a > 0) {
            f313a--;
        }
        Log.d("GalaAppManager", f313a + " unregisterReceiver,mIsRegistered=" + this.f321b);
    }

    private void m169a(int i, int i2) {
        this.f320a.m212a(i, i2);
    }

    public boolean isSystemApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a == null) {
            return true;
        }
        return a.getIsSystem();
    }

    public boolean isUninstalledApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a == null) {
            return true;
        }
        return a.isUninstalled();
    }

    public boolean isUninstallApp(String pkgName) {
        AppInfo a = this.f320a.m183a(pkgName);
        if (a == null) {
            return true;
        }
        return a.isUninstalled();
    }

    public boolean isUpdateSystemApp(int position) {
        AppInfo a = this.f320a.m190a(position);
        if (a == null) {
            return false;
        }
        return a.isUpdateSystem();
    }

    public int getTopCount() {
        return this.f320a.m193b();
    }

    public static void setApkType(boolean isLauncher) {
        f316a = isLauncher;
    }

    public static boolean isLauncher() {
        Log.d("GalaAppManager", "isLauncher=" + f316a);
        return f316a;
    }

    private void m170a(int i, AppInfo appInfo, int i2) {
        Log.d("GalaAppManager", "notifyObservers(),index=" + i + ",app=" + appInfo + ",type=" + i2);
        AppOperation appOperation = new AppOperation(i, i2);
        appOperation.setApp(appInfo);
        setChanged();
        notifyObservers(appOperation);
    }

    private void m175a(List<AppInfo> list) {
        if (list != null) {
            for (AppInfo appInfo : list) {
                Log.d("GalaAppManager", appInfo.getAppName() + SOAP.DELIM + appInfo.getAppPackageName());
            }
        }
    }
}
