package com.gala.sdk.player;

import android.util.SparseArray;

public class GalaVideoPlayerParams {
    public static final int BUNDLE = 1012;
    public static final int CONTEXT = 1000;
    public static final int ERROR_FINISH_LISTENER = 1018;
    public static final int EVENT_INPUT = 1003;
    public static final int FULL_ERROR_STRATEGY = 1009;
    public static final int HISTORY_FETCHER = 1002;
    public static final int HISTORY_RECORDER_LISTENER = 1006;
    public static final int INITIAL_ERROR_STRATEGY = 1008;
    public static final int INITIAL_LAYOUT_PARAMS = 1015;
    public static final int INITIAL_SCREEN_MODE = 1014;
    public static final int MESSAGE_REMINDER = 1004;
    public static final int NET_DIAGNOSE_PROVIDER = 1011;
    public static final int PLAYER_OVERLAY = 1001;
    public static final int PLAYER_STATE_LISTENER = 1013;
    public static final int PROJ_EVENT_REPORTER = 1005;
    public static final int SHOW_HINT_LISTENER = 1007;
    public static final int SPECIAL_EVENT_LISTENER = 1019;
    public static final int WINDOW_ERROR_STRATEGY = 1010;
    public static final int WINDOW_ZOOM_RATIO = 1017;
    private final SparseArray<Object> f651a = new SparseArray();

    public GalaVideoPlayerParams set(int key, Object value) {
        this.f651a.put(key, value);
        return this;
    }

    public Object get(int key) {
        return this.f651a.get(key);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Params{");
        stringBuilder.append(this.f651a.toString());
        stringBuilder.append("}");
        return super.toString();
    }
}
