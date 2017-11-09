package com.gala.sdk.player;

import java.util.HashMap;
import java.util.Map.Entry;

public class OverrideableBuilder {
    private HashMap<Overrideables, IOverrideableRunner> a = new HashMap();

    public OverrideableBuilder append(Overrideables key, IOverrideableRunner value) {
        this.a.put(key, value);
        return this;
    }

    public HashMap<Overrideables, IOverrideableRunner> getOverrides() {
        return this.a;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Entry entry : this.a.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
