package com.gala.tv.voice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.gala.tv.voice.IVoiceEventCallback;
import com.gala.tv.voice.IVoiceService.Stub;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventGroup;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.ParamsHelper;
import com.gala.tv.voice.core.VoiceUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VoiceService extends Service {
    private RemoteCallbackList<IVoiceEventCallback> f835a = new RemoteCallbackList();
    private VoiceBinder f836a = new VoiceBinder(this);

    private static class VoiceBinder extends Stub {
        private WeakReference<VoiceService> f834a;

        public VoiceBinder(VoiceService voiceService) {
            this.f834a = new WeakReference(voiceService);
        }

        public Bundle invoke(Bundle bundle) throws RemoteException {
            int i = 0;
            Log.m525d("VoiceService", "invoke() begin.");
            Bundle createResultBundle = VoiceUtils.createResultBundle(1);
            if (bundle == null) {
                return createResultBundle;
            }
            bundle.setClassLoader(VoiceEvent.class.getClassLoader());
            VoiceUtils.dumpBundle("VoiceService", "invoke()", bundle);
            int parseOperationTarget = ParamsHelper.parseOperationTarget(bundle);
            int parseOperationType = ParamsHelper.parseOperationType(bundle);
            if (parseOperationTarget != 10001) {
                return createResultBundle;
            }
            switch (parseOperationType) {
                case 20001:
                    Boolean valueOf;
                    VoiceEvent voiceEvent = (VoiceEvent) ParamsHelper.parseResultData(bundle);
                    Boolean.valueOf(false);
                    try {
                        valueOf = Boolean.valueOf(((VoiceService) this.f834a.get()).m549a(voiceEvent));
                        parseOperationTarget = 0;
                    } catch (Exception e) {
                        Exception exception = e;
                        valueOf = Boolean.valueOf(false);
                        exception.printStackTrace();
                        parseOperationTarget = -1;
                    }
                    return VoiceUtils.createResultBundle(parseOperationTarget, valueOf.booleanValue());
                case 20002:
                    Collection a;
                    try {
                        a = ((VoiceService) this.f834a.get()).f835a;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        a = null;
                        i = -1;
                    }
                    if (a != null) {
                        return VoiceUtils.createResultBundle(i, new ArrayList(a));
                    }
                    return VoiceUtils.createResultBundle(i);
                default:
                    return createResultBundle;
            }
        }

        public void registerCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException {
            Log.m525d("VoiceService", "registerCallback(cb):cb = " + iVoiceEventCallback);
            ((VoiceService) this.f834a.get()).f835a.register(iVoiceEventCallback);
        }

        public void unregisterCallback(IVoiceEventCallback iVoiceEventCallback) throws RemoteException {
            Log.m525d("VoiceService", "unregisterCallback(cb):cb = " + iVoiceEventCallback);
            ((VoiceService) this.f834a.get()).f835a.unregister(iVoiceEventCallback);
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public IBinder onBind(Intent intent) {
        Log.m525d("VoiceService", "onBind(" + intent + ") mBinder = " + this.f836a);
        return this.f836a;
    }

    public synchronized boolean onUnbind(Intent intent) {
        Log.m525d("VoiceService", "onUnbind(" + intent + ")");
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean m549a(com.gala.tv.voice.VoiceEvent r7) {
        /*
        r6 = this;
        r1 = 0;
        r0 = r6.f835a;	 Catch:{ Exception -> 0x003a }
        r3 = r0.beginBroadcast();	 Catch:{ Exception -> 0x003a }
        r0 = "VoiceService";
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x003a }
        r4 = " mCallbackList.beginBroadcast().size =  ";
        r2.<init>(r4);	 Catch:{ Exception -> 0x003a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x003a }
        r2 = r2.toString();	 Catch:{ Exception -> 0x003a }
        com.gala.tv.voice.core.Log.m527e(r0, r2);	 Catch:{ Exception -> 0x003a }
        r2 = r1;
    L_0x001e:
        if (r2 >= r3) goto L_0x0033;
    L_0x0020:
        r0 = r6.f835a;	 Catch:{ Exception -> 0x003a }
        r0 = r0.getBroadcastItem(r2);	 Catch:{ Exception -> 0x003a }
        r0 = (com.gala.tv.voice.IVoiceEventCallback) r0;	 Catch:{ Exception -> 0x003a }
        r0 = r0.dispatchVoice(r7);	 Catch:{ Exception -> 0x003a }
        if (r0 != 0) goto L_0x0034;
    L_0x002e:
        r1 = r2 + 1;
        r2 = r1;
        r1 = r0;
        goto L_0x001e;
    L_0x0033:
        r0 = r1;
    L_0x0034:
        r1 = r6.f835a;
        r1.finishBroadcast();
    L_0x0039:
        return r0;
    L_0x003a:
        r0 = move-exception;
        r5 = r0;
        r0 = r1;
        r1 = r5;
        r2 = "VoiceService";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0061 }
        r4 = "dispathVoice Exception --e ";
        r3.<init>(r4);	 Catch:{ all -> 0x0061 }
        r4 = r1.getMessage();	 Catch:{ all -> 0x0061 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0061 }
        r3 = r3.toString();	 Catch:{ all -> 0x0061 }
        com.gala.tv.voice.core.Log.m527e(r2, r3);	 Catch:{ all -> 0x0061 }
        r1.printStackTrace();	 Catch:{ all -> 0x0061 }
        r1 = r6.f835a;
        r1.finishBroadcast();
        goto L_0x0039;
    L_0x0061:
        r0 = move-exception;
        r1 = r6.f835a;
        r1.finishBroadcast();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.tv.voice.service.VoiceService.a(com.gala.tv.voice.VoiceEvent):boolean");
    }

    private List<VoiceEventGroup> m547a() {
        List<VoiceEventGroup> arrayList = new ArrayList();
        try {
            int beginBroadcast = this.f835a.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                Bundle supportedVoices = ((IVoiceEventCallback) this.f835a.getBroadcastItem(i)).getSupportedVoices();
                supportedVoices.setClassLoader(VoiceEventGroup.class.getClassLoader());
                ArrayList arrayList2 = (ArrayList) ParamsHelper.parseResultData(supportedVoices);
                if (!(arrayList2 == null || arrayList2.size() == 0)) {
                    arrayList.addAll(arrayList2);
                }
            }
        } catch (Exception e) {
            Log.m527e("VoiceService", "getSupportVoiceEventGroups Exception --e " + e.getMessage());
            e.printStackTrace();
        } finally {
            this.f835a.finishBroadcast();
        }
        return arrayList;
    }
}
