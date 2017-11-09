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
    private static GlobalPerformanceTracker a;
    private int f367a;
    private long f368a;
    private Pair<String, Long> f369a;
    private OnPerformanceTrackerDispatcher f370a = new OnPerformanceTrackerDispatcher();
    private IPerformanceMonitor f371a;
    private List<Pair<String, Long>> f372a = new ArrayList();
    private boolean f373a;
    private PerformanceStepInfo[] f374a = new PerformanceStepInfo[5];
    private long b;

    class OnPerformanceTrackerDispatcher extends Observable<OnPerformanceTrackerListener> implements OnPerformanceTrackerListener {
        OnPerformanceTrackerDispatcher() {
        }

        public void onRoutineStart(String requestName, String tvId) {
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "onRoutineStart(" + requestName + ", " + tvId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineStart(requestName, tvId);
            }
        }

        public void onRoutineEnd(String requestName, String tvId) {
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "onRoutineEnd(" + requestName + ", " + tvId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineEnd(requestName, tvId);
            }
        }

        public void onRoutineEnd(String requestName, String requestId, long time, String st, String pfec, List<Long> requestTimes, long jsonParseTime, boolean isMainRoute) {
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "onRoutineEnd(" + requestName + ", " + requestId + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onRoutineEnd(requestName, requestId, time, st, pfec, requestTimes, jsonParseTime, isMainRoute);
            }
        }

        public void onPlayerLoadingInfo(String requestId, long td, long tm1, long tm2, String ra, boolean isAd, String playerModeDesc, boolean isVip, boolean isPreview) {
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "onPlayerLodingInfo(requestId=" + requestId + ", td=" + td + ", tm1=" + tm1 + ", tm2=" + tm2 + ", ra=" + ra + ", isAd=" + isAd + ")");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onPlayerLoadingInfo(requestId, td, tm1, tm2, ra, isAd, playerModeDesc, isVip, isPreview);
            }
        }

        public void onPlayerLoadingStepInfo(PerformanceStepInfo[] stepInfos) {
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "onPlayerLoadingStepInfo()");
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OnPerformanceTrackerListener) it.next()).onPlayerLoadingStepInfo(stepInfos);
            }
        }
    }

    public boolean addOnPerformanceTrackerListener(OnPerformanceTrackerListener listener) {
        return this.f370a.addListener(listener);
    }

    public boolean removeOnPerformanceTrackerListener(OnPerformanceTrackerListener listener) {
        return this.f370a.removeListener(listener);
    }

    private GlobalPerformanceTracker() {
    }

    public static synchronized GlobalPerformanceTracker instance() {
        GlobalPerformanceTracker globalPerformanceTracker;
        synchronized (GlobalPerformanceTracker.class) {
            if (a == null) {
                a = new GlobalPerformanceTracker();
            }
            globalPerformanceTracker = a;
        }
        return globalPerformanceTracker;
    }

    public void initialize(IPerformanceMonitor monitor) {
        this.f371a = monitor;
        if (this.f370a != null) {
            this.f370a.clear();
        }
    }

    private Pair<String, Long> a(String str, long j) {
        if (StringUtils.isEmpty(str)) {
            MyLogUtils.w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: invalid routineName");
            return null;
        } else if (j <= 0) {
            MyLogUtils.w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: invalid time token");
            return null;
        } else {
            Pair<String, Long> pair;
            Iterator it = this.f372a.iterator();
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

    private static String a() {
        return new String(" @" + new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).format(new Date()) + "\n");
    }

    public synchronized void recordSpecialEvent(String eventName) {
        CharSequence spannableString = new SpannableString(eventName + a());
        spannableString.setSpan(new ForegroundColorSpan(-65536), 0, eventName.length(), 33);
        spannableString.setSpan(new ForegroundColorSpan(-16711936), eventName.length(), spannableString.length(), 33);
        a(spannableString);
    }

    private void a(CharSequence charSequence) {
        if (this.f371a != null) {
            this.f371a.updateContent(charSequence);
        }
    }

    public synchronized void startTracking() {
        MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "startTracking: mMonitor=" + this.f371a);
        if (this.f371a != null) {
            this.f371a.start();
        }
        recordSpecialEvent("START TRACKING");
    }

    public synchronized void stopTracking() {
        MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "stopTracking: mMonitor=" + this.f371a);
        if (this.f371a != null) {
            this.f371a.stop();
        }
    }

    public synchronized long recordRoutineStart(String routineName) {
        long uptimeMillis;
        uptimeMillis = SystemClock.uptimeMillis();
        MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", ">> recordRoutineStart: " + routineName + ", " + uptimeMillis);
        if (StringUtils.isEmpty(routineName)) {
            throw new IllegalArgumentException("Please provide valid routine name!");
        }
        this.f372a.add(new Pair(routineName, Long.valueOf(uptimeMillis)));
        if (this.f371a != null) {
            this.f371a.updateTitle(routineName);
        }
        return uptimeMillis;
    }

    public synchronized void recordRoutineEnd(String routineName, long timeToken) {
        MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", ">> recordRoutineEnd: " + routineName + ", " + timeToken);
        long uptimeMillis = SystemClock.uptimeMillis();
        Pair a = a(routineName, timeToken);
        if (a == null) {
            MyLogUtils.w("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: no such routine recorded before!");
        } else {
            String str = "[" + (uptimeMillis - ((Long) a.second).longValue()) + "ms] ";
            Object a2 = a();
            CharSequence spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(str);
            spannableStringBuilder.append(routineName);
            spannableStringBuilder.append(a2);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(-16711936), 0, str.length() + 0, 33);
            int length = (str.length() + routineName.length()) + 0;
            spannableStringBuilder.setSpan(new ForegroundColorSpan(-16711936), length, a2.length() + length, 33);
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "recordRoutineEnd: msg=" + spannableStringBuilder);
            a(spannableStringBuilder);
        }
    }

    public synchronized void recordPerformanceStepStart(String str, String stepName) {
        this.f369a = new Pair(stepName, Long.valueOf(SystemClock.uptimeMillis()));
    }

    public synchronized void recordPerformanceStepEnd(String eventId, String stepName) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.f369a != null && (StringUtils.equals("stop", stepName) || StringUtils.equals((String) this.f369a.first, stepName))) {
            Object obj;
            String str = (String) this.f369a.first;
            String valueOf = String.valueOf(uptimeMillis - ((Long) this.f369a.second).longValue());
            if (StringUtils.equals("stop", stepName)) {
                valueOf = str + "#" + valueOf;
            }
            this.f369a = null;
            int i = 0;
            while (i < this.f374a.length) {
                if (this.f374a[i] != null && StringUtils.equals(eventId, this.f374a[i].getEventId())) {
                    this.f374a[i].addStep(stepName, valueOf);
                    this.f370a.onPlayerLoadingStepInfo(this.f374a);
                    obj = 1;
                    break;
                }
                i++;
            }
            obj = null;
            if (obj == null) {
                PerformanceStepInfo performanceStepInfo = new PerformanceStepInfo(eventId);
                performanceStepInfo.addStep(stepName, valueOf);
                if (this.f367a < 0 || this.f367a >= 5) {
                    this.f367a = 0;
                }
                this.f374a[this.f367a] = performanceStepInfo;
                this.f367a++;
            }
        }
    }

    public synchronized void setFirstStarted(boolean isFirstarted) {
        this.f373a = isFirstarted;
    }

    public void setPageInitToken(long token) {
        this.b = token;
    }

    public void setPlayerInitToken(long token) {
        this.f368a = token;
    }

    public synchronized void recordRoutineInitToStarted(String str, String str2, boolean z, String str3, boolean z2, boolean z3) {
        if (this.f373a) {
            MyLogUtils.w("Player/Perf/GlobalPerformanceTracker", "<<recordRoutineEnd: not first started!");
        } else {
            SystemClock.uptimeMillis();
            MyLogUtils.d("Player/Perf/GlobalPerformanceTracker", "mPageInitToken=" + this.b + ", mPlayerInitToken=" + this.f368a);
        }
    }
}
