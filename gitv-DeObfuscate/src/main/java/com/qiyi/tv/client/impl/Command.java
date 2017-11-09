package com.qiyi.tv.client.impl;

import android.content.Context;
import android.os.Bundle;

public abstract class Command {
    private final int f1922a;
    private Context f1923a;
    private final int f1924b;
    private final int f1925c;

    public abstract Bundle process(Bundle bundle);

    public Command(Context context, int target, int operation, int dataType) {
        this.f1923a = context;
        this.f1922a = target;
        this.f1924b = operation;
        this.f1925c = dataType;
    }

    public void setContext(Context context) {
        this.f1923a = context;
    }

    public Context getContext() {
        return this.f1923a;
    }

    public int getTarget() {
        return this.f1922a;
    }

    public int getOperationType() {
        return this.f1924b;
    }

    public int getDataType() {
        return this.f1925c;
    }
}
