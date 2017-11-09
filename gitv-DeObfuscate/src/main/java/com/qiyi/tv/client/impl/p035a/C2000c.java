package com.qiyi.tv.client.impl.p035a;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import com.qiyi.tv.client.ConnectionListener;
import com.qiyi.tv.client.IQiyiService;
import com.qiyi.tv.client.IQiyiService.Stub;
import com.qiyi.tv.client.NotInitializedException;
import com.qiyi.tv.client.Version;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.concurrent.atomic.AtomicInteger;

public class C2000c {
    private static C2000c f2093a;
    private final Context f2094a;
    private final ServiceConnection f2095a = new C19972(this);
    private Handler f2096a = new Handler(Looper.getMainLooper());
    private DeathRecipient f2097a = new C19983(this);
    private ConnectionListener f2098a;
    private IQiyiService f2099a;
    private Runnable f2100a = new C19961(this);
    private final String f2101a;
    private final AtomicInteger f2102a = new AtomicInteger(0);
    private boolean f2103a;
    private final String f2104b;
    private final AtomicInteger f2105b = new AtomicInteger(0);
    private String f2106c;
    private String f2107d;

    class C19961 implements Runnable {
        final /* synthetic */ C2000c f2088a;

        C19961(C2000c c2000c) {
            this.f2088a = c2000c;
        }

        public final void run() {
            boolean z = true;
            Log.m1620d("ClientHelper", "mAuthRunanble.run() begin." + this.f2088a.f2096a);
            if (this.f2088a.f2096a.get() == 2) {
                int parseResultCode;
                try {
                    Bundle bundle = new Bundle();
                    ParamsHelper.setOperationTarget(bundle, TargetType.TARGET_AUTH);
                    ParamsHelper.setOperationType(bundle, 20000);
                    ParamsHelper.setOperationDataType(bundle, 30000);
                    ParamsHelper.setClientInfo(bundle, "2.0", Version.VERSION_CODE, this.f2088a.f2107d, this.f2088a.f2104b);
                    bundle = this.f2088a.f2096a.invoke(bundle);
                    C2017q.m1718a(ParamsHelper.parsePageMaxSize(bundle));
                    parseResultCode = ParamsHelper.parseResultCode(bundle);
                    this.f2088a.f2103a = parseResultCode == 0;
                } catch (Throwable e) {
                    Log.m1621d("ClientHelper", "mAuthRunanble.run()", e);
                    this.f2088a.f2103a = false;
                    parseResultCode = Utils.parseErrorCode(e);
                }
                synchronized (this.f2088a) {
                    if (this.f2088a.f2107d.get() != 2) {
                        z = false;
                    }
                }
                if (z) {
                    z = this.f2088a.f2096a;
                    this.f2088a.f2096a.post(new Runnable(this) {
                        private /* synthetic */ C19961 f2086a;

                        public final void run() {
                            if (z) {
                                this.f2086a.f2088a.f2096a;
                            } else {
                                this.f2086a.f2088a.m1649a(parseResultCode);
                            }
                        }
                    });
                    return;
                }
                Log.m1620d("ClientHelper", "mAuthRunanble.run() code=" + parseResultCode + ", needNotify=" + z + ", mAuthSuccess=" + this.f2088a.f2096a);
            }
        }
    }

    class C19972 implements ServiceConnection {
        private /* synthetic */ C2000c f2089a;

        C19972(C2000c c2000c) {
            this.f2089a = c2000c;
        }

        public final void onServiceConnected(ComponentName name, IBinder service) {
            Object obj = null;
            Log.m1620d("ClientHelper", "onServiceConnected(" + name + ")" + this.f2089a.f2096a);
            synchronized (this.f2089a) {
                try {
                    service.linkToDeath(this.f2089a.f2096a, 0);
                    if (this.f2089a.f2096a.get() == 0) {
                        obj = 1;
                    }
                    this.f2089a.f2099a = Stub.asInterface(service);
                    this.f2089a.f2107d.set(2);
                } catch (Throwable e) {
                    Log.m1625w("ClientHelper", "onServiceConnected() link death recipient error!", e);
                    this.f2089a.f2107d.set(0);
                    this.f2089a.m1649a(8);
                    return;
                }
            }
            Log.m1620d("ClientHelper", "onServiceConnected() mAuthSuccess=" + this.f2089a.f2096a);
            this.f2089a.f2107d;
            if (obj != null) {
                this.f2089a.m1661d();
            } else {
                new Thread(this.f2089a.f2096a).start();
            }
        }

        public final void onServiceDisconnected(ComponentName name) {
            Object obj = null;
            Log.m1620d("ClientHelper", "onServiceDisconnected(" + name + ")" + this.f2089a.f2096a);
            synchronized (this.f2089a) {
                this.f2089a.f2099a = null;
                this.f2089a.f2107d.set(0);
                if (this.f2089a.f2096a.get() == 2) {
                    obj = 1;
                }
            }
            this.f2089a.f2104b;
            if (obj != null) {
                this.f2089a.m1666b();
            }
        }
    }

    class C19983 implements DeathRecipient {
        private /* synthetic */ C2000c f2090a;

        C19983(C2000c c2000c) {
            this.f2090a = c2000c;
        }

        public final void binderDied() {
            Log.m1620d("ClientHelper", "binderDied()" + this.f2090a.f2096a);
            this.f2090a.f2107d.set(0);
            this.f2090a.m1649a(8);
        }
    }

    static /* synthetic */ void m1651a(C2000c c2000c) {
        Log.m1620d("ClientHelper", "notifyAuthSuccess()" + c2000c.m1643a());
        if (c2000c.f2098a != null) {
            c2000c.f2098a.onAuthSuccess();
        }
    }

    static /* synthetic */ void m1659c(C2000c c2000c) {
        Log.m1620d("ClientHelper", "notifyDisconnected()" + c2000c.m1643a());
        if (c2000c.f2098a != null) {
            c2000c.f2098a.onDisconnected();
        }
    }

    public static synchronized void m1650a(Context context, String str, String str2) {
        synchronized (C2000c.class) {
            Log.m1620d("ClientHelper", "initialize(" + context + ")");
            if (f2093a == null) {
                f2093a = new C2000c(context, str, str2);
            } else {
                Log.m1625w("ClientHelper", "Don't need to initlize it again.", new Throwable());
            }
        }
    }

    public static synchronized C2000c m1643a() {
        C2000c c2000c;
        synchronized (C2000c.class) {
            if (f2093a == null) {
                throw new NotInitializedException("Please call QiyiClient.initlized(Context) first.", null);
            }
            c2000c = f2093a;
        }
        return c2000c;
    }

    public static synchronized void m1648a() {
        synchronized (C2000c.class) {
            Log.m1620d("ClientHelper", "release()");
            if (f2093a != null) {
                f2093a.m1661d();
                f2093a = null;
            }
        }
    }

    private void m1662e() {
        Log.m1620d("ClientHelper", "notifyConnected()" + m1643a());
        if (this.f2098a != null) {
            this.f2098a.onConnected();
        }
    }

    private void m1649a(final int i) {
        Log.m1620d("ClientHelper", "notifyError(" + i + ")" + m1643a());
        this.f2096a.post(new Runnable(this) {
            private /* synthetic */ C2000c f2092a;

            public final void run() {
                if (this.f2092a.f2096a != null) {
                    this.f2092a.f2096a.onError(i);
                }
            }
        });
    }

    private C2000c(Context context, String str, String str2) {
        this.f2094a = context;
        this.f2106c = str2;
        this.f2107d = this.f2094a.getPackageName();
        this.f2101a = str.substring(0, 16);
        this.f2104b = str.substring(16);
    }

    private synchronized boolean m1660c() {
        boolean z = true;
        synchronized (this) {
            Log.m1620d("ClientHelper", "isConnecting()" + m1643a());
            if (this.f2102a.get() != 1) {
                z = false;
            }
        }
        return z;
    }

    public final synchronized boolean m1665a() {
        Log.m1620d("ClientHelper", "isConnected()" + m1643a());
        return this.f2102a.get() == 2;
    }

    private synchronized boolean m1661d() {
        Log.m1620d("ClientHelper", "isIdle()" + m1643a());
        return this.f2102a.get() == 0;
    }

    public final synchronized boolean m1667b() {
        Log.m1620d("ClientHelper", "isAuthSuccess()" + m1643a());
        return this.f2103a;
    }

    public final Bundle m1663a(Bundle bundle) throws RemoteException {
        Bundle invoke;
        if (m1643a()) {
            try {
                invoke = this.f2099a.invoke(bundle);
            } catch (Exception e) {
                if (Utils.isServerDied(e)) {
                    this.f2097a.binderDied();
                }
                throw e;
            }
        }
        invoke = new Bundle();
        invoke.putInt(Extras.EXTRA_RESULT_CODE, 3);
        if (invoke != null) {
            invoke.setClassLoader(C2000c.class.getClassLoader());
        }
        return invoke;
    }

    public final void m1664a(ConnectionListener connectionListener) {
        Log.m1620d("ClientHelper", "setListener(" + connectionListener + ")" + m1643a());
        this.f2098a = connectionListener;
        if (m1643a()) {
            m1662e();
        }
    }

    public final synchronized void m1666b() {
        Log.m1620d("ClientHelper", "connect() begin." + m1643a());
        if (m1661d() || m1660c()) {
            boolean bindService;
            this.f2102a.set(1);
            try {
                bindService = this.f2094a.bindService(ParamsHelper.getStartIntent(this.f2094a, this.f2101a, this.f2106c), this.f2095a, 1);
            } catch (Throwable e) {
                Log.m1625w("ClientHelper", "connect() bind service error!", e);
                bindService = false;
            }
            if (!bindService) {
                this.f2102a.set(0);
                m1649a(3);
            }
        } else {
            m1643a();
        }
        this.f2105b.set(2);
        Log.m1620d("ClientHelper", "connect() end." + m1643a());
    }

    public final synchronized void m1668c() {
        Log.m1620d("ClientHelper", "authenticate() begin." + m1643a());
        if (!(this.f2102a.get() == 1 || this.f2102a.get() == 0 || this.f2103a)) {
            if (this.f2102a.get() == 2) {
                new Thread(this.f2100a).start();
            }
            Log.m1620d("ClientHelper", "authenticate() end." + m1643a());
        }
    }

    public final synchronized void m1669d() {
        Log.m1620d("ClientHelper", "disconnect() begin." + m1643a());
        this.f2103a = false;
        if (!m1661d() && (m1660c() || m1643a())) {
            try {
                this.f2099a.asBinder().unlinkToDeath(this.f2097a, 0);
            } catch (Throwable e) {
                Log.m1625w("ClientHelper", "disconnect() unlink death error!", e);
            }
            try {
                this.f2094a.unbindService(this.f2095a);
            } catch (Throwable e2) {
                Log.m1625w("ClientHelper", "disconnect() unbind error!", e2);
            }
            this.f2102a.set(0);
        }
        this.f2105b.set(0);
        Log.m1620d("ClientHelper", "disconnect() end." + m1643a());
    }

    private String m1645a() {
        return " ClientHelper@" + Integer.toHexString(hashCode()) + "{mCurrentState=" + this.f2102a.get() + ", mTargetState=" + this.f2105b.get() + ", mAuthSuccess=" + this.f2103a + ", mListener=" + this.f2098a + ", mService=" + this.f2099a + "}";
    }
}
