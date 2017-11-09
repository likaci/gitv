package com.gala.sdk.player;

import java.util.HashMap;
import java.util.Map.Entry;

public class OverrideableBuilder {
    private HashMap<Overrideables, IOverrideableRunner> f662a = new HashMap();

    public OverrideableBuilder append(Overrideables key, IOverrideableRunner value) {
        this.f662a.put(key, value);
        return this;
    }

    public HashMap<Overrideables, IOverrideableRunner> getOverrides() {
        return this.f662a;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Entry entry : this.f662a.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
