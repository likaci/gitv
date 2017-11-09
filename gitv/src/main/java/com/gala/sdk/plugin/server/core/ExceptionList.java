package com.gala.sdk.plugin.server.core;

import java.util.ArrayList;
import java.util.List;

class ExceptionList extends Throwable {
    private static final long serialVersionUID = -5812311465685315224L;
    private final List<Throwable> mList = new ArrayList();

    public ExceptionList(List<Throwable> list) {
        this.mList.addAll(list);
    }

    public void addException(Throwable e) {
        this.mList.add(e);
    }

    public void addExceptions(List<Throwable> list) {
        this.mList.addAll(list);
    }

    public List<Throwable> getExceptionList() {
        return new ArrayList(this.mList);
    }
}
