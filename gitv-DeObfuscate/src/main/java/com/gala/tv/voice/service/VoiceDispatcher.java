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
    private static Comparator<Holder> f820a = new C02021();
    private ISemanticTranslator f821a;
    private final PriorityQueue<Holder> f822a = new PriorityQueue(10, f820a);

    static class C02021 implements Comparator<Holder> {
        C02021() {
        }

        public final int compare(Holder holder, Holder holder2) {
            if (holder.f818a < holder2.f818a) {
                return -1;
            }
            if (holder.f818a > holder2.f818a) {
                return 1;
            }
            return 0;
        }
    }

    private class Holder {
        int f818a;
        IVocal f819a;

        public Holder(VoiceDispatcher voiceDispatcher, int i, IVocal iVocal) {
            this.f818a = i;
            this.f819a = iVocal;
        }

        public boolean equals(Object obj) {
            if (obj == null || this == obj) {
                return false;
            }
            if (obj instanceof Holder) {
                if (this.f819a != ((Holder) obj).f819a) {
                    return false;
                }
                return true;
            } else if (!(obj instanceof IVocal)) {
                return false;
            } else {
                if (this.f819a != obj) {
                    return false;
                }
                return true;
            }
        }

        public String toString() {
            return "Holder(priority=" + this.f818a + ", listener=" + this.f819a + ")";
        }
    }

    VoiceDispatcher() {
    }

    private boolean m542a(IVocal iVocal) {
        boolean z;
        synchronized (this.f822a) {
            Iterator it = this.f822a.iterator();
            while (it.hasNext()) {
                if (((Holder) it.next()).f819a == iVocal) {
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }

    private void m537a(IVocal iVocal) {
        synchronized (this.f822a) {
            Iterator it = this.f822a.iterator();
            while (it.hasNext()) {
                if (((Holder) it.next()).f819a == iVocal) {
                    it.remove();
                }
            }
        }
    }

    private static boolean m541a(VoiceEvent voiceEvent, IVocal iVocal) {
        boolean dispatchVoiceEvent;
        Log.m525d("VoiceDispatcher", "dispatchToListener() listener=" + iVocal.getClass().getName());
        List<AbsVoiceAction> supportedVoices = iVocal.getSupportedVoices();
        if (!(supportedVoices == null || supportedVoices.isEmpty())) {
            for (AbsVoiceAction absVoiceAction : supportedVoices) {
                Log.m525d("VoiceDispatcher", "dispatchToListener() holder=" + absVoiceAction);
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
        Log.m525d("VoiceDispatcher", "dispatchToListener() return " + dispatchVoiceEvent);
        return dispatchVoiceEvent;
    }

    private boolean m539a(int i, VoiceEvent voiceEvent, IVocal iVocal) {
        boolean z;
        synchronized (this.f822a) {
            Iterator it = this.f822a.iterator();
            while (it.hasNext()) {
                Holder holder = (Holder) it.next();
                Log.m525d("VoiceDispatcher", "dispatch() find  holder.priority = " + holder.f818a + ", priority = " + i);
                if (iVocal != holder.f819a && holder.f818a >= i && holder.f818a < i + 10000 && m541a(voiceEvent, holder.f819a)) {
                    Log.m525d("VoiceDispatcher", "dispatch() find listener " + holder.f819a);
                    z = true;
                    break;
                }
            }
            z = false;
        }
        Log.m525d("VoiceDispatcher", "dispatch(" + i + ", " + voiceEvent + ") return " + z + ", listeners=" + this.f822a);
        return z;
    }

    public boolean addListener(int i, IVocal iVocal) {
        boolean z = false;
        if (iVocal != null) {
            synchronized (this.f822a) {
                if (!m537a(iVocal)) {
                    z = this.f822a.add(new Holder(this, i, iVocal));
                }
            }
        }
        Log.m525d("VoiceDispatcher", "addListener(" + i + ", " + iVocal + ") return " + z);
        return z;
    }

    public boolean removeListener(IVocal iVocal) {
        Log.m525d("VoiceDispatcher", "removeListener(" + iVocal + ")");
        m537a(iVocal);
        return true;
    }

    public void setSemanticTranslator(ISemanticTranslator iSemanticTranslator) {
        Log.m525d("VoiceDispatcher", "setSemanticTranslator(" + iSemanticTranslator + ")");
        this.f821a = iSemanticTranslator;
    }

    public List<VoiceEventGroup> getSupportedGroups() {
        List arrayList = new ArrayList();
        Log.m525d("VoiceDispatcher", "getSupportedGroups() listeners.size() = " + this.f822a.size());
        IVocal iVocal = null;
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
            m538a(arrayList, iVocal.getSupportedVoices());
        }
        if (!(iVocal instanceof IUnVocal)) {
            m538a(arrayList, m536a());
        }
        Iterator it = this.f822a.iterator();
        while (it.hasNext()) {
            m538a(arrayList, ((Holder) it.next()).f819a.getSupportedVoices());
        }
        Log.m525d("VoiceDispatcher", "getSupportedGroups() return " + arrayList.size());
        return arrayList;
    }

    private static void m538a(List<VoiceEventGroup> list, List<AbsVoiceAction> list2) {
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
        Log.m525d("VoiceDispatcher", "getSupportedGroups() listeners.size() = " + this.f822a.size());
        IVocal iVocal = null;
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
            m538a(arrayList, iVocal.getSupportedVoices());
        }
        if (!(iVocal instanceof IUnVocal)) {
            m538a(arrayList, m536a());
        }
        Log.m525d("VoiceDispatcher", "getSupportedGroups() return " + arrayList.size());
        return arrayList;
    }

    private static List<AbsVoiceAction> m536a() {
        Map scanView;
        Log.m525d("VoiceDispatcher", "getSupportedByActivity()!!");
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
        Log.m525d("VoiceDispatcher", "getSupportedByActivity() return size=" + arrayList.size());
        return arrayList;
    }

    public boolean dispatchToActivity(VoiceEvent voiceEvent) {
        IVocal iVocal;
        boolean a;
        boolean z = false;
        Log.m525d("VoiceDispatcher", "dispatchToActivity(" + voiceEvent + ")");
        Log.m525d("VoiceDispatcher", "VoiceManager.instance().getCurrentActivity() = " + VoiceManager.instance().getCurrentActivity());
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
        } else {
            iVocal = null;
        }
        if (iVocal != null) {
            a = m541a(voiceEvent, iVocal);
        } else {
            a = false;
        }
        Log.m525d("VoiceDispatcher", "dispatchToActivity currentListener return(" + a + "), isEmpty=" + VoiceUtils.isEmpty(voiceEvent.getKeyword()));
        if (!(a || voiceEvent.getType() != 4 || VoiceUtils.isEmpty(voiceEvent.getKeyword()) || (iVocal instanceof IUnVocal))) {
            String keyword = voiceEvent.getKeyword();
            Log.m525d("VoiceDispatcher", "dispatchToActivity(" + keyword + ")");
            if (!TextUtils.isEmpty(keyword)) {
                View searchView;
                if (VoiceManager.instance().getCurrentDialog() != null) {
                    Log.m525d("VoiceDispatcher", "dispatchToDialog(" + keyword + ")");
                    DialogInterface currentDialog = VoiceManager.instance().getCurrentDialog();
                    searchView = VoiceViewHelper.searchView(currentDialog, keyword);
                    if (VoiceManager.instance().getCurrentDialog() == currentDialog) {
                        z = VoiceViewHelper.performView(currentDialog, searchView);
                    }
                    Log.m525d("VoiceDispatcher", "dispatchToDialog() return " + z);
                    a = z;
                } else {
                    Log.m525d("VoiceDispatcher", "dispatchToActivity(" + keyword + ")");
                    Activity currentActivity = VoiceManager.instance().getCurrentActivity();
                    searchView = VoiceViewHelper.searchView(currentActivity, keyword);
                    if (VoiceManager.instance().getCurrentActivity() == currentActivity) {
                        z = VoiceViewHelper.performView(currentActivity, searchView);
                    }
                    Log.m525d("VoiceDispatcher", "dispatchToActivity() return " + z);
                    a = z;
                }
            }
        }
        Log.m525d("VoiceDispatcher", "dispatchToActivity(" + voiceEvent + ") return " + a);
        return a;
    }

    public boolean dispatchVoiceEvent(VoiceEvent voiceEvent) {
        Log.m525d("VoiceDispatcher", "dispatchVoiceEvent(" + voiceEvent + ")");
        boolean a = m540a(voiceEvent);
        Bundle extras = voiceEvent.getExtras();
        Log.m525d("VoiceDispatcher", "dispatchVoiceEvent(dispatch first return" + a + "), translator=" + this.f821a);
        if (!(a || this.f821a == null)) {
            int type = voiceEvent.getType();
            String keyword = voiceEvent.getKeyword();
            String standard = this.f821a.getStandard(keyword);
            Log.m525d("VoiceDispatcher", "dispatchVoiceEvent(dispatch second , keywords=" + keyword + ", newwords=" + standard);
            if (keyword.equals(standard)) {
                Log.m525d("VoiceDispatcher", "dispatchVoiceEvent() original equal standard!");
            } else {
                VoiceEvent createVoiceEvent = VoiceEventFactory.createVoiceEvent(type, standard);
                Bundle bundle = new Bundle();
                for (String str : extras.keySet()) {
                    bundle.putString(str, this.f821a.getStandard(extras.getString(str)));
                }
                if (!bundle.isEmpty()) {
                    createVoiceEvent.putExtras(bundle);
                }
                a = m540a(createVoiceEvent);
            }
        }
        Log.m525d("VoiceDispatcher", "dispatchVoiceEvent() return " + a);
        return a;
    }

    private boolean m540a(VoiceEvent voiceEvent) {
        IVocal iVocal;
        boolean z;
        Log.m525d("VoiceDispatcher", "doDispatchVoiceEvent(" + voiceEvent + ")");
        if (VoiceManager.instance().getCurrentActivity() instanceof IVocal) {
            iVocal = (IVocal) VoiceManager.instance().getCurrentActivity();
        } else {
            iVocal = null;
        }
        if (m539a(10000, voiceEvent, null)) {
            z = true;
        } else if (dispatchToActivity(voiceEvent)) {
            z = true;
        } else if (m539a(20000, voiceEvent, iVocal)) {
            z = true;
        } else if (m539a(30000, voiceEvent, iVocal)) {
            z = true;
        } else if (m539a(VoiceManager.PRIORITY_RESERVED, voiceEvent, iVocal)) {
            z = true;
        } else {
            z = false;
        }
        Log.m525d("VoiceDispatcher", "doDispatchVoiceEvent(" + voiceEvent + ") return " + z);
        return z;
    }

    public void release() {
        Log.m525d("VoiceDispatcher", "release()");
        this.f822a.clear();
    }
}
