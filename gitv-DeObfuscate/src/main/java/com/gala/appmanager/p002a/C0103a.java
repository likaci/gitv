package com.gala.appmanager.p002a;

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

public class C0103a {
    public final int f324a = 2;
    private PackageManager f325a;
    private C0101a f326a = new C0101a(this);
    private C0102b f327a = new C0102b(this);
    private C0104b f328a;
    private Method f329a;
    private Method f330b;

    class C0101a extends Stub {
        final /* synthetic */ C0103a f322a;

        C0101a(C0103a c0103a) {
            this.f322a = c0103a;
        }

        public void packageDeleted(String arg0, int arg1) throws RemoteException {
            if (this.f322a.f328a != null) {
                this.f322a.f328a.m182b(arg0, arg1);
            }
        }
    }

    class C0102b extends IPackageInstallObserver.Stub {
        final /* synthetic */ C0103a f323a;

        C0102b(C0103a c0103a) {
            this.f323a = c0103a;
        }

        public void packageInstalled(String arg0, int arg1) throws RemoteException {
            if (this.f323a.f328a != null) {
                this.f323a.f328a.m181a(arg0, arg1);
            }
        }
    }

    public C0103a(Context context) {
        this.f325a = context.getPackageManager();
        Class[] clsArr = new Class[]{String.class, IPackageDeleteObserver.class, Integer.TYPE};
        try {
            this.f329a = this.f325a.getClass().getMethod("installPackage", new Class[]{Uri.class, IPackageInstallObserver.class, Integer.TYPE, String.class});
            this.f330b = this.f325a.getClass().getMethod("deletePackage", clsArr);
        } catch (NoSuchMethodException e) {
            Log.d("ApkManager", e.getMessage());
        }
    }

    public void m178a(Uri uri) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.f329a.invoke(this.f325a, new Object[]{uri, this.f327a, Integer.valueOf(2), null});
    }

    public void m179a(File file) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        m178a(Uri.fromFile(file));
    }

    public void m180a(String str) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        m179a(new File(str));
    }
}
