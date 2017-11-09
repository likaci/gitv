package com.gala.sdk.utils.performance;

import java.util.HashMap;
import java.util.Map;

public class PerformanceStepInfo {
    private String a;
    private Map<String, String> f375a = new HashMap();

    public PerformanceStepInfo(String eventId) {
        setEventId(eventId);
    }

    public void addStep(String name, String consumeTime) {
        this.f375a.put(name, consumeTime);
    }

    public Map<String, String> getStepMap() {
        return this.f375a;
    }

    public String getEventId() {
        return this.a;
    }

    public void setEventId(String mEventId) {
        this.a = mEventId;
    }

    public String toString() {
        return "PerformanceStepInfo[" + hashCode() + "mEventId=" + this.a + "map=" + this.f375a;
    }
}
