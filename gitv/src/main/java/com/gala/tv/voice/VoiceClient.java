package com.gala.tv.voice;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Parcelable;
import android.os.RemoteException;
import com.gala.tv.voice.IVoiceService.Stub;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.Params.Extras;
import com.gala.tv.voice.core.ParamsHelper;
import java.util.ArrayList;
import java.util.List;

public class VoiceClient {
    public static final int ERROR_SERVER_DIED = 3;
    public static final int ERROR_SERVER_NOT_CONNECTED = 2;
    public static final int ERROR_UNKNOWN = 1;
    public static final int SUCCESS = 0;
    private static VoiceClient a;
    private int f400a = 0;
    private final Context f401a;
    private final ServiceConnection f402a = new ServiceConnection(this) {
        private /* synthetic */ VoiceClient a;

        {
            this.a = r1;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Object obj;
            Log.d("VoiceClient", "onServiceConnected(" + componentName + ")" + this.a.b);
            synchronized (this.a) {
                try {
                    iBinder.linkToDeath(this.a.b, 0);
                } catch (Throwable e) {
                    Log.w("VoiceClient", "onServiceConnected() link death recipient error!", e);
                }
                if (this.a.b == 0) {
                    obj = 1;
                } else {
                    obj = null;
                }
                this.a.f404a = Stub.asInterface(iBinder);
                this.a.f400a = 2;
            }
            Log.d("VoiceClient", "onServiceConnected() mAuthSuccess=" + this.a.b);
            this.a.b;
            if (obj != null) {
                this.a.disconnect();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("VoiceClient", "onServiceDisconnected(" + componentName + ")" + this.a.b);
            synchronized (this.a) {
                this.a.f404a = null;
                this.a.f400a = 0;
                int i = this.a.b == 2 ? 1 : 0;
            }
            this.a.f400a = 0;
            if (i != 0) {
                this.a.connect();
            }
        }
    };
    private DeathRecipient f403a = new DeathRecipient(this) {
        private /* synthetic */ VoiceClient a;

        {
            this.a = r1;
        }

        public void binderDied() {
            Log.d("VoiceClient", "binderDied()" + this.a.b);
            this.a.f400a = 0;
            this.a.f400a = 3;
        }
    };
    private IVoiceService f404a;
    private ConnectionListener f405a;
    private String f406a;
    private int b = 0;

    public interface ConnectionListener {
        void onConnected();

        void onDisconnected(int i);
    }

    public static synchronized void initialize(Context context, String str) {
        synchronized (VoiceClient.class) {
            Log.d("VoiceClient", "initialize(" + context + ", targetPackageName=" + str + ")");
            if (a == null) {
                a = new VoiceClient(context.getApplicationContext(), str);
            } else {
                Log.w("VoiceClient", "Don't need to initlize it again.", new Throwable());
            }
        }
    }

    public static synchronized VoiceClient instance() {
        VoiceClient voiceClient;
        synchronized (VoiceClient.class) {
            if (a == null) {
                throw new RuntimeException("Please call VoiceClient.initlized(Context) first.", null);
            }
            voiceClient = a;
        }
        return voiceClient;
    }

    public static synchronized void release() {
        synchronized (VoiceClient.class) {
            Log.d("VoiceClient", "release()");
            if (a != null) {
                a.disconnect();
                a = null;
            }
        }
    }

    private void m72a() {
        Log.d("VoiceClient", "notifyConnected()" + a());
        if (this.f405a != null) {
            this.f405a.onConnected();
        }
    }

    private void a(int i) {
        Log.d("VoiceClient", "notifyDisconnected()" + a());
        if (this.f405a != null) {
            this.f405a.onDisconnected(i);
        }
    }

    private VoiceClient(Context context, String str) {
        this.f401a = context;
        this.f406a = str;
    }

    private synchronized boolean m73a() {
        boolean z = true;
        synchronized (this) {
            Log.d("VoiceClient", "isConnecting()" + a());
            if (this.f400a != 1) {
                z = false;
            }
        }
        return z;
    }

    public synchronized boolean isConnected() {
        Log.d("VoiceClient", "isConnected()" + a());
        return this.f400a == 2;
    }

    private synchronized boolean b() {
        Log.d("VoiceClient", "isIdle()" + a());
        return this.f400a == 0;
    }

    public void setListener(ConnectionListener connectionListener) {
        Log.d("VoiceClient", "setListener(" + connectionListener + ")" + a());
        this.f405a = connectionListener;
        if (isConnected()) {
            a();
        }
    }

    public final synchronized void connect() {
        Log.d("VoiceClient", "connect() begin." + a());
        if (b()) {
            boolean bindService;
            this.f400a = 1;
            try {
                bindService = this.f401a.bindService(ParamsHelper.getStartIntent(this.f401a, this.f406a, "1.0", Version.VERSION_CODE), this.f402a, 1);
            } catch (Throwable e) {
                Log.w("VoiceClient", "connect() bind service error!", e);
                bindService = false;
            }
            if (!bindService) {
                this.f400a = 0;
                a(2);
            }
        } else if (!a()) {
            isConnected();
        }
        this.b = 2;
        Log.d("VoiceClient", "connect() end." + a());
    }

    public final synchronized void disconnect() {
        Log.d("VoiceClient", "disconnect() begin." + a());
        if (!b() && (a() || isConnected())) {
            try {
                this.f404a.asBinder().unlinkToDeath(this.f403a, 0);
            } catch (Throwable e) {
                Log.w("VoiceClient", "disconnect() unlink death error!", e);
            }
            try {
                this.f401a.unbindService(this.f402a);
            } catch (Throwable e2) {
                Log.w("VoiceClient", "disconnect() unbind error!", e2);
            }
            this.f400a = 0;
        }
        this.b = 0;
        Log.d("VoiceClient", "disconnect() end." + a());
    }

    public synchronized boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        boolean booleanValue;
        Log.d("VoiceClient", "dispatchVoiceEvent(" + voiceEvent + ")");
        Bundle bundle = new Bundle();
        ParamsHelper.setOperationTarget(bundle, 10001);
        ParamsHelper.setOperationType(bundle, 20001);
        ParamsHelper.setResultData(bundle, (Parcelable) voiceEvent);
        Bundle bundle2 = null;
        try {
            bundle2 = a(bundle);
        } catch (Throwable e) {
            Log.e("VoiceClient", "dispatchVoiceEvent(" + voiceEvent + ")", e);
        }
        Boolean bool = (Boolean) ParamsHelper.parseResultData(bundle2);
        Log.d("VoiceClient", "dispatchVoiceEvent() return " + bool);
        if (bool == null) {
            booleanValue = Boolean.FALSE.booleanValue();
        } else {
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    public List<VoiceEventGroup> getSupportedEvents() {
        Log.d("VoiceClient", "getSupportedEvents()");
        Bundle bundle = new Bundle();
        ParamsHelper.setOperationTarget(bundle, 10001);
        ParamsHelper.setOperationType(bundle, 20002);
        Bundle bundle2 = null;
        try {
            bundle2 = a(bundle);
        } catch (Throwable e) {
            Log.e("VoiceClient", "getSupportedEvents()", e);
        }
        ArrayList arrayList = (ArrayList) ParamsHelper.parseResultData(bundle2);
        Log.d("VoiceClient", "getSupportedEvents() return " + arrayList);
        return arrayList;
    }

    private Bundle a(Bundle bundle) throws RemoteException {
        Bundle invoke;
        if (isConnected()) {
            Log.d("VoiceClient", "params:" + bundle);
            if (bundle != null) {
                bundle.setClassLoader(getClass().getClassLoader());
                Log.d("VoiceClient", "params1:" + bundle + "bundle1:" + null);
            }
            invoke = this.f404a.invoke(bundle);
            Log.d("VoiceClient", "params:" + bundle + "bundle:" + invoke);
        } else {
            invoke = new Bundle();
            invoke.putInt(Extras.EXTRA_RESULT_CODE, 2);
        }
        if (invoke != null) {
            invoke.setClassLoader(VoiceClient.class.getClassLoader());
        }
        return invoke;
    }

    private String a() {
        return " VoiceClient@" + Integer.toHexString(hashCode()) + "{mCurrentState=" + this.f400a + ", mTargetState=" + this.b + ", mListener=" + this.f405a + ", mService=" + this.f404a + "}";
    }
}
