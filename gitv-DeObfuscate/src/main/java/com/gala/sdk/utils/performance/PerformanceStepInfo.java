package com.gala.sdk.utils.performance;

import java.util.HashMap;
import java.util.Map;

public class PerformanceStepInfo {
    private String f741a;
    private Map<String, String> f742a = new HashMap();

    public PerformanceStepInfo(String eventId) {
        setEventId(eventId);
    }

    public void addStep(String name, String consumeTime) {
        this.f742a.put(name, consumeTime);
    }

    public Map<String, String> getStepMap() {
        return this.f742a;
    }

    public String getEventId() {
        return this.f741a;
    }

    public void setEventId(String mEventId) {
        this.f741a = mEventId;
    }

    public String toString() {
        return "PerformanceStepInfo[" + hashCode() + "mEventId=" + this.f741a + "map=" + this.f742a;
    }
}
