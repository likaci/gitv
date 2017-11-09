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
    private static VoiceClient f788a;
    private int f789a = 0;
    private final Context f790a;
    private final ServiceConnection f791a = new C01971(this);
    private DeathRecipient f792a = new C01982(this);
    private IVoiceService f793a;
    private ConnectionListener f794a;
    private String f795a;
    private int f796b = 0;

    class C01971 implements ServiceConnection {
        private /* synthetic */ VoiceClient f786a;

        C01971(VoiceClient voiceClient) {
            this.f786a = voiceClient;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Object obj;
            Log.m525d("VoiceClient", "onServiceConnected(" + componentName + ")" + this.f786a.f796b);
            synchronized (this.f786a) {
                try {
                    iBinder.linkToDeath(this.f786a.f796b, 0);
                } catch (Throwable e) {
                    Log.m530w("VoiceClient", "onServiceConnected() link death recipient error!", e);
                }
                if (this.f786a.f796b == 0) {
                    obj = 1;
                } else {
                    obj = null;
                }
                this.f786a.f793a = Stub.asInterface(iBinder);
                this.f786a.f789a = 2;
            }
            Log.m525d("VoiceClient", "onServiceConnected() mAuthSuccess=" + this.f786a.f796b);
            this.f786a.f796b;
            if (obj != null) {
                this.f786a.disconnect();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.m525d("VoiceClient", "onServiceDisconnected(" + componentName + ")" + this.f786a.f796b);
            synchronized (this.f786a) {
                this.f786a.f793a = null;
                this.f786a.f789a = 0;
                int i = this.f786a.f796b == 2 ? 1 : 0;
            }
            this.f786a.f789a = 0;
            if (i != 0) {
                this.f786a.connect();
            }
        }
    }

    class C01982 implements DeathRecipient {
        private /* synthetic */ VoiceClient f787a;

        C01982(VoiceClient voiceClient) {
            this.f787a = voiceClient;
        }

        public void binderDied() {
            Log.m525d("VoiceClient", "binderDied()" + this.f787a.f796b);
            this.f787a.f789a = 0;
            this.f787a.f789a = 3;
        }
    }

    public interface ConnectionListener {
        void onConnected();

        void onDisconnected(int i);
    }

    public static synchronized void initialize(Context context, String str) {
        synchronized (VoiceClient.class) {
            Log.m525d("VoiceClient", "initialize(" + context + ", targetPackageName=" + str + ")");
            if (f788a == null) {
                f788a = new VoiceClient(context.getApplicationContext(), str);
            } else {
                Log.m530w("VoiceClient", "Don't need to initlize it again.", new Throwable());
            }
        }
    }

    public static synchronized VoiceClient instance() {
        VoiceClient voiceClient;
        synchronized (VoiceClient.class) {
            if (f788a == null) {
                throw new RuntimeException("Please call VoiceClient.initlized(Context) first.", null);
            }
            voiceClient = f788a;
        }
        return voiceClient;
    }

    public static synchronized void release() {
        synchronized (VoiceClient.class) {
            Log.m525d("VoiceClient", "release()");
            if (f788a != null) {
                f788a.disconnect();
                f788a = null;
            }
        }
    }

    private void m519a() {
        Log.m525d("VoiceClient", "notifyConnected()" + m517a());
        if (this.f794a != null) {
            this.f794a.onConnected();
        }
    }

    private void m520a(int i) {
        Log.m525d("VoiceClient", "notifyDisconnected()" + m517a());
        if (this.f794a != null) {
            this.f794a.onDisconnected(i);
        }
    }

    private VoiceClient(Context context, String str) {
        this.f790a = context;
        this.f795a = str;
    }

    private synchronized boolean m523a() {
        boolean z = true;
        synchronized (this) {
            Log.m525d("VoiceClient", "isConnecting()" + m517a());
            if (this.f789a != 1) {
                z = false;
            }
        }
        return z;
    }

    public synchronized boolean isConnected() {
        Log.m525d("VoiceClient", "isConnected()" + m517a());
        return this.f789a == 2;
    }

    private synchronized boolean m524b() {
        Log.m525d("VoiceClient", "isIdle()" + m517a());
        return this.f789a == 0;
    }

    public void setListener(ConnectionListener connectionListener) {
        Log.m525d("VoiceClient", "setListener(" + connectionListener + ")" + m517a());
        this.f794a = connectionListener;
        if (isConnected()) {
            m517a();
        }
    }

    public final synchronized void connect() {
        Log.m525d("VoiceClient", "connect() begin." + m517a());
        if (m524b()) {
            boolean bindService;
            this.f789a = 1;
            try {
                bindService = this.f790a.bindService(ParamsHelper.getStartIntent(this.f790a, this.f795a, "1.0", Version.VERSION_CODE), this.f791a, 1);
            } catch (Throwable e) {
                Log.m530w("VoiceClient", "connect() bind service error!", e);
                bindService = false;
            }
            if (!bindService) {
                this.f789a = 0;
                m520a(2);
            }
        } else if (!m517a()) {
            isConnected();
        }
        this.f796b = 2;
        Log.m525d("VoiceClient", "connect() end." + m517a());
    }

    public final synchronized void disconnect() {
        Log.m525d("VoiceClient", "disconnect() begin." + m517a());
        if (!m524b() && (m517a() || isConnected())) {
            try {
                this.f793a.asBinder().unlinkToDeath(this.f792a, 0);
            } catch (Throwable e) {
                Log.m530w("VoiceClient", "disconnect() unlink death error!", e);
            }
            try {
                this.f790a.unbindService(this.f791a);
            } catch (Throwable e2) {
                Log.m530w("VoiceClient", "disconnect() unbind error!", e2);
            }
            this.f789a = 0;
        }
        this.f796b = 0;
        Log.m525d("VoiceClient", "disconnect() end." + m517a());
    }

    public synchronized boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        boolean booleanValue;
        Log.m525d("VoiceClient", "dispatchVoiceEvent(" + voiceEvent + ")");
        Bundle bundle = new Bundle();
        ParamsHelper.setOperationTarget(bundle, 10001);
        ParamsHelper.setOperationType(bundle, 20001);
        ParamsHelper.setResultData(bundle, (Parcelable) voiceEvent);
        Bundle bundle2 = null;
        try {
            bundle2 = m514a(bundle);
        } catch (Throwable e) {
            Log.m528e("VoiceClient", "dispatchVoiceEvent(" + voiceEvent + ")", e);
        }
        Boolean bool = (Boolean) ParamsHelper.parseResultData(bundle2);
        Log.m525d("VoiceClient", "dispatchVoiceEvent() return " + bool);
        if (bool == null) {
            booleanValue = Boolean.FALSE.booleanValue();
        } else {
            booleanValue = bool.booleanValue();
        }
        return booleanValue;
    }

    public List<VoiceEventGroup> getSupportedEvents() {
        Log.m525d("VoiceClient", "getSupportedEvents()");
        Bundle bundle = new Bundle();
        ParamsHelper.setOperationTarget(bundle, 10001);
        ParamsHelper.setOperationType(bundle, 20002);
        Bundle bundle2 = null;
        try {
            bundle2 = m514a(bundle);
        } catch (Throwable e) {
            Log.m528e("VoiceClient", "getSupportedEvents()", e);
        }
        ArrayList arrayList = (ArrayList) ParamsHelper.parseResultData(bundle2);
        Log.m525d("VoiceClient", "getSupportedEvents() return " + arrayList);
        return arrayList;
    }

    private Bundle m514a(Bundle bundle) throws RemoteException {
        Bundle invoke;
        if (isConnected()) {
            Log.m525d("VoiceClient", "params:" + bundle);
            if (bundle != null) {
                bundle.setClassLoader(getClass().getClassLoader());
                Log.m525d("VoiceClient", "params1:" + bundle + "bundle1:" + null);
            }
            invoke = this.f793a.invoke(bundle);
            Log.m525d("VoiceClient", "params:" + bundle + "bundle:" + invoke);
        } else {
            invoke = new Bundle();
            invoke.putInt(Extras.EXTRA_RESULT_CODE, 2);
        }
        if (invoke != null) {
            invoke.setClassLoader(VoiceClient.class.getClassLoader());
        }
        return invoke;
    }

    private String m517a() {
        return " VoiceClient@" + Integer.toHexString(hashCode()) + "{mCurrentState=" + this.f789a + ", mTargetState=" + this.f796b + ", mListener=" + this.f794a + ", mService=" + this.f793a + "}";
    }
}
