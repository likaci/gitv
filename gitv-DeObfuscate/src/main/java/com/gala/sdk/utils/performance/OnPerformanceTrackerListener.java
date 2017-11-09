package com.gala.sdk.utils.performance;

import java.util.List;

public interface OnPerformanceTrackerListener {
    void onPlayerLoadingInfo(String str, long j, long j2, long j3, String str2, boolean z, String str3, boolean z2, boolean z3);

    void onPlayerLoadingStepInfo(PerformanceStepInfo[] performanceStepInfoArr);

    void onRoutineEnd(String str, String str2);

    void onRoutineEnd(String str, String str2, long j, String str3, String str4, List<Long> list, long j2, boolean z);

    void onRoutineStart(String str, String str2);
}
