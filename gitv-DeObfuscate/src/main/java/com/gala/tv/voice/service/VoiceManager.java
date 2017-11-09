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
import com.gala.tv.voice.IVoiceEventCallback.Stub;
import com.gala.tv.voice.IVoiceService;
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
    private static VoiceManager f825a;
    private Activity f826a;
    private Context f827a;
    private DialogInterface f828a;
    private ServiceConnection f829a = new C02042(this);
    IVoiceEventCallback f830a = new C02031(this);
    private IVoiceService f831a;
    private VoiceDispatcher f832a;
    private boolean f833a = false;

    class C02031 extends Stub {
        private /* synthetic */ VoiceManager f823a;

        C02031(VoiceManager voiceManager) {
            this.f823a = voiceManager;
        }

        public boolean dispatchVoice(VoiceEvent voiceEvent) throws RemoteException {
            if (this.f823a.f831a) {
                return this.f823a.dispatchVoiceEventToActivity(voiceEvent);
            }
            return this.f823a.dispatchVoiceEvent(voiceEvent);
        }

        public Bundle getSupportedVoices() throws RemoteException {
            Bundle bundle = new Bundle();
            try {
                Collection supportedEventGroupsByActivity;
                Log.m525d("VoiceManager", "mCallback>>>getSupportedVoices--isPlayerProcess=" + this.f823a.f831a);
                if (this.f823a.f831a) {
                    supportedEventGroupsByActivity = this.f823a.getSupportedEventGroupsByActivity();
                } else {
                    supportedEventGroupsByActivity = this.f823a.getSupportedEventGroups();
                }
                if (supportedEventGroupsByActivity != null) {
                    bundle = VoiceUtils.createResultBundle(0, new ArrayList(supportedEventGroupsByActivity));
                } else {
                    bundle = VoiceUtils.createResultBundle(0);
                }
                Log.m525d("VoiceManager", "VoiceUtils.createResultBundle(code, temp) return  bundle = " + bundle);
                return bundle;
            } catch (Throwable e) {
                Log.m527e("VoiceManager", "mCallback>>>getSupportedVoices..Exception = " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    class C02042 implements ServiceConnection {
        private /* synthetic */ VoiceManager f824a;

        C02042(VoiceManager voiceManager) {
            this.f824a = voiceManager;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.f824a.f831a = IVoiceService.Stub.asInterface(iBinder);
            try {
                if (this.f824a.f831a != null) {
                    this.f824a.f831a.registerCallback(this.f824a.f830a);
                }
            } catch (RemoteException e) {
                Log.m527e("VoiceManager", "onServiceConnected.RemoteException. e = " + e.getMessage());
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            try {
                if (this.f824a.f831a != null) {
                    this.f824a.f831a.unregisterCallback(this.f824a.f830a);
                }
            } catch (RemoteException e) {
                Log.m527e("VoiceManager", "onServiceDisconnected.RemoteException. e = " + e.getMessage());
                e.printStackTrace();
            }
            this.f824a.f831a = null;
        }
    }

    public static synchronized VoiceManager instance() {
        VoiceManager voiceManager;
        synchronized (VoiceManager.class) {
            if (f825a == null) {
                f825a = new VoiceManager();
            }
            voiceManager = f825a;
        }
        return voiceManager;
    }

    private VoiceManager() {
    }

    public void initialize(Context context) {
        this.f827a = context;
        this.f832a = new VoiceDispatcher();
        this.f833a = VoiceUtils.getCurProcessName(context).contains(":player");
        this.f827a.bindService(new Intent(this.f827a, VoiceService.class), this.f829a, 1);
    }

    public synchronized void onDialogShow(DialogInterface dialogInterface) {
        Log.m525d("VoiceManager", "onDialogShow(" + dialogInterface + ")");
        this.f828a = dialogInterface;
    }

    public synchronized void onDialogDismiss(DialogInterface dialogInterface) {
        Log.m525d("VoiceManager", "onDialogDismiss(" + dialogInterface + ")");
        this.f828a = null;
    }

    public synchronized DialogInterface getCurrentDialog() {
        return this.f828a;
    }

    public synchronized void onActivityResume(Activity activity) {
        Log.m525d("VoiceManager", "onActivityResume(" + activity + ")");
        this.f826a = activity;
    }

    public synchronized void onActivityPause(Activity activity) {
        Log.m525d("VoiceManager", "onActivityPause(" + activity + ") mCurDialog=" + this.f828a);
        this.f826a = null;
    }

    public synchronized Activity getCurrentActivity() {
        return this.f826a;
    }

    public synchronized Context getContext() {
        return this.f827a;
    }

    public boolean addListener(int i, IVocal iVocal) {
        return this.f832a.addListener(i, iVocal);
    }

    public boolean addListener(IVocal iVocal) {
        return addListener(20000, iVocal);
    }

    public boolean removeListener(IVocal iVocal) {
        return this.f832a.removeListener(iVocal);
    }

    public void setSemanticTranslator(ISemanticTranslator iSemanticTranslator) {
        this.f832a.setSemanticTranslator(iSemanticTranslator);
    }

    public List<VoiceEventGroup> getSupportedEventGroups() {
        return this.f832a.getSupportedGroups();
    }

    public List<VoiceEventGroup> getSupportedEventGroupsByActivity() {
        return this.f832a.getSupportedGroupsByActivity();
    }

    public boolean dispatchVoiceEventToActivity(VoiceEvent voiceEvent) {
        return this.f832a.dispatchToActivity(voiceEvent);
    }

    public boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        return this.f832a.dispatchVoiceEvent(voiceEvent);
    }

    public synchronized Context getSmartContext() {
        Context context;
        if (this.f826a != null) {
            context = this.f826a;
        } else {
            context = this.f827a;
        }
        Log.m525d("VoiceManager", "getSmartContext() return " + context);
        return context;
    }
}
