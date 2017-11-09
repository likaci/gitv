package com.qiyi.tv.client.impl.a;

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

public class c {
    private static c a;
    private final Context f827a;
    private final ServiceConnection f828a = new ServiceConnection(this) {
        private /* synthetic */ c a;

        {
            this.a = r1;
        }

        public final void onServiceConnected(ComponentName name, IBinder service) {
            Object obj = null;
            Log.d("ClientHelper", "onServiceConnected(" + name + ")" + this.a.f829a);
            synchronized (this.a) {
                try {
                    service.linkToDeath(this.a.f829a, 0);
                    if (this.a.f829a.get() == 0) {
                        obj = 1;
                    }
                    this.a.f832a = Stub.asInterface(service);
                    this.a.d.set(2);
                } catch (Throwable e) {
                    Log.w("ClientHelper", "onServiceConnected() link death recipient error!", e);
                    this.a.d.set(0);
                    this.a.a(8);
                    return;
                }
            }
            Log.d("ClientHelper", "onServiceConnected() mAuthSuccess=" + this.a.f829a);
            this.a.d;
            if (obj != null) {
                this.a.d();
            } else {
                new Thread(this.a.f829a).start();
            }
        }

        public final void onServiceDisconnected(ComponentName name) {
            Object obj = null;
            Log.d("ClientHelper", "onServiceDisconnected(" + name + ")" + this.a.f829a);
            synchronized (this.a) {
                this.a.f832a = null;
                this.a.d.set(0);
                if (this.a.f829a.get() == 2) {
                    obj = 1;
                }
            }
            this.a.b;
            if (obj != null) {
                this.a.b();
            }
        }
    };
    private Handler f829a = new Handler(Looper.getMainLooper());
    private DeathRecipient f830a = new DeathRecipient(this) {
        private /* synthetic */ c a;

        {
            this.a = r1;
        }

        public final void binderDied() {
            Log.d("ClientHelper", "binderDied()" + this.a.f829a);
            this.a.d.set(0);
            this.a.a(8);
        }
    };
    private ConnectionListener f831a;
    private IQiyiService f832a;
    private Runnable f833a = new Runnable(this) {
        final /* synthetic */ c a;

        {
            this.a = r1;
        }

        public final void run() {
            boolean z = true;
            Log.d("ClientHelper", "mAuthRunanble.run() begin." + this.a.f829a);
            if (this.a.f829a.get() == 2) {
                int parseResultCode;
                try {
                    Bundle bundle = new Bundle();
                    ParamsHelper.setOperationTarget(bundle, TargetType.TARGET_AUTH);
                    ParamsHelper.setOperationType(bundle, 20000);
                    ParamsHelper.setOperationDataType(bundle, 30000);
                    ParamsHelper.setClientInfo(bundle, "2.0", Version.VERSION_CODE, this.a.d, this.a.b);
                    bundle = this.a.f829a.invoke(bundle);
                    q.a(ParamsHelper.parsePageMaxSize(bundle));
                    parseResultCode = ParamsHelper.parseResultCode(bundle);
                    this.a.f836a = parseResultCode == 0;
                } catch (Throwable e) {
                    Log.d("ClientHelper", "mAuthRunanble.run()", e);
                    this.a.f836a = false;
                    parseResultCode = Utils.parseErrorCode(e);
                }
                synchronized (this.a) {
                    if (this.a.d.get() != 2) {
                        z = false;
                    }
                }
                if (z) {
                    z = this.a.f829a;
                    this.a.f829a.post(new Runnable(this) {
                        private /* synthetic */ AnonymousClass1 f838a;

                        public final void run() {
                            if (z) {
                                this.f838a.a.f829a;
                            } else {
                                this.f838a.a.a(parseResultCode);
                            }
                        }
                    });
                    return;
                }
                Log.d("ClientHelper", "mAuthRunanble.run() code=" + parseResultCode + ", needNotify=" + z + ", mAuthSuccess=" + this.a.f829a);
            }
        }
    };
    private final String f834a;
    private final AtomicInteger f835a = new AtomicInteger(0);
    private boolean f836a;
    private final String b;
    private final AtomicInteger f837b = new AtomicInteger(0);
    private String c;
    private String d;

    static /* synthetic */ void a(c cVar) {
        Log.d("ClientHelper", "notifyAuthSuccess()" + cVar.a());
        if (cVar.f831a != null) {
            cVar.f831a.onAuthSuccess();
        }
    }

    static /* synthetic */ void c(c cVar) {
        Log.d("ClientHelper", "notifyDisconnected()" + cVar.a());
        if (cVar.f831a != null) {
            cVar.f831a.onDisconnected();
        }
    }

    public static synchronized void a(Context context, String str, String str2) {
        synchronized (c.class) {
            Log.d("ClientHelper", "initialize(" + context + ")");
            if (a == null) {
                a = new c(context, str, str2);
            } else {
                Log.w("ClientHelper", "Don't need to initlize it again.", new Throwable());
            }
        }
    }

    public static synchronized c a() {
        c cVar;
        synchronized (c.class) {
            if (a == null) {
                throw new NotInitializedException("Please call QiyiClient.initlized(Context) first.", null);
            }
            cVar = a;
        }
        return cVar;
    }

    public static synchronized void m213a() {
        synchronized (c.class) {
            Log.d("ClientHelper", "release()");
            if (a != null) {
                a.d();
                a = null;
            }
        }
    }

    private void e() {
        Log.d("ClientHelper", "notifyConnected()" + a());
        if (this.f831a != null) {
            this.f831a.onConnected();
        }
    }

    private void a(final int i) {
        Log.d("ClientHelper", "notifyError(" + i + ")" + a());
        this.f829a.post(new Runnable(this) {
            private /* synthetic */ c f840a;

            public final void run() {
                if (this.f840a.f829a != null) {
                    this.f840a.f829a.onError(i);
                }
            }
        });
    }

    private c(Context context, String str, String str2) {
        this.f827a = context;
        this.c = str2;
        this.d = this.f827a.getPackageName();
        this.f834a = str.substring(0, 16);
        this.b = str.substring(16);
    }

    private synchronized boolean c() {
        boolean z = true;
        synchronized (this) {
            Log.d("ClientHelper", "isConnecting()" + a());
            if (this.f835a.get() != 1) {
                z = false;
            }
        }
        return z;
    }

    public final synchronized boolean m214a() {
        Log.d("ClientHelper", "isConnected()" + a());
        return this.f835a.get() == 2;
    }

    private synchronized boolean d() {
        Log.d("ClientHelper", "isIdle()" + a());
        return this.f835a.get() == 0;
    }

    public final synchronized boolean m215b() {
        Log.d("ClientHelper", "isAuthSuccess()" + a());
        return this.f836a;
    }

    public final Bundle a(Bundle bundle) throws RemoteException {
        Bundle invoke;
        if (a()) {
            try {
                invoke = this.f832a.invoke(bundle);
            } catch (Exception e) {
                if (Utils.isServerDied(e)) {
                    this.f830a.binderDied();
                }
                throw e;
            }
        }
        invoke = new Bundle();
        invoke.putInt(Extras.EXTRA_RESULT_CODE, 3);
        if (invoke != null) {
            invoke.setClassLoader(c.class.getClassLoader());
        }
        return invoke;
    }

    public final void a(ConnectionListener connectionListener) {
        Log.d("ClientHelper", "setListener(" + connectionListener + ")" + a());
        this.f831a = connectionListener;
        if (a()) {
            e();
        }
    }

    public final synchronized void b() {
        Log.d("ClientHelper", "connect() begin." + a());
        if (d() || c()) {
            boolean bindService;
            this.f835a.set(1);
            try {
                bindService = this.f827a.bindService(ParamsHelper.getStartIntent(this.f827a, this.f834a, this.c), this.f828a, 1);
            } catch (Throwable e) {
                Log.w("ClientHelper", "connect() bind service error!", e);
                bindService = false;
            }
            if (!bindService) {
                this.f835a.set(0);
                a(3);
            }
        } else {
            a();
        }
        this.f837b.set(2);
        Log.d("ClientHelper", "connect() end." + a());
    }

    public final synchronized void m216c() {
        Log.d("ClientHelper", "authenticate() begin." + a());
        if (!(this.f835a.get() == 1 || this.f835a.get() == 0 || this.f836a)) {
            if (this.f835a.get() == 2) {
                new Thread(this.f833a).start();
            }
            Log.d("ClientHelper", "authenticate() end." + a());
        }
    }

    public final synchronized void m217d() {
        Log.d("ClientHelper", "disconnect() begin." + a());
        this.f836a = false;
        if (!d() && (c() || a())) {
            try {
                this.f832a.asBinder().unlinkToDeath(this.f830a, 0);
            } catch (Throwable e) {
                Log.w("ClientHelper", "disconnect() unlink death error!", e);
            }
            try {
                this.f827a.unbindService(this.f828a);
            } catch (Throwable e2) {
                Log.w("ClientHelper", "disconnect() unbind error!", e2);
            }
            this.f835a.set(0);
        }
        this.f837b.set(0);
        Log.d("ClientHelper", "disconnect() end." + a());
    }

    private String m212a() {
        return " ClientHelper@" + Integer.toHexString(hashCode()) + "{mCurrentState=" + this.f835a.get() + ", mTargetState=" + this.f837b.get() + ", mAuthSuccess=" + this.f836a + ", mListener=" + this.f831a + ", mService=" + this.f832a + "}";
    }
}
