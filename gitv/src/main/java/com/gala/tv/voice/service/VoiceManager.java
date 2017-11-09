package com.gala.tv.voice.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.gala.tv.voice.IVoiceEventCallback;
import com.gala.tv.voice.IVoiceService;
import com.gala.tv.voice.IVoiceService.Stub;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventGroup;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.VoiceUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VoiceManager {
    public static final int PRIORITY_CUSTOM = 20000;
    public static final int PRIORITY_HIGH = 10000;
    public static final int PRIORITY_LOW = 30000;
    public static final int PRIORITY_RESERVED = 40000;
    private static VoiceManager a;
    private Activity f420a;
    private Context f421a;
    private DialogInterface f422a;
    private ServiceConnection f423a = new ServiceConnection(this) {
        private /* synthetic */ VoiceManager a;

        {
            this.a = r1;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.a.f425a = Stub.asInterface(iBinder);
            try {
                if (this.a.f425a != null) {
                    this.a.f425a.registerCallback(this.a.f424a);
                }
            } catch (RemoteException e) {
                Log.e("VoiceManager", "onServiceConnected.RemoteException. e = " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            try {
                if (this.a.f425a != null) {
                    this.a.f425a.unregisterCallback(this.a.f424a);
                }
            } catch (RemoteException e) {
                Log.e("VoiceManager", "onServiceDisconnected.RemoteException. e = " + e.getMessage());
                e.printStackTrace();
            }
            this.a.f425a = null;
        }
    };
    IVoiceEventCallback f424a = new IVoiceEventCallback.Stub(this) {
        private /* synthetic */ VoiceManager a;

        {
            this.a = r1;
        }

        public boolean dispatchVoice(VoiceEvent voiceEvent) throws RemoteException {
            if (this.a.f425a) {
                return this.a.dispatchVoiceEventToActivity(voiceEvent);
            }
            return this.a.dispatchVoiceEvent(voiceEvent);
        }

        public Bundle getSupportedVoices() throws RemoteException {
            Bundle bundle = new Bundle();
            try {
                Collection supportedEventGroupsByActivity;
                Log.d("VoiceManager", "mCallback>>>getSupportedVoices--isPlayerProcess=" + this.a.f425a);
                if (this.a.f425a) {
                    supportedEventGroupsByActivity = this.a.getSupportedEventGroupsByActivity();
                } else {
                    supportedEventGroupsByActivity = this.a.getSupportedEventGroups();
                }
                if (supportedEventGroupsByActivity != null) {
                    bundle = VoiceUtils.createResultBundle(0, new ArrayList(supportedEventGroupsByActivity));
                } else {
                    bundle = VoiceUtils.createResultBundle(0);
                }
                Log.d("VoiceManager", "VoiceUtils.createResultBundle(code, temp) return  bundle = " + bundle);
                return bundle;
            } catch (Throwable e) {
                Log.e("VoiceManager", "mCallback>>>getSupportedVoices..Exception = " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    };
    private IVoiceService f425a;
    private VoiceDispatcher f426a;
    private boolean f427a = false;

    public static synchronized VoiceManager instance() {
        VoiceManager voiceManager;
        synchronized (VoiceManager.class) {
            if (a == null) {
                a = new VoiceManager();
            }
            voiceManager = a;
        }
        return voiceManager;
    }

    private VoiceManager() {
    }

    public void initialize(Context context) {
        this.f421a = context;
        this.f426a = new VoiceDispatcher();
        this.f427a = VoiceUtils.getCurProcessName(context).contains(":player");
        this.f421a.bindService(new Intent(this.f421a, VoiceService.class), this.f423a, 1);
    }

    public synchronized void onDialogShow(DialogInterface dialogInterface) {
        Log.d("VoiceManager", "onDialogShow(" + dialogInterface + ")");
        this.f422a = dialogInterface;
    }

    public synchronized void onDialogDismiss(DialogInterface dialogInterface) {
        Log.d("VoiceManager", "onDialogDismiss(" + dialogInterface + ")");
        this.f422a = null;
    }

    public synchronized DialogInterface getCurrentDialog() {
        return this.f422a;
    }

    public synchronized void onActivityResume(Activity activity) {
        Log.d("VoiceManager", "onActivityResume(" + activity + ")");
        this.f420a = activity;
    }

    public synchronized void onActivityPause(Activity activity) {
        Log.d("VoiceManager", "onActivityPause(" + activity + ") mCurDialog=" + this.f422a);
        this.f420a = null;
    }

    public synchronized Activity getCurrentActivity() {
        return this.f420a;
    }

    public synchronized Context getContext() {
        return this.f421a;
    }

    public boolean addListener(int i, IVocal iVocal) {
        return this.f426a.addListener(i, iVocal);
    }

    public boolean addListener(IVocal iVocal) {
        return addListener(20000, iVocal);
    }

    public boolean removeListener(IVocal iVocal) {
        return this.f426a.removeListener(iVocal);
    }

    public void setSemanticTranslator(ISemanticTranslator iSemanticTranslator) {
        this.f426a.setSemanticTranslator(iSemanticTranslator);
    }

    public List<VoiceEventGroup> getSupportedEventGroups() {
        return this.f426a.getSupportedGroups();
    }

    public List<VoiceEventGroup> getSupportedEventGroupsByActivity() {
        return this.f426a.getSupportedGroupsByActivity();
    }

    public boolean dispatchVoiceEventToActivity(VoiceEvent voiceEvent) {
        return this.f426a.dispatchToActivity(voiceEvent);
    }

    public boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        return this.f426a.dispatchVoiceEvent(voiceEvent);
    }

    public synchronized Context getSmartContext() {
        Context context;
        if (this.f420a != null) {
            context = this.f420a;
        } else {
            context = this.f421a;
        }
        Log.d("VoiceManager", "getSmartContext() return " + context);
        return context;
    }
}
