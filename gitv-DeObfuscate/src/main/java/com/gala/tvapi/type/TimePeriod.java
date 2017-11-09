package com.gala.tvapi.type;

public enum TimePeriod {
    INVALID(0),
    VALID(1),
    LOCKED(2),
    OVERDUE(3);
    
    private int f1153a;

    private TimePeriod(int value) {
        this.f1153a = value;
    }

    public final int getValue() {
        return this.f1153a;
    }
}
