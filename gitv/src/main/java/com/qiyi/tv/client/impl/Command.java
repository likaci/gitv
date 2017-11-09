package com.qiyi.tv.client.impl;

import android.content.Context;
import android.os.Bundle;

public abstract class Command {
    private final int a;
    private Context f822a;
    private final int b;
    private final int c;

    public abstract Bundle process(Bundle bundle);

    public Command(Context context, int target, int operation, int dataType) {
        this.f822a = context;
        this.a = target;
        this.b = operation;
        this.c = dataType;
    }

    public void setContext(Context context) {
        this.f822a = context;
    }

    public Context getContext() {
        return this.f822a;
    }

    public int getTarget() {
        return this.a;
    }

    public int getOperationType() {
        return this.b;
    }

    public int getDataType() {
        return this.c;
    }
}
