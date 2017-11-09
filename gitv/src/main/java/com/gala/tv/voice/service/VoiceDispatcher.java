package com.gala.tv.voice.service;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.VoiceEventGroup;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.VoiceUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

class VoiceDispatcher {
    private static Comparator<Holder> a = new Comparator<Holder>() {
        public final int compare(Holder holder, Holder holder2) {
            if (holder.a < holder2.a) {
                return -1;
            }
            if (holder.a > holder2.a) {
                return 1;
            }
            return 0;
        }
    };
    private ISemanticTranslator f417a;
    private final PriorityQueue<Holder> f418a = new PriorityQueue(10, a);

    private class Holder {
        int a;
        IVocal f419a;

        public Holder(VoiceDispatcher voiceDispatcher, int i, IVocal iVocal) {
            this.a = i;
            this.f419a = iVocal;
        }

        public boolean equals(Object obj) {
            if (obj == null || this == obj) {
                return false;
            }
            if (obj instanceof Holder) {
                if (this.f419a != ((Holder) obj).f419a) {
                    return false;
                }
                return true;
            } else if (!(obj instanceof IVocal)) {
                return false;
            } else {
                if (this.f419a != obj) {
                    return false;
                }
                return true;
            }
        }

        public String toString() {
            return "Holder(priority=" + this.a + ", listener=" + this.f419a + ")";
        }
    }

    VoiceDispatcher() {
    }

    private boolean m78a(IVocal iVocal) {
        boolean z;
        synchronized (this.f418a) {
            Iterator it = this.f418a.iterator();
            while (it.hasNext()) {
                if (((Holder) it.next()).f419a == iVocal) {
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }

    private void a(IVocal iVocal) {
        synchronized (this.f418a) {
            Iterator it = this.f418a.iterator();
            while (it.hasNext()) {
                if (((Holder) it.next()).f419a == iVocal) {
                    it.remove();
                }
            }
        }
    }

    private static boolean a(VoiceEvent voiceEvent, IVocal iVocal) {
        boolean dispatchVoiceEvent;
        Log.d("VoiceDispatcher", "dispatchToListener() listener=" + iVocal.getClass().getName());
        List<AbsVoiceAction> supportedVoices = iVocal.getSupportedVoices();
        if (!(supportedVoices == null || supportedVoices.isEmpty())) {
            for (AbsVoiceAction absVoiceAction : supportedVoices) {
                Log.d("VoiceDispatcher", "dispatchToListener() holder=" + absVoiceAction);
                if (absVoiceAction.accept(voiceEvent)) {
                    break;
                }
            }
        }
        AbsVoiceAction absVoiceAction2 = null;
        if (absVoiceAction2 != null) {
            dispatchVoiceEvent = absVoiceAction2.dispatchVoiceEvent(voiceEvent);
        } else {
            dispatchVoiceEvent = false;
        }
        Log.d("VoiceDispatcher", "dispatchToListener() return " + dispatchVoiceEvent);
        return dispatchVoiceEvent;
    }

    private boolean a(int i, VoiceEvent voiceEvent, IVocal iVocal) {
        boolean z;
        synchronized (this.f418a) {
            Iterator it = this.f418a.iterator();
            while (it.hasNext()) {
                Holder holder = (Holder) it.next();
                Log.d("VoiceDispatcher", "dispatch() find  holder.priority = " + holder.a + ", priority = " + i);
                if (iVocal != holder.f419a && holder.a >= i && holder.a < i + 10000 && a(voiceEvent, holder.f419a)) {
                    Log.d("VoiceDispatcher", "dispatch() find listener " + holder.f419a);
                    z = true;
                    break;
                }
            }
            z = false;
        }
        Log.d("VoiceDispatcher", "dispatch(" + i + ", " + voiceEvent + ") return " + z + ", listeners=" + this.f418a);
        return z;
    }

    public boolean addListener(int i, IVocal iVocal) {
        boolean z = false;
        if (iVocal != null) {
            synchronized (this.f418a) {
                if (!a(iVocal)) {
                    z = this.f418a.add(new Holder(this, i, iVocal));
                }
            }
        }
        Log.d("VoiceDispatcher", "addListener(" + i + ", " + iVocal + ") return " + z);
        return z;
    }

    public boolean removeListener(IVocal iVocal) {
        Log.d("VoiceDispatcher", "removeListener(" + iVocal + ")");
        a(iVocal);
        return true;
    }

    public void setSemanticTranslator(ISemanticTranslator iSemanticTranslator) {
        Log.d("VoiceDispatcher", "setSemanticTranslator(" + iSemanticTranslator + ")");
        this.f417a = iSemanticTranslator;
    }

    public List<VoiceEventGroup> getSupportedGroups() {
        List arrayList = new ArrayList();
        Log.d("VoiceDispatcher", "getSupportedGroups() listeners.size() = " + this.f418a.size());
        IVocal iVocal = null;
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
            a(arrayList, iVocal.getSupportedVoices());
        }
        if (!(iVocal instanceof IUnVocal)) {
            a(arrayList, a());
        }
        Iterator it = this.f418a.iterator();
        while (it.hasNext()) {
            a(arrayList, ((Holder) it.next()).f419a.getSupportedVoices());
        }
        Log.d("VoiceDispatcher", "getSupportedGroups() return " + arrayList.size());
        return arrayList;
    }

    private static void a(List<VoiceEventGroup> list, List<AbsVoiceAction> list2) {
        if (list != null && list2 != null && !list2.isEmpty()) {
            List arrayList = new ArrayList();
            for (AbsVoiceAction absVoiceAction : list2) {
                if (!(absVoiceAction == null || absVoiceAction.getSupportedEvent() == null || absVoiceAction.getSupportedEvent().getType() == -1 || absVoiceAction.getSupportedEvent().getType() == 0)) {
                    arrayList.add(absVoiceAction.getSupportedEvent());
                }
            }
            if (!arrayList.isEmpty()) {
                list.add(new VoiceEventGroup(arrayList));
            }
        }
    }

    public List<VoiceEventGroup> getSupportedGroupsByActivity() {
        List arrayList = new ArrayList();
        Log.d("VoiceDispatcher", "getSupportedGroups() listeners.size() = " + this.f418a.size());
        IVocal iVocal = null;
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
            a(arrayList, iVocal.getSupportedVoices());
        }
        if (!(iVocal instanceof IUnVocal)) {
            a(arrayList, a());
        }
        Log.d("VoiceDispatcher", "getSupportedGroups() return " + arrayList.size());
        return arrayList;
    }

    private static List<AbsVoiceAction> a() {
        Map scanView;
        Log.d("VoiceDispatcher", "getSupportedByActivity()!!");
        List<AbsVoiceAction> arrayList = new ArrayList();
        DialogInterface currentDialog = VoiceManager.instance().getCurrentDialog();
        if (currentDialog != null) {
            scanView = VoiceViewHelper.scanView(currentDialog);
        } else {
            scanView = VoiceViewHelper.scanView(VoiceManager.instance().getCurrentActivity());
        }
        if (!(scanView == null || scanView.isEmpty())) {
            for (String str : scanView.keySet()) {
                arrayList.add(new ViewVoiceHolder(str, (View) scanView.get(str)));
            }
        }
        Log.d("VoiceDispatcher", "getSupportedByActivity() return size=" + arrayList.size());
        return arrayList;
    }

    public boolean dispatchToActivity(VoiceEvent voiceEvent) {
        IVocal iVocal;
        boolean a;
        boolean z = false;
        Log.d("VoiceDispatcher", "dispatchToActivity(" + voiceEvent + ")");
        Log.d("VoiceDispatcher", "VoiceManager.instance().getCurrentActivity() = " + VoiceManager.instance().getCurrentActivity());
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
        } else {
            iVocal = null;
        }
        if (iVocal != null) {
            a = a(voiceEvent, iVocal);
        } else {
            a = false;
        }
        Log.d("VoiceDispatcher", "dispatchToActivity currentListener return(" + a + "), isEmpty=" + VoiceUtils.isEmpty(voiceEvent.getKeyword()));
        if (!(a || voiceEvent.getType() != 4 || VoiceUtils.isEmpty(voiceEvent.getKeyword()) || (iVocal instanceof IUnVocal))) {
            String keyword = voiceEvent.getKeyword();
            Log.d("VoiceDispatcher", "dispatchToActivity(" + keyword + ")");
            if (!TextUtils.isEmpty(keyword)) {
                View searchView;
                if (VoiceManager.instance().getCurrentDialog() != null) {
                    Log.d("VoiceDispatcher", "dispatchToDialog(" + keyword + ")");
                    DialogInterface currentDialog = VoiceManager.instance().getCurrentDialog();
                    searchView = VoiceViewHelper.searchView(currentDialog, keyword);
                    if (VoiceManager.instance().getCurrentDialog() == currentDialog) {
                        z = VoiceViewHelper.performView(currentDialog, searchView);
                    }
                    Log.d("VoiceDispatcher", "dispatchToDialog() return " + z);
                    a = z;
                } else {
                    Log.d("VoiceDispatcher", "dispatchToActivity(" + keyword + ")");
                    Activity currentActivity = VoiceManager.instance().getCurrentActivity();
                    searchView = VoiceViewHelper.searchView(currentActivity, keyword);
                    if (VoiceManager.instance().getCurrentActivity() == currentActivity) {
                        z = VoiceViewHelper.performView(currentActivity, searchView);
                    }
                    Log.d("VoiceDispatcher", "dispatchToActivity() return " + z);
                    a = z;
                }
            }
        }
        Log.d("VoiceDispatcher", "dispatchToActivity(" + voiceEvent + ") return " + a);
        return a;
    }

    public boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        Log.d("VoiceDispatcher", "dispatchVoiceEvent(" + voiceEvent + ")");
        boolean a = a(voiceEvent);
        Bundle extras = voiceEvent.getExtras();
        Log.d("VoiceDispatcher", "dispatchVoiceEvent(dispatch first return" + a + "), translator=" + this.f417a);
        if (!(a || this.f417a == null)) {
            int type = voiceEvent.getType();
            String keyword = voiceEvent.getKeyword();
            String standard = this.f417a.getStandard(keyword);
            Log.d("VoiceDispatcher", "dispatchVoiceEvent(dispatch second , keywords=" + keyword + ", newwords=" + standard);
            if (keyword.equals(standard)) {
                Log.d("VoiceDispatcher", "dispatchVoiceEvent() original equal standard!");
            } else {
                VoiceEvent createVoiceEvent = VoiceEventFactory.createVoiceEvent(type, standard);
                Bundle bundle = new Bundle();
                for (String str : extras.keySet()) {
                    bundle.putString(str, this.f417a.getStandard(extras.getString(str)));
                }
                if (!bundle.isEmpty()) {
                    createVoiceEvent.putExtras(bundle);
                }
                a = a(createVoiceEvent);
            }
        }
        Log.d("VoiceDispatcher", "dispatchVoiceEvent() return " + a);
        return a;
    }

    private boolean a(VoiceEvent voiceEvent) {
        IVocal iVocal;
        boolean z;
        Log.d("VoiceDispatcher", "doDispatchVoiceEvent(" + voiceEvent + ")");
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
        } else {
            iVocal = null;
        }
        if (a(10000, voiceEvent, null)) {
            z = true;
        } else if (dispatchToActivity(voiceEvent)) {
            z = true;
        } else if (a(20000, voiceEvent, iVocal)) {
            z = true;
        } else if (a(30000, voiceEvent, iVocal)) {
            z = true;
        } else if (a(VoiceManager.PRIORITY_RESERVED, voiceEvent, iVocal)) {
            z = true;
        } else {
            z = false;
        }
        Log.d("VoiceDispatcher", "doDispatchVoiceEvent(" + voiceEvent + ") return " + z);
        return z;
    }

    public void release() {
        Log.d("VoiceDispatcher", "release()");
        this.f418a.clear();
    }
}
