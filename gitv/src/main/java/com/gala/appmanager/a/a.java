package com.gala.appmanager.a;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageDeleteObserver.Stub;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class a {
    public final int a = 2;
    private PackageManager f238a;
    private a f239a = new a(this);
    private b f240a = new b(this);
    private b f241a;
    private Method f242a;
    private Method b;

    class a extends Stub {
        final /* synthetic */ a a;

        a(a aVar) {
            this.a = aVar;
        }

        public void packageDeleted(String arg0, int arg1) throws RemoteException {
            if (this.a.f241a != null) {
                this.a.f241a.b(arg0, arg1);
            }
        }
    }

    class b extends IPackageInstallObserver.Stub {
        final /* synthetic */ a a;

        b(a aVar) {
            this.a = aVar;
        }

        public void packageInstalled(String arg0, int arg1) throws RemoteException {
            if (this.a.f241a != null) {
                this.a.f241a.a(arg0, arg1);
            }
        }
    }

    public a(Context context) {
        this.f238a = context.getPackageManager();
        Class[] clsArr = new Class[]{String.class, IPackageDeleteObserver.class, Integer.TYPE};
        try {
            this.f242a = this.f238a.getClass().getMethod("installPackage", new Class[]{Uri.class, IPackageInstallObserver.class, Integer.TYPE, String.class});
            this.b = this.f238a.getClass().getMethod("deletePackage", clsArr);
        } catch (NoSuchMethodException e) {
            Log.d("ApkManager", e.getMessage());
        }
    }

    public void a(Uri uri) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.f242a.invoke(this.f238a, new Object[]{uri, this.f240a, Integer.valueOf(2), null});
    }

    public void a(File file) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        a(Uri.fromFile(file));
    }

    public void a(String str) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        a(new File(str));
    }
}
