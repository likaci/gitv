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
import com.push.mqttv3.internal.ClientDefaults;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.cybergarage.soap.SOAP;

public class GalaAppManager extends Observable {
    private static int a = 0;
    private static LoadAppCallback f205a;
    private static GalaAppManager f206a = null;
    private static boolean f207a = false;
    private static int[] f208a = new int[2];
    private final BroadcastReceiver f209a = new BroadcastReceiver(this) {
        final /* synthetic */ GalaAppManager a;

        {
            this.a = r1;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String substring = intent.getDataString().substring("package:".length());
            Log.d("GalaAppManager", "action=" + action + ",pageckage=" + substring);
            int a;
            AppInfo c;
            if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                if (this.a.f211a.a(substring)) {
                    a = this.a.f211a.a(substring);
                    c = this.a.f211a.c(substring);
                    if (c != null) {
                        this.a.a(a, c, 4);
                        return;
                    }
                    return;
                }
                AppInfo b = this.a.f211a.b(substring);
                if (b != null) {
                    this.a.a(this.a.f211a.a() - 1, b, 0);
                }
            } else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                c = this.a.f211a.a(substring);
                if (c != null && c.isUpdateSystem()) {
                    a = this.a.f211a.a(substring);
                    c.setUpdateSystem(false);
                    this.a.a(a, c, 5);
                } else if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    this.a.a(-1, null, 2);
                } else {
                    a = this.a.f211a.a(substring);
                    this.a.f211a.b(substring);
                    this.a.a(a, null, 1);
                }
            }
        }
    };
    private Context f210a;
    private a f211a;
    private boolean b = false;

    public interface LoadAppCallback {
        void onLoadDone(List<AppInfo> list);
    }

    class a extends AsyncTask<Void, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager a;

        a(GalaAppManager galaAppManager) {
            this.a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((Void[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            a((List) obj);
        }

        protected List<AppInfo> a(Void... voidArr) {
            return new ArrayList(this.a.f211a.a());
        }

        protected void a(List<AppInfo> list) {
            this.a.a((List) list);
            if (GalaAppManager.f205a != null) {
                GalaAppManager.f205a.onLoadDone(list);
            }
        }
    }

    class b extends AsyncTask<String, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager a;

        b(GalaAppManager galaAppManager) {
            this.a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((String[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            a((List) obj);
        }

        protected List<AppInfo> a(String... strArr) {
            return new ArrayList(this.a.f211a.b(strArr));
        }

        protected void a(List<AppInfo> list) {
            this.a.a((List) list);
            if (GalaAppManager.f205a != null) {
                GalaAppManager.f205a.onLoadDone(list);
            }
        }
    }

    class c extends AsyncTask<String, Void, List<AppInfo>> {
        final /* synthetic */ GalaAppManager a;

        c(GalaAppManager galaAppManager) {
            this.a = galaAppManager;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return a((String[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            a((List) obj);
        }

        protected List<AppInfo> a(String... strArr) {
            return this.a.f211a.a(strArr);
        }

        protected void a(List<AppInfo> list) {
            this.a.a((List) list);
            if (GalaAppManager.f205a != null) {
                GalaAppManager.f205a.onLoadDone(list);
            }
        }
    }

    public static void setXmlPath(String xmlPath) {
        a.a(xmlPath);
    }

    public static boolean isReadFromXml() {
        return a.a();
    }

    @Deprecated
    public static synchronized GalaAppManager createAppManager(Context context, LoadAppCallback callback) {
        GalaAppManager galaAppManager;
        synchronized (GalaAppManager.class) {
            if (f206a != null) {
                f205a = callback;
                galaAppManager = f206a;
            } else {
                f206a = new GalaAppManager(context);
                f205a = callback;
                galaAppManager = f206a;
            }
        }
        return galaAppManager;
    }

    public static synchronized GalaAppManager getInstance(Context context) {
        GalaAppManager galaAppManager;
        synchronized (GalaAppManager.class) {
            if (f206a == null) {
                f206a = new GalaAppManager(context);
            }
            galaAppManager = f206a;
        }
        return galaAppManager;
    }

    public void setDefaultOrder(String[] order) {
        this.f211a.a(order);
    }

    public static void setIconSize(int widthPixel, int heightPixel) {
        f208a[0] = widthPixel;
        f208a[1] = heightPixel;
    }

    public static int[] getIconSize() {
        return f208a;
    }

    public void setNotShowPackages(String[] packages) {
        this.f211a.b(packages);
    }

    public void setCountShowInHome(int count) {
        this.f211a.a(count);
    }

    private GalaAppManager(Context context) {
        this.f211a = new a(context);
        this.f210a = context;
    }

    @Deprecated
    public void getAllAppsSync() {
        new a(this).execute(new Void[0]);
    }

    public void getAllApp(final LoadAppCallback callback) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f212a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(new ArrayList(this.f212a.f211a.a()));
                }
            }
        }).start();
    }

    @Deprecated
    public void getAllAppsSync(String... excludes) {
        new b(this).execute(excludes);
    }

    public void getAllApps(final LoadAppCallback callback, final String... excludes) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f213a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(new ArrayList(this.f213a.f211a.b(excludes)));
                }
            }
        }).start();
    }

    public List<AppInfo> getAllApps() {
        return new ArrayList(this.f211a.a());
    }

    public void getHomePageAppsSync(String... pkgNames) {
        new c(this).execute(pkgNames);
    }

    public void getHomePageApps(final LoadAppCallback callback, final String... pkgNames) {
        new Thread(new Runnable(this) {
            final /* synthetic */ GalaAppManager f215a;

            public void run() {
                if (callback != null) {
                    callback.onLoadDone(this.f215a.f211a.a(pkgNames));
                }
            }
        }).start();
    }

    public List<AppInfo> getHomePageApps(String... pkgNames) {
        return this.f211a.a(pkgNames);
    }

    public List<String> getLeftImagePath() {
        return this.f211a.a();
    }

    public String[] getLeftPackages() {
        return this.f211a.a();
    }

    public void startApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a != null) {
            a(a);
        }
    }

    public void startApp(String pkgName) {
        AppInfo a = this.f211a.a(pkgName);
        if (a != null) {
            a(a);
        }
    }

    private void a(AppInfo appInfo) {
        this.f210a.startActivity(this.f210a.getPackageManager().getLaunchIntentForPackage(appInfo.getAppPackageName()));
    }

    public void uninstallApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a != null) {
            b(a);
        }
    }

    public void uninstallApp(String pkgName) {
        AppInfo a = this.f211a.a(pkgName);
        if (a != null) {
            b(a);
        }
    }

    private void b(AppInfo appInfo) {
        String str = "android.intent.action.DELETE";
        Intent intent = new Intent(str, Uri.parse("package:" + appInfo.getAppPackageName()));
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        this.f210a.startActivity(intent);
    }

    public void stickApp(int position) {
        a(position, this.f211a.b());
        a(position, null, 3);
    }

    public void installApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a != null) {
            a(a, false);
        }
    }

    public void installApp(String pkgName) {
        AppInfo a = this.f211a.a(pkgName);
        if (a != null) {
            a(a, false);
        }
    }

    private void a(AppInfo appInfo, boolean z) {
        if (z) {
            try {
                new com.gala.appmanager.a.a(this.f210a).a(appInfo.getApkAbsolutePath());
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
        this.f210a.startActivity(intent);
    }

    public void installSilent(int position) {
        AppInfo a = this.f211a.a(position);
        if (a != null) {
            a(a, true);
        }
    }

    public void installSilent(String pkgName) {
        AppInfo a = this.f211a.a(pkgName);
        if (a != null) {
            a(a, true);
        }
    }

    public boolean contains(String pkgName) {
        return this.f211a.a(pkgName);
    }

    public synchronized void registerReceiver() {
        if (a == 0) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
            intentFilter.addDataScheme("package");
            this.f210a.registerReceiver(this.f209a, intentFilter);
            this.b = true;
        }
        a++;
        Log.d("GalaAppManager", a + " registerReceiver,mIsRegistered=" + this.b);
    }

    public synchronized void unregisterReceiver() {
        try {
            if (a == 1) {
                this.f210a.unregisterReceiver(this.f209a);
                this.b = false;
            }
        } catch (IllegalArgumentException e) {
            Log.i("GalaAppManager", e.toString());
        }
        if (a > 0) {
            a--;
        }
        Log.d("GalaAppManager", a + " unregisterReceiver,mIsRegistered=" + this.b);
    }

    private void a(int i, int i2) {
        this.f211a.a(i, i2);
    }

    public boolean isSystemApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a == null) {
            return true;
        }
        return a.getIsSystem();
    }

    public boolean isUninstalledApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a == null) {
            return true;
        }
        return a.isUninstalled();
    }

    public boolean isUninstallApp(String pkgName) {
        AppInfo a = this.f211a.a(pkgName);
        if (a == null) {
            return true;
        }
        return a.isUninstalled();
    }

    public boolean isUpdateSystemApp(int position) {
        AppInfo a = this.f211a.a(position);
        if (a == null) {
            return false;
        }
        return a.isUpdateSystem();
    }

    public int getTopCount() {
        return this.f211a.b();
    }

    public static void setApkType(boolean isLauncher) {
        f207a = isLauncher;
    }

    public static boolean isLauncher() {
        Log.d("GalaAppManager", "isLauncher=" + f207a);
        return f207a;
    }

    private void a(int i, AppInfo appInfo, int i2) {
        Log.d("GalaAppManager", "notifyObservers(),index=" + i + ",app=" + appInfo + ",type=" + i2);
        AppOperation appOperation = new AppOperation(i, i2);
        appOperation.setApp(appInfo);
        setChanged();
        notifyObservers(appOperation);
    }

    private void a(List<AppInfo> list) {
        if (list != null) {
            for (AppInfo appInfo : list) {
                Log.d("GalaAppManager", appInfo.getAppName() + SOAP.DELIM + appInfo.getAppPackageName());
            }
        }
    }
}
