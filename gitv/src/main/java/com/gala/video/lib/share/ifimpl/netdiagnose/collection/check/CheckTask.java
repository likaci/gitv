package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import android.content.Context;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public abstract class CheckTask {
    protected CheckEntity mCheckEntity;

    protected abstract boolean runCheck();

    public CheckTask(CheckEntity checkEntity) {
        this.mCheckEntity = checkEntity;
    }

    public Context getContext() {
        return AppRuntimeEnv.get().getApplicationContext();
    }
}
