package com.gala.sdk.event;

import java.util.Arrays;

public class AdSpecialEvent {
    private EventType f609a;
    private Object[] f610a;

    public enum EventType {
        AD_HIDE,
        STARTUP_AD_REDIRECT
    }

    public AdSpecialEvent(EventType eventType) {
        this.f609a = eventType;
    }

    public AdSpecialEvent(EventType eventType, Object... args) {
        this.f609a = eventType;
        this.f610a = args;
    }

    public EventType getEventType() {
        return this.f609a;
    }

    public Object[] getEventParams() {
        return this.f610a;
    }

    public Object getEventParamAt(int index) {
        if (index < 0 || this.f610a == null || this.f610a.length <= index) {
            return null;
        }
        return this.f610a[index];
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SpecialEvent@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("type=").append(this.f609a);
        stringBuilder.append(", params=").append(Arrays.toString(this.f610a));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
