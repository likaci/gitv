package com.gala.sdk.event;

import java.util.Arrays;

public class AdSpecialEvent {
    private EventType a;
    private Object[] f312a;

    public enum EventType {
        AD_HIDE,
        STARTUP_AD_REDIRECT
    }

    public AdSpecialEvent(EventType eventType) {
        this.a = eventType;
    }

    public AdSpecialEvent(EventType eventType, Object... args) {
        this.a = eventType;
        this.f312a = args;
    }

    public EventType getEventType() {
        return this.a;
    }

    public Object[] getEventParams() {
        return this.f312a;
    }

    public Object getEventParamAt(int index) {
        if (index < 0 || this.f312a == null || this.f312a.length <= index) {
            return null;
        }
        return this.f312a[index];
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SpecialEvent@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("type=").append(this.a);
        stringBuilder.append(", params=").append(Arrays.toString(this.f312a));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
