package com.gala.sdk.utils.performance;

import android.os.SystemClock;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.sdk.utils.Observable;
import com.gala.sdk.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class GlobalPerformanceTracker {
    public static final String ACTIVITY_CREATE_STEP = "tm_activity.create";
    public static final String CONTROLLER_INIT_STEP = "tm_controller.init";
    public static final String CONTROLLER_SHOWLOADING_STEP = "tm_controller.showloading";
    public static final String DATA_LOAD_STEP = "tm_data.load";
    public static final String PLAYER_ADAPTER_INIT_STEP = "tm_player-adapter.init";
    public static final String PLAYER_ADAPTER_PREPARE_STEP = "tm_player-adapter.prepare";
    public static final String PLAYER_ADAPTER_START_STEP = "tm_player-adapter.start";
    public static final String PLAYER_PREF_INIT_STEP = "tm_player.prefinit";
    public static final String PLAYER_UI_INIT_STEP = "tm_player-ui.init";
    public static final String PLUGIN_LOAD_STEP = "tm_plugin.load";
    public static final String STOP_LOADING_STEP = "stop";
    private static GlobalPerformanceTracker f731a;
    private int f732a;
    private long f733a;
    private Pair<String, Long> f734a;
    private OnPerformanceTrackerDispatcher f735a = new OnPerformanceTrackerDispatcher();
    private IPerformanceMonitor f736a;
    private List<Pair<String, Long>> f737a = new ArrayList();
    private boolean f738a;
    private PerformanceStepInfo[] f739a = new PerformanceStepInfo[5];
    private long f740b;

    class OnPerformanceTrackerDispatcher extends Observable<OnPerformanceTrackerListener> implements OnPerformanceTrackerListener {
        OnPerformanceTrackerDispatcher() {
        }

        public void onRoutineStart(String requestName, String tvId) {
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "onRoutineStart(" + requestName + ", " + tvId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineStart(requestName, tvId);
            }
        }

        public void onRoutineEnd(String requestName, String tvId) {
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "onRoutineEnd(" + requestName + ", " + tvId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineEnd(requestName, tvId);
            }
        }

        public void onRoutineEnd(String requestName, String requestId, long time, String st, String pfec, List<Long> requestTimes, long jsonParseTime, boolean isMainRoute) {
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "onRoutineEnd(" + requestName + ", " + requestId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineEnd(requestName, requestId, time, st, pfec, requestTimes, jsonParseTime, isMainRoute);
            }
        }

        public void onPlayerLoadingInfo(String requestId, long td, long tm1, long tm2, String ra, boolean isAd, String playerModeDesc, boolean isVip, boolean isPreview) {
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "onPlayerLodingInfo(requestId=" + requestId + ", td=" + td + ", tm1=" + tm1 + ", tm2=" + tm2 + ", ra=" + ra + ", isAd=" + isAd + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onPlayerLoadingInfo(requestId, td, tm1, tm2, ra, isAd, playerModeDesc, isVip, isPreview);
            }
        }

        public void onPlayerLoadingStepInfo(PerformanceStepInfo[] stepInfos) {
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "onPlayerLoadingStepInfo()");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onPlayerLoadingStepInfo(stepInfos);
            }
        }
    }

    public boolean addOnPerformanceTrackerListener(OnPerformanceTrackerListener listener) {
        return this.f735a.addListener(listener);
    }

    public boolean removeOnPerformanceTrackerListener(OnPerformanceTrackerListener listener) {
        return this.f735a.removeListener(listener);
    }

    private GlobalPerformanceTracker() {
    }

    public static synchronized GlobalPerformanceTracker instance() {
        GlobalPerformanceTracker globalPerformanceTracker;
        synchronized (GlobalPerformanceTracker.class) {
            if (f731a == null) {
                f731a = new GlobalPerformanceTracker();
            }
            globalPerformanceTracker = f731a;
        }
        return globalPerformanceTracker;
    }

    public void initialize(IPerformanceMonitor monitor) {
        this.f736a = monitor;
        if (this.f735a != null) {
            this.f735a.clear();
        }
    }

    private Pair<String, Long> m485a(String str, long j) {
        if (StringUtils.isEmpty(str)) {
            MyLogUtils.m468w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: invalid routineName");
            return null;
        } else if (j <= 0) {
            MyLogUtils.m468w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: invalid time token");
            return null;
        } else {
            Pair<String, Long> pair;
            Iterator it = this.f737a.iterator();
            while (it.hasNext()) {
                pair = (Pair) it.next();
                if (((Long) pair.second).longValue() == j) {
                    it.remove();
                    break;
                }
            }
            pair = null;
            return pair;
        }
    }

    private static String m486a() {
        return new String(" @" + new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).format(new Date()) + "\n");
    }

    public synchronized void recordSpecialEvent(String eventName) {
        CharSequence spannableString = new SpannableString(eventName + m486a());
        spannableString.setSpan(new ForegroundColorSpan(-65536), 0, eventName.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(-16711936), eventName.length(), spannableString.length(), 33);
        m487a(spannableString);
    }

    private void m487a(CharSequence charSequence) {
        if (this.f736a != null) {
            this.f736a.updateContent(charSequence);
        }
    }

    public synchronized void startTracking() {
        MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "startTracking: mMonitor=" + this.f736a);
        if (this.f736a != null) {
            this.f736a.start();
        }
        recordSpecialEvent("START TRACKING");
    }

    public synchronized void stopTracking() {
        MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "stopTracking: mMonitor=" + this.f736a);
        if (this.f736a != null) {
            this.f736a.stop();
        }
    }

    public synchronized long recordRoutineStart(String routineName) {
        long uptimeMillis;
        uptimeMillis = SystemClock.uptimeMillis();
        MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", ">> recordRoutineStart: " + routineName + ", " + uptimeMillis);
        if (StringUtils.isEmpty(routineName)) {
            throw new IllegalArgumentException("Please provide valid routine name!");
        }
        this.f737a.add(new Pair(routineName, Long.valueOf(uptimeMillis)));
        if (this.f736a != null) {
            this.f736a.updateTitle(routineName);
        }
        return uptimeMillis;
    }

    public synchronized void recordRoutineEnd(String routineName, long timeToken) {
        MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", ">> recordRoutineEnd: " + routineName + ", " + timeToken);
        long uptimeMillis = SystemClock.uptimeMillis();
        Pair a = m485a(routineName, timeToken);
        if (a == null) {
            MyLogUtils.m468w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: no such routine recorded before!");
        } else {
            String str = "[" + (uptimeMillis - ((Long) a.second).longValue()) + "ms] ";
            Object a2 = m486a();
            CharSequence spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(str);
            spannableStringBuilder.append(routineName);
            spannableStringBuilder.append(a2);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(-16711936), 0, str.length() + 0, 33);
            int length = (str.length() + routineName.length()) + 0;
            spannableStringBuilder.setSpan(new ForegroundColorSpan(-16711936), length, a2.length() + length, 33);
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: msg=" + spannableStringBuilder);
            m487a(spannableStringBuilder);
        }
    }

    public synchronized void recordPerformanceStepStart(String str, String stepName) {
        this.f734a = new Pair(stepName, Long.valueOf(SystemClock.uptimeMillis()));
    }

    public synchronized void recordPerformanceStepEnd(String eventId, String stepName) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.f734a != null && (StringUtils.equals("stop", stepName) || StringUtils.equals((String) this.f734a.first, stepName))) {
            Object obj;
            String str = (String) this.f734a.first;
            String valueOf = String.valueOf(uptimeMillis - ((Long) this.f734a.second).longValue());
            if (StringUtils.equals("stop", stepName)) {
                valueOf = str + "#" + valueOf;
            }
            this.f734a = null;
            int i = 0;
            while (i < this.f739a.length) {
                if (this.f739a[i] != null && StringUtils.equals(eventId, this.f739a[i].getEventId())) {
                    this.f739a[i].addStep(stepName, valueOf);
                    this.f735a.onPlayerLoadingStepInfo(this.f739a);
                    obj = 1;
                    break;
                }
                i++;
            }
            obj = null;
            if (obj == null) {
                PerformanceStepInfo performanceStepInfo = new PerformanceStepInfo(eventId);
                performanceStepInfo.addStep(stepName, valueOf);
                if (this.f732a < 0 || this.f732a >= 5) {
                    this.f732a = 0;
                }
                this.f739a[this.f732a] = performanceStepInfo;
                this.f732a++;
            }
        }
    }

    public synchronized void setFirstStarted(boolean isFirstarted) {
        this.f738a = isFirstarted;
    }

    public void setPageInitToken(long token) {
        this.f740b = token;
    }

    public void setPlayerInitToken(long token) {
        this.f733a = token;
    }

    public synchronized void recordRoutineInitToStarted(String str, String str2, boolean z, String str3, boolean z2, boolean z3) {
        if (this.f738a) {
            MyLogUtils.m468w("Player/Perf/GlobalPerformanceTracker", "<<recordRoutineEnd: not first started!");
        } else {
            SystemClock.uptimeMillis();
            MyLogUtils.m462d("Player/Perf/GlobalPerformanceTracker", "mPageInitToken=" + this.f740b + ", mPlayerInitToken=" + this.f733a);
        }
    }
}
